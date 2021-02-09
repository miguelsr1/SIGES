/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
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
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgChequera;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;
import sv.gob.mined.siges.web.dto.SgPago;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroChequera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCuentasBancarias;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPago;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.lazymodels.LazyChequeraDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ChequeraRestClient;
import sv.gob.mined.siges.web.restclient.CuentasBancariasRestClient;
import sv.gob.mined.siges.web.restclient.PagoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 * Bean para la gestión de las chequeras.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ChequeraBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ChequeraBean.class.getName());

    @Inject
    private ChequeraRestClient restClient;
    @Inject
    private SessionBean sessionBean;
    @Inject
    private SedeRestClient sedeRestClient;
    @Inject
    private PagoRestClient pagosRestClient;
    @Inject
    private CuentasBancariasRestClient cuentasRestClient;
    private LazyChequeraDataModel chqueraLazyModel;
    private Integer paginado = 10;
    private Long totalResultados;
    private FiltroChequera filtro = new FiltroChequera();
    private SgChequera entidadEnEdicion = new SgChequera();
    private Locale locale;
    private String titulo = "";
    private SgSede sedeSeleccionada;
    private SgSede sede;
    private SofisComboG<SgCuentasBancarias> comboCuentasBancarias = new SofisComboG<>();
    private SofisComboG<SgCuentasBancarias> comboCuentasBancariasFiltro = new SofisComboG<>();
    private FiltroSedes filtroSede = new FiltroSedes();
    private SgSede sedeSeleccionadaFiltro;
    private Boolean actualizar = Boolean.FALSE;
    private List<SgPago> pagos = new ArrayList();
    private String banco = new String();
    private String titular = new String();
    private Boolean verDatosCuenta = Boolean.FALSE;

    @Inject
    @Param(name = "id")
    private Long chequeraId;

    /**
     * Constructor de la clase.
     */
    public ChequeraBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Proveedores.
     */
    @PostConstruct
    public void init() {
        try {
            buscar();
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            if (chequeraId != null && chequeraId > 0) {
                buscar();
                cargarComboCuentasBanc();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de chequera.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        sede = null;
        sedeSeleccionada = null;
        entidadEnEdicion = new SgChequera();
        comboCuentasBancarias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        titulo = "Agregar Chequera";
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        sedeSeleccionada = null;
        sedeSeleccionadaFiltro = null;
        sede = null;
        comboCuentasBancarias = new SofisComboG<>();
        comboCuentasBancariasFiltro = new SofisComboG<>();
        filtro = new FiltroChequera();
        filtroSede = new FiltroSedes();
        buscar();
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
     * Carga el combo de Sede para el filtro de pantalla.
     *
     * @param query String: Palabra de filtro digitada en el combo
     * @return List<SgSede>
     */
    public List<SgSede> completeSedeFiltro(String query) {
        try {
            filtroSede = new FiltroSedes();
            filtroSede.setSedCodigoONombre(query);
            filtroSede.setSedHabilitado(Boolean.TRUE);
            filtroSede.setMaxResults(11L);
            filtroSede.setOrderBy(new String[]{"sedNombre"});
            filtroSede.setAscending(new boolean[]{false});
            filtroSede.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeRestClient.buscar(filtroSede);
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
        sede = sedeSeleccionada;
        if (sede != null) {
            cargarCuentasBanc();
        }
    }

    /**
     * Setea el valor de la Sede Selecccionada devuelve el objeto Sede.
     */
    public void setearSedeFiltro() {
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
            comboCuentasBancariasFiltro = new SofisComboG<>();
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
                comboCuentasBancariasFiltro = new SofisComboG<>(cuentasRestClient.buscar(filtroCb), "cuentaLabelCombo");
            }
            comboCuentasBancariasFiltro.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

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
     * Busca los registros de Proveedores según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));

            if (sedeSeleccionadaFiltro != null) {
                filtro.setSedePk(sedeSeleccionadaFiltro.getSedPk());
            }

            filtro.setIncluirCampos(new String[]{
                "chePk",
                "cheCuentaBancariaFk.cbcPk",
                "cheCuentaBancariaFk.cbcSedeFk.sedPk",
                "cheCuentaBancariaFk.cbcSedeFk.sedTipo",
                "cheCuentaBancariaFk.cbcSedeFk.sedCodigo",
                "cheCuentaBancariaFk.cbcSedeFk.sedNombre",
                "cheCuentaBancariaFk.cbcSedeFk.sedVersion",
                "cheCuentaBancariaFk.cbcNumeroCuenta",
                "cheCuentaBancariaFk.cbcTitular",
                "cheCuentaBancariaFk.cbcBancoFk.bncNombre",
                "cheCuentaBancariaFk.cbcVersion",
                "cheFechaChequera",
                "cheSerie",
                "cheNumeroInicial",
                "cheNumeroFinal",
                "cheUltMod",
                "cheUltUsuario",
                "cheVersion"
            });

            if (comboCuentasBancariasFiltro.getSelectedT() != null) {
                filtro.setCuentaBancaria(comboCuentasBancariasFiltro.getSelectedT().getCbcPk());
            }
            filtro.setOrderBy(new String[]{"cheFechaChequera"});
            filtro.setAscending(new boolean[]{false});
            totalResultados = restClient.buscarTotal(filtro);
            chqueraLazyModel = new LazyChequeraDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los datos de un pago para poder ser editados.
     *
     * @param var SgPago: Elemento del grid seleccionado de cuenta bancaria.
     */
    public void actualizar(SgChequera var) {
        JSFUtils.limpiarMensajesError();
        limpiar();
        entidadEnEdicion = (SgChequera) SerializationUtils.clone(var);
        titulo = StringUtils.replace(Etiquetas.getValue("edicionChequerax"), "[x]", entidadEnEdicion.getCheCuentaBancariaFk().getCbcNumeroCuenta().toString());
        sedeSeleccionada = entidadEnEdicion.getCheCuentaBancariaFk().getCbcSedeFk();
        sede = sedeSeleccionada;
        cargarCuentasBanc();
        comboCuentasBancarias.setSelectedT(entidadEnEdicion.getCheCuentaBancariaFk());
        datosCuenta();
    }

    /**
     * Crea o actualiza un registro de pago.
     */
    public void guardar() {
        try {
            if (comboCuentasBancarias.getSelectedT() != null) {
                entidadEnEdicion.setCheCuentaBancariaFk(comboCuentasBancarias.getSelectedT());
                entidadEnEdicion.setCheSedeFk(sede);
            }
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            limpiar();
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
            restClient.eliminar(entidadEnEdicion.getChePk());
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
     * Maneja el render de la accion Eliminar.
     */
    public boolean inicializarBorrar(SgChequera var) {
        try {
            pagos = new ArrayList<>();
            FiltroPago pago = new FiltroPago();
            pago.setIncluirCampos(new String[]{"pgsPk",
                "pgsChequeraFk.chePk",
                "pgsChequeraFk.cheSerie",
                "pgsChequeraFk.cheVersion",
                "pgsVersion"
            });
            pago.setPgsChequeraFk(var.getChePk());
            pagos = pagosRestClient.buscar(pago);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        if (pagos.isEmpty()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Ajax que carga los datos de la cuenta.
     */
    public void datosCuenta() {
        banco = new String();
        titular = new String();
        verDatosCuenta = Boolean.FALSE;
        if (comboCuentasBancarias.getSelectedT() != null) {
            verDatosCuenta = Boolean.TRUE;
            banco = comboCuentasBancarias.getSelectedT().getCbcBancoFk().getBncNombre();
            titular = comboCuentasBancarias.getSelectedT().getCbcTitular();
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

    public ChequeraRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ChequeraRestClient restClient) {
        this.restClient = restClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public LazyChequeraDataModel getChqueraLazyModel() {
        return chqueraLazyModel;
    }

    public void setChqueraLazyModel(LazyChequeraDataModel chqueraLazyModel) {
        this.chqueraLazyModel = chqueraLazyModel;
    }

    public FiltroChequera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroChequera filtro) {
        this.filtro = filtro;
    }

    public SgChequera getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgChequera entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Long getChequeraId() {
        return chequeraId;
    }

    public void setChequeraId(Long chequeraId) {
        this.chequeraId = chequeraId;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
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

    public SofisComboG<SgCuentasBancarias> getComboCuentasBancariasFiltro() {
        return comboCuentasBancariasFiltro;
    }

    public void setComboCuentasBancariasFiltro(SofisComboG<SgCuentasBancarias> comboCuentasBancariasFiltro) {
        this.comboCuentasBancariasFiltro = comboCuentasBancariasFiltro;
    }

    public SgSede getSedeSeleccionadaFiltro() {
        return sedeSeleccionadaFiltro;
    }

    public void setSedeSeleccionadaFiltro(SgSede sedeSeleccionadaFiltro) {
        this.sedeSeleccionadaFiltro = sedeSeleccionadaFiltro;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public Boolean getVerDatosCuenta() {
        return verDatosCuenta;
    }

    public void setVerDatosCuenta(Boolean verDatosCuenta) {
        this.verDatosCuenta = verDatosCuenta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    // </editor-fold>
}
