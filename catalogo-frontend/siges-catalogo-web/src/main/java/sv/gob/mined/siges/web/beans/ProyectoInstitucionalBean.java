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
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAsociacion;
import sv.gob.mined.siges.web.dto.SgBeneficio;
import sv.gob.mined.siges.web.dto.SgComponente;
import sv.gob.mined.siges.web.dto.SgProgramaInstitucional;
import sv.gob.mined.siges.web.dto.SgProyectoInstitucional;
import sv.gob.mined.siges.web.dto.SgTipoAsociacion;
import sv.gob.mined.siges.web.enumerados.EnumTipoBeneficio;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroAsociacion;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.AsociacionRestClient;
import sv.gob.mined.siges.web.restclient.BeneficioRestClient;
import sv.gob.mined.siges.web.restclient.ComponenteRestClient;
import sv.gob.mined.siges.web.restclient.ProgramaInstitucionalRestClient;
import sv.gob.mined.siges.web.restclient.ProyectoInstitucionalRestClient;
import sv.gob.mined.siges.web.restclient.TipoAsociacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class ProyectoInstitucionalBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ProyectoInstitucionalBean.class.getName());

    @Inject
    private ProyectoInstitucionalRestClient restClient;

    @Inject
    private TipoAsociacionRestClient tipoAsociacionRestClient;

    @Inject
    private AsociacionRestClient asociacionRestClient;
    
    @Inject
    private ProgramaInstitucionalRestClient restPorgramaInstitucional;
    
    @Inject
    private ComponenteRestClient restComponente;
    
    @Inject
    private BeneficioRestClient restBeneficio;

    @Inject
    @Param(name = "id")
    private Long pinId;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgProyectoInstitucional entidadEnEdicion = new SgProyectoInstitucional();
    private List<SgProyectoInstitucional> historialProyectoInstitucional = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private SofisComboG<SgTipoAsociacion> comboTipoAsociacion;
    private SofisComboG<SgAsociacion> comboAsociacion;
    private SgAsociacion asociacionEnEdicion;
    private SofisComboG<SgProgramaInstitucional> comboProgramaInstitucional;
    private SgComponente componenteEnEdicion = new SgComponente();
    private SgBeneficio beneficioEnEdicion = new SgBeneficio();
    private SofisComboG<EnumTipoBeneficio> comboTipoBeneficio;

    public ProyectoInstitucionalBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            if (pinId != null) {
                this.actualizar(restClient.obtenerPorId(pinId));
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            entidadEnEdicion = restClient.obtenerPorId(entidadEnEdicion!=null?entidadEnEdicion.getPinPk():null);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc = new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            fc.setAscending(new boolean[]{true});
            
            
            fc.setOrderBy(new String[]{"tasNombre"});
            List<SgTipoAsociacion> tiposAsociaciones = tipoAsociacionRestClient.buscar(fc);
            comboTipoAsociacion = new SofisComboG(tiposAsociaciones, "tasNombre");
            comboTipoAsociacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboAsociacion = new SofisComboG();
            comboAsociacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            
            fc.setOrderBy(new String[]{"pinNombre"});
            comboProgramaInstitucional = new SofisComboG(restPorgramaInstitucional.buscar(fc), "pinNombre");
            comboProgramaInstitucional.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            comboTipoBeneficio = new SofisComboG(new ArrayList(Arrays.asList(EnumTipoBeneficio.values())), "text");
            comboTipoBeneficio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarAsociaciones() {
        try {
            if (comboTipoAsociacion.getSelectedT() != null) {
                FiltroAsociacion fc = new FiltroAsociacion();
                fc.setTipoAsociacion(comboTipoAsociacion.getSelectedT().getTasPk());
                fc.setHabilitado(Boolean.TRUE);
                fc.setAscending(new boolean[]{true});
                fc.setOrderBy(new String[]{"asoNombre"});

                List<SgAsociacion> listAsociacion = asociacionRestClient.buscar(fc);

                comboAsociacion = new SofisComboG(listAsociacion, "asoNombre");
                comboAsociacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgProyectoInstitucional();
    }

    public void actualizar(SgProyectoInstitucional var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgProyectoInstitucional) SerializationUtils.clone(var);
        comboProgramaInstitucional.setSelectedT(entidadEnEdicion.getPinProgramaInstitucional());
    }

    public void guardar() {
        try {
            entidadEnEdicion.setPinProgramaInstitucional(comboProgramaInstitucional!=null?comboProgramaInstitucional.getSelectedT():null);
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getPinPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialProyectoInstitucional = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void agregarAsociacion() {
        JSFUtils.limpiarMensajesError();
        asociacionEnEdicion = new SgAsociacion();
        comboTipoAsociacion.setSelected(-1);
        comboAsociacion = new SofisComboG();
        comboAsociacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }

    public void editarAsociacion(SgAsociacion aso) {
        JSFUtils.limpiarMensajesError();
        comboTipoAsociacion.setSelectedT(aso.getAsoTipoAsociacion());
        cargarAsociaciones();
        comboAsociacion.setSelectedT(aso);
        asociacionEnEdicion=aso;
    }

    public void guardarAsociacion() {
        try {
            if (comboAsociacion.getSelectedT() != null) {
                if(entidadEnEdicion.getPinAsociaciones()==null){
                    entidadEnEdicion.setPinAsociaciones(new ArrayList<>());
                }
                if(!entidadEnEdicion.getPinAsociaciones().contains(comboAsociacion.getSelectedT())){
                    entidadEnEdicion.getPinAsociaciones().add(comboAsociacion.getSelectedT());
                    restClient.guardar(entidadEnEdicion);
                }                
  
                JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
                PrimeFaces.current().executeScript("PF('proyectoDialog').hide()");
            }else{
                JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_ASOCIACION_VACIO), "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarAsociacion() {
        try {
            entidadEnEdicion.getPinAsociaciones().remove(asociacionEnEdicion);
            restClient.guardar(entidadEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            asociacionEnEdicion=null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    
    
    

    public void agregarComponente() {
        JSFUtils.limpiarMensajesError();
        componenteEnEdicion = new SgComponente();
    }

    public void editarComponente(SgComponente com) {
        JSFUtils.limpiarMensajesError();
        componenteEnEdicion = com;
    }

    public void guardarComponente() {
        try {
            if(entidadEnEdicion.getPinComponentes()==null){
                entidadEnEdicion.setPinComponentes(new ArrayList<>());
            }
            componenteEnEdicion.setCpnProyectoInstitucional(entidadEnEdicion);
            if(!entidadEnEdicion.getPinComponentes().contains(componenteEnEdicion)){
                restComponente.guardar(componenteEnEdicion);
                entidadEnEdicion.getPinComponentes().add(componenteEnEdicion);
            }                
            PrimeFaces.current().executeScript("PF('componenteDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgComponente", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError("popupmsgComponente", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarComponente() {
        try {
            restComponente.eliminar(componenteEnEdicion.getCpnPk());
            entidadEnEdicion.getPinComponentes().remove(componenteEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    
    
    
    public void agregarBeneficio() {
        JSFUtils.limpiarMensajesError();
        beneficioEnEdicion = new SgBeneficio();
        comboTipoBeneficio.setSelected(-1);
    }

    public void editarBeneficio(SgBeneficio ben) {
        JSFUtils.limpiarMensajesError();
        beneficioEnEdicion = ben;
        comboTipoBeneficio.setSelectedT(beneficioEnEdicion.getBnfTipoBeneficio());
    }

    public void guardarBeneficio() {
        try {
            if(entidadEnEdicion.getPinBeneficios()==null){
                entidadEnEdicion.setPinBeneficios(new ArrayList<>());
            }
            beneficioEnEdicion.setBnfProyectoInstitucional(entidadEnEdicion);
            beneficioEnEdicion.setBnfTipoBeneficio(comboTipoBeneficio!=null?comboTipoBeneficio.getSelectedT():null);
            if(!entidadEnEdicion.getPinBeneficios().contains(beneficioEnEdicion)){
                restBeneficio.guardar(beneficioEnEdicion);
                entidadEnEdicion.getPinBeneficios().add(beneficioEnEdicion);
            }                

            PrimeFaces.current().executeScript("PF('beneficioDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes("popupmsgBeneficio", FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError("popupmsgBeneficio", Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminarBeneficio() {
        try {
            restBeneficio.eliminar(beneficioEnEdicion.getBnfPk());
            entidadEnEdicion.getPinBeneficios().remove(beneficioEnEdicion);
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgProyectoInstitucional getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgProyectoInstitucional entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgProyectoInstitucional> getHistorialProyectoInstitucional() {
        return historialProyectoInstitucional;
    }

    public void setHistorialProyectoInstitucional(List<SgProyectoInstitucional> historialProyectoInstitucional) {
        this.historialProyectoInstitucional = historialProyectoInstitucional;
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
    
    public SofisComboG<SgTipoAsociacion> getComboTipoAsociacion() {
        return comboTipoAsociacion;
    }

    public void setComboTipoAsociacion(SofisComboG<SgTipoAsociacion> comboTipoAsociacion) {
        this.comboTipoAsociacion = comboTipoAsociacion;
    }

    public SofisComboG<SgAsociacion> getComboAsociacion() {
        return comboAsociacion;
    }

    public void setComboAsociacion(SofisComboG<SgAsociacion> comboAsociacion) {
        this.comboAsociacion = comboAsociacion;
    }

    public SgAsociacion getAsociacionEnEdicion() {
        return asociacionEnEdicion;
    }

    public void setAsociacionEnEdicion(SgAsociacion asociacionEnEdicion) {
        this.asociacionEnEdicion = asociacionEnEdicion;
    }

    public SofisComboG<SgProgramaInstitucional> getComboProgramaInstitucional() {
        return comboProgramaInstitucional;
    }

    public void setComboProgramaInstitucional(SofisComboG<SgProgramaInstitucional> comboProgramaInstitucional) {
        this.comboProgramaInstitucional = comboProgramaInstitucional;
    }

    public SgComponente getComponenteEnEdicion() {
        return componenteEnEdicion;
    }

    public void setComponenteEnEdicion(SgComponente componenteEnEdicion) {
        this.componenteEnEdicion = componenteEnEdicion;
    }

    public SgBeneficio getBeneficioEnEdicion() {
        return beneficioEnEdicion;
    }

    public void setBeneficioEnEdicion(SgBeneficio beneficioEnEdicion) {
        this.beneficioEnEdicion = beneficioEnEdicion;
    }

    public SofisComboG<EnumTipoBeneficio> getComboTipoBeneficio() {
        return comboTipoBeneficio;
    }

    public void setComboTipoBeneficio(SofisComboG<EnumTipoBeneficio> comboTipoBeneficio) {
        this.comboTipoBeneficio = comboTipoBeneficio;
    }

}
