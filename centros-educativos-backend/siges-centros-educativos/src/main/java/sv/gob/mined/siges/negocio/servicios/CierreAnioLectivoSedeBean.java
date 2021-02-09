/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.negocio.servicios;

import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.opentracing.Traced;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgResumenCierreSedeRequest;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCierreAnioLectivoSede;
import sv.gob.mined.siges.filtros.FiltroRelPreguntaCierreAnioSede;
import sv.gob.mined.siges.filtros.FiltroResumenCierreSeccion;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.SgCierreAnioLectivoSedeValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.CierreAnioLectivoSedeDAO;
import sv.gob.mined.siges.persistencia.daos.RelPreguntaCierreAnioSedeDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivo;
import sv.gob.mined.siges.persistencia.entidades.SgCierreAnioLectivoSede;
import sv.gob.mined.siges.persistencia.entidades.SgResumenCierreSeccion;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgConfiguracionFirmaElectronica;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;
import sv.gob.mined.siges.wsclient.ResultadoValidacion;

/**
 *
 * @author bruno
 */
@Stateless
@Traced
public class CierreAnioLectivoSedeBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private SeccionBean seccionBean;

    @Inject
    private ResumenCierreSeccionBean resumenCierreSeccionBean;

    @Inject
    private FirmaElectronicaBean firmaElectronicaBean;

    @Inject
    private ConfiguracionFirmaElectronicaBean configFirmaElectronicaBean;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgCierreAnioLectivoSede
     * @return SgCierreAnioLectivoSede
     * @throws GeneralException
     */
    @Interceptors({AuditInterceptor.class})
    public SgCierreAnioLectivoSede guardar(SgCierreAnioLectivoSede entity) throws GeneralException {
        try {
            if (entity != null) {
                if (SgCierreAnioLectivoSedeValidacionNegocio.validar(entity)) {
                    if (entity.getCalPk() != null) {
                        //No actualizar cierres
                        return entity;
                    }
                    //Se guarda el cierre del año        
                    CodigueraDAO<SgCierreAnioLectivoSede> codDAO = new CodigueraDAO<>(em, SgCierreAnioLectivoSede.class);
                    SgCierreAnioLectivoSede c = codDAO.guardar(entity, ConstantesOperaciones.CREAR_CIERRE_ANIO_LECTIVO_SEDE);
                    return c;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return entity;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param cma SgCierreAnioLectivoSede
     * @return SgCierreAnioLectivoSede
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCierreAnioLectivoSede preconfirmar(SgCierreAnioLectivoSede cierre) throws GeneralException {
        try {
            if (cierre != null && cierre.getCalPk() != null) {
                CloseableHttpClient httpclient = null;
                CloseableHttpResponse response = null;
                int timeout = 2 * 120 * 1000;

                SgCierreAnioLectivoSede cierreBd = em.find(SgCierreAnioLectivoSede.class, cierre.getCalPk());

                SgConfiguracionFirmaElectronica conf = configFirmaElectronicaBean.obtenerPorCodigo(Constantes.CONFIG_FIRMA_ELECTRONICA_CIERRE_ANIO);

                if (conf != null && BooleanUtils.isTrue(conf.getConActivada())) {

                    CodigueraDAO<SgCierreAnioLectivoSede> codDAO = new CodigueraDAO<>(em, SgCierreAnioLectivoSede.class);
                    cierreBd.setCalFirmado(Boolean.FALSE);

                    SSLContextBuilder builder = new SSLContextBuilder();

                    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                            builder.build(), SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                    RequestConfig config = RequestConfig.custom()
                            .setConnectTimeout(timeout)
                            .setConnectionRequestTimeout(timeout)
                            .setSocketTimeout(timeout).build();

                    httpclient = HttpClients.custom().setDefaultRequestConfig(config).setSSLSocketFactory(
                            sslsf).build();

                    URIBuilder ubuilder = new URIBuilder(System.getProperty("service.report-generator.baseUrl") + "/ma/cierreAnio");
                    ubuilder.setParameter("cierreId", cierreBd.getCalPk().toString());
                    HttpGet get = new HttpGet(ubuilder.build());
                    get.addHeader(org.apache.http.HttpHeaders.AUTHORIZATION, "Bearer " + Lookup.obtenerJWT().getRawToken());

                    response = httpclient.execute(get);

                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        InputStream is = response.getEntity().getContent();
                        byte[] byteArray = IOUtils.toByteArray(is);

                        List<byte[]> send = new ArrayList<>();
                        send.add(byteArray);

                        String usuarioEnviaYFirma = Lookup.obtenerJWT().getSubject();

                        List<ResultadoValidacion> res = firmaElectronicaBean.enviarDocumentosParaFirmar(send, cierre.getCalTransactionReturnUrl(), "cierre_año.pdf", usuarioEnviaYFirma, usuarioEnviaYFirma);

                        if (res == null || res.isEmpty() || !res.get(0).getCode().equalsIgnoreCase("OK")) {
                            BusinessException be = new BusinessException();
                            be.addError("ERROR_AL_ENVIAR_DOCUMENTO");
                            throw be;
                        }

                        cierreBd.setCalFirmaTransactionId(res.get(0).getMessage());
                        cierreBd = codDAO.guardar(cierreBd, ConstantesOperaciones.CREAR_CIERRE_ANIO_LECTIVO_SEDE);

                        cierreBd.setCalTransactionSignatureUrl(System.getProperty("service.firmar-documentos.baseUrl") + "?id_transaccion=" + res.get(0).getMessage());
                        return cierreBd;
                    } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                        BusinessException be = new BusinessException();
                        be.addError(Errores.ERROR_SEGURIDAD);
                        throw be;
                    } else {
                        throw new TechnicalException("ERROR_AL_GENERAR_PFD");
                    }

                } else {

                    //Firma deshabilitada. Se confirma de forma inmediata
                    CodigueraDAO<SgCierreAnioLectivoSede> codDAO = new CodigueraDAO<>(em, SgCierreAnioLectivoSede.class);
                    cierreBd.setCalFechaCierre(LocalDateTime.now());
                    cierreBd.setCalUsuarioCierre(Lookup.obtenerJWT().getSubject());
                    cierreBd.setCalFirmado(Boolean.FALSE);
                    return codDAO.guardar(cierreBd, ConstantesOperaciones.CREAR_CIERRE_ANIO_LECTIVO_SEDE);

                }

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param firmaTransactionId String
     * @return SgConfirmacionPromocion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgCierreAnioLectivoSede confirmar(String firmaTransactionId) throws GeneralException {
        try {
            if (firmaTransactionId != null) {

                byte[] doc = firmaElectronicaBean.obtenerDocumentoFirmado(firmaTransactionId, "cierre_año.pdf");

                FiltroCierreAnioLectivoSede fil = new FiltroCierreAnioLectivoSede();
                fil.setFirmaTransactionId(firmaTransactionId);
                fil.setFirmado(Boolean.FALSE);
                List<SgCierreAnioLectivoSede> cierres = this.obtenerPorFiltro(fil);

                if (!cierres.isEmpty()) {

                    SgCierreAnioLectivoSede cierre = cierres.get(0);
                    cierre.setCalFirmado(Boolean.TRUE);
                    cierre.setCalFechaCierre(LocalDateTime.now());
                    cierre.setCalUsuarioCierre(Lookup.obtenerJWT().getSubject());

                    Path folder = Paths.get(tmpBasePath);
                    Path tmpFile = Files.createTempFile(folder, null, ".pdf");
                    Files.write(tmpFile, doc);

                    SgArchivo ar = new SgArchivo();
                    ar.setAchTmpPath(tmpFile.getFileName().toString());
                    ar.setAchExt("pdf");
                    ar.setAchContentType("application/pdf");
                    ar.setAchNombre("cierre_año_" + cierre.getCalSede().getSedCodigo() + "_" + cierre.getCalAnioLectivo().getAleAnio());
                    cierre.setCalArchivoFirmado(ar);

                    CodigueraDAO<SgCierreAnioLectivoSede> codDAO = new CodigueraDAO<>(em, SgCierreAnioLectivoSede.class);
                    return codDAO.guardar(cierre, ConstantesOperaciones.CREAR_CIERRE_ANIO_LECTIVO_SEDE);

                } else {
                    BusinessException be = new BusinessException();
                    be.addError(Errores.ERROR_NO_EXISTE_CIERRE_ANIO_SIN_FIRMAR_PARA_DATOS_INGRESADOS);
                    throw be;
                }

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return null;
    }

    /**
     * Devuelve si el objeto existe
     *
     * @param id Long
     * @return Boolean
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCierreAnioLectivoSede> codDAO = new CodigueraDAO<>(em, SgCierreAnioLectivoSede.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_CIERRE_ANIO_LECTIVO_SEDE);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Elimina el objeto con la id indicada
     *
     * @param id Long
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgCierreAnioLectivoSede> codDAO = new CodigueraDAO<>(em, SgCierreAnioLectivoSede.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_CIERRE_ANIO_LECTIVO_SEDE);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroCierreAnioLectivoSede filtro) throws GeneralException {
        try {
            CierreAnioLectivoSedeDAO DAO = new CierreAnioLectivoSedeDAO(em, seguridadBean);
            return DAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CIERRE_ANIO_LECTIVO_SEDE);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgServicioEducativo
     * @throws GeneralException
     */
    public List<SgCierreAnioLectivoSede> obtenerPorFiltro(FiltroCierreAnioLectivoSede filtro) throws GeneralException {
        try {
            CierreAnioLectivoSedeDAO DAO = new CierreAnioLectivoSedeDAO(em, seguridadBean);
            List<SgCierreAnioLectivoSede> ca = DAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_CIERRE_ANIO_LECTIVO_SEDE);
            return ca;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public SgCierreAnioLectivoSede obtenerCierreAnioLectivoSede(SgResumenCierreSedeRequest req) throws GeneralException {
        if (req != null) {
            try {
                if ((req.getAnioLectivoPk() != null && req.getSedePk() != null) || req.getCierreAnioPk() != null) {

                    //Verificar si ya existe un cierre calculado
                    FiltroCierreAnioLectivoSede filtro = new FiltroCierreAnioLectivoSede();
                    filtro.setIncluirCampos(new String[]{
                        "calAnioLectivo.aleAnio",
                        "calAnioLectivo.alePk",
                        "calAnioLectivo.aleVersion",
                        "calSede.sedTipo",
                        "calSede.sedPk",
                        "calSede.sedCodigo",
                        "calSede.sedNombre",
                        "calVersion",
                        "calPromocionesPendientesMasc",
                        "calPromocionesPendientesFem",
                        "calPromovidosMasc",
                        "calPromovidosFem",
                        "calNoPromovidosMasc",
                        "calNoPromovidosFem",
                        "calAsistencias",
                        "calInasistencias",
                        "calInasistenciasJust",
                        "calSolicitudesTitulosMasc",
                        "calSolicitudesTitulosFem",
                        "calUsuarioCierre",
                        "calFechaCierre",
                        "calArchivoFirmado.achPk",
                        "calArchivoFirmado.achContentType",
                        "calArchivoFirmado.achNombre",
                        "calArchivoFirmado.achExt",
                        "calFirmado",
                        "calFirmaTransactionId",});

                    filtro.setSedeId(req.getSedePk());
                    filtro.setAnioLectivoId(req.getAnioLectivoPk());
                    filtro.setCierreAnioPk(req.getCierreAnioPk());
                    List<SgCierreAnioLectivoSede> cierre = this.obtenerPorFiltro(filtro);
                    if (!cierre.isEmpty()) {

                        SgCierreAnioLectivoSede c = cierre.get(0);

                        FiltroResumenCierreSeccion fil = new FiltroResumenCierreSeccion();
                        fil.setCierreAnioSedePk(c.getCalPk());
                        fil.setIncluirCampos(new String[]{
                            "rcsSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                            "rcsSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                            "rcsSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                            "rcsSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                            "rcsSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                            "rcsSeccion.secServicioEducativo.sduOpcion.opcNombre",
                            "rcsSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
                            "rcsSeccion.secServicioEducativo.sduGrado.graNombre",
                            "rcsSeccion.secNombre",
                            "rcsSeccion.secCodigo",
                            "rcsSeccion.secEstado",
                            "rcsSeccion.secJornadaLaboral.jlaNombre",
                            "rcsSeccion.secPk",
                            "rcsSeccion.secVersion",
                            "rcsPromocionesPendientesMasc",
                            "rcsPromocionesPendientesFem",
                            "rcsPromovidosMasc",
                            "rcsPromovidosFem",
                            "rcsNoPromovidosMasc",
                            "rcsNoPromovidosFem",
                            "rcsAsistencias",
                            "rcsInasistencias",
                            "rcsInasistenciasJust",
                            "rcsSolicitudesTitulosMasc",
                            "rcsSolicitudesTitulosFem"
                        });
                        c.setCalResumenSecciones(resumenCierreSeccionBean.obtenerPorFiltro(fil));

                        RelPreguntaCierreAnioSedeDAO relDao = new RelPreguntaCierreAnioSedeDAO(em);
                        FiltroRelPreguntaCierreAnioSede filp = new FiltroRelPreguntaCierreAnioSede();
                        filp.setCierreAnioSedePk(c.getCalPk());
                        filp.setIncluirCampos(new String[]{"rpcPreguntaValidada", "rpcPreguntaCierreAnio.pcaPregunta"});
                        c.setCalRelPreguntaCierreAnio(relDao.obtenerPorFiltro(filp));

                        return c;
                    }

                    if (req.getCierreAnioPk() != null) {
                        //Estamos buscando por pk y no lo encontramos
                        return null;
                    }

                    if (req.getSedePk() != null && req.getAnioLectivoPk() != null) {

                        //En caso contrario se crea
                        SgCierreAnioLectivoSede res = new SgCierreAnioLectivoSede();

                        List<SgResumenCierreSeccion> resSec = new ArrayList<>();
                        HashMap<Long, SgResumenCierreSeccion> resSecHash = new HashMap<>();

                        FiltroSeccion filtroSeccion = new FiltroSeccion();
                        filtroSeccion.setSecSedeFk(req.getSedePk());
                        filtroSeccion.setSecAnioLectivoFk(req.getAnioLectivoPk());
                        filtroSeccion.setIncluirCampos(new String[]{
                            "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                            "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                            "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                            "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                            "secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                            "secServicioEducativo.sduOpcion.opcNombre",
                            "secServicioEducativo.sduProgramaEducativo.pedNombre",
                            "secServicioEducativo.sduGrado.graNombre",
                            "secNombre",
                            "secCodigo",
                            "secEstado",
                            "secJornadaLaboral.jlaNombre",
                            "secPk",
                            "secVersion"});
                        filtroSeccion.setOrderBy(new String[]{"secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrden",
                            "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicOrden",
                            "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modOrden",
                            "secServicioEducativo.sduGrado.graOrden",
                            "secNombre"});
                        filtroSeccion.setAscending(new boolean[]{true, true, true, true, true});
                        List<SgSeccion> sec = seccionBean.obtenerPorFiltro(filtroSeccion);

                        for (SgSeccion s : sec) {
                            SgResumenCierreSeccion rcs = new SgResumenCierreSeccion();
                            rcs.setRcsSeccion(s);
                            resSec.add(rcs);
                            resSecHash.put(s.getSecPk(), rcs);
                        }

                        //Asistencias
                        Query qasistencias = em.createNativeQuery("SELECT sec.sec_pk,"
                                + " Sum(cac_estudiantes_presentes) asistencias,"
                                + "	Sum(cac_estudiantes_ausentes_justificados + cac_estudiantes_ausentes_sin_justificar) inasistencias,"
                                + " Sum(cac_estudiantes_ausentes_justificados) inasistencias_justificadas"
                                + " FROM   centros_educativos.sg_control_asistencia_cabezal cac"
                                + " INNER JOIN centros_educativos.sg_secciones sec ON ( cac.cac_seccion_fk = sec.sec_pk )"
                                + " INNER JOIN centros_educativos.sg_servicio_educativo sdu ON ( sec.sec_servicio_educativo_fk = sdu.sdu_pk )"
                                + " WHERE  sec.sec_anio_lectivo_fk = :anioLectivoPk AND sdu.sdu_sede_fk = :sedePk"
                                + " GROUP  BY sec.sec_pk")
                                .setParameter("anioLectivoPk", req.getAnioLectivoPk())
                                .setParameter("sedePk", req.getSedePk());

                        List<Object[]> resqasistencias = qasistencias.getResultList();

                        for (Object[] o : resqasistencias) {

                            Long secPk = ((BigInteger) o[0]).longValue();
                            Integer asistencias = ((BigInteger) o[1]).intValue();
                            Integer inasistencias = ((BigInteger) o[2]).intValue();
                            Integer inasistenciasJust = ((BigInteger) o[3]).intValue();

                            if (resSecHash.containsKey(secPk)) {
                                SgResumenCierreSeccion rcs = resSecHash.get(secPk);
                                rcs.setRcsAsistencias(asistencias);
                                rcs.setRcsInasistencias(inasistencias);
                                rcs.setRcsInasistenciasJust(inasistenciasJust);
                            }
                        }

                        //Promociones
                        Query qpromociones = em.createNativeQuery("select sec.sec_pk, p.per_sexo_fk, "
                                + "     SUM(CASE WHEN mat.mat_promocion_grado = 'PROMOVIDO' THEN 1 ELSE 0 END) promovidos, "
                                + "     SUM(CASE WHEN mat.mat_promocion_grado is null THEN 1 ELSE 0 END) pendientes, "
                                + "     SUM(CASE WHEN mat.mat_promocion_grado = 'NO_PROMOVIDO' THEN 1 ELSE 0 END) nopromovidos "
                                + "   from centros_educativos.sg_matriculas mat "
                                + "     INNER JOIN centros_educativos.sg_estudiantes e ON (mat.mat_estudiante_fk = e.est_pk) "
                                + "     INNER JOIN centros_educativos.sg_personas p ON (e.est_persona = p.per_pk) "
                                + "     INNER JOIN centros_educativos.sg_secciones sec ON (mat.mat_seccion_fk = sec.sec_pk) "
                                + "     INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk) "
                                + "   where sec.sec_anio_lectivo_fk = :anioLectivoPk and sdu.sdu_sede_fk = :sedePk and p.per_sexo_fk IN (1, 2) "
                                + "   and mat.mat_anulada = false and mat.mat_retirada = false "
                                + "   group by sec.sec_pk, p.per_sexo_fk")                                
                                .setParameter("anioLectivoPk", req.getAnioLectivoPk())
                                .setParameter("sedePk", req.getSedePk());

                        List<Object[]> resqpromociones = qpromociones.getResultList();

                        for (Object[] o : resqpromociones) {

                            Long secPk = ((BigInteger) o[0]).longValue();
                            Long sexoPk = ((BigInteger) o[1]).longValue();
                            Integer promovidos = ((BigInteger) o[2]).intValue();
                            Integer pendientes = ((BigInteger) o[3]).intValue();
                            Integer nopromovidos = ((BigInteger) o[4]).intValue();

                            if (resSecHash.containsKey(secPk)) {
                                SgResumenCierreSeccion rcs = resSecHash.get(secPk);
                                if (Constantes.SEXO_MASCULINO_PK.equals(sexoPk)) {
                                    rcs.setRcsPromovidosMasc(promovidos);
                                    rcs.setRcsPromocionesPendientesMasc(pendientes);
                                    rcs.setRcsNoPromovidosMasc(nopromovidos);
                                } else if (Constantes.SEXO_FEMENINO_PK.equals(sexoPk)) {
                                    rcs.setRcsPromovidosFem(promovidos);
                                    rcs.setRcsPromocionesPendientesFem(pendientes);
                                    rcs.setRcsNoPromovidosFem(nopromovidos);
                                }
                            }
                        }

                        //Solicitudes imp
                        Query qsolicitudesimp = em.createNativeQuery("select sec.sec_pk, p.per_sexo_fk, "
                                + "	count(*) as cantidad"
                                + "	from centros_educativos.sg_estudiante_impresion imp"
                                + "	INNER JOIN centros_educativos.sg_solicitudes_impresion sol ON (imp.eim_solicitud_impresion_fk = sol.sim_pk)"
                                + "	INNER JOIN centros_educativos.sg_estudiantes e ON (imp.eim_estudiante_fk = e.est_pk)"
                                + "	INNER JOIN centros_educativos.sg_personas p ON (e.est_persona = p.per_pk)"
                                + " INNER JOIN centros_educativos.sg_secciones sec ON (sol.sim_seccion_fk = sec.sec_pk)"
                                + " INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk)"
                                + "	where sec.sec_anio_lectivo_fk = :anioLectivoPk and sdu.sdu_sede_fk = :sedePk and p.per_sexo_fk IN (1, 2)"
                                + "	and sim_estado IN ( 'ENVIADA', 'CONFIRMADA', 'CON_EXCEPCIONES', 'PENDIENTE_IMPRESION', 'IMPRESA')"
                                + "	group by sec.sec_pk, p.per_sexo_fk")
                                .setParameter("anioLectivoPk", req.getAnioLectivoPk())
                                .setParameter("sedePk", req.getSedePk());

                        List<Object[]> resqsolicitudesimp = qsolicitudesimp.getResultList();

                        for (Object[] o : resqsolicitudesimp) {

                            Long secPk = ((BigInteger) o[0]).longValue();
                            Long sexoPk = ((BigInteger) o[1]).longValue();
                            Integer solicitudes = ((BigInteger) o[2]).intValue();

                            if (resSecHash.containsKey(secPk)) {
                                SgResumenCierreSeccion rcs = resSecHash.get(secPk);
                                if (Constantes.SEXO_MASCULINO_PK.equals(sexoPk)) {
                                    rcs.setRcsSolicitudesTitulosMasc(solicitudes);
                                } else if (Constantes.SEXO_FEMENINO_PK.equals(sexoPk)) {
                                    rcs.setRcsSolicitudesTitulosFem(solicitudes);
                                }
                            }
                        }

                        res.setCalResumenSecciones(resSec);
                        res.inferirDatosDeSecciones();
                        return res;

                    }

                }
            } catch (NoResultException nre) {
                return null;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }
}
