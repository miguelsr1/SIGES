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
import sv.gob.mined.siges.web.dto.SgEvaluacionItemLiquidacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroEvaluacionItemLiquidacion;
import sv.gob.mined.siges.web.lazymodels.LazyEvaluacionItemLiquidacionDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EvaluacionItemLiquidacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 * Bean para la gestión de evaluacíon item liquidación.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EvaluacionItemLiquidacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EvaluacionItemLiquidacionBean.class.getName());

    @Inject
    private EvaluacionItemLiquidacionRestClient restClient;

    private FiltroEvaluacionItemLiquidacion filtro = new FiltroEvaluacionItemLiquidacion();
    private SgEvaluacionItemLiquidacion entidadEnEdicion = new SgEvaluacionItemLiquidacion();
    private List<SgEvaluacionItemLiquidacion> historialEvaluacionItemLiquidacion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyEvaluacionItemLiquidacionDataModel evaluacionItemLiquidacionLazyModel;

    /**
     * Constructor de la clase.
     */
    public EvaluacionItemLiquidacionBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Evaluación itém
     * liquidación.
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
     * Busca los registros de Evaluación itém liquidación según los filtros
     * utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            evaluacionItemLiquidacionLazyModel = new LazyEvaluacionItemLiquidacionDataModel(restClient, filtro, totalResultados, paginado);
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
        filtro = new FiltroEvaluacionItemLiquidacion();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Evaluación itém
     * liquidación.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgEvaluacionItemLiquidacion();
    }

    /**
     * Crea o actualiza un registro de Evaluación itém liquidación.
     *
     * @param var : Objeto SgEvaluacionItemLiquidacion
     */
    public void actualizar(SgEvaluacionItemLiquidacion var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgEvaluacionItemLiquidacion) SerializationUtils.clone(var);
    }

    /**
     * Crea o actualiza un registro de movimientos de Evaluación itém
     * liquidación.
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
     * Elimina un registro de movimientos de Evaluación itém liquidación.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getEilPk());
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
            historialEvaluacionItemLiquidacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroEvaluacionItemLiquidacion getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroEvaluacionItemLiquidacion filtro) {
        this.filtro = filtro;
    }

    public SgEvaluacionItemLiquidacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEvaluacionItemLiquidacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgEvaluacionItemLiquidacion> getHistorialEvaluacionItemLiquidacion() {
        return historialEvaluacionItemLiquidacion;
    }

    public void setHistorialEvaluacionItemLiquidacion(List<SgEvaluacionItemLiquidacion> historialEvaluacionItemLiquidacion) {
        this.historialEvaluacionItemLiquidacion = historialEvaluacionItemLiquidacion;
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

    public LazyEvaluacionItemLiquidacionDataModel getEvaluacionItemLiquidacionLazyModel() {
        return evaluacionItemLiquidacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyEvaluacionItemLiquidacionDataModel evaluacionItemLiquidacionLazyModel) {
        this.evaluacionItemLiquidacionLazyModel = evaluacionItemLiquidacionLazyModel;
    }
    // </editor-fold>
}
