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
import sv.gob.mined.siges.web.dto.SgCategoriaOperacion;
import sv.gob.mined.siges.web.dto.SgOperacion;
import sv.gob.mined.siges.web.dto.centros_educativos.RevHistorico;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroOperacion;
import sv.gob.mined.siges.web.lazymodels.LazyOperacionDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CategoriaOperacionRestClient;
import sv.gob.mined.siges.web.restclient.OperacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class OperacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(OperacionBean.class.getName());

    @Inject
    private OperacionRestClient restClient;
    
    @Inject
    private CategoriaOperacionRestClient restCategoriaClient;

    private FiltroOperacion filtro = new FiltroOperacion();
    private SgOperacion entidadEnEdicion = new SgOperacion();
    private List<RevHistorico> historialOperacion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyOperacionDataModel operacionLazyModel;
    private SofisComboG<SgCategoriaOperacion> comboCategorias= new SofisComboG<>();
    private SofisComboG<SgCategoriaOperacion> comboCategoriasBuscar= new SofisComboG<>();

    public OperacionBean() {
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
            filtro.setCategoria(comboCategoriasBuscar.getSelectedT()!=null?comboCategoriasBuscar.getSelectedT().getCopPk():null);
            filtro.setIncluirCampos(new String[]{"opePk", "opeCodigo","opeNombre","opeCategoriaOperacion","opeHabilitado","opeDescripcion","opeUltModUsuario","opeUltModFecha","opeVersion"});
            totalResultados = restClient.buscarTotal(filtro);
            operacionLazyModel = new LazyOperacionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
        try {
            FiltroCodiguera fc= new FiltroCodiguera();
            fc.setHabilitado(Boolean.TRUE);
            List<SgCategoriaOperacion> listaCategorias=restCategoriaClient.buscar(fc);
            comboCategorias=new SofisComboG<>(listaCategorias,"copNombre");
            comboCategorias.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
            
            comboCategoriasBuscar=new SofisComboG<>(listaCategorias,"copNombre");
            comboCategoriasBuscar.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        } catch ( Exception ex) {
             LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }

    }

    private void limpiarCombos() {
        cargarCombos();
    }

    public void limpiar() {
        filtro = new FiltroOperacion();
        comboCategoriasBuscar.setSelected(-1);
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgOperacion();
    }

    public void actualizar(SgOperacion var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgOperacion) SerializationUtils.clone(var);
        comboCategorias.setSelectedT(entidadEnEdicion.getOpeCategoriaOperacion());
    }

    public void guardar() {
        try {
            if (comboCategorias.getSelectedT()!=null) {
                entidadEnEdicion.setOpeCategoriaOperacion(comboCategorias.getSelectedT());
            }
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
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
            restClient.eliminar(entidadEnEdicion.getOpePk());
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
            historialOperacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroOperacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroOperacion filtro) {
        this.filtro = filtro;
    }

    public SgOperacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgOperacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialOperacion() {
        return historialOperacion;
    }

    public void setHistorialOperacion(List<RevHistorico> historialOperacion) {
        this.historialOperacion = historialOperacion;
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

    public LazyOperacionDataModel getOperacionLazyModel() {
        return operacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyOperacionDataModel operacionLazyModel) {
        this.operacionLazyModel = operacionLazyModel;
    }

    public SofisComboG<SgCategoriaOperacion> getComboCategorias() {
        return comboCategorias;
    }

    public void setComboCategorias(SofisComboG<SgCategoriaOperacion> comboCategorias) {
        this.comboCategorias = comboCategorias;
    }

    public OperacionRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(OperacionRestClient restClient) {
        this.restClient = restClient;
    }

    public CategoriaOperacionRestClient getRestCategoriaClient() {
        return restCategoriaClient;
    }

    public void setRestCategoriaClient(CategoriaOperacionRestClient restCategoriaClient) {
        this.restCategoriaClient = restCategoriaClient;
    }

    public SofisComboG<SgCategoriaOperacion> getComboCategoriasBuscar() {
        return comboCategoriasBuscar;
    }

    public void setComboCategoriasBuscar(SofisComboG<SgCategoriaOperacion> comboCategoriasBuscar) {
        this.comboCategoriasBuscar = comboCategoriasBuscar;
    }
    
    

}
