/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.Function;
import sv.gob.mined.siges.auditoria.AuditInterceptor;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.dto.SgDatosCalculoCalificaciones;
import sv.gob.mined.siges.dto.SgPeriodosOrdExord;
import sv.gob.mined.siges.dto.SgPorcentajeAsistenciasRequest;
import sv.gob.mined.siges.dto.SgPorcentajeAsistenciasResponse;
import sv.gob.mined.siges.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.enumerados.EnumEstadoHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.enumerados.EnumPromovidoCalificacion;
import sv.gob.mined.siges.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.enumerados.TipoComponentePlanEstudio;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.excepciones.GeneralException;
import sv.gob.mined.siges.excepciones.TechnicalException;
import sv.gob.mined.siges.filtros.FiltroCalificacion;
import sv.gob.mined.siges.filtros.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.filtros.FiltroComponentePlanGrado;
import sv.gob.mined.siges.filtros.FiltroEscolaridadEstudiante;
import sv.gob.mined.siges.filtros.FiltroEstudiante;
import sv.gob.mined.siges.filtros.FiltroEstudianteImpresion;
import sv.gob.mined.siges.filtros.FiltroHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.filtros.FiltroMatricula;
import sv.gob.mined.siges.filtros.FiltroPeriodoCalendario;
import sv.gob.mined.siges.filtros.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.filtros.FiltroRangoFecha;
import sv.gob.mined.siges.filtros.FiltroTitulo;
import sv.gob.mined.siges.filtros.catalogo.FiltroFormula;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.validaciones.HabilitacionPeriodoCalificacionValidacionNegocio;
import sv.gob.mined.siges.persistencia.daos.CodigueraDAO;
import sv.gob.mined.siges.persistencia.daos.HabilitacionPeriodoCalificacionDAO;
import sv.gob.mined.siges.persistencia.daos.MatriculaDAO;
import sv.gob.mined.siges.persistencia.daos.catalogo.FormulaDAO;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCE;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado;
import sv.gob.mined.siges.persistencia.entidades.SgEscolaridadEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteImpresion;
import sv.gob.mined.siges.persistencia.entidades.SgHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.persistencia.entidades.SgMatricula;
import sv.gob.mined.siges.persistencia.entidades.SgPeriodoCalendario;
import sv.gob.mined.siges.persistencia.entidades.SgPeriodoCalificacion;
import sv.gob.mined.siges.persistencia.entidades.SgRangoFecha;
import sv.gob.mined.siges.persistencia.entidades.SgRelPeriodosHabilitacionPeriodo;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudImpresionLite;
import sv.gob.mined.siges.persistencia.entidades.SgTitulo;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCalificacion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgFormula;

/**
 *
 * @author Sofis Solutions
 */
@Stateless
public class HabilitacionPeriodoCalificacionBean {

    @Inject
    private SeguridadBean seguridadBean;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ConsultaHistoricoBean ch;

    @Inject
    private EstudianteBean estudianteBean;

    @Inject
    private CalificacionEstudianteBean calificacionEstudianteBean;

    @Inject
    private EstudianteImpresionBean estudianteImpresionBean;

    @Inject
    private TituloBean tituloBean;

    @Inject
    private ComponentePlanGradoBean componentePlanGradoBean;

    @Inject
    private RangoFechaBean rangoFechaBean;

    @Inject
    private CalendarioEscolarBean calendarioEscolarBean;

    @Inject
    private MatriculaBean matriculaBean;

    @Inject
    private CalificacionBean calificacionBean;

    @Inject
    private PeriodoCalificacionBean periodoCalificacionBean;

    @Inject
    private RelPeriodosHabilitacionPeriodoBean relPeriodosHabilitacionPeriodoBean;

    @Inject
    private EscolaridadEstudianteBean escolaridadEstudianteBean;

    private String[] formulasAuxiliares = new String[]{"maximo(a,b)=if(isNaN(a),if(isNaN(b),0,b),if(isNaN(b),a,max(a,b)))", "maxTres(a,b,c)=maximo(a,maximo(b,c))"};

