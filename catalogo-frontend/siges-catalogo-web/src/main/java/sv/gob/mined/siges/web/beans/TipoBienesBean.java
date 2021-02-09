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
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.PrimeFaces;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAfCategoriaBienes;
import sv.gob.mined.siges.web.dto.SgAfClasificacionBienes;
import sv.gob.mined.siges.web.dto.SgAfTipoBienes;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaBienes;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroTipoBienes;
import sv.gob.mined.siges.web.lazymodels.LazyTipoBienesDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CategoriaBienesRestClient;
import sv.gob.mined.siges.web.restclient.ClasificacionBienesRestClient;
import sv.gob.mined.siges.web.restclient.TipoBienesRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class TipoBienesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ProyectosBean.class.getName());

    @Inject
    private TipoBienesRestClient restClient;
    
    @Inject
    private ClasificacionBienesRestClient clasificacionBienesRestClient;
    
    @Inject
    private CategoriaBienesRestClient categoriaBienesRestClient;
    
    private FiltroTipoBienes filtro = new FiltroTipoBienes();
    private SgAfTipoBienes entidadEnEdicion = new SgAfTipoBienes();
    private List<SgAfTipoBienes> historialTipoBienes = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyTipoBienesDataModel tipoBienesLazyModel;
    
    private SofisComboG<SgAfClasificacionBienes> comboClasificacion;
    private SofisComboG<SgAfClasificacionBienes> comboClasificacionInsert;
    private SofisComboG<SgAfCategoriaBienes> comboCategorias;
    private SofisComboG<SgAfCategoriaBienes> comboCategoriasInsert;
    
    
    
    public TipoBienesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            filtro.setClasificacionId(comboClasificacion.getSelectedT() != null ? comboClasificacion.getSelectedT().getCbiPk() : null);
            filtro.setCategoriaId(comboCategorias.getSelectedT() != null ? comboCategorias.getSelectedT().getCabPk() : null);
            totalResultados = restClient.buscarTotal(filtro);
            tipoBienesLazyModel = new LazyTipoBienesDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

     public void cargarCombos() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        FiltroCodiguera fc = new FiltroCodiguera();
        fc.setHabilitado(Boolean.TRUE);
        List<SgAfClasificacionBienes> lista = clasificacionBienesRestClient.buscar(fc);
 
        comboClasificacion = new SofisComboG(lista, "cbiNombre");
        comboClasificacion.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        
        comboClasificacionInsert = new SofisComboG(lista, "cbiNombre");
        comboClasificacionInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        
        comboCategorias = new SofisComboG<>();
        comboCategoriasInsert = new SofisComboG<>();
    }
    
    public void limpiar() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        cargarCombos();
        filtro = new FiltroTipoBienes();
        comboClasificacion.setSelected(-1);
        comboCategorias.setSelected(-1);
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        comboClasificacionInsert.setSelected(-1);
        comboCategoriasInsert.setSelected(-1);
        entidadEnEdicion = new SgAfTipoBienes();
    }

    public void actualizar(SgAfTipoBienes var) throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        JSFUtils.limpiarMensajesError();
        comboClasificacionInsert.setSelected(-1);
        comboCategoriasInsert.setSelected(-1);
        
        entidadEnEdicion = (SgAfTipoBienes) SerializationUtils.clone(var);

        FiltroCodiguera fc = new FiltroCodiguera();
        fc.setHabilitado(Boolean.TRUE);
        List<SgAfClasificacionBienes> lista = clasificacionBienesRestClient.buscar(fc);
        comboClasificacionInsert = new SofisComboG(lista, "cbiNombre");
        comboClasificacionInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        
        comboClasificacionInsert.setSelectedT(entidadEnEdicion.getTbiCategoriaBienes().getCabClasificacionBienes());
        
        cargarCategoriasInsert();
    }

    public void cargarCategorias() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if(comboClasificacion != null && comboClasificacion.getSelectedT() != null && comboClasificacion.getSelectedT().getCbiPk() != null) {
            FiltroCategoriaBienes filtro = new FiltroCategoriaBienes();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setClasificacionId(comboClasificacion.getSelectedT().getCbiPk());
            List<SgAfCategoriaBienes> listaCategoria = categoriaBienesRestClient.buscar(filtro);
            comboCategorias = new SofisComboG(listaCategoria, "cabNombre");
            comboCategorias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } else {
            comboCategorias = new SofisComboG();
            comboCategorias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        }
    }
    
    public void cargarCategoriasInsert() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        if(comboClasificacionInsert != null && comboClasificacionInsert.getSelectedT() != null && comboClasificacionInsert.getSelectedT().getCbiPk() != null) {
            FiltroCategoriaBienes filtro = new FiltroCategoriaBienes();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setClasificacionId(comboClasificacionInsert.getSelectedT().getCbiPk());
            List<SgAfCategoriaBienes> listaCategoria = categoriaBienesRestClient.buscar(filtro);
            comboCategoriasInsert = new SofisComboG(listaCategoria, "cabNombre");
            comboCategoriasInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            if(entidadEnEdicion.getTbiCategoriaBienes() != null) {
                comboCategoriasInsert.setSelectedT(entidadEnEdicion.getTbiCategoriaBienes());
            }
        } else {
            comboCategoriasInsert = new SofisComboG();
            comboCategoriasInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        }
    }
            
    public void guardar() {
        try {
            entidadEnEdicion.setTbiCategoriaBienes(comboCategoriasInsert.getSelectedT());
            restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
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
            restClient.eliminar(entidadEnEdicion.getTbiPk());
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
            historialTipoBienes = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroTipoBienes getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroTipoBienes filtro) {
        this.filtro = filtro;
    }

    public SgAfTipoBienes getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfTipoBienes entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgAfTipoBienes> getHistorialTipoBienes() {
        return historialTipoBienes;
    }

    public void setHistorialTipoBienes(List<SgAfTipoBienes> historialTipoBienes) {
        this.historialTipoBienes = historialTipoBienes;
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

    public LazyTipoBienesDataModel getTipoBienesLazyModel() {
        return tipoBienesLazyModel;
    }

    public void setTipoBienesLazyModel(LazyTipoBienesDataModel tipoBienesLazyModel) {
        this.tipoBienesLazyModel = tipoBienesLazyModel;
    }

    public SofisComboG<SgAfClasificacionBienes> getComboClasificacion() {
        return comboClasificacion;
    }

    public void setComboClasificacion(SofisComboG<SgAfClasificacionBienes> comboClasificacion) {
        this.comboClasificacion = comboClasificacion;
    }

    public SofisComboG<SgAfClasificacionBienes> getComboClasificacionInsert() {
        return comboClasificacionInsert;
    }

    public void setComboClasificacionInsert(SofisComboG<SgAfClasificacionBienes> comboClasificacionInsert) {
        this.comboClasificacionInsert = comboClasificacionInsert;
    }

    public SofisComboG<SgAfCategoriaBienes> getComboCategorias() {
        return comboCategorias;
    }

    public void setComboCategorias(SofisComboG<SgAfCategoriaBienes> comboCategorias) {
        this.comboCategorias = comboCategorias;
    }

    public SofisComboG<SgAfCategoriaBienes> getComboCategoriasInsert() {
        return comboCategoriasInsert;
    }

    public void setComboCategoriasInsert(SofisComboG<SgAfCategoriaBienes> comboCategoriasInsert) {
        this.comboCategoriasInsert = comboCategoriasInsert;
    }
}
