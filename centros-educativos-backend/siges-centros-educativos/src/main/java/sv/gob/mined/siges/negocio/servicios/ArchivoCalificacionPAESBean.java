/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import com.sofis.persistence.dao.exceptions.DAOGeneralException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import org.eclipse.microprofile.opentracing.Traced;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroArchivoCalificacionPAES;
import sv.gob.mined.siges.negocio.validaciones.ArchivoCalificacionPAESValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.ArchivoCalificacionPAESDAO;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.entidades.SgArchivoCalificacionPAES;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.ejb3.annotation.TransactionTimeout;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadoArchivoCalificacionPAES;
import sv.gob.mined.siges.enumerados.EnumResolucionCalificacion;
import sv.gob.mined.siges.enumerados.EnumTipoPruebaPaes;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.filtros.FiltroCalificacion;
import sv.gob.mined.siges.filtros.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.filtros.FiltroComponentePlanEstudio;
import sv.gob.mined.siges.filtros.FiltroComponentePlanGrado;
import sv.gob.mined.siges.filtros.FiltroEstructuraCalificacionPaes;
import sv.gob.mined.siges.filtros.catalogo.FiltroCalificacionCa;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.daos.CalificacionDAO;
import sv.gob.mined.siges.persistencia.daos.EstructuraCalificacionPaesDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCE;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanEstudio;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado;
import sv.gob.mined.siges.persistencia.entidades.SgEstructuraCalificacionPaes;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCalificacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

@Stateless
@Traced
public class ArchivoCalificacionPAESBean {

    private static final Logger LOGGER = Logger.getLogger(ArchivoCalificacionPAESBean.class.getName());

    @Inject
    @ConfigProperty(name = "files.media.path")
    private String basePath;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpMediaPath;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private EstudianteBean estudianteBean;

    @Inject
    private ComponentePlanGradoBean componentePlanGradoBean;

    @Inject
    private CalificacionBean calificacionBean;

    @Inject
    private CalificacionCatalogosBean calificacionCatalogosBean;

    @Inject
    private CalificacionEstudianteBean calificacionEstudianteBean;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private ComponentesPlanEstudioBean componentePlanEstudioBean;

    static DataFormatter dataFormatter = new DataFormatter(new Locale("es-SV"));

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgArchivoCalificacionPAES
     * @throws GeneralException
     */
    public SgArchivoCalificacionPAES obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgArchivoCalificacionPAES> codDAO = new CodigueraDAO<>(em, SgArchivoCalificacionPAES.class);
                return codDAO.obtenerPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
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
                CodigueraDAO<SgArchivoCalificacionPAES> codDAO = new CodigueraDAO<>(em, SgArchivoCalificacionPAES.class);
                return codDAO.objetoExistePorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param entity SgArchivoCalificacionPAES
     * @return SgArchivoCalificacionPAES
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    @TransactionTimeout(value = 3, unit = TimeUnit.HOURS)
    public SgArchivoCalificacionPAES guardar(SgArchivoCalificacionPAES entity) throws GeneralException {
        InputStream stream = null;
        try {
            LOGGER.log(Level.INFO, "Iniciando guardar de PAES");
            if (entity != null) {
                //Validar el elemento a guardar. Si no valida, se lanza una excepcion
                LOGGER.log(Level.INFO, "Iniciando validacion guardar de PAES");
                if (ArchivoCalificacionPAESValidacionNegocio.validar(entity)) {
                    LOGGER.log(Level.INFO, "Ha validado");
                    CodigueraDAO<SgArchivoCalificacionPAES> codDAO = new CodigueraDAO(em, SgArchivoCalificacionPAES.class);
                    stream = new BufferedInputStream(new FileInputStream(tmpMediaPath + entity.getAcpArchivo().getAchTmpPath()));
                    SgArchivoCalificacionPAES arch = codDAO.guardar(entity, null);
                    //       em.flush();
                    LOGGER.log(Level.INFO, "Iniciando guardar de los registros del Excel PAES");
                    guardarRegistrosDeExcel(entity, stream);
                    LOGGER.log(Level.INFO, "Finalizaa guardar de PAES");
                    return arch;
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ex) {
                }
            }
        }

        return entity;
    }

