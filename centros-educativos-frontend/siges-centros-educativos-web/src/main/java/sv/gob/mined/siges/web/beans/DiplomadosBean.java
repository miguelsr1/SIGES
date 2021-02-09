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
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SgDiplomado;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.lazymodels.LazyDiplomadoDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DiplomadoRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.dto.SgCalificacionDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCalificacionDiplomado;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDiplomado;
import sv.gob.mined.siges.web.restclient.CalificacionDiplomadoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class DiplomadosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DiplomadoBean.class.getName());

    @Inject
    private DiplomadoRestClient restClient;

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private CalificacionDiplomadoRestClient calificacionDiplomadorestClient;

    private FiltroDiplomado filtro = new FiltroDiplomado();
    private SgDiplomado entidadEnEdicion = new SgDiplomado();
    private List<RevHistorico> historialDiplomado = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyDiplomadoDataModel diplomadoLazyModel;

    public DiplomadosBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_DIPLOMADO)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtro.setFirst(new Long(0));
            totalResultados = restClient.buscarTotal(filtro);
            diplomadoLazyModel = new LazyDiplomadoDataModel(restClient, filtro, totalResultados, paginado);
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
        filtro = new FiltroDiplomado();
        buscar();
    }

    public void agregar() {
        limpiarCombos();
        entidadEnEdicion = new SgDiplomado();
        JSFUtils.limpiarMensajesError();
    }

    public void actualizar(SgDiplomado var) {
        limpiarCombos();
        entidadEnEdicion = (SgDiplomado) SerializationUtils.clone(var);
        JSFUtils.limpiarMensajesError();
    }

    public void prepararParaEliminar(SgDiplomado var) {
        try {
            limpiarCombos();
            entidadEnEdicion = (SgDiplomado) SerializationUtils.clone(var);
            JSFUtils.limpiarMensajesError();

            FiltroCalificacionDiplomado fcd = new FiltroCalificacionDiplomado();
            fcd.setCalDiplomadoFk(var.getDipPk());
            fcd.setIncluirCampos(new String[]{"cadPk"});
            fcd.setMaxResults(1L);
            List<SgCalificacionDiplomado> calDip = calificacionDiplomadorestClient.buscar(fcd);
            
            if(calDip!=null && !calDip.isEmpty()){
                JSFUtils.agregarWarn("eliminarMsg", "ADVERTENCIA: El diplomado que desea eliminar tiene calificaciones asociadas, de continuar las mismas serán eliminadas.", "");
            }
        } catch (BusinessException be) {
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
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
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getDipPk());
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
            historialDiplomado = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroDiplomado getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroDiplomado filtro) {
        this.filtro = filtro;
    }

    public SgDiplomado getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgDiplomado entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<RevHistorico> getHistorialDiplomado() {
        return historialDiplomado;
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

    public LazyDiplomadoDataModel getDiplomadoLazyModel() {
        return diplomadoLazyModel;
    }

    public void setTiposCalendarioLazyModel(LazyDiplomadoDataModel diplomadoLazyModel) {
        this.diplomadoLazyModel = diplomadoLazyModel;
    }

}
