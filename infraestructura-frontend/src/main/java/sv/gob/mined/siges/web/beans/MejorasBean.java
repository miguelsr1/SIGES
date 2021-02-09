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
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgEdificio;
import sv.gob.mined.siges.web.dto.SgInmueblesSedes;
import sv.gob.mined.siges.web.dto.SgMejoras;
import sv.gob.mined.siges.web.dto.catalogo.SgFuenteFinanciamiento;
import sv.gob.mined.siges.web.dto.catalogo.SgTipoMejora;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEdificio;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMejora;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTipoMejora;
import sv.gob.mined.siges.web.lazymodels.LazyMejorasDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.EdificioRestClient;
import sv.gob.mined.siges.web.restclient.MejorasRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MejorasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MejorasBean.class.getName());

    @Inject
    private MejorasRestClient restClient;
    
    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    @Inject
    private EdificioRestClient edificioRestClient;

    private FiltroMejora filtro = new FiltroMejora();
    private SgInmueblesSedes inmuebleEnEdicion=new SgInmueblesSedes();
    private SgMejoras entidadEnEdicion = new SgMejoras();
    private List<RevHistorico> historialMejoras = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyMejorasDataModel mejorasLazyModel;
    private Boolean soloLectura = Boolean.FALSE;
    private String securityOperation;
    private SofisComboG<SgTipoMejora> comboTipoMejora;
    private SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento;
    private SofisComboG<SgTipoMejora> comboTipoMejoraFiltro;
    private SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamientoFiltro;
    private SofisComboG<SgEdificio> comboEdificio;
    private SofisComboG<SgEdificio> comboEdificioFiltro;


    public MejorasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            inmuebleEnEdicion = (SgInmueblesSedes) request.getAttribute("inmueble");
            securityOperation = (String) request.getAttribute("securityOperation");
            soloLectura = request.getAttribute("soloLectura") != null ? ((Boolean)request.getAttribute("soloLectura")) : soloLectura ;
            cargarCombos();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            
            filtro.setInmueble(inmuebleEnEdicion.getTisPk());
            filtro.setFuenteFinanciamiento(comboFuenteFinanciamientoFiltro.getSelectedT()!=null?comboFuenteFinanciamientoFiltro.getSelectedT().getFfiPk():null);
            filtro.setTipoMejora(comboTipoMejoraFiltro.getSelectedT()!=null?comboTipoMejoraFiltro.getSelectedT().getTmePk():null);
            filtro.setEdificio(comboEdificioFiltro.getSelectedT()!=null?comboEdificioFiltro.getSelectedT().getEdiPk():null);
            totalResultados = restClient.buscarTotal(filtro);
            mejorasLazyModel = new LazyMejorasDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try{
        FiltroTipoMejora ftm= new FiltroTipoMejora();
        ftm.setAplicaInmueble(Boolean.TRUE);
        List<SgTipoMejora> listtipoMejora=catalogosRestClient.buscarTipoMejora(ftm);
        comboTipoMejora = new SofisComboG(listtipoMejora, "tmeNombre");
        comboTipoMejora.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboTipoMejoraFiltro = new SofisComboG(listtipoMejora, "tmeNombre");
        comboTipoMejoraFiltro.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));       
                
        
        
        FiltroCodiguera fc=new FiltroCodiguera();
        fc.setOrderBy(new String[]{"ffiNombre"});
        fc.setAscending(new boolean[]{true});
        List<SgFuenteFinanciamiento> listFuente = catalogosRestClient.buscarFuenteFinanciamiento(fc);
        comboFuenteFinanciamiento= new SofisComboG(listFuente, "ffiNombre");
        comboFuenteFinanciamiento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        comboFuenteFinanciamientoFiltro= new SofisComboG(listFuente, "ffiNombre");
        comboFuenteFinanciamientoFiltro.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        
        FiltroEdificio fil = new FiltroEdificio();
        fil.setInmuebleId(inmuebleEnEdicion.getTisPk());
        fil.setIncluirCampos(new String[]{"ediPk", "ediNombre","ediCantidadNiveles", "ediVersion"});
        List<SgEdificio> listEdificio=edificioRestClient.buscar(fil);
        comboEdificio= new SofisComboG(listEdificio, "ediNombre");
        comboEdificio.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));    
        comboEdificioFiltro= new SofisComboG(listEdificio, "ediNombre");
        comboEdificioFiltro.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));   
        
        } catch (Exception ex) {
        LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    private void limpiarCombos() {
        comboTipoMejora.setSelected(-1);
        comboFuenteFinanciamiento.setSelected(-1);
        comboEdificio.setSelected(-1);
    }

    public void limpiar() {
        filtro = new FiltroMejora();
        comboTipoMejoraFiltro.setSelected(-1);
        comboFuenteFinanciamientoFiltro.setSelected(-1);
        comboEdificioFiltro.setSelected(-1);
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgMejoras();
    }

    public void actualizar(SgMejoras var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgMejoras) SerializationUtils.clone(var);
        comboTipoMejora.setSelectedT(var.getMejTipoMejora());
        comboFuenteFinanciamiento.setSelectedT(var.getMejFuenteFinanciamiento());
        comboEdificio.setSelectedT(entidadEnEdicion.getMejEdificio());
    }

    public void guardar() {
        try {
            entidadEnEdicion.setMejFuenteFinanciamiento(comboFuenteFinanciamiento.getSelectedT());
            entidadEnEdicion.setMejTipoMejora(comboTipoMejora.getSelectedT());
            entidadEnEdicion.setMejInmueble(inmuebleEnEdicion);
            entidadEnEdicion.setMejEdificio(comboEdificio.getSelectedT());
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialogMejora').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getMejPk());
            buscar();
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public List<SgEdificio> completeEdificio(String query) {
        try {
            FiltroEdificio fil = new FiltroEdificio();
            fil.setInmuebleId(inmuebleEnEdicion.getTisPk());
            fil.setCodigo(query);
            fil.setMaxResults(11L);
            fil.setIncluirCampos(new String[]{"ediPk", "ediCodigo","ediCantidadNiveles", "ediVersion"});
            return edificioRestClient.buscar(fil);
        } catch (HttpClientException ce) {
            LOGGER.log(Level.SEVERE, ce.getMessage(), ce);
            return new ArrayList<>();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void historial(Long id) {
        try {
            historialMejoras = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroMejora getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroMejora filtro) {
        this.filtro = filtro;
    }

    public SgMejoras getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMejoras entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialMejoras() {
        return historialMejoras;
    }

    public void setHistorialMejoras(List<RevHistorico> historialMejoras) {
        this.historialMejoras = historialMejoras;
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

    public LazyMejorasDataModel getMejorasLazyModel() {
        return mejorasLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyMejorasDataModel mejorasLazyModel) {
        this.mejorasLazyModel = mejorasLazyModel;
    }

    public SgInmueblesSedes getInmuebleEnEdicion() {
        return inmuebleEnEdicion;
    }

    public void setInmuebleEnEdicion(SgInmueblesSedes inmuebleEnEdicion) {
        this.inmuebleEnEdicion = inmuebleEnEdicion;
    }

    public Boolean getSoloLectura() {
        return soloLectura;
    }

    public void setSoloLectura(Boolean soloLectura) {
        this.soloLectura = soloLectura;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public SofisComboG<SgTipoMejora> getComboTipoMejora() {
        return comboTipoMejora;
    }

    public void setComboTipoMejora(SofisComboG<SgTipoMejora> comboTipoMejora) {
        this.comboTipoMejora = comboTipoMejora;
    }

    public SofisComboG<SgFuenteFinanciamiento> getComboFuenteFinanciamiento() {
        return comboFuenteFinanciamiento;
    }

    public void setComboFuenteFinanciamiento(SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamiento) {
        this.comboFuenteFinanciamiento = comboFuenteFinanciamiento;
    }

    public SofisComboG<SgTipoMejora> getComboTipoMejoraFiltro() {
        return comboTipoMejoraFiltro;
    }

    public void setComboTipoMejoraFiltro(SofisComboG<SgTipoMejora> comboTipoMejoraFiltro) {
        this.comboTipoMejoraFiltro = comboTipoMejoraFiltro;
    }

    public SofisComboG<SgFuenteFinanciamiento> getComboFuenteFinanciamientoFiltro() {
        return comboFuenteFinanciamientoFiltro;
    }

    public void setComboFuenteFinanciamientoFiltro(SofisComboG<SgFuenteFinanciamiento> comboFuenteFinanciamientoFiltro) {
        this.comboFuenteFinanciamientoFiltro = comboFuenteFinanciamientoFiltro;
    }

    public SofisComboG<SgEdificio> getComboEdificio() {
        return comboEdificio;
    }

    public void setComboEdificio(SofisComboG<SgEdificio> comboEdificio) {
        this.comboEdificio = comboEdificio;
    }

    public SofisComboG<SgEdificio> getComboEdificioFiltro() {
        return comboEdificioFiltro;
    }

    public void setComboEdificioFiltro(SofisComboG<SgEdificio> comboEdificioFiltro) {
        this.comboEdificioFiltro = comboEdificioFiltro;
    }
    
    

}