    public Boolean guardarRegistrosDeExcel(SgArchivoCalificacionPAES entity, InputStream stream) throws Exception {
        Workbook workbook = null;
        try {
            if (entity.getAcpArchivo() != null) {

                JsonWebToken jwt = Lookup.obtenerJWT();

                String Usuario = jwt.getSubject();

                workbook = WorkbookFactory.create(stream);

                Boolean puedeProcesarPaes = jwt.getGroups().contains(ConstantesOperaciones.HABILITADO_CALIFICACIONES_PAES);
                Boolean puedeProcesarExternas = jwt.getGroups().contains(ConstantesOperaciones.HABILITADO_CALIFICACIONES_EXTERNAS);

                HashMap<Integer, SgComponentePlanEstudio> componentes = new HashMap<>();

                workbook.forEach(sheet -> {

                    Iterator<Row> rowIterator = sheet.rowIterator();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();

                        if (row.getRowNum() > 0) {

                            String codigoComponente = dataFormatter.formatCellValue(row.getCell(6));

                            if (!StringUtils.isBlank(codigoComponente)) {

                                SgComponentePlanEstudio componente = null;
                                Integer codigoInt = null;
                                try {
                                    codigoInt = Integer.parseInt(codigoComponente);
                                } catch (NumberFormatException ex) {
                                    //Luego se muestra el error al procesar estructura
                                    continue;
                                }

                                if (componentes.containsKey(codigoInt)) {
                                    componente = componentes.get(codigoInt);
                                } else {
                                    FiltroComponentePlanEstudio fcpe = new FiltroComponentePlanEstudio();
                                    fcpe.setCpeCodigoExterno(codigoInt);
                                    fcpe.setMaxResults(1L);
                                    fcpe.setCpeEsPaes(Boolean.TRUE);
                                    fcpe.setIncluirCampos(new String[]{"cpePk", "cpeTipo", "cpeVersion", "cpeTipoPaes"});
                                    List<SgComponentePlanEstudio> componentePAESList = componentePlanEstudioBean.obtenerPorFiltro(fcpe);
                                    if (!componentePAESList.isEmpty()) {
                                        componente = componentePAESList.get(0);
                                        componentes.put(codigoInt, componente);
                                    }
                                }

                                if (componente != null) {

                                    if (EnumTipoPruebaPaes.EXTERNA.equals(componente.getCpeTipoPaes())) {

                                        if (!puedeProcesarExternas) {
                                            BusinessException be = new BusinessException();
                                            be.addError(Errores.ERROR_PERMISOS_INSUFICIENTES_CALIFICACIONES_EXTERNAS);
                                            throw be;

                                        }

                                    } else if (EnumTipoPruebaPaes.PAES.equals(componente.getCpeTipoPaes())) {

                                        if (!puedeProcesarPaes) {
                                            BusinessException be = new BusinessException();
                                            be.addError(Errores.ERROR_PERMISOS_INSUFICIENTES_CALIFICACIONES_PAES);
                                            throw be;

                                        }

                                    } else {

                                        BusinessException be = new BusinessException();
                                        be.addError(Errores.ERROR_TIPO_PAES_VACIO);
                                        throw be;

                                    }
                                }
                            }
                        }
                    }

                });

                Query q = em.createNativeQuery("insert into centros_educativos.sg_estructura_calificacion_paes(ecp_nie,ecp_calificacion,ecp_codigo_cpe,ecp_archivo_calificacion_paes_fk,ecp_estado,ecp_version,ecp_ult_mod_fecha,ecp_ult_mod_usuario) "
                        + "values(:nie,:cal,:cpe,:archvioPaesFk,'SIN_PROCESAR',0,:ultFecha,:ultUsuario)");

                workbook.forEach(sheet -> {

                    Iterator<Row> rowIterator = sheet.rowIterator();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        if (row.getRowNum() > 0) {
                            procesar(row, q, entity.getAcpPk(), Usuario);
                        }
                    }

                }
                );

            }

            return Boolean.TRUE;

        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    public void procesar(Row x, Query q, Long archivoPk, String usuario) {

        String nie = dataFormatter.formatCellValue(x.getCell(3));
        String calificacion = dataFormatter.formatCellValue(x.getCell(4));
        String codigoComponente = dataFormatter.formatCellValue(x.getCell(6));
        if (!StringUtils.isBlank(nie) && !StringUtils.isBlank(calificacion) && !StringUtils.isBlank(codigoComponente)) {
            q.setParameter("nie", nie);
            q.setParameter("cal", calificacion);
            q.setParameter("cpe", codigoComponente);
            q.setParameter("archvioPaesFk", archivoPk);
            q.setParameter("ultFecha", LocalDateTime.now());
            q.setParameter("ultUsuario", usuario);
            q.executeUpdate();
        }

        if (x.getRowNum() % 100 == 0) {
            em.flush();
            em.clear();
        }

    }

