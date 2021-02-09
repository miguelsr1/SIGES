/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.dto.SgMovimientoLiquidacion;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.SgPago;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.SgRelCuentaTipoCuenta;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumEvaluacionLiquidacion;
import sv.gob.mined.siges.web.enumerados.EnumFacturaEstado;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.web.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.web.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.web.enumerados.EnumTipoRetiroMovimientoCB;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientos;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPago;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelCuentaTipoCuenta;
import sv.gob.mined.siges.web.lazymodels.LazyMovimientoCuentaBancariaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.CuentasBancariasRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoCuentaBancariaRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoLiquidacionRestClient;
import sv.gob.mined.siges.web.restclient.MovimientosRestClient;
import sv.gob.mined.siges.web.restclient.PagoRestClient;
import sv.gob.mined.siges.web.restclient.PresupuestoEscolarRestCliente;
import sv.gob.mined.siges.web.restclient.RelCuentaTipoCuentaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean correspondiente a los movimientos de la cuenta bancaria
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MovimientoCuentaBancariaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MovimientoCuentaBancariaBean.class.getName());

    @Inject
    private MovimientoCuentaBancariaRestClient restClient;

    @Inject
    private MovimientoLiquidacionRestClient movLiquidacionRestClient;

    @Inject
    private CuentasBancariasRestClient cuentasRestClient;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteClient;

    @Inject
    private GesPresEsRestClient subComponenteClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private MovimientosRestClient movimientoRestClient;

    @Inject
    private PresupuestoEscolarRestCliente presupuesotRestClient;

    @Inject
    private RelCuentaTipoCuentaRestClient relCTipoCuentaRestClient;

    @Inject
    private PagoRestClient pagoRestClient;

    @Inject
    @Param(name = "id")
    private Long cuentaId;

    private Locale locale;
    private BigDecimal totali = BigDecimal.ZERO;
    private BigDecimal totale = BigDecimal.ZERO;
    private String comentLiquidacion = new String();
    private String tiposCuentaBancaria = StringUtils.EMPTY;
    private BigDecimal saldoInicial = BigDecimal.ZERO;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroMovimientoCuentaBancaria filtroMov = new FiltroMovimientoCuentaBancaria();
    private FiltroCuentasBancarias filtroBanco = new FiltroCuentasBancarias();
    private SgMovimientoCuentaBancaria entidadEnEdicion = new SgMovimientoCuentaBancaria();
    private List<SgMovimientoCuentaBancaria> historialMovimientoCuentaBancaria = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyMovimientoCuentaBancariaDataModel movimientoCuentaBancariaLazyModel;
    //Enum de tipo de movimiento de cuenta
    private TipoMovimiento tipomovimiento;
    private List<SelectItem> tipoMovimiento = new ArrayList<>();
    private SgCuentasBancarias cuenta;
    private SgCuentasBancarias movimientoCheque;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate currentDate = LocalDate.now();
    private LocalDateTime currentDateTime = LocalDateTime.now();
    private Integer inicial = 0;
    private List<SgPago> pagos = new ArrayList();
    private SofisComboG<EnumTipoRetiroMovimientoCB> comboTipoRetiro = new SofisComboG<>();
    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();
    private SofisComboG<TipoMovimiento> comboTipoMovimiento = new SofisComboG<>();
    private SofisComboG<SgMovimientos> fuenteIngresoCombo = new SofisComboG<>();
    private SofisComboG<EnumEvaluacionLiquidacion> estadosLiq = new SofisComboG<>();
    private List<EnumTipoRetiroMovimientoCB> items = new ArrayList();
    private List<SgRelCuentaTipoCuenta> tiposCuentas = new ArrayList<>();

    private EnumTipoRetiroMovimientoCB retiroCheque;
    private Boolean esRetiro = Boolean.FALSE;
    private Boolean esCheque = Boolean.FALSE;
    private Boolean cobrado = Boolean.FALSE;
    private Boolean esIngreso = Boolean.FALSE;
    private Boolean visible = Boolean.FALSE;

    private List<SgMovimientoCuentaBancaria> listMovCuentaBancaria = new ArrayList<>();
    private List<SgMovimientoCuentaBancaria> ingresosList = new ArrayList<>();
    private List<SgMovimientoCuentaBancaria> egresosList = new ArrayList<>();
    private List<SgMovimientoCuentaBancaria> listSelected = new ArrayList<>();
    private List<SgMovimientos> listMovPresupuesto = new ArrayList<>();
    private String sede = new String("");

    private Boolean agregarTransaccion = Boolean.FALSE;
    private Boolean otrosIngresosFiltro = null;

    private List<TipoMovimiento> itemsTipoMov = new ArrayList();

    /**
     * Constructor de la clase.
     */
    public MovimientoCuentaBancariaBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de movimientos de
     * cuenta bancaria.
     */
    @PostConstruct
    public void init() {
        try {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            cargarCombos();
            currentDate.format(dateFormatter);
            currentDateTime.format(formatter);
            if (cuentaId != null && cuentaId > 0) {
                FiltroCuentasBancarias cuentas = new FiltroCuentasBancarias();
                cuentas.setOrderBy(new String[]{"cbcPk"});
                cuentas.setAscending(new boolean[]{true});
                cuentas.setIncluirCampos(new String[]{
                    "cbcPk",
                    "cbcNumeroCuenta",
                    "cbcTitular",
                    "cbcSedeFk.sedPk",
                    "cbcSedeFk.sedTipo",
                    "cbcSedeFk.sedCodigo",
                    "cbcSedeFk.sedNombre",
                    "cbcSedeFk.sedVersion",
                    "cbcVersion"
                });
                cuentas.setCbcPk(cuentaId);
                cuentas.setCbcHabilitado(Boolean.TRUE);
                List<SgCuentasBancarias> listCuenta = cuentasRestClient.buscar(cuentas);
                if (!listCuenta.isEmpty()) {
                    cuenta = listCuenta.get(0);
                    obtenerTipoCuentas();

                }
                entidadEnEdicion.setMcbCuentaFK(cuenta);
                sede = StringUtils.replace(Etiquetas.getValue("sedeCuentaBancariax"), "[x]",
                        cuenta.getCbcSedeFk().getSedCodigoNombre());
                filtroMov.setMcbFiltroFecha(1);
                cambioFiltroFecha();
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Obtiene los tipos de cuenta de una cuenta bancaria
     *
     * @return List<SgRelCuentaTipoCuenta>
     */
    public void obtenerTipoCuentas() {
        try {
            FiltroRelCuentaTipoCuenta fil = new FiltroRelCuentaTipoCuenta();
            fil.setCuentaBancPk(cuenta.getCbcPk());
            fil.setIncluirCampos(new String[]{"relPk", "cbcTipoCuentaBacFk.tcbPk", "cbcTipoCuentaBacFk.tcbNombre", "cbcTipoCuentaBacFk.tcbVersion", "relVersion"});
            List<SgRelCuentaTipoCuenta> list = relCTipoCuentaRestClient.buscar(fil);
            if (list != null && !list.isEmpty()) {
                tiposCuentaBancaria = list.stream().map(l -> l.getCbcTipoCuentaBacFk().getTcbNombre()).collect(Collectors.joining(","));
            }
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Busca los registros de movimientos de cuenta bancaria según los filtros
     * utilizados.
     */
    public void buscar() {
        try {
            saldoInicial = BigDecimal.ZERO;
            filtroMov.setFirst(new Long(0));
            filtroMov.setOrderBy(new String[]{"mcbFecha", "mcbTipoMovimiento"});
            filtroMov.setAscending(new boolean[]{true, false});
            filtroMov.setMcbCuentaFK(cuenta.getCbcPk());
            if (filtroMov.getMcbFecha() != null) {
                //filtroMov.setMcbFecha(currentDate.withDayOfMonth(1).w);
                filtroMov.setMcbFechaDesde(currentDate.withDayOfMonth(1).atTime(LocalTime.MIN));
                filtroMov.setMcbFechaHasta(currentDate.withDayOfMonth(currentDate.lengthOfMonth()).atTime(LocalTime.MAX));
            }
            if (filtroMov.getMcbFechaDesdeS() != null) {
                filtroMov.setMcbFechaDesde(filtroMov.getMcbFechaDesdeS().atTime(LocalTime.MIN));
            }
            if (filtroMov.getMcbFechaHastaS() != null) {
                filtroMov.setMcbFechaHasta(filtroMov.getMcbFechaHastaS().atTime(LocalTime.MAX));
            }
            if (inicial != null) {
                if (inicial == 1) {
                    filtroMov.setMcbTipoMovimiento(TipoMovimiento.DEBE);
                } else if (inicial == 2) {
                    filtroMov.setMcbTipoMovimiento(TipoMovimiento.HABER);
                }
            }

            if (comboComponente.getSelectedT() != null) {
                filtroMov.setComponentePk(comboComponente.getSelectedT());
            }

            if (comboSubComponente.getSelectedT() != null) {
                filtroMov.setSubComponentePk(comboSubComponente.getSelectedT());
            }

            if (otrosIngresosFiltro != null) {
                if (otrosIngresosFiltro.equals(Boolean.TRUE)) {
                    filtroMov.setMovimientoOrigen(EnumMovimientosOrigen.P);
                } else if (otrosIngresosFiltro.equals(Boolean.FALSE)) {
                    filtroMov.setMovimientoOrigen(EnumMovimientosOrigen.T);
                }
            } else {
                filtroMov.setMovimientoOrigen(null);
            }

            filtroMov.setIncluirCampos(new String[]{
                "mcbPk",
                "mcbCuentaFK.cbcPk",
                "mcbCuentaFK.cbcVersion",
                "mcbFecha",
                "mcbDetalle",
                "mcbMonto",
                "mcbSaldo",
                "mcbTipoMovimiento",
                "mcbMovFuenteIngresosFk.movPk",
                "mcbMovFuenteIngresosFk.movNumMoviento",
                "mcbMovFuenteIngresosFk.movVersion",
                "mcbMovFuenteIngresosFk.movRubroPk.ruId",
                "mcbMovFuenteIngresosFk.movRubroPk.ruNombre",
                "mcbMovFuenteIngresosFk.movRubroPk.ruVersion",
                "mcbChequeCb",
                "mcbProveedor",
                "mcbPagoFk.pgsPk",
                "mcbPagoFk.pgsComentario",
                "mcbPagoFk.pgsFactura.facPk",
                "mcbPagoFk.pgsFactura.facTipoDocumento",
                "mcbPagoFk.pgsFactura.facItemPlanCompra.comPk",
                "mcbPagoFk.pgsFactura.facItemPlanCompra.comInsumoFk.insPk",
                "mcbPagoFk.pgsFactura.facItemPlanCompra.comInsumoFk.insNombre",
                "mcbPagoFk.pgsFactura.facItemPlanCompra.comInsumoFk.insVersion",
                "mcbPagoFk.pgsFactura.facItemPlanCompra.comVersion",
                "mcbPagoFk.pgsFactura.facNumero",
                "mcbPagoFk.pgsFactura.facVersion",
                "mcbPagoFk.pgsChequeraFk.chePk",
                "mcbPagoFk.pgsChequeraFk.cheSerie",
                "mcbPagoFk.pgsChequeraFk.cheVersion",
                "mcbPagoFk.pgsVersion",
                "mcbDesembolsoCedFk.dcePk",
                "mcbDesembolsoCedFk.dceVersion",
                "mcbEstadoLiquidacion",
                "mcbChequeCobrado",
                "mcbAplicaConciliacion",
                "mcbChequeAnulado",
                "mcbTransaccion",
                "mcbTipoRetiro",
                "mcbVersion",
                "movimientoLiquidacion.mlqPk",
                "movimientoLiquidacion.mlqMovimientoPk.mcbPk",
                "movimientoLiquidacion.mlqLiquidacionPk.liqPk",
                "movimientoLiquidacion.mlqVersion"
            });
            totalResultados = restClient.buscarTotal(filtroMov);
            listMovCuentaBancaria = restClient.buscar(filtroMov);
            if (!listMovCuentaBancaria.isEmpty()) {
                ingresosList = listMovCuentaBancaria.stream().filter(c -> c.getMcbTipoMovimiento().equals(TipoMovimiento.HABER)).collect(Collectors.toList());
                egresosList = listMovCuentaBancaria.stream().filter(c -> c.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE)).collect(Collectors.toList());
                totali = ingresosList.stream().filter(x -> x.getMcbMonto() != null).map(e -> e.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
                totale = egresosList.stream().filter(x -> x.getMcbMonto() != null).map(e -> e.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);

                for (SgMovimientoCuentaBancaria list : listMovCuentaBancaria) {
                    BigDecimal montoDebe = BigDecimal.ZERO;
                    BigDecimal montoHaber = BigDecimal.ZERO;
                    BigDecimal saldo = BigDecimal.ZERO;
                    if (list.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE)) {
                        montoDebe = list.getMcbMonto();
                        saldo = montoHaber.subtract(montoDebe);
                        saldoInicial = saldoInicial.add(saldo);
                    }
                    if (list.getMcbTipoMovimiento().equals(TipoMovimiento.HABER)) {
                        montoHaber = list.getMcbMonto();
                        saldo = montoHaber.subtract(montoDebe);
                        saldoInicial = saldoInicial.add(saldo);
                    }
                    list.setMcbSaldoFinal(saldoInicial);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de filtros.
     */
    public void cargarCombos() {
        try {

            FiltroCodiguera filtroComponente = new FiltroCodiguera();
            filtroComponente.setOrderBy(new String[]{"cpeNombre"});
            filtroComponente.setIncluirCampos(new String[]{"cpeVersion", "cpeNombre", "cpeCodigo"});
            comboComponente = new SofisComboG<>(componenteClient.buscar(filtroComponente), "cpeNombre");
            comboComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            if (estadosLiq.isEmpty()) {
                estadosLiq = new SofisComboG(Arrays.asList(EnumEvaluacionLiquidacion.values()), "text");
                estadosLiq.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el combo de subcomponente
     */
    public void cargarSubComponente() {
        try {
            if (comboComponente.getSelectedT() != null) {
                FiltroGesPresEs filtroSubComponente = new FiltroGesPresEs();
                filtroSubComponente.setOrderBy(new String[]{"gesNombre"});
                filtroSubComponente.setIncluirCampos(new String[]{"gesVersion", "gesNombre", "gesCod"});
                filtroSubComponente.setCpeId(comboComponente.getSelectedT().getCpeId());
                comboSubComponente = new SofisComboG<>(subComponenteClient.buscarSubcomponente(filtroSubComponente), "gesNombre");
                comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
        }
    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {
        comboComponente.setSelected(-1);
        comboSubComponente.setSelected(-1);
    }

    /**
     * Control de renderizado de elementos en la pantalla, dependiendo si es
     * Retiro o Abono
     */
    public void obtenerModoRetiro() {
        if (comboTipoMovimiento.getSelectedT().getText().equals("Egreso")) {
            esRetiro = true;
        } else {
            esRetiro = false;
        }
        if (comboTipoMovimiento.getSelectedT().getText().equals("Ingreso")) {
            esIngreso = true;
            cargarFuenteIngresos();
        } else {
            esIngreso = false;
        }
    }

    /**
     * Control de renderizado de elementos en la pantalla, para definir si el
     * tipo de Retiro
     */
    public void obtenerTipoRetiro() {
        if (comboTipoRetiro.getSelectedT().getText().equals(EnumTipoRetiroMovimientoCB.CHEQUE.getText())) {
            esCheque = true;
        } else {
            esCheque = false;
        }
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroCodiguera();
        filtroMov = new FiltroMovimientoCuentaBancaria();
        inicial = 0;
        limpiarCombos();
        buscar();
    }

    /**
     * Cargar combos dialog
     */
    public void combosDialog() {

        items = new ArrayList(Arrays.asList(EnumTipoRetiroMovimientoCB.values()));
        comboTipoRetiro = new SofisComboG(items, "text");
        comboTipoRetiro.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        itemsTipoMov = new ArrayList(Arrays.asList(TipoMovimiento.values()));
        comboTipoMovimiento = new SofisComboG(itemsTipoMov, "text");
        comboTipoMovimiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de movimiento.
     */
    public void agregar() {
        esRetiro = Boolean.FALSE;
        esIngreso = Boolean.FALSE;
        esCheque = Boolean.FALSE;
        JSFUtils.limpiarMensajesError();
        combosDialog();
        entidadEnEdicion = new SgMovimientoCuentaBancaria();
    }

    /**
     * Carga los datos del movimiento seleccionado para poder ser editados.
     *
     * @param var SgMovimientoCuentaBancaria: Elemento del grid seleccionado de
     * movimientos de cuenta bancaria.
     */
    public void actualizar(SgMovimientoCuentaBancaria var) {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = (SgMovimientoCuentaBancaria) SerializationUtils.clone(var);
        combosDialog();
        agregarTransaccion = Boolean.TRUE;
        if (entidadEnEdicion.getMcbTipoMovimiento() != null) {
            comboTipoMovimiento.setSelectedT(entidadEnEdicion.getMcbTipoMovimiento());
        }
    }

    /**
     * Crea o actualiza un registro de movimientos de cuenta bancaria.
     */
    public void guardar() {
        try {
            entidadEnEdicion.setMcbCuentaFK(cuenta);
            entidadEnEdicion.setMcbTipoMovimiento(comboTipoMovimiento.getSelectedT());
            entidadEnEdicion.setMcbTipoRetiro(comboTipoRetiro.getSelectedT());
            entidadEnEdicion.setMcbChequeCobrado(Boolean.FALSE);
            if (comboTipoMovimiento != null && entidadEnEdicion.getMcbTipoMovimiento().equals(tipomovimiento.HABER)) {
                entidadEnEdicion.setMcbMovFuenteIngresosFk(fuenteIngresoCombo.getSelectedT());
            }
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            agregarTransaccion = Boolean.TRUE;
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Crea o actualiza un registro de movimientos de cuenta bancaria.
     */
    public void evaluarMovLiquidacion() {
        try {
            SgMovimientoLiquidacion movLiq = entidadEnEdicion.getMovimientoLiquidacion();
            movLiq.setMlqComentario(comentLiquidacion);
            movLiq.setMlqEvaluado(Boolean.FALSE);
            movLiquidacionRestClient.guardar(movLiq);
            PrimeFaces.current().executeScript("PF('itemDialog2').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el filtro fecha para realizar las búsquedas de la siguiente forma:
     * filtroMov.getMccFiltroFecha().equals(1) : Carga los registros del mes
     * actual, sino, carga los registros según el rango de fecha establecido en
     * los filtros.
     */
    public void cambioFiltroFecha() {
        filtroMov.setMcbFecha(null);
        if (filtroMov.getMcbFiltroFecha() != null) {
            if (filtroMov.getMcbFiltroFecha().equals(1)) {
                filtroMov.setMcbFecha(currentDateTime);
                filtroMov.setMcbFechaDesdeS(null);
                filtroMov.setMcbFechaHastaS(null);
            } else {
                filtroMov.setMcbFechaDesdeS(currentDate);
                filtroMov.setMcbFechaHastaS(currentDate);
                filtroMov.setMcbFecha(null);
            }
        }
    }

    /**
     * Carga el filtro tipo movimiento para realizar las búsquedas de la
     * siguiente forma: inicial.equals(1) : Carga los registros del Débito sino,
     * carga los registros del Crédito
     */
    public void cambioFiltroMovimiento() {
        filtroMov.setMcbTipoMovimiento(null);
        if (inicial.equals(1)) {
            filtroMov.setMcbTipoMovimiento(TipoMovimiento.DEBE);
        }
        if (inicial.equals(2)) {
            filtroMov.setMcbTipoMovimiento(TipoMovimiento.HABER);
        } else {
            filtroMov.setMcbTipoMovimiento(null);
        }
        filtroMov.setMcbTipoMovimiento(null);
    }

    /**
     * Carga la columna "Debe" si el tipo de movimiento es debe
     *
     * @param mov SgMovimientoCuentaBancaria: es el movimiento que se carga en
     * el grid
     * @return BigDecimal 0 si es haber, sino, retorna el monto guardado
     */
    public BigDecimal cargarMontoDebe(SgMovimientoCuentaBancaria mov) {
        BigDecimal monto = BigDecimal.ZERO;
        if (mov != null) {
            if (mov.getMcbTipoMovimiento().getText().equals(tipomovimiento.DEBE.getText())) {
                monto = mov.getMcbMonto();
            }
        }
        return monto;
    }

    /**
     * Carga la columna "Haber" si el tipo de movimiento es haber
     *
     * @param mov SgMovimientoCuentaBancaria: es el movimiento que se carga en
     * el grid
     * @return BigDecimal 0 si es debe, sino, retorna el monto guardado
     */
    public BigDecimal cargarMontoHaber(SgMovimientoCuentaBancaria mov) {
        BigDecimal monto = BigDecimal.ZERO;
        if (mov != null) {
            if (mov.getMcbTipoMovimiento().getText().equals(tipomovimiento.HABER.getText())) {
                monto = mov.getMcbMonto();
            }
        }
        return monto;
    }

    /**
     * Elimina un registro de movimientos de cuenta bancaria.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getMcbPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga la columna "Cheque Cobrado"
     *
     * @param mov SgMovimientoCuentaBancaria: es el movimiento que se carga en
     * el grid
     */
    public void cambiarChequeCobrado(SgMovimientoCuentaBancaria mov) {
        Integer estado = cobrado ? 0 : 1;
        try {
            if (estado == 1 && mov.getMcbChequeCobrado() && mov.getMcbChequeCobrado() != null) {
                mov.setMcbChequeCobrado(Boolean.TRUE);
            } else {
                mov.setMcbChequeCobrado(Boolean.FALSE);
            }
            mov = restClient.cobrar(mov);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Muestra el historial del registro.
     *
     * @param id Long: id del registro del cual se quiere obtener el historial
     */
    public void historial(Long id) {
        try {
            historialMovimientoCuentaBancaria = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Cierre de dialog para guardar movimiento
     */
    public void cierreDialog() {
        if (agregarTransaccion.equals(Boolean.TRUE)) {
            agregarTransaccion = Boolean.FALSE;
        }
    }

    public Boolean validarColumnaChequeCobrado(SgMovimientoCuentaBancaria mov) {
        if (mov.getMcbTipoMovimiento() != null && mov.getMcbTipoRetiro() != null) {
            if (mov.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE) && mov.getMcbTipoRetiro().equals(EnumTipoRetiroMovimientoCB.CHEQUE)) {
//                if (mov.getMcbPagoFk() != null) {
                try {
                    FiltroPago pagos = new FiltroPago();
                    pagos.setPgsMovimientoCBFk(mov);
                    pagos.setIncluirCampos(new String[]{
                        "pgsPk",
                        "pgsFactura.facPk",
                        "pgsFactura.facEstado",
                        "pgsFactura.facVersion",
                        "pgsMovimientoCBFk.mcbPk",
                        "pgsMovimientoCBFk.mcbVersion",
                        "pgsVersion"
                    });
                    List<SgPago> listPago = pagoRestClient.buscar(pagos);
                    if (!listPago.isEmpty()) {
                        SgPago pago = listPago.get(0);
                        if (pago.getPgsFactura().getFacEstado().equals(EnumFacturaEstado.PAGADO)) {
                            return true;
                        }
                    }
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
                    JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
                }
//                }

            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Carga el combo de Fuente de Ingresos con Otros ingresos de presupuesto
     * Aprobado.
     */
    private void cargarFuenteIngresos() {
        try {
            FiltroPresupuestoEscolar pres = new FiltroPresupuestoEscolar();
            pres.setOrderBy(new String[]{"pesPk"});
            pres.setPesEstado(EnumPresupuestoEscolarEstado.APROBADO);
            pres.setPesHabilitado(Boolean.TRUE);
            pres.setIdSede(cuenta.getCbcSedeFk().getSedPk());
            pres.setIncluirCampos(new String[]{
                "pesPk",
                "pesNombre",
                "pesVersion",
                "pesEstado",
                "pesCodigo",
                "pesHabilitado",
                "pesSedeFk.sedPk",
                "pesSedeFk.sedTipo",
                "pesSedeFk.sedVersion",
                "pesVersion"
            });

            List<SgPresupuestoEscolar> listPres = presupuesotRestClient.buscar(pres);
            if (!listPres.isEmpty() && listPres.size() == 1) {
                listPres.get(0);
                FiltroMovimientos mov = new FiltroMovimientos();
                mov.setOrderBy(new String[]{"movPk"});
                mov.setIncluirCampos(new String[]{
                    "movPk",
                    "movTipo",
                    "movMonto",
                    "movNumMoviento",
                    "movOrigen",
                    "movPresupuestoFk.pesPk",
                    "movPresupuestoFk.pesNombre",
                    "movPresupuestoFk.pesVersion",
                    "movRubroPk.ruId",
                    "movRubroPk.ruNombre",
                    "movRubroPk.ruCodigo",
                    "movRubroPk.ruHabilitado",
                    "movRubroPk.ruUltmodFecha",
                    "movRubroPk.ruUltmodUsuario",
                    "movRubroPk.ruVersion",
                    "movFuenteRecursos",
                    "movVersion"
                });
                mov.setMovPresupuestoFk(listPres.get(0).getPesPk());
                mov.setAscending(new boolean[]{true});
                listMovPresupuesto = movimientoRestClient.buscar(mov);
                List<SgMovimientos> resultado
                        = listMovPresupuesto
                                .stream()
                                .filter(x -> x.getMovTipo().equals(EnumMovimientosTipo.I)
                                && x.getMovNumMoviento() != null
                                && x.getMovOrigen().equals(EnumMovimientosOrigen.P))
                                .collect(Collectors.toList());

                if (!resultado.isEmpty()) {
                    fuenteIngresoCombo = new SofisComboG(resultado, "movCodigoRubro");
                    fuenteIngresoCombo.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                }

            } else {
                fuenteIngresoCombo.addEmptyItem(Etiquetas.getValue("comboIngresosItem"));
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Cambia el estado movimiento liquidación
     *
     */
    public void cambiarEstadoMov() {
        try {
            if (listSelected != null && !listSelected.isEmpty()) {
                if (estadosLiq.getSelectedT() != null) {
                    if (estadosLiq.getSelectedT().equals(EnumEvaluacionLiquidacion.NO_CORRECTO) && listSelected.size() > 1) {
                        //MENSAJE DE QUE CON EL ESTADO DE EVALUADA SOLO SE PUEDE SELECCIONAR UN MOV A LA VEZ
                    } else {
                        for (SgMovimientoCuentaBancaria mov : listSelected) {
                            if (estadosLiq.getSelectedT().equals(EnumEvaluacionLiquidacion.NO_CORRECTO)) {
                                entidadEnEdicion = (SgMovimientoCuentaBancaria) SerializationUtils.clone(mov);
                                PrimeFaces.current().executeScript("PF('itemDialog2').show()");
                            } else {
                                SgMovimientoLiquidacion movLiq = mov.getMovimientoLiquidacion();
                                movLiq.setMlqEvaluado(Boolean.TRUE);
                                movLiquidacionRestClient.guardar(movLiq);
                            }
                        }
                    }
                } else {
                    //DEBE SELECCIONAR UN ESTADO
                }
            } else {
                //MENSAJE QUE DEBE SELECIONAR AL MENOS UNO
            }
        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarEdicionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Getter-Setter">
    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgMovimientoCuentaBancaria getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMovimientoCuentaBancaria entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgMovimientoCuentaBancaria> getHistorialMovimientoCuentaBancaria() {
        return historialMovimientoCuentaBancaria;
    }

    public void setHistorialMovimientoCuentaBancaria(List<SgMovimientoCuentaBancaria> historialMovimientoCuentaBancaria) {
        this.historialMovimientoCuentaBancaria = historialMovimientoCuentaBancaria;
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

    public LazyMovimientoCuentaBancariaDataModel getMovimientoCuentaBancariaLazyModel() {
        return movimientoCuentaBancariaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyMovimientoCuentaBancariaDataModel movimientoCuentaBancariaLazyModel) {
        this.movimientoCuentaBancariaLazyModel = movimientoCuentaBancariaLazyModel;
    }

    public TipoMovimiento getTipomovimiento() {
        return tipomovimiento;
    }

    public void setTipomovimiento(TipoMovimiento tipomovimiento) {
        this.tipomovimiento = tipomovimiento;
    }

    public List<SelectItem> getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(List<SelectItem> tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public SgCuentasBancarias getCuenta() {
        return cuenta;
    }

    public void setCuenta(SgCuentasBancarias cuenta) {
        this.cuenta = cuenta;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public FiltroMovimientoCuentaBancaria getFiltroMov() {
        return filtroMov;
    }

    public void setFiltroMov(FiltroMovimientoCuentaBancaria filtroMov) {
        this.filtroMov = filtroMov;
    }

    public Integer getInicial() {
        return inicial;
    }

    public void setInicial(Integer inicial) {
        this.inicial = inicial;
    }

    public List<SgPago> getPagos() {
        return pagos;
    }

    public void setPagos(List<SgPago> pagos) {
        this.pagos = pagos;
    }

    public SofisComboG<EnumTipoRetiroMovimientoCB> getComboTipoRetiro() {
        return comboTipoRetiro;
    }

    public void setComboTipoRetiro(SofisComboG<EnumTipoRetiroMovimientoCB> comboTipoRetiro) {
        this.comboTipoRetiro = comboTipoRetiro;
    }

    public List<EnumTipoRetiroMovimientoCB> getItems() {
        return items;
    }

    public void setItems(List<EnumTipoRetiroMovimientoCB> items) {
        this.items = items;
    }

    public EnumTipoRetiroMovimientoCB getRetiroCheque() {
        return retiroCheque;
    }

    public void setRetiroCheque(EnumTipoRetiroMovimientoCB retiroCheque) {
        this.retiroCheque = retiroCheque;
    }

    public boolean isEsRetiro() {
        return esRetiro;
    }

    public void setEsRetiro(boolean esRetiro) {
        this.esRetiro = esRetiro;
    }

    public SofisComboG<TipoMovimiento> getComboTipoMovimiento() {
        return comboTipoMovimiento;
    }

    public void setComboTipoMovimiento(SofisComboG<TipoMovimiento> comboTipoMovimiento) {
        this.comboTipoMovimiento = comboTipoMovimiento;
    }

    public List<TipoMovimiento> getItemsTipoMov() {
        return itemsTipoMov;
    }

    public void setItemsTipoMov(List<TipoMovimiento> itemsTipoMov) {
        this.itemsTipoMov = itemsTipoMov;
    }

    public boolean isEsCheque() {
        return esCheque;
    }

    public void setEsCheque(boolean esCheque) {
        this.esCheque = esCheque;
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

    public Boolean getCobrado() {
        return cobrado;
    }

    public void setCobrado(Boolean cobrado) {
        this.cobrado = cobrado;
    }

    public FiltroCuentasBancarias getFiltroBanco() {
        return filtroBanco;
    }

    public void setFiltroBanco(FiltroCuentasBancarias filtroBanco) {
        this.filtroBanco = filtroBanco;
    }

    public List<SgMovimientoCuentaBancaria> getListMovCuentaBancaria() {
        return listMovCuentaBancaria;
    }

    public void setListMovCuentaBancaria(List<SgMovimientoCuentaBancaria> listMovCuentaBancaria) {
        this.listMovCuentaBancaria = listMovCuentaBancaria;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public BigDecimal getTotali() {
        return totali;
    }

    public void setTotali(BigDecimal totali) {
        this.totali = totali;
    }

    public BigDecimal getTotale() {
        return totale;
    }

    public void setTotale(BigDecimal totale) {
        this.totale = totale;
    }

    public Boolean getEsIngreso() {
        return esIngreso;
    }

    public void setEsIngreso(Boolean esIngreso) {
        this.esIngreso = esIngreso;
    }

    public SofisComboG<SgMovimientos> getFuenteIngresoCombo() {
        return fuenteIngresoCombo;
    }

    public void setFuenteIngresoCombo(SofisComboG<SgMovimientos> fuenteIngresoCombo) {
        this.fuenteIngresoCombo = fuenteIngresoCombo;
    }

    public SofisComboG<SsGesPresEs> getComboSubComponente() {
        return comboSubComponente;
    }

    public void setComboSubComponente(SofisComboG<SsGesPresEs> comboSubComponente) {
        this.comboSubComponente = comboSubComponente;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public Boolean getAgregarTransaccion() {
        return agregarTransaccion;
    }

    public void setAgregarTransaccion(Boolean agregarTransaccion) {
        this.agregarTransaccion = agregarTransaccion;
    }

    public List<SgMovimientoCuentaBancaria> getListSelected() {
        return listSelected;
    }

    public void setListSelected(List<SgMovimientoCuentaBancaria> listSelected) {
        this.listSelected = listSelected;
    }

    public SofisComboG<EnumEvaluacionLiquidacion> getEstadosLiq() {
        return estadosLiq;
    }

    public void setEstadosLiq(SofisComboG<EnumEvaluacionLiquidacion> estadosLiq) {
        this.estadosLiq = estadosLiq;
    }

    public List<SgRelCuentaTipoCuenta> getTiposCuentas() {
        return tiposCuentas;
    }

    public void setTiposCuentas(List<SgRelCuentaTipoCuenta> tiposCuentas) {
        this.tiposCuentas = tiposCuentas;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getComentLiquidacion() {
        return comentLiquidacion;
    }

    public void setComentLiquidacion(String comentLiquidacion) {
        this.comentLiquidacion = comentLiquidacion;
    }

    public String getTiposCuentaBancaria() {
        return tiposCuentaBancaria;
    }

    public void setTiposCuentaBancaria(String tiposCuentaBancaria) {
        this.tiposCuentaBancaria = tiposCuentaBancaria;
    }

    public Boolean getOtrosIngresosFiltro() {
        return otrosIngresosFiltro;
    }

    public void setOtrosIngresosFiltro(Boolean otrosIngresosFiltro) {
        this.otrosIngresosFiltro = otrosIngresosFiltro;
    }

    // </editor-fold>
}
