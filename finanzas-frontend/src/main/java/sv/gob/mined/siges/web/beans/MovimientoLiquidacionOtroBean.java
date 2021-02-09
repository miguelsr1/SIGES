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
import sv.gob.mined.siges.web.dto.SgMovimientoLiquidacionOtro;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMovimientoLiquidacionOtro;
import sv.gob.mined.siges.web.lazymodels.LazyMovimientoLiquidacionOtroDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.MovimientoLiquidacionOtroRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 * Bean para la gestión de movimiento liquidación.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MovimientoLiquidacionOtroBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MovimientoLiquidacionOtroBean.class.getName());

    @Inject
    private MovimientoLiquidacionOtroRestClient restClient;

    private FiltroMovimientoLiquidacionOtro filtro = new FiltroMovimientoLiquidacionOtro();
    private SgMovimientoLiquidacionOtro entidadEnEdicion = new SgMovimientoLiquidacionOtro();
    private List<SgMovimientoLiquidacionOtro> historialMovimientoLiquidacionOtro = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyMovimientoLiquidacionOtroDataModel movimientoLiquidacionOtroLazyModel;

    /**
     * Constructor de la clase.
     */
    public MovimientoLiquidacionOtroBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Liquidaciones.
     */
    @PostConstruct
    public void init() {
        try {
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
            movimientoLiquidacionOtroLazyModel = new LazyMovimientoLiquidacionOtroDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Limpia los filtros.
     */
    public void limpiar() {
        filtro = new FiltroMovimientoLiquidacionOtro();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Liquidaciones.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiar();
        entidadEnEdicion = new SgMovimientoLiquidacionOtro();
    }

    /**
     * Crea o actualiza un registro de Liquidaciones.
     *
     * @param SgMovimientoLiquidacionOtro : var.
     */
    public void actualizar(SgMovimientoLiquidacionOtro var) {
        JSFUtils.limpiarMensajesError();
        limpiar();
        entidadEnEdicion = (SgMovimientoLiquidacionOtro) SerializationUtils.clone(var);
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
            restClient.eliminar(entidadEnEdicion.getMloPk());
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
            historialMovimientoLiquidacionOtro = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroMovimientoLiquidacionOtro getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroMovimientoLiquidacionOtro filtro) {
        this.filtro = filtro;
    }

    public SgMovimientoLiquidacionOtro getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMovimientoLiquidacionOtro entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgMovimientoLiquidacionOtro> getHistorialMovimientoLiquidacionOtro() {
        return historialMovimientoLiquidacionOtro;
    }

    public void setHistorialMovimientoLiquidacionOtro(List<SgMovimientoLiquidacionOtro> historialMovimientoLiquidacionOtro) {
        this.historialMovimientoLiquidacionOtro = historialMovimientoLiquidacionOtro;
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

    public LazyMovimientoLiquidacionOtroDataModel getMovimientoLiquidacionOtroLazyModel() {
        return movimientoLiquidacionOtroLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyMovimientoLiquidacionOtroDataModel movimientoLiquidacionOtroLazyModel) {
        this.movimientoLiquidacionOtroLazyModel = movimientoLiquidacionOtroLazyModel;
    }

    // </editor-fold>
}
