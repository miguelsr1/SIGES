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
import sv.gob.mined.siges.web.dto.SgEstadoCivil;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyEstadoCivilDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.EstadoCivilRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class EstadoCivilBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EstadoCivilBean.class.getName());

    @Inject
    private EstadoCivilRestClient restClient;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgEstadoCivil entidadEnEdicion = new SgEstadoCivil();
    private List<SgEstadoCivil> historialEstadoCivil = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyEstadoCivilDataModel estadoCivilLazyModel;

    public EstadoCivilBean() {
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
            estadoCivilLazyModel = new LazyEstadoCivilDataModel(restClient, filtro, totalResultados, paginado);
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
        entidadEnEdicion = new SgEstadoCivil();
    }

    public void actualizar(SgEstadoCivil var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgEstadoCivil) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            restClient.guardar(entidadEnEdicion);
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
            restClient.eliminar(entidadEnEdicion.getEciPk());
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
            historialEstadoCivil = restClient.obtenerHistorialPorId(id);
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

    public SgEstadoCivil getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgEstadoCivil entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgEstadoCivil> getHistorialEstadoCivil() {
        return historialEstadoCivil;
    }

    public void setHistorialEstadoCivil(List<SgEstadoCivil> historialEstadoCivil) {
        this.historialEstadoCivil = historialEstadoCivil;
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

    public LazyEstadoCivilDataModel getEstadoCivilLazyModel() {
        return estadoCivilLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyEstadoCivilDataModel estadoCivilLazyModel) {
        this.estadoCivilLazyModel = estadoCivilLazyModel;
    }

}
