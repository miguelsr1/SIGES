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
import sv.gob.mined.siges.web.dto.SgCajaChica;
import sv.gob.mined.siges.web.dto.SgChequera;
import sv.gob.mined.siges.web.dto.SgMovimientoCajaChica;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.EnumTipoMovimientoCajaChica;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCajaChica;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroChequera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroGesPresEs;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCajaChica;
import sv.gob.mined.siges.web.lazymodels.LazyMovimientoCajaChicaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CajaChicaRestClient;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;
import sv.gob.mined.siges.web.restclient.ChequeraRestClient;
import sv.gob.mined.siges.web.restclient.GesPresEsRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoCajaChicaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean correspondiente a los movimientos de la caja chica
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MovimientoCajaChicaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MovimientoCajaChicaBean.class.getName());

    @Inject
    private MovimientoCajaChicaRestClient restClient;

    @Inject
    private CajaChicaRestClient cuentasRestClient;

    @Inject
    private CategoriaPresupuestoEscolarRestClient componenteClient;

    @Inject
    private GesPresEsRestClient subComponenteClient;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private ChequeraRestClient chequeraRestClient;

    @Inject
    @Param(name = "id")
    private Long cuentaId;
    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroMovimientoCajaChica filtroMov = new FiltroMovimientoCajaChica();
    private SgMovimientoCajaChica entidadEnEdicion = new SgMovimientoCajaChica();
    private List<SgMovimientoCajaChica> historialMovimientoCuentaBancaria = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyMovimientoCajaChicaDataModel movimientoCajaChicaLazyModel;
    //Enum de tipo de movimiento de caja chica
    private TipoMovimiento tipomovimiento;
    private List<SelectItem> tipoMovimiento = new ArrayList<>();
    private SgCajaChica cuenta;
    private String sede = new String("");

    private SofisComboG<SsCategoriaPresupuestoEscolar> comboComponente = new SofisComboG<>();
    private SofisComboG<SsGesPresEs> comboSubComponente = new SofisComboG<>();

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate currentDate = LocalDate.now();
    private LocalDateTime currentDateTime = LocalDateTime.now();
    private Locale locale;
    private BigDecimal totali = BigDecimal.ZERO;
    private BigDecimal totale = BigDecimal.ZERO;
    private List<SgMovimientoCajaChica> listMovCajaChica = new ArrayList<>();
    private List<SgMovimientoCajaChica> ingresosList = new ArrayList<>();
    private List<SgMovimientoCajaChica> egresosList = new ArrayList<>();
    private BigDecimal saldoInicial = BigDecimal.ZERO;
    private Boolean esEgreso = Boolean.FALSE;
    private Boolean esIngreso = Boolean.FALSE;
    private SgChequera chequeraSeleccionada;

    /**
     * Constructor de la clase.
     */
    public MovimientoCajaChicaBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de movimientos de caja
     * chica.
     */
    @PostConstruct
    public void init() {
        try {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            cargarCombos();
            currentDate.format(dateFormatter);
            currentDateTime.format(formatter);
            if (cuentaId != null && cuentaId > 0) {
                FiltroCajaChica cuentas = new FiltroCajaChica();
                cuentas.setOrderBy(new String[]{"bccPk"});
                cuentas.setAscending(new boolean[]{true});
                cuentas.setIncluirCampos(new String[]{
                    "bccPk",
                    "bccNumeroCuenta",
                    "bccOtroIngreso",
                    "bccSubcomponenteFk.gesNombre",
                    "bccSubcomponenteFk.gesCategoriaComponente.cpeNombre",
                    "bccCuentaBancFk.cbcNumeroCuenta",
                    "bccCuentaBancFk.cbcBancoFk.bncNombre",
                    "bccTitular",
                    "bccOtroIngreso",
                    "bccSedeFk.sedPk",
                    "bccSedeFk.sedTipo",
                    "bccSedeFk.sedCodigo",
                    "bccSedeFk.sedNombre",
                    "bccSedeFk.sedVersion",
                    "bccVersion"
                });
                cuentas.setBccPk(cuentaId);
                List<SgCajaChica> listCuenta = cuentasRestClient.buscar(cuentas);
                if (!listCuenta.isEmpty()) {
                    cuenta = listCuenta.get(0);
                }
                entidadEnEdicion.setMccCuentaFK(cuenta);
                sede = StringUtils.replace(Etiquetas.getValue("sedeCuentaBancariax"), "[x]",
                        cuenta.getBccSedeFk().getSedCodigoNombre());

                filtroMov.setMccFiltroFecha(1);
                cambioFiltroFecha();
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca los registros de movimientos de la caja chica según los filtros
     * utilizados.
     */
    public void buscar() {
        try {
            filtroMov.setFirst(new Long(0));
            filtroMov.setOrderBy(new String[]{"mccFecha", "mccTipoMovimiento"});
            filtroMov.setAscending(new boolean[]{true, false});
            filtroMov.setMccCuentaFK(cuentaId);
            filtroMov.setIncluirCampos(new String[]{
                "mccPk",
                "mccCuentaFK.bccPk",
                "mccCuentaFK.bccNumeroCuenta",
                "mccCuentaFK.bccVersion",
                "mccFecha",
                "mccMonto",
                "mccSaldo",
                "mccDetalle",
                "mccCheque",
                "mccTipoMovimiento",
                "mccPagoFk.pgsPk",
                "mccPagoFk.pgsMovimientoCBFk.mcbPk",
                "mccPagoFk.pgsMovimientoCBFk.mcbVersion",
                "mccPagoFk.pgsFactura.facPk",
                "mccPagoFk.pgsFactura.facTipoDocumento",
                "mccPagoFk.pgsFactura.facItemPlanCompra.comPk",
                "mccPagoFk.pgsFactura.facItemPlanCompra.comInsumoFk.insPk",
                "mccPagoFk.pgsFactura.facItemPlanCompra.comInsumoFk.insNombre",
                "mccPagoFk.pgsFactura.facItemPlanCompra.comInsumoFk.insVersion",
                "mccPagoFk.pgsFactura.facItemPlanCompra.comVersion",
                "mccPagoFk.pgsFactura.facNumero",
                "mccPagoFk.pgsFactura.facVersion",
                "mccPagoFk.pgsFactura.facProveedorFk.proId",
                "mccPagoFk.pgsFactura.facProveedorFk.proNombre",
                "mccPagoFk.pgsFactura.facProveedorFk.proVersion",
                "mccPagoFk.pgsComentario",
                "mccPagoFk.pgsVersion",
                "mccChequeraFK.chePk",
                "mccChequeraFK.cheSerie",
                "mccChequeraFK.cheVersion",
                "mccVersion"

            });
            if (filtroMov.getMccFecha() != null) {
                filtroMov.setMccFechaDesde(currentDate.withDayOfMonth(1).atTime(LocalTime.MIN));
                filtroMov.setMccFechaHasta(currentDate.withDayOfMonth(currentDate.lengthOfMonth()).atTime(LocalTime.MAX));
            }
            if (filtroMov.getMccFechaDesdeS() != null) {
                filtroMov.setMccFechaDesde(filtroMov.getMccFechaDesdeS().atTime(LocalTime.MIN));
            }
            if (filtroMov.getMccFechaHastaS() != null) {
                filtroMov.setMccFechaHasta(filtroMov.getMccFechaHastaS().atTime(LocalTime.MAX));
            }
            if (comboComponente.getSelectedT() != null) {

                filtroMov.setComponentePk(comboComponente.getSelectedT());
            }
            if (comboSubComponente.getSelectedT() != null) {
                filtroMov.setSubComponentePk(comboSubComponente.getSelectedT());
            }
            totalResultados = restClient.buscarTotal(filtroMov);
            listMovCajaChica = restClient.buscar(filtroMov);
            //movimientoCajaChicaLazyModel = new LazyMovimientoCajaChicaDataModel(restClient, filtroMov, totalResultados, paginado);
            if (!listMovCajaChica.isEmpty()) {
                ingresosList = listMovCajaChica.stream().filter(c -> c.getMccTipoMovimiento().equals(TipoMovimiento.HABER)).collect(Collectors.toList());
                egresosList = listMovCajaChica.stream().filter(c -> c.getMccTipoMovimiento().equals(TipoMovimiento.DEBE)).collect(Collectors.toList());
                totali = ingresosList.stream().filter(x -> x.getMccMonto() != null).map(e -> e.getMccMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
                totale = egresosList.stream().filter(x -> x.getMccMonto() != null).map(e -> e.getMccMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
                for (SgMovimientoCajaChica list : listMovCajaChica) {
                    BigDecimal montoDebe = BigDecimal.ZERO;
                    BigDecimal montoHaber = BigDecimal.ZERO;
                    BigDecimal saldo = BigDecimal.ZERO;
                    if (list.getMccTipoMovimiento().equals(TipoMovimiento.DEBE)) {
                        montoDebe = list.getMccMonto();
                        saldo = montoHaber.subtract(montoDebe);
                        saldoInicial = saldoInicial.add(saldo);
                    }
                    if (list.getMccTipoMovimiento().equals(TipoMovimiento.HABER)) {
                        montoHaber = list.getMccMonto();
                        saldo = montoHaber.subtract(montoDebe);
                        saldoInicial = saldoInicial.add(saldo);
                    }
                    list.setMccSaldoFinal(saldoInicial);
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
            filtroComponente.setIncluirCampos(new String[]{"cpeId", "cpeVersion", "cpeNombre", "cpeCodigo"});
            comboComponente = new SofisComboG<>(componenteClient.buscar(filtroComponente), "cpeNombre");
            comboComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            comboSubComponente.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
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
                filtroSubComponente.setIncluirCampos(new String[]{"gesId", "gesVersion", "gesNombre", "gesCod"});
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

    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        JSFUtils.limpiarMensajesError();
        filtro = new FiltroCodiguera();
        filtroMov = new FiltroMovimientoCajaChica();
        comboComponente.setSelected(-1);
        comboSubComponente.setSelected(-1);
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de movimiento.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgMovimientoCajaChica();
        if (tipoMovimiento.isEmpty()) {
            Arrays.asList(TipoMovimiento.values()).forEach(e -> {
                tipoMovimiento.add(new SelectItem(e, e.getText()));
            });
        }
    }

    /**
     * Carga los datos del movimiento seleccionado para poder ser editados.
     *
     * @param var SgMovimientoCajaChica: Elemento del grid seleccionado de
     * movimientos de la caja chica.
     */
    public void actualizar(SgMovimientoCajaChica var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgMovimientoCajaChica) SerializationUtils.clone(var);
        if (tipoMovimiento.isEmpty()) {
            Arrays.asList(TipoMovimiento.values()).forEach(e -> {
                tipoMovimiento.add(new SelectItem(e, e.getText()));
            });
        }
    }

    /**
     * Crea o actualiza un registro de movimientos de la caja chica.
     */
    public void guardar() {
        try {
            entidadEnEdicion.setMccCuentaFK(cuenta);
            entidadEnEdicion.setMccTipoMovimiento(tipomovimiento);
            entidadEnEdicion.setMccChequeraFK(chequeraSeleccionada);
            if (entidadEnEdicion.getMccTipoMovimiento().equals(TipoMovimiento.HABER)) {
                entidadEnEdicion.setMccTipoIngreso(EnumTipoMovimientoCajaChica.MANUAL);
            }
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
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
     * Carga la columna "Debe" si el tipo de movimiento es debe
     *
     * @param mov SgMovimientoCajaChica: es el movimiento que se carga en el
     * grid
     * @return BigDecimal 0 si es haber, sino, retorna el monto guardado
     */
    public BigDecimal cargarMontoDebe(SgMovimientoCajaChica mov) {
        BigDecimal monto = BigDecimal.ZERO;
        if (mov != null) {
            if (mov.getMccTipoMovimiento().getText().equals(tipomovimiento.DEBE.getText())) {
                monto = mov.getMccMonto();
            }
        }
        return monto;
    }

    /**
     * Carga la columna "Haber" si el tipo de movimiento es haber
     *
     * @param mov: es el movimiento que se carga en el grid
     * @return BigDecimal 0 si es debe, sino, retorna el monto guardado
     */
    public BigDecimal cargarMontoHaber(SgMovimientoCajaChica mov) {
        BigDecimal monto = BigDecimal.ZERO;
        if (mov != null) {
            if (mov.getMccTipoMovimiento().getText().equals(tipomovimiento.HABER.getText())) {
                monto = mov.getMccMonto();
            }
        }
        return monto;
    }

    /**
     * Carga el filtro fecha para realizar las búsquedas de la siguiente forma:
     * filtroMov.getMccFiltroFecha().equals(1) : Carga los registros del mes
     * actual, sino, carga los registros según el rango de fecha establecido en
     * los filtros.
     */
    public void cambioFiltroFecha() {
        filtroMov.setMccFecha(null);
        if (filtroMov.getMccFiltroFecha().equals(1)) {
            filtroMov.setMccFecha(currentDateTime);

            filtroMov.setMccFechaDesdeS(null);
            filtroMov.setMccFechaHastaS(null);
        } else {
            filtroMov.setMccFechaDesdeS(currentDate);
            filtroMov.setMccFechaHastaS(currentDate);

            filtroMov.setMccFecha(null);
        }
    }

    /**
     * Elimina un registro de movimientos de la caja chica.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getMccPk());
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
     * Control de renderizado de elementos en la pantalla, dependiendo si es
     * Retiro o Abono
     */
    public void obtenerTipoMovimiento() {
        if (tipomovimiento.getText().equals("Egreso")) {
            esEgreso = Boolean.TRUE;
        } else {
            esEgreso = Boolean.FALSE;
        }
        if (tipomovimiento.getText().equals("Ingreso")) {
            esIngreso = Boolean.TRUE;
        } else {
            esIngreso = Boolean.FALSE;
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

            LOGGER.log(Level.SEVERE, "La sede es -->" + cuenta.getBccSedeFk().getSedPk());

            FiltroChequera chequera = new FiltroChequera();
            chequera.setCheSerieComplete(query);
            chequera.setSede(cuenta.getBccSedeFk().getSedPk());
            chequera.setOrderBy(new String[]{"cheSerie"});
            chequera.setAscending(new boolean[]{false});
            chequera.setIncluirCampos(new String[]{
                "chePk",
                "cheSerie",
                "cheSedeFk.sedPk",
                "cheSedeFk.sedTipo",
                "cheSedeFk.sedVersion",
                "cheNumeroInicial",
                "cheNumeroFinal",
                "cheVersion"});
            return chequeraRestClient.buscar(chequera);
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
    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgMovimientoCajaChica getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMovimientoCajaChica entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgMovimientoCajaChica> getHistorialMovimientoCuentaBancaria() {
        return historialMovimientoCuentaBancaria;
    }

    public void setHistorialMovimientoCuentaBancaria(List<SgMovimientoCajaChica> historialMovimientoCuentaBancaria) {
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

    public LazyMovimientoCajaChicaDataModel getMovimientoCajaChicaLazyModel() {
        return movimientoCajaChicaLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyMovimientoCajaChicaDataModel movimientoCuentaBancariaLazyModel) {
        this.movimientoCajaChicaLazyModel = movimientoCuentaBancariaLazyModel;
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

    public SgCajaChica getCuenta() {
        return cuenta;
    }

    public void setCuenta(SgCajaChica cuenta) {
        this.cuenta = cuenta;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public FiltroMovimientoCajaChica getFiltroMov() {
        return filtroMov;
    }

    public void setFiltroMov(FiltroMovimientoCajaChica filtroMov) {
        this.filtroMov = filtroMov;
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

    public List<SgMovimientoCajaChica> getListMovCajaChica() {
        return listMovCajaChica;
    }

    public void setListMovCajaChica(List<SgMovimientoCajaChica> listMovCajaChica) {
        this.listMovCajaChica = listMovCajaChica;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public Boolean getEsEgreso() {
        return esEgreso;
    }

    public void setEsEgreso(Boolean esEgreso) {
        this.esEgreso = esEgreso;
    }

    public SgChequera getChequeraSeleccionada() {
        return chequeraSeleccionada;
    }

    public void setChequeraSeleccionada(SgChequera chequeraSeleccionada) {
        this.chequeraSeleccionada = chequeraSeleccionada;
    }

    public Boolean getEsIngreso() {
        return esIngreso;
    }

    public void setEsIngreso(Boolean esIngreso) {
        this.esIngreso = esIngreso;
    }
    // </editor-fold>
}
