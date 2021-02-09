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
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgCuentasBancariasDD;
import sv.gob.mined.siges.web.dto.SgMovimientoDireccionDepartamental;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoDireccionDepartamental;
import sv.gob.mined.siges.web.lazymodels.LazyMovimientoDireccionDepartamentalDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CuentasBancariasDDRestClient;
import sv.gob.mined.siges.web.restclient.MovimientoDireccionDepartamentalRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 * Bean correspondiente a los movimientos de la pagaduría de la pagaduría de la
 * direccion departamental.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MovimientoDireccionDepartamentalBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MovimientoDireccionDepartamentalBean.class.getName());

    @Inject
    private MovimientoDireccionDepartamentalRestClient restClient;

    @Inject
    private CuentasBancariasDDRestClient cuentasRestClient;

    @Inject
    @Param(name = "id")
    private Long cuentaId;
    private FiltroCodiguera filtro = new FiltroCodiguera();
    private FiltroMovimientoDireccionDepartamental filtroMov = new FiltroMovimientoDireccionDepartamental();
    private SgMovimientoDireccionDepartamental entidadEnEdicion = new SgMovimientoDireccionDepartamental();
    private List<SgMovimientoDireccionDepartamental> historialMovimientoCuentaBancaria = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyMovimientoDireccionDepartamentalDataModel movimientoLazyModel;
    //Enum de tipo de movimiento de cuenta
    private TipoMovimiento tipomovimiento;
    private List<SelectItem> tipoMovimiento = new ArrayList<>();
    private SgCuentasBancariasDD cuenta;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate currentDate = LocalDate.now();
    private LocalDateTime currentDateTime = LocalDateTime.now();
    private List<SgMovimientoDireccionDepartamental> listMovCuentasDepartamentales = new ArrayList<>();
    private BigDecimal totali = BigDecimal.ZERO;
    private BigDecimal totale = BigDecimal.ZERO;
    private List<SgMovimientoDireccionDepartamental> ingresosList = new ArrayList<>();
    private List<SgMovimientoDireccionDepartamental> egresosList = new ArrayList<>();
    private Locale locale;
    private boolean historial = false;
    private boolean agregarTransaccion = false;

    /**
     * Constructor de la clase.
     */
    public MovimientoDireccionDepartamentalBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de movimientos de
     * pagaduría.
     */
    @PostConstruct
    public void init() {
        try {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            cargarCombos();
            currentDate.format(dateFormatter);
            currentDateTime.format(formatter);
            if (cuentaId != null && cuentaId > 0) {
                cuenta = cuentasRestClient.obtenerPorId(cuentaId);
                entidadEnEdicion.setMddCuentaFK(cuenta);
                filtroMov.setMddFiltroFecha(1);
                cambioFiltroFecha();
                buscar();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca los registros de movimientos de pagaduría según los filtros
     * utilizados.
     */
    public void buscar() {
        try {
            filtroMov.setFirst(new Long(0));
            filtroMov.setOrderBy(new String[]{"mddFecha", "mddPk"});
            filtroMov.setAscending(new boolean[]{true, false});
            filtroMov.setMddCuentaFK(cuentaId);
            if (filtroMov.getMddFecha() != null) {
                //filtroMov.setMddFecha(currentDate.withDayOfMonth(1).w);
                filtroMov.setMddFechaDesde(currentDate.withDayOfMonth(1).atTime(LocalTime.MIN));
                filtroMov.setMddFechaHasta(currentDate.withDayOfMonth(currentDate.lengthOfMonth()).atTime(LocalTime.MAX));
            }
            if (filtroMov.getMddFechaDesdeS() != null) {
                filtroMov.setMddFechaDesde(filtroMov.getMddFechaDesdeS().atTime(LocalTime.MIN));
            }
            if (filtroMov.getMddFechaHastaS() != null) {
                filtroMov.setMddFechaHasta(filtroMov.getMddFechaHastaS().atTime(LocalTime.MAX));
            }

            totalResultados = restClient.buscarTotal(filtroMov);
            //movimientoLazyModel = new LazyMovimientoDireccionDepartamentalDataModel(restClient, filtroMov, totalResultados, paginado);
            listMovCuentasDepartamentales = restClient.buscar(filtroMov);

            if (!listMovCuentasDepartamentales.isEmpty()) {
                ingresosList = listMovCuentasDepartamentales.stream().filter(c -> c.getMddTipoMovimiento().equals(TipoMovimiento.HABER)).collect(Collectors.toList());
                egresosList = listMovCuentasDepartamentales.stream().filter(c -> c.getMddTipoMovimiento().equals(TipoMovimiento.DEBE)).collect(Collectors.toList());
                totali = ingresosList.stream().filter(x -> x.getMddMonto() != null).map(e -> e.getMddMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
                totale = egresosList.stream().filter(x -> x.getMddMonto() != null).map(e -> e.getMddMonto()).reduce(BigDecimal.ZERO, BigDecimal::add);
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
        Arrays.asList(TipoMovimiento.values()).forEach(e -> {
            tipoMovimiento.add(new SelectItem(e, e.getText()));
        });
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
        filtro = new FiltroCodiguera();
        filtroMov = new FiltroMovimientoDireccionDepartamental();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de movimiento.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgMovimientoDireccionDepartamental();
    }

    /**
     * Carga los datos del movimiento seleccionado para poder ser editados.
     *
     * @param var SgMovimientoDireccionDepartamental: Elemento del grid
     * seleccionado de movimientos de pagaduría.
     */
    public void actualizar(SgMovimientoDireccionDepartamental var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgMovimientoDireccionDepartamental) SerializationUtils.clone(var);
        historial = true;
        agregarTransaccion = true;
    }

    /**
     * Crea o actualiza un registro de movimientos de pagaduría.
     */
    public void guardar() {
        try {
            entidadEnEdicion.setMddCuentaFK(cuenta);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
            agregarTransaccion = false;
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
        filtroMov.setMddFecha(null);
        if (filtroMov.getMddFiltroFecha().equals(1)) {
            filtroMov.setMddFecha(currentDateTime);

            filtroMov.setMddFechaDesdeS(null);
            filtroMov.setMddFechaHastaS(null);
        } else {
            filtroMov.setMddFechaDesdeS(currentDate);
            filtroMov.setMddFechaHastaS(currentDate);

            filtroMov.setMddFecha(null);
        }
    }

    /**
     * Carga la columna "Debe" si el tipo de movimiento es debe
     *
     * @param mov SgMovimientoDireccionDepartamental: es el movimiento que se
     * carga en el grid
     * @return BigDecimal 0 si es haber, sino, retorna el monto guardado
     */
    public BigDecimal cargarMontoDebe(SgMovimientoDireccionDepartamental mov) {
        BigDecimal monto = BigDecimal.ZERO;
        if (mov != null) {
            if (mov.getMddTipoMovimiento().getText().equals(tipomovimiento.DEBE.getText())) {
                monto = mov.getMddMonto();
            }
        }
        return monto;
    }

    /**
     * Carga la columna "Haber" si el tipo de movimiento es haber
     *
     * @param mov SgMovimientoDireccionDepartamental: es el movimiento que se
     * carga en el grid
     * @return BigDecimal 0 si es debe, sino, retorna el monto guardado
     */
    public BigDecimal cargarMontoHaber(SgMovimientoDireccionDepartamental mov) {
        BigDecimal monto = BigDecimal.ZERO;
        if (mov != null) {
            if (mov.getMddTipoMovimiento().getText().equals(tipomovimiento.HABER.getText())) {
                monto = mov.getMddMonto();
            }
        }
        return monto;
    }

    /**
     * Elimina un registro de movimientos de pagaduría.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getMddPk());
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
     * Cambiar valores de historial y agregarTransaccion
     */
    public void banderasFalse() {
        historial = false;
        agregarTransaccion = false;
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

    public SgMovimientoDireccionDepartamental getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMovimientoDireccionDepartamental entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgMovimientoDireccionDepartamental> getHistorialMovimientoCuentaBancaria() {
        return historialMovimientoCuentaBancaria;
    }

    public void setHistorialMovimientoCuentaBancaria(List<SgMovimientoDireccionDepartamental> historialMovimientoCuentaBancaria) {
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

    public LazyMovimientoDireccionDepartamentalDataModel getMovimientoLazyModel() {
        return movimientoLazyModel;
    }

    public void setMovimientoLazyModel(LazyMovimientoDireccionDepartamentalDataModel movimientoLazyModel) {
        this.movimientoLazyModel = movimientoLazyModel;
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

    public SgCuentasBancariasDD getCuenta() {
        return cuenta;
    }

    public void setCuenta(SgCuentasBancariasDD cuenta) {
        this.cuenta = cuenta;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }

    public FiltroMovimientoDireccionDepartamental getFiltroMov() {
        return filtroMov;
    }

    public void setFiltroMov(FiltroMovimientoDireccionDepartamental filtroMov) {
        this.filtroMov = filtroMov;
    }

    public LocalDateTime getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(LocalDateTime currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public List<SgMovimientoDireccionDepartamental> getListMovCuentasDepartamentales() {
        return listMovCuentasDepartamentales;
    }

    public void setListMovCuentasDepartamentales(List<SgMovimientoDireccionDepartamental> listMovCuentasDepartamentales) {
        this.listMovCuentasDepartamentales = listMovCuentasDepartamentales;
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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public boolean getHistorial() {
        return historial;
    }

    public void setHistorial(boolean historial) {
        this.historial = historial;
    }

    // </editor-fold>
}
