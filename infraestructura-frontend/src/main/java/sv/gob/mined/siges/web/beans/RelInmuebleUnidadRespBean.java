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
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgRelInmuebleUnidadResp;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtros.FiltroRelInmuebleUnidadResp;
import sv.gob.mined.siges.web.lazymodels.LazyRelInmuebleUnidadRespDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.RelInmuebleUnidadRespRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class RelInmuebleUnidadRespBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RelInmuebleUnidadRespBean.class.getName());

    @Inject
    private RelInmuebleUnidadRespRestClient restClient;

    private FiltroRelInmuebleUnidadResp filtro = new FiltroRelInmuebleUnidadResp();
    private SgRelInmuebleUnidadResp entidadEnEdicion = new SgRelInmuebleUnidadResp();
    private List<RevHistorico> historialRelInmuebleUnidadResp = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyRelInmuebleUnidadRespDataModel relInmuebleUnidadRespLazyModel;

    public RelInmuebleUnidadRespBean() {
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
            relInmuebleUnidadRespLazyModel = new LazyRelInmuebleUnidadRespDataModel(restClient, filtro, totalResultados, paginado);
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
        filtro = new FiltroRelInmuebleUnidadResp();
        buscar();
    }

    public void agregar() {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgRelInmuebleUnidadResp();
    }

    public void actualizar(SgRelInmuebleUnidadResp var) {
	JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgRelInmuebleUnidadResp) SerializationUtils.clone(var);
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
            restClient.eliminar(entidadEnEdicion.getRiuPk());
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
            historialRelInmuebleUnidadResp = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroRelInmuebleUnidadResp getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroRelInmuebleUnidadResp filtro) {
        this.filtro = filtro;
    }

    public SgRelInmuebleUnidadResp getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgRelInmuebleUnidadResp entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialRelInmuebleUnidadResp() {
        return historialRelInmuebleUnidadResp;
    }

    public void setHistorialRelInmuebleUnidadResp(List<RevHistorico> historialRelInmuebleUnidadResp) {
        this.historialRelInmuebleUnidadResp = historialRelInmuebleUnidadResp;
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

    public LazyRelInmuebleUnidadRespDataModel getRelInmuebleUnidadRespLazyModel() {
        return relInmuebleUnidadRespLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyRelInmuebleUnidadRespDataModel relInmuebleUnidadRespLazyModel) {
        this.relInmuebleUnidadRespLazyModel = relInmuebleUnidadRespLazyModel;
    }

}
