/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleSelectEvent;
import org.primefaces.event.UnselectEvent;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgEdificio;
import sv.gob.mined.siges.web.dto.SgRelEdificioServicio;
import sv.gob.mined.siges.web.dto.SgRelEdificioServicioSanitario;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.SgTipoServicioSanitario;
import sv.gob.mined.siges.web.dto.catalogo.SgServicioBasico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelEdificioServicio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelEdificioServicioSanitario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioBasico;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTipoServicioSanitario;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EdificioRestClient;
import sv.gob.mined.siges.web.restclient.RelEdificioServicioRestClient;
import sv.gob.mined.siges.web.restclient.RelEdificioServicioSanitarioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RelEdificioServicioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RelEdificioServicioBean.class.getName());

    @Inject
    private RelEdificioServicioRestClient restClient;
    
    @Inject
    private EdificioRestClient edificioRestClient;
    
    @Inject
    private RelEdificioServicioSanitarioRestClient relEdificioServicioSanitarioRestClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;    
    
    @Inject
    private InmuebleSedesBean inmuebleSedeBean;
    
    
    private SgEdificio entidadEnEdicion = new SgEdificio();
    
    
    private SgSede sedeSeleccionada;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean callingPostConstruct;
    private String securityOperation;

    private List<SgRelEdificioServicio> edificioServicio= new ArrayList();
    private List<SgRelEdificioServicio> edificioServicioSeleccionados= new ArrayList();
    private List<SgRelEdificioServicioSanitario> edificioServicioSanitario= new ArrayList();
    private List<SgRelEdificioServicioSanitario> edificioServicioSanitarioSeleccionados= new ArrayList();
    private Boolean beanIniciado=Boolean.FALSE;
 
    private Boolean[] habilitarEntradaTextoServicioSanitario;
    
    public RelEdificioServicioBean() {
        
    }
    
    
    @PostConstruct
    public void init() {
        try {          

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgEdificio) request.getAttribute("edificio");
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
                cargarEdificioServicio();
                cargarEdificioServicioSanitario();
                beanIniciado=Boolean.TRUE;
           }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
  



    
    public void cargarEdificioServicio() {
        try {
           
            edificioServicio= new ArrayList();
            edificioServicioSeleccionados=new ArrayList();
            List<SgServicioBasico> servicioBasicoList = new ArrayList();   
            
            FiltroServicioBasico filtro = new FiltroServicioBasico();
            filtro.setHabilitado(Boolean.TRUE);      
            filtro.setAplicaEdificio(Boolean.TRUE);
            servicioBasicoList = catalogosRestClient.buscarServicioBasico(filtro);
            
            FiltroRelEdificioServicio fic=new FiltroRelEdificioServicio();
            fic.setResEdificioFk(entidadEnEdicion.getEdiPk());
            edificioServicioSeleccionados=restClient.buscar(fic);
            
            if(!edificioServicioSeleccionados.isEmpty()){
                edificioServicio.addAll(edificioServicioSeleccionados);
                List<SgServicioBasico> serviciosSeleccionados=edificioServicio.stream().map(c->c.getResServicio()).collect(Collectors.toList());
                for(SgServicioBasico esp:servicioBasicoList){
                    if(!serviciosSeleccionados.contains(esp)){
                        SgRelEdificioServicio relEspCom=new SgRelEdificioServicio();
                        relEspCom.setResServicio(esp);
                        edificioServicio.add(relEspCom);
                    }
                }
            }else{
                for(SgServicioBasico esp:servicioBasicoList){
                        SgRelEdificioServicio relEspCom=new SgRelEdificioServicio();
                        relEspCom.setResServicio(esp);
                        edificioServicio.add(relEspCom);
                }
            }         
                                  
           Collections.sort(edificioServicio, (o1, o2) -> o1.getResServicio().getSbaNombre().compareTo(o2.getResServicio().getSbaNombre()));  
            
          

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }
    
     public void cargarEdificioServicioSanitario() {
        try {
           
            edificioServicioSanitario= new ArrayList();
            edificioServicioSanitarioSeleccionados=new ArrayList();
            List<SgTipoServicioSanitario> espacioComunList = new ArrayList();  
                
            FiltroTipoServicioSanitario filtro = new FiltroTipoServicioSanitario();
            filtro.setHabilitado(Boolean.TRUE);      
            filtro.setTssAplicaEdificio(Boolean.TRUE);
            espacioComunList = catalogosRestClient.buscarTipoServicioSanitario(filtro);
            
            FiltroRelEdificioServicioSanitario fic=new FiltroRelEdificioServicioSanitario();
            fic.setEdificioFk(entidadEnEdicion.getEdiPk());
            edificioServicioSanitarioSeleccionados=relEdificioServicioSanitarioRestClient.buscar(fic);
            
            if(!edificioServicioSanitarioSeleccionados.isEmpty()){
                edificioServicioSanitario.addAll(edificioServicioSanitarioSeleccionados);
                List<SgTipoServicioSanitario> espaciosSeleccionados=edificioServicioSanitario.stream().map(c->c.getRetServicioSanitario()).collect(Collectors.toList());
                for(SgTipoServicioSanitario esp:espacioComunList){
                    if(!espaciosSeleccionados.contains(esp)){
                        SgRelEdificioServicioSanitario relEspCom=new SgRelEdificioServicioSanitario();
                        relEspCom.setRetServicioSanitario(esp);
                        edificioServicioSanitario.add(relEspCom);
                    }
                }
            }else{
                for(SgTipoServicioSanitario esp:espacioComunList){
                        SgRelEdificioServicioSanitario relEspCom=new SgRelEdificioServicioSanitario();
                        relEspCom.setRetServicioSanitario(esp);
                        edificioServicioSanitario.add(relEspCom);
                }
            }         
                                  
           Collections.sort(edificioServicioSanitario, (o1, o2) -> o1.getRetServicioSanitario().getTssNombre().compareTo(o2.getRetServicioSanitario().getTssNombre()));  
            
           habilitarEntradaTextoServicioSanitario=new Boolean[edificioServicioSanitario.size()];
           Arrays.fill(habilitarEntradaTextoServicioSanitario,Boolean.FALSE);
           
           for(SgRelEdificioServicioSanitario relEsp:edificioServicioSanitarioSeleccionados){
               habilitarEntradaTextoServicioSanitario[edificioServicioSanitario.indexOf(relEsp)]=Boolean.TRUE;
           }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }
    
    
    
    

    public void guardar() {
        try {           
            entidadEnEdicion.setEdiRelEdificioServicioSanitario(new ArrayList());
            entidadEnEdicion.getEdiRelEdificioServicioSanitario().addAll(edificioServicioSanitarioSeleccionados);  
            entidadEnEdicion.setEdiRelEdificioServicio(new ArrayList());
            entidadEnEdicion.getEdiRelEdificioServicio().addAll(edificioServicioSeleccionados);          
          
            edificioRestClient.guardarListaServiciosBasicos(entidadEnEdicion);  
           
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
             cargarEdificioServicio();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }   
    
    public void checkboxSelect(SelectEvent selectEvent){
        SgRelEdificioServicioSanitario relImpSelect = (SgRelEdificioServicioSanitario)selectEvent.getObject();
        habilitarEntradaTextoServicioSanitario[edificioServicioSanitario.indexOf(relImpSelect)]=Boolean.TRUE; 
        relImpSelect.setRetBueno(null);
        relImpSelect.setRetMalo(null);
        relImpSelect.setRetRegular(null);
        relImpSelect.setRetNinos(null);
        relImpSelect.setRetNinas(null);
        relImpSelect.setRetAdministrativos(null);
        relImpSelect.setRetMaestros(null);
    }
    
    public void checkboxUnselect(UnselectEvent selectEvent){
        SgRelEdificioServicioSanitario relImpSelect = (SgRelEdificioServicioSanitario)selectEvent.getObject();
        habilitarEntradaTextoServicioSanitario[edificioServicioSanitario.indexOf(relImpSelect)]=Boolean.FALSE;   
        relImpSelect.setRetBueno(null);
        relImpSelect.setRetMalo(null);
        relImpSelect.setRetRegular(null);
        relImpSelect.setRetNinos(null);
        relImpSelect.setRetNinas(null);
        relImpSelect.setRetAdministrativos(null);
        relImpSelect.setRetMaestros(null);
    }
    
    public void toggleSelect(ToggleSelectEvent selectEvent){
        if(selectEvent.isSelected()){
            Arrays.fill(habilitarEntradaTextoServicioSanitario,Boolean.TRUE);
        }else{
            Arrays.fill(habilitarEntradaTextoServicioSanitario,Boolean.FALSE);
            edificioServicioSanitario.stream().forEach(c->{ c.setRetBueno(null);c.setRetMalo(null);c.setRetRegular(null);c.setRetNinos(null);c.setRetNinas(null);c.setRetAdministrativos(null);c.setRetMaestros(null);} );
        }
    }

    public SgEdificio getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEdificio entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public List<SgRelEdificioServicio> getEdificioServicio() {
        return edificioServicio;
    }

    public void setEdificioServicio(List<SgRelEdificioServicio> edificioServicio) {
        this.edificioServicio = edificioServicio;
    }

    public List<SgRelEdificioServicio> getEdificioServicioSeleccionados() {
        return edificioServicioSeleccionados;
    }

    public void setEdificioServicioSeleccionados(List<SgRelEdificioServicio> edificioServicioSeleccionados) {
        this.edificioServicioSeleccionados = edificioServicioSeleccionados;
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

    public List<SgRelEdificioServicioSanitario> getEdificioServicioSanitario() {
        return edificioServicioSanitario;
    }

    public void setEdificioServicioSanitario(List<SgRelEdificioServicioSanitario> edificioServicioSanitario) {
        this.edificioServicioSanitario = edificioServicioSanitario;
    }

    public List<SgRelEdificioServicioSanitario> getEdificioServicioSanitarioSeleccionados() {
        return edificioServicioSanitarioSeleccionados;
    }

    public void setEdificioServicioSanitarioSeleccionados(List<SgRelEdificioServicioSanitario> edificioServicioSanitarioSeleccionados) {
        this.edificioServicioSanitarioSeleccionados = edificioServicioSanitarioSeleccionados;
    }

    public Boolean[] getHabilitarEntradaTextoServicioSanitario() {
        return habilitarEntradaTextoServicioSanitario;
    }

    public void setHabilitarEntradaTextoServicioSanitario(Boolean[] habilitarEntradaTextoServicioSanitario) {
        this.habilitarEntradaTextoServicioSanitario = habilitarEntradaTextoServicioSanitario;
    }


}
