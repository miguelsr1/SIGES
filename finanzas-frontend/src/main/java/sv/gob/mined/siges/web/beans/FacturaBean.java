/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgAnioFiscal;
import sv.gob.mined.siges.web.dto.SgCajaChica;
import sv.gob.mined.siges.web.dto.SgChequera;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgEncabezadoPlanCompras;
import sv.gob.mined.siges.web.dto.SgFactura;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgPago;
import sv.gob.mined.siges.web.dto.SgPlanCompras;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsCuenta;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.dto.siap2.SsProveedor;
import sv.gob.mined.siges.web.enumerados.EnumFacturaEstado;
import sv.gob.mined.siges.web.enumerados.EnumModoPago;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.web.enumerados.EnumPlanCompraEstado;
import sv.gob.mined.siges.web.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.web.enumerados.EnumTipoDocumentoPago;
import sv.gob.mined.siges.web.enumerados.EnumTipoItemFactura;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAnioFiscal;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCajaChica;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroChequera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuenta;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEncabezadoPlanCompra;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFactura;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlanCompras;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProveedor;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyFacturaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AnioFiscalRestClient;
import sv.gob.mined.siges.web.restclient.CajaChicaRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.ChequeraRestClient;
import sv.gob.mined.siges.web.restclient.CuentaRestClient;
import sv.gob.mined.siges.web.restclient.CuentasBancariasRestClient;
import sv.gob.mined.siges.web.restclient.FacturaRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.MovimientosRestClient;
import sv.gob.mined.siges.web.restclient.PagoRestClient;
import sv.gob.mined.siges.web.restclient.PlanComprasRestCliente;
import sv.gob.mined.siges.web.restclient.PresupuestoEscolarRestCliente;
import sv.gob.mined.siges.web.restclient.ProveedorRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de facturas.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped

