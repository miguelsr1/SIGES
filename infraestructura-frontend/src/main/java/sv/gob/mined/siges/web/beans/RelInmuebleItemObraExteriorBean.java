/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import sv.gob.mined.siges.web.dto.SgAuxiliarObraExterior;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgRelInmuebleItemObraExterior;
import sv.gob.mined.siges.web.dto.catalogo.SgInfItemObraExterior;
import sv.gob.mined.siges.web.dto.catalogo.SgInfObraExterior;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroRelInmuebleItemObraExterior;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.InmueblesSedesRestClient;
import sv.gob.mined.siges.web.restclient.RelInmuebleItemObraExteriorRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RelInmuebleItemObraExteriorBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RelInmuebleItemObraExteriorBean.class.getName());

    @Inject
    private InmueblesSedesRestClient restClient;   
    
    
    @Inject
    private RelInmuebleItemObraExteriorRestClient rInmuebleItemObraExteriorRestClient;

    
    @Inject
    private CatalogosRestClient catalogoRestClient;

    private FiltroRelInmuebleItemObraExterior filtro = new FiltroRelInmuebleItemObraExterior();
    private SgInmueblesSedes entidadEnEdicion = new SgInmueblesSedes();
    private List<SgRelInmuebleItemObraExterior> historialRelInmuebleItemObraExterior = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    
    private Boolean soloLectura = Boolean.FALSE;
    private String securityOperation;
    private List<SgInfObraExterior> listObraExterior=new ArrayList();
    private List<SgAuxiliarObraExterior> listAuxiliarObra=new ArrayList(); 
    private Boolean[][] habilitarEntradaTexto;


    public RelInmuebleItemObraExteriorBean() {
    }

    @PostConstruct
    public void init() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            entidadEnEdicion = (SgInmueblesSedes) request.getAttribute("inmueble");
            securityOperation = (String) request.getAttribute("securityOperation");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean)request.getAttribute("soloLectura")) : soloLectura ;
            cargarTablas();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = rInmuebleItemObraExteriorRestClient.buscarTotal(filtro);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    

    public void cargarTablas() {
        try{
            FiltroCodiguera fc=new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            List<SgInfItemObraExterior> listItemObra=catalogoRestClient.buscarItemObraExterior(fc);
            listAuxiliarObra=new ArrayList(); 
            
            listObraExterior=new ArrayList();
            for(SgInfItemObraExterior item:listItemObra){
                if(!listObraExterior.contains(item.getIoeObraExterior())){
                    listObraExterior.add(item.getIoeObraExterior());
                }
            }
            if(!listObraExterior.isEmpty()){
                habilitarEntradaTexto=new Boolean[listObraExterior.size()][];
                FiltroRelInmuebleItemObraExterior fri=new FiltroRelInmuebleItemObraExterior();
                fri.setInmuebleFk(entidadEnEdicion.getTisPk());
                List<SgRelInmuebleItemObraExterior> listRelInmuebleItem=rInmuebleItemObraExteriorRestClient.buscar(fri);
                for(SgInfObraExterior obra:listObraExterior){                    
                  
                    List<SgRelInmuebleItemObraExterior> listRelInmuebleItemObra=new ArrayList();
                    List<SgRelInmuebleItemObraExterior> listRelInmuebleItemObraSeleccionado=new ArrayList();
                    
                    List<SgInfItemObraExterior> listItems=listItemObra.stream().filter(c->c.getIoeObraExterior().getOexPk().equals(obra.getOexPk())).collect(Collectors.toList());
                    habilitarEntradaTexto[listObraExterior.indexOf(obra)] =new Boolean[listItems.size()];
                    Arrays.fill(habilitarEntradaTexto[listObraExterior.indexOf(obra)], Boolean.FALSE);
                    for(SgInfItemObraExterior itemObra:listItems){
                        
                        List<SgRelInmuebleItemObraExterior> elementoExistente=listRelInmuebleItem.stream().filter(c->c.getRixItemObraExterior().getIoePk().equals(itemObra.getIoePk())).collect(Collectors.toList());
                        if(!elementoExistente.isEmpty()){
                            listRelInmuebleItemObra.add(elementoExistente.get(0));
                            listRelInmuebleItemObraSeleccionado.add(elementoExistente.get(0));
                            habilitarEntradaTexto[listObraExterior.indexOf(obra)][listRelInmuebleItemObra.indexOf(elementoExistente.get(0))]=Boolean.TRUE;
                        }else{
                            SgRelInmuebleItemObraExterior relItem=new SgRelInmuebleItemObraExterior();
                            relItem.setRixItemObraExterior(itemObra);
                            listRelInmuebleItemObra.add(relItem);
                        }
                        
                    }
                    Collections.sort(listRelInmuebleItemObra, (o1,o2)-> (o1.getRixItemObraExterior().getIoeOrden()!= null ? o1.getRixItemObraExterior().getIoeOrden()  : 0) - (o2.getRixItemObraExterior().getIoeOrden() != null ? o2.getRixItemObraExterior().getIoeOrden()  : 0));
            
                    SgAuxiliarObraExterior aux=new SgAuxiliarObraExterior();
                    aux.setRixItemObraExterior(new ArrayList());
                    aux.getRixItemObraExterior().addAll(listRelInmuebleItemObra);
                    aux.setRixItemObraExteriorSeleccionado(new ArrayList());
                    aux.getRixItemObraExteriorSeleccionado().addAll(listRelInmuebleItemObraSeleccionado);
                    aux.setRixObra(obra);
                    listAuxiliarObra.add(aux);
                }
                Collections.sort(listAuxiliarObra, (o1,o2)-> (o1.getRixObra().getOexOrden()!= null ? o1.getRixObra().getOexOrden()  : 0) - (o2.getRixObra().getOexOrden() != null ? o2.getRixObra().getOexOrden()  : 0));
                
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void checkboxSelect(SelectEvent selectEvent){
        SgRelInmuebleItemObraExterior relImpSelect = (SgRelInmuebleItemObraExterior)selectEvent.getObject();
        List<SgAuxiliarObraExterior> auxList=listAuxiliarObra.stream().filter(c->c.getRixObra().getOexPk().equals(relImpSelect.getRixItemObraExterior().getIoeObraExterior().getOexPk())).collect(Collectors.toList());
        SgAuxiliarObraExterior aux=auxList.get(0);
        habilitarEntradaTexto[listAuxiliarObra.indexOf(aux)][aux.getRixItemObraExterior().indexOf(relImpSelect)]=Boolean.TRUE; 
        relImpSelect.setRixBueno(null);
        relImpSelect.setRixMalo(null);
        relImpSelect.setRixRegular(null);
        relImpSelect.setRixDescripcion(null);

    }
    
    public void checkboxUnselect(UnselectEvent selectEvent){
        SgRelInmuebleItemObraExterior relImpSelect = (SgRelInmuebleItemObraExterior)selectEvent.getObject();
        List<SgAuxiliarObraExterior> auxList=listAuxiliarObra.stream().filter(c->c.getRixObra().getOexPk().equals(relImpSelect.getRixItemObraExterior().getIoeObraExterior().getOexPk())).collect(Collectors.toList());
        SgAuxiliarObraExterior aux=auxList.get(0);
        habilitarEntradaTexto[listAuxiliarObra.indexOf(aux)][aux.getRixItemObraExterior().indexOf(relImpSelect)]=Boolean.FALSE; 
        relImpSelect.setRixBueno(null);
        relImpSelect.setRixMalo(null);
        relImpSelect.setRixRegular(null);
        relImpSelect.setRixDescripcion(null);
    }
    
    public void toggleSelect(ToggleSelectEvent selectEvent) {
        String idComponent = selectEvent.getComponent().getId();
        Map<String,String> par_map = new HashMap<String,String>(); 
        par_map=FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String index=par_map.get(selectEvent.getComponent().getNamingContainer().getParent().getNamingContainer().getClientId()+":index");
        
        if (selectEvent.isSelected()) {
                    Arrays.fill(habilitarEntradaTexto[Integer.valueOf(index)], Boolean.TRUE);
                } else {
                    Arrays.fill(habilitarEntradaTexto[Integer.valueOf(index)], Boolean.FALSE);
                    listAuxiliarObra.get(Integer.valueOf(index)).getRixItemObraExterior().stream().forEach(c -> {
                        c.setRixBueno(null);
                        c.setRixMalo(null);
                        c.setRixRegular(null);
                        c.setRixDescripcion(null);
                    });
                }
        
        
    }

    public void limpiar() {
        filtro = new FiltroRelInmuebleItemObraExterior();
        buscar();
    }


    public void guardar() {
        try {
            
            entidadEnEdicion.setTisObraExterior(new ArrayList());
            for(SgAuxiliarObraExterior aux: listAuxiliarObra){        
                entidadEnEdicion.getTisObraExterior().addAll(aux.getRixItemObraExteriorSeleccionado());
            }
            entidadEnEdicion = restClient.guardarListaObraExterior(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }




    public FiltroRelInmuebleItemObraExterior getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroRelInmuebleItemObraExterior filtro) {
        this.filtro = filtro;
    }

    public SgInmueblesSedes getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgInmueblesSedes entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgInfObraExterior> getListObraExterior() {
        return listObraExterior;
    }

    public void setListObraExterior(List<SgInfObraExterior> listObraExterior) {
        this.listObraExterior = listObraExterior;
    }

 

    public List<SgRelInmuebleItemObraExterior> getHistorialRelInmuebleItemObraExterior() {
        return historialRelInmuebleItemObraExterior;
    }

    public void setHistorialRelInmuebleItemObraExterior(List<SgRelInmuebleItemObraExterior> historialRelInmuebleItemObraExterior) {
        this.historialRelInmuebleItemObraExterior = historialRelInmuebleItemObraExterior;
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

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }


    public List<SgAuxiliarObraExterior> getListAuxiliarObra() {
        return listAuxiliarObra;
    }

    public void setListAuxiliarObra(List<SgAuxiliarObraExterior> listAuxiliarObra) {
        this.listAuxiliarObra = listAuxiliarObra;
    }

    public Boolean[][] getHabilitarEntradaTexto() {
        return habilitarEntradaTexto;
    }

    public void setHabilitarEntradaTexto(Boolean[][] habilitarEntradaTexto) {
        this.habilitarEntradaTexto = habilitarEntradaTexto;
    }    
    

}