    @TransactionTimeout(value = 30, unit = TimeUnit.HOURS)
    public Boolean ejecutarEstructuraCalificacionPAES(Long cantidadBusqueda, Long inicioPk, Long finPk, String codigoExterno) throws DAOGeneralException, Exception {
        LOGGER.log(Level.WARNING, "COMIENZO EJECUCION ESTRUCTURA PAES. Cant busqueda: {0} Inicio: {1} Fin: {2} Codexterno: {3}", new Object[]{cantidadBusqueda, inicioPk, finPk, codigoExterno});
        FiltroEstructuraCalificacionPaes fecp = new FiltroEstructuraCalificacionPaes();
        fecp.setEstadoPaes(EnumEstadoArchivoCalificacionPAES.SIN_PROCESAR);
        fecp.setDesdePk(inicioPk);
        fecp.setHastaPk(finPk);
        fecp.setMaxResults(cantidadBusqueda);
        fecp.setCodigoExterno(codigoExterno);
        fecp.setOrderBy(new String[]{"ecpPk"});
        fecp.setAscending(new boolean[]{true});
        fecp.setIncluirCampos(new String[]{"ecpNie", "ecpEstado", "ecpCalificacion", "ecpCodigoCpe", "ecpVersion",
            "ecpArchivoCalificacionPaesFk.acpPk", "ecpArchivoCalificacionPaesFk.acpEstadoArchivo", "ecpArchivoCalificacionPaesFk.acpResultado",
            "ecpArchivoCalificacionPaesFk.acpVersion", "ecpArchivoCalificacionPaesFk.acpArchivo.achPk", "ecpArchivoCalificacionPaesFk.acpArchivo.achVersion"});
        EstructuraCalificacionPaesDAO codDAO = new EstructuraCalificacionPaesDAO(em);
        List<SgEstructuraCalificacionPaes> listEstructura = codDAO.obtenerPorFiltro(fecp);

        String usuario = Lookup.obtenerJWT().getSubject();

        LOGGER.log(Level.WARNING, " Cantidad a ejecutar: " + listEstructura.size());

        Query a = em.createNativeQuery("select nextval ('hibernate_sequence')");
        Long seq = ((BigInteger) a.getSingleResult()).longValue();

        Query est = em.createNativeQuery("INSERT INTO revinfo (rev, revtstmp, usuario) values(:seq, extract(epoch from now()), :usuario)");
        est.setParameter("usuario", usuario != null ? usuario : "cron");
        est.setParameter("seq", seq);
        est.executeUpdate();

        Integer i = 0;
        if (!listEstructura.isEmpty()) {
            for (SgEstructuraCalificacionPaes estCalPaes : listEstructura) {
                i++;
                LOGGER.log(Level.WARNING, "Iter: {0}. Ejecutando linea {1}", new Object[]{i, estCalPaes.getEcpPk()});
                SgEstructuraCalificacionPaes estructura = calificarEstudiantePAES(estCalPaes.getEcpNie(), estCalPaes.getEcpCalificacion(), estCalPaes.getEcpCodigoCpe());
                if (estructura.getEcpEstado().equals(EnumEstadoArchivoCalificacionPAES.PROCESADO)) {

                    est = em.createNativeQuery("update  centros_educativos.sg_estructura_calificacion_paes set ecp_estado='PROCESADO',ecp_resultado=null,ecp_persona_fk=:perPk where ecp_pk=:ecpPk");
                    est.setParameter("ecpPk", estCalPaes.getEcpPk());
                    est.setParameter("perPk", estructura.getEcpPersonaFk().getPerPk());
                    est.executeUpdate();
                } else {

                    est = em.createNativeQuery("update  centros_educativos.sg_estructura_calificacion_paes set ecp_estado='PROCESADO_ERROR',ecp_resultado=:resultado where ecp_pk=:ecpPk");
                    est.setParameter("ecpPk", estCalPaes.getEcpPk());
                    est.setParameter("resultado", estructura.getEcpResultado());
                    est.executeUpdate();
                }

                Query rev = em.createNativeQuery("INSERT INTO centros_educativos.sg_estructura_calificacion_paes_aud\n"
                        + "(ecp_pk, ecp_nie, ecp_calificacion, ecp_codigo_cpe, ecp_ult_mod_fecha, ecp_ult_mod_usuario, ecp_version, rev, revtype)\n"
                        + "VALUES(:ecp_pk, :ecpNie, :ecpCalificacion, :ecpCodigo,  :ultimaFecha, :ultUsu, :version, :rev, :revtype)");
                rev.setParameter("ecp_pk", estCalPaes.getEcpPk());
                rev.setParameter("ecpNie", estCalPaes.getEcpNie());
                rev.setParameter("ecpCalificacion", estCalPaes.getEcpCalificacion());
                rev.setParameter("ecpCodigo", estCalPaes.getEcpCodigoCpe());
                rev.setParameter("ultimaFecha", LocalDateTime.now());
                rev.setParameter("ultUsu", usuario != null ? usuario : "cron");
                rev.setParameter("version", estCalPaes.getEcpVersion() + 1);
                rev.setParameter("rev", seq);
                rev.setParameter("revtype", 1);
                rev.executeUpdate();

                if (i % 50 == 0) {
                    em.clear();
                }
            }
        }

        FiltroArchivoCalificacionPAES facp = new FiltroArchivoCalificacionPAES();
        facp.setAcpEstado(EnumEstadoArchivoCalificacionPAES.SIN_PROCESAR);
        facp.setIncluirCampos(new String[]{"acpArchivo.achPk", "acpArchivo.achNombre", "acpArchivo.achDescripcion", "acpArchivo.achTmpPath", "acpArchivo.achContentType", "acpArchivo.achExt", "acpArchivo.achVersion", "acpEstadoArchivo", "acpResultado", "acpVersion", "acpAnio"});
        List<SgArchivoCalificacionPAES> archivosPaesList = obtenerPorFiltro(facp);

        CodigueraDAO<SgArchivoCalificacionPAES> codDAOArchivoPaes = new CodigueraDAO(em, SgArchivoCalificacionPAES.class);

        for (SgArchivoCalificacionPAES archivoPaes : archivosPaesList) {
            FiltroEstructuraCalificacionPaes fesp = new FiltroEstructuraCalificacionPaes();
            fesp.setEstadoPaes(EnumEstadoArchivoCalificacionPAES.SIN_PROCESAR);
            fesp.setMaxResults(1L);
            fesp.setEstArchivoCalificacionPAES(archivoPaes.getAcpPk());
            Long cantidad = codDAO.cantidadTotal(fesp);

            if (cantidad.compareTo(0L) == 0) {
                fesp.setEstadoPaes(EnumEstadoArchivoCalificacionPAES.PROCESADO_ERROR);
                fesp.setMaxResults(1L);
                fesp.setEstArchivoCalificacionPAES(archivoPaes.getAcpPk());
                Long cantidad_error = codDAO.cantidadTotal(fesp);

                if (cantidad_error.compareTo(0L) != 0) {

                    Query errores = em.createNativeQuery("select array_to_string(array_agg(ecp_resultado),'<br/> ') from centros_educativos.sg_estructura_calificacion_paes where ecp_estado='PROCESADO_ERROR' and ecp_archivo_calificacion_paes_fk=:accpk");
                    errores.setParameter("accpk", archivoPaes.getAcpPk());
                    List res = errores.getResultList();

                    archivoPaes.setAcpResultado(((String) res.get(0)));
                    archivoPaes.setAcpEstadoArchivo(EnumEstadoArchivoCalificacionPAES.PROCESADO_ERROR);

                    SgArchivoCalificacionPAES arch = codDAOArchivoPaes.guardar(archivoPaes, null);
                } else {
                    archivoPaes.setAcpEstadoArchivo(EnumEstadoArchivoCalificacionPAES.PROCESADO);
                    SgArchivoCalificacionPAES arch = codDAOArchivoPaes.guardar(archivoPaes, null);
                }
            }
        }

        return Boolean.TRUE;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroArchivoCalificacionPAES filtro) throws GeneralException {
        try {
            ArchivoCalificacionPAESDAO codDAO = new ArchivoCalificacionPAESDAO(em);
            return codDAO.obtenerTotalPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgCalendario>
     * @throws GeneralException
     */
    public List<SgArchivoCalificacionPAES> obtenerPorFiltro(FiltroArchivoCalificacionPAES filtro) throws GeneralException {
        try {

            ArchivoCalificacionPAESDAO codDAO = new ArchivoCalificacionPAESDAO(em);
            return codDAO.obtenerPorFiltro(filtro);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
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
                SgArchivoCalificacionPAES entity = em.find(SgArchivoCalificacionPAES.class, id);
                CodigueraDAO<SgArchivoCalificacionPAES> codDAO = new CodigueraDAO<>(em, SgArchivoCalificacionPAES.class);
                codDAO.eliminar(entity, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    public SgEstructuraCalificacionPaes calificarEstudiantePAES(String nieSt, String calificacion, String componentePAEScodigo) throws Exception {
        SgEstructuraCalificacionPaes estructura = new SgEstructuraCalificacionPaes();
        String respuesta = "";
        Long nie = null;
        try {
            nie = Long.parseLong(nieSt);
        } catch (NumberFormatException ex) {
            respuesta += "Código de nie incorrecto para el estudiante de nie " + nieSt + " y componente " + componentePAEScodigo;
            estructura.setEcpEstado(EnumEstadoArchivoCalificacionPAES.PROCESADO_ERROR);
            estructura.setEcpResultado(respuesta);
            return estructura;
        }

        FiltroComponentePlanEstudio fcpe = new FiltroComponentePlanEstudio();
        try {
            fcpe.setCpeCodigoExterno(Integer.parseInt(componentePAEScodigo));
        } catch (NumberFormatException ex) {
            respuesta += "Código de componente incorrecto para el estudiante de nie " + nie + " y componente " + componentePAEScodigo;
            estructura.setEcpEstado(EnumEstadoArchivoCalificacionPAES.PROCESADO_ERROR);
            estructura.setEcpResultado(respuesta);
            return estructura;
        }
        fcpe.setMaxResults(1L);
        fcpe.setCpeEsPaes(Boolean.TRUE);
        fcpe.setIncluirCampos(new String[]{"cpePk", "cpeTipo", "cpeVersion"});
        List<SgComponentePlanEstudio> componentePAESList = componentePlanEstudioBean.obtenerPorFiltro(fcpe);
        if (componentePAESList.isEmpty()) {
            respuesta += "No existe componente plan estudio " + componentePAEScodigo;
            estructura.setEcpEstado(EnumEstadoArchivoCalificacionPAES.PROCESADO_ERROR);
            estructura.setEcpResultado(respuesta);
            return estructura;
        }

        SgComponentePlanEstudio componentePAES = componentePAESList.get(0);

        if (nie != null && !StringUtils.isBlank(calificacion) && componentePAES != null) {

            Query q = em.createNativeQuery("select "
                    + "  mat.mat_pk,sec.sec_pk,sec.sec_version,gra.gra_pk,plaest.pes_pk,est.est_pk,est.est_version,ale.ale_pk,per.per_pk "
                    + "from "
                    + "  centros_educativos.sg_matriculas mat "
                    + "  inner join centros_educativos.sg_secciones sec on mat.mat_seccion_fk = sec.sec_pk "
                    + "  inner join centros_educativos.sg_servicio_educativo sered on sered.sdu_pk=sec.sec_servicio_educativo_fk "
                    + "  inner join centros_educativos.sg_grados gra on gra.gra_pk=sered.sdu_grado_fk "
                    + "  inner join centros_educativos.sg_planes_estudio plaest on plaest.pes_pk=sec.sec_plan_estudio_fk  "
                    + "  inner join centros_educativos.sg_estudiantes est on est.est_pk=mat.mat_estudiante_fk "
                    + "  inner join centros_educativos.sg_personas per on per.per_pk=est.est_persona "
                    + "  inner join centros_educativos.sg_anio_lectivo ale on ale.ale_pk=sec.sec_anio_lectivo_fk "
                    + "where "
                    + "  per.per_nie=:nie "
                    + "  and "
                    + "  exists (select 1 from centros_educativos.sg_componente_plan_grado cpg "
                    + "			inner join centros_educativos.sg_componente_plan_estudio cpe on cpe.cpe_pk=cpg.cpg_componente_plan_estudio_fk "
                    + "			inner join centros_educativos.sg_grados gra_aux on gra_aux.gra_pk=cpg.cpg_grado_fk "
                    + "			inner join centros_educativos.sg_planes_estudio plaest_aux on plaest_aux.pes_pk=cpg.cpg_plan_estudio_fk "
                    + "			where cpe.cpe_pk=:cpePk and gra_aux.gra_pk=gra.gra_pk and plaest_aux.pes_pk=plaest.pes_pk) "
                    + " order by ale.ale_anio desc, mat.mat_pk desc "
                    + " limit 1 ");
            q.setParameter("nie", nie);
            q.setParameter("cpePk", componentePAES.getCpePk());

            List<Object[]> objs = (List<Object[]>) q.getResultList();

            if (!objs.isEmpty()) {

                Object[] obj = objs.get(0);
                SgSeccion sec = new SgSeccion(((BigInteger) obj[1]).longValue(), ((Integer) obj[2]));
                FiltroComponentePlanGrado fpg = new FiltroComponentePlanGrado();
                fpg.setCpgGradoPk(((BigInteger) obj[3]).longValue());
                fpg.setCpgPlanEstudioPk(((BigInteger) obj[4]).longValue());
                fpg.setCpgComponentePlanEstudioPk(componentePAES.getCpePk());
                fpg.setIncluirCampos(new String[]{"cpgEscalaCalificacion.ecaTipoEscala", "cpgEscalaCalificacion.ecaPk",
                    "cpgEscalaCalificacion.ecaValorMinimo.calOrden",
                    "cpgEscalaCalificacion.ecaMinimo",
                    "cpgEscalaCalificacion.ecaMaximo",
                    "cpgEscalaCalificacion.ecaMinimoAprobacion"});
                List<SgComponentePlanGrado> componentesPlanGradoList = componentePlanGradoBean.obtenerPorFiltro(fpg);

                if (!componentesPlanGradoList.isEmpty()) {

                    SgComponentePlanGrado cpg = componentesPlanGradoList.get(0);
                    SgEscalaCalificacion escCalificacion = cpg.getCpgEscalaCalificacion();
                    Double num = null;
                    SgCalificacion cal = null;
                    if (TipoEscalaCalificacion.NUMERICA.equals(cpg.getCpgEscalaCalificacion().getEcaTipoEscala())) {

                        try {
                            num = Double.parseDouble(calificacion);
                            if ((cpg.getCpgEscalaCalificacion().getEcaMaximo() < num) || (cpg.getCpgEscalaCalificacion().getEcaMinimo() > num)) {
                                respuesta += "Calificación " + calificacion + " no cumple con escala para el estudiante " + nie + " en componente " + componentePAES.getCpeCodigoExterno();

                            }
                        } catch (NumberFormatException e) {
                            respuesta += "Calificación " + calificacion + " no cumple con escala para el estudiante " + nie + " en componente " + componentePAES.getCpeCodigoExterno();

                        }

                    } else {

                        FiltroCalificacionCa fc = new FiltroCalificacionCa();
                        fc.setCalEscalaCalificacionPk(cpg.getCpgEscalaCalificacion().getEcaPk());
                        fc.setCalValor(calificacion);

                        List<SgCalificacion> calList = calificacionCatalogosBean.obtenerPorFiltro(fc); //Tiene cache
                        if (calList.isEmpty()) {
                            respuesta += "Calificación " + calificacion + " no cumple con escala para el estudiante " + nie + " en componente " + componentePAES.getCpeCodigoExterno();

                        } else {
                            cal = calList.get(0);
                        }

                    }

                    FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
                    fce.setCaeEstudiantePk(((BigInteger) obj[5]).longValue());
                    fce.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{EnumTiposPeriodosCalificaciones.PAES, EnumTiposPeriodosCalificaciones.NOTIN, EnumTiposPeriodosCalificaciones.APR}));
                    fce.setAnioLectivoPk(((BigInteger) obj[7]).longValue());
                    fce.setCaeComponentePlanEstudio(componentePAES.getCpePk());
                    fce.setIncluirCampos(new String[]{"caeCalificacion.calPk",
                        "caeCalificacion.calPk",
                        "caeCalificacion.calVersion",
                        "caeCalificacion.calTipoPeriodoCalificacion",
                        "caeEstudiante.estPk",
                        "caeEstudiante.estVersion",
                        "caeResolucion",
                        "caePromovidoCalificacion",
                        "caeCalificacionNumericaEstudiante",
                        "caeCalificacionConceptualEstudiante.calPk",
                        "caeCalificacionConceptualEstudiante.calVersion",
                        "caeObservacion",
                        "caeFechaRealizado",
                        "caeVersion",
                        "caeProcesoDeCreacion"});
                    List<SgCalificacionEstudiante> listCalificaciones = calificacionEstudianteBean.obtenerPorFiltro(fce);
                    List<SgCalificacionEstudiante> listCalEs = listCalificaciones.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.PAES)).collect(Collectors.toList());
                    listCalificaciones = listCalificaciones.stream().filter(c -> !c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.PAES)).collect(Collectors.toList());

                    FiltroCalificacion fc = new FiltroCalificacion();
                    fc.setSecPk(sec.getSecPk());
                    fc.setSecAnioLectivoFk(((BigInteger) obj[7]).longValue());
                    fc.setCalComponentePlanEstudio(componentePAES.getCpePk());
                    fc.setCalTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{EnumTiposPeriodosCalificaciones.PAES, EnumTiposPeriodosCalificaciones.NOTIN, EnumTiposPeriodosCalificaciones.APR}));
                    fc.setIncluirCampos(new String[]{"calPk", "calComponentePlanEstudio.cpePk",
                        "calComponentePlanEstudio.cpeTipo",
                        "calComponentePlanEstudio.cpeVersion",
                        "calSeccion.secPk",
                        "calSeccion.secVersion",
                        "calFecha",
                        "calTipoPeriodoCalificacion",
                        "calVersion"});
                    CalificacionDAO codDAOCal = new CalificacionDAO(em, null);
                    List<SgCalificacionCE> listCalAux = codDAOCal.obtenerPorFiltro(fc, null);

                    SgCalificacionEstudiante calEstudiante = new SgCalificacionEstudiante();
                    SgCalificacionCE calificacionPaes = new SgCalificacionCE();

                    SgCalificacionEstudiante calEstudianteNOTIN = new SgCalificacionEstudiante();
                    SgCalificacionCE calificacionNOTIN = new SgCalificacionCE();

                    SgCalificacionEstudiante calEstudianteAPR = new SgCalificacionEstudiante();
                    SgCalificacionCE calificacionAPR = new SgCalificacionCE();

                    SgEstudiante est = new SgEstudiante(((BigInteger) obj[5]).longValue(), ((Integer) obj[6]));

                    if (!listCalEs.isEmpty()) {
                        calEstudiante = listCalEs.get(0);

                    } else {

                        List<SgCalificacionCE> listCal = listCalAux.stream().filter(c -> c.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.PAES)).collect(Collectors.toList());
                        if (!listCal.isEmpty()) {
                            calificacionPaes = listCal.get(0);

                        } else {
                            calificacionPaes.setCalComponentePlanEstudio(componentePAES);
                            calificacionPaes.setCalFecha(LocalDate.now());
                            calificacionPaes.setCalSeccion(sec);
                            calificacionPaes.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.PAES);
                        }

                        calEstudiante.setCaeEstudiante(est);
                        calEstudiante.setCaeCalificacion(calificacionPaes);

                    }

