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
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgPropuestaPedagogica;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.PropuestaPedagogicaRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgAcuerdo;
import sv.gob.mined.siges.web.dto.SgAcuerdoSede;
import sv.gob.mined.siges.web.dto.SgArchivo;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumEstadoAcuerdo;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtros.FiltroAcuerdo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroSedes;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.AcuerdoRestClient;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.FileUploadUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class PropuestaPedagogicaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(PropuestaPedagogicaBean.class.getName());

    @Inject
    private SedeRestClient sedeClient;

    @Inject
    private PropuestaPedagogicaRestClient restClient;

    @Inject
    private AcuerdoRestClient acuerdoRestClient;    

    @Inject
    private SessionBean sessionBean;

    @Inject
    @Param(name = "id")
    private Long propuestaId;

    @Inject
    @Param(name = "edit")
    private Boolean editable;

    @Inject
    @ConfigProperty(name = "files.tmp.path")
    private String tmpBasePath;

    private Boolean soloLectura = Boolean.FALSE;
    private SgPropuestaPedagogica entidadEnEdicion = new SgPropuestaPedagogica();
    private SgSede sedeSeleccionada;
    private List<SgAcuerdoSede> acuerdosSedeSelected = new ArrayList<>(); // los seleccionados en el popup
    private List<SgAcuerdoSede> acuerdosSede = new ArrayList<>(); // los listados en el popup
    private SgAcuerdoSede acuerdoSeleccionado;
    private SofisComboG<EnumEstadoAcuerdo> comboEstado;
    private SgAcuerdo acuerdoEnEdicion;
    private List<SgAcuerdo> acuerdoList;

    public PropuestaPedagogicaBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (propuestaId != null && propuestaId > 0) {
                this.actualizar(restClient.obtenerPorId(propuestaId));
                soloLectura = editable != null ? !editable : soloLectura;
            } else {
                agregar();
            }
            validarAcceso();
            buscarAcuerdosSede();
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }

    }

    public void validarAcceso() {
        if (soloLectura) {
            if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.AUTENTICADO)) {
                JSFUtils.redirectToIndex();
            }
        } else {
            if (entidadEnEdicion.getPpePk() == null) {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.CREAR_PROPUESTA_PEDAGOGICA)) {
                    JSFUtils.redirectToIndex();
                }
            } else {
                if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_PROPUESTA_PEDAGOGICA)) {
                    JSFUtils.redirectToIndex();
                }
            }
        }
    }

    public void actualizar(SgPropuestaPedagogica cal) {
        entidadEnEdicion = cal;
        sedeSeleccionada = entidadEnEdicion.getPpeSede();
        buscarAcuerdosSede();
    }

    public void cargarCombos() {
        try {
            comboEstado = new SofisComboG(new ArrayList(Arrays.asList(EnumEstadoAcuerdo.values())), "text");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public List<SgSede> completeSede(String query) {
        try {
            FiltroSedes fil = new FiltroSedes();
            fil.setSedCodigoONombre(query);
            fil.setSedHabilitado(Boolean.TRUE);
            fil.setMaxResults(11L);
            fil.setOrderBy(new String[]{"sedNombre"});
            fil.setAscending(new boolean[]{false});
            fil.setIncluirCampos(new String[]{"sedCodigo", "sedNombre", "sedTipo", "sedVersion"});
            return sedeClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ce), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    private void limpiarCombos() {
    }

    public void limpiar() {
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgPropuestaPedagogica();
        sedeSeleccionada = sessionBean.getSedeDefecto();
    }

    public void guardar() {
        try {
            entidadEnEdicion.setPpeSede(sedeSeleccionada);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            buscarAcuerdosSede();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void agregarAcuerdo(){
        acuerdoEnEdicion=new SgAcuerdo();
        comboEstado.setSelectedT(EnumEstadoAcuerdo.VIGENTE);
    }
    
    public void editarAcuerdo(SgAcuerdo acu){
        acuerdoEnEdicion=SerializationUtils.clone(acu);
        comboEstado.setSelectedT(acuerdoEnEdicion.getAcuEstado());
    }
    
    public void eliminarAcuerdo(){        
         try{
            acuerdoEnEdicion.setAcuPropuestaPedagogica(entidadEnEdicion);
            acuerdoRestClient.eliminar(acuerdoEnEdicion.getAcuPk());
            buscarAcuerdosSede();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    public void guardarAcuerdo(){
        try{
            acuerdoEnEdicion.setAcuPropuestaPedagogica(entidadEnEdicion);
            acuerdoEnEdicion.setAcuEstado(comboEstado.getSelectedT());
            acuerdoRestClient.guardar(acuerdoEnEdicion);
            buscarAcuerdosSede();
            PrimeFaces.current().executeScript("PF('AcuerdoSedeDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void buscarAcuerdosSede() {
        try {
            this.acuerdoList = new ArrayList<>();
            if(entidadEnEdicion.getPpePk()!=null){                
                FiltroAcuerdo filtro = new FiltroAcuerdo();
                filtro.setAcuPropuestaPedagogica(entidadEnEdicion.getPpePk());
                acuerdoList = acuerdoRestClient.buscar(filtro);
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

  

    public void handleFileUpload(FileUploadEvent event) {
        if (entidadEnEdicion.getPpeArchivo() == null) {
            entidadEnEdicion.setPpeArchivo(new SgArchivo());
        }
        FileUploadUtils.handleFileUpload(event.getFile(), entidadEnEdicion.getPpeArchivo(), tmpBasePath);
    }

    public void eliminarArchivo() {
        this.entidadEnEdicion.setPpeArchivo(null);
    }

    public SgPropuestaPedagogica getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgPropuestaPedagogica entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public List<SgAcuerdoSede> getAcuerdosSedeSelected() {
        return acuerdosSedeSelected;
    }

    public void setAcuerdosSedeSelected(List<SgAcuerdoSede> acuerdosSedeSelected) {
        this.acuerdosSedeSelected = acuerdosSedeSelected;
    }

    public List<SgAcuerdoSede> getAcuerdosSede() {
        return acuerdosSede;
    }

    public void setAcuerdosSede(List<SgAcuerdoSede> acuerdosSede) {
        this.acuerdosSede = acuerdosSede;
    }


    public SgAcuerdoSede getAcuerdoSeleccionado() {
        return acuerdoSeleccionado;
    }

    public void setAcuerdoSeleccionado(SgAcuerdoSede acuerdoSeleccionado) {
        this.acuerdoSeleccionado = acuerdoSeleccionado;
    }

    public SofisComboG<EnumEstadoAcuerdo> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<EnumEstadoAcuerdo> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SgAcuerdo getAcuerdoEnEdicion() {
        return acuerdoEnEdicion;
    }

    public void setAcuerdoEnEdicion(SgAcuerdo acuerdoEnEdicion) {
        this.acuerdoEnEdicion = acuerdoEnEdicion;
    }

    public List<SgAcuerdo> getAcuerdoList() {
        return acuerdoList;
    }

    public void setAcuerdoList(List<SgAcuerdo> acuerdoList) {
        this.acuerdoList = acuerdoList;
    }
    
    
}
