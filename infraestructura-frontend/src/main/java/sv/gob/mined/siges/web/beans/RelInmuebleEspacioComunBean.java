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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgRelInmuebleEspacioComun;
import sv.gob.mined.siges.web.dto.catalogo.SgEspacioComun;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEspacioComplementario;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleEspacioComun;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.InmueblesSedesRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleEspacioCoumnRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RelInmuebleEspacioComunBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RelInmuebleEspacioComunBean.class.getName());

    @Inject
    private SessionBean sessionBean;


    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private InmueblesSedesRestClient restClient;
    
    @Inject
    private RelInmuebleEspacioCoumnRestClient relInmueblesEspacioComunRestClient;
    
    @Inject
    private InmuebleSedesBean inmuebleSedeBean;
    
    
    private SgInmueblesSedes entidadEnEdicion = new SgInmueblesSedes();
    
    
    private SgSede sedeSeleccionada;
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean callingPostConstruct;
    private String securityOperation;

    private List<SgRelInmuebleEspacioComun> inmueblesEspacio= new ArrayList();
    private List<SgRelInmuebleEspacioComun> inmueblesEspacioSeleccionados= new ArrayList();
    private List<SgRelInmuebleEspacioComun> inmueblesEspacioAntesDeCambio= new ArrayList(); 
    private Integer paginado = 10;
    private Long totalResultados;
    private Boolean beanIniciado=Boolean.FALSE;
    private Boolean[] habilitarEntradaTexto;
 

    public RelInmuebleEspacioComunBean() {
        
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
           if(!beanIniciado){
                cargarInmueblesEspacio();
                beanIniciado=Boolean.TRUE;
           }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
  



    
    public void cargarInmueblesEspacio() {
        try {
           
            inmueblesEspacio= new ArrayList();
            inmueblesEspacioSeleccionados=new ArrayList();
            inmueblesEspacioAntesDeCambio= new ArrayList(); 
            List<SgEspacioComun> espacioComunList = new ArrayList();  
            List<SgRelInmuebleEspacioComun> listAuxiliar = new ArrayList();   
            
            FiltroEspacioComplementario filtro = new FiltroEspacioComplementario();
            filtro.setHabilitado(Boolean.TRUE);      
            filtro.setAplicaInmueble(Boolean.TRUE);
            espacioComunList = catalogosRestClient.buscarEspacioComun(filtro);
            
            FiltroRelInmuebleEspacioComun fic=new FiltroRelInmuebleEspacioComun();
            fic.setInmuebleFk(entidadEnEdicion.getTisPk());
            inmueblesEspacioSeleccionados=relInmueblesEspacioComunRestClient.buscar(fic);
            
            if(!inmueblesEspacioSeleccionados.isEmpty()){
                inmueblesEspacio.addAll(inmueblesEspacioSeleccionados);
                List<SgEspacioComun> espaciosSeleccionados=inmueblesEspacio.stream().map(c->c.getRieEspacioComun()).collect(Collectors.toList());
                for(SgEspacioComun esp:espacioComunList){
                    if(!espaciosSeleccionados.contains(esp)){
                        SgRelInmuebleEspacioComun relEspCom=new SgRelInmuebleEspacioComun();
                        relEspCom.setRieEspacioComun(esp);
                        inmueblesEspacio.add(relEspCom);
                    }
                }
            }else{
                for(SgEspacioComun esp:espacioComunList){
                        SgRelInmuebleEspacioComun relEspCom=new SgRelInmuebleEspacioComun();
                        relEspCom.setRieEspacioComun(esp);
                        inmueblesEspacio.add(relEspCom);
                }
            }         
                                  
            Collections.sort(inmueblesEspacio, (o1,o2)-> (o1.getRieEspacioComun().getEcoOrden() != null ? o1.getRieEspacioComun().getEcoOrden()  : 0) - (o2.getRieEspacioComun().getEcoOrden() != null ? o2.getRieEspacioComun().getEcoOrden()  : 0));
           
            
           habilitarEntradaTexto=new Boolean[inmueblesEspacio.size()];
           Arrays.fill(habilitarEntradaTexto,Boolean.FALSE);
           
           for(SgRelInmuebleEspacioComun relEsp:inmueblesEspacioSeleccionados){
               habilitarEntradaTexto[inmueblesEspacio.indexOf(relEsp)]=Boolean.TRUE;
           }
           inmueblesEspacioAntesDeCambio=inmueblesEspacioSeleccionados;

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }
    

    public void guardar() {
        try {           
           
                entidadEnEdicion.setTisRelInmuebleEspacioComun(new ArrayList());
                entidadEnEdicion.getTisRelInmuebleEspacioComun().addAll(inmueblesEspacioSeleccionados);          
          
            restClient.guardarListaEspacioComunes(entidadEnEdicion);  
           
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
             cargarInmueblesEspacio();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }   
    
    public void checkboxSelect(SelectEvent selectEvent){
        SgRelInmuebleEspacioComun relImpSelect = (SgRelInmuebleEspacioComun)selectEvent.getObject();
        habilitarEntradaTexto[inmueblesEspacio.indexOf(relImpSelect)]=Boolean.TRUE; 
        relImpSelect.setRieBueno(null);
        relImpSelect.setRieMalo(null);
        relImpSelect.setRieRegular(null);
        relImpSelect.setRieDescripcion(null);

    }
    
    public void checkboxUnselect(UnselectEvent selectEvent){
        SgRelInmuebleEspacioComun relImpSelect = (SgRelInmuebleEspacioComun)selectEvent.getObject();
        habilitarEntradaTexto[inmueblesEspacio.indexOf(relImpSelect)]=Boolean.FALSE;    
        relImpSelect.setRieBueno(null);
        relImpSelect.setRieMalo(null);
        relImpSelect.setRieRegular(null);
        relImpSelect.setRieDescripcion(null);
    }
    
    public void toggleSelect(ToggleSelectEvent selectEvent){
        if(selectEvent.isSelected()){
            Arrays.fill(habilitarEntradaTexto,Boolean.TRUE);
        }else{
            Arrays.fill(habilitarEntradaTexto,Boolean.FALSE);
            inmueblesEspacio.stream().forEach(c->{c.setRieBueno(null);c.setRieMalo(null);c.setRieRegular(null);c.setRieDescripcion(null);} );
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

    public List<SgRelInmuebleEspacioComun> getInmueblesEspacio() {
        return inmueblesEspacio;
    }

    public void setInmueblesEspacio(List<SgRelInmuebleEspacioComun> inmueblesEspacio) {
        this.inmueblesEspacio = inmueblesEspacio;
    }

    public List<SgRelInmuebleEspacioComun> getInmueblesEspacioSeleccionados() {
        return inmueblesEspacioSeleccionados;
    }

    public void setInmueblesEspacioSeleccionados(List<SgRelInmuebleEspacioComun> inmueblesEspacioSeleccionados) {
        this.inmueblesEspacioSeleccionados = inmueblesEspacioSeleccionados;
    }

    public List<SgRelInmuebleEspacioComun> getInmueblesEspacioAntesDeCambio() {
        return inmueblesEspacioAntesDeCambio;
    }

    public void setInmueblesEspacioAntesDeCambio(List<SgRelInmuebleEspacioComun> inmueblesEspacioAntesDeCambio) {
        this.inmueblesEspacioAntesDeCambio = inmueblesEspacioAntesDeCambio;
    }

    public Boolean[] getHabilitarEntradaTexto() {
        return habilitarEntradaTexto;
    }

    public void setHabilitarEntradaTexto(Boolean[] habilitarEntradaTexto) {
        this.habilitarEntradaTexto = habilitarEntradaTexto;
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


}
