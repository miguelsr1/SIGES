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
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAsociacion;
import sv.gob.mined.siges.web.dto.SgProyectoInstitucional;
import sv.gob.mined.siges.web.dto.SgTelefono;
import sv.gob.mined.siges.web.dto.SgTipoAsociacion;
import sv.gob.mined.siges.web.dto.SgTipoTelefono;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsociacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroProyectoInstitucional;
import sv.gob.mined.siges.web.lazymodels.LazyAsociacionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AsociacionRestClient;
import sv.gob.mined.siges.web.restclient.ProyectoInstitucionalRestClient;
import sv.gob.mined.siges.web.restclient.TelefonoRestClient;
import sv.gob.mined.siges.web.restclient.TipoAsociacionRestClient;
import sv.gob.mined.siges.web.restclient.TipoTelefonoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class AsociacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AsociacionBean.class.getName());

    @Inject
    private AsociacionRestClient restClient;
    
    @Inject
    private TipoAsociacionRestClient restTipoAsociacion;
    
    @Inject
    private TipoTelefonoRestClient restTipoTelefono;
    
    @Inject
    private ProyectoInstitucionalRestClient restProyectos;
    
    @Inject
    private TelefonoRestClient restTelefono;
    
    @Inject
    @Param(name = "id")
    private Long asoId;

    private FiltroAsociacion filtro = new FiltroAsociacion();
    private SgAsociacion entidadEnEdicion = new SgAsociacion();
    private List<SgAsociacion> historialAsociacion = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyAsociacionDataModel asociacionLazyModel;
    private SofisComboG<SgTipoAsociacion> comboTipoAsociacion;
    private SgTelefono telefonoEnEdicion = new SgTelefono();
    private SofisComboG<SgTipoTelefono> comboTiposTelefonos;
    private SofisComboG<SgProyectoInstitucional> comboProyectos;

    public AsociacionBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
            if(asoId!=null){
                this.actualizar(restClient.obtenerPorId(asoId));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setTipoAsociacion((comboTipoAsociacion.getSelectedT()!=null)?comboTipoAsociacion.getSelectedT().getTasPk():null);
            totalResultados = restClient.buscarTotal(filtro);
            asociacionLazyModel = new LazyAsociacionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
        
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            fc.setOrderBy(new String[]{"tasNombre"});
            
            List<SgTipoAsociacion> tiposAsociaciones = restTipoAsociacion.buscar(fc);
            comboTipoAsociacion = new SofisComboG(tiposAsociaciones, "tasNombre");
            comboTipoAsociacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
            FiltroProyectoInstitucional fp = new FiltroProyectoInstitucional();
            fp.setHabilitado(Boolean.TRUE);
            fp.setAscending(new boolean[]{true});
            fp.setOrderBy(new String[]{"pinNombre"});
            comboProyectos = new SofisComboG(restProyectos.buscar(fp), "pinNombre");
            comboProyectos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            

            fc.setOrderBy(new String[]{"ttoNombre"});
            List<SgTipoTelefono> tiposTelefonos = restTipoTelefono.buscar(fc);
            comboTiposTelefonos = new SofisComboG(new ArrayList(tiposTelefonos), "ttoNombre");
            comboTiposTelefonos.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroAsociacion();
        comboTipoAsociacion.setSelected(-1);
        buscar();
    }
    
    public void comboTipoAsociacionSelected(){
        this.entidadEnEdicion.setAsoTipoAsociacion(comboTipoAsociacion.getSelectedT());
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgAsociacion();
    }

    public void actualizar(SgAsociacion var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgAsociacion) SerializationUtils.clone(var);
        if(entidadEnEdicion!=null){
            comboTipoAsociacion.setSelectedT(entidadEnEdicion.getAsoTipoAsociacion());
        }
    }

    public void guardar() {
        try {
            entidadEnEdicion=restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    
    public void agregarTelefono() {
        JSFUtils.limpiarMensajesError();
        telefonoEnEdicion = new SgTelefono();
        comboTiposTelefonos.setSelected(-1);
    }
    
    public void editarTelefono(SgTelefono telefono) {
        JSFUtils.limpiarMensajesError();
        telefonoEnEdicion = (SgTelefono) SerializationUtils.clone(telefono);
        comboTiposTelefonos.setSelectedT(telefonoEnEdicion.getTelTipoTelefono());
    }

    public void eliminarTelefono(SgTelefono telefono) {
        try{
            restTelefono.eliminar(telefono.getTelPk());
            this.entidadEnEdicion.getAsoTelefonos().remove(telefono);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    public void agregarTelefonoAAsociacion() {
        try {
            telefonoEnEdicion.setTelTipoTelefono(comboTiposTelefonos.getSelectedT());
            telefonoEnEdicion.setTelAsociaciones(this.entidadEnEdicion);
            
            if(this.entidadEnEdicion.getAsoTelefonos()==null){
                this.entidadEnEdicion.setAsoTelefonos(new ArrayList<>());
            }
            
            if (this.entidadEnEdicion.getAsoTelefonos().contains(telefonoEnEdicion)) {
                this.entidadEnEdicion.getAsoTelefonos().set(this.entidadEnEdicion.getAsoTelefonos().indexOf(telefonoEnEdicion), telefonoEnEdicion);
            } else {
                restTelefono.guardar(telefonoEnEdicion);
                this.entidadEnEdicion.getAsoTelefonos().add(telefonoEnEdicion);
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('telefonoDialog').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    public void agregarProyecto() {
        comboProyectos.setSelected(-1);
    }
    
    public void editarProyecto(SgProyectoInstitucional proyecto) {
        comboProyectos.setSelectedT(proyecto);
    }

    public void eliminarProyecto(SgProyectoInstitucional proyecto) {
        try{
            this.entidadEnEdicion.getAsoProyectos().remove(proyecto);
            restClient.guardar(entidadEnEdicion);
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void agregarProyectoAAsociacion() {
        
        try {
            if(comboProyectos.getSelectedT()!=null){
                if(this.entidadEnEdicion.getAsoProyectos()==null){
                    this.entidadEnEdicion.setAsoProyectos(new ArrayList<>());
                }
                if(!this.entidadEnEdicion.getAsoProyectos().contains(comboProyectos.getSelectedT())){
                    this.entidadEnEdicion.getAsoProyectos().add(comboProyectos.getSelectedT());
                    restClient.guardar(entidadEnEdicion);
                }
            }
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            PrimeFaces.current().executeScript("PF('proyectoDialog').hide()");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    
    
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getAsoPk());
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
            historialAsociacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroAsociacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroAsociacion filtro) {
        this.filtro = filtro;
    }

    public SgAsociacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAsociacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgAsociacion> getHistorialAsociacion() {
        return historialAsociacion;
    }

    public void setHistorialAsociacion(List<SgAsociacion> historialAsociacion) {
        this.historialAsociacion = historialAsociacion;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
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

    public LazyAsociacionDataModel getAsociacionLazyModel() {
        return asociacionLazyModel;
    }

    public void setAsociacionLazyModel(LazyAsociacionDataModel asociacionLazyModel) {
        this.asociacionLazyModel = asociacionLazyModel;
    }

    public AsociacionRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(AsociacionRestClient restClient) {
        this.restClient = restClient;
    }

    public SofisComboG<SgTipoAsociacion> getComboTipoAsociacion() {
        return comboTipoAsociacion;
    }

    public void setComboTipoAsociacion(SofisComboG<SgTipoAsociacion> comboTipoAsociacion) {
        this.comboTipoAsociacion = comboTipoAsociacion;
    }

    public SgTelefono getTelefonoEnEdicion() {
        return telefonoEnEdicion;
    }

    public void setTelefonoEnEdicion(SgTelefono telefonoEnEdicion) {
        this.telefonoEnEdicion = telefonoEnEdicion;
    }

    public SofisComboG<SgTipoTelefono> getComboTiposTelefonos() {
        return comboTiposTelefonos;
    }

    public void setComboTiposTelefonos(SofisComboG<SgTipoTelefono> comboTiposTelefonos) {
        this.comboTiposTelefonos = comboTiposTelefonos;
    }

    public TipoAsociacionRestClient getRestTipoAsociacion() {
        return restTipoAsociacion;
    }

    public void setRestTipoAsociacion(TipoAsociacionRestClient restTipoAsociacion) {
        this.restTipoAsociacion = restTipoAsociacion;
    }

    public TipoTelefonoRestClient getRestTipoTelefono() {
        return restTipoTelefono;
    }

    public void setRestTipoTelefono(TipoTelefonoRestClient restTipoTelefono) {
        this.restTipoTelefono = restTipoTelefono;
    }

    public Long getAsoId() {
        return asoId;
    }

    public void setAsoId(Long asoId) {
        this.asoId = asoId;
    }

    public SofisComboG<SgProyectoInstitucional> getComboProyectos() {
        return comboProyectos;
    }

    public void setComboProyectos(SofisComboG<SgProyectoInstitucional> comboProyectos) {
        this.comboProyectos = comboProyectos;
    }

}
