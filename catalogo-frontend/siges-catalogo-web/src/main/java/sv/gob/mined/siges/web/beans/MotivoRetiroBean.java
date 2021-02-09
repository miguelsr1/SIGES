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
import sv.gob.mined.siges.web.dto.SgMotivoRetiro;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroMotivoRetiro;
import sv.gob.mined.siges.web.lazymodels.LazyMotivoRetiroDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.MotivoRetiroRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class MotivoRetiroBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(MotivoRetiroBean.class.getName());

    @Inject
    private MotivoRetiroRestClient restClient;

    private FiltroMotivoRetiro filtro = new FiltroMotivoRetiro();
    private SgMotivoRetiro entidadEnEdicion = new SgMotivoRetiro();
    private List<SgMotivoRetiro> historialMotivoRetiro = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyMotivoRetiroDataModel motivoRetiroLazyModel;

    public MotivoRetiroBean() {
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
            motivoRetiroLazyModel = new LazyMotivoRetiroDataModel(restClient, filtro, totalResultados, paginado);
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
        filtro = new FiltroMotivoRetiro();
        buscar();
    }

    public void agregar() {
        limpiarCombos();
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgMotivoRetiro();
    }

    public void validarDefinitivo() {
        if(this.entidadEnEdicion.getMreDefinitivo()){
            this.entidadEnEdicion.setMreTraslado(Boolean.FALSE);
            this.entidadEnEdicion.setMreCambioSecc(Boolean.FALSE);
        }
    }

    public void actualizar(SgMotivoRetiro var) {
        limpiarCombos();
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = (SgMotivoRetiro) SerializationUtils.clone(var);
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
            restClient.eliminar(entidadEnEdicion.getMrePk());
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
            historialMotivoRetiro = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroMotivoRetiro getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroMotivoRetiro filtro) {
        this.filtro = filtro;
    }

    public SgMotivoRetiro getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgMotivoRetiro entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgMotivoRetiro> getHistorialMotivoRetiro() {
        return historialMotivoRetiro;
    }

    public void setHistorialMotivoRetiro(List<SgMotivoRetiro> historialMotivoRetiro) {
        this.historialMotivoRetiro = historialMotivoRetiro;
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

    public LazyMotivoRetiroDataModel getMotivoRetiroLazyModel() {
        return motivoRetiroLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyMotivoRetiroDataModel motivoRetiroLazyModel) {
        this.motivoRetiroLazyModel = motivoRetiroLazyModel;
    }

}
