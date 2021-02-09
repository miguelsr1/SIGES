/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
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
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.SedeRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgProcesoLegalizacion;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoInmueble;
import sv.gob.mined.siges.web.dto.catalogo.SgEstadoProcesoLegalizacion;
import sv.gob.mined.siges.web.dto.catalogo.SgInfNaturalezaContrato;
import sv.gob.mined.siges.web.dto.catalogo.SgInfTipoAbastecimiento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstadoProcesoLegalizacion;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.InmueblesSedesRestClient;
import sv.gob.mined.siges.web.restclient.VulnerabilidadesRestClient;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class LegalizacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(LegalizacionBean.class.getName());

    @Inject
    private SedeRestClient restSede;

    @Inject
    private SessionBean sessionBean;

    @Inject
    private VulnerabilidadesRestClient vulnerabilidadesRestClient;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private InmueblesSedesRestClient restClient;
    
    @Inject
    private InmuebleSedesBean inmuebleSedeBean;
    
    
    private SgInmueblesSedes entidadEnEdicion = new SgInmueblesSedes();   
    
 
  
    private Boolean soloLectura = Boolean.FALSE;
    private Boolean callingPostConstruct;
    private String securityOperation;

    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<SgEstadoInmueble> comboEstadoInmueble;
    private SgProcesoLegalizacion procesoLegalizacionEnEdicion;
    
    private SofisComboG<SgEstadoProcesoLegalizacion> comboEstado;
    private SofisComboG<SgInfNaturalezaContrato> comboNaturalezaContrato;
    public LegalizacionBean() {
        
    }
    
    
    @PostConstruct
    public void init() {
        try {
            cargarCombo();        
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
            cargarEstado();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarCombo(){
        try{
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            List<SgEstadoInmueble> est=catalogosRestClient.buscarEstadoInmueble(fc);
            comboEstadoInmueble= new SofisComboG(est, "einNombre");
            comboEstadoInmueble.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            FiltroEstadoProcesoLegalizacion fepl= new FiltroEstadoProcesoLegalizacion();
            fepl.setHabilitado(Boolean.TRUE);
            fepl.setOrderBy(new String[]{"eplNombre"});
            fepl.setAscending(new boolean[]{true});
            List<SgEstadoProcesoLegalizacion> estadoProcesoLegalizacion=catalogosRestClient.buscarEstadoProcesoLegalizacion(fepl);
            comboEstado= new SofisComboG(estadoProcesoLegalizacion.stream().filter(c->c.getEplAplicaDatoPresentacion().equals(Boolean.TRUE)).collect(Collectors.toList()), "eplNombre");
            comboEstado.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
       
            
            fc.setOrderBy(new String[]{"nacNombre"});
            fc.setAscending(new boolean[]{true});
            List<SgInfNaturalezaContrato> listNaturaleza=catalogosRestClient.buscarNaturalezaContrato(fc);
            comboNaturalezaContrato= new SofisComboG(listNaturaleza, "nacNombre");
            comboNaturalezaContrato.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
  
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void limpiarCombos(){
        comboEstado.setSelected(-1);
        comboNaturalezaContrato.setSelected(-1);
    }
    
    public void cargarEstado(){
        procesoLegalizacionEnEdicion=new SgProcesoLegalizacion();
        if(entidadEnEdicion.getTisPk()!=null){
            if(entidadEnEdicion.getTisEstadoInmueble()!=null){
                comboEstadoInmueble.setSelectedT(entidadEnEdicion.getTisEstadoInmueble());
            }
            procesoLegalizacionEnEdicion=entidadEnEdicion.getTisProcesoLegalizacion();
            if(procesoLegalizacionEnEdicion!=null){
                if(procesoLegalizacionEnEdicion.getPlePk()!=null){
                    comboEstado.setSelectedT(procesoLegalizacionEnEdicion.getPleEstado());
                    comboNaturalezaContrato.setSelectedT(procesoLegalizacionEnEdicion.getPleNaturalezaContrato());
                }
            }else{
                procesoLegalizacionEnEdicion=new SgProcesoLegalizacion();
            }
            
        }
    }
    
    public void comboEstadoSelected(){
        entidadEnEdicion.setTisEstadoInmueble(comboEstadoInmueble.getSelectedT());
        
    }    

    
    public void guardar() {
        try {           
            if(entidadEnEdicion.getTisPk()!=null){
                if(entidadEnEdicion.getTisEstadoInmueble()!=null){
                    if(BooleanUtils.isTrue(entidadEnEdicion.getTisEstadoInmueble().getEinHabilitaDatosInscripcion())){
                        procesoLegalizacionEnEdicion.setPleEstado(comboEstado.getSelectedT());
                        procesoLegalizacionEnEdicion.setPleNaturalezaContrato(comboNaturalezaContrato.getSelectedT());
                        entidadEnEdicion.setTisProcesoLegalizacion(procesoLegalizacionEnEdicion);
                    }else{
                        procesoLegalizacionEnEdicion.setPleMatriculaAntecedente(null);
                        procesoLegalizacionEnEdicion.setPleAsientoAntecedente(null);
                        procesoLegalizacionEnEdicion.setPleNumeroAntecedente(null);
                        procesoLegalizacionEnEdicion.setPleLibroAntecedente(null);
                        procesoLegalizacionEnEdicion.setPleNumeroInscripcionLibro(null);
                        procesoLegalizacionEnEdicion.setPleLibroInscripcionLibro(null);
                        procesoLegalizacionEnEdicion.setPleFechaInscripcionLibro(null);
                        procesoLegalizacionEnEdicion.setPleMatriculaInscripcionMatricula(null);
                        procesoLegalizacionEnEdicion.setPleAsientoInscripcionMatricula(null);
                        procesoLegalizacionEnEdicion.setPleFechaInscripcionMatricula(null);
                        procesoLegalizacionEnEdicion.setPleObservaciones(null);
                        entidadEnEdicion.setTisProcesoLegalizacion(procesoLegalizacionEnEdicion);
                    }
                }else{                    
                        entidadEnEdicion.setTisProcesoLegalizacion(null);
                        procesoLegalizacionEnEdicion=new SgProcesoLegalizacion();
                        limpiarCombos();                    
                }
                
                //Esta pestaña no tiene el dato tipoAbastecimiento, al mandar a guardar la entidad este se pierde, por eso se guarda antes de enviar
                List<SgInfTipoAbastecimiento> tipoAbs=entidadEnEdicion.getTisTipoAbastecimiento();
                entidadEnEdicion = restClient.guardar(entidadEnEdicion); 
                entidadEnEdicion.setTisTipoAbastecimiento(tipoAbs);
                if(entidadEnEdicion.getTisProcesoLegalizacion()!=null){
                    procesoLegalizacionEnEdicion=entidadEnEdicion.getTisProcesoLegalizacion();
                }
                inmuebleSedeBean.setEntidadEnEdicion(entidadEnEdicion);
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            }          
        
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


    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
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

    public SofisComboG<SgEstadoInmueble> getComboEstadoInmueble() {
        return comboEstadoInmueble;
    }

    public void setComboEstadoInmueble(SofisComboG<SgEstadoInmueble> comboEstadoInmueble) {
        this.comboEstadoInmueble = comboEstadoInmueble;
    }

    public SgProcesoLegalizacion getProcesoLegalizacionEnEdicion() {
        return procesoLegalizacionEnEdicion;
    }

    public void setProcesoLegalizacionEnEdicion(SgProcesoLegalizacion procesoLegalizacionEnEdicion) {
        this.procesoLegalizacionEnEdicion = procesoLegalizacionEnEdicion;
    }

    public SofisComboG<SgEstadoProcesoLegalizacion> getComboEstado() {
        return comboEstado;
    }

    public void setComboEstado(SofisComboG<SgEstadoProcesoLegalizacion> comboEstado) {
        this.comboEstado = comboEstado;
    }

    public SofisComboG<SgInfNaturalezaContrato> getComboNaturalezaContrato() {
        return comboNaturalezaContrato;
    }

    public void setComboNaturalezaContrato(SofisComboG<SgInfNaturalezaContrato> comboNaturalezaContrato) {
        this.comboNaturalezaContrato = comboNaturalezaContrato;
    }




}
