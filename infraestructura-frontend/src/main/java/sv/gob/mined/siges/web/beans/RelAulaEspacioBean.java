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
import sv.gob.mined.siges.web.dto.SgAula;
import sv.gob.mined.siges.web.dto.SgRelAulaEspacio;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgEspacioComun;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEspacioComplementario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelAulaEspacio;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.AulaRestClient;
import sv.gob.mined.siges.web.restclient.RelAulaEspacioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RelAulaEspacioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RelAulaEspacioBean.class.getName());

    @Inject
    private RelAulaEspacioRestClient restClient;
    
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

    private List<SgRelAulaEspacio> aulaEspacio= new ArrayList();
    private List<SgRelAulaEspacio> aulaEspacioSeleccionados= new ArrayList();
    private List<SgRelAulaEspacio> aulaEspacioAntesDeCambio= new ArrayList(); 
    private Boolean beanIniciado=Boolean.FALSE;
    private Boolean[] habilitarEntradaTexto;

    public RelAulaEspacioBean() {
        
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
                cargarAulaEspacio();
                beanIniciado=Boolean.TRUE;
           }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
  



    
    public void cargarAulaEspacio() {
        try {
           
            aulaEspacio= new ArrayList();
            aulaEspacioSeleccionados=new ArrayList();
            aulaEspacioAntesDeCambio= new ArrayList(); 
            List<SgEspacioComun> espacioComunList = new ArrayList();  
            
            FiltroEspacioComplementario filtro = new FiltroEspacioComplementario();
            filtro.setHabilitado(Boolean.TRUE);      
            filtro.setAplicaAula(Boolean.TRUE);
            espacioComunList = catalogosRestClient.buscarEspacioComun(filtro);
            
            FiltroRelAulaEspacio fic=new FiltroRelAulaEspacio();
            fic.setRaeAulaFk(entidadEnEdicion.getAulPk());
            aulaEspacioSeleccionados=restClient.buscar(fic);
            
            if(!aulaEspacioSeleccionados.isEmpty()){
                aulaEspacio.addAll(aulaEspacioSeleccionados);
                List<SgEspacioComun> espaciosSeleccionados=aulaEspacio.stream().map(c->c.getRaeEspacioComun()).collect(Collectors.toList());
                for(SgEspacioComun esp:espacioComunList){
                    if(!espaciosSeleccionados.contains(esp)){
                        SgRelAulaEspacio relEspCom=new SgRelAulaEspacio();
                        relEspCom.setRaeEspacioComun(esp);
                        aulaEspacio.add(relEspCom);
                    }
                }
            }else{
                for(SgEspacioComun esp:espacioComunList){
                        SgRelAulaEspacio relEspCom=new SgRelAulaEspacio();
                        relEspCom.setRaeEspacioComun(esp);
                        aulaEspacio.add(relEspCom);
                }
            }         
                                  
           Collections.sort(aulaEspacio, (o1, o2) -> o1.getRaeEspacioComun().getEcoNombre().compareTo(o2.getRaeEspacioComun().getEcoNombre()));  
            habilitarEntradaTexto=new Boolean[aulaEspacio.size()];
           Arrays.fill(habilitarEntradaTexto,Boolean.FALSE);
           
           for(SgRelAulaEspacio relEsp:aulaEspacioSeleccionados){
               habilitarEntradaTexto[aulaEspacio.indexOf(relEsp)]=Boolean.TRUE;
           }
          

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }
    

    public void guardar() {
        try {           
           
            entidadEnEdicion.setAulRelAulaEspacio(new ArrayList());
            entidadEnEdicion.getAulRelAulaEspacio().addAll(aulaEspacioSeleccionados);          
          
            aulaRestClient.guardarListaEspacioComunes(entidadEnEdicion);  
           
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
             cargarAulaEspacio();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }   
    
    public void checkboxSelect(SelectEvent selectEvent){
        SgRelAulaEspacio relImpSelect = (SgRelAulaEspacio)selectEvent.getObject();
        habilitarEntradaTexto[aulaEspacio.indexOf(relImpSelect)]=Boolean.TRUE; 
        relImpSelect.setRaeBueno(null);
        relImpSelect.setRaeMalo(null);
        relImpSelect.setRaeRegular(null);
        relImpSelect.setRaeDescripcion(null);

    }
    
    public void checkboxUnselect(UnselectEvent selectEvent){
        SgRelAulaEspacio relImpSelect = (SgRelAulaEspacio)selectEvent.getObject();
        habilitarEntradaTexto[aulaEspacio.indexOf(relImpSelect)]=Boolean.FALSE;   
        relImpSelect.setRaeBueno(null);
        relImpSelect.setRaeMalo(null);
        relImpSelect.setRaeRegular(null);
        relImpSelect.setRaeDescripcion(null);
    }
    
    public void toggleSelect(ToggleSelectEvent selectEvent){
        if(selectEvent.isSelected()){
            Arrays.fill(habilitarEntradaTexto,Boolean.TRUE);
        }else{
            Arrays.fill(habilitarEntradaTexto,Boolean.FALSE);
            aulaEspacio.stream().forEach(c->{c.setRaeBueno(null);c.setRaeMalo(null);c.setRaeRegular(null);c.setRaeDescripcion(null);} );
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

    public List<SgRelAulaEspacio> getAulaEspacio() {
        return aulaEspacio;
    }

    public void setAulaEspacio(List<SgRelAulaEspacio> aulaEspacio) {
        this.aulaEspacio = aulaEspacio;
    }

    public List<SgRelAulaEspacio> getAulaEspacioSeleccionados() {
        return aulaEspacioSeleccionados;
    }

    public void setAulaEspacioSeleccionados(List<SgRelAulaEspacio> aulaEspacioSeleccionados) {
        this.aulaEspacioSeleccionados = aulaEspacioSeleccionados;
    }

    public List<SgRelAulaEspacio> getAulaEspacioAntesDeCambio() {
        return aulaEspacioAntesDeCambio;
    }

    public void setAulaEspacioAntesDeCambio(List<SgRelAulaEspacio> aulaEspacioAntesDeCambio) {
        this.aulaEspacioAntesDeCambio = aulaEspacioAntesDeCambio;
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

    public Boolean[] getHabilitarEntradaTexto() {
        return habilitarEntradaTexto;
    }

    public void setHabilitarEntradaTexto(Boolean[] habilitarEntradaTexto) {
        this.habilitarEntradaTexto = habilitarEntradaTexto;
    }


}
