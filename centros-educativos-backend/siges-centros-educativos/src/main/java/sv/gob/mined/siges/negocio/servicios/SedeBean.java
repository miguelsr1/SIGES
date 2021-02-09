/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import org.eclipse.microprofile.opentracing.Traced;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Query;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgCambioDomicilioSedeRequest;
import sv.gob.mined.siges.dto.SgCambioNominacionSedeRequest;
import sv.gob.mined.siges.dto.SgConsultaMatriculasSeccionResponseSAT;
import sv.gob.mined.siges.dto.SgDatoGeneralCentroEducativo;
import sv.gob.mined.siges.dto.SgDocenteCentroEducativo;
import sv.gob.mined.siges.dto.SgIndicador;
import sv.gob.mined.siges.dto.SgIndicadorNota;
import sv.gob.mined.siges.dto.SgRegistroSedeRequest;
import sv.gob.mined.siges.dto.SgRegistroServiciosEducativos;
import sv.gob.mined.siges.dto.SgRevocacionSedeRequest;
import sv.gob.mined.siges.dto.SgSeccionCentroEducativo;
import sv.gob.mined.siges.dto.SgSeccionIndicadores;
import sv.gob.mined.siges.dto.SgSeccionIndicadoresNota;
import sv.gob.mined.siges.dto.SgSedeMatriculasValidadas;
import sv.gob.mined.siges.enumerados.EnumEstadoOrganismoAdministrativo;
import sv.gob.mined.siges.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.enumerados.TipoSede;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroDatoContratacion;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.filtros.FiltroMatriculasEnSeccionSAT;
import sv.gob.mined.siges.filtros.FiltroNivel;
import sv.gob.mined.siges.filtros.FiltroOrganismoAdministrativoEscolar;
import sv.gob.mined.siges.filtros.FiltroRelPersonalEspecialidad;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.filtros.FiltroSedes;
import sv.gob.mined.siges.filtros.FiltroServicioEducativo;
import sv.gob.mined.siges.filtros.catalogo.FiltroTipoAcuerdo;
import sv.gob.mined.siges.filtros.seguridad.FiltroUsuario;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.SedesValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SedesDAO;
import sv.gob.mined.siges.persistencia.entidades.SgAcuerdoSede;
import sv.gob.mined.siges.persistencia.entidades.SgCentroEducativo;
import sv.gob.mined.siges.persistencia.entidades.SgCentroEducativoOficial;
import sv.gob.mined.siges.persistencia.entidades.SgCentroEducativoPrivado;
import sv.gob.mined.siges.persistencia.entidades.SgCirculoAlfabetizacion;
import sv.gob.mined.siges.persistencia.entidades.SgCirculoInfancia;
import sv.gob.mined.siges.persistencia.entidades.SgDatoContratacion;
import sv.gob.mined.siges.persistencia.entidades.SgDireccion;
import sv.gob.mined.siges.persistencia.entidades.SgRelPersonalEspecialidad;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.entidades.SgSedeEducame;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCanton;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgEspecialidad;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgJornadaLaboral;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoCierreSede;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMunicipio;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoAcuerdo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCalendarioEscolar;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoOrganismoAdministrativo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgZona;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuario;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