                    //CREAR O EDITAR NOTA INSTITUCIONAL                   
                    List<SgCalificacionEstudiante> calEstNOTIN = listCalificaciones.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)).collect(Collectors.toList());
                    if (!calEstNOTIN.isEmpty()) {
                        calEstudianteNOTIN = calEstNOTIN.get(0);
                    } else {
                        //Nota institucional
                        List<SgCalificacionCE> listCalNotin = listCalAux.stream().filter(c -> c.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)).collect(Collectors.toList());
                        if (!listCalNotin.isEmpty()) {
                            calificacionNOTIN = listCalNotin.get(0);
                        } else {
                            calificacionNOTIN.setCalComponentePlanEstudio(componentePAES);
                            calificacionNOTIN.setCalFecha(LocalDate.now());
                            calificacionNOTIN.setCalSeccion(sec);
                            calificacionNOTIN.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.NOTIN);
                        }

                        calEstudianteNOTIN.setCaeEstudiante(est);
                        calEstudianteNOTIN.setCaeCalificacion(calificacionNOTIN);
                    }

                    //CREAR O EDITAR APROBACION               
                    List<SgCalificacionEstudiante> calEstAPR = listCalificaciones.stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.APR)).collect(Collectors.toList());
                    if (!calEstAPR.isEmpty()) {
                        calEstudianteAPR = calEstAPR.get(0);
                    } else {
                        //Aprobacion
                        List<SgCalificacionCE> listCalApr = listCalAux.stream().filter(c -> c.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.APR)).collect(Collectors.toList());
                        if (!listCalApr.isEmpty()) {
                            calificacionAPR = listCalApr.get(0);
                        } else {
                            calificacionAPR.setCalComponentePlanEstudio(componentePAES);
                            calificacionAPR.setCalFecha(LocalDate.now());
                            calificacionAPR.setCalSeccion(sec);
                            calificacionAPR.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.APR);
                        }

                        calEstudianteAPR.setCaeEstudiante(est);
                        calEstudianteAPR.setCaeCalificacion(calificacionAPR);
                    }

                    //ASIGNAR VALOR CALIFICACION
                    if (num != null) {
                        calEstudiante.setCaeCalificacionNumericaEstudiante(calificacion);

                        calEstudianteNOTIN.setCaeCalificacionNumericaEstudiante(calificacion);

                        calEstudianteAPR.setCaeCalificacionNumericaEstudiante(calificacion);
                        calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.PENDIENTE);
                        if (escCalificacion.getEcaMinimoAprobacion().compareTo(num) > 0) {
                            calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.NO_APROBADO);
                        } else {
                            calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.APROBADO);
                        }
                    } else {
                        if (cal != null) {
                            calEstudiante.setCaeCalificacionConceptualEstudiante(cal);

                            calEstudianteNOTIN.setCaeCalificacionConceptualEstudiante(cal);

                            calEstudianteAPR.setCaeCalificacionConceptualEstudiante(cal);
                            calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.PENDIENTE);
                            if (escCalificacion.getEcaValorMinimo() != null && escCalificacion.getEcaValorMinimo().getCalOrden() != null) {
                                if (cal.getCalOrden() >= escCalificacion.getEcaValorMinimo().getCalOrden()) {
                                    calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.APROBADO);
                                } else {
                                    calEstudianteAPR.setCaeResolucion(EnumResolucionCalificacion.NO_APROBADO);
                                }
                            }
                        } else {
                            respuesta += "Calificación " + calificacion + " no cumple con escala para el estudiante " + nie + " en componente " + componentePAES.getCpeCodigoExterno();
                            estructura.setEcpEstado(EnumEstadoArchivoCalificacionPAES.PROCESADO_ERROR);
                            estructura.setEcpResultado(respuesta);
                            return estructura;
                        }
                    }

                    //GUARDAR
                    if (StringUtils.isBlank(respuesta)) {
                        if (calEstudiante.getCaePk() != null || calEstudiante.getCaeCalificacion().getCalPk() != null) {
                            CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                            calEstudiante = codDAO.guardar(calEstudiante, null);
                            this.calificacionBean.marcarPromedioYModaDesactualizados(calEstudiante.getCaeCalificacion().getCalPk());
                        } else {
                            List<SgCalificacionEstudiante> calEstList = new ArrayList();
                            calEstList.add(calEstudiante);
                            calificacionPaes.setCalCalificacionesEstudiante(calEstList);

                            CodigueraDAO<SgCalificacionCE> codDAO = new CodigueraDAO<>(em, SgCalificacionCE.class);
                            codDAO.guardar(calificacionPaes, null);
                            this.calificacionBean.marcarPromedioYModaDesactualizados(calificacionPaes.getCalPk());
                        }
                        //Guardar Nota institucional
                        if (calEstudianteNOTIN.getCaePk() != null || calEstudianteNOTIN.getCaeCalificacion().getCalPk() != null) {
                            //cabezal existente o calificacion existente
                            CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                            codDAO.guardar(calEstudianteNOTIN, null);
                            this.calificacionBean.marcarPromedioYModaDesactualizados(calEstudianteNOTIN.getCaeCalificacion().getCalPk());
                        } else {
                            //Si ambos son null, entonces cabezal no existe en la base. Se crea
                            List<SgCalificacionEstudiante> calEstList = new ArrayList();
                            calEstList.add(calEstudianteNOTIN);
                            calificacionNOTIN.setCalCalificacionesEstudiante(calEstList);

                            CodigueraDAO<SgCalificacionCE> codDAO = new CodigueraDAO<>(em, SgCalificacionCE.class);
                            codDAO.guardar(calificacionNOTIN, null);
                            this.calificacionBean.marcarPromedioYModaDesactualizados(calificacionNOTIN.getCalPk());
                        }

                        //Guardar Aprobacion
                        if (calEstudianteAPR.getCaePk() != null || calEstudianteAPR.getCaeCalificacion().getCalPk() != null) {
                            CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                            codDAO.guardar(calEstudianteAPR, null);
                            this.calificacionBean.marcarPromedioYModaDesactualizados(calEstudianteAPR.getCaeCalificacion().getCalPk());
                        } else {
                            List<SgCalificacionEstudiante> calEstList = new ArrayList();
                            calEstList.add(calEstudianteAPR);
                            calificacionAPR.setCalCalificacionesEstudiante(calEstList);

                            CodigueraDAO<SgCalificacionCE> codDAO = new CodigueraDAO<>(em, SgCalificacionCE.class);
                            codDAO.guardar(calificacionAPR, null);
                            this.calificacionBean.marcarPromedioYModaDesactualizados(calificacionAPR.getCalPk());
                        }

                        estructura.setEcpEstado(EnumEstadoArchivoCalificacionPAES.PROCESADO);
                        estructura.setEcpPersonaFk(new SgPersona(((BigInteger) obj[8]).longValue()));

                    }

                } else {
                    respuesta += "No existe componente plan grado para el componente plan de estudio " + componentePAEScodigo + " y el estudiante de nie" + nieSt;
                }

            } else {
                respuesta += "El estudiante de NIE " + nieSt + " no tiene relación con la componente " + componentePAEScodigo;
            }
        }
        if (!StringUtils.isBlank(respuesta)) {
            estructura.setEcpEstado(EnumEstadoArchivoCalificacionPAES.PROCESADO_ERROR);
            estructura.setEcpResultado(respuesta);
        }
        return estructura;

    }

}
