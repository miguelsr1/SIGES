/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.time.LocalDate;
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
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgPlantilla;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlantilla;
import sv.gob.mined.siges.web.lazymodels.LazyPlantillaDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PlantillaRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PlantillaBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(PlantillaBean.class.getName());
    
    @Inject
    private PlantillaRestClient restClient;
    
    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;
    
    private FiltroPlantilla filtro = new FiltroPlantilla();
    private SgPlantilla entidadEnEdicion = new SgPlantilla();
    private List<SgPlantilla> historialPlantilla = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyPlantillaDataModel plantillaLazyModel;
    
    public PlantillaBean() {
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
            filtro.setSoloPlantillasPorDefecto(Boolean.TRUE);
            totalResultados = restClient.buscarTotal(filtro);
            plantillaLazyModel = new LazyPlantillaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarCombos() {
        
    }
    
    private void limpiarCombos() {
        
    }
    
    public void limpiar() {
        filtro = new FiltroPlantilla();
        buscar();
    }
    
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgPlantilla();
    }
    
    public void actualizar(SgPlantilla var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgPlantilla) SerializationUtils.clone(var);
    }
    
    public void guardar() {
        try {
            if (entidadEnEdicion.getPlaPk() == null) {
                if (entidadEnEdicion.getPlaHabilitado()) {
                    entidadEnEdicion.setPlaFechaHabilitada(LocalDate.now());
                }
            } else {
                if (entidadEnEdicion.getPlaFechaHabilitada() == null) {
                    entidadEnEdicion.setPlaFechaHabilitada(entidadEnEdicion.getPlaHabilitado() ? LocalDate.now() : null);
                } else {
                    if (entidadEnEdicion.getPlaFechaDeshabilitada() == null) {
                        entidadEnEdicion.setPlaFechaDeshabilitada(!entidadEnEdicion.getPlaHabilitado() ? LocalDate.now() : null);
                    }
                }
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
    
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getPlaPk());
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
            historialPlantilla = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void handleFileUploadPlantilla(FileUploadEvent event) {
        if (entidadEnEdicion.getPlaArchivo() == null) {
            entidadEnEdicion.setPlaArchivo(new SgArchivo());
        }
        FileUploadUtils.handleFileUpload(event.getFile(), entidadEnEdicion.getPlaArchivo(), tmpBasePath);
    }
    
    public void eliminarArchivoPlantilla() {
        this.entidadEnEdicion.setPlaArchivo(null);
    }
    
    public FiltroPlantilla getFiltro() {
        return filtro;
    }
    
    public void setFiltro(FiltroPlantilla filtro) {
        this.filtro = filtro;
    }
    
    public SgPlantilla getEntidadEnEdicion() {
        return entidadEnEdicion;
    }
    
    public void setEntidadEnEdicion(SgPlantilla entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }
    
    public List<SgPlantilla> getHistorialPlantilla() {
        return historialPlantilla;
    }
    
    public void setHistorialPlantilla(List<SgPlantilla> historialPlantilla) {
        this.historialPlantilla = historialPlantilla;
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
    
    public LazyPlantillaDataModel getPlantillaLazyModel() {
        return plantillaLazyModel;
    }
    
    public void setTiposCalendarioLazyModel(LazyPlantillaDataModel plantillaLazyModel) {
        this.plantillaLazyModel = plantillaLazyModel;
    }
    
}
