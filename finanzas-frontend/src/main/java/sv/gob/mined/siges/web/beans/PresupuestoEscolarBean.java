/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.Jsoup;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioFiscal;
import sv.gob.mined.siges.web.dto.SgAnioLectivo;
import sv.gob.mined.siges.web.dto.SgDetallePlanEscolar;
import sv.gob.mined.siges.web.dto.SgLiquidacion;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.SgRubros;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.siap2.SgAreaInversion;
import sv.gob.mined.siges.web.enumerados.EnumEstadoLiquidacion;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.web.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioFiscal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioLectivo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAreaInversion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDetallePlanEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroLiquidacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRubros;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyPresupuestosEscolaresDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioFiscalRestClient;
import sv.gob.mined.siges.web.restclient.AnioLectivoRestClient;
import sv.gob.mined.siges.web.restclient.AreaInversionRestClient;
import sv.gob.mined.siges.web.restclient.DetallePlanEscolarRestClient;
import sv.gob.mined.siges.web.restclient.LiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.MovimientosRestClient;
import sv.gob.mined.siges.web.restclient.PlanEscolarAnualRestClient;
import sv.gob.mined.siges.web.restclient.PresupuestoEscolarRestCliente;
import sv.gob.mined.siges.web.restclient.RubrosRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de presupuesto escolar.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PresupuestoEscolarBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PresupuestoEscolarBean.class.getName());

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
    private LiquidacionRestClient liquidacionRestClient;

    @Inject
    @Param(name = "id")
    private Long presupuestoEscolarId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    @Param(name = "rev")
    private Long presRev;

    private Locale locale;
    private SgPresupuestoEscolar entidadEnEdicion = new SgPresupuestoEscolar();
    private SgAreaInversion areaInversionEnEdicion = new SgAreaInversion();
    private FiltroPresupuestoEscolar filtro = new FiltroPresupuestoEscolar();
    private FiltroAreaInversion adit = new FiltroAreaInversion();
    private SgSede sedeSeleccionada;
    private SgSede sede;
    private Long totalResultados;
    private LazyPresupuestosEscolaresDataModel reglasPresupuestoEscolarDataModel;
    private Integer paginado = 5;
    private List<RevHistorico> historialPresupuestoEscolar = new ArrayList();
    private SofisComboG<SgAnioLectivo> anioLectivoCombo;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean permiteEdicion = Boolean.FALSE;
    private Boolean borrar = Boolean.FALSE;
    private Integer selectedTab;
    private List<SgMovimientos> listMovPresupuesto = new ArrayList<>();
    private List<SgMovimientos> ingresosList;
    private List<SgMovimientos> egresosList;
    private List<SgMovimientos> transferenciaList;
    private SgMovimientos movimientoEnEdicion;
    private SofisComboG<SgDetallePlanEscolar> detPlanEscolarCombo = new SofisComboG<>();
    private SofisComboG<SgMovimientos> fuenteIngresoCombo = new SofisComboG<>();
    private String leyendaTitulo = "Edición de Movimiento";
    private String textoConfirmacionEliminacion = "";
    private String codigo = "";
    private List<SgMovimientos> listaMovE = new ArrayList();
    private SofisComboG<SgRubros> rubrosCombo = new SofisComboG<>();
    private SgMovimientos movimientoAEliminar;
    private SofisComboG<SgAreaInversion> areaInversionCombo = new SofisComboG<>();
    private SofisComboG<SgAreaInversion> subareaInversionCombo = new SofisComboG<>();
    private List<SgMovimientos> eneroList;
    private SofisComboG<SgAnioFiscal> anioFiscalCombo;
    private Boolean transferencia = Boolean.FALSE;
    private FiltroMovimientos filtromov = new FiltroMovimientos();
    private BigDecimal egresosGlobales = BigDecimal.ZERO;
    private Integer conteoSaldoOtrosIngresos = 0;
    private Integer conteoSaldoOtrosIngresosAprobado = 0;
    private Integer conteoSaldoTransferecias = 0;
    private Integer conteoSaldoTransfereciasSaldoEgresosAprobado = 0;
    private Integer conteoEgresosMontoAprobado = 0;
    private SgAreaInversion areaInversionSeleccionada;
    private SgAreaInversion subAreaInversionSeleccionada;
    EnumPresupuestoEscolarEstado estadoActual;
    private Boolean controlInput = Boolean.FALSE;
    private Boolean botonGuardar = Boolean.FALSE;
    private Boolean botonFormulacion = Boolean.FALSE;
    private Boolean botonObservar = Boolean.FALSE;
    private Boolean panelObservacion = Boolean.FALSE;
    private Boolean botonAjuste = Boolean.FALSE;
    private Boolean botonAprobado = Boolean.FALSE;
    private Boolean botonCerrar = Boolean.FALSE;
    private Boolean controlInput2 = Boolean.FALSE;
    private Boolean montoAprobado = Boolean.FALSE;
    private Boolean guardarEgreso = Boolean.FALSE;
    private SgDetallePlanEscolar pea = new SgDetallePlanEscolar();
    private Long peaId;

    private InputStream file;
    private String nombreFile;
    private Boolean uploading = Boolean.FALSE;
    private Boolean saving = Boolean.FALSE;
    private Integer filasCantMax = 2000;
    private String tamanioImportArchivo = "1048576"; //1MB por defecto
    private String tipoImportArchivo = "/(\\.|\\/)(xlsx)$/";//xlsx por defecto
    private LocalDateTime fechaMovs = LocalDateTime.now();
    private Boolean egreso = Boolean.FALSE;

    private Boolean saldoNegativoOtrosIngresos = Boolean.FALSE;
    private Boolean saldoNegativoOtrosIngresosAprobado = Boolean.FALSE;
    private Boolean saldoCeroTransferecias = Boolean.FALSE;
    private Boolean saldoNegativoSaldoEgresosAprobado = Boolean.FALSE;
    private Boolean egresosMontoAprobado = Boolean.FALSE;
    private SgAnioLectivo anioLec = new SgAnioLectivo();

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
    private Boolean mostrarBotonPEA = Boolean.FALSE;
    private Boolean aplicaCerrar = Boolean.FALSE;

    /**
     * Constructor.
     */
    public PresupuestoEscolarBean() {
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
                    "pesVersion",
                    "pesEstado",
                    "pesCodigo",
                    "pesNombre",
                    "pesUltmodFecha",
                    "pesUltmodUsuario",
                    "pesObservacion",
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
                        if (presRev != null && presRev > 0) {
                            this.actualizar(restClient.obtenerEnRevision(presupuestoEscolarId, presRev));
                            this.soloLectura = Boolean.TRUE;
                        } else {
                            actualizar(entidadEnEdicion);
                            soloLectura = editable != null ? !editable : soloLectura;
                            inicializarBotones();
                            inicializarColumnaAcciones();
                            cargarEncabezado();
                            PlanEscolarAnual();
                        }

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
     * Inicializa las columnas de acciones.
     */
    public void inicializarColumnaAcciones() {
        permiteEdicion = Boolean.FALSE;
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_PROCESO) || entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE) || entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.OBSERVADO)) {
            permiteEdicion = Boolean.TRUE;
        }
    }

    /**
     * Render para columna borrar.
     */
    public Boolean columnaBorrar(SgMovimientos mov) {

        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_PROCESO) || entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE)) {
            if (mov.getMovMonto().compareTo(BigDecimal.ZERO) == 0) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Muestra el estado y la configuración para editar el encabezado.
     */
    public void cargarEncabezado() {
        controlInput = Boolean.FALSE;
        controlInput2 = Boolean.FALSE;
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE) || entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_PROCESO)) {
            controlInput = Boolean.TRUE;
            controlInput2 = Boolean.FALSE;
        }
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.FORMULADO)
                || entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.AJUSTADO)
                || entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.OBSERVADO)
                || entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.CERRADO)
                || entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.APROBADO)) {
            controlInput = Boolean.TRUE;
            controlInput2 = Boolean.TRUE;
        }
    }

    /**
     * Maneja el control render de los botones a mostrar.
     */
    public void inicializarBotones() {
        botonGuardar = Boolean.FALSE;
        botonFormulacion = Boolean.FALSE;
        botonAjuste = Boolean.FALSE;
        botonAprobado = Boolean.FALSE;
        botonObservar = Boolean.FALSE;
        botonCerrar = Boolean.FALSE;
        panelObservacion = Boolean.FALSE;
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_PROCESO) && !soloLectura) {
            botonFormulacion = Boolean.TRUE;
            botonGuardar = Boolean.TRUE;
        }
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE) && !soloLectura) {
            botonAjuste = Boolean.TRUE;
            botonGuardar = Boolean.TRUE;

        }
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.AJUSTADO) && !soloLectura) {
            botonAprobado = Boolean.TRUE;
            botonObservar = Boolean.TRUE;
            panelObservacion = Boolean.TRUE;
        }
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.OBSERVADO) && !soloLectura) {
            botonAjuste = Boolean.TRUE;
            panelObservacion = Boolean.TRUE;
        }
        if (entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.APROBADO)) {
            botonCerrar = Boolean.TRUE;
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
            fis.setIncluirCampos(new String[]{
                "aniPk",
                "aniAnio",
                "aniVersion",});
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
     * Obtiene el id del PEA al que esta relacionado el presupuesto
     *
     */
    private void PlanEscolarAnual() {
        try {

            FiltroDetallePlanEscolar filtro = new FiltroDetallePlanEscolar();
            filtro.setSedePk(entidadEnEdicion.getPesSedeFk().getSedPk());
            List<SgDetallePlanEscolar> resultado = restDetalle.buscar(filtro);

            if (!resultado.isEmpty()) {
                for (SgDetallePlanEscolar det : resultado) {
                    det.setDpeActividad(Jsoup.parse(det.getDpeActividad()).text());
                }
                List<SgDetallePlanEscolar> nuevoResultado = resultado
                        .stream()
                        .filter(x -> x.getDpePlanEscolarAnual().getPeaAnioLectivo().getAleAnio().equals(entidadEnEdicion.getPesAnioFiscalFk().getAniAnio()))
                        .collect(Collectors.toList());
                if (!nuevoResultado.isEmpty()) {
                    pea = nuevoResultado.get(0);
                    peaId = pea.getDpePlanEscolarAnual().getPeaPk();
                    mostrarBotonPEA = Boolean.TRUE;
                }
            }

        } catch (BusinessException ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
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

            fuenteIngresoCombo = new SofisComboG(resultado, "movFuenteIngreso");
            fuenteIngresoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
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

            if (transferencia) {
                Long pkMovimientoSelect = fuenteIngresoCombo.getSelectedT().getMovPk();
                adit.setAscending(new boolean[]{true});
                return areasInversionRestClient.buscarAreaTransferencia(pkMovimientoSelect);

            } else {
                FiltroAreaInversion adi = new FiltroAreaInversion();
                adi.setAdiNombre(query);
                adi.setAdiHabilitado(Boolean.TRUE);
                adi.setMaxResults(11L);
                adi.setOrderBy(new String[]{"adiNombre"});
                adi.setAscending(new boolean[]{true});
                adi.setIncluirCampos(new String[]{"adiPk", "adiNombre", "adiVersion"});
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
     * Carga el combo oncomplete de sub áreas de Inversión relacionadas a las
     * áreas.
     *
     * @param query
     * @return
     */
    public List<SgAreaInversion> completeSubAreaInversion(String query) {
        try {
//            if (transferencia) {
//                Long pkMovimientoSelect = fuenteIngresoCombo.getSelectedT().getMovPk();
//                adit.setAscending(new boolean[]{true});
//                return areasInversionRestClient.buscarSubAreaTransferencia(pkMovimientoSelect);
//
//            } else {
            FiltroAreaInversion adi = new FiltroAreaInversion();
            adi.setOrderBy(new String[]{"adiNombre"});
            adi.setAscending(new boolean[]{true});
            adi.setIncluirCampos(new String[]{"adiPk", "adiNombre", "adiVersion", "adiPadrePk"});

            if (areaInversionSeleccionada != null) {
                adi.setAdiPadrePk(areaInversionSeleccionada.getAdiPk());
            }
            return areasInversionRestClient.buscar(adi);
//            }

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
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Inicializa el objeto.
     */
    public void agregar() {
        entidadEnEdicion = new SgPresupuestoEscolar();
        botonGuardar = Boolean.TRUE;
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
                //aqui ya esta asignado
                entidadEnEdicion = req;
                if (req.getPesAnioFiscalFk() != null) {
                    anioFiscalCombo.setSelectedT(req.getPesAnioFiscalFk());
                }

                filtromov.setIncluirCampos(new String[]{
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
                    "movTipo",
                    "movFuenteIngresos",
                    "movMonto",
                    "movVersion",
                    "movNumMoviento",
                    "movOrigen",
                    "movAnulacion",
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
                    "movSubAreaInversionPk.adiVersion"

                });
                filtromov.setOrderBy(new String[]{"movPk"});
                filtromov.setAscending(new boolean[]{true});
                filtromov.setMovPresupuestoFk(entidadEnEdicion.getPesPk());
                listMovPresupuesto = movimientosClient.buscar(filtromov);
                if (listMovPresupuesto != null && !listMovPresupuesto.isEmpty()) {
                    ingresosList = listMovPresupuesto.stream().filter(c -> c.getMovTipo().equals(EnumMovimientosTipo.I) && c.getMovOrigen().equals(EnumMovimientosOrigen.P)).collect(Collectors.toList());
                    egresosList = listMovPresupuesto.stream().filter(c -> c.getMovTipo().equals(EnumMovimientosTipo.E)).collect(Collectors.toList());
                    transferenciaList = listMovPresupuesto.stream().filter(c -> c.getMovTipo().equals(EnumMovimientosTipo.I) && c.getMovOrigen().equals(EnumMovimientosOrigen.T)).collect(Collectors.toList());
                    transferenciaList.forEach(x
                            -> x.setSaldoEgresos(
                                    x.getMovMonto().subtract(
                                            egresosList.stream()
                                                    .filter(y -> y.getMovFuenteIngresos() != null)
                                                    .filter(y -> y.getMovFuenteIngresos().getMovPk().equals(x.getMovPk())).
                                                    map(SgMovimientos::getMovMonto).reduce(BigDecimal.ZERO, BigDecimal::add)
                                    )
                            )
                    );
                    ingresosList.forEach(x
                            -> x.setSaldoEgresos(
                                    x.getMovMonto().subtract(
                                            egresosList.stream()
                                                    .filter(y -> y.getMovFuenteIngresos() != null)
                                                    .filter(y -> y.getMovFuenteIngresos().getMovPk().equals(x.getMovPk())).
                                                    map(SgMovimientos::getMovMonto).
                                                    reduce(BigDecimal.ZERO, BigDecimal::add)
                                    )
                            )
                    );

                    ingresosList.forEach(x
                            -> x.setSaldoOtrosIngresosAprobado(
                                    x.getMovMonto().subtract(
                                            egresosList.stream()
                                                    .filter(y -> y.getMovFuenteIngresos() != null)
                                                    .filter(y -> y.getMovFuenteIngresos().getMovPk().equals(x.getMovPk())).
                                                    map(SgMovimientos::getMovMontoAprobado).
                                                    reduce(BigDecimal.ZERO, BigDecimal::add)
                                    )
                            )
                    );

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
        movimientoEnEdicion.setMovTipo(EnumMovimientosTipo.I);
        movimientoEnEdicion.setMovOrigen(EnumMovimientosOrigen.P);
        leyendaTitulo = "Ingreso";
        cargarComboRubros();

    }

    /**
     * Crea un nuevo egreso de un presupuesto escolar
     */
    public void agregarEgreso() {
        JSFUtils.limpiarMensajesError();
        montoAprobado = Boolean.FALSE;
        areaInversionSeleccionada = null;
        subAreaInversionSeleccionada = null;
        movimientoEnEdicion = new SgMovimientos();
        if (!entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE)) {
            movimientoEnEdicion.setMovMontoAprobado(BigDecimal.ZERO);
        }
        movimientoEnEdicion.setMovTipo(EnumMovimientosTipo.E);
        cargarComboActividades();
        cargarComboFuenteIngresos();
        leyendaTitulo = "Egreso";

    }

    /**
     * Carga los datos de un egreso para su edicción.
     */
    public void editarEgreso(SgMovimientos mov) {
        try {
            montoAprobado = Boolean.FALSE;
            leyendaTitulo = "Edición de Movimiento";
            movimientoEnEdicion = mov;
            cargarComboActividades();
            detPlanEscolarCombo.setSelectedT(movimientoEnEdicion.getMovActividadPk());
            cargarComboFuenteIngresos();
            fuenteIngresoCombo.setSelectedT(movimientoEnEdicion.getMovFuenteIngresos());
            cargarComboAreaInversion();
            areaInversionSeleccionada = movimientoEnEdicion.getMovAreaInversionPk();
            subAreaInversionSeleccionada = movimientoEnEdicion.getMovSubAreaInversionPk();
            if (movimientoEnEdicion.getMovMonto() != null && entidadEnEdicion.getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE)) {
                montoAprobado = Boolean.TRUE;
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
            JSFUtils.limpiarMensajesError();
            leyendaTitulo = "Edición de Movimiento";
            movimientoEnEdicion = mov;
            cargarComboRubros();
            if (movimientoEnEdicion.getMovRubroPk() != null) {
                rubrosCombo.setSelectedT(movimientoEnEdicion.getMovRubroPk());
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Inicializa el objeto para su eliminación.
     */
    public void prepararEliminarMovimiento(SgMovimientos mov) {
        try {
            movimientoAEliminar = null;
            movimientoAEliminar = mov;
            if (movimientoAEliminar != null && mov.getMovTipo().equals(EnumMovimientosTipo.I)) {
                FiltroMovimientos movE = new FiltroMovimientos();
                movE.setOrderBy(new String[]{"movPk"});
                movE.setIncluirCampos(new String[]{
                    "movPk",
                    "movTipo",
                    "movVersion",
                    "movFuenteIngresos.movPk",
                    "movFuenteIngresos.movVersion"
                });
                movE.setMovFuenteIngresos(movimientoAEliminar.getMovPk());
                movE.setAscending(new boolean[]{true});
                movE.setMovPresupuestoFk(movE.getMovPresupuestoFk());
                listaMovE = movimientosClient.buscar(movE);

                if (!listaMovE.isEmpty()) {
                    textoConfirmacionEliminacion = "El registro posee egresos relacionados ¿Realmente desea continuar?";
                    listaMovE = egresosList.stream().filter(e -> e.getMovFuenteIngresos().getMovPk().equals(movimientoAEliminar.getMovPk())).collect(Collectors.toList());
                } else {
                    textoConfirmacionEliminacion = "¿Realmente desea eliminar este registro?";
                }
            } else {
                textoConfirmacionEliminacion = "¿Realmente desea eliminar este registro?";
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
                if (movimientoAEliminar.getMovTipo().equals(EnumMovimientosTipo.I)) {
                    if (!listaMovE.isEmpty() && listaMovE != null) {
                        eliminarIngreso();
                    } else {
                        movimientosClient.eliminar(movimientoAEliminar.getMovPk());
                        JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                        movimientoAEliminar = null;
                        listaMovE = null;
                        actualizar(entidadEnEdicion);
                    }
                } else {
                    movimientosClient.eliminar(movimientoAEliminar.getMovPk());
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                    movimientoAEliminar = null;
                    listaMovE = null;
                    actualizar(entidadEnEdicion);
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
     * Elimina el movimiento de Ingreso del presupuesto.
     */
    public void eliminarIngreso() {
        try {
            if (movimientoAEliminar != null) {
                movimientosClient.eliminarIngreso(movimientoAEliminar.getMovPk());
            }
            movimientoAEliminar = null;
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            actualizar(entidadEnEdicion);
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

                if (movimientoEnEdicion.isIngreso()) {
                    guardarIngreso();
                } else if (movimientoEnEdicion.isEgreso()) {
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
            movimientoEnEdicion.setMovFuenteIngresos(fuenteIngresoCombo.getSelectedT());
            movimientoEnEdicion.setMovRubroPk(rubrosCombo.getSelectedT());
            movimientoEnEdicion.setMovFuenteIngresos(null);
            movimientoEnEdicion = movimientosClient.guardar(movimientoEnEdicion);
            actualizar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialogEgresos').hide()");
            movimientoEnEdicion = null;
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
            guardarEgreso = Boolean.FALSE;
            movimientoEnEdicion.setMovPresupuestoFk(entidadEnEdicion);
            movimientoEnEdicion.setMovActividadPk(detPlanEscolarCombo.getSelectedT());
            movimientoEnEdicion.setMovFuenteIngresos(fuenteIngresoCombo.getSelectedT());
            if (transferencia == true) {
            } else {
                movimientoEnEdicion.setMovAreaInversionPk(areaInversionSeleccionada);
            }
            movimientoEnEdicion.setMovSubAreaInversionPk(subAreaInversionSeleccionada);

            if (!guardarEgreso) {
                movimientoEnEdicion = movimientosClient.guardar(movimientoEnEdicion);
                PrimeFaces.current().executeScript("PF('itemDialogEgresos').hide()");
                actualizar(entidadEnEdicion);
                movimientoEnEdicion = null;
                areaInversionSeleccionada = null;
                subAreaInversionSeleccionada = null;
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            }

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
            return Etiquetas.getValue("edicionPresupuestoEscolar");
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
            if (ingresosList.isEmpty() && egresosList.isEmpty()) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_PRESUPUESTO_SIN_MOVIMIENTOS), "");
                saldoNegativoOtrosIngresos = Boolean.FALSE;
                saldoCeroTransferecias = Boolean.FALSE;
            } else {
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
                        inicializarBotones();
                        inicializarColumnaAcciones();
                        cargarEncabezado();
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
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Valadación de montos que se realiza previa al cambio de estado.
     */
    public void confirmarAjuste() {
        try {

            transferenciaList.forEach(mov -> {
                try {
                    if (mov.getSaldoEgresosAprobado().compareTo(BigDecimal.ZERO) != 0) {
                        conteoSaldoTransfereciasSaldoEgresosAprobado = conteoSaldoTransfereciasSaldoEgresosAprobado + 1;
                        saldoNegativoSaldoEgresosAprobado = Boolean.TRUE;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            });

            ingresosList.forEach(mov -> {
                try {
                    if (mov.getSaldoOtrosIngresosAprobado().signum() < 0) {
                        conteoSaldoOtrosIngresosAprobado = conteoSaldoOtrosIngresosAprobado + 1;
                        saldoNegativoOtrosIngresosAprobado = Boolean.TRUE;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            });

            if (!saldoNegativoSaldoEgresosAprobado && !saldoNegativoOtrosIngresosAprobado) {
                estadoActual = entidadEnEdicion.getPesEstado();
                entidadEnEdicion.setPesEstado(EnumPresupuestoEscolarEstado.AJUSTADO);
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                inicializarBotones();
                inicializarColumnaAcciones();
                cargarEncabezado();
            } else if (saldoNegativoSaldoEgresosAprobado) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_VERIFICAR_SALDOS_EGRESOS_APROBADOS), "");
                saldoNegativoSaldoEgresosAprobado = Boolean.FALSE;
            } else if (saldoNegativoOtrosIngresosAprobado) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NEGATIVO_EGRESOS_MONTO_APROBADO), "");
                saldoNegativoOtrosIngresosAprobado = Boolean.FALSE;
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
     * Guarda el objeto.
     */
    public void guardar() {
        try {
            if (entidadEnEdicion.getPesPk() != null) {
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                inicializarBotones();
                inicializarColumnaAcciones();
                cargarEncabezado();

            } else {
                entidadEnEdicion.setPesEstado(EnumPresupuestoEscolarEstado.EN_PROCESO);
                entidadEnEdicion.setPesSedeFk(sedeSeleccionada);
                entidadEnEdicion.setPesAnioFiscalFk(anioFiscalCombo.getSelectedT());
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                inicializarBotones();
                inicializarColumnaAcciones();
                cargarEncabezado();
                sede = null;

            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
            entidadEnEdicion.setPesEstado(null);
        } catch (Exception ex) {
            entidadEnEdicion.setPesEstado(null);
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
                    if (mov.getSaldoEgresosAprobado().compareTo(BigDecimal.ZERO) != 0) {
                        conteoSaldoTransfereciasSaldoEgresosAprobado = conteoSaldoTransfereciasSaldoEgresosAprobado + 1;
                        saldoNegativoSaldoEgresosAprobado = Boolean.TRUE;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            });

            ingresosList.forEach(mov -> {
                try {
                    if (mov.getSaldoOtrosIngresosAprobado().signum() < 0) {
                        conteoSaldoOtrosIngresosAprobado = conteoSaldoOtrosIngresosAprobado + 1;
                        saldoNegativoOtrosIngresosAprobado = Boolean.TRUE;
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
            });

            if (!saldoNegativoSaldoEgresosAprobado && !saldoNegativoOtrosIngresosAprobado) {
                estadoActual = entidadEnEdicion.getPesEstado();
                entidadEnEdicion.setPesEstado(EnumPresupuestoEscolarEstado.APROBADO);
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                inicializarBotones();
                inicializarColumnaAcciones();
                cargarEncabezado();
            } else if (saldoNegativoSaldoEgresosAprobado) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_VERIFICAR_SALDOS_EGRESOS_APROBADOS), "");
                saldoNegativoSaldoEgresosAprobado = Boolean.FALSE;
            } else if (saldoNegativoOtrosIngresosAprobado) {
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_NEGATIVO_EGRESOS_MONTO_APROBADO), "");
                saldoNegativoOtrosIngresosAprobado = Boolean.FALSE;
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
     * Guarda la observacion y cambia de estado.
     */
    public void guardarObservacion() {
        try {
            entidadEnEdicion.setPesEstado(EnumPresupuestoEscolarEstado.OBSERVADO);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            inicializarBotones();
            inicializarColumnaAcciones();
            cargarEncabezado();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
            entidadEnEdicion.setPesEstado(null);
        } catch (Exception ex) {
            entidadEnEdicion.setPesEstado(null);
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Guarda cambio de estado presupuesto a cerrado.
     */
    public void cerrarPresupuesto() {
        try {
            aplicaCerrar = Boolean.FALSE;
            FiltroAnioLectivo anio = new FiltroAnioLectivo();
            anio.setAleAnio(entidadEnEdicion.getPesAnioFiscalFk().getAniAnio());
            List<SgAnioLectivo> listAnio = anioLectRestClient.buscar(anio);
            if (!listAnio.isEmpty()) {
                anioLec = listAnio.get(0);
            }

            for (int i = 0; i < transferenciaList.size(); i++) {
                try {
                    SgMovimientos mov = transferenciaList.get(0);
                    FiltroLiquidacion liquidacion = new FiltroLiquidacion();
                    liquidacion.setLiqSedePk(entidadEnEdicion.getPesSedeFk().getSedPk());
                    liquidacion.setLiqComponentePk(mov.getMovTechoPresupuestal().getSubComponente().getGesCategoriaComponente().getCpeId());
                    liquidacion.setLiqSubComponenteFk(mov.getMovTechoPresupuestal().getSubComponente().getGesId());
                    liquidacion.setLiqEstado(EnumEstadoLiquidacion.APROBADA);
                    liquidacion.setAnioLectivoPk(anioLec.getAlePk());
                    List<SgLiquidacion> liquidaciones = liquidacionRestClient.buscar(liquidacion);

                    if (liquidaciones.isEmpty()) {

                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_PRESUPUESTO_LIQUIDACIONES_PENDIENTES), "");
                        aplicaCerrar = Boolean.FALSE;
                        break;

                    } else {
                        aplicaCerrar = Boolean.TRUE;
                    }

                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }

            }
            if (aplicaCerrar) {
                entidadEnEdicion.setPesEstado(EnumPresupuestoEscolarEstado.CERRADO);
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                inicializarBotones();
                inicializarColumnaAcciones();
                cargarEncabezado();
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
            entidadEnEdicion.setPesEstado(null);
        } catch (Exception ex) {
            entidadEnEdicion.setPesEstado(null);
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
     * Setea el código de la sede seleccionada.
     */
    public void setearCodigo() {
        if (sedeSeleccionada != null) {
            sede = sedeSeleccionada;
        }
        if (anioFiscalCombo.getSelectedT() != null && sede != null) {
            codigo = (anioFiscalCombo.getSelectedT().getAniAnio() + "-" + sede.getSedCodigo()).toString();
            entidadEnEdicion.setPesCodigo(codigo);
        }
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
     * Método event change tab.
     */
    public void changeTab(TabChangeEvent event) {

    }

    /**
     * Inicialización de las variables para subida de archivo
     */
    public void nuevoImport() {
        nombreFile = null;
        file = null;
        uploading = Boolean.FALSE;
    }

    /**
     * Inicialización de las variables para subida de archivo
     */
    public void nuevoImportEgreso() {
        nombreFile = null;
        file = null;
        uploading = Boolean.FALSE;
        egreso = Boolean.TRUE;

    }

    /**
     * Render en subida archivo de archivo
     */
    public void renderLoading() {
        if (file != null) {
            if (uploading) {
                uploading = Boolean.FALSE;
                if (egreso) {
                    procesarEgreso();
                } else {
                    procesarArchivo();
                }
            } else {
                uploading = Boolean.TRUE;
            }
        }
    }

    /**
     * Subida de archivo de archivo
     */
    public void handleFileUpload(FileUploadEvent event) {
        try {
            nombreFile = event.getFile().getFileName();
            file = event.getFile().getInputstream();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void procesarArchivo() {
        org.apache.poi.ss.usermodel.Workbook myWorkBook = null;
        try {
            if (file != null) {
                myWorkBook = WorkbookFactory.create(file);
                org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

                Integer cantRows = 0;

                // Get iterator to all the rows in current sheet
                Iterator<Row> rowIterator = mySheet.iterator();
                List<String[]> archivo = new ArrayList<>();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    if (row.getRowNum() > 0) {
                        Cell cell;
                        Long rubroId = null;
                        String descripcion = null;
                        BigDecimal monto = BigDecimal.ZERO;
                        Long presupuestoPk = entidadEnEdicion.getPesPk();

                        //Se extraen los demas datos
                        String[] datos = new String[4];

                        //Extraemos el valor del rubro
                        cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            rubroId = new Long((long) cell.getNumericCellValue());
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_ID_REQ_IMPORT_VACIO), "");
                            return;
                        }

                        datos[0] = String.valueOf(rubroId);

                        //Extraemos la descripcion del ingreso
                        cell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && StringUtils.isNotBlank(cell.getStringCellValue())) {
                            descripcion = cell.getStringCellValue();
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_NIE_IMPORT_VACIO), "");
                            return;
                        }
                        datos[1] = String.valueOf(descripcion);

                        //Extraemos el monto
                        cell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            monto = new BigDecimal(cell.getNumericCellValue());
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_NIE_IMPORT_VACIO), "");
                            return;
                        }
                        datos[2] = String.valueOf(monto);
                        datos[3] = String.valueOf(presupuestoPk);

                        archivo.add(datos);
                        cantRows++;

                    }
                }

                if (cantRows > filasCantMax) {
                    file = null;
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "La cantidad de filas del archivo no puede ser mayor a " + filasCantMax + ".", "");
                    return;
                }

                String[][] itemsArray = new String[archivo.size()][];
                itemsArray = archivo.toArray(new String[archivo.size()][]);

                movimientosClient.importar(itemsArray);  // NUEVOOOO
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, "Se procesaron correctamente " + cantRows + " Ingresos.", "");
                actualizar(entidadEnEdicion);
            }
        } catch (BusinessException be) {
            file = null;
            JSFUtils.agregarMensajesDetalle(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            file = null;
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } finally {
            if (myWorkBook != null) {
                try {
                    myWorkBook.close();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
        }
    }

    public void procesarEgreso() {
        org.apache.poi.ss.usermodel.Workbook myWorkBook = null;
        try {
            if (file != null) {
                myWorkBook = WorkbookFactory.create(file);
                org.apache.poi.ss.usermodel.Sheet mySheet = myWorkBook.getSheetAt(0);

                Integer cantRows = 0;

                // Get iterator to all the rows in current sheet
                Iterator<Row> rowIterator = mySheet.iterator();
                List<String[]> archivo = new ArrayList<>();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();

                    if (row.getRowNum() > 0) {
                        Cell cell;

                        String descripcion = null;
                        BigDecimal monto = BigDecimal.ZERO;
                        Long actividadPk = null;
                        Long fuenteIngresosPk = null;
                        Long subAreaInversionPk = null;
                        Long presupuestoPk = entidadEnEdicion.getPesPk();

                        //Se extraen los demas datos
                        String[] datos = new String[6];

                        //Extraemos la descripcion del egreso
                        cell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && StringUtils.isNotBlank(cell.getStringCellValue())) {
                            descripcion = cell.getStringCellValue();
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_NIE_IMPORT_VACIO), "");
                            return;
                        }
                        datos[0] = String.valueOf(descripcion);

                        //Extraemos el monto
                        cell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            monto = new BigDecimal(cell.getNumericCellValue());
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_NIE_IMPORT_VACIO), "");
                            return;
                        }
                        datos[1] = String.valueOf(monto);

                        //Extraemos el valor de la actividad
                        cell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            actividadPk = new Long((long) cell.getNumericCellValue());
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_ID_REQ_IMPORT_VACIO), "");
                            return;
                        }
                        datos[2] = String.valueOf(actividadPk);

                        //Extraemos el valor de la fuente de ingreso
                        cell = row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            fuenteIngresosPk = new Long((long) cell.getNumericCellValue());
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_ID_REQ_IMPORT_VACIO), "");
                            return;
                        }
                        datos[3] = String.valueOf(fuenteIngresosPk);

                        //Extraemos el valor de la sub area de inversion
                        cell = row.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                        if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            subAreaInversionPk = new Long((long) cell.getNumericCellValue());
                        } else {
                            file = null;
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_ID_REQ_IMPORT_VACIO), "");
                            return;
                        }
                        datos[4] = String.valueOf(subAreaInversionPk);

                        datos[5] = String.valueOf(presupuestoPk);

                        archivo.add(datos);
                        cantRows++;

                    }
                }

                if (cantRows > filasCantMax) {
                    file = null;
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, "La cantidad de filas del archivo no puede ser mayor a " + filasCantMax + ".", "");
                    return;
                }

                String[][] itemsArray = new String[archivo.size()][];
                itemsArray = archivo.toArray(new String[archivo.size()][]);

                movimientosClient.importarEgresos(itemsArray);  // NUEVOOOO
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, "Se procesaron correctamente " + cantRows + " Egresos.", "");
                actualizar(entidadEnEdicion);
            }
        } catch (BusinessException be) {
            file = null;
            JSFUtils.agregarMensajesDetalle(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            file = null;
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        } finally {
            if (myWorkBook != null) {
                try {
                    myWorkBook.close();
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
                }
            }
        }
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

    public String getTextoConfirmacionEliminacion() {
        return textoConfirmacionEliminacion;
    }

    public void setTextoConfirmacionEliminacion(String textoConfirmacionEliminacion) {
        this.textoConfirmacionEliminacion = textoConfirmacionEliminacion;
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

    public List<SgMovimientos> getListaMovE() {
        return listaMovE;
    }

    public void setListaMovE(List<SgMovimientos> listaMovE) {
        this.listaMovE = listaMovE;
    }

    public Boolean getControlInput2() {
        return controlInput2;
    }

    public void setControlInput2(Boolean controlInput2) {
        this.controlInput2 = controlInput2;
    }

    public Boolean getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(Boolean montoAprobado) {
        this.montoAprobado = montoAprobado;
    }

    public SgSede getSede() {
        return sede;
    }

    public void setSede(SgSede sede) {
        this.sede = sede;
    }

    public BigDecimal getTotal_ita() {
        return total_ita;
    }

    public void setTotal_ita(BigDecimal total_ita) {
        this.total_ita = total_ita;
    }

    public Boolean getBorrar() {
        return borrar;
    }

    public void setBorrar(Boolean borrar) {
        this.borrar = borrar;
    }

    public Long getPeaId() {
        return peaId;
    }

    public void setPeaId(Long peaId) {
        this.peaId = peaId;
    }

    public Boolean getMostrarBotonPEA() {
        return mostrarBotonPEA;
    }

    public void setMostrarBotonPEA(Boolean mostrarBotonPEA) {
        this.mostrarBotonPEA = mostrarBotonPEA;
    }

    public EnumPresupuestoEscolarEstado getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EnumPresupuestoEscolarEstado estadoActual) {
        this.estadoActual = estadoActual;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public String getNombreFile() {
        return nombreFile;
    }

    public void setNombreFile(String nombreFile) {
        this.nombreFile = nombreFile;
    }

    public Boolean getUploading() {
        return uploading;
    }

    public void setUploading(Boolean uploading) {
        this.uploading = uploading;
    }

    public Boolean getSaving() {
        return saving;
    }

    public void setSaving(Boolean saving) {
        this.saving = saving;
    }

    public Integer getFilasCantMax() {
        return filasCantMax;
    }

    public void setFilasCantMax(Integer filasCantMax) {
        this.filasCantMax = filasCantMax;
    }

    public String getTamanioImportArchivo() {
        return tamanioImportArchivo;
    }

    public void setTamanioImportArchivo(String tamanioImportArchivo) {
        this.tamanioImportArchivo = tamanioImportArchivo;
    }

    public String getTipoImportArchivo() {
        return tipoImportArchivo;
    }

    public void setTipoImportArchivo(String tipoImportArchivo) {
        this.tipoImportArchivo = tipoImportArchivo;
    }

    public Boolean getBotonObservar() {
        return botonObservar;
    }

    public void setBotonObservar(Boolean botonObservar) {
        this.botonObservar = botonObservar;
    }

    public Boolean getPanelObservacion() {
        return panelObservacion;
    }

    public void setPanelObservacion(Boolean panelObservacion) {
        this.panelObservacion = panelObservacion;
    }

    public Boolean getBotonCerrar() {
        return botonCerrar;
    }

    public void setBotonCerrar(Boolean botonCerrar) {
        this.botonCerrar = botonCerrar;
    }

    public LocalDateTime getFechaMovs() {
        return fechaMovs;
    }

    public void setFechaMovs(LocalDateTime fechaMovs) {
        this.fechaMovs = fechaMovs;
    }

    // </editor-fold>
}
