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
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgFactura;
import sv.gob.mined.siges.web.dto.siap2.SsProveedor;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroFactura;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProveedor;
import sv.gob.mined.siges.web.lazymodels.LazyProveedorDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.FacturaRestClient;
import sv.gob.mined.siges.web.restclient.ProveedorRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 * Bean para la gestión de proveedor.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped

public class ProveedorBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ProveedorBean.class.getName());

    @Inject
    private ProveedorRestClient restClient;
    @Inject
    private SessionBean sessionBean;
    private LazyProveedorDataModel proveedorLazyModel;
    private Integer paginado = 10;
    private Long totalResultados;
    private FiltroProveedor filtro = new FiltroProveedor();
    private SsProveedor entidadEnEdicion = new SsProveedor();
    private Locale locale;
    @Inject
    private FacturaRestClient facturaRestClient;
    @Inject
    @Param(name = "id")
    private Long proveedorId;
    private List<SgFactura> facturas = new ArrayList();

    /**
     * Constructor de la clase.
     */
    public ProveedorBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Proveedores.
     */
    @PostConstruct
    public void init() {
        try {
            buscar();
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            if (proveedorId != null && proveedorId > 0) {
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
        filtro = new FiltroProveedor();
        buscar();
    }

    /**
     * Busca los registros de Proveedores según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            proveedorLazyModel = new LazyProveedorDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Maneja el render de la columna Eliminar.
     */
    public boolean inicializarBorrar(SsProveedor var) {
        try {
            facturas = new ArrayList<>();
            FiltroFactura factura = new FiltroFactura();
            factura.setIncluirCampos(new String[]{"facPk",
                "facVersion",
                "facNumero",
                "facProveedorFk.proId",
                "facProveedorFk.proVersion",
                "facEstado",
                "facTotal",
                "facTipoDocumento"
            });
            factura.setFacProveedorId(var.getProId());
            facturas = facturaRestClient.buscar(factura);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        if (facturas.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Carga los datos de un Proveedor para poder ser editados.
     *
     * @param var SsProveedor: Elemento del grid seleccionado de proveedores.
     */
    public void actualizar(SsProveedor var) {
        JSFUtils.limpiarMensajesError();
        limpiar();
        entidadEnEdicion = (SsProveedor) SerializationUtils.clone(var);

    }

    /**
     * Crea o actualiza un registro de pago.
     */
    public void guardar() {
        try {
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
     * Elimina un registro de Proveedor.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getProId());
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

    public ProveedorRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ProveedorRestClient restClient) {
        this.restClient = restClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public LazyProveedorDataModel getProveedorLazyModel() {
        return proveedorLazyModel;
    }

    public void setProveedorLazyModel(LazyProveedorDataModel proveedorLazyModel) {
        this.proveedorLazyModel = proveedorLazyModel;
    }

    public SsProveedor getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SsProveedor entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroProveedor getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroProveedor filtro) {
        this.filtro = filtro;
    }

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }
    // </editor-fold>
}
