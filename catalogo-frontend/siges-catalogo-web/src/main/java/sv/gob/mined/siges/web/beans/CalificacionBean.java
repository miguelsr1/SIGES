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
import sv.gob.mined.siges.web.dto.SgCalificacion;
import sv.gob.mined.siges.web.dto.SgEscalaCalificacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEscalaCalificacion;
import sv.gob.mined.siges.web.lazymodels.LazyCalificacionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CalificacionRestClient;
import sv.gob.mined.siges.web.restclient.EscalaCalificacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CalificacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CalificacionBean.class.getName());

    @Inject
    private CalificacionRestClient restClient;
    
    @Inject
    private EscalaCalificacionRestClient ecaRestClient;
    
    @Inject
    private SessionBean sessionBean;

    private FiltroCalificacion filtro = new FiltroCalificacion();
    private SgCalificacion entidadEnEdicion = new SgCalificacion();
    private List<SgCalificacion> historialCalificacion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCalificacionDataModel calificacionLazyModel;
    private SofisComboG<SgEscalaCalificacion> comboEscalaCalificacion;

    public CalificacionBean() {
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
            if(comboEscalaCalificacion.getSelectedT()!=null){
                filtro.setCalEscala(comboEscalaCalificacion.getSelectedT().getEcaNombre());
            }
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            calificacionLazyModel = new LazyCalificacionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
     public List<SgEscalaCalificacion> completeEscalaCalificacion(String query) {
        try {
            FiltroEscalaCalificacion fil = new FiltroEscalaCalificacion();
            fil.setEcaHabilitado(Boolean.TRUE);
            fil.setEcaNombre(query);
            fil.setMaxResults(10L);
            return ecaRestClient.buscar(fil);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {
        try{
            FiltroEscalaCalificacion fec= new FiltroEscalaCalificacion();
        
            comboEscalaCalificacion=new SofisComboG(ecaRestClient.buscar(fec),"ecaNombre");
            comboEscalaCalificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
         } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboEscalaCalificacion.setSelected(0);
    }

    public void limpiar() {
        comboEscalaCalificacion.setSelected(0);
        filtro = new FiltroCalificacion();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgCalificacion();
    }

    public void actualizar(SgCalificacion var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgCalificacion) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCalPk());
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
            historialCalificacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCalificacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCalificacion filtro) {
        this.filtro = filtro;
    }

    public SgCalificacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCalificacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgCalificacion> getHistorialCalificacion() {
        return historialCalificacion;
    }

    public void setHistorialCalificacion(List<SgCalificacion> historialCalificacion) {
        this.historialCalificacion = historialCalificacion;
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

    public LazyCalificacionDataModel getCalificacionLazyModel() {
        return calificacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCalificacionDataModel calificacionLazyModel) {
        this.calificacionLazyModel = calificacionLazyModel;
    }

    public SofisComboG<SgEscalaCalificacion> getComboEscalaCalificacion() {
        return comboEscalaCalificacion;
    }

    public void setComboEscalaCalificacion(SofisComboG<SgEscalaCalificacion> comboEscalaCalificacion) {
        this.comboEscalaCalificacion = comboEscalaCalificacion;
    }

}
