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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgRelInmuebleServicioBasico;
import sv.gob.mined.siges.web.dto.catalogo.SgServicioBasico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleServicio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroServicioBasico;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.InmueblesSedesRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleServicioBasicoRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RelInmuebleOtroServicioBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RelInmuebleOtroServicioBean.class.getName());

    @Inject
    private SessionBean sessionBean;


    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private InmueblesSedesRestClient restClient;
    
    @Inject
    private RelInmuebleServicioBasicoRestClient relInmueblesServicioBasicoRestClient;
    
    @Inject
    private InmuebleSedesBean inmuebleSedeBean;
    
    
    private SgInmueblesSedes entidadEnEdicion = new SgInmueblesSedes();
    
    
    private SgSede sedeSeleccionada;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean callingPostConstruct;
    private String securityOperation;

    private List<SgRelInmuebleServicioBasico> inmueblesServicio= new ArrayList();
    private List<SgRelInmuebleServicioBasico> inmueblesServicioSeleccionados= new ArrayList();
    
    private Integer paginado = 10;
    private Long totalResultados;
    private Boolean beanIniciado=Boolean.FALSE;
    private Boolean[] habilitarEntradaTextoServicioSanitario;   

 

    public RelInmuebleOtroServicioBean() {
        
    }
    
    
    @PostConstruct
    public void init() {
        try {
           
            
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgInmueblesSedes) request.getAttribute("inmueble");
            securityOperation = (String) request.getAttribute("securityOperation");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean)request.getAttribute("soloLectura")) : soloLectura ;
            cargarEntidad();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }
    

    public void cargarEntidad() {
        try {        
  
            cargarInmueblesServicio();
            beanIniciado=Boolean.TRUE;
        
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    

    
    
    public void cargarInmueblesServicio() {
        try {
           
            inmueblesServicio= new ArrayList();
            inmueblesServicioSeleccionados=new ArrayList();
            List<SgServicioBasico> espacioComunList = new ArrayList();  
            List<SgRelInmuebleServicioBasico> listAuxiliar = new ArrayList();   
                
            FiltroServicioBasico filtro = new FiltroServicioBasico();
            filtro.setHabilitado(Boolean.TRUE);      
            filtro.setAplicaInmueble(Boolean.TRUE);
            espacioComunList = catalogosRestClient.buscarServicioBasico(filtro);
            
            FiltroRelInmuebleServicio fic=new FiltroRelInmuebleServicio();
            fic.setInmuebleFk(entidadEnEdicion.getTisPk());
            inmueblesServicioSeleccionados=relInmueblesServicioBasicoRestClient.buscar(fic);
            
            if(!inmueblesServicioSeleccionados.isEmpty()){
                inmueblesServicio.addAll(inmueblesServicioSeleccionados);
                List<SgServicioBasico> espaciosSeleccionados=inmueblesServicio.stream().map(c->c.getRisServicio()).collect(Collectors.toList());
                for(SgServicioBasico esp:espacioComunList){
                    if(!espaciosSeleccionados.contains(esp)){
                        SgRelInmuebleServicioBasico relEspCom=new SgRelInmuebleServicioBasico();
                        relEspCom.setRisServicio(esp);
                        inmueblesServicio.add(relEspCom);
                    }
                }
            }else{
                for(SgServicioBasico esp:espacioComunList){
                        SgRelInmuebleServicioBasico relEspCom=new SgRelInmuebleServicioBasico();
                        relEspCom.setRisServicio(esp);
                        inmueblesServicio.add(relEspCom);
                }
            }         
                                  
           Collections.sort(inmueblesServicio, (o1, o2) -> o1.getRisServicio().getSbaNombre().compareTo(o2.getRisServicio().getSbaNombre()));  
            
       

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }
    
    

    public void guardar() {
        try {           
                entidadEnEdicion.setTisServicioBasico(new ArrayList());
                entidadEnEdicion.getTisServicioBasico().addAll(inmueblesServicioSeleccionados);               
               
          
            SgInmueblesSedes sed= restClient.guardarListaOtroServiciosBasicos(entidadEnEdicion);  
            inmuebleSedeBean.getEntidadEnEdicion().setTisVersion(sed.getTisVersion());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            cargarInmueblesServicio();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }   
    

   


    public SgInmueblesSedes getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgInmueblesSedes entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
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

    public List<SgRelInmuebleServicioBasico> getInmueblesServicio() {
        return inmueblesServicio;
    }

    public void setInmueblesServicio(List<SgRelInmuebleServicioBasico> inmueblesServicio) {
        this.inmueblesServicio = inmueblesServicio;
    }

    public List<SgRelInmuebleServicioBasico> getInmueblesServicioSeleccionados() {
        return inmueblesServicioSeleccionados;
    }

    public void setInmueblesServicioSeleccionados(List<SgRelInmuebleServicioBasico> inmueblesServicioSeleccionados) {
        this.inmueblesServicioSeleccionados = inmueblesServicioSeleccionados;
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


    public Boolean[] getHabilitarEntradaTextoServicioSanitario() {
        return habilitarEntradaTextoServicioSanitario;
    }

    public void setHabilitarEntradaTextoServicioSanitario(Boolean[] habilitarEntradaTextoServicioSanitario) {
        this.habilitarEntradaTextoServicioSanitario = habilitarEntradaTextoServicioSanitario;
    }

    public InmuebleSedesBean getInmuebleSedeBean() {
        return inmuebleSedeBean;
    }

    public void setInmuebleSedeBean(InmuebleSedesBean inmuebleSedeBean) {
        this.inmuebleSedeBean = inmuebleSedeBean;
    }

 


}
