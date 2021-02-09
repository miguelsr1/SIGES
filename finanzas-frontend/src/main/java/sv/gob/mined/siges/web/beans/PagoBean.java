/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgCajaChica;
import sv.gob.mined.siges.web.dto.SgChequera;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgFactura;
import sv.gob.mined.siges.web.dto.SgPago;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsCuenta;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumFacturaEstado;
import sv.gob.mined.siges.web.enumerados.EnumModoPago;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCajaChica;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroChequera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuenta;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFactura;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPago;
import sv.gob.mined.siges.web.lazymodels.LazyPagoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CajaChicaRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.ChequeraRestClient;
import sv.gob.mined.siges.web.restclient.CuentaRestClient;
import sv.gob.mined.siges.web.restclient.CuentasBancariasRestClient;
import sv.gob.mined.siges.web.restclient.FacturaRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.PagoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de pagos.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PagoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PagoBean.class.getName());

    @Inject
    private PagoRestClient restClient;

    @Inject
    private CuentasBancariasRestClient cuentasRestClient;

    @Inject
    private CajaChicaRestClient cajaChicaClient;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteClient;

    @Inject
    private GesPresEsRestClient subComponenteClient;

    @Inject
    private FacturaRestClient facturaRestClient;

    @Inject
    private CuentaRestClient unidadRestClient;

    @Inject
    private GesPresEsRestClient subComponenteRestClient;

    @Inject
    private ChequeraRestClient chqueraRestClient;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteRestClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroPago filtro = new FiltroPago();
    private SgPago entidadEnEdicion = new SgPago();
    private List<RevHistorico> historialPago = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private String titulo = new String("");
    private LazyPagoDataModel pagoLazyModel;

    //Enum de modo de pago
    private EnumModoPago modoPago;
    private List<SelectItem> modosPago = new ArrayList<>();
    private List<SgFactura> facturas = new ArrayList();
    private List<EnumModoPago> items = new ArrayList();
    private SofisComboG<EnumModoPago> comboModoPago = new SofisComboG<>();
    private SofisComboG<SgFactura> comboFacturas = new SofisComboG<>();
    private SofisComboG<SgFactura> comboFiltroFacturas = new SofisComboG<>();
    private SofisComboG<SgCuentasBancarias> comboCuentasBancarias = new SofisComboG<>();
    private SofisComboG<SgCajaChica> comboCuentasBancariasCC = new SofisComboG<>();
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente;
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();
    private SofisComboG<SsCuenta> comboUnidadPresupuestaria = new SofisComboG<>();
    private SofisComboG<SsCuenta> comboLineaPresupuestaria = new SofisComboG<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate currentDate = LocalDate.now();
    private LocalDateTime currentDateTime = LocalDateTime.now();
    private SgPresupuestoEscolar sede = new SgPresupuestoEscolar();
    private SgSede sedeSeleccionada;
    private boolean historial = false;
    private SgFactura factura = new SgFactura();
    private SgFactura factura2 = new SgFactura();
    private Boolean agregar = false;
    private boolean esCheque = Boolean.TRUE;
    private boolean ver = Boolean.TRUE;
    private String razonAnulacion = new String("");
    private SgFactura facturaSeleccionada;
    private Boolean aplicaGuardar = false;
    private Boolean otrosIngresosFiltro = null;
    private SgChequera chequeraSeleccionada;
    private SgCuentasBancarias cuenta = new SgCuentasBancarias();

    /**
     * Constructor de la clase.
     */
    public PagoBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda.
     */
    @PostConstruct
    public void init() {
        try {
            currentDate.format(dateFormatter);
            currentDateTime.format(formatter);
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Control de renderizado de elementos en la pantalla, dependiendo si es
     * cheque o efectivo
     */
    public void obtenerModoPago() {
        if (comboModoPago.getSelectedT() != null) {
            if (comboModoPago.getSelectedT().equals(EnumModoPago.CHEQUE)) {
                esCheque = true;
            } else {
                esCheque = false;
            }
            if (entidadEnEdicion.getPgsPk() == null) {
                comboFacturas.setSelected(-1);
            }
        }
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
     * Busca los pagos según los filtros utilizados.
     */
    public void buscar() {
        try {

            if (filtro.getPgsFechaDesdeS() != null) {
                filtro.setPgsFechaDesde(filtro.getPgsFechaDesdeS().atTime(LocalTime.MIN));
            }
            if (filtro.getPgsFechaHastaS() != null) {
                filtro.setPgsFechaHasta(filtro.getPgsFechaHastaS().atTime(LocalTime.MAX));
            }
            if (comboFiltroFacturas.getSelectedT() != null) {
                filtro.setPgsFactura(comboFiltroFacturas.getSelectedT().getFacPk());
            }
            if (comboComponente.getSelectedT() != null) {
                filtro.setComponente(comboComponente.getSelectedT().getCpeId());
            }
            if (comboSubComponente.getSelectedT() != null) {
                filtro.setSubcomponente(comboSubComponente.getSelectedT().getGesId());
            }
            if (comboUnidadPresupuestaria.getSelectedT() != null) {
                filtro.setUnidadPresupuestaria(comboUnidadPresupuestaria.getSelectedT().getCuId());
            }
            if (comboLineaPresupuestaria.getSelectedT() != null) {
                filtro.setLineaPresupuestaria(comboLineaPresupuestaria.getSelectedT().getCuId());
            }

            if (otrosIngresosFiltro != null) {
                if (otrosIngresosFiltro.equals(Boolean.TRUE)) {
                    filtro.setMovimientoOrigenFac(EnumMovimientosOrigen.P);
                } else if (otrosIngresosFiltro.equals(Boolean.FALSE)) {
                    filtro.setMovimientoOrigenFac(EnumMovimientosOrigen.T);
                }
            } else {
                filtro.setMovimientoOrigenFac(null);
            }

            filtro.setIncluirCampos(new String[]{"pgsPk",
                "pgsFactura.facPk",
                "pgsFactura.facNumero",
                "pgsFactura.facEstado",
                "pgsFactura.facTotal",
                "pgsFactura.facFechaFactura",
                "pgsFactura.facTipoDocumento",
                "pgsFactura.facVersion",
                "pgsFechaPago",
                "pgsModoPago",
                "pgsNumeroCheque",
                "pgsFactura.facProveedorFk.proId",
                "pgsFactura.facProveedorFk.proNombre",
                "pgsFactura.facProveedorFk.proVersion",
                "pgsMovimientoCBFk.mcbCuentaFK.cbcPk",
                "pgsMovimientoCBFk.mcbCuentaFK.cbcVersion",
                "pgsMovimientoCCFk.mccCuentaFK.bccPk",
                "pgsMovimientoCCFk.mccCuentaFK.bccVersion",
                "pgsUltModUsuario",
                "pgsImporte",
                "pgsComentario",
                "pgsUltModFecha",
                "pgsVersion"
            });
            filtro.setOrderBy(new String[]{"pgsFechaPago"});
            filtro.setAscending(new boolean[]{false});
            totalResultados = restClient.buscarTotal(filtro);
            pagoLazyModel = new LazyPagoDataModel(restClient, filtro, totalResultados, paginado);
            agregar = Boolean.FALSE;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos usados para crear y editar registros.
     */
    public void cargarCombos() {
        try {

            comboFiltroFacturas = new SofisComboG<>(cargarFacturasFiltros(), "facNumero");
            comboFiltroFacturas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

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

            FiltroCuenta unp = new FiltroCuenta();
            unp.setCuHabilitado(Boolean.TRUE);
            unp.setOrderBy(new String[]{"cuNombre"});
            unp.setIncluirCampos(new String[]{"cuId",
                "cuNombre",
                "cuVersion"});
            unp.setAscending(new boolean[]{true});
            List<SsCuenta> listUnidad = unidadRestClient.buscarUnidadPresupuestaria(unp);
            comboUnidadPresupuestaria = new SofisComboG(listUnidad, "cuNombre");
            comboUnidadPresupuestaria.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (BusinessException ex) {
            Logger.getLogger(PagoBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    /**
     * Se carga el combo de unidad presupuestaria (para realizar búsquedas).
     */
    public void cargarComboSubcomponente() {
        try {
            if (comboComponente.getSelectedT() != null) {
                FiltroGesPresEs filtroSubCo = new FiltroGesPresEs();
                filtroSubCo.setCpeId(comboComponente.getSelectedT().getCpeId());
                filtroSubCo.setIncluirCampos(new String[]{
                    "gesVersion",
                    "gesNombre",
                    "gesCod"});
                filtroSubCo.setOrderBy(new String[]{"gesNombre"});
                comboSubComponente = new SofisComboG<>(subComponenteRestClient.buscarSubcomponente(filtroSubCo), "gesNombre");
                comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se carga el combo de línea presupuestaria (para realizar búsquedas).
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
                comboLineaPresupuestaria.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se carga los combos de cuentas bancarias
     */
    public void cargarCuentas() {
        //if (comboFacturas.getSelectedT() != null) {
        if (facturaSeleccionada != null) {
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
     * Se carga los combos de cuentas bancarias de caja chica una vez selecciona
     * la factura
     */
    public void cargarCuentasBanCC() {
        try {
            JSFUtils.limpiarMensajesError();
            FiltroCajaChica filtroCC = new FiltroCajaChica();
            comboCuentasBancariasCC = new SofisComboG<>();
            if (facturaSeleccionada != null && facturaSeleccionada.getFacSedeFk() != null) {

                filtroCC.setBccHabilitado(Boolean.TRUE);
                filtroCC.setBccSedeFk(facturaSeleccionada.getFacSedeFk().getSedPk());

                if (facturaSeleccionada.getFacItemMovimiento() != null) {
                    if (facturaSeleccionada.getFacItemMovimiento().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.P)) {
                        filtroCC.setOtrosIngresos(Boolean.TRUE);
                    }
                    if (facturaSeleccionada.getFacItemMovimiento().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.T)) {
                        filtroCC.setSubComponenteFk(facturaSeleccionada.getFacItemMovimiento().getMovFuenteIngresos().getMovTechoPresupuestal().getSubComponente().getGesId());
                    }
                }
                if (facturaSeleccionada.getFacItemPlanCompra() != null) {
                    if (facturaSeleccionada.getFacItemPlanCompra().getComMovimientosFk().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.P)) {
                        filtroCC.setOtrosIngresos(Boolean.TRUE);
                    }
                    if (facturaSeleccionada.getFacItemPlanCompra().getComMovimientosFk().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.T)) {
                        filtroCC.setSubComponenteFk(facturaSeleccionada.getFacItemPlanCompra().getComMovimientosFk().getMovFuenteIngresos().getMovTechoPresupuestal().getSubComponente().getGesId());
                    }
                }

                filtroCC.setIncluirCampos(new String[]{
                    "bccPk",
                    "bccVersion",
                    "bccNumeroCuenta"});
                comboCuentasBancariasCC = new SofisComboG<>(cajaChicaClient.buscar(filtroCC), "bccNumeroCuenta");

                List<SgCajaChica> cajas = cajaChicaClient.buscar(filtroCC);
                if (facturaSeleccionada.getFacItemMovimiento() != null && cajas.size() == 0) {

                    if (facturaSeleccionada.getFacItemMovimiento().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.P)) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_CAJA_CHICA_OTROS_INGRESOS), "");
                    }
                    if (facturaSeleccionada.getFacItemMovimiento().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.T)) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_CAJA_CHICA_SUBCOMPONENTE), "");
                    }
                }
                if (facturaSeleccionada.getFacItemPlanCompra() != null && cajas.size() == 0) {
                    if (facturaSeleccionada.getFacItemPlanCompra().getComMovimientosFk().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.P)) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_CAJA_CHICA_OTROS_INGRESOS), "");
                    }
                    if (facturaSeleccionada.getFacItemPlanCompra().getComMovimientosFk().getMovFuenteIngresos().getMovOrigen().equals(EnumMovimientosOrigen.T)) {
                        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_CAJA_CHICA_SUBCOMPONENTE), "");
                    }
                }

            }
            comboCuentasBancariasCC.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (entidadEnEdicion.getPgsMovimientoCCFk() != null) {
                comboCuentasBancariasCC.setSelectedT(entidadEnEdicion.getPgsMovimientoCCFk().getMccCuentaFK());
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Se carga los combos de cuentas bancarias una vez selecciona la factura
     */
    public void cargarCuentasBanc() {
        try {
            FiltroCuentasBancarias filtroCb = new FiltroCuentasBancarias();
            comboCuentasBancarias = new SofisComboG<>();
            if (facturaSeleccionada != null && facturaSeleccionada.getFacSedeFk() != null) {
                filtroCb.setCbcHabilitado(Boolean.TRUE);
                filtroCb.setCbcSedeFk(facturaSeleccionada.getFacSedeFk().getSedPk());
                filtroCb.setIncluirCampos(new String[]{
                    "cbcPk",
                    "cbcVersion",
                    "cbcNumeroCuenta"});
                comboCuentasBancarias = new SofisComboG<>(cuentasRestClient.buscar(filtroCb), "cbcNumeroCuenta");
            }
            comboCuentasBancarias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (entidadEnEdicion.getPgsMovimientoCBFk() != null) {
                comboCuentasBancarias.setSelectedT(entidadEnEdicion.getPgsMovimientoCBFk().getMcbCuentaFK());
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
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
                "facItemPlanCompra.comVersion",
                "facItemMovimiento.movPk",
                "facItemMovimiento.movAreaInversionPk.adiPk",
                "facItemMovimiento.movAreaInversionPk.adiNombre",
                "facItemMovimiento.movAreaInversionPk.adiVersion",
                "facItemMovimiento.movVersion",
                "facEstado",
                "facTotal",
                "facTipoDocumento"
            });
            filtroFactura.setFacEstado(EnumFacturaEstado.EN_PROCESO);
            facturas = facturaRestClient.buscar(filtroFactura);
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return facturas;
    }

    /**
     * Se obtiene el listado de facturas
     */
    private List<SgFactura> cargarFacturasFiltros() {
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
                "facItemPlanCompra.comVersion",
                "facItemMovimiento.movPk",
                "facItemMovimiento.movAreaInversionPk.adiPk",
                "facItemMovimiento.movAreaInversionPk.adiNombre",
                "facItemMovimiento.movAreaInversionPk.adiVersion",
                "facItemMovimiento.movVersion",
                "facEstado",
                "facTotal",
                "facTipoDocumento"
            });
            facturas = facturaRestClient.buscar(filtroFactura);
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return facturas;
    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {
        comboCuentasBancarias = new SofisComboG<>();
        comboCuentasBancariasCC = new SofisComboG<>();
        comboFacturas = new SofisComboG<>();
        comboFiltroFacturas = new SofisComboG<>();
        modosPago = new ArrayList<>();
        modoPago = EnumModoPago.CHEQUE;
        comboUnidadPresupuestaria = new SofisComboG<>();
        comboLineaPresupuestaria = new SofisComboG<>();
        comboSubComponente = new SofisComboG<>();
        cargarCombos();
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroPago();
        limpiarCombos();
        otrosIngresosFiltro = null;
        buscar();
    }

    /**
     * Cargo el cotrol para el monto de acuerdo a la factura seleccionada.
     */
    public void cargarDatosFactura() {
//        if (comboFacturas.getSelectedT() != null && agregar) {
//            factura = comboFacturas.getSelectedT();

        if (facturaSeleccionada != null && agregar) {
            factura = facturaSeleccionada;
            entidadEnEdicion.setPgsImporte(factura.getFacTotal());
        } else {
            if (entidadEnEdicion.getPgsFactura() != null) {
                factura = entidadEnEdicion.getPgsFactura();
                entidadEnEdicion.setPgsImporte(factura.getFacTotal());
            }
        }
    }

    /**
     * Cargo los combos para agregar o editar un Pago
     */
    public void cargarCombosPopup(SgPago entidadEnEdicion) {
        try {
            if (agregar) {
                comboFacturas = new SofisComboG<>(cargarFacturas(), "facNumero");
            } else {
                comboFacturas = new SofisComboG<>(cargarFacturasFiltros(), "facNumero");
            }
            comboFacturas.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            if (entidadEnEdicion.getPgsFactura() != null) {
                comboFacturas.setSelectedT(entidadEnEdicion.getPgsFactura());
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
     * Limpia los filtros, además de crear un nuevo objeto de pago.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgPago();
        facturaSeleccionada = null;
        chequeraSeleccionada = null;
        agregar = Boolean.TRUE;
        ver = Boolean.FALSE;
        comboCuentasBancarias = new SofisComboG<>();
        comboCuentasBancariasCC = new SofisComboG<>();
        cargarCombosPopup(entidadEnEdicion);
        obtenerModoPago();
        titulo = Etiquetas.getValue("nuevoPago");
    }

    /**
     * Carga los datos de un pago para poder ser editados.
     *
     * @param var SgPago: Elemento del grid seleccionado de cuenta bancaria.
     */
    public void actualizar(SgPago var) {
        JSFUtils.limpiarMensajesError();
        agregar = Boolean.FALSE;
        ver = Boolean.TRUE;
        razonAnulacion = new String("");
        entidadEnEdicion = (SgPago) SerializationUtils.clone(var);
        facturaSeleccionada = entidadEnEdicion.getPgsFactura();
        if (entidadEnEdicion.getPgsModoPago() != null && entidadEnEdicion.getPgsModoPago().equals(EnumModoPago.CHEQUE)) {
            esCheque = Boolean.TRUE;
        } else {
            esCheque = Boolean.FALSE;
        }
        cargarCombosPopup(entidadEnEdicion);
        titulo = StringUtils.replace(Etiquetas.getValue("verPagox"), "[x]", entidadEnEdicion.getPgsPk().toString());
    }

    /**
     * Crea o actualiza un registro de pago.
     */
    public void guardar() {
        aplicaGuardar = Boolean.FALSE;
        try {
            if (facturaSeleccionada != null) {
                entidadEnEdicion.setPgsFactura(facturaSeleccionada);
            }
            if (comboModoPago.getSelectedT() != null) {
                if (comboModoPago.getSelectedT().equals(EnumModoPago.CHEQUE) && comboCuentasBancarias.getSelectedT() != null) {
                    entidadEnEdicion.setPgsCuentaBancaria(comboCuentasBancarias.getSelectedT());
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
                                if (entidadEnEdicion.getPgsNumeroCheque() >= che.getCheNumeroInicial() && entidadEnEdicion.getPgsNumeroCheque() <= che.getCheNumeroFinal()) {
                                    aplicaGuardar = Boolean.TRUE;
                                }
                            }
                        }
                        if (aplicaGuardar) {
                            entidadEnEdicion.setPgsModoPago(comboModoPago.getSelectedT());
                            entidadEnEdicion.setPgsChequeraFk(chequeraSeleccionada);
                            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
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
                    entidadEnEdicion.setPgsCuentaBancariaCC(comboCuentasBancariasCC.getSelectedT());
                    entidadEnEdicion.setPgsModoPago(comboModoPago.getSelectedT());
                    entidadEnEdicion = restClient.guardar(entidadEnEdicion);
                    PrimeFaces.current().executeScript("PF('itemDialog').hide()");
                    JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                    buscar();
                }
            } else {
                entidadEnEdicion.setPgsModoPago(comboModoPago.getSelectedT());
                entidadEnEdicion = restClient.guardar(entidadEnEdicion);
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
     * Elimina un registro de pago.
     */
    public void eliminar() {
        try {
            razonAnulacion = new String(razonAnulacion);
            if (!razonAnulacion.isEmpty()) {
                restClient.eliminar(entidadEnEdicion.getPgsPk(), razonAnulacion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
                buscar();
                entidadEnEdicion = null;
                razonAnulacion = new String("");
                PrimeFaces.current().executeScript("PF('confirmDialog').hide()");
            } else {
                razonAnulacion = new String("");
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_COMENTARIO_ELIMINACION_PAGO_VACIO), "");
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el autocomplete para la seleccción de Factura.
     *
     * @param id String: query del registro del cual se quiere obtener la
     * Factura.
     */
    public List<SgFactura> completeFactura(String query) {
        try {
            FiltroFactura fil = new FiltroFactura();
            fil.setFacNumeroComplete(query);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"facNumero"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{
                "facPk",
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
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movOrigen",
                "facItemPlanCompra.comMovimientosFk.movFuenteIngresos.movVersion",
                "facItemPlanCompra.comVersion",
                "facItemMovimiento.movPk",
                "facItemMovimiento.movAreaInversionPk.adiPk",
                "facItemMovimiento.movAreaInversionPk.adiNombre",
                "facItemMovimiento.movAreaInversionPk.adiVersion",
                "facItemMovimiento.movFuenteIngresos.movPk",
                "facItemMovimiento.movFuenteIngresos.movOrigen",
                "facItemMovimiento.movFuenteIngresos.movVersion",
                "facItemMovimiento.movVersion",
                "facEstado",
                "facTotal",
                "facTipoDocumento"
            });
            fil.setFacEstado(EnumFacturaEstado.EN_PROCESO);
            return facturaRestClient.buscar(fil);
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
     * Muestra el historial del registro.
     */
    public void historial(Long id) {
        try {
            historialPago = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
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
            JSFUtils.limpiarMensajesError();
            if (estId != null && estId > 0) {
                if (estRev != null && estRev > 0) {
                    entidadEnEdicion = restClient.obtenerEnRevision(estId, estRev);
                } else {
                    entidadEnEdicion = restClient.obtenerPorId(estId);
                }
            }

            if (entidadEnEdicion.getPgsModoPago() != null && entidadEnEdicion.getPgsModoPago().equals(EnumModoPago.CHEQUE)) {
                esCheque = Boolean.TRUE;
            } else {
                esCheque = Boolean.FALSE;
            }

            cargarCombosPopup(entidadEnEdicion);

            titulo = StringUtils.replace(Etiquetas.getValue("edicionPagox"), "[x]", entidadEnEdicion.getPgsPk().toString());

            historial = true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public FiltroPago getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroPago filtro) {
        this.filtro = filtro;
    }

    public SgPago getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPago entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialPago() {
        return historialPago;
    }

    public void setHistorialPago(List<RevHistorico> historialPago) {
        this.historialPago = historialPago;
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

    public LazyPagoDataModel getPagoLazyModel() {
        return pagoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyPagoDataModel pagoLazyModel) {
        this.pagoLazyModel = pagoLazyModel;
    }

    public EnumModoPago getModoPago() {
        return modoPago;
    }

    public void setModoPago(EnumModoPago modoPago) {
        this.modoPago = modoPago;
    }

    public List<SelectItem> getModosPago() {
        return modosPago;
    }

    public void setModosPago(List<SelectItem> modosPago) {
        this.modosPago = modosPago;
    }

    public SofisComboG<SgCuentasBancarias> getComboCuentasBancarias() {
        return comboCuentasBancarias;
    }

    public void setComboCuentasBancarias(SofisComboG<SgCuentasBancarias> comboCuentasBancarias) {
        this.comboCuentasBancarias = comboCuentasBancarias;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public SofisComboG<SsCategoriaPresupuestoEscolar> getComboComponente() {
        return comboComponente;
    }

    public void setComboComponente(SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente) {
        this.comboComponente = comboComponente;
    }

    public SofisComboG<SsGesPresEs> getComboSubComponente() {
        return comboSubComponente;
    }

    public void setComboSubComponente(SofisComboG<SsGesPresEs> comboSubComponente) {
        this.comboSubComponente = comboSubComponente;
    }

    public SofisComboG<SgCajaChica> getComboCuentasBancariasCC() {
        return comboCuentasBancariasCC;
    }

    public void setComboCuentasBancariasCC(SofisComboG<SgCajaChica> comboCuentasBancariasCC) {
        this.comboCuentasBancariasCC = comboCuentasBancariasCC;
    }

    public boolean isEsCheque() {
        return esCheque;
    }

    public void setEsCheque(boolean esCheque) {
        this.esCheque = esCheque;
    }

    public SofisComboG<SgFactura> getComboFacturas() {
        return comboFacturas;
    }

    public void setComboFacturas(SofisComboG<SgFactura> comboFacturas) {
        this.comboFacturas = comboFacturas;
    }

    public SofisComboG<SgFactura> getComboFiltroFacturas() {
        return comboFiltroFacturas;
    }

    public void setComboFiltroFacturas(SofisComboG<SgFactura> comboFiltroFacturas) {
        this.comboFiltroFacturas = comboFiltroFacturas;
    }

    public SofisComboG<SsCuenta> getComboUnidadPresupuestaria() {
        return comboUnidadPresupuestaria;
    }

    public void setComboUnidadPresupuestaria(SofisComboG<SsCuenta> comboUnidadPresupuestaria) {
        this.comboUnidadPresupuestaria = comboUnidadPresupuestaria;
    }

    public SgPresupuestoEscolar getSede() {
        return sede;
    }

    public void setSede(SgPresupuestoEscolar sede) {
        this.sede = sede;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public SofisComboG<SsCuenta> getComboLineaPresupuestaria() {
        return comboLineaPresupuestaria;
    }

    public void setComboLineaPresupuestaria(SofisComboG<SsCuenta> comboLineaPresupuestaria) {
        this.comboLineaPresupuestaria = comboLineaPresupuestaria;
    }

    public boolean isHistorial() {
        return historial;
    }

    public void setHistorial(boolean historial) {
        this.historial = historial;
    }

    public SgFactura getFactura() {
        return factura;
    }

    public void setFactura(SgFactura factura) {
        this.factura = factura;
    }

    public SofisComboG<EnumModoPago> getComboModoPago() {
        return comboModoPago;
    }

    public void setComboModoPago(SofisComboG<EnumModoPago> comboModoPago) {
        this.comboModoPago = comboModoPago;
    }

    public boolean isVer() {
        return ver;
    }

    public void setVer(boolean ver) {
        this.ver = ver;
    }

    public String getRazonAnulacion() {
        return razonAnulacion;
    }

    public void setRazonAnulacion(String razonAnulacion) {
        this.razonAnulacion = razonAnulacion;
    }

    public SgFactura getFacturaSeleccionada() {
        return facturaSeleccionada;
    }

    public void setFacturaSeleccionada(SgFactura facturaSeleccionada) {
        this.facturaSeleccionada = facturaSeleccionada;
    }

    public Boolean getOtrosIngresosFiltro() {
        return otrosIngresosFiltro;
    }

    public void setOtrosIngresosFiltro(Boolean otrosIngresosFiltro) {
        this.otrosIngresosFiltro = otrosIngresosFiltro;
    }

    public SgChequera getChequeraSeleccionada() {
        return chequeraSeleccionada;
    }

    public void setChequeraSeleccionada(SgChequera chequeraSeleccionada) {
        this.chequeraSeleccionada = chequeraSeleccionada;
    }
    // </editor-fold>
}
