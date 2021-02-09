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
import sv.gob.mined.siges.web.dto.SgMovimientoLiquidacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoLiquidacion;
import sv.gob.mined.siges.web.lazymodels.LazyMovimientoLiquidacionDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.MovimientoLiquidacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 * Bean para la gestión de movimientos liquidacion.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MovimientoLiquidacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MovimientoLiquidacionBean.class.getName());

    @Inject
    private MovimientoLiquidacionRestClient restClient;

    private FiltroMovimientoLiquidacion filtro = new FiltroMovimientoLiquidacion();
    private SgMovimientoLiquidacion entidadEnEdicion = new SgMovimientoLiquidacion();
    private List<SgMovimientoLiquidacion> historialMovimientoLiquidacion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyMovimientoLiquidacionDataModel movimientoLiquidacionLazyModel;

    /**
     * Constructor de la clase.
     */
    public MovimientoLiquidacionBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Liquidaciones.
     */
    @PostConstruct
    public void init() {
        try {
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Busca los registros de Liquidaciones según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            movimientoLiquidacionLazyModel = new LazyMovimientoLiquidacionDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Carga los combos de filtros.
     */
    public void cargarCombos() {

    }

    /**
     * Limpia los combos usados para filtrar y crear.
     */
    private void limpiarCombos() {

    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroMovimientoLiquidacion();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Liquidaciones.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgMovimientoLiquidacion();
    }

    /**
     * Crea o actualiza un registro de Liquidaciones.
     *
     * @param SgMovimientoLiquidacion : var.
     */
    public void actualizar(SgMovimientoLiquidacion var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgMovimientoLiquidacion) SerializationUtils.clone(var);
    }

    /**
     * Crea o actualiza un registro de Liquidaciones.
     */
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

    /**
     * Elimina un registro de Liquidaciones.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getMlqPk());
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

    /**
     * Muestra el historial del registro.
     *
     * @param id Long: id del registro del cual se quiere obtener el historial
     */
    public void historial(Long id) {
        try {
            historialMovimientoLiquidacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroMovimientoLiquidacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroMovimientoLiquidacion filtro) {
        this.filtro = filtro;
    }

    public SgMovimientoLiquidacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMovimientoLiquidacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgMovimientoLiquidacion> getHistorialMovimientoLiquidacion() {
        return historialMovimientoLiquidacion;
    }

    public void setHistorialMovimientoLiquidacion(List<SgMovimientoLiquidacion> historialMovimientoLiquidacion) {
        this.historialMovimientoLiquidacion = historialMovimientoLiquidacion;
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

    public LazyMovimientoLiquidacionDataModel getMovimientoLiquidacionLazyModel() {
        return movimientoLiquidacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyMovimientoLiquidacionDataModel movimientoLiquidacionLazyModel) {
        this.movimientoLiquidacionLazyModel = movimientoLiquidacionLazyModel;
    }

    // </editor-fold>
}