public class FacturaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(FacturaBean.class.getName());

    @Inject
    private FacturaRestClient restClient;
    @Inject
    private SessionBean sessionBean;
    @Inject
    private PresupuestoEscolarRestCliente presupuestoEscolarClient;
    @Inject
    private SedeRestClient sedeRestClient;
    @Inject
    private ProveedorRestClient proveedorRestClient;
    @Inject
    private MovimientosRestClient movimientosRestClient;
    @Inject
    private PlanComprasRestCliente planCompraRestClient;
    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteRestClient;
    @Inject
    private GesPresEsRestClient subComponenteRestClient;
    @Inject
    private CuentaRestClient unidadRestClient;
    @Inject
    private AnioFiscalRestClient anioFiscalRestClient;
    @Inject
    private ChequeraRestClient chqueraRestClient;

    private LazyFacturaDataModel facturasLazyModel;
    private Integer paginado = 10;
    private Long totalResultados;
    private SgFactura entidadEnEdicion = new SgFactura();
    private SsProveedor proveedorEnEdicion = new SsProveedor();
    private FiltroFactura filtro = new FiltroFactura();
    private FiltroMovimientos filtroMovimientos = new FiltroMovimientos();
    private SgSede sedeSeleccionadaFiltro;
    private SgSede sedeSeleccionada;
    private SgPresupuestoEscolar presupuestoSeleccionado;
    private SofisComboG<EnumTipoItemFactura> comboTipoItem;
    private List<EnumTipoItemFactura> items = new ArrayList();
    private Boolean itemPlanCompras = Boolean.FALSE;
    private Boolean itemPresupuesto = Boolean.FALSE;
    private SofisComboG<SgMovimientos> comboItemMovimientos = new SofisComboG<>();
    private SofisComboG<SgPlanCompras> comboItemPlanCompras = new SofisComboG<>();
    private SgPresupuestoEscolar presupuesto = new SgPresupuestoEscolar();
    private SgPresupuestoEscolar sede = new SgPresupuestoEscolar();
    private SsProveedor proveedor = new SsProveedor();
    private BigDecimal totalFactura = BigDecimal.ZERO;
    private Long presupuestoId;
    private Locale locale;
    private List<RevHistorico> historialFacturas = new ArrayList();
    private SofisComboG<EnumFacturaEstado> comboEstado;
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubcomponente = new SofisComboG<>();
    private SofisComboG<SsCuenta> comboUnidadPresupuestaria = new SofisComboG<>();
    private SofisComboG<SsCuenta> comboLineaPresupuestaria = new SofisComboG<>();
    private SofisComboG<SgPresupuestoEscolar> comboPresupuesto = new SofisComboG<>();

    private SsProveedor proveedorSeleccionado;
    private SsProveedor proveedorSeleccionadoFiltro;
    private SofisComboG<SgAnioFiscal> anioFiscalCombo;
    private EnumTipoDocumentoPago facTipoDocumentoPago;
    private List<EnumTipoDocumentoPago> tipoDocumentoList;
    private SofisComboG<EnumTipoDocumentoPago> comboTipoDocumento;
    private Boolean anulacion = Boolean.FALSE;
    private String leyendaBoton = "";
    private Boolean tipoPlanilla = Boolean.FALSE;
    private boolean historial = false;
    private Boolean traeError = Boolean.FALSE;
    private SgEncabezadoPlanCompras encabPlan = new SgEncabezadoPlanCompras();

    /**
     * Variables que controlan la gestion de Pagos.
     */
    private SofisComboG<EnumModoPago> comboModoPago = new SofisComboG<>();
    private boolean esCheque = Boolean.TRUE;
    private SgPago pagoEnEdicion = new SgPago();
    private SofisComboG<SgFactura> comboFacturas = new SofisComboG<>();
    private List<SgFactura> facturas = new ArrayList();
    private SofisComboG<SgCuentasBancarias> comboCuentasBancarias = new SofisComboG<>();
    private SofisComboG<SgCajaChica> comboCuentasBancariasCC = new SofisComboG<>();
    private LocalDateTime currentDateTime = LocalDateTime.now();
    private LocalDate currentDate = LocalDate.now();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private SgFactura factura = new SgFactura();
    private Boolean aplicaGuardar = false;
    private Boolean permiteEdicion = Boolean.FALSE;
    private Boolean otrosIngresosFiltro = null;
    private Boolean efectivo = false;
    private SgCuentasBancarias cuenta = new SgCuentasBancarias();
    private SgChequera chequeraSeleccionada;

    @Inject
    private CuentasBancariasRestClient cuentasRestClient;
    @Inject
    private CajaChicaRestClient cajaChicaClient;

    @Inject
    private PagoRestClient pagoRestClient;

    @Inject
    private PlanComprasRestCliente planRestClient;

    @Inject
    @Param(name = "id")
    private Long facturaId;

    /**
     * Constructor de la clase.
     */
    public FacturaBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Facturas.
     */
    @PostConstruct
    public void init() {
        try {
            currentDate.format(dateFormatter);
            currentDateTime.format(formatter);
            cargarCombos();
            buscar();
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            if (facturaId != null && facturaId > 0) {
                cargarCombos();
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga los combos de filtros.
     */
    public void cargarCombos() {
        try {
            setDefaultValueCombo();
            tipoDocumentoList = new ArrayList(Arrays.asList(EnumTipoDocumentoPago.values()));
            FiltroAnioFiscal filtroAnioFiscal = new FiltroAnioFiscal();
            filtroAnioFiscal.setIncluirCampos(new String[]{
                "aniPk",
                "aniAnio",
                "aniVersion"});
            List<SgAnioFiscal> listAnio = anioFiscalRestClient.buscar(filtroAnioFiscal);
            anioFiscalCombo = new SofisComboG(listAnio, "aniAnio");
            anioFiscalCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboItemMovimientos = new SofisComboG();
            comboItemMovimientos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboItemPlanCompras = new SofisComboG();
            comboItemPlanCompras.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            items = new ArrayList(Arrays.asList(EnumTipoItemFactura.values()));
            comboTipoItem = new SofisComboG(items, "text");
            comboTipoItem.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumFacturaEstado> listEstados = new ArrayList(Arrays.asList(EnumFacturaEstado.values()));
            comboEstado = new SofisComboG(listEstados, "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            List<EnumTipoDocumentoPago> listTiposDoc = new ArrayList(Arrays.asList(EnumTipoDocumentoPago.values()));
            comboTipoDocumento = new SofisComboG(listTiposDoc, "text");
            comboTipoDocumento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            cargarComboComponentes();
//            comboSubcomponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            cargarComboUnidadaPresupuestaria();
            comboLineaPresupuestaria.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    /**
     * Carga el combo de filtro Componentes.
     */
    private void cargarComboComponentes() {
        try {
            FiltroCategoriaPresupuestoEscolar comp = new FiltroCategoriaPresupuestoEscolar();
            comp.setCpeHabilitado(Boolean.TRUE);
            comp.setOrderBy(new String[]{"cpeNombre"});
            comp.setIncluirCampos(new String[]{
                "cpeId",
                "cpeNombre",
                "cpeVersion"});
            comp.setAscending(new boolean[]{true});
            List<SsCategoriaPresupuestoEscolar> listComponente = componenteRestClient.buscarComponente(comp);
            comboComponente = new SofisComboG(listComponente, "cpeNombre");
            comboComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se asigna el primer valor por defecto de un combo.
     */
    private void setDefaultValueCombo() {
        comboSubcomponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboLineaPresupuestaria.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    /**
     * Carga el combo de filtro Subcomponente.
     */
    public void cargarComboSubcomponentes() {
        try {
            if (comboComponente.getSelectedT() != null) {
                FiltroGesPresEs filtroSubCo = new FiltroGesPresEs();
                filtroSubCo.setCpeId(comboComponente.getSelectedT().getCpeId());
                filtroSubCo.setIncluirCampos(new String[]{
                    "gesVersion",
                    "gesNombre",
                    "gesCod"});
                filtroSubCo.setOrderBy(new String[]{"gesNombre"});
                comboSubcomponente = new SofisComboG<>(subComponenteRestClient.buscarSubcomponente(filtroSubCo), "gesNombre");
                comboSubcomponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
            comboSubcomponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Cargar los presupuestos por sede
     */
    public void cargarPresupuestos() {
        try {
            comboPresupuesto = new SofisComboG<>();
            if (sedeSeleccionada != null) {
                FiltroPresupuestoEscolar filtro = new FiltroPresupuestoEscolar();
                filtro.setIdSede(sedeSeleccionada.getSedPk());
                filtro.setPesEstado(EnumPresupuestoEscolarEstado.APROBADO);
                filtro.setIncluirCampos(new String[]{
                    "pesPk",
                    "pesNombre",
                    "pesVersion"});
                filtro.setOrderBy(new String[]{"pesAnioFiscalFk.aniAnio"});
                comboPresupuesto = new SofisComboG<>(presupuestoEscolarClient.buscar(filtro), "pesComboLabel");
                comboPresupuesto.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            } else {
                comboPresupuesto.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga el combo de Unidad Presupuestaria.
     */
    private void cargarComboUnidadaPresupuestaria() {
        try {
            FiltroCuenta unp = new FiltroCuenta();
            unp.setCuHabilitado(Boolean.TRUE);
            unp.setOrderBy(new String[]{"cuNombre"});
            unp.setIncluirCampos(new String[]{
                "cuId",
                "cuNombre",
                "cuVersion"
            });
            unp.setAscending(new boolean[]{true});
            List<SsCuenta> listUnidad = unidadRestClient.buscarUnidadPresupuestaria(unp);
            comboUnidadPresupuestaria = new SofisComboG(listUnidad, "cuNombre");
            comboUnidadPresupuestaria.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga el combo de Linea Presupuestaria.
     */
    public void cargarComboLineaPresupuestaria() {
        try {
            if (Optional.of(comboUnidadPresupuestaria.getSelectedT()).isPresent()) {
                FiltroCuenta lin = new FiltroCuenta();
                lin.setCuHabilitado(Boolean.TRUE);
                lin.setOrderBy(new String[]{"cuNombre"});
                lin.setIncluirCampos(new String[]{
                    "cuId",
                    "cuNombre",
                    "cuVersion"
                });
                lin.setAscending(new boolean[]{true});
                lin.setCuCuentaPadre(comboUnidadPresupuestaria.getSelectedT());
                List<SsCuenta> listUnidad = unidadRestClient.buscar(lin);
                comboLineaPresupuestaria = new SofisComboG(listUnidad, "cuNombre");
            }
            comboLineaPresupuestaria.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {
        comboEstado.setSelected(-1);
        anioFiscalCombo.setSelected(-1);
        comboComponente.setSelected(-1);
        comboSubcomponente = new SofisComboG();
        comboUnidadPresupuestaria.setSelected(-1);
        comboLineaPresupuestaria = new SofisComboG();
        comboItemMovimientos.setSelected(-1);
        comboItemPlanCompras.setSelected(-1);
        comboTipoDocumento.setSelected(-1);
        setDefaultValueCombo();

    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Factura.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgFactura();
        permiteEdicion = Boolean.FALSE;
        anulacion = Boolean.FALSE;
        leyendaBoton = "Guardar";
        proveedorSeleccionado = null;
        proveedorSeleccionadoFiltro = null;
        sedeSeleccionada = null;
        sedeSeleccionadaFiltro = null;
        limpiarCombos();
        cargarCombos();
        pagoEnEdicion = new SgPago();
        comboModoPago = new SofisComboG<>();
        comboFacturas = new SofisComboG<>();
        //agregarPago();
        comboPresupuesto.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Pago
     */
    public void agregarPago() {
        JSFUtils.limpiarMensajesError();
        pagoEnEdicion = new SgPago();
        if (entidadEnEdicion != null) {
            factura = entidadEnEdicion;
        }
        cargarCombosPopup(pagoEnEdicion);
        obtenerModoPago();
    }

    /**
     * Crea un nuevo objeto de Proveedor.
     */
    public void agregarProveedor() {
        JSFUtils.limpiarMensajesError();
        proveedorEnEdicion = new SsProveedor();
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroFactura();
        proveedorSeleccionado = null;
        proveedorSeleccionadoFiltro = null;
        sedeSeleccionadaFiltro = null;
        otrosIngresosFiltro = null;
        limpiarCombos();
        buscar();
    }

    /**
     * Crea o actualiza un registro de Facturas.
     *
     * @param SgFactura : var.
     */
    public void actualizar(SgFactura var) {
        permiteEdicion = Boolean.FALSE;
        anulacion = Boolean.FALSE;
        leyendaBoton = "Guardar";
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (var);
        if (entidadEnEdicion != null) {
            prepararParaCargar();
            sedeSeleccionada = entidadEnEdicion.getFacSedeFk();
            comboTipoItem.setSelectedT(entidadEnEdicion.getFacTipoItem());
            cargarComboItemMovimientos();
            cargarComboItemPlanCompra();
            comboItemMovimientos.setSelectedT(entidadEnEdicion.getFacItemMovimiento());
            comboItemPlanCompras.setSelectedT(entidadEnEdicion.getFacItemPlanCompra());
            proveedorSeleccionado = entidadEnEdicion.getFacProveedorFk();

            agregarPago();

        }
    }

    /**
     * Anula un registro de Facturas.
     */
    public void anular(SgFactura var) {
        permiteEdicion = Boolean.TRUE;
        JSFUtils.limpiarMensajesError();
        anulacion = Boolean.TRUE;
        leyendaBoton = "Anular";
        entidadEnEdicion = (var);
    }

    /**
     * Busca los registros de Facturas según los filtros utilizados.
     */
    public void buscar() {
        try {

            filtro.setFirst(new Long(0));

            if (sedeSeleccionadaFiltro != null) {
                filtro.setSedePk(sedeSeleccionadaFiltro.getSedPk());
            }

            if (comboEstado.getSelectedT() != null) {
                filtro.setFacEstado(comboEstado.getSelectedT());
            }

            if (comboComponente.getSelectedT() != null) {
                filtro.setComponente(comboComponente.getSelected().longValue());
            }
            if (comboSubcomponente.getSelectedT() != null) {
                filtro.setSubcomponente(comboSubcomponente.getSelected().longValue());
            }
            if (comboUnidadPresupuestaria.getSelectedT() != null) {
                filtro.setUnidadPresupuestaria(comboUnidadPresupuestaria.getSelected().longValue());
            }
            if (comboLineaPresupuestaria.getSelectedT() != null) {
                filtro.setLineaPresupuestaria(comboLineaPresupuestaria.getSelected().longValue());
            }

            if (proveedorSeleccionadoFiltro != null) {
                filtro.setFacProveedorId(proveedorSeleccionadoFiltro.getProId());
            }

            if (anioFiscalCombo.getSelectedT() != null) {
                filtro.setFacAnioFiscalId(anioFiscalCombo.getSelected().longValue());
            }

            if (comboTipoDocumento.getSelectedT() != null) {
                filtro.setFacTipoDocumento(comboTipoDocumento.getSelectedT());
            }

            if (sedeSeleccionadaFiltro != null) {
                filtro.setSedePk(sedeSeleccionadaFiltro.getSedPk());
            }

            if (otrosIngresosFiltro != null) {
                if (otrosIngresosFiltro.equals(Boolean.TRUE)) {
                    filtro.setMovimientoOrigen(EnumMovimientosOrigen.P);
                } else if (otrosIngresosFiltro.equals(Boolean.FALSE)) {
                    filtro.setMovimientoOrigen(EnumMovimientosOrigen.T);
                }
            } else {
                filtro.setMovimientoOrigen(null);
            }

            filtro.setIncluirCampos(new String[]{
                "facPk",
                "facSedeFk.sedPk",
                "facSedeFk.sedCodigo",
                "facSedeFk.sedNombre",
                "facSedeFk.sedTipo",
                "facSedeFk.sedVersion",
                "facItemMovimiento.movPresupuestoFk.pesAnioFiscalFk.aniPk",
                "facItemMovimiento.movPresupuestoFk.pesAnioFiscalFk.aniAnio",
                "facItemMovimiento.movPresupuestoFk.pesAnioFiscalFk.aniVersion",
                "facItemPlanCompra.comPresupuestoFk.pesAnioFiscalFk.aniVersion",
                "facItemPlanCompra.comPresupuestoFk.pesAnioFiscalFk.aniAnio",
                "facItemPlanCompra.comPresupuestoFk.pesAnioFiscalFk.aniVersion",
                "facTipoDocumento",
                "facNumero",
                "facFechaFactura",
                "facTotal",
                "facSubTotal",
                "facDeducciones",
                "facTipoItem",
                "facItemPlanCompra.comPk",
                "facItemPlanCompra.comVersion",
                "facItemMovimiento.movPk",
                "facItemMovimiento.movVersion",
                "facProveedorFk.proId",
                "facProveedorFk.proCodigo",
                "facProveedorFk.proNombre",
                "facProveedorFk.proNit",
                "facProveedorFk.proVersion",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeId",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeNombre",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesCategoriaComponente.cpeVersion",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesId",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesNombre",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesVersion",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subCuenta.cuCuentaPadre.cuId",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subCuenta.cuCuentaPadre.cuNombre",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subCuenta.cuCuentaPadre.cuVersion",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subCuenta.cuId",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subCuenta.cuNombre",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subCuenta.cuVersion",
                "facItemMovimiento.movPk",
                "facItemMovimiento.movFuenteRecursos",
                "facItemMovimiento.movVersion",
                "facItemPlanCompra.comInsumoFk.insPk",
                "facItemPlanCompra.comInsumoFk.insNombre",
                "facItemPlanCompra.comInsumoFk.insVersion",
                "facItemPlanCompra.comPk",
                "facItemPlanCompra.comMovimientosFk.movPk",
                "facItemPlanCompra.comMovimientosFk.movVersion",
                "facItemPlanCompra.comVersion",
                "facEstado",
                "facNotaCredito",
                "facFechaNotaCredito",
                "facVersion"
            });
            totalResultados = restClient.buscarTotal(filtro);
            facturasLazyModel = new LazyFacturaDataModel(restClient, filtro, totalResultados, paginado);

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de movimientos de Proveedor.
     */
    public void guardarProveedor() {
        try {
            proveedorEnEdicion.setProActivo(Boolean.TRUE);
            proveedorEnEdicion.setProveedorMined(Boolean.FALSE);
            proveedorEnEdicion = proveedorRestClient.guardar(proveedorEnEdicion);
            PrimeFaces.current().executeScript("PF('itemProveedor').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Anula un registro de movimientos de Factura.
     */
    public void anular() {
        try {
            if (anulacion) {
                entidadEnEdicion.setFacEstado(EnumFacturaEstado.ANULACION);
                entidadEnEdicion = restClient.anular(entidadEnEdicion);
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                buscar();
            }
            anulacion = Boolean.FALSE;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de movimientos de Factura.
     */
    public void guardar() {
        try {
            traeError = Boolean.FALSE;
            entidadEnEdicion.setFacSedeFk(sedeSeleccionada);
            entidadEnEdicion.setFacProveedorFk(proveedorSeleccionado);
            entidadEnEdicion.setFacEstado(EnumFacturaEstado.EN_PROCESO);
            entidadEnEdicion.setFacTipoItem(comboTipoItem.getSelectedT());
            if (comboTipoItem.getSelectedT() != null) {
                if (comboTipoItem.getSelectedT().equals(EnumTipoItemFactura.MOVIMIENTOS)) {
                    if (comboItemMovimientos.getSelectedT() != null) {
                        entidadEnEdicion.setFacItemMovimiento(comboItemMovimientos.getSelectedT());
                        FiltroMovimientos mov = new FiltroMovimientos();
                        mov.setMovPresupuestoFk(presupuesto.getPesPk());
                        mov.setMovPk(comboItemMovimientos.getSelectedT().getMovPk());
                        mov.setOrderBy(new String[]{"movPk"});
                        mov.setAscending(new boolean[]{true});
                        mov.setIncluirCampos(new String[]{"movPk",
                            "movPresupuestoFk.pesPk",
                            "movPresupuestoFk.pesVersion",
                            "movActividadPk.dpePk",
                            "movActividadPk.dpeActividad",
                            "movActividadPk.dpeVersion",
                            "movFuenteRecursos",
                            "movMontoAprobado",
                            "movVersion"});
                        List<SgMovimientos> monto = movimientosRestClient.buscar(mov);

                        if (!monto.isEmpty()) {
                            SgMovimientos mov2 = monto.get(0);
                            if (!monto.isEmpty() && entidadEnEdicion.getFacSubTotal() != null) {
                                if (mov2.getMovMontoAprobado().compareTo(entidadEnEdicion.getFacTotal()) == 1
                                        || mov2.getMovMontoAprobado().compareTo(entidadEnEdicion.getFacTotal()) == 0) {
                                } else {
                                    traeError = Boolean.TRUE;
                                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_MONTO_APROBADO_PLAN_COMPRA), "");
                                }

                            }
                        }

                    }

                } else if (comboTipoItem.getSelectedT().equals(EnumTipoItemFactura.PLAN_COMPRAS)) {
                    if (comboItemPlanCompras.getSelectedT() != null) {
                        entidadEnEdicion.setFacItemPlanCompra(comboItemPlanCompras.getSelectedT());
                        FiltroPlanCompras com = new FiltroPlanCompras();
                        com.setComPresupuestoFk(presupuesto.getPesPk());
                        if (comboItemPlanCompras.getSelectedT() != null) {
                            com.setComPk(comboItemPlanCompras.getSelectedT().getComPk());
                        }
                        com.setOrderBy(new String[]{"comPk"});
                        com.setAscending(new boolean[]{true});
                        com.setIncluirCampos(new String[]{"comPk",
                            "comMovimientosFk.movPk",
                            "comMovimientosFk.movVersion",
                            "comMontoTotal",
                            "comPresupuestoFk",
                            "comVersion"});
                        List<SgPlanCompras> plan = planCompraRestClient.buscar(com);
                        SgPlanCompras plan2 = plan.get(0);
                        if (!plan.isEmpty()) {
                            FiltroMovimientos movp = new FiltroMovimientos();
                            movp.setMovPresupuestoFk(presupuesto.getPesPk());
                            movp.setMovPk(plan2.getComMovimientosFk().getMovPk());
                            movp.setOrderBy(new String[]{"movPk"});
                            movp.setAscending(new boolean[]{true});
                            movp.setIncluirCampos(new String[]{"movPk", "movPresupuestoFk", "movActividadPk", "movFuenteRecursos", "movMontoAprobado", "movVersion"});
                            List<SgMovimientos> montop = movimientosRestClient.buscar(movp);
                            SgMovimientos mov3 = montop.get(0);
                            if (!montop.isEmpty() && entidadEnEdicion.getFacSubTotal() != null) {
                                if (mov3.getMovMontoAprobado().compareTo(entidadEnEdicion.getFacTotal()) == 1
                                        || mov3.getMovMontoAprobado().compareTo(entidadEnEdicion.getFacTotal()) == 0) {

                                } else {
                                    traeError = Boolean.TRUE;
                                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_MONTO_APROBADO_PLAN_COMPRA), "");
                                }
                            }
                        }
                    }
                }
            }

            if (!traeError) {
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            }
            buscar();
            agregarPago();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Elimina un registro de movimientos de Factura.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getFacPk());
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
     * Muestra el historial del registro.
     *
     * @param id Long: id del registro del cual se quiere obtener el historial
     */
    public void historial(Long id) {
        permiteEdicion = Boolean.TRUE;
        try {
            historialFacturas = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Muestra los datos de auditoría.
     *
     * @param estId Long: Id del registro
     * @param estRev Long: Revisión del registro
     */
    public void mostrarDatos(Long estId, Long estRev) {
        try {
            limpiarCombos();
            if (estId != null && estId > 0) {
                if (estRev != null && estRev > 0) {
                    entidadEnEdicion = restClient.obtenerEnRevision(estId, estRev);
                } else {
                    entidadEnEdicion = restClient.obtenerPorId(estId);
                }
            }

            historial = true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Setea el valor de la Sede Selecccionada devuelve el objeto Sede.
     */
    public void setearSede() {

        sede.setPesSedeFk(sedeSeleccionada);
        cargarPresupuestos();
    }

    /**
     * Ajax tipo de documento.
     */
    public void tipoDocAjax() {
        if (entidadEnEdicion.getFacTipoDocumento() != null) {
            if (entidadEnEdicion.getFacTipoDocumento().equals(EnumTipoDocumentoPago.PLANILLA)) {
                tipoPlanilla = Boolean.TRUE;
            } else {
                tipoPlanilla = Boolean.FALSE;
            }
        }
    }

    /**
     * Prepara las condiciones para cargar los combos segun el tipo
     * seleccionado.
     */
    public void prepararParaCargar() {
        try {
            itemPresupuesto = Boolean.FALSE;
            itemPlanCompras = Boolean.FALSE;
            if (comboTipoItem.getSelectedT() != null) {
                if (comboTipoItem.getSelectedT().equals(EnumTipoItemFactura.MOVIMIENTOS)) {
                    itemPlanCompras = Boolean.TRUE;
                    comboItemPlanCompras.setSelected(-1);
                    cargarComboItemMovimientos();

                } else if (comboTipoItem.getSelectedT().equals(EnumTipoItemFactura.PLAN_COMPRAS)) {
                    itemPresupuesto = Boolean.TRUE;
                    comboItemMovimientos.setSelected(-1);
                    cargarComboItemPlanCompra();
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Carga el combo de Item Movimientos.
     */
    public void cargarComboItemMovimientos() {
        try {

            if (comboPresupuesto.getSelectedT() != null) {
                comboItemMovimientos = new SofisComboG();
                comboItemMovimientos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                FiltroMovimientos mov = new FiltroMovimientos();
                mov.setIncluirCampos(new String[]{
                    "movPk",
                    "movTipo",
                    "movFuenteRecursos",
                    "movPresupuestoFk",
                    "movNumMoviento",
                    "movVersion"
                });
                mov.setMovPresupuestoFk(comboPresupuesto.getSelectedT().getPesPk());
                mov.setMovTipo(EnumMovimientosTipo.E);
                List<SgMovimientos> listDescripcion = movimientosRestClient.buscar(mov);
                if (!listDescripcion.isEmpty()) {
                    comboItemMovimientos = new SofisComboG(listDescripcion, "movFuenteRecursos");
                } else {
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_FACTURA_MOVIMIENTOS), "");
                }
                comboItemMovimientos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga el combo de Items Plan de Compra.
     */
    public void cargarComboItemPlanCompra() {
        try {
            if (comboPresupuesto.getSelectedT() != null) {
                FiltroEncabezadoPlanCompra plan = new FiltroEncabezadoPlanCompra();
                plan.setPlaPresupuestoFk(comboPresupuesto.getSelectedT().getPesPk());
                plan.setIncluirCampos(new String[]{"plaPk",
                    "plaEstado",
                    "plaVersion"});
                List<SgEncabezadoPlanCompras> listPlan = planCompraRestClient.buscarPlan(plan);

                if (!listPlan.isEmpty()) {
                    encabPlan = listPlan.get(0);
                    if (encabPlan.getPlaEstado().equals(EnumPlanCompraEstado.APROBADO)) {

                        FiltroPlanCompras comp = new FiltroPlanCompras();
                        comp.setComPresupuestoFk(comboPresupuesto.getSelectedT().getPesPk());
                        comp.setIncluirCampos(new String[]{"comPk",
                            "comInsumoFk.insPk",
                            "comInsumoFk.insNombre",
                            "comInsumoFk.insVersion",
                            "comCodigo",
                            "comVersion"});
                        List<SgPlanCompras> listPlanCompras = planCompraRestClient.buscar(comp);
                        comboItemPlanCompras = new SofisComboG(listPlanCompras, "idInsumo");
                        comboItemPlanCompras.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga el autocomplete para la seleccción de Sede.
     *
     * @param id String: query del registro del cual se quiere obtener la Sede.
     */
    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{
                "sedCodigo",
                "sedNombre",
                "sedTipo",
                "sedVersion"});
            return sedeRestClient.buscar(fil);
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
     * Carga el valor total de la factura
     */
    public void calculoTotal() {
        if (entidadEnEdicion.getFacSubTotal() != null) {
            if (entidadEnEdicion.getFacDeducciones() != null) {
                totalFactura = entidadEnEdicion.getFacSubTotal().add(entidadEnEdicion.getFacDeducciones());
                entidadEnEdicion.setFacTotal(totalFactura);
            } else {
                entidadEnEdicion.setFacTotal(entidadEnEdicion.getFacSubTotal());
            }
        } else if (entidadEnEdicion.getFacDeducciones() != null) {
            if (entidadEnEdicion.getFacSubTotal() != null) {
                totalFactura = entidadEnEdicion.getFacSubTotal().add(entidadEnEdicion.getFacDeducciones());
                entidadEnEdicion.setFacTotal(totalFactura);
            } else {
                entidadEnEdicion.setFacTotal(entidadEnEdicion.getFacDeducciones());
            }
        }

    }

    /**
     * Carga el autocomplete para la seleccción de Proveedor.
     *
     * @param id String: query del registro del cual se quiere obtener el
     * Proveedor.
     */
    public List<SsProveedor> completeProveedor(String query) {
        try {
            FiltroProveedor pro = new FiltroProveedor();
            pro.setProNitNombre(query);
            pro.setProActivo(Boolean.TRUE);
            pro.setMaxResults(11L);
            pro.setOrderBy(new String[]{"proNombre"});
            pro.setAscending(new boolean[]{true});
            pro.setIncluirCampos(new String[]{"proNit",
                "proNombre",
                "proCodigo",
                "proVersion"});
            return proveedorRestClient.buscar(pro);
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
     * Carga el autocomplete para el filtro busqueda de Proveedor.
     *
     * @param id String: query del registro del cual se quiere obtener el
     * Proveedor.
     */
    public List<SsProveedor> completeProveedorFiltro(String query) {
        try {
            FiltroProveedor prob = new FiltroProveedor();
            prob.setProNitNombre(query);
            prob.setProActivo(Boolean.TRUE);
            prob.setMaxResults(11L);
            prob.setOrderBy(new String[]{"proNombre"});
            prob.setAscending(new boolean[]{true});
            prob.setIncluirCampos(new String[]{
                "proNit",
                "proNombre",
                "proCodigo",
                "proVersion"
            });
            return proveedorRestClient.buscar(prob);
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
     * Proceesos para la Gestion de Pagos.
     */
    /**
     * Control de renderizado de elementos en la pantalla, dependiendo si es
     * cheque o efectivo
     */
    public void obtenerModoPago() {
        if (comboModoPago.getSelectedT() != null) {
            if (comboModoPago.getSelectedT().equals(EnumModoPago.CHEQUE)) {
                esCheque = true;
                cargarCuentas();
            } else {
                esCheque = false;
                cargarCuentas();
            }
            if (pagoEnEdicion.getPgsPk() == null) {
                //comboFacturas.setSelected(-1);
            }
        }
    }

    /**
     * Cargo los combos para agregar o editar un Pago
     */
    public void cargarCombosPopup(SgPago entidadEnEdicion) {
        try {

            comboFacturas = new SofisComboG<>(cargarFacturas(), "facNumero");
            comboFacturas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (factura != null) {
                comboFacturas.setSelectedT(factura);
                entidadEnEdicion.setPgsImporte(factura.getFacTotal());
                entidadEnEdicion.setPgsFechaPago(factura.getFacFechaFactura().atStartOfDay());
            }
            items = new ArrayList(Arrays.asList(EnumModoPago.values()));
            comboModoPago = new SofisComboG(items, "text");
            comboModoPago.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (entidadEnEdicion.getPgsModoPago() != null) {
                comboModoPago.setSelectedT(entidadEnEdicion.getPgsModoPago());

                if (esCheque) {
                    cargarCuentasBanc();
                } else {
                    cargarCuentasBanCC();
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se carga los combos de cuentas bancarias
     */
    public void cargarCuentas() {
        if (comboFacturas.getSelectedT() != null) {
            if (comboModoPago.getSelectedT() != null) {
                if (comboModoPago.getSelectedT().equals(EnumModoPago.CHEQUE)) {
                    cargarCuentasBanc();
                } else {
                    cargarCuentasBanCC();
                }
            } else {
                //mensaje debe seleccionar el modo de pago para cargar las cuentas
            }
        }
    }

    /**
     * Se obtiene el listado de facturas
     */
    private List<SgFactura> cargarFacturas() {
        try {
            facturas = new ArrayList<>();
            FiltroFactura filtroFactura = new FiltroFactura();
            filtroFactura.setIncluirCampos(new String[]{"facPk",
                "facVersion",
                "facNumero",
                "facSedeFk.sedPk",
                "facSedeFk.sedVersion",
                "facSedeFk.sedTipo",
                "facProveedorFk.proId",
                "facProveedorFk.proVersion",
                "facFechaFactura",
                "facSubTotal",
                "facDeducciones",
                "facTipoItem",
                "facItemPlanCompra.comPk",
                "facItemPlanCompra.comMovimientosFk.movPk",
                "facItemPlanCompra.comMovimientosFk.movAreaInversionPk.adiPk",
                "facItemPlanCompra.comMovimientosFk.movAreaInversionPk.adiNombre",
                "facItemPlanCompra.comMovimientosFk.movAreaInversionPk.adiVersion",
                "facItemPlanCompra.comMovimientosFk.movVersion",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movPk",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movTechoPresupuestal.subComponente.gesId",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movTechoPresupuestal.subComponente.gesNombre",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movTechoPresupuestal.subComponente.gesVersion",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movOrigen",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movVersion",
                "facItemPlanCompra.comVersion",
                "facItemMovimiento.movPk",
                "facItemMovimiento.movAreaInversionPk.adiPk",
                "facItemMovimiento.movAreaInversionPk.adiNombre",
                "facItemMovimiento.movAreaInversionPk.adiVersion",
                "facItemMovimiento.movFuenteIngresos.movPk",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesId",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesNombre",
                "facItemMovimiento.movFuenteIngresos.movTechoPresupuestal.subComponente.gesVersion",
                "facItemMovimiento.movFuenteIngresos.movOrigen",
                "facItemMovimiento.movFuenteIngresos.movVersion",
                "facItemMovimiento.movVersion",
                "facEstado",
                "facTotal",
                "facTipoDocumento"
            });
            filtroFactura.setFacEstado(EnumFacturaEstado.EN_PROCESO);
            facturas = restClient.buscar(filtroFactura);
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return facturas;
    }

    /**
     * Se carga los combos de cuentas bancarias una vez selecciona la factura
     */
    public void cargarCuentasBanc() {
        try {
            FiltroCuentasBancarias filtroCb = new FiltroCuentasBancarias();
            comboCuentasBancarias = new SofisComboG<>();
            if (comboFacturas.getSelectedT() != null) {
                filtroCb.setCbcHabilitado(Boolean.TRUE);
                filtroCb.setCbcSedeFk(comboFacturas.getSelectedT().getFacSedeFk().getSedPk());
                filtroCb.setIncluirCampos(new String[]{
                    "cbcPk",
                    "cbcVersion",
                    "cbcNumeroCuenta"});
                comboCuentasBancarias = new SofisComboG<>(cuentasRestClient.buscar(filtroCb), "cbcNumeroCuenta");
            }
            comboCuentasBancarias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (pagoEnEdicion.getPgsMovimientoCBFk() != null) {
                comboCuentasBancarias.setSelectedT(pagoEnEdicion.getPgsMovimientoCBFk().getMcbCuentaFK());
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se carga los combos de cuentas bancarias de caja chica una vez selecciona
     * la factura
     */
    public void cargarCuentasBanCC() {
        try {
            JSFUtils.limpiarMensajesError();
            FiltroCajaChica filtroCC = new FiltroCajaChica();
            comboCuentasBancariasCC = new SofisComboG<>();
            if (comboFacturas.getSelectedT() != null) {
                filtroCC.setBccHabilitado(Boolean.TRUE);
                filtroCC.setBccSedeFk(comboFacturas.getSelectedT().getFacSedeFk().getSedPk());
                if (comboFacturas.getSelectedT().getFacItemMovimiento() != null) {
                    if (comboFacturas.getSelectedT().getFacItemMovimiento().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.P)) {
                        filtroCC.setOtrosIngresos(Boolean.TRUE);
                    }
                    if (comboFacturas.getSelectedT().getFacItemMovimiento().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.T)) {
                        filtroCC.setSubComponenteFk(comboFacturas.getSelectedT().getFacItemMovimiento().getMovFuenteIngresos().getMovTechoPresupuestal().getSubComponente().getGesId());
                    }
                }
                if (comboFacturas.getSelectedT().getFacItemPlanCompra() != null) {
                    if (comboFacturas.getSelectedT().getFacItemPlanCompra().getComMovimientosFk().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.P)) {
                        filtroCC.setOtrosIngresos(Boolean.TRUE);
                    }
                    if (comboFacturas.getSelectedT().getFacItemPlanCompra().getComMovimientosFk().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.T)) {
                        filtroCC.setSubComponenteFk(comboFacturas.getSelectedT().getFacItemPlanCompra().getComMovimientosFk().getMovFuenteIngresos().getMovTechoPresupuestal().getSubComponente().getGesId());
                    }
                }
                filtroCC.setIncluirCampos(new String[]{
                    "bccPk",
                    "bccVersion",
                    "bccNumeroCuenta"
                });
                comboCuentasBancariasCC = new SofisComboG<>(cajaChicaClient.buscar(filtroCC), "bccNumeroCuenta");

                List<SgCajaChica> cajas = cajaChicaClient.buscar(filtroCC);
                if (comboFacturas.getSelectedT().getFacItemMovimiento() != null && cajas.size() == 0) {

                    if (comboFacturas.getSelectedT().getFacItemMovimiento().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.P)) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_CAJA_CHICA_OTROS_INGRESOS), "");
                    }
                    if (comboFacturas.getSelectedT().getFacItemMovimiento().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.T)) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_CAJA_CHICA_SUBCOMPONENTE), "");
                    }
                }
                if (comboFacturas.getSelectedT().getFacItemPlanCompra() != null && cajas.size() == 0) {
                    if (comboFacturas.getSelectedT().getFacItemPlanCompra().getComMovimientosFk().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.P)) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_CAJA_CHICA_OTROS_INGRESOS), "");
                    }
                    if (comboFacturas.getSelectedT().getFacItemPlanCompra().getComMovimientosFk().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.T)) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_CAJA_CHICA_SUBCOMPONENTE), "");
                    }
                }

            }
            comboCuentasBancariasCC.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (pagoEnEdicion.getPgsMovimientoCCFk() != null) {
                comboCuentasBancariasCC.setSelectedT(pagoEnEdicion.getPgsMovimientoCCFk().getMccCuentaFK());
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Crea o actualiza un registro de pago.
     */
    public void guardarPago() {
        aplicaGuardar = Boolean.FALSE;
        try {

            if (comboFacturas.getSelectedT() != null) {
                pagoEnEdicion.setPgsFactura(comboFacturas.getSelectedT());
                entidadEnEdicion = comboFacturas.getSelectedT();
            }
            if (comboModoPago.getSelectedT() != null) {
                if (comboModoPago.getSelectedT().equals(EnumModoPago.CHEQUE) && comboCuentasBancarias.getSelectedT() != null) {
                    pagoEnEdicion.setPgsCuentaBancaria(comboCuentasBancarias.getSelectedT());
                    FiltroChequera chequera = new FiltroChequera();
                    chequera.setCuentaBancaria(cuenta.getCbcPk());
                    chequera.setIncluirCampos(new String[]{
                        "chePk",
                        "cheCuentaBancariaFk.cbcPk",
                        "cheCuentaBancariaFk.cbcNumeroCuenta",
                        "cheCuentaBancariaFk.cbcVersion",
                        "cheSerie",
                        "cheNumeroInicial",
                        "cheNumeroFinal",
                        "cheVersion"});
                    List<SgChequera> listChequeras = chqueraRestClient.buscar(chequera);
                    if (!listChequeras.isEmpty()) {
                        if (listChequeras.size() > 0) {
                            for (int i = 0; i < listChequeras.size(); i++) {
                                SgChequera che = listChequeras.get(i);
                                if (pagoEnEdicion.getPgsNumeroCheque() >= che.getCheNumeroInicial() && pagoEnEdicion.getPgsNumeroCheque() <= che.getCheNumeroFinal()) {
                                    aplicaGuardar = Boolean.TRUE;
                                }
                            }
                        }
                        if (aplicaGuardar) {
                            pagoEnEdicion.setPgsModoPago(comboModoPago.getSelectedT());
                            pagoEnEdicion.setPgsChequeraFk(chequeraSeleccionada);
                            pagoEnEdicion = pagoRestClient.guardar(pagoEnEdicion);
                            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                            buscar();
                        } else {
                            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_CHEQUE_NO_EXITE), "");
                        }
                    } else {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_CHEQUERA_NO_EXITE), "");
                    }
                }
                if (comboModoPago.getSelectedT().equals(EnumModoPago.EFECTIVO) && comboCuentasBancariasCC.getSelectedT() != null) {
                    pagoEnEdicion.setPgsCuentaBancariaCC(comboCuentasBancariasCC.getSelectedT());
                    pagoEnEdicion.setPgsModoPago(comboModoPago.getSelectedT());
                    pagoEnEdicion = pagoRestClient.guardar(pagoEnEdicion);
                    PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                    buscar();
                }
            } else {
                pagoEnEdicion.setPgsModoPago(comboModoPago.getSelectedT());
                pagoEnEdicion = pagoRestClient.guardar(pagoEnEdicion);
                PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                buscar();
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Maneja el render de la columna Acciones.
     */
    public boolean inicializarColumnaAcciones(SgFactura var) {

        if (var.getFacEstado().equals(EnumFacturaEstado.PAGADO) || var.getFacEstado().equals(EnumFacturaEstado.ANULACION)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void changeTab(TabChangeEvent event) {

    }

    /**
     * Carga el oncomplete de sedes en facturas.
     */
    public List<SgSede> completeSedeFiltro(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(fil);
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
     * Control que obtiene la cuenta seleccionada
     */
    public void obtenerCuentaSeleccioanda() {
        if (comboCuentasBancarias != null) {
            cuenta = comboCuentasBancarias.getSelectedT();
        }
    }

    /**
     * Carga el autocomplete para la seleccción de Factura.
     *
     * @param id String: query del registro del cual se quiere obtener la
     * Factura.
     */
    public List<SgChequera> completeChequera(String query) {
        try {
            FiltroChequera chequera = new FiltroChequera();
            chequera.setCheSerieComplete(query);
            chequera.setCuentaBancaria(cuenta.getCbcPk());
            chequera.setOrderBy(new String[]{"cheSerie"});
            chequera.setAscending(new boolean[]{false});
            chequera.setIncluirCampos(new String[]{
                "chePk",
                "cheCuentaBancariaFk.cbcPk",
                "cheCuentaBancariaFk.cbcNumeroCuenta",
                "cheCuentaBancariaFk.cbcVersion",
                "cheSerie",
                "cheNumeroInicial",
                "cheNumeroFinal",
                "cheVersion"});
            return chqueraRestClient.buscar(chequera);
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
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public Boolean getOtrosIngresosFiltro() {
        return otrosIngresosFiltro;
    }

    public void setOtrosIngresosFiltro(Boolean otrosIngresosFiltro) {
        this.otrosIngresosFiltro = otrosIngresosFiltro;
    }

    public LazyFacturaDataModel getFacturasLazyModel() {
        return facturasLazyModel;
    }

    public void setFacturasLazyModel(LazyFacturaDataModel facturasLazyModel) {
        this.facturasLazyModel = facturasLazyModel;
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

    public SgFactura getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgFactura entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroFactura getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroFactura filtro) {
        this.filtro = filtro;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<EnumTipoItemFactura> getComboTipoItem() {
        return comboTipoItem;
    }

    public void setComboTipoItem(SofisComboG<EnumTipoItemFactura> comboTipoItem) {
        this.comboTipoItem = comboTipoItem;
    }

    public SofisComboG<SgAnioFiscal> getAnioFiscalCombo() {
        return anioFiscalCombo;
    }

    public void setAnioFiscalCombo(SofisComboG<SgAnioFiscal> anioFiscalCombo) {
        this.anioFiscalCombo = anioFiscalCombo;
    }

    public List<EnumTipoItemFactura> getItems() {
        return items;
    }

    public void setItems(List<EnumTipoItemFactura> items) {
        this.items = items;
    }

    public Boolean getItemPlanCompras() {
        return itemPlanCompras;
    }

    public void setItemPlanCompras(Boolean itemPlanCompras) {
        this.itemPlanCompras = itemPlanCompras;
    }

    public Boolean getItemPresupuesto() {
        return itemPresupuesto;
    }

    public void setItemPresupuesto(Boolean itemPresupuesto) {
        this.itemPresupuesto = itemPresupuesto;
    }

    public SofisComboG<SgMovimientos> getComboItemMovimientos() {
        return comboItemMovimientos;
    }

    public void setComboItemMovimientos(SofisComboG<SgMovimientos> comboItemMovimientos) {
        this.comboItemMovimientos = comboItemMovimientos;
    }

    public SofisComboG<SgPlanCompras> getComboItemPlanCompras() {
        return comboItemPlanCompras;
    }

    public void setComboItemPlanCompras(SofisComboG<SgPlanCompras> comboItemPlanCompras) {
        this.comboItemPlanCompras = comboItemPlanCompras;
    }

    public SgPresupuestoEscolar getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(SgPresupuestoEscolar presupuesto) {
        this.presupuesto = presupuesto;
    }

    public SgPresupuestoEscolar getSede() {
        return sede;
    }

    public void setSede(SgPresupuestoEscolar sede) {
        this.sede = sede;
    }

    public Boolean getAnulacion() {
        return anulacion;
    }

    public void setAnulacion(Boolean anulacion) {
        this.anulacion = anulacion;
    }

    public Long getPresupuestoId() {
        return presupuestoId;
    }

    public void setPresupuestoId(Long presupuestoId) {
        this.presupuestoId = presupuestoId;
    }

    public SgPresupuestoEscolar getPresupuestoSeleccionado() {
        return presupuestoSeleccionado;
    }

    public void setPresupuestoSeleccionado(SgPresupuestoEscolar presupuestoSeleccionado) {
        this.presupuestoSeleccionado = presupuestoSeleccionado;
    }

    public BigDecimal getTotalFactura() {

        return totalFactura;
    }

    public void setTotalFactura(BigDecimal totalFactura) {
        this.totalFactura = totalFactura;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public List<RevHistorico> getHistorialFacturas() {
        return historialFacturas;
    }

    public void setHistorialFacturas(List<RevHistorico> historialFacturas) {
        this.historialFacturas = historialFacturas;
    }

    public SofisComboG<EnumFacturaEstado> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumFacturaEstado> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SsCategoriaPresupuestoEscolar> getComboComponente() {
        return comboComponente;
    }

    public void setComboComponente(SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente) {
        this.comboComponente = comboComponente;
    }

    public SofisComboG<SsGesPresEs> getComboSubcomponente() {
        return comboSubcomponente;
    }

    public void setComboSubcomponente(SofisComboG<SsGesPresEs> comboSubcomponente) {
        this.comboSubcomponente = comboSubcomponente;
    }

    public FiltroMovimientos getFiltroMovimientos() {
        return filtroMovimientos;
    }

    public void setFiltroMovimientos(FiltroMovimientos filtroMovimientos) {
        this.filtroMovimientos = filtroMovimientos;
    }

    public SofisComboG<SsCuenta> getComboUnidadPresupuestaria() {
        return comboUnidadPresupuestaria;
    }

    public void setComboUnidadPresupuestaria(SofisComboG<SsCuenta> comboUnidadPresupuestaria) {
        this.comboUnidadPresupuestaria = comboUnidadPresupuestaria;
    }

    public SofisComboG<SsCuenta> getComboLineaPresupuestaria() {
        return comboLineaPresupuestaria;
    }

    public void setComboLineaPresupuestaria(SofisComboG<SsCuenta> comboLineaPresupuestaria) {
        this.comboLineaPresupuestaria = comboLineaPresupuestaria;
    }

    public SsProveedor getProveedorSeleccionado() {
        return proveedorSeleccionado;
    }

    public void setProveedorSeleccionado(SsProveedor proveedorSeleccionado) {
        this.proveedorSeleccionado = proveedorSeleccionado;
    }

    public SsProveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(SsProveedor proveedor) {
        this.proveedor = proveedor;
    }

    public EnumTipoDocumentoPago getFacTipoDocumentoPago() {
        return facTipoDocumentoPago;
    }

    public void setFacTipoDocumentoPago(EnumTipoDocumentoPago facTipoDocumentoPago) {
        this.facTipoDocumentoPago = facTipoDocumentoPago;
    }

    public SsProveedor getProveedorSeleccionadoFiltro() {
        return proveedorSeleccionadoFiltro;
    }

    public List<EnumTipoDocumentoPago> getTipoDocumentoList() {
        return tipoDocumentoList;
    }

    public SofisComboG<EnumTipoDocumentoPago> getComboTipoDocumento() {
        return comboTipoDocumento;
    }

    public void setComboTipoDocumento(SofisComboG<EnumTipoDocumentoPago> comboTipoDocumento) {
        this.comboTipoDocumento = comboTipoDocumento;
    }

    public String getLeyendaBoton() {
        return leyendaBoton;
    }

    public void setLeyendaBoton(String leyendaBoton) {
        this.leyendaBoton = leyendaBoton;
    }

    public void setTipoDocumentoList(List<EnumTipoDocumentoPago> tipoDocumentoList) {
        this.tipoDocumentoList = tipoDocumentoList;
    }

    public void setProveedorSeleccionadoFiltro(SsProveedor proveedorSeleccionadoFiltro) {
        this.proveedorSeleccionadoFiltro = proveedorSeleccionadoFiltro;
    }

    public Boolean getTipoPlanilla() {
        return tipoPlanilla;
    }

    public void setTipoPlanilla(Boolean tipoPlanilla) {
        this.tipoPlanilla = tipoPlanilla;
    }

    public boolean isHistorial() {
        return historial;
    }

    public void setHistorial(boolean historial) {
        this.historial = historial;
    }

    public SsProveedor getProveedorEnEdicion() {
        return proveedorEnEdicion;
    }

    public void setProveedorEnEdicion(SsProveedor proveedorEnEdicion) {
        this.proveedorEnEdicion = proveedorEnEdicion;
    }

    public SofisComboG<SgPresupuestoEscolar> getComboPresupuesto() {
        return comboPresupuesto;
    }

    public void setComboPresupuesto(SofisComboG<SgPresupuestoEscolar> comboPresupuesto) {
        this.comboPresupuesto = comboPresupuesto;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's-Pagos">
    public SofisComboG<EnumModoPago> getComboModoPago() {
        return comboModoPago;
    }

    public void setComboModoPago(SofisComboG<EnumModoPago> comboModoPago) {
        this.comboModoPago = comboModoPago;
    }

    public SofisComboG<SgFactura> getComboFacturas() {
        return comboFacturas;
    }

    public void setComboFacturas(SofisComboG<SgFactura> comboFacturas) {
        this.comboFacturas = comboFacturas;
    }

    public SgPago getPagoEnEdicion() {
        return pagoEnEdicion;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public List<SgFactura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<SgFactura> facturas) {
        this.facturas = facturas;
    }

    public void setPagoEnEdicion(SgPago pagoEnEdicion) {
        this.pagoEnEdicion = pagoEnEdicion;
    }

    public SofisComboG<SgCuentasBancarias> getComboCuentasBancarias() {
        return comboCuentasBancarias;
    }

    public void setComboCuentasBancarias(SofisComboG<SgCuentasBancarias> comboCuentasBancarias) {
        this.comboCuentasBancarias = comboCuentasBancarias;
    }

    public boolean isEsCheque() {
        return esCheque;
    }

    public SgFactura getFactura() {
        return factura;
    }

    public Boolean getPermiteEdicion() {
        return permiteEdicion;
    }

    public void setPermiteEdicion(Boolean permiteEdicion) {
        this.permiteEdicion = permiteEdicion;
    }

    public void setFactura(SgFactura factura) {
        this.factura = factura;
    }

    public SofisComboG<SgCajaChica> getComboCuentasBancariasCC() {
        return comboCuentasBancariasCC;
    }

    public Boolean getTraeError() {
        return traeError;
    }

    public void setTraeError(Boolean traeError) {
        this.traeError = traeError;
    }

    public SgCuentasBancarias getCuenta() {
        return cuenta;
    }

    public void setCuenta(SgCuentasBancarias cuenta) {
        this.cuenta = cuenta;
    }

    public SgChequera getChequeraSeleccionada() {
        return chequeraSeleccionada;
    }

    public void setChequeraSeleccionada(SgChequera chequeraSeleccionada) {
        this.chequeraSeleccionada = chequeraSeleccionada;
    }

    public void setComboCuentasBancariasCC(SofisComboG<SgCajaChica> comboCuentasBancariasCC) {
        this.comboCuentasBancariasCC = comboCuentasBancariasCC;
    }

    public void setEsCheque(boolean esCheque) {
        this.esCheque = esCheque;
    }

    // </editor-fold>
}
