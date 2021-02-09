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
import sv.gob.mined.siges.web.dto.SgRelEdificioEspacio;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.dto.catalogo.SgEspacioComun;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEspacioComplementario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelEdificioEspacio;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EdificioRestClient;
import sv.gob.mined.siges.web.restclient.RelEdificioEspacioRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RelEdificioEspacioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RelEdificioEspacioBean.class.getName());

    @Inject
    private RelEdificioEspacioRestClient restClient;
    
    @Inject
    private EdificioRestClient edificioRestClient;

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

    private List<SgRelEdificioEspacio> edificioEspacio= new ArrayList();
    private List<SgRelEdificioEspacio> edificioEspacioSeleccionados= new ArrayList();
    private List<SgRelEdificioEspacio> edificioEspacioAntesDeCambio= new ArrayList(); 
    private Boolean beanIniciado=Boolean.FALSE;
    private Boolean[] habilitarEntradaTexto;

    public RelEdificioEspacioBean() {
        
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
                cargarEdificioEspacio();
                beanIniciado=Boolean.TRUE;
           }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
  



    
    public void cargarEdificioEspacio() {
        try {
           
            edificioEspacio= new ArrayList();
            edificioEspacioSeleccionados=new ArrayList();
            edificioEspacioAntesDeCambio= new ArrayList(); 
            List<SgEspacioComun> espacioComunList = new ArrayList();  
            
            FiltroEspacioComplementario filtro = new FiltroEspacioComplementario();
            filtro.setHabilitado(Boolean.TRUE);      
            filtro.setAplicaEdificio(Boolean.TRUE);
            espacioComunList = catalogosRestClient.buscarEspacioComun(filtro);
            
            FiltroRelEdificioEspacio fic=new FiltroRelEdificioEspacio();
            fic.setReeEdificioFk(entidadEnEdicion.getEdiPk());
            edificioEspacioSeleccionados=restClient.buscar(fic);
            
            if(!edificioEspacioSeleccionados.isEmpty()){
                edificioEspacio.addAll(edificioEspacioSeleccionados);
                List<SgEspacioComun> espaciosSeleccionados=edificioEspacio.stream().map(c->c.getReeEspacioComun()).collect(Collectors.toList());
                for(SgEspacioComun esp:espacioComunList){
                    if(!espaciosSeleccionados.contains(esp)){
                        SgRelEdificioEspacio relEspCom=new SgRelEdificioEspacio();
                        relEspCom.setReeEspacioComun(esp);
                        edificioEspacio.add(relEspCom);
                    }
                }
            }else{
                for(SgEspacioComun esp:espacioComunList){
                        SgRelEdificioEspacio relEspCom=new SgRelEdificioEspacio();
                        relEspCom.setReeEspacioComun(esp);
                        edificioEspacio.add(relEspCom);
                }
            }      
            
       
           Collections.sort(edificioEspacio, (o1,o2)-> (o1.getReeEspacioComun().getEcoOrden() != null ? o1.getReeEspacioComun().getEcoOrden()  : 0) - (o2.getReeEspacioComun().getEcoOrden() != null ? o2.getReeEspacioComun().getEcoOrden()  : 0));
           
           habilitarEntradaTexto=new Boolean[edificioEspacio.size()];
           Arrays.fill(habilitarEntradaTexto,Boolean.FALSE);
           
           for(SgRelEdificioEspacio relEsp:edificioEspacioSeleccionados){
               habilitarEntradaTexto[edificioEspacio.indexOf(relEsp)]=Boolean.TRUE;
           }
          

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }
    

    public void guardar() {
        try {           
           
            entidadEnEdicion.setEdiRelEdificioEspacio(new ArrayList());
            entidadEnEdicion.getEdiRelEdificioEspacio().addAll(edificioEspacioSeleccionados);          
          
            edificioRestClient.guardarListaEspacioComunes(entidadEnEdicion);  
           
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
             cargarEdificioEspacio();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }   
    
     public void checkboxSelect(SelectEvent selectEvent){
        SgRelEdificioEspacio relImpSelect = (SgRelEdificioEspacio)selectEvent.getObject();
        habilitarEntradaTexto[edificioEspacio.indexOf(relImpSelect)]=Boolean.TRUE; 
        relImpSelect.setReeBueno(null);
        relImpSelect.setReeMalo(null);
        relImpSelect.setReeRegular(null);
        relImpSelect.setReeDescripcion(null);

    }
    
    public void checkboxUnselect(UnselectEvent selectEvent){
        SgRelEdificioEspacio relImpSelect = (SgRelEdificioEspacio)selectEvent.getObject();
        habilitarEntradaTexto[edificioEspacio.indexOf(relImpSelect)]=Boolean.FALSE;   
        relImpSelect.setReeBueno(null);
        relImpSelect.setReeMalo(null);
        relImpSelect.setReeRegular(null);
        relImpSelect.setReeDescripcion(null);
    }
    
    public void toggleSelect(ToggleSelectEvent selectEvent){
        if(selectEvent.isSelected()){
            Arrays.fill(habilitarEntradaTexto,Boolean.TRUE);
        }else{
            Arrays.fill(habilitarEntradaTexto,Boolean.FALSE);
            edificioEspacio.stream().forEach(c->{ c.setReeBueno(null);c.setReeMalo(null);c.setReeRegular(null);c.setReeDescripcion(null);} );
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

    public List<SgRelEdificioEspacio> getEdificioEspacio() {
        return edificioEspacio;
    }

    public void setEdificioEspacio(List<SgRelEdificioEspacio> edificioEspacio) {
        this.edificioEspacio = edificioEspacio;
    }

    public List<SgRelEdificioEspacio> getEdificioEspacioSeleccionados() {
        return edificioEspacioSeleccionados;
    }

    public void setEdificioEspacioSeleccionados(List<SgRelEdificioEspacio> edificioEspacioSeleccionados) {
        this.edificioEspacioSeleccionados = edificioEspacioSeleccionados;
    }

    public List<SgRelEdificioEspacio> getEdificioEspacioAntesDeCambio() {
        return edificioEspacioAntesDeCambio;
    }

    public void setEdificioEspacioAntesDeCambio(List<SgRelEdificioEspacio> edificioEspacioAntesDeCambio) {
        this.edificioEspacioAntesDeCambio = edificioEspacioAntesDeCambio;
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
