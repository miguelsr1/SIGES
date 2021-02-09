/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import sv.gob.mined.siges.web.dto.SgAccionSolicitudTraslado;
import sv.gob.mined.siges.web.enumerados.EnumEstadoSolicitudTraslado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAccion;
import sv.gob.mined.siges.web.lazymodels.LazyAccionSolicitudTrasladoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AccionSolicitudTrasladoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AccionSolicitudTrasladoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AccionSolicitudTrasladoBean.class.getName());

    @Inject
    private AccionSolicitudTrasladoRestClient restClient;

    private FiltroAccion filtro = new FiltroAccion();
    private SgAccionSolicitudTraslado entidadEnEdicion = new SgAccionSolicitudTraslado();
    private List<SgAccionSolicitudTraslado> historialAccion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyAccionSolicitudTrasladoDataModel AccionLazyModel;
    private SofisComboG<EnumEstadoSolicitudTraslado> comboEstadoOrigen;
    private SofisComboG<EnumEstadoSolicitudTraslado> comboEstadoDestino;

    public AccionSolicitudTrasladoBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setAccEstadoOrigen(comboEstadoOrigen.getSelectedT()!=null?comboEstadoOrigen.getSelectedT():null);
            filtro.setAccEstadoDestino(comboEstadoDestino.getSelectedT()!=null?comboEstadoDestino.getSelectedT():null);
            totalResultados = restClient.buscarTotal(filtro);
            AccionLazyModel = new LazyAccionSolicitudTrasladoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        List<EnumEstadoSolicitudTraslado> estados = new ArrayList(Arrays.asList(EnumEstadoSolicitudTraslado.values()));
        
        comboEstadoOrigen = new SofisComboG(estados, "text");
        comboEstadoOrigen.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        
        comboEstadoDestino = new SofisComboG(estados, "text");
        comboEstadoDestino.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroAccion();
        comboEstadoOrigen.setSelected(-1);
        comboEstadoDestino.setSelected(-1);
        buscar();
    }

    public void agregar() {
        comboEstadoOrigen.setSelected(-1);
        comboEstadoDestino.setSelected(-1);
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgAccionSolicitudTraslado();
    }

    public void actualizar(SgAccionSolicitudTraslado var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgAccionSolicitudTraslado) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion.setAccEstadoOrigen(comboEstadoOrigen.getSelectedT());
            entidadEnEdicion.setAccEstadoDestino(comboEstadoDestino.getSelectedT());
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
            restClient.eliminar(entidadEnEdicion.getAccPk());
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
            historialAccion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    
    
    
    
    
    
    
    
    
    
    
    
    public AccionSolicitudTrasladoRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(AccionSolicitudTrasladoRestClient restClient) {
        this.restClient = restClient;
    }

    public SofisComboG<EnumEstadoSolicitudTraslado> getComboEstadoOrigen() {
        return comboEstadoOrigen;
    }

    public void setComboEstadoOrigen(SofisComboG<EnumEstadoSolicitudTraslado> comboEstadoOrigen) {
        this.comboEstadoOrigen = comboEstadoOrigen;
    }

    public SofisComboG<EnumEstadoSolicitudTraslado> getComboEstadoDestino() {
        return comboEstadoDestino;
    }

    public void setComboEstadoDestino(SofisComboG<EnumEstadoSolicitudTraslado> comboEstadoDestino) {
        this.comboEstadoDestino = comboEstadoDestino;
    }
    
    
    
    
    
    
    
    

    public FiltroAccion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAccion filtro) {
        this.filtro = filtro;
    }

    public SgAccionSolicitudTraslado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAccionSolicitudTraslado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgAccionSolicitudTraslado> getHistorialAccion() {
        return historialAccion;
    }

    public void setHistorialAccion(List<SgAccionSolicitudTraslado> historialAccion) {
        this.historialAccion = historialAccion;
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

    public LazyAccionSolicitudTrasladoDataModel getAccionLazyModel() {
        return AccionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyAccionSolicitudTrasladoDataModel AccionLazyModel) {
        this.AccionLazyModel = AccionLazyModel;
    }

}
