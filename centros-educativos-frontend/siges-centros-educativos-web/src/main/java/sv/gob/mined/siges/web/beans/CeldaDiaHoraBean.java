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
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgCeldaDiaHora;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyCeldaDiaHoraDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CeldaDiaHoraRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.utils.LoggingUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class CeldaDiaHoraBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(CeldaDiaHoraBean.class.getName());

    @Inject
    private CeldaDiaHoraRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    private FiltroCodiguera filtro = new FiltroCodiguera();
    private SgCeldaDiaHora entidadEnEdicion = new SgCeldaDiaHora();
    private List<SgCeldaDiaHora> historialCeldaDiaHora = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyCeldaDiaHoraDataModel celdaDiaHoraLazyModel;

    public CeldaDiaHoraBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargarCombos();
            buscar();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.BUSCAR_CELDAS_DIA_HORA)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            //  celdaDiaHoraLazyModel = new LazyCeldaDiaHoraDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
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
        limpiarCombos();
        entidadEnEdicion = new SgCeldaDiaHora();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgCeldaDiaHora var) {
        limpiarCombos();
        entidadEnEdicion = (SgCeldaDiaHora) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
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
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_POPUP, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getCdhPk());
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ELIMINADO_CORRECTO), "");
            buscar();
            entidadEnEdicion = null;
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void historial(Long id) {
        try {
            historialCeldaDiaHora = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public SgCeldaDiaHora getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgCeldaDiaHora entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgCeldaDiaHora> getHistorialCeldaDiaHora() {
        return historialCeldaDiaHora;
    }

    public void setHistorialCeldaDiaHora(List<SgCeldaDiaHora> historialCeldaDiaHora) {
        this.historialCeldaDiaHora = historialCeldaDiaHora;
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

    public LazyCeldaDiaHoraDataModel getCeldaDiaHoraLazyModel() {
        return celdaDiaHoraLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyCeldaDiaHoraDataModel celdaDiaHoraLazyModel) {
        this.celdaDiaHoraLazyModel = celdaDiaHoraLazyModel;
    }

}