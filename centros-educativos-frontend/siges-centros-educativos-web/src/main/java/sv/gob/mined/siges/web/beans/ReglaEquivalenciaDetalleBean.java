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
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.omnifaces.cdi.Param;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgGrado;
import sv.gob.mined.siges.web.dto.SgPlanEstudio;
import sv.gob.mined.siges.web.dto.SgReglaEquivalencia;
import sv.gob.mined.siges.web.dto.SgReglaEquivalenciaDetalle;
import sv.gob.mined.siges.web.enumerados.EnumOperacionReglaEquivalencia;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroPlanEstudio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroReglaEquivalenciaDetalle;
import sv.gob.mined.siges.web.lazymodels.LazyReglasEquivalenciaDetalleDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.GradoRestClient;
import sv.gob.mined.siges.web.restclient.PlanesEstudioRestClient;
import sv.gob.mined.siges.web.restclient.ReglaEquivalenciaDetalleRestClient;
import sv.gob.mined.siges.web.restclient.ReglasEquivalenciaRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

@Named
@ViewScoped
public class ReglaEquivalenciaDetalleBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ReglaEquivalenciaDetalleBean.class.getName());

    @Inject
    @Param(name = "id")
    private Long reglaId;  
    
    @Inject
    @Param(name = "edit")
    private Boolean editable;
    
    @Inject
    private SeleccionarGradoBean grado;
    
    @Inject
    private ReglasEquivalenciaRestClient reglasQuivalenciaRestClient;
    
    @Inject
    private GradoRestClient gradoRestClient;
    
    @Inject
    private PlanesEstudioRestClient planesEstudioRestClient;

    @Inject
    private ReglaEquivalenciaDetalleRestClient reglaEquivalenciaDetalleRestClient;
    
    @Inject
    private SessionBean sessionBean;
    
    private Boolean soloLectura;
    
    private SgGrado gradoSeleccionado;
    
    private FiltroReglaEquivalenciaDetalle filtro = new FiltroReglaEquivalenciaDetalle();
    
    private SgReglaEquivalencia reglaEquivalenciaEdicion;
    
    private SgReglaEquivalenciaDetalle entidadEnEdicion = new SgReglaEquivalenciaDetalle();
    
    private Integer paginadoOperacionIzquierda = 10;
    private Integer paginadoOperacionDerecha = 10;

    private Long totalReglasIzquierdo;
    private Long totalReglasDerecho;
    private Long operacionRealizada;
    
    private Boolean habilitado;
    
    private List<RevHistorico> historialRegla = new ArrayList();
    
    private SofisComboG<SgPlanEstudio> comboPlanEsudioInsert = new SofisComboG();;
    
    private LazyReglasEquivalenciaDetalleDataModel reglasEquivalenciaDetalleIzquierdoDataModel;
    private LazyReglasEquivalenciaDetalleDataModel reglasEquivalenciaDetalleDerechoDataModel;
    
    List<SgReglaEquivalenciaDetalle> listaEquivalenciasIzquierda = new ArrayList();
    List<SgReglaEquivalenciaDetalle> listaEquivalenciasDerecha = new ArrayList();
    
    public ReglaEquivalenciaDetalleBean() {
    }

    @PostConstruct
    public void init() {
        try {
            if (reglaId != null && reglaId > 0) {
                this.actualizar(reglasQuivalenciaRestClient.obtenerPorId(reglaId));
                soloLectura = editable != null ? !editable : soloLectura;
            } else {
                JSFUtils.redirectToIndex();
            }
            validarAcceso();
            buscar();
            
        } catch (HttpClientException ce) {
            JSFUtils.redirectNotFound();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    public String getTituloPagina() {
        if(this.reglaEquivalenciaEdicion!=null){
            if (this.soloLectura) {
                return Etiquetas.getValue("visualizarReglaEquivalencia")+ " " + reglaEquivalenciaEdicion.getReqNombre();
            } else {
                return Etiquetas.getValue("edicionReglaEquivalencia")+ " " + reglaEquivalenciaEdicion.getReqNombre();
            }
        }else{
            return "";
        }
    }
    
    public void actualizar(SgReglaEquivalencia req) {
        try {
            if(req == null) {
                JSFUtils.redirectToIndex();
            } else {
                reglaEquivalenciaEdicion = req;
            }
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void actualizar(SgReglaEquivalenciaDetalle red) {
        try {
            if(red == null) {
                JSFUtils.redirectToIndex();
            } else {
                entidadEnEdicion = reglaEquivalenciaDetalleRestClient.obtenerPorId(red.getRedPk());
                if(entidadEnEdicion != null) {
                    if(comboPlanEsudioInsert!=null){
                        comboPlanEsudioInsert.setSelectedT(entidadEnEdicion.getRedPlanEstudio());
                    }
                    
                    grado.getComboOrganizacionCurricular().setSelectedT(entidadEnEdicion.getRedGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel().getNivOrganizacionCurricular());
                    grado.seleccionarOrganizacionCurricular();
                    grado.getComboNivel().setSelectedT(entidadEnEdicion.getRedGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo().getCicNivel());
                    grado.seleccionarNivel();
                    grado.getComboCiclo().setSelectedT(entidadEnEdicion.getRedGrado().getGraRelacionModalidad().getReaModalidadEducativa().getModCiclo());
                    grado.seleccionarCiclo();
                    
                    if(entidadEnEdicion.getRedGrado().getGraRelacionModalidad().getReaModalidadEducativa() != null) {
                        grado.getComboModalidad().setSelectedT(entidadEnEdicion.getRedGrado().getGraRelacionModalidad().getReaModalidadEducativa());
                        grado.seleccionarModalidad();
                    }
                    
                    if(entidadEnEdicion.getRedOpcion()!=null){
                        grado.getComboOpcion().setSelectedT(entidadEnEdicion.getRedOpcion());
                        grado.seleccionarOpcion();
                    }
                    
                    if(entidadEnEdicion.getRedProgramaEducativo()!=null){
                        grado.getComboProgramaEducativo().setSelectedT(entidadEnEdicion.getRedProgramaEducativo());
                        grado.seleccionarProgramaEducativo();
                    }
                    
                    if(entidadEnEdicion.getRedGrado().getGraRelacionModalidad().getReaModalidadAtencion() != null) {
                        grado.getComboModalidadAtencion().setSelectedT(entidadEnEdicion.getRedGrado().getGraRelacionModalidad().getReaModalidadAtencion());
                        grado.seleccionarModalidadAtencion();
                    }
                    
                    if(entidadEnEdicion.getRedGrado().getGraRelacionModalidad().getReaSubModalidadAtencion() != null) {
                       grado.getComboSubModalidadAtencion().setSelectedT(entidadEnEdicion.getRedGrado().getGraRelacionModalidad().getReaSubModalidadAtencion());
                       grado.seleccionarSubModalidadAtencion(); 
                    }
                    
                    grado.getComboGrado().setSelectedT(entidadEnEdicion.getRedGrado());
                    gradoSeleccionado = entidadEnEdicion.getRedGrado();
                    cargarCombos();
                    
                    if(entidadEnEdicion.getRedPlanEstudio()!=null && comboPlanEsudioInsert!=null){
                        comboPlanEsudioInsert.setSelectedT(entidadEnEdicion.getRedPlanEstudio());
                    }
                   
                }
                
            }
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void actualizarParaEliminar(SgReglaEquivalenciaDetalle red) {
        try {
            if(red == null) {
                JSFUtils.redirectToIndex();
            } else {
                entidadEnEdicion = reglaEquivalenciaDetalleRestClient.obtenerPorId(red.getRedPk());
            }
            JSFUtils.limpiarMensajesError();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void buscar() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        
        listaEquivalenciasIzquierda.clear();
        listaEquivalenciasDerecha.clear();
        
        filtro = new FiltroReglaEquivalenciaDetalle();    
        filtro.setReglaNormativaId(reglaId);
        
        List<SgReglaEquivalenciaDetalle> listaEquivalencias = reglaEquivalenciaDetalleRestClient.buscar(filtro);
        for(SgReglaEquivalenciaDetalle regla : listaEquivalencias) {
            if(regla.getRedOperacion() != null) {
                if(EnumOperacionReglaEquivalencia.ORIGEN.equals(regla.getRedOperacion())) {
                    listaEquivalenciasIzquierda.add(regla);
                } else if(EnumOperacionReglaEquivalencia.DESTINO.equals(regla.getRedOperacion())) {
                    listaEquivalenciasDerecha.add(regla);
                }
            }
        }
    }
    
    public void cargarCombos() {
        try{
            if(gradoSeleccionado!=null){
                FiltroPlanEstudio filtroPlanEstudio = new FiltroPlanEstudio();
                
                filtroPlanEstudio.setNivel(grado.getComboNivel().getSelectedT().getNivPk());
                filtroPlanEstudio.setCiclo(grado.getComboCiclo().getSelectedT().getCicPk());
                filtroPlanEstudio.setModalidadEducativa(grado.getComboModalidad().getSelectedT().getModPk());
                filtroPlanEstudio.setModalidadAtencion(grado.getComboModalidadAtencion().getSelectedT().getMatPk());
                filtroPlanEstudio.setSubModalidadAtencion((grado.getComboSubModalidadAtencion()!=null && grado.getComboSubModalidadAtencion().getSelectedT()!=null) ?grado.getComboSubModalidadAtencion().getSelectedT().getSmoPk():null);
                filtroPlanEstudio.setOpcion((grado.getComboOpcion()!=null && grado.getComboOpcion().getSelectedT()!=null)?grado.getComboOpcion().getSelectedT().getOpcPk():null);
                filtroPlanEstudio.setProgramaEducativo((grado.getComboProgramaEducativo()!=null && grado.getComboProgramaEducativo().getSelectedT()!=null)?grado.getComboProgramaEducativo().getSelectedT().getPedPk():null);
                
                filtroPlanEstudio.setVigente(Boolean.TRUE);
                filtroPlanEstudio.setHabilitado(true);
                filtroPlanEstudio.setOrderBy(new String[]{"pesNombre"});
                filtroPlanEstudio.setAscending(new boolean[]{true});
                filtroPlanEstudio.setIncluirCampos(new String[]{"pesNombre", "pesVersion"});   
                List<SgPlanEstudio> listaPlanEstudios = planesEstudioRestClient.buscar(filtroPlanEstudio);
                comboPlanEsudioInsert = new SofisComboG<>(listaPlanEstudios,"pesNombre");
            }else{
                comboPlanEsudioInsert = new SofisComboG();
            }
            comboPlanEsudioInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            PrimeFaces.current().ajax().update("form:itemDetail");
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void agregar(EnumOperacionReglaEquivalencia operacion) {
        entidadEnEdicion = new SgReglaEquivalenciaDetalle();
        entidadEnEdicion.setRedOperacion(operacion);
        entidadEnEdicion.setRedGrado(null);
        gradoSeleccionado = null;
        grado.limpiarCombos();
    }
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REGLAS_EQUIVALENCIAS)) {
            JSFUtils.redirectToIndex();
        }
    }
    
    public void guardar() {
        try {
            entidadEnEdicion.setRedGrado(grado.getComboGrado().getSelectedT());
            entidadEnEdicion.setRedPlanEstudio(comboPlanEsudioInsert.getSelectedT() != null ? comboPlanEsudioInsert.getSelectedT() : null);
            entidadEnEdicion.setRedReglaEquivalencia(reglaEquivalenciaEdicion);
            entidadEnEdicion.setRedOpcion(grado.getComboOpcion()!=null?grado.getComboOpcion().getSelectedT():null);
            entidadEnEdicion.setRedProgramaEducativo(grado.getComboProgramaEducativo()!=null?grado.getComboProgramaEducativo().getSelectedT():null);
             
            entidadEnEdicion = reglaEquivalenciaDetalleRestClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void eliminar() {
        try {
            reglaEquivalenciaDetalleRestClient.eliminar(entidadEnEdicion.getRedPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void historial(Long id) {
        try {
           historialRegla = reglaEquivalenciaDetalleRestClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void seleccionarGrado(SgGrado var){
        this.gradoSeleccionado = var;
        cargarCombos();
    }
    
    public SgReglaEquivalencia getReglaEquivalenciaEdicion() {
        return reglaEquivalenciaEdicion;
    }

    public void setReglaEquivalenciaEdicion(SgReglaEquivalencia reglaEquivalenciaEdicion) {
        this.reglaEquivalenciaEdicion = reglaEquivalenciaEdicion;
    }

    public SgReglaEquivalenciaDetalle getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgReglaEquivalenciaDetalle entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }  

    public Integer getPaginadoOperacionIzquierda() {
        return paginadoOperacionIzquierda;
    }

    public void setPaginadoOperacionIzquierda(Integer paginadoOperacionIzquierda) {
        this.paginadoOperacionIzquierda = paginadoOperacionIzquierda;
    }

    public Integer getPaginadoOperacionDerecha() {
        return paginadoOperacionDerecha;
    }

    public void setPaginadoOperacionDerecha(Integer paginadoOperacionDerecha) {
        this.paginadoOperacionDerecha = paginadoOperacionDerecha;
    }

    public SofisComboG<SgPlanEstudio> getComboPlanEsudioInsert() {
        return comboPlanEsudioInsert;
    }

    public void setComboPlanEsudioInsert(SofisComboG<SgPlanEstudio> comboPlanEsudioInsert) {
        this.comboPlanEsudioInsert = comboPlanEsudioInsert;
    }

    public Boolean getSoloLectura() {
        return this.soloLectura || !sessionBean.getOperaciones().contains(ConstantesOperaciones.ACTUALIZAR_REGLA_EQUIVALENCIA);  
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public FiltroReglaEquivalenciaDetalle getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroReglaEquivalenciaDetalle filtro) {
        this.filtro = filtro;
    }

    public Long getTotalReglasIzquierdo() {
        return totalReglasIzquierdo;
    }

    public void setTotalReglasIzquierdo(Long totalReglasIzquierdo) {
        this.totalReglasIzquierdo = totalReglasIzquierdo;
    }

    public Long getTotalReglasDerecho() {
        return totalReglasDerecho;
    }

    public void setTotalReglasDerecho(Long totalReglasDerecho) {
        this.totalReglasDerecho = totalReglasDerecho;
    }

    public Long getReglaId() {
        return reglaId;
    }

    public void setReglaId(Long reglaId) {
        this.reglaId = reglaId;
    }

    public ReglasEquivalenciaRestClient getReglasQuivalenciaRestClient() {
        return reglasQuivalenciaRestClient;
    }

    public void setReglasQuivalenciaRestClient(ReglasEquivalenciaRestClient reglasQuivalenciaRestClient) {
        this.reglasQuivalenciaRestClient = reglasQuivalenciaRestClient;
    }

    public GradoRestClient getGradoRestClient() {
        return gradoRestClient;
    }

    public void setGradoRestClient(GradoRestClient gradoRestClient) {
        this.gradoRestClient = gradoRestClient;
    }

    public PlanesEstudioRestClient getPlanesEstudioRestClient() {
        return planesEstudioRestClient;
    }

    public void setPlanesEstudioRestClient(PlanesEstudioRestClient planesEstudioRestClient) {
        this.planesEstudioRestClient = planesEstudioRestClient;
    }

    public ReglaEquivalenciaDetalleRestClient getReglaEquivalenciaDetalleRestClient() {
        return reglaEquivalenciaDetalleRestClient;
    }

    public void setReglaEquivalenciaDetalleRestClient(ReglaEquivalenciaDetalleRestClient reglaEquivalenciaDetalleRestClient) {
        this.reglaEquivalenciaDetalleRestClient = reglaEquivalenciaDetalleRestClient;
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public Long getOperacionRealizada() {
        return operacionRealizada;
    }

    public void setOperacionRealizada(Long operacionRealizada) {
        this.operacionRealizada = operacionRealizada;
    }

    public LazyReglasEquivalenciaDetalleDataModel getReglasEquivalenciaDetalleIzquierdoDataModel() {
        return reglasEquivalenciaDetalleIzquierdoDataModel;
    }

    public void setReglasEquivalenciaDetalleIzquierdoDataModel(LazyReglasEquivalenciaDetalleDataModel reglasEquivalenciaDetalleIzquierdoDataModel) {
        this.reglasEquivalenciaDetalleIzquierdoDataModel = reglasEquivalenciaDetalleIzquierdoDataModel;
    }

    public LazyReglasEquivalenciaDetalleDataModel getReglasEquivalenciaDetalleDerechoDataModel() {
        return reglasEquivalenciaDetalleDerechoDataModel;
    }

    public void setReglasEquivalenciaDetalleDerechoDataModel(LazyReglasEquivalenciaDetalleDataModel reglasEquivalenciaDetalleDerechoDataModel) {
        this.reglasEquivalenciaDetalleDerechoDataModel = reglasEquivalenciaDetalleDerechoDataModel;
    }

    public List<RevHistorico> getHistorialRegla() {
        return historialRegla;
    }

    public void setHistorialRegla(List<RevHistorico> historialRegla) {
        this.historialRegla = historialRegla;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public List<SgReglaEquivalenciaDetalle> getListaEquivalenciasDerecha() {
        return listaEquivalenciasDerecha;
    }

    public void setListaEquivalenciasDerecha(List<SgReglaEquivalenciaDetalle> listaEquivalenciasDerecha) {
        this.listaEquivalenciasDerecha = listaEquivalenciasDerecha;
    }

    public List<SgReglaEquivalenciaDetalle> getListaEquivalenciasIzquierda() {
        return listaEquivalenciasIzquierda;
    }

    public void setListaEquivalenciasIzquierda(List<SgReglaEquivalenciaDetalle> listaEquivalenciasIzquierda) {
        this.listaEquivalenciasIzquierda = listaEquivalenciasIzquierda;
    }

    public SgGrado getGradoSeleccionado() {
        return gradoSeleccionado;
    }

    public void setGradoSeleccionado(SgGrado gradoSeleccionado) {
        this.gradoSeleccionado = gradoSeleccionado;
    }
}
