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
import sv.gob.mined.siges.web.dto.SgMaximoNivelEducativoAlcanzado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyMaximoNivelEducativoAlcanzadoDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.MaximoNivelEducativoAlcanzadoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MaximoNivelEducativoAlcanzadoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MaximoNivelEducativoAlcanzadoBean.class.getName());

    @Inject
    private MaximoNivelEducativoAlcanzadoRestClient restClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgMaximoNivelEducativoAlcanzado entidadEnEdicion = new SgMaximoNivelEducativoAlcanzado();
    private List<SgMaximoNivelEducativoAlcanzado> historialMaximoNivelEducativoAlcanzado = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyMaximoNivelEducativoAlcanzadoDataModel maximoNivelEducativoAlcanzadoLazyModel;

    public MaximoNivelEducativoAlcanzadoBean() {
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
            maximoNivelEducativoAlcanzadoLazyModel = new LazyMaximoNivelEducativoAlcanzadoDataModel(restClient, filtro, totalResultados, paginado);
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
        entidadEnEdicion = new SgMaximoNivelEducativoAlcanzado();
    }

    public void actualizar(SgMaximoNivelEducativoAlcanzado var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgMaximoNivelEducativoAlcanzado) SerializationUtils.clone(var);
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
            restClient.eliminar(entidadEnEdicion.getMnePk());
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
            historialMaximoNivelEducativoAlcanzado = restClient.obtenerHistorialPorId(id);
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

    public SgMaximoNivelEducativoAlcanzado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMaximoNivelEducativoAlcanzado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgMaximoNivelEducativoAlcanzado> getHistorialMaximoNivelEducativoAlcanzado() {
        return historialMaximoNivelEducativoAlcanzado;
    }

    public void setHistorialMaximoNivelEducativoAlcanzado(List<SgMaximoNivelEducativoAlcanzado> historialMaximoNivelEducativoAlcanzado) {
        this.historialMaximoNivelEducativoAlcanzado = historialMaximoNivelEducativoAlcanzado;
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

    public LazyMaximoNivelEducativoAlcanzadoDataModel getMaximoNivelEducativoAlcanzadoLazyModel() {
        return maximoNivelEducativoAlcanzadoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyMaximoNivelEducativoAlcanzadoDataModel maximoNivelEducativoAlcanzadoLazyModel) {
        this.maximoNivelEducativoAlcanzadoLazyModel = maximoNivelEducativoAlcanzadoLazyModel;
    }

}