    /**
     * Devuelve el objeto del tipo que tiene el id indicado
     *
     * @param id Long
     * @return SgHabilitacionPeriodoCalificacion
     * @throws GeneralException
     */
    public SgHabilitacionPeriodoCalificacion obtenerPorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgHabilitacionPeriodoCalificacion> codDAO = new CodigueraDAO<>(em, SgHabilitacionPeriodoCalificacion.class);
                SgHabilitacionPeriodoCalificacion habPer = codDAO.obtenerPorId(id, null);
                if (habPer.getRelPeriodosHabilitacionPeriodo() != null) {
                    habPer.getRelPeriodosHabilitacionPeriodo().size();
                }
                if (habPer.getHpcEstudianteFk() != null) {
                    habPer.getHpcEstudianteFk().getEstPk();
                }
                if (habPer.getHpcMatriculaFk() != null) {
                    habPer.getHpcMatriculaFk().getMatPk();
                    habPer.getHpcMatriculaFk().getMatSeccion().getSecPk();
                }
                return habPer;
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
    public Boolean objetoExistePorId(Long id) throws GeneralException {
        if (id != null) {
            try {
                CodigueraDAO<SgHabilitacionPeriodoCalificacion> codDAO = new CodigueraDAO<>(em, SgHabilitacionPeriodoCalificacion.class);
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
     * @param hpc SgHabilitacionPeriodoCalificacion
     * @return SgHabilitacionPeriodoCalificacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgHabilitacionPeriodoCalificacion guardar(SgHabilitacionPeriodoCalificacion hpc) throws GeneralException {
        try {
            if (hpc != null) {
                if (hpc.getHpcPk() == null) {
                    hpc.setHpcEstado(EnumEstadoHabilitacionPeriodoCalificacion.PENDIENTE);
                    hpc.setHpcFechaSolicitud(LocalDate.now());
                }
                if (HabilitacionPeriodoCalificacionValidacionNegocio.validar(hpc)) {

                    CodigueraDAO<SgHabilitacionPeriodoCalificacion> codDAO = new CodigueraDAO<>(em, SgHabilitacionPeriodoCalificacion.class);
                    return codDAO.guardar(hpc, null);
                }
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return hpc;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param hpc SgHabilitacionPeriodoCalificacion
     * @return SgHabilitacionPeriodoCalificacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgHabilitacionPeriodoCalificacion aprobarSolicitud(SgHabilitacionPeriodoCalificacion hpc) throws GeneralException {
        try {
            if (hpc != null) {
                hpc.setHpcEstado(EnumEstadoHabilitacionPeriodoCalificacion.APROBADA);
                hpc = guardar(hpc);
                if (!hpc.getRelPeriodosHabilitacionPeriodo().isEmpty()) {
                    editarCalificacionPorSolicitud(hpc);
                }

                return hpc;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return hpc;
    }

    private void editarCalificacionPorSolicitud(SgHabilitacionPeriodoCalificacion entity) {
        try {
            if (entity != null) {

                List<SgComponentePlanGrado> componenteList = entity.getRelPeriodosHabilitacionPeriodo().stream().map(c -> c.getRphComponentePlanGradoFk()).distinct().collect(Collectors.toList());

                FiltroPeriodoCalificacion fpc = new FiltroPeriodoCalificacion();
                fpc.setPcaModalidadEducativa(entity.getHpcMatriculaFk().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModPk());
                fpc.setPcaModalidadAtencion(entity.getHpcMatriculaFk().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion().getMatPk());
                fpc.setPcaSubModalidadAtencion(entity.getHpcMatriculaFk().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null ? entity.getHpcMatriculaFk().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                fpc.setPcaAnioLectivo(entity.getHpcMatriculaFk().getMatSeccion().getSecAnioLectivo().getAlePk());
                fpc.setPcaTipoCalendario(entity.getHpcMatriculaFk().getMatSeccion().getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());

                //Se busca por periodo semestral o anual
                fpc.setPcaTipoPeriodo(entity.getHpcMatriculaFk().getMatSeccion().getSecTipoPeriodo());
                fpc.setPcaNumeroPeriodo(entity.getHpcMatriculaFk().getMatSeccion().getSecNumeroPeriodo());

                fpc.setIncluirCampos(new String[]{"pcaVersion", "pcaNumero"});

                List<SgPeriodoCalificacion> listPeriodoCalif = periodoCalificacionBean.obtenerPorFiltro(fpc);

                Boolean existeCalificacionEXORD = Boolean.FALSE;
                for (SgRelPeriodosHabilitacionPeriodo rel : entity.getRelPeriodosHabilitacionPeriodo()) {
                    if (rel.getRphTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.EXORD)) {
                        existeCalificacionEXORD = Boolean.TRUE;
                        break;
                    }
                }

                FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
                fce.setAnioLectivoPk(entity.getHpcMatriculaFk().getMatSeccion().getSecAnioLectivo().getAlePk());
                fce.setCaeEstudiantePk(entity.getHpcEstudianteFk().getEstPk());
                fce.setComponentePlanEstudioPk(componenteList.stream().map(c -> c.getCpgComponentePlanEstudio().getCpePk()).collect(Collectors.toList()));
                fce.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
                    EnumTiposPeriodosCalificaciones.ORD, EnumTiposPeriodosCalificaciones.EXORD,
                    EnumTiposPeriodosCalificaciones.NOTIN, EnumTiposPeriodosCalificaciones.APR}));
                //Filtra dependiendo si sección es anual o semestral
                fce.setCaeTipoPeriodo(entity.getHpcMatriculaFk().getMatSeccion().getSecTipoPeriodo());
                fce.setCaeNumeroPeriodo(entity.getHpcMatriculaFk().getMatSeccion().getSecNumeroPeriodo());
                
                fce.setIncluirCampos(new String[]{"caePk", "caeVersion",
                    "caeEstudiante.estPk", "caeEstudiante.estVersion",
                    "caeCalificacionNumericaEstudiante",
                    "caeCalificacionConceptualEstudiante.calPk",
                    "caeCalificacionConceptualEstudiante.calOrden",
                    "caeCalificacionConceptualEstudiante.calVersion",
                    "caeInfoProcesamientoCalificacionEstFk.ipePk",
                    "caeInfoProcesamientoCalificacionEstFk.ipeVersion",
                    "caeObservacion",
                    "caeResolucion",
                    "caePromovidoCalificacion",
                    "caeFechaRealizado",
                    "caeCalificacion.calPk",
                    "caeCalificacion.calVersion",
                    "caeCalificacion.calComponentePlanEstudio.cpePk",
                    "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                    "caeCalificacion.calComponentePlanEstudio.cpeVersion",
                    "caeCalificacion.calRangoFecha.rfePk",
                    "caeCalificacion.calRangoFecha.rfeVersion",
                    "caeCalificacion.calSeccion.secPk",
                    "caeCalificacion.calSeccion.secVersion",
                    "caeCalificacion.calFecha",
                    "caeCalificacion.calTipoPeriodoCalificacion",
                    "caeCalificacion.calTipoCalendarioEscolar",
                    "caeCalificacion.calNumero",
                    "caeCalificacion.calEstadoProcesamientoPromocion",
                    "caeCalificacion.calCerrado",
                    "caeCalificacion.calCantEstudiantesCalificados",
                    "caeCalificacion.calPromedioCalificaciones",
                    "caeCalificacion.calEstadoProcesamientoPromocion",
                    "caeCalificacion.calInfoProcesamientoCalificacionFk.ipcPk",
                    "caeCalificacion.calInfoProcesamientoCalificacionFk.ipcVersion"});
                List<SgCalificacionEstudiante> calEstOriginal = calificacionEstudianteBean.obtenerPorFiltro(fce);

                //Se busca cabezales para los casos que el cabezal existe pero no el registro de la calificación del estudiante
                FiltroCalificacion fc = new FiltroCalificacion();
                fc.setSecPk(entity.getHpcMatriculaFk().getMatSeccion().getSecPk());
                fc.setCalComponentePlanEstudioList(componenteList.stream().map(c -> c.getCpgComponentePlanEstudio().getCpePk()).collect(Collectors.toList()));
                fc.setCalTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
                    EnumTiposPeriodosCalificaciones.ORD, EnumTiposPeriodosCalificaciones.EXORD,
                    EnumTiposPeriodosCalificaciones.NOTIN, EnumTiposPeriodosCalificaciones.APR}));
                //Filtra dependiendo si sección es anual o semestral
                fc.setCaeTipoPeriodo(entity.getHpcMatriculaFk().getMatSeccion().getSecTipoPeriodo());
                fc.setCaeNumeroPeriodo(entity.getHpcMatriculaFk().getMatSeccion().getSecNumeroPeriodo());
                fc.setIncluirCampos(new String[]{
                    "calComponentePlanEstudio.cpePk",
                    "calComponentePlanEstudio.cpeTipo",
                    "calComponentePlanEstudio.cpeVersion",
                    "calRangoFecha.rfePk",
                    "calRangoFecha.rfeVersion",
                    "calTipoPeriodoCalificacion",
                    "calTipoCalendarioEscolar",
                    "calNumero",
                    "calVersion"});

                List<SgCalificacionCE> calOriginalList = calificacionBean.obtenerPorFiltro(fc);

                HashMap<SgComponentePlanGrado, List<SgCalificacionEstudiante>> componenteCalificacionesEstudiante = new HashMap<>();
                HashMap<SgComponentePlanGrado, List<SgCalificacionCE>> componenteCalificaciones = new HashMap<>();

                for (SgComponentePlanGrado cpg : componenteList) {
                    if (!componenteCalificacionesEstudiante.containsKey(cpg)) {
                        componenteCalificacionesEstudiante.put(cpg, calEstOriginal.stream().filter(c -> c.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk().equals(cpg.getCpgComponentePlanEstudio().getCpePk())).collect(Collectors.toList()));
                    }
                    if (!componenteCalificaciones.containsKey(cpg)) {
                        componenteCalificaciones.put(cpg, calOriginalList.stream().filter(c -> c.getCalComponentePlanEstudio().getCpePk().equals(cpg.getCpgComponentePlanEstudio().getCpePk())).collect(Collectors.toList()));
                    }
                }

                for (SgComponentePlanGrado cpg : componenteList) {
                    List<SgCalificacionEstudiante> calificacionesOrdDeCpg = componenteCalificacionesEstudiante.get(cpg).stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.ORD)).collect(Collectors.toList());

                    List<SgRelPeriodosHabilitacionPeriodo> relPeriodoOrdinario = entity.getRelPeriodosHabilitacionPeriodo().stream().filter(c -> c.getRphComponentePlanGradoFk().equals(cpg) && c.getRphTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.ORD)).collect(Collectors.toList());

                    //ACTUALIZO CALIFICACIONES ORDINARIAS POR COMPONENTE
                    if (!relPeriodoOrdinario.isEmpty()) {
                        for (SgRelPeriodosHabilitacionPeriodo rep : relPeriodoOrdinario) {
                            List<SgCalificacionEstudiante> calEstudianteEditar = calificacionesOrdDeCpg.stream()
                                    .filter(c -> c.getCaeCalificacion().getCalComponentePlanEstudio().equals(rep.getRphComponentePlanGradoFk().getCpgComponentePlanEstudio())
                                    && c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(rep.getRphTipoPeriodoCalificacion())
                                    && (c.getCaeCalificacion().getCalRangoFecha().getRfePk().equals(rep.getRphRangoFechaFk().getRfePk()))).collect(Collectors.toList());

                            if (calEstudianteEditar.isEmpty()) {

                                List<SgCalificacionCE> calElem = calOriginalList.stream()
                                        .filter(c -> c.getCalComponentePlanEstudio().equals(rep.getRphComponentePlanGradoFk().getCpgComponentePlanEstudio())
                                        && c.getCalTipoPeriodoCalificacion().equals(rep.getRphTipoPeriodoCalificacion())
                                        && ((c.getCalRangoFecha() != null ? c.getCalRangoFecha().getRfePk().equals(rep.getRphRangoFechaFk().getRfePk()) : Boolean.FALSE)
                                        || ((c.getCalTipoCalendarioEscolar() != null ? c.getCalTipoCalendarioEscolar().equals(rep.getRphTipoCalendarioEscolar()) : Boolean.FALSE)
                                        && (c.getCalNumero() != null ? c.getCalNumero().equals(rep.getRphNumeroExtraordinario()) : Boolean.FALSE)))).collect(Collectors.toList());

                                SgCalificacionEstudiante calEst = new SgCalificacionEstudiante();
                                calEst.setCaeCalificacionConceptualEstudiante(rep.getRphCalificacionConceptual());
                                calEst.setCaeCalificacionNumericaEstudiante(rep.getRphCalificacionNumerica());
                                calEst.setCaeEstudiante(entity.getHpcEstudianteFk());

                                SgCalificacionCE ca = new SgCalificacionCE();
                                if (!calElem.isEmpty()) {
                                    ca = calElem.get(0);
                                    calEst.setCaeCalificacion(ca);
                                    
                                    CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                    calEst = codDAO.guardar(calEst, null);
                                    this.calificacionBean.marcarPromedioYModaDesactualizados(calEst.getCaeCalificacion().getCalPk());
                                } else {
                                    ca.setCalComponentePlanEstudio(rep.getRphComponentePlanGradoFk().getCpgComponentePlanEstudio());
                                    ca.setCalRangoFecha(rep.getRphRangoFechaFk());
                                    ca.setCalSeccion(entity.getHpcMatriculaFk().getMatSeccion());
                                    ca.setCalTipoPeriodoCalificacion(rep.getRphTipoPeriodoCalificacion());
                                    ca.setCalCalificacionesEstudiante(new ArrayList());
                                    calEst.setCaeCalificacion(ca);

                                    ca.getCalCalificacionesEstudiante().add(calEst);
                                    calificacionBean.guardar(ca, Boolean.FALSE,Boolean.FALSE);
                                }
                                componenteCalificacionesEstudiante.get(cpg).add(calEst);

                            } else {
                                if (calEstudianteEditar.size() == 1) {
                                    SgCalificacionEstudiante calEst = calEstudianteEditar.get(0);
                                    componenteCalificacionesEstudiante.get(cpg).remove(calEst);
                                    if (!StringUtils.isBlank(rep.getRphCalificacionNumerica())) {
                                        calEst.setCaeCalificacionNumericaEstudiante(rep.getRphCalificacionNumerica());
                                        CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                        calEst = codDAO.guardar(calEst, null);
                                        componenteCalificacionesEstudiante.get(cpg).add(calEst);
                                        this.calificacionBean.marcarPromedioYModaDesactualizados(calEst.getCaeCalificacion().getCalPk());
                                    } else {
                                        if (rep.getRphCalificacionConceptual() != null) {
                                            calEst.setCaeCalificacionConceptualEstudiante(rep.getRphCalificacionConceptual());
                                            CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                            calEst = codDAO.guardar(calEst, null);
                                            componenteCalificacionesEstudiante.get(cpg).add(calEst);
                                            this.calificacionBean.marcarPromedioYModaDesactualizados(calEst.getCaeCalificacion().getCalPk());
                                        }
                                    }
                                }
                            }
                        }

                        //2-ACTUALIZO CALIFICACIONES NOTAS INSTITUCIONALES POR COMPONENTE
                        if (existeCalificacionEXORD) {
                            
                            //El calculo de NOTIN se hace a esta altura solo cuando hay notas extraordinarias para crear (la formula de habilitacion depende de notin)
                            //En otro caso se hace al final
                            Object resultadoObjeto = calificacionBean.calcularNotaInstitucionalPorEstudiante(entity.getHpcEstudianteFk(), cpg, componenteCalificacionesEstudiante.get(cpg).stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.ORD)).collect(Collectors.toList()));

                            List<SgCalificacionEstudiante> calificacionNotaIns = componenteCalificacionesEstudiante.get(cpg).stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)).collect(Collectors.toList());

                            SgCalificacionEstudiante calNOTIN = new SgCalificacionEstudiante();
                            if (calificacionNotaIns.isEmpty()) {
                                List<SgCalificacionCE> calCabezalNOTINList = componenteCalificaciones.get(cpg).stream().filter(c -> c.getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)).collect(Collectors.toList());

                                calNOTIN.setCaeCalificacionConceptualEstudiante(resultadoObjeto instanceof SgCalificacion ? (SgCalificacion) resultadoObjeto : null);
                                calNOTIN.setCaeCalificacionNumericaEstudiante(resultadoObjeto instanceof String ? (String) resultadoObjeto : null);
                                calNOTIN.setCaeEstudiante(entity.getHpcEstudianteFk());

                                SgCalificacionCE calCabezalNOTIN = new SgCalificacionCE();
                                if (!calCabezalNOTINList.isEmpty()) {
                                    calCabezalNOTIN = calCabezalNOTINList.get(0);
                                    calNOTIN.setCaeCalificacion(calCabezalNOTIN);

                                    CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                    calNOTIN = codDAO.guardar(calNOTIN, null);
                                    componenteCalificacionesEstudiante.get(cpg).add(calNOTIN);
                                } else {
                                    calCabezalNOTIN.setCalComponentePlanEstudio(cpg.getCpgComponentePlanEstudio());
                                    calCabezalNOTIN.setCalSeccion(entity.getHpcMatriculaFk().getMatSeccion());
                                    calCabezalNOTIN.setCalTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.NOTIN);
                                    calCabezalNOTIN.setCalCalificacionesEstudiante(new ArrayList());

                                    calCabezalNOTIN = calificacionBean.guardar(calCabezalNOTIN, Boolean.FALSE,Boolean.FALSE);
                                    calNOTIN.setCaeCalificacion(calCabezalNOTIN);

                                    CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                    calNOTIN = codDAO.guardar(calNOTIN, null);
                                    componenteCalificacionesEstudiante.get(cpg).add(calNOTIN);
                                }

                            } else {
                                calNOTIN = calificacionNotaIns.get(0);
                                componenteCalificacionesEstudiante.get(cpg).remove(calNOTIN);
                                calNOTIN.setCaeCalificacionConceptualEstudiante(resultadoObjeto instanceof SgCalificacion ? (SgCalificacion) resultadoObjeto : null);
                                calNOTIN.setCaeCalificacionNumericaEstudiante(resultadoObjeto instanceof String ? (String) resultadoObjeto : null);
                                CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                calNOTIN = codDAO.guardar(calNOTIN, null);
                                componenteCalificacionesEstudiante.get(cpg).add(calNOTIN);
                            }
                        }
                    }
                }
                em.flush();
                //3-CALIFICACIONES EXTRAORDINARIAS

                FiltroCalificacionEstudiante fcePRUEBA = new FiltroCalificacionEstudiante();
                fcePRUEBA.setAnioLectivoPk(entity.getHpcMatriculaFk().getMatSeccion().getSecAnioLectivo().getAlePk());
                fcePRUEBA.setCaeEstudiantePk(entity.getHpcEstudianteFk().getEstPk());
                fcePRUEBA.setComponentePlanEstudioPk(componenteList.stream().map(c -> c.getCpgComponentePlanEstudio().getCpePk()).collect(Collectors.toList()));
                fcePRUEBA.setCaeTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
                    EnumTiposPeriodosCalificaciones.NOTIN}));
                //Filtra dependiendo si sección es anual o semestral
                fcePRUEBA.setCaeTipoPeriodo(entity.getHpcMatriculaFk().getMatSeccion().getSecTipoPeriodo());
                fcePRUEBA.setCaeNumeroPeriodo(entity.getHpcMatriculaFk().getMatSeccion().getSecNumeroPeriodo());
                fcePRUEBA.setCpeTipo(TipoComponentePlanEstudio.PRU);
                fcePRUEBA.setIncluirCampos(new String[]{"caePk", "caeVersion",
                    "caeCalificacionNumericaEstudiante",
                    "caeCalificacionConceptualEstudiante.calPk",
                    "caeCalificacionConceptualEstudiante.calOrden",
                    "caeCalificacionConceptualEstudiante.calVersion",
                    "caeCalificacion.calComponentePlanEstudio.cpePk",
                    "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                    "caeCalificacion.calComponentePlanEstudio.cpeVersion"});
                List<SgCalificacionEstudiante> calEstPRUEBAS = calificacionEstudianteBean.obtenerPorFiltro(fcePRUEBA);

                List<SgComponentePlanGrado> componenteListEXORD = entity.getRelPeriodosHabilitacionPeriodo().stream().filter(c -> c.getRphTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.EXORD)).map(c -> c.getRphComponentePlanGradoFk()).distinct().collect(Collectors.toList());

                for (SgComponentePlanGrado comp : componenteListEXORD) {

                    List<SgCalificacionEstudiante> calificacionesExordDeCpg = componenteCalificacionesEstudiante.get(comp).stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.EXORD)).collect(Collectors.toList());

                    SgPorcentajeAsistenciasRequest datosHabilitacionEXORD = new SgPorcentajeAsistenciasRequest();
                    List<SgEstudiante> est = new ArrayList();
                    est.add(entity.getHpcEstudianteFk());
                    datosHabilitacionEXORD.setPinEstudiantes(est);
                    datosHabilitacionEXORD.setComponentePlanEstudio(comp.getCpgComponentePlanEstudio().getCpePk());
                    datosHabilitacionEXORD.setPinSeccion(entity.getHpcMatriculaFk().getMatSeccion().getSecPk());
                    datosHabilitacionEXORD.setComponentePlanGrado(comp);
                    List<SgPorcentajeAsistenciasResponse> respuestaDatosHabilitacionEXORDList = calificacionBean.obtenerDatosHabilitacionPeriodoExtraordinario(datosHabilitacionEXORD);

                    SgPorcentajeAsistenciasResponse respuestaDatosHabilitacionEXORD = respuestaDatosHabilitacionEXORDList.get(0);

                    List<SgRelPeriodosHabilitacionPeriodo> relComponenteEXORD = entity.getRelPeriodosHabilitacionPeriodo().stream().filter(c -> c.getRphTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.EXORD) && comp.equals(c.getRphComponentePlanGradoFk())).collect(Collectors.toList());

                    for (SgRelPeriodosHabilitacionPeriodo rel : relComponenteEXORD) {
                        SgFormula formulaHabilitaPExtraordinario = new SgFormula();
                        SgComponentePlanGrado parametroFormulaHabilitaPExtraordinario = null;

                        switch (rel.getRphTipoCalendarioEscolar()) {
                            case PREC:
                                formulaHabilitaPExtraordinario = comp.getCpgFormulaHabilitacionPP();
                                parametroFormulaHabilitaPExtraordinario = comp.getCpgParametroFormulaPruebaPP();
                                break;
                            case PRECPS:
                                formulaHabilitaPExtraordinario = comp.getCpgFormulaHabilitacionPS();
                                parametroFormulaHabilitaPExtraordinario = comp.getCpgParametroFormulaPruebaPS();
                                break;
                            case SREC:
                                formulaHabilitaPExtraordinario = comp.getCpgFormulaHabilitacionSP();
                                parametroFormulaHabilitaPExtraordinario = comp.getCpgParametroFormulaPruebaSP();
                                break;
                            case SRECPS:
                                formulaHabilitaPExtraordinario = comp.getCpgFormulaHabilitacionPP();
                                parametroFormulaHabilitaPExtraordinario = comp.getCpgParametroFormulaPruebaSS();
                                break;
                            default:
                        }

                        if (parametroFormulaHabilitaPExtraordinario != null) {
                            String calificacionPrueba = null;
                            for (SgCalificacionEstudiante calPrueba : calEstPRUEBAS) {
                                if (calPrueba.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk().equals(parametroFormulaHabilitaPExtraordinario.getCpgComponentePlanEstudio().getCpePk())) {
                                    calificacionPrueba = calPrueba.getCaeCalificacionNumericaEstudiante();
                                    break;
                                }

                            }
                            respuestaDatosHabilitacionEXORD.setPinNotaInstitucionalPrueba(calificacionPrueba);
                        }

                        //Se cambia NOTIN ya que la misma pudo ser modificada
                        List<SgCalificacionEstudiante> notin = componenteCalificacionesEstudiante.get(comp).stream().filter(c -> c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(EnumTiposPeriodosCalificaciones.NOTIN)).collect(Collectors.toList());
                        if (!notin.isEmpty()) {
                            respuestaDatosHabilitacionEXORD.setPinNotaInstitucional(notin.get(0).getCaeCalificacionNumericaEstudiante());
                        }

                        Boolean habilitadoCalificarEXORD = this.habilitacionEstudiantePeriodoExtraordinario(formulaHabilitaPExtraordinario, respuestaDatosHabilitacionEXORD, comp);

                        if (habilitadoCalificarEXORD) {
                            List<SgCalificacionEstudiante> calEstudianteEditar = calificacionesExordDeCpg.stream()
                                    .filter(c -> c.getCaeCalificacion().getCalComponentePlanEstudio().equals(rel.getRphComponentePlanGradoFk().getCpgComponentePlanEstudio())
                                    && c.getCaeCalificacion().getCalTipoPeriodoCalificacion().equals(rel.getRphTipoPeriodoCalificacion())
                                    && (c.getCaeCalificacion().getCalTipoCalendarioEscolar().equals(rel.getRphTipoCalendarioEscolar()))
                                    && (c.getCaeCalificacion().getCalNumero().equals(rel.getRphNumeroExtraordinario()))).collect(Collectors.toList());

                            if (calEstudianteEditar.isEmpty()) {

                                List<SgCalificacionCE> calElem = calOriginalList.stream()
                                        .filter(c -> c.getCalComponentePlanEstudio().equals(rel.getRphComponentePlanGradoFk().getCpgComponentePlanEstudio())
                                        && c.getCalTipoPeriodoCalificacion().equals(rel.getRphTipoPeriodoCalificacion())
                                        && ((c.getCalRangoFecha() != null ? c.getCalRangoFecha().getRfePk().equals(rel.getRphRangoFechaFk().getRfePk()) : Boolean.FALSE)
                                        || ((c.getCalTipoCalendarioEscolar() != null ? c.getCalTipoCalendarioEscolar().equals(rel.getRphTipoCalendarioEscolar()) : Boolean.FALSE)
                                        && (c.getCalNumero() != null ? c.getCalNumero().equals(rel.getRphNumeroExtraordinario()) : Boolean.FALSE)))).collect(Collectors.toList());

                                SgCalificacionEstudiante calEst = new SgCalificacionEstudiante();
                                calEst.setCaeCalificacionConceptualEstudiante(rel.getRphCalificacionConceptual());
                                calEst.setCaeCalificacionNumericaEstudiante(rel.getRphCalificacionNumerica());
                                calEst.setCaeEstudiante(entity.getHpcEstudianteFk());

                                SgCalificacionCE ca = new SgCalificacionCE();
                                if (!calElem.isEmpty()) {
                                    ca = calElem.get(0);
                                    calEst.setCaeCalificacion(ca);
                                    CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                    calEst = codDAO.guardar(calEst, null);
                                } else {
                                    ca.setCalComponentePlanEstudio(rel.getRphComponentePlanGradoFk().getCpgComponentePlanEstudio());
                                    ca.setCalNumero(rel.getRphNumeroExtraordinario());
                                    ca.setCalSeccion(entity.getHpcMatriculaFk().getMatSeccion());
                                    ca.setCalTipoCalendarioEscolar(rel.getRphTipoCalendarioEscolar());
                                    ca.setCalTipoPeriodoCalificacion(rel.getRphTipoPeriodoCalificacion());
                                    ca.setCalCalificacionesEstudiante(new ArrayList());
                                    calEst.setCaeCalificacion(ca);

                                    ca.getCalCalificacionesEstudiante().add(calEst);
                                    calificacionBean.guardar(ca, Boolean.FALSE,Boolean.FALSE);
                                }
                                componenteCalificacionesEstudiante.get(comp).add(calEst);

                            } else {
                                if (calEstudianteEditar.size() == 1) {
                                    SgCalificacionEstudiante calEst = calEstudianteEditar.get(0);
                                    componenteCalificacionesEstudiante.get(comp).remove(calEst);
                                    if (!StringUtils.isBlank(rel.getRphCalificacionNumerica())) {
                                        calEst.setCaeCalificacionNumericaEstudiante(rel.getRphCalificacionNumerica());
                                        CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                        calEst = codDAO.guardar(calEst, null);
                                        componenteCalificacionesEstudiante.get(comp).add(calEst);
                                    } else {
                                        if (rel.getRphCalificacionConceptual() != null) {
                                            calEst.setCaeCalificacionConceptualEstudiante(rel.getRphCalificacionConceptual());
                                            CodigueraDAO<SgCalificacionEstudiante> codDAO = new CodigueraDAO<>(em, SgCalificacionEstudiante.class);
                                            calEst = codDAO.guardar(calEst, null);
                                            componenteCalificacionesEstudiante.get(comp).add(calEst);
                                        }
                                    }
                                }
                            }

                        } else {
                            rel.setRphInfoProcesamiento("Estudiante no habilitado en período extraordinario.");
                            relPeriodosHabilitacionPeriodoBean.guardar(rel);
                        }

                    }
                }

                //4-PROMOCION
                //Se busca cabezales para los casos que el cabezal existe pero no el registro de la calificación del estudiante
                FiltroCalificacion fcGRA = new FiltroCalificacion();
                fcGRA.setSecPk(entity.getHpcMatriculaFk().getMatSeccion().getSecPk());
                fcGRA.setCalTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
                    EnumTiposPeriodosCalificaciones.GRA}));
                //Filtra dependiendo si sección es anual o semestral
                fcGRA.setCaeTipoPeriodo(entity.getHpcMatriculaFk().getMatSeccion().getSecTipoPeriodo());
                fcGRA.setCaeNumeroPeriodo(entity.getHpcMatriculaFk().getMatSeccion().getSecNumeroPeriodo());
                fcGRA.setIncluirCampos(new String[]{
                    "calFecha",
                    "calUltModFecha",
                    "calUltModUsuario",
                    "calVersion",
                    "calTipoPeriodoCalificacion",
                    "calCerrado",
                    "calSeccion.secAnioLectivo.aleAnio",
                    "calSeccion.secAnioLectivo.alePk",
                    "calSeccion.secAnioLectivo.aleVersion",
                    "calSeccion.secPlanEstudio.pesPk",
                    "calSeccion.secPlanEstudio.pesNombre",
                    "calSeccion.secPlanEstudio.pesVersion",
                    "calSeccion.secServicioEducativo.sduGrado.graPk",
                    "calSeccion.secServicioEducativo.sduGrado.graNombre",
                    "calSeccion.secServicioEducativo.sduOpcion.opcNombre",
                    "calSeccion.secServicioEducativo.sduOpcion.opcPk",
                    "calSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
                    "calSeccion.secServicioEducativo.sduProgramaEducativo.pedPk",
                    "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk",
                    "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk",
                    "calSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk",
                    "calSeccion.secServicioEducativo.sduSede.sedPk",
                    "calSeccion.secServicioEducativo.sduSede.sedTipo",
                    "calSeccion.secServicioEducativo.sduSede.sedTipoCalendario.tcePk",
                    "calSeccion.secNombre",
                    "calSeccion.secPk",
                    "calSeccion.secTipoPeriodo",
                    "calSeccion.secNumeroPeriodo",
                    "calSeccion.secVersion",
                    "calEstadoProcesamientoPromocion",
                    "calInfoProcesamientoCalificacionFk.ipcPk",
                    "calInfoProcesamientoCalificacionFk.ipcError",
                    "calInfoProcesamientoCalificacionFk.ipcVersion"});

                List<SgCalificacionCE> calGRAList = calificacionBean.obtenerPorFiltro(fcGRA);

                if (!calGRAList.isEmpty()) {
                    //EN CASO QUE EXISTA PROMOCION SE ACTUALIZA DESDE PROMOCION
                    SgCalificacionCE calGra=calificacionBean.calcularPromocion(calGRAList.get(0), entity.getHpcEstudianteFk().getEstPk());
                    

                    FiltroEscolaridadEstudiante fee = new FiltroEscolaridadEstudiante();
                    fee.setEstudiantePk(entity.getHpcEstudianteFk().getEstPk());
                    fee.setServicioEducativoPk(entity.getHpcMatriculaFk().getMatSeccion().getSecServicioEducativo().getSduPk());
                    fee.setAnioPk(entity.getHpcMatriculaFk().getMatSeccion().getSecAnioLectivo().getAlePk());
                    fee.setIncluirCampos(new String[]{"escEstudiante.estPk", "escEstudiante.estVersion",
                        "escServicioEducativo.sduPk",
                        "escServicioEducativo.sduVersion",
                        "escAnio.alePk",
                        "escAnio.aleVersion",
                        "escResultado",
                        "escAsistencias",
                        "escInasistencias",
                        "escVersion",
                        "escMatriculaFk.matPk",
                        "escMatriculaFk.matVersion",
                        "escCreadoCierre",
                        "escGeneradaPorEquivalencia",
                        "escNombreSede"
                    });
                    List<SgEscolaridadEstudiante> escList = escolaridadEstudianteBean.obtenerPorFiltro(fee);
                    if (!escList.isEmpty()) {
                        List<SgCalificacionEstudiante> calEstGRA = calGra.getCalCalificacionesEstudiante().stream().filter(c->c.getCaeEstudiante().getEstPk().equals( entity.getHpcEstudianteFk().getEstPk())).collect(Collectors.toList());

                        if(!calEstGRA.isEmpty()){
                            SgEscolaridadEstudiante esc=escList.get(0);
                            if(calEstGRA.get(0).getCaePromovidoCalificacion()!=null && !esc.getEscResultado().equals(calEstGRA.get(0).getCaePromovidoCalificacion()) && !calEstGRA.get(0).getCaePromovidoCalificacion().equals(EnumPromovidoCalificacion.PENDIENTE)){
                               
                                esc.setEscResultado(calEstGRA.get(0).getCaePromovidoCalificacion());
                                escolaridadEstudianteBean.guardar(esc);
                               
                            }
                        }
                    }

                } else {
                    //EN OTRO CASO SE SE ACTUALIZAN LAS NOTAS DE APROBACION E INSITUCIONALES
                    for (SgComponentePlanGrado cpg : componenteList) {
                        Optional<SgPeriodoCalificacion> periodo = listPeriodoCalif.stream().filter(p -> p.getPcaNumero().equals(cpg.getCpgPeriodosCalificacion())).findAny();
                        if (periodo.isPresent()) {
                            SgDatosCalculoCalificaciones calculoCalificaciones = new SgDatosCalculoCalificaciones();
                            calculoCalificaciones.setComponentePlanGrado(cpg);
                            calculoCalificaciones.setPeriodoCalificacionPk(periodo.get().getPcaPk());
                            calculoCalificaciones.setSeccion(entity.getHpcMatriculaFk().getMatSeccion());
                            calculoCalificaciones.setEstudiantePk(entity.getHpcEstudianteFk().getEstPk());
                            calificacionBean.calcularNotaAprobacion(calculoCalificaciones);
                        }

                    }
                }

                //5-ANULAR TITULO O ANULAR SOLICITUD
                FiltroTitulo ft = new FiltroTitulo();
                ft.setTitNoAnulada(Boolean.TRUE);
                ft.setTitEstudiante(entity.getHpcEstudianteFk().getEstPk());
                ft.setTitSeccionFk(entity.getHpcMatriculaFk().getMatSeccion().getSecPk());
                ft.setIncluirCampos(new String[]{"titEstudianteFk.estPk",
                    "titEstudianteFk.estVersion",
                    "titNombreEstudiante",
                    "titNombreEstudianteBusqueda",
                    "titNombreEstudiantePartida",
                    "titNombreCertificado",
                    "titFechaValidez",
                    "titFechaEmision",
                    "titSelloFirmaDirectorFk.sfiPk",
                    "titSelloFirmaDirectorFk.sfiVersion",
                    "titSelloFirmaTitularMinistroFk.sftPk",
                    "titSelloFirmaTitularMinistroFk.sftVersion",
                    "titSelloFirmaTitularAutenticaFk.sftPk",
                    "titSelloFirmaTitularAutenticaFk.sftVersion",
                    "titNombreDirector",
                    "titNombreMinistro",
                    "titNombreTitular",
                    "titAnio",
                    "titSedeFk.sedPk",
                    "titSedeFk.sedVersion",
                    "titSedeCodigo",
                    "titSedeNombre",
                    "titServicioEducativoFk.sduPk",
                    "titServicioEducativoFk.sduVersion",
                    "titSolicitudImpresionFk.simPk",
                    "titSolicitudImpresionFk.simVersion",
                    "titUsuarioEnviaImprimir",
                    "titVersion",
                    "titAnulada",
                    "titMotivoReimpresion.mriPk",
                    "titMotivoReimpresion.mriVersion",
                    "titDuiEstudiante",
                    "titFechaLegalizacionTitulo",
                    "titTituloAnterior2008",
                    "titNumeroResolucion",
                    "titNumeroRegistro",
                    "titReposicion",
                    "titSeccionFk.secPk",
                    "titSeccionFk.secVersion"
                });
                List<SgTitulo> listTitulo = tituloBean.obtenerPorFiltro(ft);
                if (!listTitulo.isEmpty()) {
                    SgTitulo tit = listTitulo.get(0);
                    tit.setTitAnulada(Boolean.TRUE);
                    tituloBean.guardar(tit);
                } else {
                    FiltroEstudianteImpresion fei = new FiltroEstudianteImpresion();
                    fei.setEimEstudiantePk(entity.getHpcEstudianteFk().getEstPk());
                    fei.setEimNoAnulada(Boolean.TRUE);
                    fei.setEimSeccion(entity.getHpcMatriculaFk().getMatSeccion().getSecPk());
                    List<EnumEstadoSolicitudImpresion> listEstado = new ArrayList<>();
                    listEstado.add(EnumEstadoSolicitudImpresion.ENVIADA);
                    listEstado.add(EnumEstadoSolicitudImpresion.CON_EXCEPCIONES);
                    listEstado.add(EnumEstadoSolicitudImpresion.CONFIRMADA);
                    fei.setEimEstadosSolicitud(listEstado);
                    fei.setIncluirCampos(new String[]{"eimVersion",
                        "eimSolicitudImpresion.simPk",
                        "eimSolicitudImpresion.simVersion"
                    });
                    List<SgEstudianteImpresion> solIMpList = estudianteImpresionBean.obtenerPorFiltro(fei);

                    if (!solIMpList.isEmpty()) {
                        SgSolicitudImpresionLite solIm = new SgSolicitudImpresionLite();
                        solIm.setSimPk(solIMpList.get(0).getEimSolicitudImpresion().getSimPk());
                        solIm.setSimVersion(solIMpList.get(0).getEimSolicitudImpresion().getSimVersion());
                        solIm.setSimEstado(EnumEstadoSolicitudImpresion.ANULADA);
                        CodigueraDAO<SgSolicitudImpresionLite> codDAO = new CodigueraDAO<>(em, SgSolicitudImpresionLite.class);
                        codDAO.guardar(solIm, null);

                    }

                }

            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public Boolean habilitacionEstudiantePeriodoExtraordinario(SgFormula formulaHabilitaPExtraordinario, SgPorcentajeAsistenciasResponse datoHabilitacion, SgComponentePlanGrado componentePlanGrado) {
        try {
            if (formulaHabilitaPExtraordinario != null) {
                if (formulaHabilitaPExtraordinario.getFomPk() != null && !StringUtils.isBlank(formulaHabilitaPExtraordinario.getFomTextoLargo())) {
                    Function funcion = new Function(formulaHabilitaPExtraordinario.getFomTextoLargo());

                    if (BooleanUtils.isTrue(formulaHabilitaPExtraordinario.getFomTienSubformula())) {

                        FiltroFormula ff = new FiltroFormula();
                        ff.setIncluirSubformulas(Boolean.TRUE);
                        ff.setFormulaPk(formulaHabilitaPExtraordinario.getFomPk());
                        FormulaDAO codDAO = new FormulaDAO(em);
                        List<SgFormula> listForm = codDAO.obtenerPorFiltro(ff);
                        if (BooleanUtils.isTrue(ff.getIncluirSubformulas())) {
                            List<SgFormula> formulas = new ArrayList(listForm);
                            for (SgFormula form : formulas) {
                                Boolean descartarFormula = inicializarSubformulas(form, ff.getDescartarSubFormulasPk());
                                if (descartarFormula) {
                                    listForm.remove(form);
                                }
                            }
                        }

                        formulaHabilitaPExtraordinario = listForm.get(0);
                        funcion = cargarSubformula(formulaHabilitaPExtraordinario);
                    }

                    Function formulaAx0 = new Function(formulasAuxiliares[0]);
                    Function formulaAx1 = new Function(formulasAuxiliares[1]);
                    formulaAx1.addFunctions(formulaAx0);
                    funcion.addDefinitions(formulaAx0);
                    funcion.addDefinitions(formulaAx1);

                    String parteDefinicion;

                    String[] formulaSeparada = formulaHabilitaPExtraordinario.getFomTextoLargo().split("=", 2);
                    parteDefinicion = formulaSeparada[0];
                    parteDefinicion = parteDefinicion.trim();
                    Boolean errorEnFormula = Boolean.FALSE;

                    if (BooleanUtils.isFalse(datoHabilitacion.getPinEstudianteConTodosLosPeriodosCalificados())) {
                        return Boolean.FALSE;
                    }

                    Expression expresion = new Expression(parteDefinicion, funcion);
                    Argument arg_asis = new Argument("asistencias=" + datoHabilitacion.getPinCantidadAsistencias());
                    expresion.addArguments(arg_asis);
                    Argument arg_notaAsis = new Argument("ni=" + datoHabilitacion.getPinNotaInstitucional());
                    expresion.addArguments(arg_notaAsis);
                    Argument arg_cantidad = new Argument("cantidadAsignReprobadas=" + datoHabilitacion.getPinCantidadNoAprobado());
                    expresion.addArguments(arg_cantidad);
                    Argument arg_nota_eval = new Argument("NOTAEVAL=" + datoHabilitacion.getPinNotaInstitucionalPrueba());
                    expresion.addArguments(arg_nota_eval);
                    Argument arg_mayor_nota = new Argument("mayorNota=" + datoHabilitacion.getPinMayorNota());
                    expresion.addArguments(arg_mayor_nota);

                    Argument arg_ppe = new Argument("ppe", Double.NaN);
                    expresion.addArguments(arg_ppe);
                    if (datoHabilitacion.getPinPpe() != null) {
                        expresion.setArgumentValue("ppe", datoHabilitacion.getPinPpe());
                    }
                    Argument arg_ppeps = new Argument("ppeps", Double.NaN);
                    expresion.addArguments(arg_ppeps);
                    if (datoHabilitacion.getPinPpeps() != null) {
                        expresion.setArgumentValue("ppeps", datoHabilitacion.getPinPpeps());
                    }
                    Argument arg_spe = new Argument("spe", Double.NaN);
                    expresion.addArguments(arg_spe);
                    if (datoHabilitacion.getPinSpe() != null) {
                        expresion.setArgumentValue("spe", datoHabilitacion.getPinSpe());
                    }
                    Argument arg_sppe = new Argument("sppe", Double.NaN);
                    expresion.addArguments(arg_sppe);
                    if (datoHabilitacion.getPinSppe() != null) {
                        expresion.setArgumentValue("sppe", datoHabilitacion.getPinSppe());
                    }

                    if (componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimoAprobacion() != null) {
                        Argument arg_speps = new Argument("ma=" + componentePlanGrado.getCpgEscalaCalificacion().getEcaMinimoAprobacion().toString());
                        expresion.addArguments(arg_speps);
                    }

                    Double res = expresion.calculate();

                    if (Double.compare(res, 0) == 0 || Double.isNaN(res)) {
                        return Boolean.FALSE;
                    } else {
                        return Boolean.TRUE;
                    }

                }
            }

        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
        return Boolean.FALSE;
    }

    private Function cargarSubformula(SgFormula formula) {
        Function f = new Function(formula.getFomTextoLargo());
        if (formula.getFomTienSubformula()) {
            for (SgFormula fr : formula.getFomSubFormula()) {
                f.addDefinitions(cargarSubformula(fr));
            }
        }
        return f;
    }

    private Boolean inicializarSubformulas(SgFormula form, Long descartarSub) {
        if (descartarSub != null) {
            if (form.getFomPk().equals(descartarSub)) {
                return Boolean.TRUE;
            }

        }
        Boolean descartar=Boolean.FALSE;
        if (form.getFomTienSubformula()) {
            if(form.getFomSubFormula()!=null){
                form.getFomSubFormula().size();
                for (SgFormula subform : form.getFomSubFormula()) {
                   if(BooleanUtils.isTrue(inicializarSubformulas(subform, descartarSub))){
                        descartar= Boolean.TRUE;
                    }
                }
            }
        }
        return descartar;
    }

    /**
     * Guarda el objeto indicado
     *
     * @param hpc SgHabilitacionPeriodoCalificacion
     * @return SgHabilitacionPeriodoCalificacion
     * @throws GeneralException
     */
    @Interceptors(AuditInterceptor.class)
    public SgHabilitacionPeriodoCalificacion rechazarSolicitud(SgHabilitacionPeriodoCalificacion hpc) throws GeneralException {
        try {
            if (hpc != null) {
                hpc.setHpcEstado(EnumEstadoHabilitacionPeriodoCalificacion.RECHAZADA);
                return guardar(hpc);
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }

        return hpc;
    }

    /**
     * Devuelve la cantidad total de elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Long
     * @throws GeneralException
     */
    public Long obtenerTotalPorFiltro(FiltroHabilitacionPeriodoCalificacion filtro) throws GeneralException {
        try {
            HabilitacionPeriodoCalificacionDAO codDAO = new HabilitacionPeriodoCalificacionDAO(em, seguridadBean);
            return codDAO.obtenerTotalPorFiltro(filtro, ConstantesOperaciones.BUSCAR_SOLICITUD_CORRECCION_CALIFICACION);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Devuelve los elementos que satisfacen el criterio
     *
     * @param filtro FiltroCodiguera
     * @return Lista de <SgHabilitacionPeriodoCalificacion>
     * @throws GeneralException
     */
    public List<SgHabilitacionPeriodoCalificacion> obtenerPorFiltro(FiltroHabilitacionPeriodoCalificacion filtro) throws GeneralException {
        try {
            HabilitacionPeriodoCalificacionDAO codDAO = new HabilitacionPeriodoCalificacionDAO(em, seguridadBean);
            return codDAO.obtenerPorFiltro(filtro, ConstantesOperaciones.BUSCAR_SOLICITUD_CORRECCION_CALIFICACION);
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public List<SgMatricula> buscarEstudiante(FiltroEstudiante fe) throws Exception {
        try {

            if (fe.getNie() != null) {
                
                FiltroEstudiante fest=new FiltroEstudiante();
                fest.setNie(fe.getNie());
                fest.setSecurityOperation(ConstantesOperaciones.BUSCAR_ESTUDIANTES);
                fest.setIncluirCampos(new String[]{"estPk"});
                List<SgEstudiante> estList=estudianteBean.obtenerPorFiltro(fest);
                if(estList.isEmpty()){
                    BusinessException ge = new BusinessException();
                    ge.addError(Errores.ERROR_ESTUDIANTE_NO_EXISTE);
                    throw ge;
                }

                FiltroMatricula fm = new FiltroMatricula();
                fm.setNie(fe.getNie());
                fm.setMatCerradaCierreAnioOAbierta(Boolean.TRUE);
                fm.setSecurityOperation(ConstantesOperaciones.BUSCAR_MATRICULA);
                fm.setAscending(new boolean[]{true, true});                      
                fm.setOrderBy(new String[]{"matFechaIngreso", "matPk"});
                fm.setIncluirCampos(new String[]{"matVersion",
                    "matEstado",
                    "matSeccion.secPk",
                    "matSeccion.secVersion",
                    "matSeccion.secPlanEstudio.pesPk",
                    "matSeccion.secPlanEstudio.pesVersion",
                    "matSeccion.secAnioLectivo.alePk",
                    "matSeccion.secAnioLectivo.aleAnio",
                    "matSeccion.secAnioLectivo.aleVersion",
                    "matSeccion.secServicioEducativo.sduPk",
                    "matSeccion.secServicioEducativo.sduVersion",
                    "matSeccion.secServicioEducativo.sduGrado.graPk",
                    "matSeccion.secServicioEducativo.sduGrado.graVersion",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk",
                    "matSeccion.secServicioEducativo.sduSede.sedTipoCalendario.tcePk",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoNombre",
                    "matSeccion.secServicioEducativo.sduGrado.graNombre",
                    "matSeccion.secServicioEducativo.sduOpcion.opcNombre",
                    "matSeccion.secServicioEducativo.sduProgramaEducativo.pedNombre",
                    "matSeccion.secNombre",
                    "matSeccion.secServicioEducativo.sduSede.sedCodigo",
                    "matSeccion.secServicioEducativo.sduSede.sedTipo",
                    "matSeccion.secServicioEducativo.sduSede.sedNombre",
                    "matSeccion.secServicioEducativo.sduSede.sedTelefono",
                    "matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                    "matSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                    "matSeccion.secTipoPeriodo",
                    "matSeccion.secNumeroPeriodo",
                    "matEstudiante.estPk",
                    "matEstudiante.estVersion",
                    "matEstudiante.estPersona.perPrimerNombre",
                    "matEstudiante.estPersona.perSegundoNombre",
                    "matEstudiante.estPersona.perPrimerApellido",
                    "matEstudiante.estPersona.perSegundoApellido",
                    "matEstudiante.estPersona.perNie",});
                MatriculaDAO codDAO = new MatriculaDAO(em, seguridadBean);
                List<SgMatricula> list = codDAO.obtenerPorFiltro(fm, fm.getSecurityOperation());
                if (list.isEmpty()) {
                    BusinessException ge = new BusinessException();
                    ge.addError(Errores.ERROR_ESTUDIANTE_NO_TIENE_MATRICULA_ABIERTA_O_CERRADA_POR_CIERRE);
                    throw ge;
                }
                return list;
            } else {
                BusinessException ge = new BusinessException();
                ge.addError(Errores.ERROR_NIE_VACIO);
                throw ge;
            }
        } catch (BusinessException be) {
            throw be;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    public SgPeriodosOrdExord busarPeriodos(SgMatricula mat) {
        try {
            SgPeriodosOrdExord per = new SgPeriodosOrdExord();
            FiltroComponentePlanGrado fpg = new FiltroComponentePlanGrado();
            fpg.setCpgGradoPk(mat.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
            fpg.setCpgPlanEstudioPk(mat.getMatSeccion().getSecPlanEstudio().getPesPk());
            fpg.setCpgEsTipoPaes(Boolean.FALSE);
            fpg.setIncluirCampos(new String[]{"cpgPeriodosCalificacion",
                "cpgCantidadPrimeraPrueba",
                "cpgCantidadPrimeraSuficiencia",
                "cpgCantidadSegundaPrueba",
                "cpgCantidadSegundaSuficiencia",
                "cpgComponentePlanEstudio.cpePk",
                "cpgComponentePlanEstudio.cpeCodigo",
                "cpgComponentePlanEstudio.cpeNombre",
                "cpgComponentePlanEstudio.cpeTipo",
                "cpgComponentePlanEstudio.cpeVersion",
                "cpgVersion",
                "cpgNombrePublicable",
                "cpgEscalaCalificacion.ecaPk",
                "cpgEscalaCalificacion.ecaTipoEscala",
                "cpgEscalaCalificacion.ecaMinimo",
                "cpgEscalaCalificacion.ecaMaximo",
                "cpgEscalaCalificacion.ecaPrecision"
            });

            List<SgComponentePlanGrado> cpg = componentePlanGradoBean.obtenerPorFiltro(fpg);

            if (!cpg.isEmpty()) {
                per.setComponentesPlanGrado(cpg);
                List<Integer> cantPeriodos = cpg.stream().map(c -> c.getCpgPeriodosCalificacion()).distinct().collect(Collectors.toList());

                FiltroRangoFecha frf = new FiltroRangoFecha();
                frf.setModalidadAtencionPk(mat.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion().getMatPk());
                frf.setModalidadEducativaPk(mat.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModPk());
                frf.setSubModalidadAtencionPk(mat.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null ? mat.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                frf.setHabilitado(Boolean.FALSE);
                frf.setPcaNumeros(cantPeriodos);
                frf.setPcaTipoPeriodo(mat.getMatSeccion().getSecTipoPeriodo());
                frf.setPcaNumeroPeriodo(mat.getMatSeccion().getSecNumeroPeriodo());
                frf.setPcaAnioLectivo(mat.getMatSeccion().getSecAnioLectivo().getAlePk());
                frf.setPcaTipoCalendario(mat.getMatSeccion().getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());
                frf.setPcaEsPrueba(Boolean.FALSE);
                frf.setIncluirCampos(new String[]{"rfePk", "rfeCodigo", "rfeVersion", "rfePeriodoCalificacion.pcaPk", "rfePeriodoCalificacion.pcaVersion", "rfePeriodoCalificacion.pcaNombre", "rfePeriodoCalificacion.pcaNumero", "rfeFechaDesde", "rfeFechaHasta"});
                List<SgRangoFecha> rangos = rangoFechaBean.obtenerPorFiltro(frf);
                per.setRangoFechas(rangos);

                FiltroPeriodoCalendario fperCal = new FiltroPeriodoCalendario();
                fperCal.setCesNivelFk(mat.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk());
                fperCal.setCesModalidadAtencionFk(mat.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion().getMatPk());
                fperCal.setCesSubModalidadAtencionFk(mat.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null ? mat.getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                fperCal.setFechaCalificacion(LocalDate.now());
                fperCal.setCesHabilitado(Boolean.TRUE);
                fperCal.setIncluirCampos(new String[]{
                    "cesTipo",
                    "cesVersion"
                });

                List<SgPeriodoCalendario> listPeriodoCalendario = calendarioEscolarBean.obtenerPorFiltro(fperCal);
                List<EnumCalendarioEscolar> listHabilitados = listPeriodoCalendario.stream().map(c -> c.getCesTipo()).distinct().collect(Collectors.toList());
                List<EnumCalendarioEscolar> listDeshabilitados = new ArrayList(Arrays.asList(EnumCalendarioEscolar.values()));
                listDeshabilitados.remove(EnumCalendarioEscolar.MAT);
                listHabilitados.remove(EnumCalendarioEscolar.MAT);
                listDeshabilitados.removeAll(listHabilitados);
                per.setPeriodosExtraordinarios(listDeshabilitados);
                return per;
            } else {
                BusinessException ge = new BusinessException();
                ge.addError(Errores.ERROR_COMPONENTE_PLAN_GRADO_PARA_MATRICULA_VACIO);
                throw ge;
            }
        } catch (BusinessException be) {
            throw be;
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

                CodigueraDAO<SgHabilitacionPeriodoCalificacion> codDAO = new CodigueraDAO<>(em, SgHabilitacionPeriodoCalificacion.class);
                codDAO.eliminarPorId(id, null);
            } catch (Exception ex) {
                throw new TechnicalException(ex);
            }
        }
    }

}