@Stateless
@Traced
public class SedeBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private NivelBean nivelBean;

    @Inject
    private ServicioEducativoBean servicioEducativoBean;

    @Inject
    private SeccionBean seccionBean;

    @Inject
    private DatoContratacionBean datoContratacionBean;

    @Inject
    private RelPersonalEspecialidadBean relPersonalEspecialidadBean;

    @Inject
    private MatriculaBean matriculaBean;

    @Inject
    private OrganismoAdministracionEscolarBean organismoAdministracionEscolarBean;

    @Inject
    private CentroEducativoOficialBean centroOficialBean;

    @Inject
    private CentroEducativoPrivadoBean centroPrivadoBean;

    @Inject
    private CirculoAlfabetizacionBean circuloAlfBean;

    @Inject
    private CirculoInfanciaBean circuloInfBean;

    @Inject
    private SedeEducameBean sedeEducameBean;

    @Inject
    private AcuerdoSedeBean acuerdoSedeBean;

    @Inject
    private CatalogoBean catalogoBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSedes
     * @throws GeneralException
     */
    public SgSede obtenerPorId(Long id, Boolean dataSecurity) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSede> codDAO = new CodigueraDAO<>(em, SgSede.class);
                SgSede sed = codDAO.obtenerPorId(id, dataSecurity ? ConstantesOperaciones.BUSCAR_SEDES : null);
                if (sed.getSedPk() != null && (sed.getSedTipo().equals(TipoSede.CIR_ALF) || sed.getSedTipo().equals(TipoSede.CIR_INF) || sed.getSedTipo().equals(TipoSede.UBI_EDUC))) {
                    FiltroSedes fs = new FiltroSedes();
                    fs.setSedAdscritaPk(sed.getSedPk());
                    fs.setIncluirCampos(new String[]{"sedPk"});
                    fs.setMaxResults(1L);
                    List<SgSede> listSed = this.obtenerPorFiltro(fs);
                    if (!listSed.isEmpty()) {
                        sed.setSedEsAdscriptaAOtraSede(Boolean.TRUE);
                    }
                }

                if (sed != null) {
                    if (sed.getSedJornadasLaborales() != null) {
                        sed.getSedJornadasLaborales().size();
                    }
                    if (sed.getSedRelServicioInfra() != null) {
                        sed.getSedRelServicioInfra().size();
                    }
                    if (sed.getSedFactoresRiesgoSocial() != null) {
                        sed.getSedFactoresRiesgoSocial().size();
                    }
                }

                return sed;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSedes
     * @throws GeneralException
     */
    public SgSede obtenerPorIdLazy(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSede> codDAO = new CodigueraDAO<>(em, SgSede.class);
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
                CodigueraDAO<SgSede> codDAO = new CodigueraDAO<>(em, SgSede.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SEDES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroSedes filtro) throws GeneralException {
        try {
            SedesDAO dao = new SedesDAO(em, seguridadBean);
            return dao.cantidadTotal(filtro, filtro.getSecurityOperation());
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Realiza la consulta de los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de SgSedes
     * @throws GeneralException
     */
    public List<SgSede> obtenerPorFiltro(FiltroSedes filtro) throws GeneralException {
        try {
            SedesDAO dao = new SedesDAO(em, seguridadBean);
            List<SgSede> sedes = dao.obtenerPorFiltro(filtro, filtro.getSecurityOperation());
            if (BooleanUtils.isTrue(filtro.getIncluirJornadas())) {
                for (SgSede sed : sedes) {
                    sed.getSedJornadasLaborales().size();
                }
            }

            List<Long> sedesPk = sedes.stream().map(s -> s.getSedPk()).collect(Collectors.toList());
            if (!sedesPk.isEmpty()) {
                if (BooleanUtils.isTrue(filtro.getIncluirCantidadSeccionesAbiertasAnioCorriente())) {
                    HashMap<Long, Long> ret = this.obtenerSeccionesAbiertasAnioCorriente(sedesPk, filtro.getSoloModalidadesFlexibles());
                    for (SgSede sed : sedes) {
                        sed.setSedCantidadSeccionesAbiertasAnioCorriente(ret.get(sed.getSedPk()) != null ? ret.get(sed.getSedPk()) : 0L);
                    }
                }
                if (BooleanUtils.isTrue(filtro.getIncluirCantidadMatriculasValidadasYNoValidadasAnioCorriente())) {
                    HashMap<Long, SgSedeMatriculasValidadas> ret = this.obtenerMatriculasValidadasYNoValidadasAnioCorriente(sedesPk, filtro.getSoloModalidadesFlexibles());
                    for (SgSede sed : sedes) {
                        SgSedeMatriculasValidadas mv = ret.get(sed.getSedPk());

                        sed.setSedCantidadMatriculasNoValidadasAnioCorriente(mv != null ? mv.getCantidadMatriculasNoValidadas() : 0L);
                        sed.setSedCantidadMatriculasValidadasAnioCorriente(mv != null ? mv.getCantidadMatriculasValidadas() : 0L);
                    }
                }
            }

            return sedes;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @Interceptors(AuditInterceptor.class)
    public void eliminar(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSede> codDAO = new CodigueraDAO<>(em, SgSede.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_SEDES);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

    /**
     * Retorna cantidad de matrículas validadas y no validadas por sede para el
     * año corriente
     *
     * @param sedesPk List<Long>
     * @return HashMap<Long, SgSedeMatriculasValidadas>, key = sedePk cantidad
     * @throws GeneralException
     */
    public HashMap<Long, SgSedeMatriculasValidadas> obtenerMatriculasValidadasYNoValidadasAnioCorriente(List<Long> sedesPk, Boolean soloModalidadesFlexibles) throws GeneralException {
        try {
            if (sedesPk != null && !sedesPk.isEmpty()) {

                String where = "";

                if (BooleanUtils.isTrue(soloModalidadesFlexibles)) {
                    where += " and relmodaten.rea_modalidad_atencion_flexible ";
                }

                Query q = em.createNativeQuery(" select sede.sed_pk as sedePk, mat.mat_validacion_academica, count(*)"
                        + "     from centros_educativos.sg_matriculas mat"
                        + "     INNER JOIN centros_educativos.sg_secciones sec ON (mat.mat_seccion_fk = sec.sec_pk)"
                        + "     INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sdu.sdu_pk = sec.sec_servicio_educativo_fk)"
                        + "	INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk)"
                        + "	INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)"
                        + "     INNER JOIN centros_educativos.sg_sedes sede ON (sdu.sdu_sede_fk = sede.sed_pk)"
                        + "     INNER JOIN centros_educativos.sg_anio_lectivo ale ON (sec.sec_anio_lectivo_fk = ale.ale_pk)"
                        + "     where mat.mat_retirada = false and mat.mat_anulada = false "
                        + "     and ale_pk = (select ale_pk from centros_educativos.sg_anio_lectivo ale where ale_corriente order by ale_anio desc limit 1) "
                        + "     and sede.sed_pk IN (:sedesPk)" + where
                        + "     group by sede.sed_pk, mat.mat_validacion_academica");

                q.setParameter("sedesPk", sedesPk);

                List<Object[]> ret = q.getResultList();

                HashMap<Long, SgSedeMatriculasValidadas> sedesCantidad = new HashMap<>();

                for (Object[] o : ret) {

                    Long sedePk = ((BigInteger) o[0]).longValue();

                    if (Boolean.TRUE.equals((Boolean) o[1])) {
                        sedesCantidad.computeIfAbsent(sedePk, s -> new SgSedeMatriculasValidadas(sedePk))
                                .setCantidadMatriculasValidadas(((BigInteger) o[2]).longValue());
                    } else {
                        sedesCantidad.computeIfAbsent(sedePk, s -> new SgSedeMatriculasValidadas(sedePk))
                                .setCantidadMatriculasNoValidadas(((BigInteger) o[2]).longValue());
                    }
                }

                return sedesCantidad;
            } else {
                return null;
            }

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    /**
     * Retorna cantidad de secciones abiertas por sede para el año corriente
     *
     * @param sedesPk List<Long>
     * @return HashMap<Long, Long>. key = sedePk, value = cantidad
     * @throws GeneralException
     */
    public HashMap<Long, Long> obtenerSeccionesAbiertasAnioCorriente(List<Long> sedesPk, Boolean soloModalidadesFlexibles) throws GeneralException {
        try {

            if (sedesPk != null && !sedesPk.isEmpty()) {

                String where = "";

                if (BooleanUtils.isTrue(soloModalidadesFlexibles)) {
                    where += " and relmodaten.rea_modalidad_atencion_flexible ";
                }

                Query q = em.createNativeQuery(" select sede.sed_pk as sedePk, count(*) "
                        + "  from centros_educativos.sg_secciones sec "
                        + "  INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sdu.sdu_pk = sec.sec_servicio_educativo_fk) "
                        + "  INNER JOIN centros_educativos.sg_grados gra ON (sdu.sdu_grado_fk = gra.gra_pk)"
                        + "  INNER JOIN centros_educativos.sg_rel_mod_ed_mod_aten relmodaten ON (gra.gra_relacion_modalidad_fk = relmodaten.rea_pk)"
                        + "  INNER JOIN centros_educativos.sg_sedes sede ON (sdu.sdu_sede_fk = sede.sed_pk) "
                        + "  INNER JOIN centros_educativos.sg_anio_lectivo ale ON (sec.sec_anio_lectivo_fk = ale.ale_pk) "
                        + "  where sec.sec_estado = 'ABIERTA' "
                        + "  and ale_pk = (select ale_pk from centros_educativos.sg_anio_lectivo ale where ale_corriente order by ale_anio desc limit 1)"
                        + "  and sede.sed_pk IN (:sedesPk) " + where
                        + "  group by sede.sed_pk");

                q.setParameter("sedesPk", sedesPk);

                List<Object[]> ret = q.getResultList();

                HashMap<Long, Long> sedesCantidad = new HashMap<>();

                for (Object[] o : ret) {
                    sedesCantidad.put(((BigInteger) o[0]).longValue(), ((BigInteger) o[1]).longValue());
                }

                return sedesCantidad;

            } else {
                return null;
            }

        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    public Integer obtenerAnioUltimaMatricula(Long sedePk) throws GeneralException {
        if (sedePk != null) {
            try {
                return (Integer) em.createNativeQuery("SELECT CAST(date_part('year', m.mat_fecha_ingreso) as integer) from centros_educativos.sg_matriculas m "
                        + " INNER JOIN centros_educativos.sg_secciones sec ON (m.mat_seccion_fk = sec.sec_pk)"
                        + " INNER JOIN centros_educativos.sg_servicio_educativo se ON (sec.sec_servicio_educativo_fk = se.sdu_pk)  "
                        + " INNER JOIN centros_educativos.sg_sedes sed ON (se.sdu_sede_fk = sed.sed_pk)"
                        + " where sed_pk = :sedPk order by m.mat_fecha_ingreso desc limit 1")
                        .setParameter("sedPk", sedePk)
                        .getSingleResult();
            } catch (NoResultException nre) {
                return null;
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return null;
    }

    public SgDatoGeneralCentroEducativo obtenerDatosGeneralesCentroEducativo(String codigoCentro) throws GeneralException {
        try {
            if (StringUtils.isBlank(codigoCentro)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CODIGO_VACIO);
                throw be;
            }

            FiltroSedes filtro = new FiltroSedes();
            filtro.setSedCodigoExacto(codigoCentro);
            filtro.setSecurityOperation(null);
            List<SgSede> sedes = this.obtenerPorFiltro(filtro);

            if (!sedes.isEmpty()) {

                SgSede sede = sedes.get(0);

                SgDatoGeneralCentroEducativo ret = new SgDatoGeneralCentroEducativo();
                ret.setCentroEducativoCodigo(sede.getSedCodigo());
                ret.setCentroEducativoNombre(sede.getSedNombre());

                ret.setDireccion(sede.getSedDireccion().getDirDireccion());
                ret.setJornadas(sede.getSedJornadasLaborales().stream().map(j -> j.getJlaNombre()).collect(Collectors.toList()));
                ret.setLatitud(sede.getSedDireccion().getDirLatitud() != null ? sede.getSedDireccion().getDirLatitud().toString() : null);
                ret.setLongitud(sede.getSedDireccion().getDirLongitud() != null ? sede.getSedDireccion().getDirLongitud().toString() : null);
                ret.setDepartamentoNombre(sede.getSedDireccion().getDirDepartamento() != null ? sede.getSedDireccion().getDirDepartamento().getDepNombre() : null);
                ret.setMunicipioNombre(sede.getSedDireccion().getDirMunicipio() != null ? sede.getSedDireccion().getDirMunicipio().getMunNombre() : null);
                ret.setCantonNombre(sede.getSedDireccion().getDirCanton() != null ? sede.getSedDireccion().getDirCanton().getCanNombre() : null);
                ret.setZonaNombre(sede.getSedDireccion().getDirZona() != null ? sede.getSedDireccion().getDirZona().getZonNombre() : null);
                ret.setSistemaIntegrado(!sede.getSedSistemas().isEmpty() ? sede.getSedSistemas().get(0).getSinPk().getSinNombre() : null);
                ret.setTelefonos(Stream.of(sede.getSedTelefono(), sede.getSedTelefonoMovil()).filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(", ")));

                switch (sede.getSedTipo()) {
                    case CED_OFI:
                    case CIR_ALF:
                    case CIR_INF:
                    case UBI_EDUC:
                        ret.setSector("Público");
                        break;
                    case CED_PRI:
                        ret.setSector("Privado");
                        break;
                }

                FiltroServicioEducativo filServ = new FiltroServicioEducativo();
                filServ.setTieneOpcion(Boolean.TRUE);
                filServ.setSecSedeFk(sede.getSedPk());
                filServ.setSecurityOperation(null);
                ret.setOpcionBTV(servicioEducativoBean.obtenerTotalPorFiltro(filServ) > 0L);

                FiltroNivel filNivel = new FiltroNivel();
                filNivel.setSedPk(sede.getSedPk());
                filNivel.setIncluirCampos(new String[]{"nivNombre"});
                ret.setNivelesEducativos(nivelBean.obtenerPorFiltro(filNivel).stream().map(n -> n.getNivNombre()).collect(Collectors.toList()));

                FiltroDatoContratacion filtroCont = new FiltroDatoContratacion();
                filtroCont.setDcoCentroEducativo(sede.getSedPk());
                filtroCont.setDcoFecha(LocalDate.now());
                filtroCont.setDcoCargoCodigo(Constantes.CARGO_DIRECTOR);
                filtroCont.setIncluirCampos(new String[]{
                    "dcoDatoEmpleado.demPersonalSede.psePersona.perPrimerNombre",
                    "dcoDatoEmpleado.demPersonalSede.psePersona.perSegundoNombre",
                    "dcoDatoEmpleado.demPersonalSede.psePersona.perPrimerApellido",
                    "dcoDatoEmpleado.demPersonalSede.psePersona.perSegundoApellido",});

                List<SgDatoContratacion> datosCont = datoContratacionBean.obtenerPorFiltro(filtroCont);
                if (!datosCont.isEmpty()) {
                    ret.setNombreDirector(datosCont.get(0).getDcoDatoEmpleado().getDemPersonalSede().getPsePersona().getPerNombreCompleto());
                }

                return ret;
            } else {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_SEDE_INEXISTENTE);
                throw be;

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    public List<SgDocenteCentroEducativo> obtenerPlantaCentroEducativo(String codigoCentro, Integer anio) throws GeneralException {
        try {
            if (StringUtils.isBlank(codigoCentro)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CODIGO_VACIO);
                throw be;
            }
            if (anio == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CODIGO_VACIO);
                throw be;
            }

            List<SgDocenteCentroEducativo> ret = new ArrayList<>();

            FiltroDatoContratacion filtroCont = new FiltroDatoContratacion();
            filtroCont.setDcoCentroEducativoCodigo(codigoCentro);
            filtroCont.setDcoAnio(anio);
            filtroCont.setDcoCargoCodigos(Arrays.asList(new String[]{Constantes.CARGO_DIRECTOR, Constantes.CARGO_DOCENTE, Constantes.CARGO_SUBDIRECTOR}));
            filtroCont.setIncluirCampos(new String[]{
                "dcoDatoEmpleado.demPersonalSede.psePersona.perPrimerNombre",
                "dcoDatoEmpleado.demPersonalSede.psePersona.perSegundoNombre",
                "dcoDatoEmpleado.demPersonalSede.psePersona.perPrimerApellido",
                "dcoDatoEmpleado.demPersonalSede.psePersona.perSegundoApellido",
                "dcoDatoEmpleado.demPersonalSede.psePersona.perSexo.sexNombre",
                "dcoDatoEmpleado.demPersonalSede.psePersona.perDui",
                "dcoDatoEmpleado.demPersonalSede.psePersona.perNip",
                "dcoDatoEmpleado.demPersonalSede.psePk",
                "dcoCargo.carNombre",
                "dcoDesde",
                "dcoHasta"});

            List<SgDatoContratacion> datosCont = datoContratacionBean.obtenerPorFiltro(filtroCont)
                    .stream().filter(d -> d.getDcoDatoEmpleado() != null && d.getDcoDatoEmpleado().getDemPersonalSede() != null).collect(Collectors.toList());
            if (!datosCont.isEmpty()) {

                FiltroRelPersonalEspecialidad filtroRelPE = new FiltroRelPersonalEspecialidad();
                filtroRelPE.setIncluirCampos(new String[]{"rpePersonal.psePk", "rpeEspecialidad.espNombre"});
                filtroRelPE.setPersonalPks(datosCont.stream().map(c -> c.getDcoDatoEmpleado().getDemPersonalSede().getPsePk()).distinct().collect(Collectors.toList()));
                List<SgRelPersonalEspecialidad> rel = relPersonalEspecialidadBean.obtenerPorFiltro(filtroRelPE);

                HashMap<Long, List<SgEspecialidad>> perEspecialidades = new HashMap<>();

                for (SgRelPersonalEspecialidad r : rel) {
                    perEspecialidades.computeIfAbsent(r.getRpePersonal().getPsePk(), s -> new ArrayList<>()).add(r.getRpeEspecialidad());
                }

                for (SgDatoContratacion dat : datosCont) {
                    List<SgEspecialidad> especialidades = perEspecialidades.get(dat.getDcoDatoEmpleado().getDemPersonalSede().getPsePk());

                    SgDocenteCentroEducativo dce = new SgDocenteCentroEducativo();
                    dce.setCargo(dat.getDcoCargo().getCarNombre());
                    dce.setCentroEducativoCodigo(codigoCentro);
                    dce.setDocenteNombre(dat.getDcoDatoEmpleado().getDemPersonalSede().getPsePersona().getPerNombreCompleto());
                    dce.setDui(dat.getDcoDatoEmpleado().getDemPersonalSede().getPsePersona().getPerDui());
                    dce.setEspecialidad(especialidades != null ? especialidades.stream().map(e -> e.getEspNombre()).collect(Collectors.joining(", ")) : null);
                    dce.setNip(dat.getDcoDatoEmpleado().getDemPersonalSede().getPsePersona().getPerNip());
                    dce.setSexo(dat.getDcoDatoEmpleado().getDemPersonalSede().getPsePersona().getPerSexo().getSexNombre());
                    dce.setActivo((dat.getDcoDesde() == null || !dat.getDcoDesde().isAfter(LocalDate.now()))
                            && (dat.getDcoHasta() == null || !dat.getDcoHasta().isBefore(LocalDate.now())));

                    ret.add(dce);
                }

            }

            return ret;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    public List<SgSeccionCentroEducativo> obtenerSeccionesCentroEducativo(String codigoCentro, Integer anio) throws GeneralException {
        try {
            if (StringUtils.isBlank(codigoCentro)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CODIGO_VACIO);
                throw be;
            }
            if (anio == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_ANIO_VACIO);
                throw be;
            }

            List<SgSeccionCentroEducativo> ret = new ArrayList<>();

            FiltroSeccion filtroSec = new FiltroSeccion();
            filtroSec.setSecAnioLectivoAnio(anio);
            filtroSec.setCodigoSede(codigoCentro);
            filtroSec.setSecurityOperation(null);
            filtroSec.setIncluirCampos(new String[]{
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrganizacionCurricular.ocuNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                //"secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                "secServicioEducativo.sduOpcion.opcNombre",
                "secServicioEducativo.sduProgramaEducativo.pedNombre",
                "secServicioEducativo.sduGrado.graNombre",
                "secNombre",
                "secCodigo",
                "secEstado",
                "secJornadaLaboral.jlaNombre",
                "secPk",
                "secVersion"});
            filtroSec.setOrderBy(new String[]{"secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivOrden",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicOrden",
                "secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modOrden",
                "secServicioEducativo.sduGrado.graOrden",
                "secNombre"});
            filtroSec.setAscending(new boolean[]{true, true, true, true, true});
            List<SgSeccion> secciones = seccionBean.obtenerPorFiltro(filtroSec);

            for (SgSeccion sec : secciones) {

                SgSeccionCentroEducativo s = new SgSeccionCentroEducativo();
                s.setCentroEducativoCodigo(codigoCentro);
                s.setGrado(sec.getSecServicioEducativo().getSduGrado().getGraNombre());
                s.setId(sec.getSecPk());
                s.setJornada(sec.getSecJornadaLaboral().getJlaNombre());
                s.setModalidadAtencion(sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion().getMatNombre());
                s.setModalidadEducativa(sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModNombre());
                s.setOrganizacionCurricular(sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivOrganizacionCurricular().getOcuNombre());
                s.setNivelEducativo(sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivNombre());
                s.setCiclo(sec.getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNombre());
                s.setOpcion(sec.getSecServicioEducativo().getSduOpcion() != null ? sec.getSecServicioEducativo().getSduOpcion().getOpcNombre() : null);
                s.setProgramaEducativo(sec.getSecServicioEducativo().getSduProgramaEducativo() != null ? sec.getSecServicioEducativo().getSduProgramaEducativo().getPedNombre() : null);
                s.setSeccion(sec.getSecCodigo());
                //s.setSectorEconomico();

                ret.add(s);

            }

            return ret;
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    public List<SgSeccionIndicadores> obtenerIndicadoresCentroEducativo(String codigoCentro, LocalDate fecha) throws GeneralException {
        try {
            if (StringUtils.isBlank(codigoCentro)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CODIGO_VACIO);
                throw be;
            }
            if (fecha == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_FECHA_VACIO);
                throw be;
            }

            FiltroMatriculasEnSeccionSAT filtro = new FiltroMatriculasEnSeccionSAT(codigoCentro, fecha);

            List<SgConsultaMatriculasSeccionResponseSAT> matriculas = matriculaBean.obtenerMatriculasEnSeccionSAT(filtro);

            filtro = new FiltroMatriculasEnSeccionSAT(codigoCentro, fecha);
            filtro.setSoloRepetidores(Boolean.TRUE);
            List<SgConsultaMatriculasSeccionResponseSAT> repetidores = matriculaBean.obtenerMatriculasEnSeccionSAT(filtro);

            filtro = new FiltroMatriculasEnSeccionSAT(codigoCentro, fecha);
            filtro.setSoloSobreedad(Boolean.TRUE);
            List<SgConsultaMatriculasSeccionResponseSAT> sobreedad = matriculaBean.obtenerMatriculasEnSeccionSAT(filtro);

            HashMap<Long, SgSeccionIndicadores> sechash = new HashMap<>();

            for (SgConsultaMatriculasSeccionResponseSAT it : matriculas) {
                sechash.computeIfAbsent(it.getSeccionPk(), s -> new SgSeccionIndicadores(it.getSeccionPk())).getIndicadores().add(new SgIndicador("Masculino", "MATRICULA", it.getCantMatMasculino()));
                sechash.computeIfAbsent(it.getSeccionPk(), s -> new SgSeccionIndicadores(it.getSeccionPk())).getIndicadores().add(new SgIndicador("Femenino", "MATRICULA", it.getCantMatFemenino()));
            }

            for (SgConsultaMatriculasSeccionResponseSAT it : repetidores) {
                sechash.computeIfAbsent(it.getSeccionPk(), s -> new SgSeccionIndicadores(it.getSeccionPk())).getIndicadores().add(new SgIndicador("Masculino", "REPITENCIA", it.getCantMatMasculino()));
                sechash.computeIfAbsent(it.getSeccionPk(), s -> new SgSeccionIndicadores(it.getSeccionPk())).getIndicadores().add(new SgIndicador("Femenino", "REPITENCIA", it.getCantMatFemenino()));
            }

            for (SgConsultaMatriculasSeccionResponseSAT it : sobreedad) {
                sechash.computeIfAbsent(it.getSeccionPk(), s -> new SgSeccionIndicadores(it.getSeccionPk())).getIndicadores().add(new SgIndicador("Masculino", "SOBREEDAD", it.getCantMatMasculino()));
                sechash.computeIfAbsent(it.getSeccionPk(), s -> new SgSeccionIndicadores(it.getSeccionPk())).getIndicadores().add(new SgIndicador("Femenino", "SOBREEDAD", it.getCantMatFemenino()));
            }

            return new ArrayList<>(sechash.values());

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }

    public List<SgSeccionIndicadoresNota> obtenerIndicadoresNotaCentroEducativo(String codigoCentro, Integer anio, String codigoPeriodo) throws GeneralException {
        try {
            if (StringUtils.isBlank(codigoCentro)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CODIGO_VACIO);
                throw be;
            }
            if (anio == null) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_ANIO_VACIO);
                throw be;
            }
            if (StringUtils.isBlank(codigoPeriodo)) {
                BusinessException be = new BusinessException();
                be.addError(Errores.ERROR_CODIGO_VACIO);
                throw be;
            }

            String query = " select sed.sed_codigo," //0
                    + " sec.sec_pk," //1
                    + " rf.rfe_codigo," //2
                    + " ale.ale_anio," //3
                    + " cpe.cpe_pk," //4
                    + " cpe.cpe_tipo," //5
                    + " cpe.cpe_nombre, " //6
                    + " cabezal.cal_promedio_calificaciones, " //7
                    + " cabezal.cal_promedio_calificaciones_masculino, "//8
                    + " cabezal.cal_promedio_calificaciones_femenino, " //9
                    + " (select c.cal_codigo from catalogo.sg_calificaciones c where c.cal_pk = cabezal.cal_moda_calificaciones_conceptuales) as moda_conceptual,\n" //10
                    + " (select c.cal_codigo from catalogo.sg_calificaciones c where c.cal_pk = cabezal.cal_moda_calificaciones_conceptuales_masculino) as moda_conceptual_masculino,\n" //11
                    + " (select c.cal_codigo from catalogo.sg_calificaciones c where c.cal_pk = cabezal.cal_moda_calificaciones_conceptuales_femenino) as moda_conceptual_femenino\n" //12
                    + " 	from centros_educativos.sg_calificaciones cabezal\n"
                    + "			INNER JOIN centros_educativos.sg_componente_plan_estudio cpe ON (cpe.cpe_pk = cabezal.cal_componente_plan_estudio_fk) \n"
                    + "                 INNER JOIN centros_educativos.sg_secciones sec ON (cabezal.cal_seccion_fk = sec.sec_pk)\n"
                    + "			INNER JOIN centros_educativos.sg_anio_lectivo ale ON (ale.ale_pk = sec.sec_anio_lectivo_fk)\n"
                    + "                 INNER JOIN centros_educativos.sg_servicio_educativo sdu ON (sec.sec_servicio_educativo_fk = sdu.sdu_pk)\n"
                    + "		    	INNER JOIN centros_educativos.sg_sedes sed ON (sed.sed_pk = sdu.sdu_sede_fk)\n"
                    + "                 INNER JOIN centros_educativos.sg_rango_fecha rf ON (cabezal.cal_rango_fecha_fk = rf.rfe_pk)\n"
                    + "		where cabezal.cal_tipo_periodo_calificacion = 'ORD'\n"
                    + "			and sed.sed_codigo = :sedCodigo "
                    + "                 and rf.rfe_codigo = :codigoPeriodo "
                    + "                 and ale.ale_anio = :anio "
                    + "		group by sed.sed_pk, sec.sec_pk, rf.rfe_pk, ale.ale_pk, cpe.cpe_pk, cabezal.cal_pk;\n";

            List<Object[]> result = em.createNativeQuery(query)
                    .setParameter("sedCodigo", codigoCentro)
                    .setParameter("anio", anio)
                    .setParameter("codigoPeriodo", codigoPeriodo)
                    .getResultList();

            HashMap<Long, SgSeccionIndicadoresNota> sechash = new HashMap<>();

            for (Object[] ob : result) {

                Long secPk = ((BigInteger) ob[1]).longValue();

                SgSeccionIndicadoresNota ind = null;

                if (sechash.containsKey(secPk)) {
                    ind = sechash.get(secPk);
                } else {
                    ind = new SgSeccionIndicadoresNota(secPk, (String) ob[0], (Integer) ob[3], (String) ob[2]);
                    sechash.put(secPk, ind);
                }

                SgIndicadorNota in = new SgIndicadorNota();
                in.setComponenteId(((BigInteger) ob[4]).longValue());
                in.setComponenteNombre((String) ob[6]);
                in.setComponenteTipo((String) ob[5]);
                in.setModa((String) ob[11]);
                in.setPromedio(ob[8] != null ? ((BigDecimal) ob[8]).doubleValue() : null);
                in.setSexo("Masculino");
                ind.getIndicadores().add(in);

                in = new SgIndicadorNota();
                in.setComponenteId(((BigInteger) ob[4]).longValue());
                in.setComponenteNombre((String) ob[6]);
                in.setComponenteTipo((String) ob[5]);
                in.setModa((String) ob[12]);
                in.setPromedio(ob[9] != null ? ((BigDecimal) ob[9]).doubleValue() : null);
                in.setSexo("Femenino");
                ind.getIndicadores().add(in);

            }

            return new ArrayList<>(sechash.values());

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

    }
    
    /**
     * Devuelve el tipo de oae de una sede
     *
     * @param idSede Long
     * @throws GeneralException
     */
    public List<SgCentroEducativo> obtenerTipoOaePorSede(Long idSede) throws GeneralException {
        if (idSede != null) {
            try {
                String consulta = "select c from SgCentroEducativo c  INNER JOIN SgTipoOrganismoAdministrativo t ON (c.cedTipoOrganismoAdministrativo = t.toaPk) where c.sedPk = :sedePk";
            
                TypedQuery<SgCentroEducativo> query = this.em.createQuery(consulta,SgCentroEducativo.class);
                query.setParameter("sedePk", idSede);


                return query.getResultList();
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        else{
            return null;
        }
    }

    /**
     * Activa y habilita una sede revocada
     *
     * @param req SgRevocacionSedeRequest
     * @throws GeneralException
     */
    public void activar(Long sedPk) throws GeneralException {
        try {

            BusinessException be = new BusinessException();
            if (sedPk == null) {
                be.addError("sedePk", Errores.ERROR_SEDE_VACIO);
                throw be;
            }

            SgSede sede = em.find(SgSede.class, sedPk);
            sede.setSedNumeroTramiteRevocatoria(null);
            sede.setSedHabilitado(Boolean.TRUE);
            sede.setSedActivo(Boolean.TRUE);
            sede.setSedFechaCierre(null);
            sede.setSedObservacionCierre(null);
            sede.setSedNumeroActaCierre(null);
            sede.setSedMotivoCierre(null);

            if (sede instanceof SgCentroEducativo) {
                SgCentroEducativo centro = (SgCentroEducativo) sede;
                centro.setCedLegalizado(Boolean.TRUE);
            }

            //Guardado automático por hibernate al salir de la transacción
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Revocar sede
     *
     * @param req SgRevocacionSedeRequest
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void revocar(SgRevocacionSedeRequest req) throws GeneralException {
        try {

            BusinessException be = new BusinessException();
            if (req.getSedePk() == null) {
                be.addError("sedePk", Errores.ERROR_SEDE_VACIO);
            }
            if (StringUtils.isBlank(req.getNumTramite())) {
                be.addError("mumTramite", Errores.ERROR_NUMERO_TRAMITE_VACIO);
            } else if (req.getNumTramite().length() > 255) {
                be.addError("mumTramite", Errores.ERROR_LARGO_NUMERO_TRAMITE_255);
            }

            if (req.getMotivoCierrePk() == null) {
                be.addError("motivoCierrePk", Errores.ERROR_MOTIVO_VACIO);
            }

            if (req.getNumAcuerdo() != null && req.getNumAcuerdo().length() > 255) {
                be.addError("numAcuerdo", Errores.ERROR_LARGO_NUMERO_ACTA_255);
            }

            if (req.getObservacion() != null && req.getObservacion().length() > 255) {
                be.addError("observacion", Errores.ERROR_LARGO_OBSERVACIONES_255);
            }

            if (req.getFechaCierre() == null) {
                be.addError("fechaCierre", Errores.ERROR_FECHA_VACIO);

            }

            if (be.getErrores().size() > 0) {
                throw be;
            }

            //Controlar sede no tenga matrículas abiertas
            FiltroMatricula fm = new FiltroMatricula();
            fm.setMatEstado(EnumMatriculaEstado.ABIERTO);
            fm.setSecSedeFk(req.getSedePk());
            Long totalMatAbiertas = matriculaBean.obtenerTotalPorFiltro(fm);
            if (totalMatAbiertas > 0) {
                be.addError(Errores.ERROR_SEDE_TIENE_MATRICULAS_ABIERTAS);
                throw be;
            }

            FiltroDatoContratacion filtroCont = new FiltroDatoContratacion();
            filtroCont.setDcoCentroEducativo(req.getSedePk());
            filtroCont.setDcoFecha(LocalDate.now());
            Long totaldatosCont = datoContratacionBean.obtenerTotalPorFiltro(filtroCont);
            if (totaldatosCont > 0) {
                be.addError(Errores.ERROR_SEDE_TIENE_DATOS_CONTRATACION_ACTIVOS);
                throw be;
            }

            FiltroOrganismoAdministrativoEscolar filtroOrg = new FiltroOrganismoAdministrativoEscolar();
            filtroOrg.setOaeSedeFk(req.getSedePk());
            filtroOrg.setOaeFechaVencimientoDesde(LocalDate.now());
            filtroOrg.setOaeEstados(Arrays.asList(new EnumEstadoOrganismoAdministrativo[]{
                EnumEstadoOrganismoAdministrativo.AMPLIAR_DATOS,
                EnumEstadoOrganismoAdministrativo.APROBADO,
                EnumEstadoOrganismoAdministrativo.ELABORACION,
                EnumEstadoOrganismoAdministrativo.ENVIADO,}));
            Long totalOrganismosNoCerrados = organismoAdministracionEscolarBean.obtenerTotalPorFiltro(filtroOrg);
            if (totalOrganismosNoCerrados > 0) {
                be.addError(Errores.ERROR_SEDE_TIENE_ORGANISMOS_ADMINISTRACION_ESCOLAR_NO_CERRADOS);
                throw be;
            }

            Long cantCuentas = ((BigInteger) em.createNativeQuery("Select count(*) from finanzas.sg_cuentas_bancarias where cbc_habilitado and cbc_sede_fk = :sedPk")
                    .setParameter("sedPk", req.getSedePk())
                    .getSingleResult()).longValue();

            if (cantCuentas > 0) {
                be.addError(Errores.ERROR_SEDE_TIENE_CUENTAS_BANCARIAS_ACTIVAS);
                throw be;
            }

            SgSede sede = em.find(SgSede.class, req.getSedePk());
            sede.setSedNumeroTramiteRevocatoria(req.getNumTramite());
            sede.setSedHabilitado(Boolean.FALSE);
            sede.setSedActivo(Boolean.FALSE);
            sede.setSedFechaCierre(req.getFechaCierre());
            sede.setSedObservacionCierre(req.getObservacion());
            sede.setSedNumeroActaCierre(req.getNumAcuerdo());
            sede.setSedMotivoCierre(em.getReference(SgMotivoCierreSede.class, req.getMotivoCierrePk()));

            if (sede instanceof SgCentroEducativo) {
                SgCentroEducativo centro = (SgCentroEducativo) sede;
                centro.setCedLegalizado(Boolean.FALSE);
            }

            FiltroTipoAcuerdo filtroTipoAcuerdo = new FiltroTipoAcuerdo();
            filtroTipoAcuerdo.setHabilitado(Boolean.TRUE);
            filtroTipoAcuerdo.setTaoTramiteRevocatoriaSede(Boolean.TRUE);
            List<SgTipoAcuerdo> tiposAcuerdo = catalogoBean.buscarTipoAcuerdo(filtroTipoAcuerdo);

            if (!tiposAcuerdo.isEmpty()) {
                String usuario = Lookup.obtenerJWT().getSubject();

                if (usuario != null && !StringUtils.isBlank(usuario)) {
                    FiltroUsuario fu = new FiltroUsuario();
                    fu.setCodigo(usuario);
                    List<SgUsuario> list=seguridadBean.obtenerUsuarios(fu);
                    if(list!=null && !list.isEmpty()){
                        usuario=list.get(0).getUsuCodigo()+" - "+list.get(0).getUsuNombre();
                    }
                }
                SgAcuerdoSede acuerdo = new SgAcuerdoSede();
                acuerdo.setAseOrigenExterno(Boolean.TRUE);
                acuerdo.setAseSede(sede);
                acuerdo.setAseTipoAcuerdo(tiposAcuerdo.get(0));
                acuerdo.setAseNumeroSolicitud(req.getNumTramite());
                acuerdo.setAseFechaRegistro(LocalDate.now());
                acuerdo.setAseNombreResponsable(usuario);
                acuerdo.setAseSoloLectura(Boolean.TRUE);
                acuerdo.setAseObservacion(req.getObservacion());
                acuerdo.setAseFechaGeneracion(req.getFechaGeneracion());
                acuerdo.setAseNumeroAcuerdo(req.getNumAcuerdo());
                acuerdo.setAseNumeroResolucion(req.getNumResolucion() );
                acuerdo.setAseFechaResolucion(req.getFechaResolucion());
                acuerdoSedeBean.guardar(acuerdo, Boolean.FALSE);
            }

            //Guardado sede automático por hibernate al salir de la transacción
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Cambiar domicilio sede
     *
     * @param req SgCambioDomicilioSedeRequest
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void cambiarDomicilio(SgCambioDomicilioSedeRequest req) throws GeneralException {
        try {

            BusinessException be = new BusinessException();
            if (req.getSedePk() == null) {
                be.addError(Errores.ERROR_SEDE_VACIO);
            }
            if (StringUtils.isBlank(req.getNumTramite())) {
                be.addError(Errores.ERROR_NUMERO_TRAMITE_VACIO);
            } else if (req.getNumTramite().length() > 255) {
                be.addError(Errores.ERROR_LARGO_NUMERO_TRAMITE_255);
            }

            if (StringUtils.isBlank(req.getDireccion())) {
                be.addError("direccion", Errores.ERROR_DIRECCION_VACIA);
            } else if (req.getDireccion().length() > 255) {
                be.addError("direccion", Errores.ERROR_LARGO_DIRECCION_255);
            }
            if (req.getDepartamentoPk() == null) {
                be.addError("departamentoPk", Errores.ERROR_DEPARTAMENTO_VACIO);
            }
            if (req.getMunicipioPk() == null) {
                be.addError("municipioPk", Errores.ERROR_MUNICIPIO_VACIO);
            }
            if (req.getZonaPk() == null) {
                be.addError("zonaPk", Errores.ERROR_ZONA_VACIO);
            }

            if (be.getErrores().size() > 0) {
                throw be;
            }

            SgSede sede = em.find(SgSede.class, req.getSedePk());
            sede.setSedNumeroTramiteCambioDomicilio(req.getNumTramite());

            SgDireccion dir = sede.getSedDireccion();

            dir.setDirDireccion(req.getDireccion());
            dir.setDirDepartamento(em.getReference(SgDepartamento.class, req.getDepartamentoPk()));
            dir.setDirMunicipio(em.getReference(SgMunicipio.class, req.getMunicipioPk()));
            dir.setDirZona(em.getReference(SgZona.class, req.getZonaPk()));
            if (req.getCantonPk() != null) {
                dir.setDirCanton(em.getReference(SgCanton.class, req.getCantonPk()));
            } else {
                dir.setDirCanton(null);
            }
            dir.setDirCaserioTexto(req.getCaserio());

            if (StringUtils.isNotBlank(req.getTelefono())) {
                sede.setSedTelefono(req.getTelefono());
            }

            FiltroTipoAcuerdo filtroTipoAcuerdo = new FiltroTipoAcuerdo();
            filtroTipoAcuerdo.setHabilitado(Boolean.TRUE);
            filtroTipoAcuerdo.setTaoTramiteCambioDomicilioSede(Boolean.TRUE);
            List<SgTipoAcuerdo> tiposAcuerdo = catalogoBean.buscarTipoAcuerdo(filtroTipoAcuerdo);

            if (!tiposAcuerdo.isEmpty()) {
                String usuario = Lookup.obtenerJWT().getSubject();

                if (usuario != null && !StringUtils.isBlank(usuario)) {
                    FiltroUsuario fu = new FiltroUsuario();
                    fu.setCodigo(usuario);
                    List<SgUsuario> list=seguridadBean.obtenerUsuarios(fu);
                    if(list!=null && !list.isEmpty()){
                        usuario=list.get(0).getUsuCodigo()+" - "+list.get(0).getUsuNombre();
                    }
                }
                
                SgAcuerdoSede acuerdo = new SgAcuerdoSede();
                acuerdo.setAseOrigenExterno(Boolean.TRUE);
                acuerdo.setAseSede(sede);
                acuerdo.setAseTipoAcuerdo(tiposAcuerdo.get(0));
                acuerdo.setAseNumeroSolicitud(req.getNumTramite());
                acuerdo.setAseFechaRegistro(LocalDate.now());
                acuerdo.setAseNombreResponsable(usuario);
                acuerdo.setAseSoloLectura(Boolean.TRUE);
                acuerdo.setAseObservacion(req.getObservacion());
                acuerdo.setAseFechaGeneracion(req.getFechaGeneracion());
                acuerdo.setAseNumeroAcuerdo(req.getNumAcuerdo());
                acuerdo.setAseNumeroResolucion(req.getNumResolucion() );
                acuerdo.setAseFechaResolucion(req.getFechaResolucion());
                acuerdoSedeBean.guardar(acuerdo, Boolean.FALSE);
            }

            //Guardado sede automático por hibernate al salir de la transacción
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Cambiar nominación de sede
     *
     * @param req SgCambioNominacionSedeRequest
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void cambiarNominacion(SgCambioNominacionSedeRequest req) throws GeneralException {
        try {

            BusinessException be = new BusinessException();
            if (req.getSedePk() == null) {
                be.addError(Errores.ERROR_SEDE_VACIO);
            }
            if (StringUtils.isBlank(req.getNumTramite())) {
                be.addError(Errores.ERROR_NUMERO_TRAMITE_VACIO);
            } else if (req.getNumTramite().length() > 255) {
                be.addError(Errores.ERROR_LARGO_NUMERO_TRAMITE_255);
            }

            if (StringUtils.isBlank(req.getNombre())) {
                be.addError("nombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (req.getNombre().length() > 500) {
                be.addError("nombre", Errores.ERROR_LARGO_NOMBRE_500);
            }

            if (be.getErrores().size() > 0) {
                throw be;
            }

            SgSede sede = em.find(SgSede.class, req.getSedePk());
            sede.setSedNumeroTramiteCambioNominacion(req.getNumTramite());
            sede.setSedNombre(req.getNombre());

            FiltroTipoAcuerdo filtroTipoAcuerdo = new FiltroTipoAcuerdo();
            filtroTipoAcuerdo.setHabilitado(Boolean.TRUE);
            filtroTipoAcuerdo.setTaoTramiteNominacionSede(Boolean.TRUE);
            List<SgTipoAcuerdo> tiposAcuerdo = catalogoBean.buscarTipoAcuerdo(filtroTipoAcuerdo);

            if (!tiposAcuerdo.isEmpty()) {
                String usuario = Lookup.obtenerJWT().getSubject();

                if (usuario != null && !StringUtils.isBlank(usuario)) {
                    FiltroUsuario fu = new FiltroUsuario();
                    fu.setCodigo(usuario);
                    List<SgUsuario> list=seguridadBean.obtenerUsuarios(fu);
                    if(list!=null && !list.isEmpty()){
                        usuario=list.get(0).getUsuCodigo()+" - "+list.get(0).getUsuNombre();
                    }
                }
                
                SgAcuerdoSede acuerdo = new SgAcuerdoSede();
                acuerdo.setAseOrigenExterno(Boolean.TRUE);
                acuerdo.setAseSede(sede);
                acuerdo.setAseTipoAcuerdo(tiposAcuerdo.get(0));
                acuerdo.setAseNumeroSolicitud(req.getNumTramite());
                acuerdo.setAseFechaRegistro(LocalDate.now());
                acuerdo.setAseNombreResponsable(usuario);
                acuerdo.setAseSoloLectura(Boolean.TRUE);
                acuerdo.setAseObservacion(req.getObservacion());
                acuerdo.setAseFechaGeneracion(req.getFechaGeneracion());
                acuerdo.setAseNumeroAcuerdo(req.getNumAcuerdo());
                acuerdo.setAseNumeroResolucion(req.getNumResolucion() );
                acuerdo.setAseFechaResolucion(req.getFechaResolucion());
                acuerdoSedeBean.guardar(acuerdo, Boolean.FALSE);
            }

            //Guardado sede automático por hibernate al salir de la transacción
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Registra nueva sede con servicios educativos
     *
     * @param req SgRegistroSedeRequest
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public void registroSede(SgRegistroSedeRequest req) throws GeneralException {
        try {

            BusinessException be = new BusinessException();
            if (StringUtils.isBlank(req.getNumTramite())) {
                be.addError("numTramite", Errores.ERROR_NUMERO_TRAMITE_VACIO);
            } else if (req.getNumTramite().length() > 255) {
                be.addError("numTramite", Errores.ERROR_LARGO_NUMERO_TRAMITE_255);
            }
            if (req.getSedTipo() == null) {
                be.addError("tipoSede", Errores.ERROR_TIPO_VACIO);
            }

            //El resto de validaciones se hacen en los métodos de guardado respectivos
            if (be.getErrores().size() > 0) {
                throw be;
            }

            JsonWebToken jwt = Lookup.obtenerJWT();

            SgSede sede = null;

            switch (req.getSedTipo()) {
                case CED_OFI:
                    if (!jwt.getGroups().contains(ConstantesOperaciones.CREAR_CENTRO_EDUCATIVO_OFICIAL)) {
                        be.addError(Errores.ERROR_SEGURIDAD);
                        throw be;
                    }
                    sede = new SgCentroEducativoOficial();
                    sede.setSedTipo(TipoSede.CED_OFI);
                    break;
                case CED_PRI:
                    if (!jwt.getGroups().contains(ConstantesOperaciones.CREAR_CENTRO_EDUCATIVO_PRIVADO)) {
                        be.addError(Errores.ERROR_SEGURIDAD);
                        throw be;
                    }
                    sede = new SgCentroEducativoPrivado();
                    sede.setSedTipo(TipoSede.CED_PRI);
                    break;
                case CIR_ALF:
                    if (!jwt.getGroups().contains(ConstantesOperaciones.CREAR_SEDE_CIRCULO_ALFABETIZACION)) {
                        be.addError(Errores.ERROR_SEGURIDAD);
                        throw be;
                    }
                    sede = new SgCirculoAlfabetizacion();
                    sede.setSedTipo(TipoSede.CIR_ALF);
                    break;
                case CIR_INF:
                    if (!jwt.getGroups().contains(ConstantesOperaciones.CREAR_SEDE_CIRCULO_INFANCIA)) {
                        be.addError(Errores.ERROR_SEGURIDAD);
                        throw be;
                    }
                    sede = new SgCirculoInfancia();
                    sede.setSedTipo(TipoSede.CIR_INF);
                    break;
                case UBI_EDUC:
                    if (!jwt.getGroups().contains(ConstantesOperaciones.CREAR_EDUCAME)) {
                        be.addError(Errores.ERROR_SEGURIDAD);
                        throw be;
                    }
                    sede = new SgSedeEducame();
                    sede.setSedTipo(TipoSede.UBI_EDUC);
                    break;

            }

            if (sede instanceof SgCentroEducativo) {
                SgCentroEducativo centro = (SgCentroEducativo) sede;
                centro.setCedLegalizado(Boolean.TRUE);
                centro.setCedFinesDeLucro(req.getSedFinesDeLucro());

                if (req.getSedTipoOrganismoAdministrativo() != null) {
                    centro.setCedTipoOrganismoAdministrativo(em.getReference(SgTipoOrganismoAdministrativo.class, req.getSedTipoOrganismoAdministrativo()));
                }
            }

            sede.setSedActivo(Boolean.TRUE);
            sede.setSedHabilitado(Boolean.TRUE);
            sede.setSedOrigenExterno(Boolean.TRUE);
            sede.setSedNumeroTramiteCreacion(req.getNumTramite());
            sede.setSedNombre(req.getSedNombre());
            sede.setSedCodigo(req.getSedCodigo());
            sede.setSedAnioInicioAct(req.getSedAnioInicioActividades());
            sede.setSedCorreoElectronico(req.getSedCorreoElectronico());
            sede.setSedPropietario(req.getSedPropietario());
            if (req.getSedTipoCalendario() != null) {
                sede.setSedTipoCalendario(em.getReference(SgTipoCalendarioEscolar.class, req.getSedTipoCalendario()));
            }

            sede.setSedDireccion(new SgDireccion());
            SgDireccion dir = sede.getSedDireccion();

            dir.setDirDireccion(req.getDirDireccion());
            if (req.getDirDepartamento() != null) {
                dir.setDirDepartamento(em.find(SgDepartamento.class, req.getDirDepartamento())); //Se utiliza find porque dataSecurity lo utiliza
            }
            if (req.getDirMunicipio() != null) {
                dir.setDirMunicipio(em.getReference(SgMunicipio.class, req.getDirMunicipio()));
            }
            if (req.getDirZona() != null) {
                dir.setDirZona(em.getReference(SgZona.class, req.getDirZona()));
            }
            if (req.getDirCanton() != null) {
                dir.setDirCanton(em.getReference(SgCanton.class, req.getDirCanton()));
            } else {
                dir.setDirCanton(null);
            }
            dir.setDirCaserioTexto(req.getDirCaserio());

            sede.setSedJornadasLaborales(new ArrayList<>());
            if (req.getSedJornadasLaborales() != null) {
                for (Long jornadaPk : req.getSedJornadasLaborales()) {
                    sede.getSedJornadasLaborales().add(em.getReference(SgJornadaLaboral.class, jornadaPk));
                }
            }

            switch (req.getSedTipo()) {
                case CED_OFI:
                    sede = centroOficialBean.guardar((SgCentroEducativoOficial) sede);
                    break;
                case CED_PRI:
                    sede = centroPrivadoBean.guardar((SgCentroEducativoPrivado) sede);
                    break;
                case CIR_ALF:
                    sede = circuloAlfBean.guardar((SgCirculoAlfabetizacion) sede);
                    break;
                case CIR_INF:
                    sede = circuloInfBean.guardar((SgCirculoInfancia) sede);
                    break;
                case UBI_EDUC:
                    sede = sedeEducameBean.guardar((SgSedeEducame) sede);
                    break;

            }

            // Generar servicios
            SgRegistroServiciosEducativos regServ = new SgRegistroServiciosEducativos();
            regServ.setNumTramite(req.getNumTramite());
            regServ.setSedePk(sede.getSedPk());
            regServ.setListadoServicios(req.getListadoServicios());
            servicioEducativoBean.habilitarServiciosEducativos(regServ, Boolean.FALSE);

            FiltroTipoAcuerdo filtroTipoAcuerdo = new FiltroTipoAcuerdo();
            filtroTipoAcuerdo.setHabilitado(Boolean.TRUE);
            filtroTipoAcuerdo.setTaoTramiteCreacionSede(Boolean.TRUE);
            List<SgTipoAcuerdo> tiposAcuerdo = catalogoBean.buscarTipoAcuerdo(filtroTipoAcuerdo);

            if (!tiposAcuerdo.isEmpty()) {
                String usuario = Lookup.obtenerJWT().getSubject();

                if (usuario != null && !StringUtils.isBlank(usuario)) {
                    FiltroUsuario fu = new FiltroUsuario();
                    fu.setCodigo(usuario);
                    List<SgUsuario> list=seguridadBean.obtenerUsuarios(fu);
                    if(list!=null && !list.isEmpty()){
                        usuario=list.get(0).getUsuCodigo()+" - "+list.get(0).getUsuNombre();
                    }
                }
                
                SgAcuerdoSede acuerdo = new SgAcuerdoSede();
                acuerdo.setAseOrigenExterno(Boolean.TRUE);
                acuerdo.setAseSede(sede);
                acuerdo.setAseTipoAcuerdo(tiposAcuerdo.get(0));
                acuerdo.setAseNumeroSolicitud(req.getNumTramite());
                acuerdo.setAseFechaRegistro(LocalDate.now());
                acuerdo.setAseNombreResponsable(usuario);
                acuerdo.setAseSoloLectura(Boolean.TRUE);
                acuerdo.setAseObservacion(req.getObservacion());
                acuerdo.setAseFechaGeneracion(req.getFechaGeneracion());
                acuerdo.setAseNumeroAcuerdo(req.getNumAcuerdo());
                acuerdo.setAseNumeroResolucion(req.getNumResolucion() );
                acuerdo.setAseFechaResolucion(req.getFechaResolucion());
                acuerdoSedeBean.guardar(acuerdo, Boolean.FALSE);
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }
    
    public void cerrarSede(SgSede sed){
        try{
            if(sed!=null && sed.getSedPk()!=null){
                if(SedesValidacionNegocio.validarCierre(sed)){
                    sed.setSedHabilitado(Boolean.FALSE);
                    sed.setSedActivo(Boolean.FALSE);
                    
                
                    CodigueraDAO<SgSede> dao = new CodigueraDAO<>(em, SgSede.class);
                    dao.guardar(sed,null);            
                    em.flush();
                    if(sed.getSedTipo().equals(TipoSede.CED_OFI) || sed.getSedTipo().equals(TipoSede.CED_PRI)){
                        Query q = em.createNativeQuery("update centros_educativos.sg_sedes_ced set ced_legalizado=false where sed_pk=:sed")
                                .setParameter("sed", sed.getSedPk());
                        q.executeUpdate();
                        
                    }
                }
            }
        
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }


}
