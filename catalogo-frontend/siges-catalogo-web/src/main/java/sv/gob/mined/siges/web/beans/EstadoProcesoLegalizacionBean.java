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
import sv.gob.mined.siges.web.dto.SgEstadoProcesoLegalizacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEstadoProcesoLegalizacion;
import sv.gob.mined.siges.web.lazymodels.LazyEstadoProcesoLegalizacionDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EstadoProcesoLegalizacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EstadoProcesoLegalizacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstadoProcesoLegalizacionBean.class.getName());

    @Inject
    private EstadoProcesoLegalizacionRestClient restClient;

    private FiltroEstadoProcesoLegalizacion filtro = new FiltroEstadoProcesoLegalizacion();
    private SgEstadoProcesoLegalizacion entidadEnEdicion = new SgEstadoProcesoLegalizacion();
    private List<SgEstadoProcesoLegalizacion> historialEstadoProcesoLegalizacion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyEstadoProcesoLegalizacionDataModel estadoProcesoLegalizacionLazyModel;

    public EstadoProcesoLegalizacionBean() {
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
            estadoProcesoLegalizacionLazyModel = new LazyEstadoProcesoLegalizacionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() {

    }

    private void limpiarCombos() {

    }

    public void limpiar() {
        filtro = new FiltroEstadoProcesoLegalizacion();
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgEstadoProcesoLegalizacion();
    }

    public void actualizar(SgEstadoProcesoLegalizacion var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgEstadoProcesoLegalizacion) SerializationUtils.clone(var);
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
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getEplPk());
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
            historialEstadoProcesoLegalizacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroEstadoProcesoLegalizacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEstadoProcesoLegalizacion filtro) {
        this.filtro = filtro;
    }

    public SgEstadoProcesoLegalizacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEstadoProcesoLegalizacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgEstadoProcesoLegalizacion> getHistorialEstadoProcesoLegalizacion() {
        return historialEstadoProcesoLegalizacion;
    }

    public void setHistorialEstadoProcesoLegalizacion(List<SgEstadoProcesoLegalizacion> historialEstadoProcesoLegalizacion) {
        this.historialEstadoProcesoLegalizacion = historialEstadoProcesoLegalizacion;
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

    public LazyEstadoProcesoLegalizacionDataModel getEstadoProcesoLegalizacionLazyModel() {
        return estadoProcesoLegalizacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyEstadoProcesoLegalizacionDataModel estadoProcesoLegalizacionLazyModel) {
        this.estadoProcesoLegalizacionLazyModel = estadoProcesoLegalizacionLazyModel;
    }

}
