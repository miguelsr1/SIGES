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
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgRhEtiqueta;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEtiqueta;
import sv.gob.mined.siges.web.lazymodels.LazyEtiquetaDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EtiquetaRestClient;
import sv.gob.mined.siges.web.restclient.NivelRestClient;

import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class EtiquetasBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EtiquetasBean.class.getName());

    @Inject
    private EtiquetaRestClient restClient;
    
    @Inject
    private NivelRestClient nivelClient;    
    
    private FiltroEtiqueta filtro = new FiltroEtiqueta();
    private SgRhEtiqueta entidadEnEdicion;
    private List<RevHistorico> historialEtiquetas = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyEtiquetaDataModel etiquetaLazyModel;

    public EtiquetasBean() {
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
            totalResultados = restClient.buscarTotal(filtro);
            etiquetaLazyModel = new LazyEtiquetaDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {
    }
    
    public void limpiar() {
        filtro = new FiltroEtiqueta();
        buscar();
    }

    public void actualizar(SgRhEtiqueta var) {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = (SgRhEtiqueta) SerializationUtils.clone(var);
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getEtiPk());
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
            historialEtiquetas = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroEtiqueta getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEtiqueta filtro) {
        this.filtro = filtro;
    }

    public SgRhEtiqueta getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgRhEtiqueta entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialEtiquetas() {
        return historialEtiquetas;
    }

    public void setHistorialEtiquetas(List<RevHistorico> historialEtiquetas) {
        this.historialEtiquetas = historialEtiquetas;
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


    public LazyEtiquetaDataModel getEtiquetaLazyModel() {
        return etiquetaLazyModel;
    }

    public void setEtiquetaLazyModel(LazyEtiquetaDataModel etiquetaLazyModel) {
        this.etiquetaLazyModel = etiquetaLazyModel;
    }           
           
}
