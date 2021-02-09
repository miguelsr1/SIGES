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
import sv.gob.mined.siges.web.dto.SgEvaluacionLiquidacion;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyEvaluacionLiquidacionDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EvaluacionLiquidacionRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 * Bean para la gestión de evaluación liquidación.
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EvaluacionLiquidacionBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EvaluacionLiquidacionBean.class.getName());

    @Inject
    private EvaluacionLiquidacionRestClient restClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgEvaluacionLiquidacion entidadEnEdicion = new SgEvaluacionLiquidacion();
    private List<SgEvaluacionLiquidacion> historialEvaluacionLiquidacion = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyEvaluacionLiquidacionDataModel evaluacionLiquidacionLazyModel;

    /**
     * Constructor de la clase.
     */
    public EvaluacionLiquidacionBean() {
    }

    /**
     * Método para incializar filtros, combos y búsqueda de Estado liquidación.
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
     * Busca los registros de Estado liquidación según los filtros utilizados.
     */
    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            evaluacionLiquidacionLazyModel = new LazyEvaluacionLiquidacionDataModel(restClient, filtro, totalResultados, paginado);
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
        filtro = new FiltroCodiguera();
        buscar();
    }

    /**
     * Limpia los filtros, además de crear un nuevo objeto de Estado
     * liquidación.
     */
    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgEvaluacionLiquidacion();
    }

    /**
     * Crea o actualiza un registro de Estado liquidación.
     *
     * @param var : Objeto SgEvaluacionLiquidacion
     */
    public void actualizar(SgEvaluacionLiquidacion var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgEvaluacionLiquidacion) SerializationUtils.clone(var);
    }

    /**
     * Crea o actualiza un registro de movimientos de Estado liquidación.
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
     * Elimina un registro de movimientos de Estado liquidación.
     */
    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getElqPk());
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
            historialEvaluacionLiquidacion = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    /**
     * Getter - Setter.
     */
    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgEvaluacionLiquidacion getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEvaluacionLiquidacion entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgEvaluacionLiquidacion> getHistorialEvaluacionLiquidacion() {
        return historialEvaluacionLiquidacion;
    }

    public void setHistorialEvaluacionLiquidacion(List<SgEvaluacionLiquidacion> historialEvaluacionLiquidacion) {
        this.historialEvaluacionLiquidacion = historialEvaluacionLiquidacion;
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

    public LazyEvaluacionLiquidacionDataModel getEvaluacionLiquidacionLazyModel() {
        return evaluacionLiquidacionLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyEvaluacionLiquidacionDataModel evaluacionLiquidacionLazyModel) {
        this.evaluacionLiquidacionLazyModel = evaluacionLiquidacionLazyModel;
    }
    // </editor-fold>
}
