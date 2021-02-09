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
import sv.gob.mined.siges.web.dto.SgEvaluacionItemLiquidacionOtros;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyEvaluacionItemLiquidacionOtrosDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EvaluacionItemLiquidacionOtrosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EvaluacionItemLiquidacionOtrosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EvaluacionItemLiquidacionOtrosBean.class.getName());

    @Inject
    private EvaluacionItemLiquidacionOtrosRestClient restClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgEvaluacionItemLiquidacionOtros entidadEnEdicion = new SgEvaluacionItemLiquidacionOtros();
    private List<SgEvaluacionItemLiquidacionOtros> historialEvaluacionItemLiquidacionOtros = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyEvaluacionItemLiquidacionOtrosDataModel evaluacionItemLiquidacionOtrosLazyModel;

    public EvaluacionItemLiquidacionOtrosBean() {
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
            evaluacionItemLiquidacionOtrosLazyModel = new LazyEvaluacionItemLiquidacionOtrosDataModel(restClient, filtro, totalResultados, paginado);
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
        filtro = new FiltroCodiguera();
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgEvaluacionItemLiquidacionOtros();
    }

    public void actualizar(SgEvaluacionItemLiquidacionOtros var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgEvaluacionItemLiquidacionOtros) SerializationUtils.clone(var);
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
            restClient.eliminar(entidadEnEdicion.getEloPk());
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
            historialEvaluacionItemLiquidacionOtros = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgEvaluacionItemLiquidacionOtros getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEvaluacionItemLiquidacionOtros entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgEvaluacionItemLiquidacionOtros> getHistorialEvaluacionItemLiquidacionOtros() {
        return historialEvaluacionItemLiquidacionOtros;
    }

    public void setHistorialEvaluacionItemLiquidacionOtros(List<SgEvaluacionItemLiquidacionOtros> historialEvaluacionItemLiquidacionOtros) {
        this.historialEvaluacionItemLiquidacionOtros = historialEvaluacionItemLiquidacionOtros;
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

    public LazyEvaluacionItemLiquidacionOtrosDataModel getEvaluacionItemLiquidacionOtrosLazyModel() {
        return evaluacionItemLiquidacionOtrosLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyEvaluacionItemLiquidacionOtrosDataModel evaluacionItemLiquidacionOtrosLazyModel) {
        this.evaluacionItemLiquidacionOtrosLazyModel = evaluacionItemLiquidacionOtrosLazyModel;
    }

}
