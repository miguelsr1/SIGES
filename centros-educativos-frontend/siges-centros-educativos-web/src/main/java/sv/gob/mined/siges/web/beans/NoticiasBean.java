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
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgNoticia;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroNoticia;
import sv.gob.mined.siges.web.lazymodels.LazyNoticiaDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.NoticiaRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class NoticiasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(NoticiasBean.class.getName());

    @Inject
    private NoticiaRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    private SgNoticia entidadEnEdicion = new SgNoticia();
    private FiltroNoticia filtro = new FiltroNoticia();
    private LazyNoticiaDataModel noticiaLazyModel;
    private List<RevHistorico> historialNoticia = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;

    public NoticiasBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_NOTICIA)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
            totalResultados = restClient.buscarTotal(filtro);
            noticiaLazyModel = new LazyNoticiaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {

    }

    private void limpiarCombos() {

    }

    public String limpiar() {
        filtro = new FiltroNoticia();
        buscar();
        return null;
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgNoticia();
    }

    public void actualizar(SgNoticia var) {
        limpiarCombos();
        entidadEnEdicion = (SgNoticia) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
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
            restClient.eliminar(entidadEnEdicion.getNotPk());
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

    public String historial(Long id) {
        try {
            historialNoticia = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public NoticiaRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(NoticiaRestClient restClient) {
        this.restClient = restClient;
    }

    public FiltroNoticia getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroNoticia filtro) {
        this.filtro = filtro;
    }

    public LazyNoticiaDataModel getNoticiaLazyModel() {
        return noticiaLazyModel;
    }

    public void setNoticiaLazyModel(LazyNoticiaDataModel noticiaLazyModel) {
        this.noticiaLazyModel = noticiaLazyModel;
    }

    public List<RevHistorico> getHistorialNoticia() {
        return historialNoticia;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
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

    public SgNoticia getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgNoticia entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

}
