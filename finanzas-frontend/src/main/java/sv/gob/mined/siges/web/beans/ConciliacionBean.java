/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgConciliacionesBancarias;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroConciliacionBancaria;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyConciliacionBancariaDataModel;
import sv.gob.mined.siges.web.lazymodels.LazyMovimientoCuentaBancariaDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ConciliacionBancariaRestClient;
import sv.gob.mined.siges.web.restclient.CuentasBancariasRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoCuentaBancariaRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de conciliación.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped

public class ConciliacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConciliacionBean.class.getName());

    @Inject
    private ConciliacionBancariaRestClient restClient;
    @Inject
    private SessionBean sessionBean;
    private LazyConciliacionBancariaDataModel conciliacionLazyModel;
    private Integer paginado = 10;
    private Long totalResultados;
    private Integer paginado2 = 10;
    private Long totalResultados2;
    private FiltroConciliacionBancaria filtro = new FiltroConciliacionBancaria();
    private SgConciliacionesBancarias entidadEnEdicion = new SgConciliacionesBancarias();
    private SgSede sedeSeleccionadaFiltro;
    private Locale locale;
    private SgSede sede;
    private SofisComboG<SgCuentasBancarias> comboCuentasBancarias = new SofisComboG<>();
    @Inject
    @Param(name = "id")
    private Long conciliacionId;
    @Inject
    private SedeRestClient sedeRestClient;
    @Inject
    private CuentasBancariasRestClient cuentasRestClient;
    @Inject
    private MovimientoCuentaBancariaRestClient movimientoCuentaBancariarestClient;

    private FiltroMovimientoCuentaBancaria filtroMov = new FiltroMovimientoCuentaBancaria();
    private FiltroMovimientoCuentaBancaria filtroSaldo = new FiltroMovimientoCuentaBancaria();
    private FiltroMovimientoCuentaBancaria filtroSaldoBanco = new FiltroMovimientoCuentaBancaria();
    private FiltroCuentasBancarias filtroBanco = new FiltroCuentasBancarias();
    private BigDecimal saldoBancario = BigDecimal.ZERO;
    private BigDecimal saldoPosible = BigDecimal.ZERO;
    private BigDecimal totalt = BigDecimal.ZERO;
    private LazyMovimientoCuentaBancariaDataModel movimientoCuentaBancariaLazyModel;
    private List<SgMovimientoCuentaBancaria> listaMovimientos = new ArrayList<>();

    /**
     * Constructor de la clase.
     */
    public ConciliacionBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Proveedores.
     */
    @PostConstruct
    public void init() {
        try {
            buscar();
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            if (conciliacionId != null && conciliacionId > 0) {
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroConciliacionBancaria();
        buscar();
    }

    /**
     * Busca los registros de Proveedores según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setIncluirCampos(new String[]{
                "conPk",
                "conCuentaFk.cbcPk",
                "conCuentaFk.cbcNumeroCuenta",
                "conCuentaFk.cbcVersion",
                "conCuentaFk.cbcBancoFk.bncPk",
                "conCuentaFk.cbcBancoFk.bncNombre",
                "conCuentaFk.cbcBancoFk.bncVersion",
                "conCuentaFk.cbcSedeFk.sedPk",
                "conCuentaFk.cbcSedeFk.sedCodigo",
                "conCuentaFk.cbcSedeFk.sedNombre",
                "conCuentaFk.cbcSedeFk.sedVersion",
                "conCuentaFk.cbcSedeFk.sedTipo",
                "conFechaDesde",
                "conFechaHasta",
                "conMonto",
                "conUltModFecha",
                "conVersion"
            });
            totalResultados = restClient.buscarTotal(filtro);
            conciliacionLazyModel = new LazyConciliacionBancariaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de chequera.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        sede = null;
        sedeSeleccionadaFiltro = null;
        entidadEnEdicion = new SgConciliacionesBancarias();
        comboCuentasBancarias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

    }

    /**
     * Crea o actualiza un registro de conciliación Bancaria.
     */
    public void guardar() {
        try {
            entidadEnEdicion.setConCuentaFk(comboCuentasBancarias.getSelectedT());
            entidadEnEdicion.setConFechaDesde(filtroMov.getMcbFechaDesdeS());
            entidadEnEdicion.setConFechaHasta(filtroMov.getMcbFechaHastaS());
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
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
     * Setea el valor de la Sede Selecccionada devuelve el objeto Sede.
     */
    public void setearSede() {
        JSFUtils.limpiarMensajesError();
        sede = sedeSeleccionadaFiltro;
        if (sede != null) {
            cargarComboCuentasBanc();
        }
    }

    /**
     * Se carga los combos de cuentas bancarias una vez selecciona la factura
     */
    public void cargarComboCuentasBanc() {
        try {
            FiltroCuentasBancarias filtroCb = new FiltroCuentasBancarias();
            comboCuentasBancarias = new SofisComboG<>();
            if (sede != null) {
                filtroCb.setCbcHabilitado(Boolean.TRUE);
                filtroCb.setCbcSedeFk(sede.getSedPk());
                filtroCb.setIncluirCampos(new String[]{
                    "cbcPk",
                    "cbcVersion",
                    "cbcNumeroCuenta",
                    "cbcTitular",
                    "cbcBancoFk.bncPk",
                    "cbcBancoFk.bncNombre"
                });
                comboCuentasBancarias = new SofisComboG<>(cuentasRestClient.buscar(filtroCb), "cuentaLabelCombo");
            }
            comboCuentasBancarias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca los registros de los cheques no cobrados para los filtros
     * seleccionados.
     */
    public void buscarCheques() {
        try {
            filtroMov.setFirst(new Long(0));

            filtroMov.setIncluirCampos(new String[]{
                "mcbPk",
                "mcbFecha",
                "mcbDetalle",
                "mcbPagoFk.pgsFactura.facProveedorFk.proId",
                "mcbPagoFk.pgsFactura.facProveedorFk.proNombre",
                "mcbPagoFk.pgsFactura.facProveedorFk.proVersion",
                "mcbChequeCb",
                "mcbMonto",
                "mcbChequeCobrado",
                "mcbTipoMovimiento",
                "mcbSaldo",
                "mcbCuentaFK.cbcPk",
                "mcbCuentaFK.cbcNumeroCuenta",
                "mcbCuentaFK.cbcVersion",
                "mcbAplicaConciliacion",
                "mcbChequeAnulado",
                "mcbVersion"

            });
            filtroMov.setOrderBy(new String[]{"mcbFecha", "mcbPk"});
            filtroMov.setAscending(new boolean[]{true, false});
            if (filtroMov.getMcbFechaDesdeS() != null) {
                filtroMov.setMcbFechaDesde(filtroMov.getMcbFechaDesdeS().atTime(LocalTime.MIN));
            }
            if (filtroMov.getMcbFechaHastaS() != null) {
                filtroMov.setMcbFechaHasta(filtroMov.getMcbFechaHastaS().atTime(LocalTime.MAX));
            }
            if (comboCuentasBancarias.getSelectedT() != null) {
                filtroMov.setMcbCuentaFK(comboCuentasBancarias.getSelectedT().getCbcPk());
//                saldoActual();
//                saldoBancarioActual();
            }

            filtroMov.setMcbChequeCobrado(Boolean.FALSE);
            filtroMov.setMcbAplicaConciliacion(Boolean.TRUE);
            filtroMov.setMcbTipoMovimiento(TipoMovimiento.DEBE);
            totalResultados2 = movimientoCuentaBancariarestClient.buscarTotal(filtroMov);
            listaMovimientos = movimientoCuentaBancariarestClient.buscar(filtroMov);
            if (!listaMovimientos.isEmpty()) {
                totalt = listaMovimientos.stream()
                        .filter(x -> x.getMcbMonto() != null)
                        .filter(x -> x.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE))
                        .filter(x -> x.getMcbChequeCobrado().equals(Boolean.FALSE))
                        .filter(x -> x.getMcbAplicaConciliacion().equals(Boolean.TRUE))
                        .map(e -> e.getMcbMonto())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public ConciliacionBancariaRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ConciliacionBancariaRestClient restClient) {
        this.restClient = restClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public LazyConciliacionBancariaDataModel getConciliacionLazyModel() {
        return conciliacionLazyModel;
    }

    public void setConciliacionLazyModel(LazyConciliacionBancariaDataModel conciliacionLazyModel) {
        this.conciliacionLazyModel = conciliacionLazyModel;
    }

    public Long getConciliacionId() {
        return conciliacionId;
    }

    public void setConciliacionId(Long conciliacionId) {
        this.conciliacionId = conciliacionId;
    }

    public SgConciliacionesBancarias getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgConciliacionesBancarias entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SgSede getSede() {
        return sede;
    }

    public void setSede(SgSede sede) {
        this.sede = sede;
    }

    public SofisComboG<SgCuentasBancarias> getComboCuentasBancarias() {
        return comboCuentasBancarias;
    }

    public void setComboCuentasBancarias(SofisComboG<SgCuentasBancarias> comboCuentasBancarias) {
        this.comboCuentasBancarias = comboCuentasBancarias;
    }

    public FiltroMovimientoCuentaBancaria getFiltroMov() {
        return filtroMov;
    }

    public void setFiltroMov(FiltroMovimientoCuentaBancaria filtroMov) {
        this.filtroMov = filtroMov;
    }

    public FiltroMovimientoCuentaBancaria getFiltroSaldo() {
        return filtroSaldo;
    }

    public void setFiltroSaldo(FiltroMovimientoCuentaBancaria filtroSaldo) {
        this.filtroSaldo = filtroSaldo;
    }

    public FiltroMovimientoCuentaBancaria getFiltroSaldoBanco() {
        return filtroSaldoBanco;
    }

    public void setFiltroSaldoBanco(FiltroMovimientoCuentaBancaria filtroSaldoBanco) {
        this.filtroSaldoBanco = filtroSaldoBanco;
    }

    public FiltroCuentasBancarias getFiltroBanco() {
        return filtroBanco;
    }

    public void setFiltroBanco(FiltroCuentasBancarias filtroBanco) {
        this.filtroBanco = filtroBanco;
    }

    public BigDecimal getSaldoBancario() {
        return saldoBancario;
    }

    public void setSaldoBancario(BigDecimal saldoBancario) {
        this.saldoBancario = saldoBancario;
    }

    public BigDecimal getSaldoPosible() {
        return saldoPosible;
    }

    public void setSaldoPosible(BigDecimal saldoPosible) {
        this.saldoPosible = saldoPosible;
    }

    public LazyMovimientoCuentaBancariaDataModel getMovimientoCuentaBancariaLazyModel() {
        return movimientoCuentaBancariaLazyModel;
    }

    public void setMovimientoCuentaBancariaLazyModel(LazyMovimientoCuentaBancariaDataModel movimientoCuentaBancariaLazyModel) {
        this.movimientoCuentaBancariaLazyModel = movimientoCuentaBancariaLazyModel;
    }

    public Integer getPaginado2() {
        return paginado2;
    }

    public void setPaginado2(Integer paginado2) {
        this.paginado2 = paginado2;
    }

    public Long getTotalResultados2() {
        return totalResultados2;
    }

    public void setTotalResultados2(Long totalResultados2) {
        this.totalResultados2 = totalResultados2;
    }

    public List<SgMovimientoCuentaBancaria> getListaMovimientos() {
        return listaMovimientos;
    }

    public void setListaMovimientos(List<SgMovimientoCuentaBancaria> listaMovimientos) {
        this.listaMovimientos = listaMovimientos;
    }

    public BigDecimal getTotalt() {
        return totalt;
    }

    public void setTotalt(BigDecimal totalt) {
        this.totalt = totalt;
    }

    public FiltroConciliacionBancaria getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroConciliacionBancaria filtro) {
        this.filtro = filtro;
    }

    // </editor-fold>
}
