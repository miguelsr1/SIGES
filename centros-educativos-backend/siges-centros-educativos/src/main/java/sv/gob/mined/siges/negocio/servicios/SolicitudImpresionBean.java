/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.filtros.FiltroSolicitudImpresion;
import sv.gob.mined.siges.negocio.validaciones.SolicitudImpresionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.SolicitudImpresionDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteImpresion;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudImpresion;
import org.mariuszgromada.math.mxparser.Function;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumTipoSolicitudImpresion;
import sv.gob.mined.siges.filtros.FiltroAsistencia;
import sv.gob.mined.siges.filtros.FiltroComponentePlanEstudio;
import sv.gob.mined.siges.filtros.FiltroDatoContratacion;
import sv.gob.mined.siges.filtros.FiltroEstudianteImpresion;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.filtros.FiltroSeccion;
import sv.gob.mined.siges.filtros.FiltroSelloFirma;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAsistencia;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanEstudio;
import sv.gob.mined.siges.persistencia.entidades.SgDatoContratacion;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;
import sv.gob.mined.siges.persistencia.entidades.SgSelloFirma;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudImpresionLite;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class SolicitudImpresionBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private SeguridadBean seguridadBean;

    @Inject
    private CalificacionEstudianteBean calificacionEstudianteBean;

    @Inject
    private AsistenciaBean asistenciaBean;

    @Inject
    private ComponentesPlanEstudioBean componentePlanEstudioBean;

    @Inject
    private SeccionBean seccionBean;

    @Inject
    private MatriculaBean matriculaBean;
    @Inject
    private EstudianteImpresionBean estudianteImpresionBean;
    @Inject
    private SelloFirmaBean selloFirmaBean;

    @Inject
    private DatoContratacionBean datoContratacionBean;

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgSolicitudImpresion
     * @throws GeneralException
     */
    public SgSolicitudImpresion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgSolicitudImpresion> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresion.class);
                SgSolicitudImpresion sim = codDAO.obtenerPorId(id, null);

                return sim;
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
                CodigueraDAO<SgSolicitudImpresion> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresion.class);
                return codDAO.objetoExistePorId(id, ConstantesOperaciones.BUSCAR_SOLICITUD_IMPRESION);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param sim SgSolicitudImpresion
     * @return SgSolicitudImpresion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolicitudImpresion guardar(SgSolicitudImpresion sim) throws GeneralException {
        try {
            if (sim != null) {
                if (SolicitudImpresionValidacionNegocio.validar(sim)) {

                    if (sim.getSimEstado().equals(EnumEstadoSolicitudImpresion.ENVIADA) && BooleanUtils.isNotTrue(sim.getSimEvitarValidacionSelloFirma())) {
                        FiltroSelloFirma fsf = new FiltroSelloFirma();
                        fsf.setSfiHabilitado(Boolean.TRUE);
                        fsf.setArchivoVacio(Boolean.FALSE);
                        if (sim.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe() != null) {
                            List<Long> sedeList = new ArrayList();
                            sedeList.add(sim.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedPk());
                            sedeList.add(sim.getSimSeccion().getSecServicioEducativo().getSduSede().getSedPk());
                            fsf.setSfiSedes(sedeList);
                        } else {
                            fsf.setSedPk(sim.getSimSeccion().getSecServicioEducativo().getSduSede().getSedPk());
                        }
                        fsf.setSfiTipoRepresentanteCodigo(Constantes.TIPO_REPRESENTANTE_DIRECTOR);
                        fsf.setIncluirCampos(new String[]{"sfiPk", "sfiPersona.perPk", "sfiSede.sedPk", "sfiSede.sedTipo", "sfiSede.sedVersion",
                            "sfiPersona.perVersion"});
                        List<SgSelloFirma> listSelloFirma = selloFirmaBean.obtenerPorFiltro(fsf);
                        if (listSelloFirma.isEmpty()) {
                            BusinessException ge = new BusinessException();
                            ge.addError("simEstudianteImpresion", Errores.ERROR_SELLO_FIRMA_DIRECTOR_VACIO);
                            throw ge;
                        } else {
                            SgSelloFirma selloFirma = listSelloFirma.get(0);
                            if (sim.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe() != null) {
                                List<SgSelloFirma> listSedePadre = listSelloFirma.stream().filter(c -> c.getSfiSede().getSedPk().equals(sim.getSimSeccion().getSecServicioEducativo().getSduSede().getSedSedeAdscritaDe().getSedPk())).collect(Collectors.toList());
                                if (!listSedePadre.isEmpty()) {
                                    selloFirma = listSedePadre.get(0);
                                }
                            }

                            FiltroDatoContratacion filtro = new FiltroDatoContratacion();
                            filtro.setDcoCentroEducativo(selloFirma.getSfiSede().getSedPk());
                            filtro.setDcoPersonaPk(selloFirma.getSfiPersona().getPerPk());
                            filtro.setDcoFecha(LocalDate.now());
                            filtro.setIncluirCampos(new String[]{"dcoPk"});
                            filtro.setMaxResults(1L);
                            List<SgDatoContratacion> listdc = datoContratacionBean.obtenerPorFiltro(filtro);

                            if (listdc.isEmpty()) {
                                BusinessException ge = new BusinessException();
                                ge.addError("simEstudianteImpresion", Errores.ERROR_DIRECTOR_NO_TIENE_CONTRATO_VIGENTE);
                                throw ge;
                            }
                        }
                    }
                    CodigueraDAO<SgSolicitudImpresion> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresion.class);
                    return codDAO.guardar(sim, sim.getSimPk() == null ? ConstantesOperaciones.CREAR_SOLICITUD_IMPRESION : ConstantesOperaciones.ACTUALIZAR_SOLICITUD_IMPRESION);

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return sim;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param sim SgSolicitudImpresion
     * @return SgSolicitudImpresion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolicitudImpresion confirmar(SgSolicitudImpresion sim) throws GeneralException {
        try {
            if (sim != null) {
                if (SolicitudImpresionValidacionNegocio.validar(sim)) {

                    if ((sim.getSimTipoImpresion().equals(EnumTipoSolicitudImpresion.REP) ||(sim.getSimTipoImpresion().equals(EnumTipoSolicitudImpresion.SIM))) && BooleanUtils.isNotTrue(sim.getSimReposicionEstudianteSiges())) {
                        if (sim.getSimEstado().equals(EnumEstadoSolicitudImpresion.ENVIADA) && BooleanUtils.isTrue(sim.getSimReposicionTitulo().getRetAnulada())) {
                            sim.setSimEstado(EnumEstadoSolicitudImpresion.RECHAZADA);
                        } else {
                            sim.setSimEstado(EnumEstadoSolicitudImpresion.CONFIRMADA);
                            SolicitudImpresionValidacionNegocio.validar(sim);
                        }

                    } else {
                        if (sim.getSimPk() != null && sim.getSimEstado().equals(EnumEstadoSolicitudImpresion.ENVIADA)) {
                            sim.setSimEstado(EnumEstadoSolicitudImpresion.CONFIRMADA);
                            if (SolicitudImpresionValidacionNegocio.validar(sim)) {
                                List<SgEstudianteImpresion> estudianteImpresionAnulada = sim.getSimEstudianteImpresion().stream().filter(c -> BooleanUtils.isTrue(c.getEimAnulada())).collect(Collectors.toList());
                                if (!estudianteImpresionAnulada.isEmpty()) {
                                    sim.setSimEstado(EnumEstadoSolicitudImpresion.CON_EXCEPCIONES);
                                    if (estudianteImpresionAnulada.size() == sim.getSimEstudianteImpresion().size()) {
                                        sim.setSimEstado(EnumEstadoSolicitudImpresion.RECHAZADA);
                                    }
                                }
                            }

                        }

                    }

                    CodigueraDAO<SgSolicitudImpresion> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresion.class);
                    SgSolicitudImpresion sol = codDAO.guardar(sim, sim.getSimPk() == null ? ConstantesOperaciones.CREAR_SOLICITUD_IMPRESION : ConstantesOperaciones.ACTUALIZAR_SOLICITUD_IMPRESION);

                    return sol;

                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return sim;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param sim SgSolicitudImpresion
     * @return SgSolicitudImpresion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolicitudImpresionLite rechazar(SgSolicitudImpresionLite sim) throws GeneralException {
        try {
            if (sim != null) {
                sim.setSimEstado(EnumEstadoSolicitudImpresion.RECHAZADA);

                CodigueraDAO<SgSolicitudImpresionLite> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresionLite.class);
                return codDAO.guardar(sim, sim.getSimPk() == null ? ConstantesOperaciones.CREAR_SOLICITUD_IMPRESION : ConstantesOperaciones.ACTUALIZAR_SOLICITUD_IMPRESION);

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return sim;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param sim SgSolicitudImpresion
     * @return SgSolicitudImpresion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolicitudImpresionLite anular(SgSolicitudImpresionLite sim) throws GeneralException {
        try {
            if (sim != null && sim.getSimPk() != null) {
                if (EnumEstadoSolicitudImpresion.IMPRESA.equals(sim.getSimEstado())) {
                    sim.setSimEstado(EnumEstadoSolicitudImpresion.ANULADA);

                    Query est = em.createNativeQuery("update centros_educativos.sg_titulo set tit_anulado=true where tit_solicitud_impresion_fk=:solPk");
                    est.setParameter("solPk", sim.getSimPk());
                    est.executeUpdate();

                    CodigueraDAO<SgSolicitudImpresionLite> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresionLite.class);
                    return codDAO.guardar(sim, sim.getSimPk() == null ? ConstantesOperaciones.CREAR_SOLICITUD_IMPRESION : ConstantesOperaciones.ACTUALIZAR_SOLICITUD_IMPRESION);

                }

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return sim;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param solicitudesImpresion
     * @param solicitudesImpresionPk
     * @return SgSolicitudImpresion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgSolicitudImpresionLite cambioEstadoImpresa(SgSolicitudImpresion solicitudesImpresion) throws GeneralException {
        try {
            if (solicitudesImpresion.getSimPk() != null && EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION.equals(solicitudesImpresion.getSimEstado())) {

                CodigueraDAO<SgSolicitudImpresionLite> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresionLite.class);

                SgSolicitudImpresionLite solImpLite = new SgSolicitudImpresionLite();
                solImpLite.setSimPk(solicitudesImpresion.getSimPk());
                solImpLite.setSimEstado(EnumEstadoSolicitudImpresion.IMPRESA);
                solImpLite.setSimVersion(solicitudesImpresion.getSimVersion());
                solImpLite = codDAO.guardar(solImpLite, ConstantesOperaciones.ACTUALIZAR_SOLICITUD_IMPRESION);

                return solImpLite;

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return new SgSolicitudImpresionLite();
    }

    public List<SgEstudiante> obtenerEstudiantesHabilitados(FiltroSolicitudImpresion filtro) {

        if (filtro.getSimSeccion() == null) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_SECCION_VACIO);
            throw be;
        }

        if (filtro.getSimDefinicionTitulo() == null) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_DEFINICION_TITULO_VACIO);
            throw be;
        }

        String parteDefinicion = null;
        Function funcion = null;
        if (!StringUtils.isBlank(filtro.getFormula())) {

            funcion = new Function(filtro.getFormula());

            String[] formulaSeparada = filtro.getFormula().split("=", 2);
            parteDefinicion = formulaSeparada[0];
            parteDefinicion = parteDefinicion.trim();
        }

        FiltroSeccion fs = new FiltroSeccion();
        fs.setIncluirCampos(new String[]{"secServicioEducativo.sduGrado.graPk", "secPlanEstudio.pesPk"});
        fs.setSecPk(filtro.getSimSeccion());
        fs.setSecurityOperation(null);
        List<SgSeccion> secciones = seccionBean.obtenerPorFiltro(fs);
        if (secciones.isEmpty()) {
            BusinessException be = new BusinessException();
            be.addError(Errores.ERROR_SECCION_VACIO);
            throw be;
        }
        SgSeccion sec = secciones.get(0);

        List<SgEstudiante> estudianteList = new ArrayList();
        FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
        fce.setSeccionPk(filtro.getSimSeccion());
        fce.setCaeTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.GRA);
        fce.setCaePromovido(EnumPromovidoCalificacion.PROMOVIDO);
        fce.setIncluirCampos(new String[]{"caeEstudiante.estPk"});
        List<SgCalificacionEstudiante> listCalEst = calificacionEstudianteBean.obtenerPorFiltro(fce);

        if (!listCalEst.isEmpty()) {

            FiltroMatricula fm = new FiltroMatricula();
            fm.setMatEstudiantesPk(listCalEst.stream().map(c -> c.getCaeEstudiante().getEstPk()).distinct().collect(Collectors.toList()));
            fm.setSecPk(filtro.getSimSeccion());
            fm.setMatValidadaAcademicamente(Boolean.TRUE);
            fm.setIncluirCampos(new String[]{"matEstudiante.estPk", "matEstudiante.estPersona.perNie", "matEstudiante.estCantidadHorasServicioSocial",
                "matEstudiante.estPersona.perPrimerNombre", "matEstudiante.estPersona.perSegundoNombre", "matEstudiante.estPersona.perNombreBusqueda",
                "matEstudiante.estPersona.perPrimerApellido", "matEstudiante.estPersona.perSegundoApellido", "matEstudiante.estPersona.perFechaNacimiento",
                "matEstudiante.estPersona.perNombrePartidaNacimiento", "matEstudiante.estVersion"});

            List<SgMatricula> matEstudiantes = matriculaBean.obtenerPorFiltro(fm);

            List<SgEstudiante> listEstudiantePromovidos = matEstudiantes.stream().map(c -> c.getMatEstudiante()).distinct().collect(Collectors.toList());

            if (!listEstudiantePromovidos.isEmpty()) {
                List<SgCalificacionEstudiante> listCalEstPAES = new ArrayList();
                List<Long> listCpePk = new ArrayList();
                if (parteDefinicion != null && parteDefinicion.contains("realizadoPaes")) {
                    FiltroComponentePlanEstudio fpe = new FiltroComponentePlanEstudio();
                    fpe.setCpeEsPaes(Boolean.TRUE);
                    fpe.setCpePlanEstudio(sec.getSecPlanEstudio().getPesPk());
                    fpe.setIncluirCampos(new String[]{"cpePk"});
                    List<SgComponentePlanEstudio> listCpe = componentePlanEstudioBean.obtenerPorFiltro(fpe);
                    listCpePk = listCpe.stream().map(c -> c.getCpePk()).collect(Collectors.toList());

                    if (!listCpePk.isEmpty()) {
                        fce = new FiltroCalificacionEstudiante();
                        fce.setComponentePlanEstudioPk(listCpePk);
                        fce.setCaeEstudiantesPk(listEstudiantePromovidos.stream().map(c -> c.getEstPk()).collect(Collectors.toList()));
                        fce.setIncluirCampos(new String[]{"caeEstudiante.estPk", "caeCalificacion.calComponentePlanEstudio.cpePk"});
                        listCalEstPAES = calificacionEstudianteBean.obtenerPorFiltro(fce);
                    }
                }

                List<SgAsistencia> asistencias = new ArrayList();
                if (parteDefinicion != null && parteDefinicion.contains("asistencias")) {
                    FiltroAsistencia fa = new FiltroAsistencia();
                    fa.setCaeEstudiantesPk(listEstudiantePromovidos.stream().map(c -> c.getEstPk()).collect(Collectors.toList()));
                    fa.setAsiInasistencia(Boolean.FALSE);
                    fa.setAsiSeccion(filtro.getSimSeccion());
                    fa.setIncluirCampos(new String[]{"asiEstudiante.estPk"});

                    asistencias = asistenciaBean.obtenerPorFiltro(fa);
                }

                FiltroEstudianteImpresion fei = new FiltroEstudianteImpresion();
                fei.setEimEstudiante(listEstudiantePromovidos.stream().map(c -> c.getEstPk()).collect(Collectors.toList()));
                fei.setEimSeccion(filtro.getSimSeccion());
                fei.setEimDefinicionTitulo(filtro.getSimDefinicionTitulo());
                fei.setEimNoAnulada(Boolean.TRUE);
                List<EnumEstadoSolicitudImpresion> listEstados = new ArrayList();
                listEstados.add(EnumEstadoSolicitudImpresion.ENVIADA);
                listEstados.add(EnumEstadoSolicitudImpresion.CONFIRMADA);
                listEstados.add(EnumEstadoSolicitudImpresion.CON_EXCEPCIONES);
                listEstados.add(EnumEstadoSolicitudImpresion.PENDIENTE_IMPRESION);
                listEstados.add(EnumEstadoSolicitudImpresion.IMPRESA);
                fei.setEimEstadosSolicitud(listEstados);

                fei.setIncluirCampos(new String[]{"eimEstudiante.estPk"});
                List<SgEstudianteImpresion> estImp = estudianteImpresionBean.obtenerPorFiltro(fei);

                for (SgEstudiante est : listEstudiantePromovidos) {
                    List<SgEstudianteImpresion> estudianteConSolicitud = new ArrayList();
                    if (estImp.size() > 0) {
                        estudianteConSolicitud = estImp.stream().filter(c -> c.getEimEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList());
                    }
                    if (estudianteConSolicitud.isEmpty()) {
                        if (funcion != null) {
                            Expression expresion = new Expression(parteDefinicion, funcion);
                            Argument arg_hss = new Argument("hss=" + (est.getEstCantidadHorasServicioSocial() != null ? est.getEstCantidadHorasServicioSocial() : 0));
                            expresion.addArguments(arg_hss);
                            Argument arg_asis = new Argument("asistencias=" + asistencias.stream().filter(c -> c.getAsiEstudiante().getEstPk().equals(est.getEstPk())).collect(Collectors.toList()).size());
                            expresion.addArguments(arg_asis);
                            Integer realizadoPaes = 0;
                            List<Long> componentePaesCalificados = listCalEstPAES.stream().filter(c -> c.getCaeEstudiante().getEstPk().equals(est.getEstPk())).map(c -> c.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk()).distinct().collect(Collectors.toList());
                            if (!listCpePk.isEmpty() && (componentePaesCalificados.size() == listCpePk.size())) {
                                realizadoPaes = 1;
                            }
                            Argument arg_realizadoPaes = new Argument("realizadoPaes=" + realizadoPaes);
                            expresion.addArguments(arg_realizadoPaes);

                            Double res = expresion.calculate();
                            if (Double.compare(res, 0) != 0) {
                                estudianteList.add(est);
                            }
                        } else {
                            estudianteList.add(est);
                        }

                    }
                }

            }
        }
        return estudianteList;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Long obtenerTotalPorFiltro(FiltroSolicitudImpresion filtro) throws GeneralException {
        try {
            SolicitudImpresionDAO codDAO = new SolicitudImpresionDAO(em, seguridadBean);
            return codDAO.cantidadTotal(filtro, ConstantesOperaciones.BUSCAR_SOLICITUD_IMPRESION);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgSolicitudImpresion>
     * @throws GeneralException
     */
    public List<SgSolicitudImpresion> obtenerPorFiltro(FiltroSolicitudImpresion filtro) throws GeneralException {
        try {
            if(filtro.getIncluirCampos()!=null && filtro.getIncluirCampos().length>0){
                filtro.setIncluirCampos(ArrayUtils.add(filtro.getIncluirCampos(),"simTipoImpresion"));
            }
            
            SolicitudImpresionDAO codDAO = new SolicitudImpresionDAO(em, seguridadBean);
            List<SgSolicitudImpresion> solImp = codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_SOLICITUD_IMPRESION);

            if (BooleanUtils.isTrue(filtro.getSimCantidadEstudiantes())) {
                if (!solImp.isEmpty()) {
                    List<Long> solImpPk = solImp.stream().map(c -> c.getSimPk()).collect(Collectors.toList());
                    FiltroEstudianteImpresion fei = new FiltroEstudianteImpresion();
                    fei.setEimSolicitudesImpresion(solImpPk);
                    fei.setIncluirCampos(new String[]{"eimSolicitudImpresion.simPk"});
                    List<SgEstudianteImpresion> estImp = estudianteImpresionBean.obtenerPorFiltro(fei);
                    for (SgSolicitudImpresion sol : solImp) {
                        sol.setSimCantidadEstudianteImpresion(estImp.stream().filter(c -> c.getEimSolicitudImpresion().getSimPk().equals(sol.getSimPk())).collect(Collectors.toList()).size());
                        if (EnumTipoSolicitudImpresion.REP.equals(sol.getSimTipoImpresion()) || EnumTipoSolicitudImpresion.SIM.equals(sol.getSimTipoImpresion())) {
                            sol.setSimCantidadEstudianteImpresion(1);
                        }

                    }
                }
            }

            if (BooleanUtils.isTrue(filtro.getInicializarEstudianteImp())) {
                List<Long> solImpPk = solImp.stream().map(c -> c.getSimPk()).collect(Collectors.toList());
                FiltroEstudianteImpresion fsi = new FiltroEstudianteImpresion();
                fsi.setEimSolicitudesImpresion(solImpPk);
                fsi.setIncluirCampos(new String[]{"eimEstudiante.estPk",
                    "eimEstudiante.estVersion",
                    "eimEstudiante.estPersona.perPrimerNombre",
                    "eimEstudiante.estPersona.perSegundoNombre",
                    "eimEstudiante.estPersona.perTercerNombre",
                    "eimEstudiante.estPersona.perPrimerApellido",
                    "eimEstudiante.estPersona.perSegundoApellido",
                    "eimEstudiante.estPersona.perTercerApellido",
                    "eimEstudiante.estPersona.perNie",
                    "eimAnulada",
                    "eimVersion",
                    "eimEstudiante.estPersona.perNombrePartidaNacimiento",
                    "eimSolicitudImpresion.simPk",
                    "eimMotivoAnulacion.matPk",
                    "eimMotivoAnulacion.matNombre",
                    "eimMotivoAnulacion.matVersion"});
                List<SgEstudianteImpresion> estImp = estudianteImpresionBean.obtenerPorFiltro(fsi);

                for (SgSolicitudImpresion sol : solImp) {
                    sol.setSimEstudianteImpresion(estImp.stream().filter(c -> c.getEimSolicitudImpresion().getSimPk().equals(sol.getSimPk())).collect(Collectors.toList()));
                }

            }

            return solImp;
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
                CodigueraDAO<SgSolicitudImpresion> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresion.class);
                codDAO.eliminarPorId(id, ConstantesOperaciones.ELIMINAR_SELLO_FIRMA);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
