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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgConciliacionesBancarias;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumTipoRetiroMovimientoCB;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoCuentaBancaria;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
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
 * Bean para la gestión de Conciliación Bancaria.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped

public class ConciliacionBancariaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConciliacionBancariaBean.class.getName());

    @Inject
    private MovimientoCuentaBancariaRestClient movimientoCuentaBancariarestClient;
    @Inject
    private SessionBean sessionBean;
    @Inject
    private SedeRestClient sedeRestClient;
    @Inject
    private CuentasBancariasRestClient cuentasRestClient;

    @Inject
    private ConciliacionBancariaRestClient restClient;

    private LazyMovimientoCuentaBancariaDataModel movimientoCuentaBancariaLazyModel;
    private FiltroMovimientoCuentaBancaria filtroMov = new FiltroMovimientoCuentaBancaria();
    private FiltroMovimientoCuentaBancaria filtroSaldo = new FiltroMovimientoCuentaBancaria();
    private FiltroMovimientoCuentaBancaria filtroSaldoBanco = new FiltroMovimientoCuentaBancaria();
    private FiltroCuentasBancarias filtroBanco = new FiltroCuentasBancarias();
    private SofisComboG<SgCuentasBancarias> comboCuentasBancarias = new SofisComboG<>();
    private SgConciliacionesBancarias entidadEnEdicion = new SgConciliacionesBancarias();
    private SgSede sede;
    private SgSede sedeSeleccionadaFiltro;
    private BigDecimal saldoBancario = BigDecimal.ZERO;
    private BigDecimal saldoPosible = BigDecimal.ZERO;
    private BigDecimal totalt = BigDecimal.ZERO;
    private Integer paginado = 10;
    private Long totalResultados;

    private Locale locale;

    /**
     * Constructor de la clase.
     */
    public ConciliacionBancariaBean() {
    }

    /**
     * Método para incializar filtroMovs, combos y búsqueda de Proveedores.
     */
    @PostConstruct
    public void init() {
        try {
            comboCuentasBancarias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtroBanco = new FiltroCuentasBancarias();
        filtroMov = new FiltroMovimientoCuentaBancaria();
        filtroSaldo = new FiltroMovimientoCuentaBancaria();
        filtroSaldoBanco = new FiltroMovimientoCuentaBancaria();
        comboCuentasBancarias = new SofisComboG<>();
        sedeSeleccionadaFiltro = null;
        entidadEnEdicion = new SgConciliacionesBancarias();
        saldoPosible = BigDecimal.ZERO;
        sede = null;
        buscar();
    }

    /**
     * Setea el valor de la Sede Selecccionada devuelve el objeto Sede.
     */
    public void setearSede() {
        JSFUtils.limpiarMensajesError();
        sede = sedeSeleccionadaFiltro;
        if (sede != null) {
            cargarComboCuentaBancaria();
        }
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
     * Carga el combo de Cuenta Bancaria.
     */
    public void cargarComboCuentaBancaria() {
        try {

            if (sede != null) {
                FiltroCuentasBancarias filtroBanco = new FiltroCuentasBancarias();
                filtroBanco.setCbcHabilitado(Boolean.TRUE);
                filtroBanco.setCbcSedeFk(sedeSeleccionadaFiltro.getSedPk());
                filtroBanco.setIncluirCampos(new String[]{"cbcVersion", "cbcNumeroCuenta", "cbcBancoFk.bncPk", "cbcBancoFk.bncNombre", "cbcPk", "cbcSedeFk.sedPk", "cbcSedeFk.sedTipo", "cbcSedeFk.sedVersion"});
                filtroBanco.setOrderBy(new String[]{"cbcNumeroCuenta"});
                comboCuentasBancarias = new SofisComboG<>(cuentasRestClient.buscar(filtroBanco), "cuentaLabelCombo");
                comboCuentasBancarias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            }

        } catch (Exception ex) {
            Logger.getLogger(PresupuestoEscolarBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Carga el combo de Sede para el filtro de pantalla.
     *
     * @param query String: Palabra de filtro digitada en el combo
     * @return List<SgSede>
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
     * Busca los registros de Proveedores según los filtros utilizados.
     */
    public void buscar() {
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
                "mcbTipoRetiro",
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
                filtroMov.setMcbChequeCobrado(Boolean.FALSE);
                filtroMov.setMcbAplicaConciliacion(Boolean.TRUE);
                filtroMov.setMcbTipoMovimiento(TipoMovimiento.DEBE);
                filtroMov.setMcbTipoRetiro(EnumTipoRetiroMovimientoCB.CHEQUE);
                saldoActual();
                saldoBancarioActual();
                totalResultados = movimientoCuentaBancariarestClient.buscarTotal(filtroMov);
                movimientoCuentaBancariaLazyModel = new LazyMovimientoCuentaBancariaDataModel(movimientoCuentaBancariarestClient, filtroMov, totalResultados, paginado);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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
            if (mov.getMcbTipoMovimiento().getText().equals(TipoMovimiento.DEBE.getText())) {
                monto = mov.getMcbMonto();
            }
        }
        return monto;
    }

    /**
     * Carga el Saldo Actual con los cheques pendientes de pago.
     *
     * Se determina en base a la suma de los cheques pendiente de pago en la
     * cuenta.
     */
    public void saldoActual() {
        try {

            filtroSaldo.setFirst(new Long(0));
            filtroSaldo.setIncluirCampos(new String[]{
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
                "mcbTipoRetiro",
                "mcbSaldo",
                "mcbCuentaFK.cbcPk",
                "mcbCuentaFK.cbcNumeroCuenta",
                "mcbCuentaFK.cbcVersion",
                "mcbAplicaConciliacion",
                "mcbChequeAnulado",
                "mcbVersion"
            });
            filtroSaldo.setOrderBy(new String[]{"mcbFecha", "mcbPk"});
            filtroSaldo.setMcbChequeCobrado(Boolean.FALSE);
            filtroSaldo.setAscending(new boolean[]{true, false});
            if (comboCuentasBancarias.getSelectedT() != null) {
                filtroSaldo.setMcbCuentaFK(comboCuentasBancarias.getSelectedT().getCbcPk());
            }
            if (filtroMov.getMcbFechaDesdeS() != null) {
                filtroSaldo.setMcbFechaDesde(filtroMov.getMcbFechaDesdeS().atTime(LocalTime.MIN));
            }
            if (filtroMov.getMcbFechaHastaS() != null) {
                filtroSaldo.setMcbFechaHasta(filtroMov.getMcbFechaHastaS().atTime(LocalTime.MAX));
            }
            List<SgMovimientoCuentaBancaria> resultado = movimientoCuentaBancariarestClient.buscar(filtroSaldo);
            totalt = resultado.stream()
                    .filter(x -> x.getMcbMonto() != null)
                    .filter(x -> x.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE))
                    .filter(x -> x.getMcbChequeCobrado().equals(Boolean.FALSE))
                    .filter(x -> x.getMcbTipoRetiro().equals(EnumTipoRetiroMovimientoCB.CHEQUE))
                    .filter(x -> x.getMcbAplicaConciliacion().equals(Boolean.TRUE))
                    .map(e -> e.getMcbMonto())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga el Saldo Segun Banco Actual
     *
     * Se determina en base a los movimientos de la cuenta bancaria.
     */
    public void saldoBancarioActual() {
        try {

            filtroSaldoBanco.setFirst(new Long(0));
            filtroSaldoBanco.setIncluirCampos(new String[]{
                "mcbPk",
                "mcbFecha",
                "mcbMonto",
                "mcbCuentaFK.cbcPk",
                "mcbCuentaFK.cbcNumeroCuenta",
                "mcbCuentaFK.cbcVersion",
                "mcbTipoMovimiento",
                "mcbSaldo",
                "mcbVersion"
            });
            filtroSaldoBanco.setOrderBy(new String[]{"mcbFecha", "mcbPk"});
            filtroSaldoBanco.setAscending(new boolean[]{true, false});
            if (comboCuentasBancarias.getSelectedT() != null) {
                filtroSaldoBanco.setMcbCuentaFK(comboCuentasBancarias.getSelectedT().getCbcPk());
            }

            List<SgMovimientoCuentaBancaria> saldoBancoResult = movimientoCuentaBancariarestClient.buscar(filtroSaldoBanco);
            BigDecimal montoDebe = BigDecimal.ZERO;
            BigDecimal montoHaber = BigDecimal.ZERO;
            if (!saldoBancoResult.isEmpty()) {
                List<SgMovimientoCuentaBancaria> debe = saldoBancoResult.stream().filter(m -> m.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE)).collect(Collectors.toList());
                List<SgMovimientoCuentaBancaria> haber = saldoBancoResult.stream().filter(m -> m.getMcbTipoMovimiento().equals(TipoMovimiento.HABER)).collect(Collectors.toList());
                if (!debe.isEmpty()) {
                    montoDebe = debe.stream().map(m -> m.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
                }
                if (!haber.isEmpty()) {
                    montoHaber = haber.stream().map(m -> m.getMcbMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
                }
            }

            saldoBancario = (montoHaber.subtract(montoDebe));

            // saldoPosible = saldoBancario.subtract(totalt);
            saldoPosible = saldoBancario;

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

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public LazyMovimientoCuentaBancariaDataModel getMovimientoCuentaBancariaLazyModel() {
        return movimientoCuentaBancariaLazyModel;
    }

    public void setMovimientoCuentaBancariaLazyModel(LazyMovimientoCuentaBancariaDataModel movimientoCuentaBancariaLazyModel) {
        this.movimientoCuentaBancariaLazyModel = movimientoCuentaBancariaLazyModel;
    }

    public FiltroCuentasBancarias getFiltroBanco() {
        return filtroBanco;
    }

    public void setFiltroBanco(FiltroCuentasBancarias filtroBanco) {
        this.filtroBanco = filtroBanco;
    }

    public FiltroMovimientoCuentaBancaria getFiltroMov() {
        return filtroMov;
    }

    public void setFiltroMov(FiltroMovimientoCuentaBancaria filtroMov) {
        this.filtroMov = filtroMov;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public SofisComboG<SgCuentasBancarias> getComboCuentasBancarias() {
        return comboCuentasBancarias;
    }

    public void setComboCuentasBancarias(SofisComboG<SgCuentasBancarias> comboCuentasBancarias) {
        this.comboCuentasBancarias = comboCuentasBancarias;
    }

    public SgSede getSede() {
        return sede;
    }

    public void setSede(SgSede sede) {
        this.sede = sede;
    }

    public BigDecimal getSaldoBancario() {
        return saldoBancario;
    }

    public void setSaldoBancario(BigDecimal saldoBancario) {
        this.saldoBancario = saldoBancario;
    }

    public FiltroMovimientoCuentaBancaria getFiltroSaldo() {
        return filtroSaldo;
    }

    public void setFiltroSaldo(FiltroMovimientoCuentaBancaria filtroSaldo) {
        this.filtroSaldo = filtroSaldo;
    }

    public BigDecimal getTotalt() {
        return totalt;
    }

    public void setTotalt(BigDecimal totalt) {
        this.totalt = totalt;
    }

    public FiltroMovimientoCuentaBancaria getFiltroSaldoBanco() {
        return filtroSaldoBanco;
    }

    public void setFiltroSaldoBanco(FiltroMovimientoCuentaBancaria filtroSaldoBanco) {
        this.filtroSaldoBanco = filtroSaldoBanco;
    }

    public BigDecimal getSaldoPosible() {
        return saldoPosible;
    }

    public void setSaldoPosible(BigDecimal saldoPosible) {
        this.saldoPosible = saldoPosible;
    }

    public SgConciliacionesBancarias getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgConciliacionesBancarias entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    // </editor-fold>
}
