/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAula;
import sv.gob.mined.siges.web.dto.SgRelAulaServicio;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgServicioBasico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelAulaServicio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioBasico;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.AulaRestClient;
import sv.gob.mined.siges.web.restclient.RelAulaServicioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RelAulaServicioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RelAulaServicioBean.class.getName());

    @Inject
    private RelAulaServicioRestClient restClient;
    
    @Inject
    private AulaRestClient aulaRestClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;  
    

    
    
    private SgAula entidadEnEdicion = new SgAula();
    
    
    private SgSede sedeSeleccionada;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean callingPostConstruct;
    private String securityOperation;

    private List<SgRelAulaServicio> aulaServicio= new ArrayList();
    private List<SgRelAulaServicio> aulaServicioSeleccionados= new ArrayList();
    private List<SgRelAulaServicio> aulaServicioAntesDeCambio= new ArrayList(); 
    private Boolean beanIniciado=Boolean.FALSE;
 

    public RelAulaServicioBean() {
        
    }
    
    
    @PostConstruct
    public void init() {
        try {          

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgAula) request.getAttribute("aula");
            securityOperation = (String) request.getAttribute("securityOperation");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean)request.getAttribute("soloLectura")) : soloLectura ;
            cargarEntidad();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void cargarEntidad() {
        try {          
           if(!beanIniciado){
                cargarAulaServicio();
                beanIniciado=Boolean.TRUE;
           }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
  



    
    public void cargarAulaServicio() {
        try {
           
            aulaServicio= new ArrayList();
            aulaServicioSeleccionados=new ArrayList();
            aulaServicioAntesDeCambio= new ArrayList(); 
            List<SgServicioBasico> servicioBasicoList = new ArrayList();   
            
            FiltroServicioBasico filtro = new FiltroServicioBasico();
            filtro.setHabilitado(Boolean.TRUE);      
            filtro.setAplicaAula(Boolean.TRUE);
            servicioBasicoList = catalogosRestClient.buscarServicioBasico(filtro);
            
            FiltroRelAulaServicio fic=new FiltroRelAulaServicio();
            fic.setRasAulaFk(entidadEnEdicion.getAulPk());
            aulaServicioSeleccionados=restClient.buscar(fic);
            
            if(!aulaServicioSeleccionados.isEmpty()){
                aulaServicio.addAll(aulaServicioSeleccionados);
                List<SgServicioBasico> serviciosSeleccionados=aulaServicio.stream().map(c->c.getRasServicio()).collect(Collectors.toList());
                for(SgServicioBasico esp:servicioBasicoList){
                    if(!serviciosSeleccionados.contains(esp)){
                        SgRelAulaServicio relEspCom=new SgRelAulaServicio();
                        relEspCom.setRasServicio(esp);
                        aulaServicio.add(relEspCom);
                    }
                }
            }else{
                for(SgServicioBasico esp:servicioBasicoList){
                        SgRelAulaServicio relEspCom=new SgRelAulaServicio();
                        relEspCom.setRasServicio(esp);
                        aulaServicio.add(relEspCom);
                }
            }         
                                  
           Collections.sort(aulaServicio, (o1, o2) -> o1.getRasServicio().getSbaNombre().compareTo(o2.getRasServicio().getSbaNombre()));  
            
          

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }
    

    public void guardar() {
        try {           
           
            entidadEnEdicion.setAulRelAulaServicio(new ArrayList());
            entidadEnEdicion.getAulRelAulaServicio().addAll(aulaServicioSeleccionados);          
          
            aulaRestClient.guardarListaServiciosBasicos(entidadEnEdicion);  
           
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
             cargarAulaServicio();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }   

    public SgAula getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAula entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public List<SgRelAulaServicio> getAulaServicio() {
        return aulaServicio;
    }

    public void setAulaServicio(List<SgRelAulaServicio> aulaServicio) {
        this.aulaServicio = aulaServicio;
    }

    public List<SgRelAulaServicio> getAulaServicioSeleccionados() {
        return aulaServicioSeleccionados;
    }

    public void setAulaServicioSeleccionados(List<SgRelAulaServicio> aulaServicioSeleccionados) {
        this.aulaServicioSeleccionados = aulaServicioSeleccionados;
    }

    public List<SgRelAulaServicio> getAulaServicioAntesDeCambio() {
        return aulaServicioAntesDeCambio;
    }

    public void setAulaServicioAntesDeCambio(List<SgRelAulaServicio> aulaServicioAntesDeCambio) {
        this.aulaServicioAntesDeCambio = aulaServicioAntesDeCambio;
    }    


    public SgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(SgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }


    public Boolean getCallingPostConstruct() {
        return callingPostConstruct;
    }

    public void setCallingPostConstruct(Boolean callingPostConstruct) {
        this.callingPostConstruct = callingPostConstruct;
    }

    public Boolean getBeanIniciado() {
        return beanIniciado;
    }

    public void setBeanIniciado(Boolean beanIniciado) {
        this.beanIniciado = beanIniciado;
    }


}
