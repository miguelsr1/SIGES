/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAfEstadosTraslado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyEstadosTrasladoDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EstadosTrasladoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EstadosTrasladoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstadosTrasladoBean.class.getName());

    @Inject
    private EstadosTrasladoRestClient restClient;
    
    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgAfEstadosTraslado entidadEnEdicion = new SgAfEstadosTraslado();
    private List<SgAfEstadosTraslado> historialEstadoTraslado = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyEstadosTrasladoDataModel estadosTrasladoLazyModel;
    
    public EstadosTrasladoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            estadosTrasladoLazyModel = new LazyEstadosTrasladoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgAfEstadosTraslado();
    }

    public void actualizar(SgAfEstadosTraslado var) {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = (SgAfEstadosTraslado) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            restClient.guardar(entidadEnEdicion);
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

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getEtrPk());
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

    public void historial(Long id) {
        try {
            historialEstadoTraslado = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public EstadosTrasladoRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(EstadosTrasladoRestClient restClient) {
        this.restClient = restClient;
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgAfEstadosTraslado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfEstadosTraslado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgAfEstadosTraslado> getHistorialEstadoTraslado() {
        return historialEstadoTraslado;
    }

    public void setHistorialEstadoTraslado(List<SgAfEstadosTraslado> historialEstadoTraslado) {
        this.historialEstadoTraslado = historialEstadoTraslado;
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

    public LazyEstadosTrasladoDataModel getEstadosTrasladoLazyModel() {
        return estadosTrasladoLazyModel;
    }

    public void setEstadosTrasladoLazyModel(LazyEstadosTrasladoDataModel estadosTrasladoLazyModel) {
        this.estadosTrasladoLazyModel = estadosTrasladoLazyModel;
    }

}