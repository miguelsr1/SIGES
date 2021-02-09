/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCalificacionCE;
import sv.gob.mined.siges.web.lazymodels.LazyCalificacionEstudianteDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgArea;
import sv.gob.mined.siges.web.dto.SgAreaCombo;
import sv.gob.mined.siges.web.dto.SgAsignatura;
import sv.gob.mined.siges.web.dto.SgCalificacionEstudiante;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.dto.SgEstudiante;
import sv.gob.mined.siges.web.dto.SgModulo;
import sv.gob.mined.siges.web.dto.SgPeriodoCalendario;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.dto.SgRelGradoPlanEstudio;
import sv.gob.mined.siges.web.dto.catalogo.SgAreaAsignaturaModulo;
import sv.gob.mined.siges.web.dto.catalogo.SgCalificacion;
import sv.gob.mined.siges.web.dto.catalogo.SgEscalaCalificacion;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.enumerados.EnumMatriculaEstado;
import sv.gob.mined.siges.web.enumerados.TipoEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstudiante;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRangoFecha;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelGradoPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.catalogo.FiltroCalificacionCatalogo;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CalendarioEscolarRestClient;
import sv.gob.mined.siges.web.restclient.CalificacionEstudianteRestClient;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanGradoRestClient;
import sv.gob.mined.siges.web.restclient.EstudianteRestClient;
import sv.gob.mined.siges.web.restclient.PeriodoCalificacionRestClient;
import sv.gob.mined.siges.web.restclient.RangoFechaRestClient;
import sv.gob.mined.siges.web.restclient.RelGradoPlanEstudioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CalificacionEstudianteComponenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalificacionEstudianteComponenteBean.class.getName());

    @Inject
    private CalificacionEstudianteRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private FiltrosSeccionBean filtrosSeccion;

    @Inject
    private EstudianteRestClient restEstudiante;

    @Inject
    private ComponentePlanGradoRestClient restCompPlanGrado;

    @Inject
    private PeriodoCalificacionRestClient restPeriodoCalificacion;

    @Inject
    private CalendarioEscolarRestClient calendarioEscolarRestClient;

    @Inject
    private CatalogosRestClient catalogoRestClient;

    @Inject
    private RangoFechaRestClient rangoFechaRestClient;

    @Inject
    private RelGradoPlanEstudioRestClient relGradoPlanEstudioRestClient;

    private FiltroCalificacionEstudiante filtro = new FiltroCalificacionEstudiante();
    private SgCalificacionCE entidadEnEdicion = new SgCalificacionCE();
    private List<RevHistorico> historialCalificacionEstudiante = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCalificacionEstudianteDataModel calificacionEstudianteLazyModel;
    private List<EnumTiposPeriodosCalificaciones> periodoOrdinarioList;
    private List<SgComponentePlanGrado> componentePlanGradoList;
    private List<SgComponentePlanGrado> componentePlanGradoTotalEstudianteList;
    private SgEstudiante estudianteEnEdicion;
    private SofisComboG<SgPeriodoCalificacion> comboPeriodoCalificacion;
    private SofisComboG<EnumCalendarioEscolar> comboTipoPeriodoCalendario;
    private List<SelectItem> comboPeriodos;
    private SofisComboG<SgRangoFecha> comboRangoFecha;
    private Integer comboPeriodoSeleccionado;
    private List<SgCalificacionEstudiante> calificacionEstList;
    private SofisComboG<SgCalificacion>[] comboCalificacionConceptual;
    private Long estudianteNie;
    private Long calificacionId;
    private List<SgCalificacionEstudiante> calEstudianteResultados;
    private SgEscalaCalificacion[] escalaCalificacion;
    private Boolean soloLectura = Boolean.FALSE;
    private SofisComboG<SgAreaCombo> comboArea;
    private List<SgCalificacionEstudiante> calificacionNoPertenecenSeccion;
    private Boolean respetarOrden = Boolean.FALSE;
    private Boolean deshabilitadoPorNoTenerValidada = Boolean.FALSE;
    private Boolean deshabilitadoPorNoTenerProvisional = Boolean.FALSE;
    private Boolean requiereValidacionAcademica = Boolean.FALSE;

    public CalificacionEstudianteComponenteBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACION_POR_ESTUDIANTE)) {
            LOGGER.log(Level.INFO, sessionBean.getUser().getName() + " - Redirigiendo a inicio. " + ConstantesOperaciones.MENU_CALIFICACION_POR_ESTUDIANTE);
            JSFUtils.redirectToIndex();
        }
    }

    public void buscarEstudiante() {
        // componentePlanGradoTotalEstudianteList utilizado en web para renderizar btn generar reporte. 
        componentePlanGradoTotalEstudianteList = null;
        try {
            limpiarCombos();
            filtro.setCaeTipoPeriodoCalificacion(null);
            estudianteEnEdicion = new SgEstudiante();
            if (estudianteNie != null) {
                FiltroEstudiante fest = new FiltroEstudiante();
                fest.setNie(estudianteNie);
                fest.setIncluirCampos(new String[]{
                    "estVersion",
                    "estPersona.perPrimerNombre", "estPersona.perSegundoNombre", "estPersona.perTercerNombre",
                    "estPersona.perPrimerApellido", "estPersona.perSegundoApellido", "estPersona.perTercerApellido",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modPk",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaModalidadAtencion.matPk",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graRelacionModalidad.reaSubModalidadAtencion.smoPk",
                    "estUltimaMatricula.matSeccion.secAnioLectivo.alePk",
                    "estUltimaMatricula.matSeccion.secAnioLectivo.aleAnio",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graPk",
                    "estUltimaMatricula.matSeccion.secPlanEstudio.pesPk",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedTipoCalendario.tcePk",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduSede.sedTipo",
                    "estUltimaMatricula.matSeccion.secPk",
                    "estUltimaMatricula.matSeccion.secVersion",
                    "estUltimaMatricula.matSeccion.secUltModUsuario",
                    "estUltimaMatricula.matSeccion.secTipoPeriodo",
                    "estUltimaMatricula.matSeccion.secNumeroPeriodo",
                    "estUltimaMatricula.matSeccion.secServicioEducativo.sduGrado.graPk",
                    "estUltimaMatricula.matAnulada",
                    "estUltimaMatricula.matValidacionAcademica",
                    "estUltimaMatricula.matProvisional",
                    "estUltimaMatricula.matEstado",
                    "estUltModFecha"
                });
                List<SgEstudiante> listEst = restEstudiante.buscar(fest);
                if (!listEst.isEmpty()) {
                    estudianteEnEdicion = listEst.get(0);
                    if (estudianteEnEdicion.getEstUltimaMatricula() != null) {
                        if (!estudianteEnEdicion.getEstUltimaMatricula().getMatAnulada()) {
                            FiltroRelGradoPlanEstudio frg = new FiltroRelGradoPlanEstudio();
                            frg.setRgpGrado(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
                            frg.setRgpPlanEstudio(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecPlanEstudio().getPesPk());
                            frg.setIncluirCampos(new String[]{"rgpConsiderarOrden", "rgpPermiteCalificarConMatProvisional", "rgpPermiteCalificarSinMatValidada", "rgpRequiereValidacionAcademica"});
                            List<SgRelGradoPlanEstudio> listRel = relGradoPlanEstudioRestClient.buscar(frg);
                            respetarOrden = Boolean.FALSE;
                            if (!listRel.isEmpty()) {
                                if (listRel.get(0).getRgpConsiderarOrden() != null) {
                                    respetarOrden = listRel.get(0).getRgpConsiderarOrden();
                                }
                                this.deshabilitadoPorNoTenerProvisional = !listRel.get(0).getRgpPermiteCalificarConMatProvisional() && BooleanUtils.isTrue(estudianteEnEdicion.getEstUltimaMatricula().getMatProvisional());
                                if (BooleanUtils.isNotTrue(listRel.get(0).getRgpRequiereValidacionAcademica())) {
                                    this.deshabilitadoPorNoTenerValidada = Boolean.FALSE;
                                } else {
                                    this.deshabilitadoPorNoTenerValidada = !listRel.get(0).getRgpPermiteCalificarSinMatValidada() && BooleanUtils.isNotTrue(estudianteEnEdicion.getEstUltimaMatricula().getMatValidacionAcademica());
                                }

                            }

                            FiltroComponentePlanGrado fcpg = new FiltroComponentePlanGrado();
                            fcpg.setCpgGradoPk(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());
                            fcpg.setCpgPlanEstudioPk(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecPlanEstudio().getPesPk());
                            fcpg.setCpgAgregandoSeccionExclusiva(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecPk());
                            fcpg.setCpgCalificacionIngresadaCE(Boolean.TRUE);
                            fcpg.setIncluirCampos(new String[]{
                                "cpgPeriodosCalificacion",
                                "cpgEscalaCalificacion.ecaPk",
                                "cpgEscalaCalificacion.ecaCodigo",
                                "cpgEscalaCalificacion.ecaHabilitado",
                                "cpgEscalaCalificacion.ecaNombre",
                                "cpgEscalaCalificacion.ecaTipoEscala",
                                "cpgEscalaCalificacion.ecaMinimo",
                                "cpgEscalaCalificacion.ecaMaximo",
                                "cpgEscalaCalificacion.ecaMinimoAprobacion",
                                "cpgEscalaCalificacion.ecaPrecision",
                                "cpgComponentePlanEstudio.indAreaIndicador.ariPk",
                                "cpgComponentePlanEstudio.indAreaIndicador.ariNombre",
                                "cpgComponentePlanEstudio.indAreaIndicador.ariCodigo",
                                "cpgComponentePlanEstudio.asigAreaAsignaturaModulo.aamPk",
                                "cpgComponentePlanEstudio.asigAreaAsignaturaModulo.aamNombre",
                                "cpgComponentePlanEstudio.asigAreaAsignaturaModulo.aamCodigo",
                                "cpgComponentePlanEstudio.modAreaAsignaturaModulo.aamPk",
                                "cpgComponentePlanEstudio.modAreaAsignaturaModulo.aamNombre",
                                "cpgComponentePlanEstudio.modAreaAsignaturaModulo.aamCodigo",
                                "cpgComponentePlanEstudio.cpeTipo",
                                "cpgComponentePlanEstudio.cpePk",
                                "cpgComponentePlanEstudio.cpeVersion",
                                "cpgComponentePlanEstudio.cpeNombre",
                                "cpgCantidadPrimeraPrueba",
                                "cpgCantidadPrimeraSuficiencia",
                                "cpgCantidadSegundaPrueba",
                                "cpgCantidadSegundaSuficiencia",
                                "cpgCalculoNotaInstitucional",
                                "cpgFuncionRedondeo",
                                "cpgOrden",
                                "cpgCodigoExterno",
                                "cpgPrecision",
                                "cpgObjetoPromocion",
                                "cpgCalificacionIngresadaCE",});

                            if (respetarOrden) {
                                fcpg.setOrderBy(new String[]{"cpgOrden"});
                                fcpg.setAscending(new boolean[]{true});
                            }
                            componentePlanGradoTotalEstudianteList = restCompPlanGrado.buscar(fcpg);
                            filtro.setCaeTipoPeriodoCalificacion(EnumTiposPeriodosCalificaciones.ORD);

                            soloLectura = Boolean.FALSE;
                            if (estudianteEnEdicion.getEstUltimaMatricula().getMatEstado().equals(EnumMatriculaEstado.CERRADO)) {
                                soloLectura = Boolean.TRUE;
                            }

                            cargarPeriodoCalificacion();
                            if (componentePlanGradoTotalEstudianteList.isEmpty()) {
                                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_EXISTEN_COMPONENTES_PLAN_GRADO), "");
                            }
                        } else {
                            estudianteEnEdicion = new SgEstudiante();
                            JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_MATRICULA_ANULADA), "");
                        }
                    } else {
                        estudianteEnEdicion = new SgEstudiante();
                        JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_MATRICULA_VACIO), "");
                    }
                } else {
                    JSFUtils.agregarWarn(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NO_SE_ENCONTRO_ESTUDIANTE), "");
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarPeriodoCalificacion() {
        try {
            comboTipoPeriodoCalendario = new SofisComboG();
            comboTipoPeriodoCalendario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboPeriodos = new ArrayList();
            comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            comboPeriodoCalificacion = new SofisComboG();
            comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboRangoFecha = new SofisComboG();
            comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboArea = new SofisComboG();
            comboArea.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (estudianteEnEdicion != null && !componentePlanGradoTotalEstudianteList.isEmpty()) {

                List<Integer> numerosPeriodo = new ArrayList();
                for (SgComponentePlanGrado compPlanGrado : componentePlanGradoTotalEstudianteList) {
                    if (!numerosPeriodo.contains(compPlanGrado.getCpgPeriodosCalificacion())) {
                        numerosPeriodo.add(compPlanGrado.getCpgPeriodosCalificacion());
                    }
                }

                switch (filtro.getCaeTipoPeriodoCalificacion()) {
                    case ORD:
                        List<SgPeriodoCalificacion> listPeriodoCalif;
                        FiltroPeriodoCalificacion fpc = new FiltroPeriodoCalificacion();
                        fpc.setIncluirCampos(new String[]{
                            "pcaNumero",
                            "pcaPermiteCalificarSinNie",
                            "pcaEsPrueba",
                            "pcaNombre",
                            "pcaVersion"});
                        fpc.setPcaEsPrueba(Boolean.FALSE);
                        fpc.setPcaModalidadEducativa(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModPk());
                        fpc.setPcaModalidadAtencion(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion().getMatPk());
                        fpc.setPcaSubModalidadAtencion(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null ? estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);

                        //Filtra dependiendo si es anual o semestral
                        fpc.setPcaTipoPeriodo(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecTipoPeriodo());
                        fpc.setPcaNumeroPeriodo(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecNumeroPeriodo());

                        fpc.setPcaNumeros(numerosPeriodo);
                        fpc.setPcaAnioLectivo(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecAnioLectivo().getAlePk());
                        fpc.setPcaTipoCalendario(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());
                        listPeriodoCalif = restPeriodoCalificacion.buscar(fpc);

                        if (listPeriodoCalif.size() == 1) {
                            comboPeriodoCalificacion.setSelectedT(listPeriodoCalif.get(0));
                            cargarRangoFecha();
                        }

                        comboPeriodoCalificacion = new SofisComboG(listPeriodoCalif, "pcaNombre");
                        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                        if (listPeriodoCalif.size() == 1) {
                            comboPeriodoCalificacion.setSelectedT(listPeriodoCalif.get(0));
                            cargarRangoFecha();
                        }
                        break;

                    case EXORD:
                        FiltroPeriodoCalendario fperCal = new FiltroPeriodoCalendario();
                        fperCal.setCesNivelFk(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk());
                        fperCal.setCesModalidadAtencionFk(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraRelacionModalidad().getReaModalidadAtencion().getMatPk());
                        fperCal.setFechaCalificacion(LocalDate.now());
                        fperCal.setIncluirCampos(new String[]{
                            "cesTipo",
                            "cesVersion"
                        });
                        List<SgPeriodoCalendario> listPeriodoCalendario = calendarioEscolarRestClient.buscar(fperCal);
                        List<EnumCalendarioEscolar> listEnumCalendario = new ArrayList();
                        for (SgPeriodoCalendario periodoCalendario : listPeriodoCalendario) {
                            if (!listEnumCalendario.contains(periodoCalendario.getCesTipo())) {
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.PREC) && componentePlanGradoTotalEstudianteList.get(0).getCpgCantidadPrimeraPrueba() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.PREC);
                                }
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.PRECPS) && componentePlanGradoTotalEstudianteList.get(0).getCpgCantidadPrimeraSuficiencia() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.PRECPS);
                                }
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.SREC) && componentePlanGradoTotalEstudianteList.get(0).getCpgCantidadSegundaPrueba() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.SREC);
                                }
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.SRECPS) && componentePlanGradoTotalEstudianteList.get(0).getCpgCantidadSegundaSuficiencia() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.SRECPS);
                                }
                            }
                        }

                        comboTipoPeriodoCalendario = new SofisComboG(listEnumCalendario, "text");
                        comboTipoPeriodoCalendario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                        comboPeriodos = new ArrayList();
                        comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
                        if (listEnumCalendario.size() == 1) {
                            comboTipoPeriodoCalendario.setSelectedT(listEnumCalendario.get(0));
                            cargarRangoFechaCalendarioEscolar();
                        }
                        if (listEnumCalendario.size() == 1) {
                            comboTipoPeriodoCalendario.setSelectedT(listEnumCalendario.get(0));
                            cargarRangoFechaCalendarioEscolar();
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarRangoFecha() {
        try {
            comboRangoFecha = new SofisComboG();
            comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboArea = new SofisComboG();
            comboArea.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (comboPeriodoCalificacion.getSelectedT() != null) {
                FiltroRangoFecha fre = new FiltroRangoFecha();
                fre.setPeriodoCalificacionPk(comboPeriodoCalificacion.getSelectedT().getPcaPk());
                fre.setHabilitado(!sessionBean.getOperaciones().contains(ConstantesOperaciones.CALIFICACIONES_SUPERUSUARIO) ? Boolean.TRUE : null);
                fre.setIncluirCampos(new String[]{
                    "rfeCodigo",
                    "rfeFechaDesde",
                    "rfeFechaDesde",
                    "rfeFechaHasta",
                    "rfeHabilitado",
                    "rfePeriodoCalificacion.pcaPk",
                    "rfePeriodoCalificacion.pcaNombre",
                    "rfePeriodoCalificacion.pcaNumero",
                    "rfePeriodoCalificacion.pcaPermiteCalificarSinNie",
                    "rfePeriodoCalificacion.pcaVersion",
                    "rfePeriodoCalificacion.pcaEsPrueba",
                    "rfeVersion"});
                fre.setOrderBy(new String[]{"rfeFechaDesde"});
                fre.setAscending(new boolean[]{true});
                List<SgRangoFecha> listrfe = rangoFechaRestClient.buscarConCache(fre);
                comboRangoFecha = new SofisComboG(listrfe, "rfeCodigoRango");
                comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                if (listrfe.size() == 1) {
                    comboRangoFecha.setSelectedT(listrfe.get(0));
                    cargaTabla();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarRangoFechaCalendarioEscolar() {
        try {
            comboPeriodoSeleccionado = 0;
            comboPeriodos = new ArrayList();
            comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
            comboArea = new SofisComboG();
            comboArea.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            Integer cantidadPrueba = 0;
            if (comboTipoPeriodoCalendario.getSelectedT() != null) {
                switch (comboTipoPeriodoCalendario.getSelectedT()) {
                    case PREC:
                        cantidadPrueba = componentePlanGradoTotalEstudianteList.get(0).getCpgCantidadPrimeraPrueba();
                        break;
                    case PRECPS:
                        cantidadPrueba = componentePlanGradoTotalEstudianteList.get(0).getCpgCantidadPrimeraSuficiencia();
                        break;
                    case SREC:
                        cantidadPrueba = componentePlanGradoTotalEstudianteList.get(0).getCpgCantidadSegundaPrueba();
                        break;
                    case SRECPS:
                        cantidadPrueba = componentePlanGradoTotalEstudianteList.get(0).getCpgCantidadSegundaSuficiencia();
                        break;
                    default:
                        cantidadPrueba = 0;
                        break;
                }
                comboPeriodos = new ArrayList();
                comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));
                for (int i = 1; i <= cantidadPrueba; i++) {
                    comboPeriodos.add(new SelectItem(i, Etiquetas.getValue("calificacion") + " " + i));
                }
                if (cantidadPrueba == 1) {
                    comboPeriodoSeleccionado = 1;
                    cargaTabla();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public String getUrlReporte() {
        return "?seccionId=" + estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecPk() + "&estudianteId=" + estudianteEnEdicion.getEstPk();
    }

    public void cargaTabla() {
        try {
            if ((EnumTiposPeriodosCalificaciones.ORD.equals(filtro.getCaeTipoPeriodoCalificacion()) && comboRangoFecha.getSelectedT() != null)
                    || (EnumTiposPeriodosCalificaciones.EXORD.equals(filtro.getCaeTipoPeriodoCalificacion()) && comboPeriodoSeleccionado > 0)) {

                FiltroCalificacionEstudiante fce = new FiltroCalificacionEstudiante();
                fce.setCaeEstudiantePk(estudianteEnEdicion.getEstPk());
                fce.setCaeGradoFk(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecServicioEducativo().getSduGrado().getGraPk());

                //Filtra dependiendo si sección es anual o semestral
                fce.setCaeTipoPeriodo(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecTipoPeriodo());
                fce.setCaeNumeroPeriodo(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecNumeroPeriodo());

                fce.setIncluirCampos(new String[]{
                    "caeCalificacion.calPk",
                    "caeCalificacion.calComponentePlanEstudio.cpePk",
                    "caeCalificacion.calComponentePlanEstudio.cpeCodigo",
                    "caeCalificacion.calComponentePlanEstudio.cpeNombre",
                    "caeCalificacion.calComponentePlanEstudio.cpeTipo",
                    "caeCalificacion.calComponentePlanEstudio.cpeVersion",
                    "caeCalificacion.calComponentePlanEstudio.cpeCodigoExterno",
                    "caeCalificacion.calComponentePlanEstudio.cpeNombrePublicable",
                    "caeCalificacion.calComponentePlanEstudio.indAreaIndicador.ariNombre",
                    "caeCalificacion.calComponentePlanEstudio.indAreaIndicador.ariCodigo",
                    "caeCalificacion.calComponentePlanEstudio.asigAreaAsignaturaModulo.aamNombre",
                    "caeCalificacion.calComponentePlanEstudio.asigAreaAsignaturaModulo.aamCodigo",
                    "caeCalificacion.calComponentePlanEstudio.modAreaAsignaturaModulo.aamNombre",
                    "caeCalificacion.calComponentePlanEstudio.modAreaAsignaturaModulo.aamCodigo",
                    "caeCalificacion.calSeccion.secPlanEstudio.pesPk",
                    "caeCalificacion.calSeccion.secPlanEstudio.pesNombre",
                    "caeCalificacion.calSeccion.secPlanEstudio.pesVersion",
                    "caeCalificacion.calSeccion.secServicioEducativo.sduGrado.graPk",
                    "caeCalificacion.calSeccion.secServicioEducativo.sduGrado.graVersion",
                    "caeCalificacion.calSeccion.secServicioEducativo.sduSede.sedCodigo",
                    "caeCalificacion.calSeccion.secServicioEducativo.sduSede.sedNombre",
                    "caeCalificacion.calSeccion.secServicioEducativo.sduSede.sedTipo",
                    "caeCalificacion.calSeccion.secNombre",
                    "caeCalificacion.calSeccion.secPk",
                    "caeCalificacion.calSeccion.secVersion",
                    "caeCalificacion.calRangoFecha.rfePk",
                    "caeCalificacion.calRangoFecha.rfeCodigo",
                    "caeCalificacion.calRangoFecha.rfeFechaDesde",
                    "caeCalificacion.calRangoFecha.rfeHabilitado",
                    "caeCalificacion.calRangoFecha.rfeFechaHasta",
                    "caeCalificacion.calRangoFecha.rfeVersion",
                    "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaPk",
                    "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaNombre",
                    "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaNumero",
                    "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaPermiteCalificarSinNie",
                    "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaVersion",
                    "caeCalificacion.calRangoFecha.rfePeriodoCalificacion.pcaEsPrueba",
                    "caeCalificacion.calVersion",
                    "caeCalificacion.calFecha",
                    "caeCalificacion.calUltModFecha",
                    "caeCalificacion.calUltModUsuario",
                    "caeCalificacion.calTipoPeriodoCalificacion",
                    "caeCalificacion.calTipoCalendarioEscolar",
                    "caeCalificacion.calNumero",
                    "caeCalificacion.calCerrado",
                    "caeEstudiante.estPk",
                    "caeEstudiante.estVersion",
                    "caeResolucion",
                    "caeCalificacionNumericaEstudiante",
                    "caeCalificacionConceptualEstudiante.calPk",
                    "caeCalificacionConceptualEstudiante.calCodigo",
                    "caeCalificacionConceptualEstudiante.calValor",
                    "caeCalificacionConceptualEstudiante.calOrden",
                    "caeCalificacionConceptualEstudiante.calVersion",
                    "caePromovidoCalificacion",
                    "caeObservacion",
                    "caeFechaRealizado",
                    "caeUltModFecha",
                    "caeUltModUsuario",
                    "caeVersion",
                    "caeProcesoDeCreacion"});

                componentePlanGradoList = new ArrayList();
                switch (filtro.getCaeTipoPeriodoCalificacion()) {
                    case ORD:
                        if (comboPeriodoCalificacion.getSelectedT() != null) {
                            for (SgComponentePlanGrado cpg : componentePlanGradoTotalEstudianteList) {
                                if (comboPeriodoCalificacion.getSelectedT().getPcaNumero().equals(cpg.getCpgPeriodosCalificacion())) {
                                    if (comboArea.getSelectedT() != null) {
                                        if (filtrarPorComboArea(cpg)) {
                                            componentePlanGradoList.add(cpg);
                                        }
                                    } else {
                                        componentePlanGradoList.add(cpg);
                                    }
                                }
                            }
                        }
                        fce.setCaeRangoFechaPk(comboRangoFecha.getSelectedT().getRfePk());
                        fce.setCaeTipoPeriodoCalificacion(filtro.getCaeTipoPeriodoCalificacion());

                        break;
                    case EXORD:
                        if (comboArea.getSelectedT() != null) {
                            for (SgComponentePlanGrado cpg : componentePlanGradoTotalEstudianteList) {
                                if (filtrarPorComboArea(cpg)) {
                                    componentePlanGradoList.add(cpg);
                                }
                            }
                        } else {
                            componentePlanGradoList = componentePlanGradoTotalEstudianteList;
                        }
                        fce.setCaeTipoCalendarioEscolar(comboTipoPeriodoCalendario.getSelectedT());
                        fce.setCaeTipoPeriodoCalificacion(filtro.getCaeTipoPeriodoCalificacion());
                        fce.setCaeNumero(comboPeriodoSeleccionado);
                        break;
                    default:
                        break;
                }

                if (comboArea.getSelectedT() == null) {
                    cargarComboArea(componentePlanGradoList);
                }

                calEstudianteResultados = new ArrayList<>();
                calificacionEstList = restClient.buscar(fce);
                calificacionNoPertenecenSeccion = calificacionEstList.stream()
                        .filter(c -> !c.getCaeCalificacion().getCalSeccion().getSecPk().equals(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion().getSecPk()) && (c.getCaeCalificacionConceptualEstudiante() != null || !StringUtils.isBlank(c.getCaeCalificacionNumericaEstudiante())))
                        .collect(Collectors.toList());
                if (!calificacionNoPertenecenSeccion.isEmpty()) {
                    Collections.sort(calificacionNoPertenecenSeccion, (o1, o2) -> o1.getCaeCalificacion().getCalComponentePlanEstudio().getCpeNombre().compareTo(o2.getCaeCalificacion().getCalComponentePlanEstudio().getCpeNombre()));
                    List<SgComponentePlanGrado> listCPGAux = new ArrayList();
                    for (SgComponentePlanGrado cpg : componentePlanGradoList) {
                        List<SgCalificacionEstudiante> calEl = calificacionNoPertenecenSeccion.stream()
                                .filter(c -> c.getCaeCalificacion().getCalComponentePlanEstudio().getCpePk().equals(cpg.getCpgComponentePlanEstudio().getCpePk()))
                                .collect(Collectors.toList());
                        if (calEl.isEmpty()) {
                            listCPGAux.add(cpg);
                        }
                    }
                    componentePlanGradoList = listCPGAux;
                }

                escalaCalificacion = new SgEscalaCalificacion[componentePlanGradoList.size()];
                comboCalificacionConceptual = new SofisComboG[componentePlanGradoList.size()];

                //Por defecto ordena por orden del componente plan grado. Si no respeta orden, entonces:
                if (!respetarOrden) {
                    Collections.sort(componentePlanGradoList, (o1, o2) -> (o1.getCpgComponentePlanEstudio().getNombreArea() == null || o2.getCpgComponentePlanEstudio().getNombreArea() == null)
                            ? Comparator.nullsFirst(String::compareTo).compare(o1.getCpgComponentePlanEstudio().getNombreArea(), o2.getCpgComponentePlanEstudio().getNombreArea())
                            : o1.getCpgComponentePlanEstudio().getNombreArea().compareTo(o2.getCpgComponentePlanEstudio().getNombreArea()) != 0
                            ? o1.getCpgComponentePlanEstudio().getNombreArea().compareTo(o2.getCpgComponentePlanEstudio().getNombreArea())
                            : o1.getCpgComponentePlanEstudio().getCpeNombre().compareTo(o2.getCpgComponentePlanEstudio().getCpeNombre()));
                    //Si un área es null, entonces se compara utilizando nullFirst.
                    //Sino, si las áreas son distintas, compara por área. Si son la misma, compara por plan de estudio.
                }

                for (SgComponentePlanGrado cpg : componentePlanGradoList) {
                    escalaCalificacion[componentePlanGradoList.indexOf(cpg)] = cpg.getCpgEscalaCalificacion();

                    if (escalaCalificacion[componentePlanGradoList.indexOf(cpg)].getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA)) {
                        obtenerCalificacionPorComponente(cpg);
                    } else {
                        FiltroCalificacionCatalogo fcal = new FiltroCalificacionCatalogo();
                        fcal.setCalEscalaCalificacionPk(cpg.getCpgEscalaCalificacion().getEcaPk());
                        fcal.setOrderBy(new String[]{"calValor"});
                        List<SgCalificacion> calificaciones = catalogoRestClient.buscarCalificacion(fcal); //Cache
                        comboCalificacionConceptual[componentePlanGradoList.indexOf(cpg)] = new SofisComboG(calificaciones, "calValor");
                        comboCalificacionConceptual[componentePlanGradoList.indexOf(cpg)].addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

                        obtenerCalificacionPorComponente(cpg);
                    }

                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void obtenerCalificacionPorComponente(SgComponentePlanGrado compPlanGrado) {
        Boolean existe = Boolean.FALSE;
        for (SgCalificacionEstudiante calEst : calificacionEstList) {
            if (calEst.getCaeCalificacion().getCalComponentePlanEstudio().equals(compPlanGrado.getCpgComponentePlanEstudio())) {
                calEst.setCaeEstudiante(estudianteEnEdicion);
                calEstudianteResultados.add(calEst);
                existe = Boolean.TRUE;
                if (!escalaCalificacion[componentePlanGradoList.indexOf(compPlanGrado)].getEcaTipoEscala().equals(TipoEscalaCalificacion.NUMERICA)) {
                    comboCalificacionConceptual[componentePlanGradoList.indexOf(compPlanGrado)].setSelectedT(calEst.getCaeCalificacionConceptualEstudiante());
                    calEst.setCaeCalificacionNumericaEstudiante(null);
                } else {
                    calEst.setCaeCalificacionConceptualEstudiante(null);
                }
                break;
            }
        }

        if (!existe) {
            SgCalificacionCE cal = new SgCalificacionCE();
            cal.setCalComponentePlanEstudio(compPlanGrado.getCpgComponentePlanEstudio());
            cal.setCalSeccion(estudianteEnEdicion.getEstUltimaMatricula().getMatSeccion());
            cal.setCalFecha(LocalDate.now());
            switch (filtro.getCaeTipoPeriodoCalificacion()) {
                case ORD:
                    cal.setCalRangoFecha(comboRangoFecha.getSelectedT());
                    cal.setCalTipoPeriodoCalificacion(filtro.getCaeTipoPeriodoCalificacion());
                    break;
                case EXORD:
                    cal.setCalTipoCalendarioEscolar(comboTipoPeriodoCalendario.getSelectedT());
                    cal.setCalTipoPeriodoCalificacion(filtro.getCaeTipoPeriodoCalificacion());
                    cal.setCalNumero(comboPeriodoSeleccionado);
                    break;
                default:
                    break;
            }

            SgCalificacionEstudiante calificacionEst = new SgCalificacionEstudiante();
            calificacionEst.setCaeEstudiante(estudianteEnEdicion);
            calificacionEst.setCaeCalificacion(cal);
            calEstudianteResultados.add(calificacionEst);
        }

    }

    public void cargarComboArea(List<SgComponentePlanGrado> list) {
        if (!list.isEmpty()) {
            List<SgAreaCombo> elementos = new ArrayList();
            for (SgComponentePlanGrado cpg : list) {
                SgAreaCombo areaComb = new SgAreaCombo();
                if (cpg.getCpgComponentePlanEstudio() instanceof SgAsignatura) {
                    SgAsignatura asig = (SgAsignatura) cpg.getCpgComponentePlanEstudio();
                    if (asig.getAsigAreaAsignaturaModulo() != null) {
                        areaComb.setAcCodigo(asig.getAsigAreaAsignaturaModulo().getAamCodigo());
                        areaComb.setAcNombre(asig.getAsigAreaAsignaturaModulo().getAamNombre());
                        areaComb.setEsAsignaturaModulo(Boolean.TRUE);
                        if (!elementos.contains(areaComb)) {
                            elementos.add(areaComb);
                        }
                    }
                }
                if (cpg.getCpgComponentePlanEstudio() instanceof SgModulo) {
                    SgModulo asig = (SgModulo) cpg.getCpgComponentePlanEstudio();
                    if (asig.getModAreaAsignaturaModulo() != null) {
                        areaComb.setAcCodigo(asig.getModAreaAsignaturaModulo().getAamCodigo());
                        areaComb.setAcNombre(asig.getModAreaAsignaturaModulo().getAamNombre());
                        areaComb.setEsAsignaturaModulo(Boolean.TRUE);
                        if (!elementos.contains(areaComb)) {
                            elementos.add(areaComb);
                        }
                    }
                }
                if (cpg.getCpgComponentePlanEstudio() instanceof SgArea) {
                    SgArea asig = (SgArea) cpg.getCpgComponentePlanEstudio();
                    if (asig.getIndAreaIndicador() != null) {
                        areaComb.setAcCodigo(asig.getIndAreaIndicador().getAriCodigo());
                        areaComb.setAcNombre(asig.getIndAreaIndicador().getAriNombre());
                        areaComb.setEsAsignaturaModulo(Boolean.FALSE);
                        if (!elementos.contains(areaComb)) {
                            elementos.add(areaComb);
                        }
                    }
                }

            }
            comboArea = new SofisComboG(elementos, "acNombre");
            comboArea.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        }

    }

    public void seleccionarArea() {
        if (comboArea.getSelectedT() != null) {

        }
    }

    public Boolean filtrarPorComboArea(SgComponentePlanGrado cpg) {
        Boolean respuesta = Boolean.FALSE;

        if (comboArea.getSelectedT().getEsAsignaturaModulo()) {

            SgAreaAsignaturaModulo areaAsig = new SgAreaAsignaturaModulo();
            if (cpg.getCpgComponentePlanEstudio() instanceof SgAsignatura) {
                SgAsignatura asig = (SgAsignatura) cpg.getCpgComponentePlanEstudio();
                areaAsig = asig.getAsigAreaAsignaturaModulo();
            }
            if (cpg.getCpgComponentePlanEstudio() instanceof SgModulo) {
                SgModulo asig = (SgModulo) cpg.getCpgComponentePlanEstudio();
                areaAsig = asig.getModAreaAsignaturaModulo();
            }
            if (areaAsig.getAamPk() != null) {
                if (areaAsig.getAamCodigo().equals(comboArea.getSelectedT().getAcCodigo())) {
                    respuesta = Boolean.TRUE;
                }
            }
        } else {
            if (cpg.getCpgComponentePlanEstudio() instanceof SgArea) {
                SgArea asig = (SgArea) cpg.getCpgComponentePlanEstudio();
                if (asig.getIndAreaIndicador() != null) {
                    if (asig.getIndAreaIndicador().getAriCodigo().equals(comboArea.getSelectedT().getAcCodigo())) {
                        respuesta = Boolean.TRUE;
                    }
                }
            }
        }
        return respuesta;
    }

    public void cargarCombos() {
        comboPeriodoCalificacion = new SofisComboG();
        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboRangoFecha = new SofisComboG();
        comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        List<EnumTiposPeriodosCalificaciones> tipoPeriodoCalList = new ArrayList();
        tipoPeriodoCalList.add(EnumTiposPeriodosCalificaciones.ORD);
        tipoPeriodoCalList.add(EnumTiposPeriodosCalificaciones.EXORD);
        periodoOrdinarioList = new ArrayList(tipoPeriodoCalList);
        comboArea = new SofisComboG();
        comboArea.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    private void limpiarCombos() {
        comboPeriodoCalificacion = new SofisComboG();
        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboRangoFecha = new SofisComboG();
        comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboPeriodoSeleccionado = 0;
        comboArea = new SofisComboG();
        comboArea.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void calificacionSelected(SgCalificacionEstudiante cal, Integer rowIndex) {
        cal.setCaeCalificacionConceptualEstudiante(this.comboCalificacionConceptual[rowIndex].getSelectedT());
    }

    public void limpiar() {
        filtro = new FiltroCalificacionEstudiante();
        this.filtrosSeccion.limpiarCombos();

    }

    public void guardar() {
        try {
            if (calEstudianteResultados != null) {
                calEstudianteResultados.removeIf(c -> c.getCaePk() == null && StringUtils.isBlank(c.getCaeCalificacionNumericaEstudiante()) && c.getCaeCalificacionConceptualEstudiante() == null);
            }

            restClient.guardarCalificacionesEstudiante(calEstudianteResultados);
            cargaTabla();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregar() {
        entidadEnEdicion = new SgCalificacionCE();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgCalificacionCE var) {
        entidadEnEdicion = (SgCalificacionCE) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public FiltroCalificacionEstudiante getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCalificacionEstudiante filtro) {
        this.filtro = filtro;
    }

    public SgCalificacionCE getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCalificacionCE entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialCalificacionEstudiante() {
        return historialCalificacionEstudiante;
    }

    public void setHistorialCalificacionEstudiante(List<RevHistorico> historialCalificacionEstudiante) {
        this.historialCalificacionEstudiante = historialCalificacionEstudiante;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazyCalificacionEstudianteDataModel getCalificacionEstudianteLazyModel() {
        return calificacionEstudianteLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCalificacionEstudianteDataModel calificacionEstudianteLazyModel) {
        this.calificacionEstudianteLazyModel = calificacionEstudianteLazyModel;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public SedeRestClient getRestSede() {
        return restSede;
    }

    public void setRestSede(SedeRestClient restSede) {
        this.restSede = restSede;
    }

    public List<EnumTiposPeriodosCalificaciones> getPeriodoOrdinarioList() {
        return periodoOrdinarioList;
    }

    public void setPeriodoOrdinarioList(List<EnumTiposPeriodosCalificaciones> periodoOrdinarioList) {
        this.periodoOrdinarioList = periodoOrdinarioList;
    }

    public List<SgComponentePlanGrado> getComponentePlanGradoList() {
        return componentePlanGradoList;
    }

    public void setComponentePlanGradoList(List<SgComponentePlanGrado> componentePlanGradoList) {
        this.componentePlanGradoList = componentePlanGradoList;
    }

    public SgEstudiante getEstudianteEnEdicion() {
        return estudianteEnEdicion;
    }

    public void setEstudianteEnEdicion(SgEstudiante estudianteEnEdicion) {
        this.estudianteEnEdicion = estudianteEnEdicion;
    }

    public SofisComboG<SgPeriodoCalificacion> getComboPeriodoCalificacion() {
        return comboPeriodoCalificacion;
    }

    public void setComboPeriodoCalificacion(SofisComboG<SgPeriodoCalificacion> comboPeriodoCalificacion) {
        this.comboPeriodoCalificacion = comboPeriodoCalificacion;
    }

    public SofisComboG<EnumCalendarioEscolar> getComboTipoPeriodoCalendario() {
        return comboTipoPeriodoCalendario;
    }

    public void setComboTipoPeriodoCalendario(SofisComboG<EnumCalendarioEscolar> comboTipoPeriodoCalendario) {
        this.comboTipoPeriodoCalendario = comboTipoPeriodoCalendario;
    }

    public List<SelectItem> getComboPeriodos() {
        return comboPeriodos;
    }

    public void setComboPeriodos(List<SelectItem> comboPeriodos) {
        this.comboPeriodos = comboPeriodos;
    }

    public SofisComboG<SgRangoFecha> getComboRangoFecha() {
        return comboRangoFecha;
    }

    public void setComboRangoFecha(SofisComboG<SgRangoFecha> comboRangoFecha) {
        this.comboRangoFecha = comboRangoFecha;
    }

    public Integer getComboPeriodoSeleccionado() {
        return comboPeriodoSeleccionado;
    }

    public void setComboPeriodoSeleccionado(Integer comboPeriodoSeleccionado) {
        this.comboPeriodoSeleccionado = comboPeriodoSeleccionado;
    }

    public List<SgCalificacionEstudiante> getCalificacionEstList() {
        return calificacionEstList;
    }

    public void setCalificacionEstList(List<SgCalificacionEstudiante> calificacionEstList) {
        this.calificacionEstList = calificacionEstList;
    }

    public SofisComboG<SgCalificacion>[] getComboCalificacionConceptual() {
        return comboCalificacionConceptual;
    }

    public void setComboCalificacionConceptual(SofisComboG<SgCalificacion>[] comboCalificacionConceptual) {
        this.comboCalificacionConceptual = comboCalificacionConceptual;
    }

    public Long getEstudianteNie() {
        return estudianteNie;
    }

    public void setEstudianteNie(Long estudianteNie) {
        this.estudianteNie = estudianteNie;
    }

    public Long getCalificacionId() {
        return calificacionId;
    }

    public void setCalificacionId(Long calificacionId) {
        this.calificacionId = calificacionId;
    }

    public List<SgCalificacionEstudiante> getCalEstudianteResultados() {
        return calEstudianteResultados;
    }

    public void setCalEstudianteResultados(List<SgCalificacionEstudiante> calEstudianteResultados) {
        this.calEstudianteResultados = calEstudianteResultados;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgEscalaCalificacion[] getEscalaCalificacion() {
        return escalaCalificacion;
    }

    public void setEscalaCalificacion(SgEscalaCalificacion[] escalaCalificacion) {
        this.escalaCalificacion = escalaCalificacion;
    }

    public List<SgComponentePlanGrado> getComponentePlanGradoTotalEstudianteList() {
        return componentePlanGradoTotalEstudianteList;
    }

    public void setComponentePlanGradoTotalEstudianteList(List<SgComponentePlanGrado> componentePlanGradoTotalEstudianteList) {
        this.componentePlanGradoTotalEstudianteList = componentePlanGradoTotalEstudianteList;
    }

    public SofisComboG<SgAreaCombo> getComboArea() {
        return comboArea;
    }

    public void setComboArea(SofisComboG<SgAreaCombo> comboArea) {
        this.comboArea = comboArea;
    }

    public List<SgCalificacionEstudiante> getCalificacionNoPertenecenSeccion() {
        return calificacionNoPertenecenSeccion;
    }

    public void setCalificacionNoPertenecenSeccion(List<SgCalificacionEstudiante> calificacionNoPertenecenSeccion) {
        this.calificacionNoPertenecenSeccion = calificacionNoPertenecenSeccion;
    }

    public Boolean getRespetarOrden() {
        return respetarOrden;
    }

    public void setRespetarOrden(Boolean respetarOrden) {
        this.respetarOrden = respetarOrden;
    }

    public Boolean getDeshabilitadoPorNoTenerValidada() {
        return deshabilitadoPorNoTenerValidada;
    }

    public Boolean getDeshabilitadoPorNoTenerProvisional() {
        return deshabilitadoPorNoTenerProvisional;
    }

}
