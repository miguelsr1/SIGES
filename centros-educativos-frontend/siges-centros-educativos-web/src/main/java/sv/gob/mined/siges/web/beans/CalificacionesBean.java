/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCalificacionCE;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacion;
import sv.gob.mined.siges.web.lazymodels.LazyCalificacionEstudianteDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.enumerados.EnumTiposPeriodosCalificaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgComponentePlanGrado;
import sv.gob.mined.siges.web.dto.SgPeriodoCalendario;
import sv.gob.mined.siges.web.dto.SgPeriodoCalificacion;
import sv.gob.mined.siges.web.dto.SgRangoFecha;
import sv.gob.mined.siges.web.dto.SgSeccion;
import sv.gob.mined.siges.web.dto.catalogo.SgPlantilla;
import sv.gob.mined.siges.web.enumerados.EnumAmbito;
import sv.gob.mined.siges.web.enumerados.EnumCalendarioEscolar;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroComponentePlanGrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalendario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPeriodoCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRangoFecha;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.ArchivoCalificacionesRestClient;
import sv.gob.mined.siges.web.restclient.CalendarioEscolarRestClient;
import sv.gob.mined.siges.web.restclient.ComponentePlanGradoRestClient;
import sv.gob.mined.siges.web.restclient.PeriodoCalificacionRestClient;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.restclient.RangoFechaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CalificacionesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalificacionesBean.class.getName());

    @Inject
    private CalificacionRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private SedeRestClient restSede;

    @Inject
    private FiltrosSeccionBean filtrosSeccion;

    @Inject
    private ArchivoCalificacionesRestClient archivoCalificacionRestClient;

    @Inject
    private PlantillaRestClient plantillaRestClient;
    
    @Inject
    private ComponentePlanGradoRestClient componentePlanGradoRestClient;
    
    @Inject
    private RangoFechaRestClient rangoFechaRestClient;
    
    @Inject
    private PeriodoCalificacionRestClient restPeriodoCalificacion;
    
    @Inject
    private CalendarioEscolarRestClient calendarioEscolarRestClient;

    private FiltroCalificacion filtro = new FiltroCalificacion();
    private SgCalificacionCE entidadEnEdicion = new SgCalificacionCE();
    private List<RevHistorico> historialCalificacionEstudiante = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCalificacionEstudianteDataModel calificacionEstudianteLazyModel;
    private SgPlantilla plantillaImportacion;
    private SgSeccion seccionEnEdicion;
    private SofisComboG<SgPeriodoCalificacion> comboPeriodoCalificacion;
    private SofisComboG<EnumCalendarioEscolar> comboTipoPeriodoCalendario;
    private SofisComboG<String> comboPeriodoCalendario;
    private SofisComboG<SgRangoFecha> comboRangoFecha;
    private SofisComboG<EnumTiposPeriodosCalificaciones> comboTipoPeriodoCalificacion;
    private SgComponentePlanGrado componentePlanGrado;
    private SofisComboG<SgComponentePlanGrado> comboComponentePlanGrado;
    private Integer comboPeriodoSeleccionado;
    private List<SelectItem> comboPeriodos;
    

    private SofisComboG<EnumAmbito> comboAmbito;

    public CalificacionesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            inicializarFiltrosSeccion();
            if (sessionBean.getAmbitosSeleccionablesBusqueda() != null && sessionBean.getAmbitosSeleccionablesBusqueda().size() > 1) {
                comboAmbito = new SofisComboG(sessionBean.getAmbitosSeleccionablesBusqueda(), "text");
            }
            obtenerPlantilla();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void obtenerPlantilla() {
        try {
            plantillaImportacion = plantillaRestClient.obtenerPorCodigo(Constantes.PLANTILLA_IMPORTACION_CALIFICACIONES);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void forceInit() {
        //Utilizado para forzar init de CalificacionesBean antes que FiltrosSeccionBean
    }

    public void inicializarFiltrosSeccion() {
        this.filtrosSeccion.setFiltro(this.filtro);
        this.filtrosSeccion.cargarCombos();
        this.filtrosSeccion.seleccionarUltimoAnio();
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_CALIFICACIONES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            if (comboAmbito != null) {
                filtro.setAmbito(comboAmbito.getSelectedT());
            }
            //No devolver NOTIN, APR, GRA..
            filtro.setCalTiposPeriodoCalificacion(Arrays.asList(new EnumTiposPeriodosCalificaciones[]{
                EnumTiposPeriodosCalificaciones.EXORD,
                EnumTiposPeriodosCalificaciones.ORD}));
            filtro.setCalComponentePlanEstudio(comboComponentePlanGrado.getSelectedT()!=null?comboComponentePlanGrado.getSelectedT().getCpgComponentePlanEstudio().getCpePk():null);
            filtro.setCalTipoPeriodoCalificacion(comboTipoPeriodoCalificacion.getSelectedT());
            filtro.setCalRangoFecha(comboRangoFecha.getSelectedT()!=null?comboRangoFecha.getSelectedT().getRfePk():null);
            filtro.setCalTipoCalendarioEscolar(comboTipoPeriodoCalendario.getSelectedT()!=null?comboTipoPeriodoCalendario.getSelectedT():null);
            filtro.setCalNumero(comboPeriodoSeleccionado>0?comboPeriodoSeleccionado:null);
            filtro.setIncluirCampos(new String[]{
                "calVersion",
                "calSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depNombre",
                "calSeccion.secServicioEducativo.sduSede.sedDireccion.dirMunicipio.munNombre",
                "calSeccion.secServicioEducativo.sduSede.sedCodigo",
                "calSeccion.secServicioEducativo.sduSede.sedNombre",
                "calSeccion.secServicioEducativo.sduSede.sedTipo",
                "calSeccion.secServicioEducativo.sduGrado.graNombre",
                "calSeccion.secNombre",
                "calComponentePlanEstudio.cpeNombre",
                "calComponentePlanEstudio.cpeTipo",
                "calUltModFecha",
                "calTipoPeriodoCalificacion",
                "calTipoCalendarioEscolar",
                "calNumero",
                "calRangoFecha.rfeCodigo",
                "calRangoFecha.rfePeriodoCalificacion.pcaNombre",
                "calTipoCalendarioEscolar",
                "calNumero"
            });

            totalResultados = restClient.buscarTotal(filtro);
            calificacionEstudianteLazyModel = new LazyCalificacionEstudianteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            List<EnumTiposPeriodosCalificaciones> listTipo=new ArrayList();
            listTipo.add(EnumTiposPeriodosCalificaciones.ORD);
            listTipo.add(EnumTiposPeriodosCalificaciones.EXORD);
            comboTipoPeriodoCalificacion= new SofisComboG(listTipo, "text");
            comboTipoPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboComponentePlanGrado = new SofisComboG();
            comboComponentePlanGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboPeriodoCalificacion = new SofisComboG();
            comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboRangoFecha = new SofisComboG();
            comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboPeriodoSeleccionado = 0;
            comboTipoPeriodoCalificacion.setSelected(-1);
            comboTipoPeriodoCalendario = new SofisComboG();
            comboTipoPeriodoCalendario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiar() {
        seccionEnEdicion = null;
        limpiarCombos();
        filtro = new FiltroCalificacion();
        this.filtrosSeccion.setFiltro(filtro);
        this.filtrosSeccion.limpiarCombos();
        comboTipoPeriodoCalificacion.setSelected(-1);
        buscar();
    }

    public void agregar() {
        entidadEnEdicion = new SgCalificacionCE();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgCalificacionCE var) {
        entidadEnEdicion = (SgCalificacionCE) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public void procesarArchivo() {
        try {
            archivoCalificacionRestClient.ejecutarProcesamientoArchivos();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialCalificacionEstudiante = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
     private void limpiarCombos() {
        comboComponentePlanGrado = new SofisComboG();
        comboComponentePlanGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboPeriodoCalificacion = new SofisComboG();
        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboRangoFecha = new SofisComboG();
        comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboPeriodoSeleccionado = 0;
        comboTipoPeriodoCalificacion.setSelected(-1);
    }

    public void componentePlanEstudioCargaCombo(SgSeccion sec) {
        try {
            limpiarCombos() ;
            seccionEnEdicion = sec;
            
            if (seccionEnEdicion != null) {
                if (seccionEnEdicion.getSecPlanEstudio() == null) {
                    return;
                }
                FiltroComponentePlanGrado fpg = new FiltroComponentePlanGrado();
                fpg.setCpgGradoPk(sec.getSecServicioEducativo().getSduGrado().getGraPk());
                fpg.setCpgPlanEstudioPk(sec.getSecPlanEstudio().getPesPk());
                fpg.setCpgAgregandoSeccionExclusiva(sec.getSecPk());
                fpg.setCpgCalificacionIngresadaCE(Boolean.TRUE);
                fpg.setIncluirCampos(new String[]{
                    "cpgNombrePublicable",
                    "cpgPeriodosCalificacion",
                    "cpgPlanEstudio.pesRelacionModalidad.reaModalidadEducativa.modPk",
                    "cpgPlanEstudio.pesRelacionModalidad.reaModalidadAtencion.matPk",
                    "cpgPlanEstudio.pesRelacionModalidad.reaSubModalidadAtencion.smoPk",
                    "cpgPlanEstudio.pesRelacionModalidad.reaModalidadEducativa.modCiclo.cicNivel.nivPk",
                    "cpgComponentePlanEstudio.cpePk",
                    "cpgComponentePlanEstudio.cpeCodigo",
                    "cpgComponentePlanEstudio.cpeNombre",
                    "cpgComponentePlanEstudio.cpeTipo",
                    "cpgComponentePlanEstudio.cpeVersion",
                    "cpgComponentePlanEstudio.cpeCodigoExterno",
                    "cpgComponentePlanEstudio.cpeNombrePublicable"});

                List<SgComponentePlanGrado> listCpg = componentePlanGradoRestClient.buscarConCache(fpg);

                comboComponentePlanGrado = new SofisComboG(listCpg, "cpgNombrePublicable");
                comboComponentePlanGrado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                comboComponentePlanGrado.ordenar();

               
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);

        }
    }
    
    public void componentePlanEstudioSelected() {
        comboPeriodoCalificacion = new SofisComboG();
        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboRangoFecha = new SofisComboG();
        comboRangoFecha.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboPeriodoSeleccionado = 0;
        comboTipoPeriodoCalificacion.setSelected(-1);

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
            comboPeriodoSeleccionado = 0;
            if (comboTipoPeriodoCalificacion.getSelectedT() != null && comboComponentePlanGrado.getSelectedT()!=null ) {
                

                switch (comboTipoPeriodoCalificacion.getSelectedT() ) {
                    case ORD:
                        FiltroPeriodoCalificacion fpc = new FiltroPeriodoCalificacion();
                        fpc.setIncluirCampos(new String[]{
                            "pcaNumero",
                            "pcaPermiteCalificarSinNie",
                            "pcaEsPrueba",
                            "pcaNombre",
                            "pcaTipoPeriodo",
                            "pcaNumeroPeriodo",
                            "pcaVersion"});
                        fpc.setPcaEsPrueba(Boolean.FALSE);
                        fpc.setPcaModalidadEducativa(comboComponentePlanGrado.getSelectedT().getCpgPlanEstudio().getPesRelacionModalidad().getReaModalidadEducativa().getModPk());
                        fpc.setPcaModalidadAtencion(comboComponentePlanGrado.getSelectedT().getCpgPlanEstudio().getPesRelacionModalidad().getReaModalidadAtencion().getMatPk());
                        fpc.setPcaNumero(comboComponentePlanGrado.getSelectedT().getCpgPeriodosCalificacion());
                        fpc.setPcaSubModalidadAtencion(comboComponentePlanGrado.getSelectedT().getCpgPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion() != null ? comboComponentePlanGrado.getSelectedT().getCpgPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                        fpc.setPcaAnioLectivo(seccionEnEdicion.getSecAnioLectivo().getAlePk());
                        fpc.setPcaTipoCalendario(seccionEnEdicion.getSecServicioEducativo().getSduSede().getSedTipoCalendario().getTcePk());

                        //Filtra dependiendo si periodo es anual o semestral
                        fpc.setPcaTipoPeriodo(seccionEnEdicion.getSecTipoPeriodo());
                        fpc.setPcaNumeroPeriodo(seccionEnEdicion.getSecNumeroPeriodo());

                        List<SgPeriodoCalificacion> listPeriodoCalif = restPeriodoCalificacion.buscar(fpc);
                        comboPeriodoCalificacion = new SofisComboG(listPeriodoCalif, "nombreTipoPeriodo");
                        comboPeriodoCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    
                      break;

                    case EXORD:
                        FiltroPeriodoCalendario fperCal = new FiltroPeriodoCalendario();
                        fperCal.setCesNivelFk(comboComponentePlanGrado.getSelectedT().getCpgPlanEstudio().getPesRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivPk());
                        fperCal.setCesModalidadAtencionFk(comboComponentePlanGrado.getSelectedT().getCpgPlanEstudio().getPesRelacionModalidad().getReaModalidadAtencion().getMatPk());
                        fperCal.setCesSubModalidadAtencionFk(comboComponentePlanGrado.getSelectedT().getCpgPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion() != null ? comboComponentePlanGrado.getSelectedT().getCpgPlanEstudio().getPesRelacionModalidad().getReaSubModalidadAtencion().getSmoPk() : null);
                        fperCal.setFechaCalificacion(LocalDate.now());
                        fperCal.setCesHabilitado(Boolean.TRUE);
                        fperCal.setIncluirCampos(new String[]{
                            "cesTipo",
                            "cesVersion"
                        });

                        List<SgPeriodoCalendario> listPeriodoCalendario = calendarioEscolarRestClient.buscar(fperCal);
                        List<EnumCalendarioEscolar> listEnumCalendario = new ArrayList();
                        for (SgPeriodoCalendario periodoCalendario : listPeriodoCalendario) {
                            if (!listEnumCalendario.contains(periodoCalendario.getCesTipo())) {
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.PREC) && comboComponentePlanGrado.getSelectedT().getCpgCantidadPrimeraPrueba() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.PREC);
                                }
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.PRECPS) && comboComponentePlanGrado.getSelectedT().getCpgCantidadPrimeraSuficiencia() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.PRECPS);
                                }
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.SREC) && comboComponentePlanGrado.getSelectedT().getCpgCantidadSegundaPrueba() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.SREC);
                                }
                                if (periodoCalendario.getCesTipo().equals(EnumCalendarioEscolar.SRECPS) && comboComponentePlanGrado.getSelectedT().getCpgCantidadSegundaSuficiencia() > 0) {
                                    listEnumCalendario.add(EnumCalendarioEscolar.SRECPS);
                                }
                            }
                        }

                        comboTipoPeriodoCalendario = new SofisComboG(listEnumCalendario, "text");
                        comboTipoPeriodoCalendario.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                        comboPeriodos = new ArrayList();
                        comboPeriodos.add(new SelectItem(0, Etiquetas.getValue("comboEmptyItem")));                      
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
            if (comboPeriodoCalificacion.getSelectedT() != null) {
                FiltroRangoFecha fre = new FiltroRangoFecha();
                fre.setPeriodoCalificacionPk(comboPeriodoCalificacion.getSelectedT().getPcaPk());
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
            Integer cantidadPrueba = 0;
            if (comboTipoPeriodoCalendario.getSelectedT() != null) {
                switch (comboTipoPeriodoCalendario.getSelectedT()) {
                    case PREC:
                        cantidadPrueba = comboComponentePlanGrado.getSelectedT().getCpgCantidadPrimeraPrueba();
                        break;
                    case PRECPS:
                        cantidadPrueba = comboComponentePlanGrado.getSelectedT().getCpgCantidadPrimeraSuficiencia();
                        break;
                    case SREC:
                        cantidadPrueba = comboComponentePlanGrado.getSelectedT().getCpgCantidadSegundaPrueba();
                        break;
                    case SRECPS:
                        cantidadPrueba = comboComponentePlanGrado.getSelectedT().getCpgCantidadSegundaSuficiencia();
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
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    public FiltroCalificacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCalificacion filtro) {
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

    public CalificacionRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(CalificacionRestClient restClient) {
        this.restClient = restClient;
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

    public SofisComboG<EnumAmbito> getComboAmbito() {
        return comboAmbito;
    }

    public void setComboAmbito(SofisComboG<EnumAmbito> comboAmbito) {
        this.comboAmbito = comboAmbito;
    }

    public SgPlantilla getPlantillaImportacion() {
        return plantillaImportacion;
    }

    public void setPlantillaImportacion(SgPlantilla plantillaImportacion) {
        this.plantillaImportacion = plantillaImportacion;
    }

    public FiltrosSeccionBean getFiltrosSeccion() {
        return filtrosSeccion;
    }

    public void setFiltrosSeccion(FiltrosSeccionBean filtrosSeccion) {
        this.filtrosSeccion = filtrosSeccion;
    }

    public SgSeccion getSeccionEnEdicion() {
        return seccionEnEdicion;
    }

    public void setSeccionEnEdicion(SgSeccion seccionEnEdicion) {
        this.seccionEnEdicion = seccionEnEdicion;
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

    public SofisComboG<String> getComboPeriodoCalendario() {
        return comboPeriodoCalendario;
    }

    public void setComboPeriodoCalendario(SofisComboG<String> comboPeriodoCalendario) {
        this.comboPeriodoCalendario = comboPeriodoCalendario;
    }

    public SofisComboG<SgRangoFecha> getComboRangoFecha() {
        return comboRangoFecha;
    }

    public void setComboRangoFecha(SofisComboG<SgRangoFecha> comboRangoFecha) {
        this.comboRangoFecha = comboRangoFecha;
    }

    public SofisComboG<EnumTiposPeriodosCalificaciones> getComboTipoPeriodoCalificacion() {
        return comboTipoPeriodoCalificacion;
    }

    public void setComboTipoPeriodoCalificacion(SofisComboG<EnumTiposPeriodosCalificaciones> comboTipoPeriodoCalificacion) {
        this.comboTipoPeriodoCalificacion = comboTipoPeriodoCalificacion;
    }

    public SofisComboG<SgComponentePlanGrado> getComboComponentePlanGrado() {
        return comboComponentePlanGrado;
    }

    public void setComboComponentePlanGrado(SofisComboG<SgComponentePlanGrado> comboComponentePlanGrado) {
        this.comboComponentePlanGrado = comboComponentePlanGrado;
    }

 

    public Integer getComboPeriodoSeleccionado() {
        return comboPeriodoSeleccionado;
    }

    public void setComboPeriodoSeleccionado(Integer comboPeriodoSeleccionado) {
        this.comboPeriodoSeleccionado = comboPeriodoSeleccionado;
    }

    public List<SelectItem> getComboPeriodos() {
        return comboPeriodos;
    }

    public void setComboPeriodos(List<SelectItem> comboPeriodos) {
        this.comboPeriodos = comboPeriodos;
    }

}
