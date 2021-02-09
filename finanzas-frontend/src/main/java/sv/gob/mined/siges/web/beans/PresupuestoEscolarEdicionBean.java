/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.jsoup.Jsoup;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioFiscal;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgAutorizacionEdicionPresupuesto;
import sv.gob.mined.siges.web.dto.SgDetallePlanEscolar;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgMovimientosEdicion;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.SgRubros;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.siap2.SgAreaInversion;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.web.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioFiscal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAreaInversion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAutorizacionEdicionPresupuesto;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDetallePlanEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientosEdicion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRubros;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyPresupuestosEscolaresDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioFiscalRestClient;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.AreaInversionRestClient;
import sv.gob.mined.siges.web.restclient.AutorizacionEdicionPresupuestoRestClient;
import sv.gob.mined.siges.web.restclient.DetallePlanEscolarRestClient;
import sv.gob.mined.siges.web.restclient.MovimientosEdicionRestClient;
import sv.gob.mined.siges.web.restclient.MovimientosRestClient;
import sv.gob.mined.siges.web.restclient.PlanEscolarAnualRestClient;
import sv.gob.mined.siges.web.restclient.PresupuestoEscolarRestCliente;
import sv.gob.mined.siges.web.restclient.RubrosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de presupuesto escolar edición.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PresupuestoEscolarEdicionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName());

    @Inject
    private PresupuestoEscolarRestCliente restClient;
    @Inject
    private SessionBean sessionBean;
    @Inject
    private AnioLectivoRestClient anioLectRestClient;
    @Inject
    private SedeRestClient sedeClient;
    @Inject
    private MovimientosRestClient movimientosClient;

    @Inject
    private MovimientosEdicionRestClient edicionMovimientosClient;

    @Inject
    private AutorizacionEdicionPresupuestoRestClient autorizacionesClient;

    @Inject
    private DetallePlanEscolarRestClient restDetalle;

    @Inject
    private PlanEscolarAnualRestClient restPlanEscolarAnual;

    @Inject
    private RubrosRestClient rubroRestClient;

    @Inject
    private AreaInversionRestClient areasInversionRestClient;
    @Inject
    private AnioFiscalRestClient anioFiscalRestClient;

    @Inject
    @Param(name = "id")
    private Long presupuestoEscolarId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    private Locale locale;
    private SgPresupuestoEscolar entidadEnEdicion = new SgPresupuestoEscolar();
    private SgAreaInversion areaInversionEnEdicion = new SgAreaInversion();
    private FiltroPresupuestoEscolar filtro = new FiltroPresupuestoEscolar();
    private FiltroAreaInversion adit = new FiltroAreaInversion();
    private SgSede sedeSeleccionada;
    private Long totalResultados;
    private LazyPresupuestosEscolaresDataModel reglasPresupuestoEscolarDataModel;
    private Integer paginado = 5;
    private List<RevHistorico> historialPresupuestoEscolar = new ArrayList();
    private SofisComboG<SgAnioLectivo> anioLectivoCombo;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean permiteEdicion = Boolean.FALSE;
    private Integer selectedTab;
    private List<SgMovimientos> ingresosList;
    private List<SgMovimientos> ingresosNuevosBusqueda;
    private List<SgMovimientos> egresosList;
    private List<SgMovimientos> edicionIngresosList;
    private List<SgMovimientos> edicionEgresosList;
    private List<SgMovimientos> transferenciaList;
    private SgMovimientos movimientoEnEdicion;
    private SgMovimientosEdicion edicionMovimientoEnEdicion;
    private SofisComboG<SgDetallePlanEscolar> detPlanEscolarCombo = new SofisComboG<>();
    private SofisComboG<SgMovimientos> fuenteIngresoCombo = new SofisComboG<>();
    private String leyendaTitulo = "Edición de Movimiento";
    private SofisComboG<SgRubros> rubrosCombo = new SofisComboG<>();
    private SgMovimientos movimientoAEliminar;
    private SofisComboG<SgAreaInversion> areaInversionCombo = new SofisComboG<>();
    private SofisComboG<SgAreaInversion> subareaInversionCombo = new SofisComboG<>();
    private List<SgMovimientos> eneroList;
    private SofisComboG<SgAnioFiscal> anioFiscalCombo;
    private List<SgMovimientos> nuevosIngresos = new ArrayList<>();
    private List<SgMovimientos> nuevosEgresos = new ArrayList<>();
    private List<SgMovimientos> ingresosEditados = new ArrayList<>();
    private List<SgMovimientos> egresosEditados = new ArrayList<>();
    private SgMovimientos movEnEdicion;
    private Boolean transferencia = Boolean.FALSE;
    private FiltroMovimientos filtromov = new FiltroMovimientos();
    private BigDecimal egresosGlobales = BigDecimal.ZERO;
    private Integer conteoSaldoOtrosIngresos = 0;
    private Integer conteoSaldoTransferecias = 0;
    private Integer conteoSaldoTransfereciasSaldoEgresosAprobado = 0;
    private SgAreaInversion areaInversionSeleccionada;
    private SgAreaInversion subAreaInversionSeleccionada;
    EnumPresupuestoEscolarEstado estadoActual;
    private Boolean controlInput = Boolean.FALSE;
    private Boolean botonGuardar = Boolean.FALSE;
    private Boolean botonFormulacion = Boolean.FALSE;
    private Boolean botonAjuste = Boolean.FALSE;
    private Boolean botonAprobado = Boolean.FALSE;
    private Boolean botonEdicion = Boolean.FALSE;
    private FiltroMovimientos filtromove = new FiltroMovimientos();
    private List<SgMovimientos> listMovPresupuesto = new ArrayList<>();
    private List<SgMovimientos> listaAnulEditado = new ArrayList();
    private List<SgMovimientos> listaAnulOriginal = new ArrayList();
    private List<SgMovimientos> listaMovEditado = new ArrayList();
    private String textoConfirmacionEliminacion = "";
    private String textoConfirmacionAnulacion = "";
    private Boolean anulacion = Boolean.FALSE;
    private String titleEdicion = "";
    private SgDetallePlanEscolar pea = new SgDetallePlanEscolar();
    private Long peaId;

    private Boolean saldoNegativoOtrosIngresos = Boolean.FALSE;
    private Boolean saldoCeroTransferecias = Boolean.FALSE;
    private Boolean saldoNegativoSaldoEgresosAprobado = Boolean.FALSE;

    private SgAreaInversion padre = new SgAreaInversion();
    private BigDecimal totali = BigDecimal.ZERO;

    private BigDecimal totale = BigDecimal.ZERO;

    private BigDecimal totalt = BigDecimal.ZERO;

    private BigDecimal total_it = BigDecimal.ZERO;

    private BigDecimal total_ita = BigDecimal.ZERO;

    private BigDecimal totalIAprobado = BigDecimal.ZERO;
    private BigDecimal totalTAprobado = BigDecimal.ZERO;
    private BigDecimal totalEAprobado = BigDecimal.ZERO;
    private BigDecimal totalActividad = BigDecimal.ZERO;

    private BigDecimal totalFEnero = BigDecimal.ZERO;
    private BigDecimal totalFFebrero = BigDecimal.ZERO;
    private BigDecimal totalFMarzo = BigDecimal.ZERO;
    private BigDecimal totalFAbril = BigDecimal.ZERO;
    private BigDecimal totalFMayo = BigDecimal.ZERO;
    private BigDecimal totalFJunio = BigDecimal.ZERO;
    private BigDecimal totalFJulio = BigDecimal.ZERO;
    private BigDecimal totalFAgosto = BigDecimal.ZERO;
    private BigDecimal totalFSeptiembre = BigDecimal.ZERO;
    private BigDecimal totalFOctubre = BigDecimal.ZERO;
    private BigDecimal totalFNoviembre = BigDecimal.ZERO;
    private BigDecimal totalFDiciembre = BigDecimal.ZERO;
    private BigDecimal totalAnual = BigDecimal.ZERO;

    private BigDecimal totalFEneroI = BigDecimal.ZERO;
    private BigDecimal totalFFebreroI = BigDecimal.ZERO;
    private BigDecimal totalFMarzoI = BigDecimal.ZERO;
    private BigDecimal totalFAbrilI = BigDecimal.ZERO;
    private BigDecimal totalFMayoI = BigDecimal.ZERO;
    private BigDecimal totalFJunioI = BigDecimal.ZERO;
    private BigDecimal totalFJulioI = BigDecimal.ZERO;
    private BigDecimal totalFAgostoI = BigDecimal.ZERO;
    private BigDecimal totalFSeptiembreI = BigDecimal.ZERO;
    private BigDecimal totalFOctubreI = BigDecimal.ZERO;
    private BigDecimal totalFNoviembreI = BigDecimal.ZERO;
    private BigDecimal totalFDiciembreI = BigDecimal.ZERO;
    private BigDecimal totalAnualI = BigDecimal.ZERO;

    private BigDecimal totalFEneroT = BigDecimal.ZERO;
    private BigDecimal totalFFebreroT = BigDecimal.ZERO;
    private BigDecimal totalFMarzoT = BigDecimal.ZERO;
    private BigDecimal totalFAbrilT = BigDecimal.ZERO;
    private BigDecimal totalFMayoT = BigDecimal.ZERO;
    private BigDecimal totalFJunioT = BigDecimal.ZERO;
    private BigDecimal totalFJulioT = BigDecimal.ZERO;
    private BigDecimal totalFAgostoT = BigDecimal.ZERO;
    private BigDecimal totalFSeptiembreT = BigDecimal.ZERO;
    private BigDecimal totalFOctubreT = BigDecimal.ZERO;
    private BigDecimal totalFNoviembreT = BigDecimal.ZERO;
    private BigDecimal totalFDiciembreT = BigDecimal.ZERO;
    private BigDecimal totalAnualT = BigDecimal.ZERO;

    /**
     * Constructor.
     */
    public PresupuestoEscolarEdicionBean() {
    }

    /**
     * Inicializa el objeto después de crearse.
     */
    @PostConstruct
    public void init() {
        try {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            cargarCombos();
            if (presupuestoEscolarId != null && presupuestoEscolarId > 0) {
                filtro.setIncluirCampos(new String[]{
                    "pesPk",
                    "pesNombre",
                    "pesNombreBusqueda",
                    "pesDescripcion",
                    "pesVersion",
                    "pesEstado",
                    "pesCodigo",
                    "pesDescripcion",
                    "pesHabilitado",
                    "pesSedeFk.sedPk",
                    "pesSedeFk.sedCodigo",
                    "pesSedeFk.sedNombre",
                    "pesSedeFk.sedTipo",
                    "pesSedeFk.sedVersion",
                    "pesAnioFiscalFk.aniPk",
                    "pesAnioFiscalFk.aniAnio",
                    "pesAnioFiscalFk.aniVersion"
                });
                filtro.setPesPk(presupuestoEscolarId);
                List<SgPresupuestoEscolar> list = restClient.buscar(filtro);
                if (!list.isEmpty()) {
                    entidadEnEdicion = list.get(0);
                    if (entidadEnEdicion != null) {
                        actualizar(entidadEnEdicion);
                        soloLectura = editable != null ? !editable : soloLectura;
                        if (entidadEnEdicion.getPesAnioFiscalFk() != null && entidadEnEdicion.getPesSedeFk() != null && entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.APROBADO)) {
                            controlInput = Boolean.TRUE;
                        }
                        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EDITADO)) {
                            permiteEdicion = Boolean.TRUE;
                        } else if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.APROBADO)) {
                            permiteEdicion = Boolean.FALSE;
                        }
                        inicializarBotones();
                        inicializarColumnaAcciones();
                        prepararAnulacion(entidadEnEdicion);
                        actualizar(entidadEnEdicion);
                        PlanEscolarAnual();

                    } else {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                    }
                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_PRESUPUESTO_NO_EXITE), "");
                }
            } else {
                agregar();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Maneja el control render de los botones de Columna Acciones
     */
    public void inicializarColumnaAcciones() {
        permiteEdicion = Boolean.TRUE;
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.APROBADO)) {
            permiteEdicion = Boolean.FALSE;
        }
    }

    /**
     * Inicializa el elemento a Anular
     *
     * @param SgPresupuestoEscolar
     */
    public void prepararAnulacion(SgPresupuestoEscolar pes) {
        try {
            entidadEnEdicion = restClient.prepararAnulacion(pes);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Maneja el control render de los botones a mostrar.
     */
    public void inicializarBotones() {
        botonEdicion = Boolean.FALSE;
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_PROCESO)) {
            botonFormulacion = Boolean.TRUE;
            botonGuardar = Boolean.TRUE;
        }
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.FORMULADO)) {
            botonAjuste = Boolean.TRUE;
            botonGuardar = Boolean.TRUE;
        }
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE)) {
            botonAprobado = Boolean.TRUE;
            botonGuardar = Boolean.TRUE;
        }
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.APROBADO)) {
            botonEdicion = Boolean.TRUE;
        }
    }

    /**
     * Carga los combos de búsqueda.
     */
    public void cargarCombos() {
        try {
            FiltroAnioFiscal fis = new FiltroAnioFiscal();
            fis.setOrderBy(new String[]{"aniAnio"});
            fis.setAscending(new boolean[]{false});
            List<SgAnioFiscal> listAnioFis = anioFiscalRestClient.buscar(fis);
            anioFiscalCombo = new SofisComboG(listAnioFis, "aniAnio");
            anioFiscalCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            detPlanEscolarCombo = new SofisComboG();
            areaInversionCombo = new SofisComboG();
            areaInversionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            cargarComboAreaInversion();
            subareaInversionCombo = new SofisComboG();
            subareaInversionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga las actividades relacionadas a la sede, y año fiscal del
     * presupuesto.
     */
    private void cargarComboActividades() {
        try {

            FiltroDetallePlanEscolar filtro = new FiltroDetallePlanEscolar();
            filtro.setSedePk(entidadEnEdicion.getPesSedeFk().getSedPk());
            List<SgDetallePlanEscolar> resultado = restDetalle.buscar(filtro);
            for (SgDetallePlanEscolar det : resultado) {
                det.setDpeActividad(Jsoup.parse(det.getDpeActividad()).text());
            }
            List<SgDetallePlanEscolar> nuevoResultado = resultado
                    .stream()
                    .filter(x -> x.getDpePlanEscolarAnual().getPeaAnioLectivo().getAleAnio().equals(entidadEnEdicion.getPesAnioFiscalFk().getAniAnio()))
                    .collect(Collectors.toList());
            detPlanEscolarCombo = new SofisComboG(nuevoResultado, "dpeActividad");
            detPlanEscolarCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Booleano que controla el ajax y deshabilitar el combo de Area de
     * Inversion.
     */
    public Boolean isTrasferencia() {
        if (fuenteIngresoCombo.getSelected() != null) {
            if (fuenteIngresoCombo.getSelectedT().getMovOrigen().equals(EnumMovimientosOrigen.T)) {
                transferencia = Boolean.TRUE;
                areaInversionSeleccionada = new SgAreaInversion();
                return true;
            } else {
                transferencia = Boolean.FALSE;
                return false;
            }
        }
        return false;
    }

    /**
     * Carga las fuentes de ingresos relacionadas al presupuesto.
     */
    private void cargarComboFuenteIngresos() {
        try {
            List<SgMovimientos> resultado
                    = listMovPresupuesto
                            .stream()
                            .filter(x -> EnumMovimientosTipo.I.equals(x.getMovTipo()))
                            .filter(x -> x.getMovNumMoviento() != null)
                            .sorted((i1, i2) -> i1.getMovNumMoviento().compareTo(i2.getMovNumMoviento()))
                            .collect(Collectors.toList());

            FiltroMovimientosEdicion filtroMovEdicion = new FiltroMovimientosEdicion();
            filtroMovEdicion.setOrderBy(new String[]{"movPk"});
            filtroMovEdicion.setAscending(new boolean[]{true});
            filtroMovEdicion.setMovPresupuestoFk(entidadEnEdicion.getPesPk());
            filtroMovEdicion.setMovTipo(EnumMovimientosTipo.IM);
            List<SgMovimientos> listaMovEdicion = edicionMovimientosClient.buscar(filtroMovEdicion);
            if (!listaMovEdicion.isEmpty()) {
                resultado.addAll(listaMovEdicion);
            }

            fuenteIngresoCombo = new SofisComboG(resultado, "movFuenteIngresoEdicion");
            fuenteIngresoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga el combo de Rubro relacionado al las actividades del sede.
     */
    private void cargarComboRubros() {
        try {
            FiltroRubros rub = new FiltroRubros();
            rub.setOrderBy(new String[]{"ruNombre"});
            rub.setAscending(new boolean[]{true});
            List<SgRubros> listRubro = rubroRestClient.buscar(rub);
            rubrosCombo = new SofisComboG(listRubro, "ruNombre");
            rubrosCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga el combo autocomplete de las áreas realacionadas de inversión
     *
     * @param query
     * @return
     */
    public List<SgAreaInversion> completeAreaInversion(String query) {
        try {
            FiltroAreaInversion adi = new FiltroAreaInversion();
            adi.setAdiNombre(query);
            adi.setAdiHabilitado(Boolean.TRUE);
            adi.setMaxResults(11L);
            adi.setOrderBy(new String[]{"adiNombre"});
            adi.setAscending(new boolean[]{true});
            adi.setIncluirCampos(new String[]{"adiPk", "adiNombre", "adiVersion"});
            return areasInversionRestClient.buscar(adi);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    /**
     * Carga el combo oncomplete de sub áreas de Inversión relacionadas a las
     * áreas.
     *
     * @param query
     * @return
     */
    public List<SgAreaInversion> completeSubAreaInversion(String query) {
        try {
            if (transferencia) {
                Long pkMovimientoSelect = fuenteIngresoCombo.getSelectedT().getMovPk();
                adit.setAscending(new boolean[]{true});
                return areasInversionRestClient.buscarSubAreaTransferencia(pkMovimientoSelect);

            } else {
                FiltroAreaInversion adi = new FiltroAreaInversion();
                adi.setOrderBy(new String[]{"adiNombre"});
                adi.setAscending(new boolean[]{true});
                adi.setIncluirCampos(new String[]{"adiPk", "adiNombre", "adiVersion", "adiPadrePk"});

                if (areaInversionSeleccionada != null) {
                    adi.setAdiPadrePk(areaInversionSeleccionada.getAdiPk());
                }
                return areasInversionRestClient.buscar(adi);
            }

        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    /**
     * Carga el combo de áreas de Inversión
     */
    private void cargarComboAreaInversion() {
        try {
            FiltroAreaInversion adi = new FiltroAreaInversion();
            adi.setOrderBy(new String[]{"adiNombre"});
            adi.setAscending(new boolean[]{true});
            adi.setIncluirCampos(new String[]{"adiPk", "adiNombre", "adiVersion",});
            List<SgAreaInversion> listAreaInversion = areasInversionRestClient.buscarAreaPrincipal(adi);
            areaInversionCombo = new SofisComboG(listAreaInversion, "adiNombre");
            areaInversionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga el combo de sub áreas de Inversión
     */
    public void cargarComboSubAreaInversion() {
        try {

            if (transferencia) {
                areaInversionCombo = new SofisComboG();
                areaInversionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                subareaInversionCombo = new SofisComboG();
                subareaInversionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                Long pkMovimientoSelect = fuenteIngresoCombo.getSelectedT().getMovPk();
                adit.setAscending(new boolean[]{true});
                List<SgAreaInversion> listAreaInversionT = areasInversionRestClient.buscarSubAreaTransferencia(pkMovimientoSelect);
                subareaInversionCombo = new SofisComboG(listAreaInversionT, "adiNombre");
                subareaInversionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            } else {
                FiltroAreaInversion adi = new FiltroAreaInversion();
                adi.setOrderBy(new String[]{"adiNombre"});
                adi.setAscending(new boolean[]{true});
                adi.setIncluirCampos(new String[]{"adiPk", "adiNombre", "adiVersion", "adiPadrePk"});

                if (areaInversionSeleccionada != null) {
                    adi.setAdiPadrePk(areaInversionSeleccionada.getAdiPk());
                }
                List<SgAreaInversion> listSubAreaInversion = areasInversionRestClient.buscar(adi);
                subareaInversionCombo = new SofisComboG(listSubAreaInversion, "adiNombre");
                subareaInversionCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            }
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Inicializa el objeto.
     */
    public void agregar() {
        entidadEnEdicion = new SgPresupuestoEscolar();
    }

    /**
     * Busca los datos que satisfacen el filtro.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            reglasPresupuestoEscolarDataModel = new LazyPresupuestosEscolaresDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los datos de un presupuesto para poder ser editados.
     *
     * @param var
     */
    public void actualizar(SgPresupuestoEscolar req) {

        ingresosList = new ArrayList();
        egresosList = new ArrayList();
        transferenciaList = new ArrayList();
        try {
            if (req.getPesPk() != null) {

                entidadEnEdicion = req;
                if (req.getPesAnioFiscalFk() != null) {
                    anioFiscalCombo.setSelectedT(req.getPesAnioFiscalFk());
                }
                sedeSeleccionada = req.getPesSedeFk();

                filtromove.setIncluirCampos(new String[]{
                    "movPk",
                    "movTechoPresupuestal.subComponente.gesId",
                    "movTechoPresupuestal.subComponente.gesNombre",
                    "movTechoPresupuestal.subComponente.gesVersion",
                    "movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeId",
                    "movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeNombre",
                    "movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeVersion",
                    "movTechoPresupuestal.subCuenta.cuId",
                    "movTechoPresupuestal.subCuenta.cuNombre",
                    "movTechoPresupuestal.subCuenta.cuCuentaPadre.cuNombre",
                    "movTechoPresupuestal.subCuenta.cuVersion",
                    "movFuenteRecursos",
                    "movPresupuestoFk", // revisar
                    "movTipo",
                    "movMonto",
                    "movVersion",
                    "movNumMoviento",
                    "movOrigen",
                    "movAnulacion",
                    "movEditado",
                    "movMontoAprobado",
                    "movRubroPk.ruId",
                    "movRubroPk.ruNombre",
                    "movRubroPk.ruCodigo",
                    "movRubroPk.ruHabilitado",
                    "movRubroPk.ruUltmodFecha",
                    "movRubroPk.ruUltmodUsuario",
                    "movRubroPk.ruVersion",
                    "movActividadPk.dpePk",
                    "movActividadPk.dpeActividad",
                    "movActividadPk.dpeVersion",
                    "movActividadPk.dpePlanEscolarAnual.peaAnioLectivo.alePk",
                    "movActividadPk.dpePlanEscolarAnual.peaAnioLectivo.aleAnio",
                    "movActividadPk.dpePlanEscolarAnual.peaAnioLectivo.aleVersion",
                    "movFuenteIngresos.movPk",
                    "movFuenteIngresos.movVersion",
                    "movFuenteIngresos.movNumMoviento",
                    "movFuenteIngresos.movFuenteRecursos",
                    "movFuenteIngresos.movRubroPk.ruId",
                    "movFuenteIngresos.movRubroPk.ruNombre",
                    "movFuenteIngresos.movVersion",
                    "movFuenteIngresos.movVersion",
                    "movAreaInversionPk.adiPk",
                    "movAreaInversionPk.adiNombre",
                    "movAreaInversionPk.adiDescripcion",
                    "movAreaInversionPk.adiVersion",
                    "movSubAreaInversionPk.adiPk",
                    "movSubAreaInversionPk.adiNombre",
                    "movSubAreaInversionPk.adiDescripcion",
                    "movSubAreaInversionPk.adiVersion"});
                filtromove.setOrderBy(new String[]{"movNumMoviento"});
                filtromove.setAscending(new boolean[]{true});
                filtromove.setMovPresupuestoFk(entidadEnEdicion.getPesPk());
                listMovPresupuesto = movimientosClient.buscar(filtromove);

                FiltroMovimientosEdicion filtroMovEd = new FiltroMovimientosEdicion();
                filtroMovEd.setOrderBy(new String[]{"movNumMoviento"});
                filtroMovEd.setAscending(new boolean[]{true});
                filtroMovEd.setMovPresupuestoFk(entidadEnEdicion.getPesPk());
                List<SgMovimientos> listaMovEdicion = edicionMovimientosClient.buscar(filtroMovEd);

                if (listMovPresupuesto != null && !listMovPresupuesto.isEmpty()) {

                    ingresosList = listMovPresupuesto.stream()
                            .filter(c -> c.getMovTipo().equals(EnumMovimientosTipo.I) && c.getMovOrigen().equals(EnumMovimientosOrigen.P))
                            .filter(c -> c.getMovEditado().equals(Boolean.FALSE))
                            .collect(Collectors.toList());

                    egresosList = listMovPresupuesto.stream()
                            .filter(c -> c.getMovTipo().equals(EnumMovimientosTipo.E))
                            .filter(c -> c.getMovEditado().equals(Boolean.FALSE))
                            .collect(Collectors.toList());

                    if (!listMovPresupuesto.isEmpty()) {
                        edicionIngresosList = listaMovEdicion.stream().filter(c -> c.getMovTipo().equals(EnumMovimientosTipo.IM) && c.getMovOrigen().equals(EnumMovimientosOrigen.P)).collect(Collectors.toList());
                        edicionEgresosList = listaMovEdicion.stream().filter(c -> c.getMovTipo().equals(EnumMovimientosTipo.EM)).collect(Collectors.toList());
                        ingresosList.addAll(edicionIngresosList);
                        egresosList.addAll(edicionEgresosList);

                    }

                    transferenciaList = listMovPresupuesto.stream().filter(c -> c.getMovTipo().equals(EnumMovimientosTipo.I) && c.getMovOrigen().equals(EnumMovimientosOrigen.T)).collect(Collectors.toList());
                    transferenciaList.forEach(x
                            -> x.setSaldoEgresos(
                                    x.getMovMonto().subtract(
                                            egresosList.stream()
                                                    .filter(y -> y.getMovFuenteIngresos() != null)
                                                    .filter(y -> y.getMovFuenteIngresos().getMovPk().equals(x.getMovPk()))
                                                    .map(SgMovimientos::getMovMonto)
                                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    )
                            )
                    );
                    ingresosList.forEach(x -> {
                        if (x.getMovTipo().equals(EnumMovimientosTipo.I)) {
                            x.setSaldoEgresos(
                                    x.getMovMonto().subtract(
                                            egresosList.stream()
                                                    .filter(y -> y.getMovFuenteIngresos() != null)
                                                    .filter(y -> y.getMovFuenteIngresos().getMovPk().equals(x.getMovPk()))
                                                    .map(SgMovimientos::getMovMonto)
                                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    )
                            );
                        } else if (x.getMovTipo().equals(EnumMovimientosTipo.IM)) {
                            x.setSaldoEgresos(
                                    x.getMovMonto().subtract(
                                            egresosList.stream()
                                                    .filter(y -> y.getMovFuenteIngresosOriginal() != null)
                                                    .filter(y -> y.getMovFuenteIngresosOriginal().getMovPk().equals(x.getMovPk()))
                                                    .map(SgMovimientos::getMovMonto)
                                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    )
                            );
                        }

                    });

                    transferenciaList.stream().filter(t -> t.getMovMontoAprobado() != null).forEach(x
                            -> x.setSaldoEgresosAprobado(
                                    x.getMovMontoAprobado().subtract(
                                            egresosList.stream()
                                                    .filter(y -> y.getMovFuenteIngresos() != null)
                                                    .filter(y -> y.getMovFuenteIngresos().getMovPk().equals(x.getMovPk()))
                                                    .map(SgMovimientos::getMovMontoAprobado)
                                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    ))
                    );
                }

            }
            actualizarTotales();
            actualizarTotalAnual();
            actualizarTotalAnualIngresos();
            actualizarTotalAnualTransferencias();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Actualiza los totales de los registros númericos
     */
    public void actualizarTotales() {
        totali = ingresosList.stream().filter(x -> x.getMovMonto() != null).map(e -> e.getMovMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
        totale = egresosList.stream().filter(x -> x.getMovMonto() != null).map(e -> e.getMovMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
        totalt = transferenciaList.stream().filter(x -> x.getMovMonto() != null).map(e -> e.getMovMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
        totalIAprobado = BigDecimal.ZERO;
        totalTAprobado = BigDecimal.ZERO;
        totalEAprobado = BigDecimal.ZERO;
        ingresosList.stream().filter(x -> x.getMovMontoAprobado() != null).forEach(x -> totalIAprobado.add(x.getMovMontoAprobado()));
        egresosList.stream().filter(x -> x.getMovMontoAprobado() != null).forEach(x -> totalEAprobado.add(x.getMovMontoAprobado()));
        transferenciaList.stream().filter(x -> x.getMovMontoAprobado() != null).forEach(x -> totalTAprobado.add(x.getMovMontoAprobado()));
        total_it = totali.add(totalt);

        totalEAprobado = egresosList
                .stream()
                .filter(x -> x.getMovMontoAprobado() != null)
                .map(SgMovimientos::getMovMontoAprobado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalIAprobado = ingresosList
                .stream()
                .filter(x -> x.getMovMontoAprobado() != null)
                .map(SgMovimientos::getMovMontoAprobado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalTAprobado = transferenciaList
                .stream()
                .filter(x -> x.getMovMontoAprobado() != null)
                .map(SgMovimientos::getMovMontoAprobado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        total_ita = (totalTAprobado);

    }

    /**
     * Crea un nuevo Inggreso de un presupuesto escolar
     */
    public void agregarIngreso() {
        JSFUtils.limpiarMensajesError();
        movimientoEnEdicion = new SgMovimientos();
        movimientoEnEdicion.setMovTipo(EnumMovimientosTipo.IM);
        movimientoEnEdicion.setMovOrigen(EnumMovimientosOrigen.P);
        leyendaTitulo = "Ingreso";
        cargarComboRubros();

    }

    /**
     * Crea un nuevo egreso de un presupuesto escolar
     */
    public void agregarEgreso() {
        JSFUtils.limpiarMensajesError();
        areaInversionSeleccionada = null;
        subAreaInversionSeleccionada = null;
        movimientoEnEdicion = new SgMovimientos();
        if (!entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE)) {
            movimientoEnEdicion.setMovMontoAprobado(BigDecimal.ZERO);
        }
        movimientoEnEdicion.setMovTipo(EnumMovimientosTipo.EM);
        cargarComboActividades();
        cargarComboFuenteIngresos();
        leyendaTitulo = "Egreso";

    }

    /**
     * Carga los datos de un egreso para su edicción.
     */
    public void editarEgreso(SgMovimientos mov) {
        try {
            if (mov.getMovPk() != null) {
                anulacion = Boolean.FALSE;
                leyendaTitulo = "Edición de Movimiento";
                movimientoEnEdicion = mov;
                cargarComboActividades();
                detPlanEscolarCombo.setSelectedT(movimientoEnEdicion.getMovActividadPk());
                cargarComboFuenteIngresos();
                if (mov.getMovFuenteIngresos() != null) {
                    fuenteIngresoCombo.setSelectedT(movimientoEnEdicion.getMovFuenteIngresos());
                } else if (mov.getMovFuenteIngresosOriginal() != null) {
                    fuenteIngresoCombo.setSelectedT(movimientoEnEdicion.getMovFuenteIngresosOriginal());
                }

                areaInversionSeleccionada = movimientoEnEdicion.getMovAreaInversionPk();
                subAreaInversionSeleccionada = movimientoEnEdicion.getMovSubAreaInversionPk();

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los datos de un Ingreso para su edicción.
     */
    public void editarIngreso(SgMovimientos mov) {
        try {
            if (mov.getMovPk() != null && mov.getMovTipo().equals(EnumMovimientosTipo.IM)) {
                anulacion = Boolean.FALSE;
                JSFUtils.limpiarMensajesError();
                leyendaTitulo = "Edición de Movimiento";
                movimientoEnEdicion = mov;
                cargarComboRubros();
                rubrosCombo.setSelectedT(movimientoEnEdicion.getMovRubroPk());
                cargarComboActividades();
                detPlanEscolarCombo.setSelectedT(movimientoEnEdicion.getMovActividadPk());
                PrimeFaces.current().executeScript("PF('itemDialogEgresos').show()");
            } else if (mov.getMovTipo().equals(EnumMovimientosTipo.I)) {

                FiltroMovimientos ingreso = new FiltroMovimientos();
                ingreso.setOrderBy(new String[]{"movPk"});
                ingreso.setIncluirCampos(new String[]{
                    "movPk",
                    "movFuenteRecursos",
                    "movTipo",
                    "movMonto",
                    "movVersion",
                    "movFuenteIngresos.movPk",
                    "movFuenteIngresos.movVersion",
                    "movFuenteIngresos.movNumMoviento"
                });
                ingreso.setMovFuenteIngresos(mov.getMovPk());
                ingreso.setAscending(new boolean[]{true});
                ingreso.setMovPresupuestoFk(entidadEnEdicion.getPesPk());
                List<SgMovimientos> resultadoIngresos = movimientosClient.buscar(ingreso);

                if (!resultadoIngresos.isEmpty()) {
                    anulacion = Boolean.FALSE;
                    JSFUtils.limpiarMensajesError();
                    leyendaTitulo = "Edición de Movimiento";
                    movimientoEnEdicion = mov;
                    cargarComboRubros();
                    rubrosCombo.setSelectedT(movimientoEnEdicion.getMovRubroPk());
                    cargarComboActividades();
                    detPlanEscolarCombo.setSelectedT(movimientoEnEdicion.getMovActividadPk());
                    PrimeFaces.current().executeScript("PF('itemDialogEgresos').show()");
                } else {
                    anulacion = Boolean.TRUE;
                    prepararEliminarMovimiento(mov);
                    PrimeFaces.current().executeScript("PF('confirmDialogAnul').show()");
                }

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los datos de un Ingreso para su edicción.
     */
    public Boolean validarIngreso(SgMovimientos mov) {
        if (mov.getMovPk() != null) {
            if (mov.getMovTipo().equals(EnumMovimientosTipo.I)) {
                try {
                    FiltroMovimientos valmov = new FiltroMovimientos();
                    valmov.setOrderBy(new String[]{"movPk"});
                    valmov.setIncluirCampos(new String[]{
                        "movPk",
                        "movFuenteRecursos",
                        "movTipo",
                        "movMonto",
                        "movVersion",
                        "movFuenteIngresos.movPk",
                        "movFuenteIngresos.movVersion",
                        "movFuenteIngresos.movNumMoviento"
                    });
                    valmov.setMovFuenteIngresos(mov.getMovPk());
                    valmov.setAscending(new boolean[]{true});
                    valmov.setMovPresupuestoFk(entidadEnEdicion.getPesPk());
                    List<SgMovimientos> resultadoValMov = movimientosClient.buscar(valmov);

                    if (!resultadoValMov.isEmpty()) {
                        titleEdicion = "Editar";
                        return true;
                    } else {
                        titleEdicion = "Anular";
                        return false;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            } else {
                titleEdicion = "Editar";
                return true;
            }
        } else {
            titleEdicion = "Anular";
            return false;
        }
        mov = null;

        return false;

    }

    /**
     * Inicializa el objeto para su eliminación.
     */
    public void prepararEliminarMovimiento(SgMovimientos mov) {
        try {
            listaMovEditado = null;
            listaAnulEditado = null;
            listaAnulOriginal = null;
            movimientoAEliminar = null;
            movimientoAEliminar = mov;
            if (movimientoAEliminar != null) {
                if (mov.getMovTipo().equals(EnumMovimientosTipo.I) && anulacion) {
                    textoConfirmacionAnulacion = "¿Realmente desea Anular este registro?";
                }
                if (mov.getMovTipo().equals(EnumMovimientosTipo.IM)) {
                    FiltroMovimientosEdicion mov2 = new FiltroMovimientosEdicion();
                    mov2.setOrderBy(new String[]{"movPk"});
                    mov2.setIncluirCampos(new String[]{
                        "movPk",
                        "movFuenteRecursos",
                        "movTipo",
                        "movMonto",
                        "movVersion",
                        "movFuenteIngresos.movPk",
                        "movFuenteIngresos.movVersion",
                        "movFuenteIngresos.movNumMoviento"
                    });
                    mov2.setMovFuenteIngresos(movimientoAEliminar.getMovPk());
                    mov2.setAscending(new boolean[]{true});
                    mov2.setMovPresupuestoFk(entidadEnEdicion.getPesPk());
                    listaAnulOriginal = edicionMovimientosClient.buscar(mov2);

                    if (!listaAnulOriginal.isEmpty()) {
                        textoConfirmacionEliminacion = "El registro posee egresos relacionados ¿Realmente desea continuar?";
                    } else {
                        textoConfirmacionEliminacion = "¿Realmente desea eliminar este registro?";
                    }

                }
                if (mov.getMovTipo().equals(EnumMovimientosTipo.E) || mov.getMovTipo().equals(EnumMovimientosTipo.EM)) {
                    textoConfirmacionEliminacion = "¿Realmente desea eliminar este registro?";
                }

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Elimina el movimiento del presupuesto.
     */
    public void eliminarMovimiento() {
        try {
            if (movimientoAEliminar != null) {
                if (movimientoAEliminar.getMovTipo().equals(EnumMovimientosTipo.E)) {
                    movimientosClient.eliminar(movimientoAEliminar.getMovPk());
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                }
                if (movimientoAEliminar.getMovTipo().equals(EnumMovimientosTipo.EM)) {
                    edicionMovimientosClient.eliminar(movimientoAEliminar.getMovPk());
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                }
                if (movimientoAEliminar.getMovTipo().equals(EnumMovimientosTipo.I)) {
                    movimientosClient.eliminar(movimientoAEliminar.getMovPk());
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ANULADO_ELIMINADO_CORRECTO), "");
                }
                if (movimientoAEliminar.getMovTipo().equals(EnumMovimientosTipo.IM)) {
                    edicionMovimientosClient.eliminarIngreso(movimientoAEliminar.getMovPk());
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                }
            }
            movimientoAEliminar = null;
            listaMovEditado = null;
            actualizar(entidadEnEdicion);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Elimina el movimiento del presupuesto.
     */
    public void eliminarMovimientoPorAnulacion() {
        try {
            if (movimientoAEliminar != null && anulacion) {
                if (!listaAnulEditado.isEmpty()) {
                    for (SgMovimientos egresosEditados : listaAnulEditado) {
                        movimientosClient.eliminar(egresosEditados.getMovPk());
                    }

                }
                if (!listaAnulOriginal.isEmpty()) {
                    for (SgMovimientos egresosOriginal : listaAnulOriginal) {
                        edicionMovimientosClient.eliminar(egresosOriginal.getMovPk());
                    }

                }
                movimientosClient.eliminar(movimientoAEliminar.getMovPk());
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            } else {
                edicionMovimientosClient.eliminarAnular(movimientoAEliminar.getMovPk());
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            }
            movimientoAEliminar = null;
            listaAnulEditado = null;
            listaAnulOriginal = null;
            actualizar(entidadEnEdicion);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Guarda o Actualiza el movimiento en edción.
     */
    public void guardarMovimiento() {
        try {
            if (movimientoEnEdicion != null) {
                if (movimientoEnEdicion.isIngresoEdicion() || movimientoEnEdicion.isIngreso()) {
                    guardarIngreso();
                } else if (movimientoEnEdicion.isEgresoEdicion() || movimientoEnEdicion.isEgreso()) {
                    guardarEgreso();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Guarda o Actualiza el Ingreso.
     */
    public void guardarIngreso() {
        try {
            movimientoEnEdicion.setMovPresupuestoFk(entidadEnEdicion);
            movimientoEnEdicion.setMovRubroPk(rubrosCombo.getSelectedT());
            if (movimientoEnEdicion.isIngresoEdicion()) {
                movimientoEnEdicion = edicionMovimientosClient.guardar(movimientoEnEdicion);
            } else if (movimientoEnEdicion.isIngreso()) {
                movimientoEnEdicion = movimientosClient.guardarEditado(movimientoEnEdicion);
            }
            actualizar(restClient.obtenerPorId(entidadEnEdicion.getPesPk()));
            PrimeFaces.current().executeScript("PF('itemDialogEgresos').hide()");
            edicionMovimientoEnEdicion = null;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Guarda o Actualiza el movimiento en edición.
     */
    public void guardarEgreso() {
        try {
            movimientoEnEdicion.setMovPresupuestoFk(entidadEnEdicion);
            movimientoEnEdicion.setMovActividadPk(detPlanEscolarCombo.getSelectedT());
            if (movimientoEnEdicion.getMovTipo().equals(EnumMovimientosTipo.EM)) {
                movimientoEnEdicion.setMovMonto(movimientoEnEdicion.getMovMontoAprobado());
            }
            if (fuenteIngresoCombo.getSelectedT() != null && fuenteIngresoCombo.getSelectedT().getMovTipo().equals(EnumMovimientosTipo.I)) {
                movimientoEnEdicion.setMovFuenteIngresosOriginal(fuenteIngresoCombo.getSelectedT());
            } else if (fuenteIngresoCombo.getSelectedT() != null && fuenteIngresoCombo.getSelectedT().getMovTipo().equals(EnumMovimientosTipo.IM)) {
                movimientoEnEdicion.setMovFuenteIngresos(fuenteIngresoCombo.getSelectedT());
            }
            if (transferencia == true) {
                movimientoEnEdicion.setMovAreaInversionPk(null);
            } else {
                movimientoEnEdicion.setMovAreaInversionPk(areaInversionSeleccionada);
            }
            movimientoEnEdicion.setMovSubAreaInversionPk(subAreaInversionSeleccionada);

            if (movimientoEnEdicion.isEgresoEdicion()) {
                movimientoEnEdicion = edicionMovimientosClient.guardar(movimientoEnEdicion);
            } else if (movimientoEnEdicion.isEgreso()) {
                movimientoEnEdicion = movimientosClient.guardarEditado(movimientoEnEdicion);
            }

            actualizar(restClient.obtenerPorId(entidadEnEdicion.getPesPk()));
            PrimeFaces.current().executeScript("PF('itemDialogEgresos').hide()");
            movimientoEnEdicion = null;
            areaInversionSeleccionada = null;
            subAreaInversionSeleccionada = null;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * String que controla el título de la página.
     */
    public String getTituloPagina() {
        if (this.presupuestoEscolarId == null) {
            return Etiquetas.getValue("nuevoPresupuestoEscolar");
        } else if (this.soloLectura) {
            return Etiquetas.getValue("verPresupuestoEscolar");
        } else {
            return Etiquetas.getValue("modificacionPresupuestoEscolar");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id
     */
    public void historial(Long id) {
        try {
            historialPresupuestoEscolar = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Limpia el filtro en edición.
     */
    public void limpiar() {
        filtro = new FiltroPresupuestoEscolar();
        buscar();
    }

    /**
     * Elimina el objeto en edición.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getPesPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Valadación de montos que se realiza previa al cambio de estado.
     */
    public void confirmarFormulacion() {
        try {
            ingresosList.forEach(mov -> {
                try {
                    if (mov.getSaldoEgresos().signum() < 0) {
                        conteoSaldoOtrosIngresos = conteoSaldoOtrosIngresos + 1;
                        saldoNegativoOtrosIngresos = Boolean.TRUE;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            });
            transferenciaList.forEach(mov -> {
                try {
                    if (mov.getSaldoEgresos().compareTo(BigDecimal.ZERO) != 0) {
                        conteoSaldoTransferecias = conteoSaldoTransferecias + 1;
                        saldoCeroTransferecias = Boolean.TRUE;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            });
            if (!saldoNegativoOtrosIngresos) {
                if (!saldoCeroTransferecias) {
                    entidadEnEdicion.setPesEstado(EnumPresupuestoEscolarEstado.FORMULADO);
                    entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_VERIFICAR_SALDOS_TRANSFERENCIAS), "");
                    saldoNegativoOtrosIngresos = Boolean.FALSE;
                    saldoCeroTransferecias = Boolean.FALSE;
                }

            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_VERIFICAR_SALDOS), "");
                saldoNegativoOtrosIngresos = Boolean.FALSE;
                saldoCeroTransferecias = Boolean.FALSE;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Valadación de montos que se realiza previa al cambio de estado.
     */
    public void confirmarEdicion() {
        try {

            ingresosList.forEach(mov -> {
                try {
                    if (mov.getSaldoEgresos().signum() < 0) {
                        conteoSaldoOtrosIngresos = conteoSaldoOtrosIngresos + 1;
                        saldoNegativoOtrosIngresos = Boolean.TRUE;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            });

            transferenciaList.forEach(mov -> {
                try {
                    if (mov.getSaldoEgresos().compareTo(BigDecimal.ZERO) != 0) {
                        conteoSaldoTransferecias = conteoSaldoTransferecias + 1;
                        saldoCeroTransferecias = Boolean.TRUE;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            });

            if (!saldoNegativoOtrosIngresos) {
                if (!saldoCeroTransferecias) {
                    entidadEnEdicion.setPesEdicion(Boolean.FALSE);
                    entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                    inicializarBotones();
                    inicializarColumnaAcciones();
                    procesarNuevosMovimientos();
                    eliminarNuevosMovimientos();
                    eliminarUsuariosAutorizados();

                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_VERIFICAR_SALDOS_TRANSFERENCIAS), "");
                    saldoNegativoOtrosIngresos = Boolean.FALSE;
                    saldoCeroTransferecias = Boolean.FALSE;
                }

            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_VERIFICAR_SALDOS), "");
                saldoNegativoOtrosIngresos = Boolean.FALSE;
                saldoCeroTransferecias = Boolean.FALSE;
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Elimina usuarios Validados para Edicio de Presupuesto.
     */
    public void eliminarUsuariosAutorizados() {
        try {

            FiltroAutorizacionEdicionPresupuesto user = new FiltroAutorizacionEdicionPresupuesto();
            user.setAutPresupuestoFk(entidadEnEdicion.getPesPk());
            user.setOrderBy(new String[]{"autPk"});
            user.setIncluirCampos(new String[]{
                "autPk",
                "autPresupuestoFk.pesPk",
                "autPresupuestoFk.pesVersion",
                "autUsuarioValidadoFk.usuPk",
                "autUsuarioValidadoFk.usuVersion",
                "autVersion"
            });
            user.setAscending(new boolean[]{false});

            List<SgAutorizacionEdicionPresupuesto> listaUsuarios = autorizacionesClient.buscar(user);

            if (!listaUsuarios.isEmpty()) {
                autorizacionesClient.eliminarUsuarios(entidadEnEdicion.getPesPk());
            }
            JSFUtils.redirectToPage(ConstantesPaginas.PRESUPUESTOS_ESCOLARES);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Guarda el objeto.
     */
    public void guardar() {
        try {
            entidadEnEdicion.setPesEstado(EnumPresupuestoEscolarEstado.EN_PROCESO);
            entidadEnEdicion.setPesSedeFk(sedeSeleccionada);
            entidadEnEdicion.setPesAnioFiscalFk(anioFiscalCombo.getSelectedT());
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Traslada los nuevos moviemientos al presupuesto consolidado.
     */
    public void procesarNuevosMovimientos() {
        try {
            edicionIngresosList.forEach(ing -> {
                if (ing.getEditados() != null) {
                    SgMovimientos ingresoAnt = new SgMovimientos();
                    ingresoAnt.setMovTipo(EnumMovimientosTipo.I);
                    ingresoAnt.setMovRubroPk(ing.getMovRubroPk());
                    ingresoAnt.setMovMonto(ing.getMovMonto());
                    ingresoAnt.setMovNumMoviento(ing.getMovNumMoviento());
                    ingresoAnt.setMovOrigen(ing.getMovOrigen());
                    ingresoAnt.setMovFuenteRecursos(ing.getMovFuenteRecursos());
                    ingresoAnt.setMovPresupuestoFk(entidadEnEdicion);
                    ingresoAnt.setMovPk(ing.getEditados().getMovOriginalEditado());
                    ingresosEditados.add(ingresoAnt);

                } else {
                    SgMovimientos ingreso = new SgMovimientos();
                    ingreso.setMovTipo(EnumMovimientosTipo.I);
                    ingreso.setMovRubroPk(ing.getMovRubroPk());
                    ingreso.setMovMonto(ing.getMovMonto());
                    ingreso.setMovNumMoviento(ing.getMovNumMoviento());
                    ingreso.setMovOrigen(ing.getMovOrigen());
                    ingreso.setMovFuenteRecursos(ing.getMovFuenteRecursos());
                    ingreso.setMovPresupuestoFk(entidadEnEdicion);
                    nuevosIngresos.add(ingreso);
                }

            });

            if (!nuevosIngresos.isEmpty()) {
                for (SgMovimientos ingresos : nuevosIngresos) {
                    movimientosClient.guardar(ingresos);
                }

            }
            if (!ingresosEditados.isEmpty()) {
                for (SgMovimientos ingresosEdit : ingresosEditados) {
                    movimientosClient.guardar(ingresosEdit);
                }

            }

            ///AGREGANDO EGRESOS
            edicionEgresosList.forEach(egr -> {
                if (egr.getEditados() != null) {

                    SgMovimientos egresosEdit = new SgMovimientos();
                    egresosEdit.setMovTipo(EnumMovimientosTipo.E);
                    egresosEdit.setMovPresupuestoFk(entidadEnEdicion);
                    egresosEdit.setMovFuenteRecursos(egr.getMovFuenteRecursos());
                    egresosEdit.setMovMonto(egr.getMovMonto());
                    egresosEdit.setMovActividadPk(egr.getMovActividadPk());
                    egresosEdit.setMovMontoAprobado(egr.getMovMontoAprobado());
                    egresosEdit.setMovAreaInversionPk(egr.getMovAreaInversionPk());
                    egresosEdit.setMovSubAreaInversionPk(egr.getMovSubAreaInversionPk());
                    egresosEdit.setMovPk(egr.getEditados().getMovOriginalEditado());

                    if (egr.getMovFuenteIngresosOriginal() != null) {
                        egresosEdit.setMovFuenteIngresos(egr.getMovFuenteIngresosOriginal());
                    } else if (egr.getMovFuenteIngresos() != null) {
                        try {
                            FiltroMovimientos filmove = new FiltroMovimientos();
                            filmove.setOrderBy(new String[]{"movPk"});
                            filmove.setMovFuenteRecursos(egr.getMovFuenteIngresos().getMovFuenteRecursos());
                            filmove.setAscending(new boolean[]{true});
                            filmove.setMovPresupuestoFk(entidadEnEdicion.getPesPk());
                            List<SgMovimientos> listaMove = movimientosClient.buscar(filmove);

                            if (!listaMove.isEmpty()) {
                                egresosEdit.setMovFuenteIngresos(listaMove.get(0));
                            }

                        } catch (Exception ex) {
                            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                        }

                    }

                    egresosEditados.add(egresosEdit);
                } else {
                    SgMovimientos egresos = new SgMovimientos();
                    egresos.setMovTipo(EnumMovimientosTipo.E);
                    egresos.setMovPresupuestoFk(entidadEnEdicion);
                    egresos.setMovFuenteRecursos(egr.getMovFuenteRecursos());
                    egresos.setMovMonto(egr.getMovMonto());
                    egresos.setMovActividadPk(egr.getMovActividadPk());
                    egresos.setMovMontoAprobado(egr.getMovMontoAprobado());
                    egresos.setMovAreaInversionPk(egr.getMovAreaInversionPk());
                    egresos.setMovSubAreaInversionPk(egr.getMovSubAreaInversionPk());

                    if (egr.getMovFuenteIngresosOriginal() != null) {
                        egresos.setMovFuenteIngresos(egr.getMovFuenteIngresosOriginal());
                    } else if (egr.getMovFuenteIngresos() != null) {
                        try {
                            FiltroMovimientos filmov = new FiltroMovimientos();
                            filmov.setOrderBy(new String[]{"movPk"});
                            filmov.setMovFuenteRecursos(egr.getMovFuenteIngresos().getMovFuenteRecursos());
                            filmov.setAscending(new boolean[]{true});
                            filmov.setMovPresupuestoFk(entidadEnEdicion.getPesPk());
                            List<SgMovimientos> listaMov = movimientosClient.buscar(filmov);

                            if (!listaMov.isEmpty()) {
                                egresos.setMovFuenteIngresos(listaMov.get(0));
                            }

                        } catch (Exception ex) {
                            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                        }

                    }

                    nuevosEgresos.add(egresos);
                }

            });

            if (!nuevosEgresos.isEmpty()) {
                for (SgMovimientos egresos : nuevosEgresos) {
                    movimientosClient.guardar(egresos);
                }

            }

            if (!egresosEditados.isEmpty()) {
                for (SgMovimientos egresoEdit : egresosEditados) {
                    movimientosClient.guardar(egresoEdit);
                }

            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    /**
     * Elimina los movimientos del presupuesto Editado.
     */
    public void eliminarNuevosMovimientos() {
        try {

            FiltroMovimientosEdicion deleteMov = new FiltroMovimientosEdicion();
            deleteMov.setOrderBy(new String[]{"movPk"});
            deleteMov.setAscending(new boolean[]{true});
            deleteMov.setIncluirCampos(new String[]{
                "movPk",
                "movTipo",
                "movVersion",
                "movPresupuestoFk.pesPk",
                "movPresupuestoFk.pesVersion"
            });
            deleteMov.setMovPresupuestoFk(entidadEnEdicion.getPesPk());
            List<SgMovimientos> listaMovEliminar = edicionMovimientosClient.buscar(deleteMov);
            if (!listaMovEliminar.isEmpty()) {
                edicionMovimientosClient.eliminarEditados(entidadEnEdicion.getPesPk());
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Guarda el objeto con un estado Arpobado previa validación.
     */
    public void guardarCambioEstado() {
        try {
            transferenciaList.forEach(mov -> {
                try {
                    if (mov.getSaldoEgresosAprobado().signum() < 0) {
                        conteoSaldoTransfereciasSaldoEgresosAprobado = conteoSaldoTransfereciasSaldoEgresosAprobado + 1;
                        saldoNegativoSaldoEgresosAprobado = Boolean.TRUE;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            });
            if (!saldoNegativoSaldoEgresosAprobado) {
                estadoActual = entidadEnEdicion.getPesEstado();
                entidadEnEdicion.setPesEstado(EnumPresupuestoEscolarEstado.APROBADO);
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            } else {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_VERIFICAR_SALDOS_EGRESOS_APROBADOS), "");
                saldoNegativoSaldoEgresosAprobado = Boolean.FALSE;
            }

        } catch (BusinessException be) {
            entidadEnEdicion.setPesEstado(estadoActual);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            entidadEnEdicion.setPesEstado(estadoActual);
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Guarda el objeto flujos en movimientos.
     */
    public void guardarFlujos() {
        JSFUtils.limpiarMensajesError();
        ingresosList.forEach(mov -> {
            try {
                if (mov.getMovTotalAnual().compareTo(mov.getMovMonto()) == -1) {
                    movimientoEnEdicion = movimientosClient.guardar(mov);
                    actualizarTotalAnualTransferencias();
                    actualizarTotalAnual();
                    actualizarTotalAnualIngresos();
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.TOTAL_MENOR_FLUJO), "");
                }
            } catch (BusinessException be) {
                JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
            }
        });
    }

    /**
     * Método para el componente autocomplete de Sede
     *
     * @param query
     * @return
     */
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    /**
     * Obtiene el id del PEA al que esta relacionado el presupuesto
     *
     */
    private void PlanEscolarAnual() {
        try {

            FiltroDetallePlanEscolar filtro = new FiltroDetallePlanEscolar();
            filtro.setSedePk(entidadEnEdicion.getPesSedeFk().getSedPk());
            List<SgDetallePlanEscolar> resultado = restDetalle.buscar(filtro);
            for (SgDetallePlanEscolar det : resultado) {
                det.setDpeActividad(Jsoup.parse(det.getDpeActividad()).text());
            }
            List<SgDetallePlanEscolar> nuevoResultado = resultado
                    .stream()
                    .filter(x -> x.getDpePlanEscolarAnual().getPeaAnioLectivo().getAleAnio().equals(entidadEnEdicion.getPesAnioFiscalFk().getAniAnio()))
                    .collect(Collectors.toList());
            pea = nuevoResultado.get(0);
            peaId = pea.getDpePlanEscolarAnual().getPeaPk();
        } catch (BusinessException ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para Actualizar el total.
     */
    public void actualizarTotalAnual() {
        totalFEnero = egresosList
                .stream()
                .filter(x -> x.getMovEnero() != null)
                .map(SgMovimientos::getMovEnero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFFebrero = egresosList
                .stream()
                .filter(x -> x.getMovFebrero() != null)
                .map(SgMovimientos::getMovFebrero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFMarzo = egresosList
                .stream()
                .filter(x -> x.getMovMarzo() != null)
                .map(SgMovimientos::getMovMarzo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFAbril = egresosList
                .stream()
                .filter(x -> x.getMovAbril() != null)
                .map(SgMovimientos::getMovAbril)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFMayo = egresosList
                .stream()
                .filter(x -> x.getMovMayo() != null)
                .map(SgMovimientos::getMovMayo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFJunio = egresosList
                .stream()
                .filter(x -> x.getMovJunio() != null)
                .map(SgMovimientos::getMovJunio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFJulio = egresosList
                .stream()
                .filter(x -> x.getMovJulio() != null)
                .map(SgMovimientos::getMovJulio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFAgosto = egresosList
                .stream()
                .filter(x -> x.getMovAgosto() != null)
                .map(SgMovimientos::getMovAgosto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFSeptiembre = egresosList
                .stream()
                .filter(x -> x.getMovSeptiembre() != null)
                .map(SgMovimientos::getMovSeptiembre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFOctubre = egresosList
                .stream()
                .filter(x -> x.getMovOctubre() != null)
                .map(SgMovimientos::getMovOctubre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFNoviembre = egresosList
                .stream()
                .filter(x -> x.getMovNoviembre() != null)
                .map(SgMovimientos::getMovNoviembre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFDiciembre = egresosList
                .stream()
                .filter(x -> x.getMovDiciembre() != null)
                .map(SgMovimientos::getMovDiciembre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

//        totalCargaAnual();
    }

    /**
     * Método para Actualizar el total anual.
     */
    public void totalAnual(SgMovimientos mov) {

        egresosList.stream().filter(e -> e.equals(mov))
                .findAny().orElse(null).sumarMeses();
    }

    /**
     * Método para Actualizar el total anual de ingresos.
     */
    public void actualizarTotalAnualIngresos() {
        totalFEneroI = ingresosList
                .stream()
                .filter(x -> x.getMovEnero() != null)
                .map(SgMovimientos::getMovEnero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFFebreroI = ingresosList
                .stream()
                .filter(x -> x.getMovFebrero() != null)
                .map(SgMovimientos::getMovFebrero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFMarzoI = ingresosList
                .stream()
                .filter(x -> x.getMovMarzo() != null)
                .map(SgMovimientos::getMovMarzo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFAbrilI = ingresosList
                .stream()
                .filter(x -> x.getMovAbril() != null)
                .map(SgMovimientos::getMovAbril)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFMayoI = ingresosList
                .stream()
                .filter(x -> x.getMovMayo() != null)
                .map(SgMovimientos::getMovMayo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFJunioI = ingresosList
                .stream()
                .filter(x -> x.getMovJunio() != null)
                .map(SgMovimientos::getMovJunio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFJulioI = ingresosList
                .stream()
                .filter(x -> x.getMovJulio() != null)
                .map(SgMovimientos::getMovJulio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFAgostoI = ingresosList
                .stream()
                .filter(x -> x.getMovAgosto() != null)
                .map(SgMovimientos::getMovAgosto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFSeptiembreI = ingresosList
                .stream()
                .filter(x -> x.getMovSeptiembre() != null)
                .map(SgMovimientos::getMovSeptiembre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFOctubreI = ingresosList
                .stream()
                .filter(x -> x.getMovOctubre() != null)
                .map(SgMovimientos::getMovOctubre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFNoviembreI = ingresosList
                .stream()
                .filter(x -> x.getMovNoviembre() != null)
                .map(SgMovimientos::getMovNoviembre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFDiciembreI = ingresosList
                .stream()
                .filter(x -> x.getMovDiciembre() != null)
                .map(SgMovimientos::getMovDiciembre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    /**
     * Método para Actualizar el total anual de transferencias.
     */
    public void actualizarTotalAnualTransferencias() {
        totalFEneroT = transferenciaList
                .stream()
                .filter(x -> x.getMovEnero() != null)
                .map(SgMovimientos::getMovEnero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFFebreroT = transferenciaList
                .stream()
                .filter(x -> x.getMovFebrero() != null)
                .map(SgMovimientos::getMovFebrero)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFMarzoT = transferenciaList
                .stream()
                .filter(x -> x.getMovMarzo() != null)
                .map(SgMovimientos::getMovMarzo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFAbrilT = transferenciaList
                .stream()
                .filter(x -> x.getMovAbril() != null)
                .map(SgMovimientos::getMovAbril)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalFMayoT = transferenciaList
                .stream()
                .filter(x -> x.getMovMayo() != null)
                .map(SgMovimientos::getMovMayo)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFJunioT = transferenciaList
                .stream()
                .filter(x -> x.getMovJunio() != null)
                .map(SgMovimientos::getMovJunio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFJulioT = transferenciaList
                .stream()
                .filter(x -> x.getMovJulio() != null)
                .map(SgMovimientos::getMovJulio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFAgostoT = transferenciaList
                .stream()
                .filter(x -> x.getMovAgosto() != null)
                .map(SgMovimientos::getMovAgosto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFSeptiembreT = transferenciaList
                .stream()
                .filter(x -> x.getMovSeptiembre() != null)
                .map(SgMovimientos::getMovSeptiembre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFOctubreT = transferenciaList
                .stream()
                .filter(x -> x.getMovOctubre() != null)
                .map(SgMovimientos::getMovOctubre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFNoviembreT = transferenciaList
                .stream()
                .filter(x -> x.getMovNoviembre() != null)
                .map(SgMovimientos::getMovNoviembre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalFDiciembreT = transferenciaList
                .stream()
                .filter(x -> x.getMovDiciembre() != null)
                .map(SgMovimientos::getMovDiciembre)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    /**
     * Método para Actualizar la carga anual.
     */
    public void totalCargaAnual() {
        egresosList.stream()
                .findAny().orElse(null).sumarMeses();
    }

    /**
     * Evento change tab.
     */
    public void changeTab(TabChangeEvent event) {

    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public void setEntidadEnEdicion(SgPresupuestoEscolar entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgPresupuestoEscolar getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public FiltroPresupuestoEscolar getFiltro() {
        return filtro;
    }

    public FiltroMovimientos getFiltromove() {
        return filtromove;
    }

    public void setFiltromove(FiltroMovimientos filtromove) {
        this.filtromove = filtromove;
    }

    public void setFiltro(FiltroPresupuestoEscolar filtro) {
        this.filtro = filtro;
    }

    public FiltroAreaInversion getAdit() {
        return adit;
    }

    public void setAdit(FiltroAreaInversion adit) {
        this.adit = adit;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public LazyPresupuestosEscolaresDataModel getReglasPresupuestoEscolarDataModel() {
        return reglasPresupuestoEscolarDataModel;
    }

    public void setReglasPresupuestoEscolarDataModel(LazyPresupuestosEscolaresDataModel reglasPresupuestoEscolarDataModel) {
        this.reglasPresupuestoEscolarDataModel = reglasPresupuestoEscolarDataModel;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public List<RevHistorico> getHistorialPresupuestoEscolar() {
        return historialPresupuestoEscolar;
    }

    public void setHistorialPresupuestoEscolar(List<RevHistorico> historialPresupuestoEscolar) {
        this.historialPresupuestoEscolar = historialPresupuestoEscolar;
    }

    public SofisComboG<SgAnioLectivo> getAnioLectivoCombo() {
        return anioLectivoCombo;
    }

    public void setAnioLectivoCombo(SofisComboG<SgAnioLectivo> anioLectivoCombo) {
        this.anioLectivoCombo = anioLectivoCombo;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public Integer getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(Integer selectedTab) {
        this.selectedTab = selectedTab;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public List<SgMovimientos> getIngresosList() {
        return ingresosList;
    }

    public void setIngresosList(List<SgMovimientos> ingresosList) {
        this.ingresosList = ingresosList;
    }

    public List<SgMovimientos> getEgresosList() {
        return egresosList;
    }

    public void setEgresosList(List<SgMovimientos> egresosList) {
        this.egresosList = egresosList;
    }

    public SgMovimientos getMovimientoEnEdicion() {
        return movimientoEnEdicion;
    }

    public void setMovimientoEnEdicion(SgMovimientos movimientoEnEdicion) {
        this.movimientoEnEdicion = movimientoEnEdicion;
    }

    public BigDecimal getTotali() {
        return totali;
    }

    public void setTotali(BigDecimal totali) {
        this.totali = totali;
    }

    public BigDecimal getEgresosGlobales() {
        return egresosGlobales;
    }

    public void setEgresosGlobales(BigDecimal egresosGlobales) {
        this.egresosGlobales = egresosGlobales;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public List<SgMovimientos> getTransferenciaList() {
        return transferenciaList;
    }

    public void setTransferenciaList(List<SgMovimientos> transferenciaList) {
        this.transferenciaList = transferenciaList;
    }

    public BigDecimal getTotalt() {
        return totalt;
    }

    public void setTotalt(BigDecimal totalt) {
        this.totalt = totalt;
    }

    public BigDecimal getTotal_it() {
        return total_it;
    }

    public void setTotal_it(BigDecimal total_it) {
        this.total_it = total_it;
    }

    public SofisComboG<SgDetallePlanEscolar> getDetPlanEscolarCombo() {
        return detPlanEscolarCombo;
    }

    public void setDetPlanEscolarCombo(SofisComboG<SgDetallePlanEscolar> detPlanEscolarCombo) {
        this.detPlanEscolarCombo = detPlanEscolarCombo;
    }

    public SofisComboG<SgMovimientos> getFuenteIngresoCombo() {
        return fuenteIngresoCombo;
    }

    public void setFuenteIngresoCombo(SofisComboG<SgMovimientos> fuenteIngresoCombo) {
        this.fuenteIngresoCombo = fuenteIngresoCombo;
    }

    public String getLeyendaTitulo() {
        return leyendaTitulo;
    }

    public void setLeyendaTitulo(String leyendaTitulo) {
        this.leyendaTitulo = leyendaTitulo;
    }

    public SofisComboG<SgRubros> getRubrosCombo() {
        return rubrosCombo;
    }

    public void setRubrosCombo(SofisComboG<SgRubros> rubrosCombo) {
        this.rubrosCombo = rubrosCombo;
    }

    public BigDecimal getTotalIAprobado() {
        return totalIAprobado;
    }

    public void setTotalIAprobado(BigDecimal totalIAprobado) {
        this.totalIAprobado = totalIAprobado;
    }

    public BigDecimal getTotalTAprobado() {
        return totalTAprobado;
    }

    public void setTotalTAprobado(BigDecimal totalTAprobado) {
        this.totalTAprobado = totalTAprobado;
    }

    public BigDecimal getTotalEAprobado() {
        return totalEAprobado;
    }

    public void setTotalEAprobado(BigDecimal totalEAprobado) {
        this.totalEAprobado = totalEAprobado;
    }

    public SofisComboG<SgAreaInversion> getAreaInversionCombo() {
        return areaInversionCombo;
    }

    public void setAreaInversionCombo(SofisComboG<SgAreaInversion> areaInversionCombo) {
        this.areaInversionCombo = areaInversionCombo;
    }

    public SofisComboG<SgAreaInversion> getSubareaInversionCombo() {
        return subareaInversionCombo;
    }

    public void setSubareaInversionCombo(SofisComboG<SgAreaInversion> subareaInversionCombo) {
        this.subareaInversionCombo = subareaInversionCombo;
    }

    public SgAreaInversion getPadre() {
        return padre;
    }

    public void setPadre(SgAreaInversion padre) {
        this.padre = padre;
    }

    public SgAreaInversion getAreaInversionEnEdicion() {
        return areaInversionEnEdicion;
    }

    public BigDecimal getTotalFEnero() {
        return totalFEnero;
    }

    public void setTotalFEnero(BigDecimal totalFEnero) {
        this.totalFEnero = totalFEnero;
    }

    public BigDecimal getTotalActividad() {
        return totalActividad;
    }

    public void setTotalActividad(BigDecimal totalActividad) {
        this.totalActividad = totalActividad;
    }

    public List<SgMovimientos> getEneroList() {
        return eneroList;
    }

    public void setEneroList(List<SgMovimientos> eneroList) {
        this.eneroList = eneroList;
    }

    public BigDecimal getTotalFFebrero() {
        return totalFFebrero;
    }

    public void setTotalFFebrero(BigDecimal totalFFebrero) {
        this.totalFFebrero = totalFFebrero;
    }

    public BigDecimal getTotalFMarzo() {
        return totalFMarzo;
    }

    public void setTotalFMarzo(BigDecimal totalFMarzo) {
        this.totalFMarzo = totalFMarzo;
    }

    public BigDecimal getTotalFAbril() {
        return totalFAbril;
    }

    public void setTotalFAbril(BigDecimal totalFAbril) {
        this.totalFAbril = totalFAbril;
    }

    public BigDecimal getTotalFMayo() {
        return totalFMayo;
    }

    public void setTotalFMayo(BigDecimal totalFMayo) {
        this.totalFMayo = totalFMayo;
    }

    public BigDecimal getTotalFJunio() {
        return totalFJunio;
    }

    public void setTotalFJunio(BigDecimal totalFJunio) {
        this.totalFJunio = totalFJunio;
    }

    public BigDecimal getTotalFJulio() {
        return totalFJulio;
    }

    public void setTotalFJulio(BigDecimal totalFJulio) {
        this.totalFJulio = totalFJulio;
    }

    public BigDecimal getTotalFAgosto() {
        return totalFAgosto;
    }

    public void setTotalFAgosto(BigDecimal totalFAgosto) {
        this.totalFAgosto = totalFAgosto;
    }

    public BigDecimal getTotalFSeptiembre() {
        return totalFSeptiembre;
    }

    public void setTotalFSeptiembre(BigDecimal totalFSeptiembre) {
        this.totalFSeptiembre = totalFSeptiembre;
    }

    public BigDecimal getTotalFOctubre() {
        return totalFOctubre;
    }

    public void setTotalFOctubre(BigDecimal totalFOctubre) {
        this.totalFOctubre = totalFOctubre;
    }

    public BigDecimal getTotalFNoviembre() {
        return totalFNoviembre;
    }

    public void setTotalFNoviembre(BigDecimal totalFNoviembre) {
        this.totalFNoviembre = totalFNoviembre;
    }

    public BigDecimal getTotalFDiciembre() {
        return totalFDiciembre;
    }

    public void setTotalFDiciembre(BigDecimal totalFDiciembre) {
        this.totalFDiciembre = totalFDiciembre;
    }

    public BigDecimal getTotalAnual() {
        return totalAnual;
    }

    public void setTotalAnual(BigDecimal totalAnual) {
        this.totalAnual = totalAnual;
    }

    public BigDecimal getTotalFEneroI() {
        return totalFEneroI;
    }

    public void setTotalFEneroI(BigDecimal totalFEneroI) {
        this.totalFEneroI = totalFEneroI;
    }

    public BigDecimal getTotalFFebreroI() {
        return totalFFebreroI;
    }

    public void setTotalFFebreroI(BigDecimal totalFFebreroI) {
        this.totalFFebreroI = totalFFebreroI;
    }

    public BigDecimal getTotalFMarzoI() {
        return totalFMarzoI;
    }

    public void setTotalFMarzoI(BigDecimal totalFMarzoI) {
        this.totalFMarzoI = totalFMarzoI;
    }

    public BigDecimal getTotalFAbrilI() {
        return totalFAbrilI;
    }

    public void setTotalFAbrilI(BigDecimal totalFAbrilI) {
        this.totalFAbrilI = totalFAbrilI;
    }

    public BigDecimal getTotalFMayoI() {
        return totalFMayoI;
    }

    public void setTotalFMayoI(BigDecimal totalFMayoI) {
        this.totalFMayoI = totalFMayoI;
    }

    public BigDecimal getTotalFJunioI() {
        return totalFJunioI;
    }

    public void setTotalFJunioI(BigDecimal totalFJunioI) {
        this.totalFJunioI = totalFJunioI;
    }

    public BigDecimal getTotalFJulioI() {
        return totalFJulioI;
    }

    public void setTotalFJulioI(BigDecimal totalFJulioI) {
        this.totalFJulioI = totalFJulioI;
    }

    public BigDecimal getTotalFAgostoI() {
        return totalFAgostoI;
    }

    public void setTotalFAgostoI(BigDecimal totalFAgostoI) {
        this.totalFAgostoI = totalFAgostoI;
    }

    public BigDecimal getTotalFSeptiembreI() {
        return totalFSeptiembreI;
    }

    public void setTotalFSeptiembreI(BigDecimal totalFSeptiembreI) {
        this.totalFSeptiembreI = totalFSeptiembreI;
    }

    public BigDecimal getTotalFOctubreI() {
        return totalFOctubreI;
    }

    public void setTotalFOctubreI(BigDecimal totalFOctubreI) {
        this.totalFOctubreI = totalFOctubreI;
    }

    public BigDecimal getTotalFNoviembreI() {
        return totalFNoviembreI;
    }

    public void setTotalFNoviembreI(BigDecimal totalFNoviembreI) {
        this.totalFNoviembreI = totalFNoviembreI;
    }

    public BigDecimal getTotalFDiciembreI() {
        return totalFDiciembreI;
    }

    public void setTotalFDiciembreI(BigDecimal totalFDiciembreI) {
        this.totalFDiciembreI = totalFDiciembreI;
    }

    public BigDecimal getTotalAnualI() {
        return totalAnualI;
    }

    public void setTotalAnualI(BigDecimal totalAnualI) {
        this.totalAnualI = totalAnualI;
    }

    public BigDecimal getTotalFEneroT() {
        return totalFEneroT;
    }

    public void setTotalFEneroT(BigDecimal totalFEneroT) {
        this.totalFEneroT = totalFEneroT;
    }

    public BigDecimal getTotalFFebreroT() {
        return totalFFebreroT;
    }

    public void setTotalFFebreroT(BigDecimal totalFFebreroT) {
        this.totalFFebreroT = totalFFebreroT;
    }

    public BigDecimal getTotalFMarzoT() {
        return totalFMarzoT;
    }

    public void setTotalFMarzoT(BigDecimal totalFMarzoT) {
        this.totalFMarzoT = totalFMarzoT;
    }

    public BigDecimal getTotalFAbrilT() {
        return totalFAbrilT;
    }

    public void setTotalFAbrilT(BigDecimal totalFAbrilT) {
        this.totalFAbrilT = totalFAbrilT;
    }

    public BigDecimal getTotalFMayoT() {
        return totalFMayoT;
    }

    public void setTotalFMayoT(BigDecimal totalFMayoT) {
        this.totalFMayoT = totalFMayoT;
    }

    public BigDecimal getTotalFJunioT() {
        return totalFJunioT;
    }

    public void setTotalFJunioT(BigDecimal totalFJunioT) {
        this.totalFJunioT = totalFJunioT;
    }

    public BigDecimal getTotalFJulioT() {
        return totalFJulioT;
    }

    public void setTotalFJulioT(BigDecimal totalFJulioT) {
        this.totalFJulioT = totalFJulioT;
    }

    public BigDecimal getTotalFAgostoT() {
        return totalFAgostoT;
    }

    public void setTotalFAgostoT(BigDecimal totalFAgostoT) {
        this.totalFAgostoT = totalFAgostoT;
    }

    public BigDecimal getTotalFSeptiembreT() {
        return totalFSeptiembreT;
    }

    public void setTotalFSeptiembreT(BigDecimal totalFSeptiembreT) {
        this.totalFSeptiembreT = totalFSeptiembreT;
    }

    public BigDecimal getTotalFOctubreT() {
        return totalFOctubreT;
    }

    public void setTotalFOctubreT(BigDecimal totalFOctubreT) {
        this.totalFOctubreT = totalFOctubreT;
    }

    public BigDecimal getTotalFNoviembreT() {
        return totalFNoviembreT;
    }

    public void setTotalFNoviembreT(BigDecimal totalFNoviembreT) {
        this.totalFNoviembreT = totalFNoviembreT;
    }

    public BigDecimal getTotalFDiciembreT() {
        return totalFDiciembreT;
    }

    public void setTotalFDiciembreT(BigDecimal totalFDiciembreT) {
        this.totalFDiciembreT = totalFDiciembreT;
    }

    public BigDecimal getTotalAnualT() {
        return totalAnualT;
    }

    public void setTotalAnualT(BigDecimal totalAnualT) {
        this.totalAnualT = totalAnualT;
    }

    public SofisComboG<SgAnioFiscal> getAnioFiscalCombo() {
        return anioFiscalCombo;
    }

    public void setAnioFiscalCombo(SofisComboG<SgAnioFiscal> anioFiscalCombo) {
        this.anioFiscalCombo = anioFiscalCombo;
    }

    public Boolean getTransferencia() {
        return transferencia;
    }

    public void setTransferencia(Boolean transferencia) {
        this.transferencia = transferencia;
    }

    public FiltroMovimientos getFiltromov() {
        return filtromov;
    }

    public void setFiltromov(FiltroMovimientos filtromov) {
        this.filtromov = filtromov;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getSaldoNegativoOtrosIngresos() {
        return saldoNegativoOtrosIngresos;
    }

    public void setSaldoNegativoOtrosIngresos(Boolean saldoNegativoOtrosIngresos) {
        this.saldoNegativoOtrosIngresos = saldoNegativoOtrosIngresos;
    }

    public Boolean getSaldoCeroTransferecias() {
        return saldoCeroTransferecias;
    }

    public void setSaldoCeroTransferecias(Boolean saldoCeroTransferecias) {
        this.saldoCeroTransferecias = saldoCeroTransferecias;
    }

    public SgAreaInversion getAreaInversionSeleccionada() {
        return areaInversionSeleccionada;
    }

    public void setAreaInversionSeleccionada(SgAreaInversion areaInversionSeleccionada) {
        this.areaInversionSeleccionada = areaInversionSeleccionada;
    }

    public SgAreaInversion getSubAreaInversionSeleccionada() {
        return subAreaInversionSeleccionada;
    }

    public void setSubAreaInversionSeleccionada(SgAreaInversion subAreaInversionSeleccionada) {
        this.subAreaInversionSeleccionada = subAreaInversionSeleccionada;
    }

    public Boolean getControlInput() {
        return controlInput;
    }

    public void setControlInput(Boolean controlInput) {
        this.controlInput = controlInput;
    }

    public Boolean getSaldoNegativoSaldoEgresosAprobado() {
        return saldoNegativoSaldoEgresosAprobado;
    }

    public void setSaldoNegativoSaldoEgresosAprobado(Boolean saldoNegativoSaldoEgresosAprobado) {
        this.saldoNegativoSaldoEgresosAprobado = saldoNegativoSaldoEgresosAprobado;
    }

    public Boolean getPermiteEdicion() {
        return permiteEdicion;
    }

    public void setPermiteEdicion(Boolean permiteEdicion) {
        this.permiteEdicion = permiteEdicion;
    }

    public Boolean getBotonGuardar() {
        return botonGuardar;
    }

    public void setBotonGuardar(Boolean botonGuardar) {
        this.botonGuardar = botonGuardar;
    }

    public Boolean getBotonFormulacion() {
        return botonFormulacion;
    }

    public void setBotonFormulacion(Boolean botonFormulacion) {
        this.botonFormulacion = botonFormulacion;
    }

    public Boolean getBotonAjuste() {
        return botonAjuste;
    }

    public void setBotonAjuste(Boolean botonAjuste) {
        this.botonAjuste = botonAjuste;
    }

    public Boolean getBotonAprobado() {
        return botonAprobado;
    }

    public void setBotonAprobado(Boolean botonAprobado) {
        this.botonAprobado = botonAprobado;
    }

    public EnumPresupuestoEscolarEstado getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EnumPresupuestoEscolarEstado estadoActual) {
        this.estadoActual = estadoActual;
    }

    public List<SgMovimientos> getEdicionIngresosList() {
        return edicionIngresosList;
    }

    public void setEdicionIngresosList(List<SgMovimientos> edicionIngresosList) {
        this.edicionIngresosList = edicionIngresosList;
    }

    public List<SgMovimientos> getEdicionEgresosList() {
        return edicionEgresosList;
    }

    public void setEdicionEgresosList(List<SgMovimientos> edicionEgresosList) {
        this.edicionEgresosList = edicionEgresosList;
    }

    public Boolean getBotonEdicion() {
        return botonEdicion;
    }

    public void setBotonEdicion(Boolean botonEdicion) {
        this.botonEdicion = botonEdicion;
    }

    public List<SgMovimientos> getNuevosIngresos() {
        return nuevosIngresos;
    }

    public void setNuevosIngresos(List<SgMovimientos> nuevosIngresos) {
        this.nuevosIngresos = nuevosIngresos;
    }

    public SgMovimientos getMovEnEdicion() {
        return movEnEdicion;
    }

    public void setMovEnEdicion(SgMovimientos movEnEdicion) {
        this.movEnEdicion = movEnEdicion;
    }

    public List<SgMovimientos> getNuevosEgresos() {
        return nuevosEgresos;
    }

    public void setNuevosEgresos(List<SgMovimientos> nuevosEgresos) {
        this.nuevosEgresos = nuevosEgresos;
    }

    public List<SgMovimientos> getIngresosNuevosBusqueda() {
        return ingresosNuevosBusqueda;
    }

    public void setIngresosNuevosBusqueda(List<SgMovimientos> ingresosNuevosBusqueda) {
        this.ingresosNuevosBusqueda = ingresosNuevosBusqueda;
    }

    public List<SgMovimientos> getListaAnulEditado() {
        return listaAnulEditado;
    }

    public void setListaAnulEditado(List<SgMovimientos> listaAnulEditado) {
        this.listaAnulEditado = listaAnulEditado;
    }

    public List<SgMovimientos> getListaAnulOriginal() {
        return listaAnulOriginal;
    }

    public void setListaAnulOriginal(List<SgMovimientos> listaAnulOriginal) {
        this.listaAnulOriginal = listaAnulOriginal;
    }

    public List<SgMovimientos> getListaMovEditado() {
        return listaMovEditado;
    }

    public void setListaMovEditado(List<SgMovimientos> listaMovEditado) {
        this.listaMovEditado = listaMovEditado;
    }

    public String getTextoConfirmacionEliminacion() {
        return textoConfirmacionEliminacion;
    }

    public void setTextoConfirmacionEliminacion(String textoConfirmacionEliminacion) {
        this.textoConfirmacionEliminacion = textoConfirmacionEliminacion;
    }

    public String getTextoConfirmacionAnulacion() {
        return textoConfirmacionAnulacion;
    }

    public void setTextoConfirmacionAnulacion(String textoConfirmacionAnulacion) {
        this.textoConfirmacionAnulacion = textoConfirmacionAnulacion;
    }

    public String getTitleEdicion() {
        return titleEdicion;
    }

    public void setTitleEdicion(String titleEdicion) {
        this.titleEdicion = titleEdicion;
    }

    public BigDecimal getTotal_ita() {
        return total_ita;
    }

    public void setTotal_ita(BigDecimal total_ita) {
        this.total_ita = total_ita;
    }

    public Long getPeaId() {
        return peaId;
    }

    public void setPeaId(Long peaId) {
        this.peaId = peaId;
    }

    public SgMovimientosEdicion getEdicionMovimientoEnEdicion() {
        return edicionMovimientoEnEdicion;
    }

    public void setEdicionMovimientoEnEdicion(SgMovimientosEdicion edicionMovimientoEnEdicion) {
        this.edicionMovimientoEnEdicion = edicionMovimientoEnEdicion;
    }

    // </editor-fold>
}
