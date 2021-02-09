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
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgRelProyectoFinanciamientoSistemaIntegrado;
import sv.gob.mined.siges.web.dto.SgSistemaIntegrado;
import sv.gob.mined.siges.web.dto.catalogo.SgProyectoFinanciamientoSistemaIntegrado;
import sv.gob.mined.siges.web.filtros.FiltroRelProyectoFinanciamientoSistemaIntegrado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.restclient.ProyectoFinanciamientoSistemaIntegradoRestClient;
import sv.gob.mined.siges.web.restclient.RelProyectoFinanciamientoSistemaIntegradoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ProyectosFinanciamientoBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(ProyectosFinanciamientoBean.class.getName());
    
    @Inject
    private ProyectoFinanciamientoSistemaIntegradoRestClient proyectoFinanciamientoSistemaIntegradoRestClient;
    
    @Inject
    private RelProyectoFinanciamientoSistemaIntegradoRestClient relProyectoFinanciamientoRecurso;
    
    @Inject
    private SessionBean sessionBean;
    
    private Boolean soloLectura = Boolean.FALSE;
    private SgSistemaIntegrado sistemaIntegrado;
    private List<SgProyectoFinanciamientoSistemaIntegrado> proyectosFinanciamientoSelected = new ArrayList<>(); // los seleccionados en el popup
    private List<SgProyectoFinanciamientoSistemaIntegrado> proyectosFinanciamiento = new ArrayList<>(); // los listados en el popup
    private List<SgRelProyectoFinanciamientoSistemaIntegrado> proyectosFinanciamientoSistemaIntegrado = new ArrayList<>(); // los proyectos asociados al SI
    private SgRelProyectoFinanciamientoSistemaIntegrado entidadEliminar;
    private SgProyectoFinanciamientoSistemaIntegrado proyectoFinanciamientoSeleccionado;
    
    public ProyectosFinanciamientoBean() {
    }
    
    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            sistemaIntegrado = (SgSistemaIntegrado) request.getAttribute("sistemaIntegrado");
            if (sistemaIntegrado != null) {
                cargarCombos();
                cargarListaProyectosAsociados();
            } else {
                throw new Exception("No sistema integrado found");
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarCombos() {
        try {
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    private void limpiarCombos() {
    }
    
    public void limpiar() {
    }
    
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
    }
    
    public void buscarProyectosFinanciamiento() {
        try {
            this.proyectosFinanciamientoSelected = new ArrayList<>();
            this.proyectosFinanciamiento = new ArrayList<>();
            FiltroCodiguera filtro = new FiltroCodiguera();
            filtro.setIncluirCampos(new String[]{"pfsNombre", "pfsCodigo", "pfsVersion"});
            List<SgProyectoFinanciamientoSistemaIntegrado> proyectosTotales = proyectoFinanciamientoSistemaIntegradoRestClient.buscar(filtro);
            List<SgProyectoFinanciamientoSistemaIntegrado> proyectosActuales = proyectosFinanciamientoSistemaIntegrado.stream()
                    .map(a -> a.getRpsProyectoFinanciamiento()).collect(Collectors.toList());
            // filtro proyectos  no seleeccionados
            proyectosFinanciamiento = proyectosTotales.stream().filter(a -> !proyectosActuales.contains(a)).collect(Collectors.toList());
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void guardarProyectos() {
        try {
            List<SgRelProyectoFinanciamientoSistemaIntegrado> rel = new ArrayList<>();
            for (SgProyectoFinanciamientoSistemaIntegrado pr : this.proyectosFinanciamientoSelected) {
                SgRelProyectoFinanciamientoSistemaIntegrado relAcProp = new SgRelProyectoFinanciamientoSistemaIntegrado();
                relAcProp.setRpsSistemaIntegrado(this.sistemaIntegrado);
                relAcProp.setRpsProyectoFinanciamiento(pr);
                rel.add(relAcProp);
            }

            // guardar lista con proyectos 
            relProyectoFinanciamientoRecurso.guardar(rel);
            this.cargarListaProyectosAsociados();
            PrimeFaces.current().executeScript("PF('seleccionarProyectosDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarListaProyectosAsociados() {
        try {
            // cargar lista de proyectos asociados al SI
            FiltroRelProyectoFinanciamientoSistemaIntegrado filtroAc = new FiltroRelProyectoFinanciamientoSistemaIntegrado();
            filtroAc.setSistemaIntegradoPk(this.sistemaIntegrado.getSinPk());
            filtroAc.setIncluirCampos(new String[]{"rpsProyectoFinanciamiento",
                "rpsProyectoFinanciamiento.pfsCodigo",
                "rpsProyectoFinanciamiento.pfsNombre",
                "rpsProyectoFinanciamiento.pfsVersion", "rpsVersion"});
            List<SgRelProyectoFinanciamientoSistemaIntegrado> list = relProyectoFinanciamientoRecurso.buscar(filtroAc);
            this.proyectosFinanciamientoSistemaIntegrado = list;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP_2, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP_2, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void actualizarDelete(SgRelProyectoFinanciamientoSistemaIntegrado var) {
        entidadEliminar = (SgRelProyectoFinanciamientoSistemaIntegrado) SerializationUtils.clone(var);
    }
    
    public void eliminarProyectoAsociado() {
        try {
            this.relProyectoFinanciamientoRecurso.eliminar(entidadEliminar.getRpsPk());
            this.cargarListaProyectosAsociados();// recargo lista
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public Boolean getSoloLectura() {
        return soloLectura;
    }
    
    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }
    
    public ProyectoFinanciamientoSistemaIntegradoRestClient getProyectoFinanciamientoSistemaIntegradoRestClient() {
        return proyectoFinanciamientoSistemaIntegradoRestClient;
    }
    
    public void setProyectoFinanciamientoSistemaIntegradoRestClient(ProyectoFinanciamientoSistemaIntegradoRestClient proyectoFinanciamientoSistemaIntegradoRestClient) {
        this.proyectoFinanciamientoSistemaIntegradoRestClient = proyectoFinanciamientoSistemaIntegradoRestClient;
    }
    
    public RelProyectoFinanciamientoSistemaIntegradoRestClient getRelProyectoFinanciamientoRecurso() {
        return relProyectoFinanciamientoRecurso;
    }
    
    public void setRelProyectoFinanciamientoRecurso(RelProyectoFinanciamientoSistemaIntegradoRestClient relProyectoFinanciamientoRecurso) {
        this.relProyectoFinanciamientoRecurso = relProyectoFinanciamientoRecurso;
    }
    
    public SessionBean getSessionBean() {
        return sessionBean;
    }
    
    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
    
    public SgSistemaIntegrado getSistemaIntegrado() {
        return sistemaIntegrado;
    }
    
    public void setSistemaIntegrado(SgSistemaIntegrado sistemaIntegrado) {
        this.sistemaIntegrado = sistemaIntegrado;
    }
    
    public List<SgProyectoFinanciamientoSistemaIntegrado> getProyectosFinanciamientoSelected() {
        return proyectosFinanciamientoSelected;
    }
    
    public void setProyectosFinanciamientoSelected(List<SgProyectoFinanciamientoSistemaIntegrado> proyectosFinanciamientoSelected) {
        this.proyectosFinanciamientoSelected = proyectosFinanciamientoSelected;
    }
    
    public List<SgProyectoFinanciamientoSistemaIntegrado> getProyectosFinanciamiento() {
        return proyectosFinanciamiento;
    }
    
    public void setProyectosFinanciamiento(List<SgProyectoFinanciamientoSistemaIntegrado> proyectosFinanciamiento) {
        this.proyectosFinanciamiento = proyectosFinanciamiento;
    }
    
    public List<SgRelProyectoFinanciamientoSistemaIntegrado> getProyectosFinanciamientoSistemaIntegrado() {
        return proyectosFinanciamientoSistemaIntegrado;
    }
    
    public void setProyectosFinanciamientoSistemaIntegrado(List<SgRelProyectoFinanciamientoSistemaIntegrado> proyectosFinanciamientoSistemaIntegrado) {
        this.proyectosFinanciamientoSistemaIntegrado = proyectosFinanciamientoSistemaIntegrado;
    }
    
    public SgRelProyectoFinanciamientoSistemaIntegrado getEntidadEliminar() {
        return entidadEliminar;
    }
    
    public void setEntidadEliminar(SgRelProyectoFinanciamientoSistemaIntegrado entidadEliminar) {
        this.entidadEliminar = entidadEliminar;
    }
    
    public SgProyectoFinanciamientoSistemaIntegrado getProyectoFinanciamientoSeleccionado() {
        return proyectoFinanciamientoSeleccionado;
    }
    
    public void setProyectoFinanciamientoSeleccionado(SgProyectoFinanciamientoSistemaIntegrado proyectoFinanciamientoSeleccionado) {
        this.proyectoFinanciamientoSeleccionado = proyectoFinanciamientoSeleccionado;
    }
    
}
