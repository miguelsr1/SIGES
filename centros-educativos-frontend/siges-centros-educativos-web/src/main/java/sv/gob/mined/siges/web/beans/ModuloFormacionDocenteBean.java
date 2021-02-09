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
import sv.gob.mined.siges.web.dto.SgModuloFormacionDocente;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroModuloFormacionDocente;
import sv.gob.mined.siges.web.lazymodels.LazyModuloFormacionDocenteDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.ModuloFormacionDocenteRestClient;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class ModuloFormacionDocenteBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ModuloFormacionDocenteBean.class.getName());

    @Inject
    private ModuloFormacionDocenteRestClient restClient;

    @Inject
    private SessionBean sessionBean;

    
    private SgModuloFormacionDocente entidadEnEdicion = new SgModuloFormacionDocente();
    private FiltroModuloFormacionDocente filtro = new FiltroModuloFormacionDocente();
    private LazyModuloFormacionDocenteDataModel moduloFormacionDocenteLazyModel;
    private List<RevHistorico> historialModuloFormacionDocente = new ArrayList();
    private String formatoSeleccionado = "csv";
    private Integer paginado = 10;
    private Long totalResultados;
    

    public ModuloFormacionDocenteBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_MODULO_FORMACION_DOCENTE)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String buscar() {
        try {
          
            totalResultados = restClient.buscarTotal(filtro);
            moduloFormacionDocenteLazyModel = new LazyModuloFormacionDocenteDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    public void cargarCombos() {

    }

    private void limpiarCombos() {
    }

    public String limpiar() {
        filtro = new FiltroModuloFormacionDocente();
        buscar();
        return null;
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = new SgModuloFormacionDocente();
    }

    public void actualizar(SgModuloFormacionDocente var) {
        JSFUtils.limpiarMensajesError();
        limpiarCombos();
        entidadEnEdicion = (SgModuloFormacionDocente) SerializationUtils.clone(var);
    }

    public void guardar() {
        try {
            entidadEnEdicion = restClient.guardar(entidadEnEdicion);
            PrimeFaces.current().executeScript("PF('itemDialog').hide()");
            JSFUtils.agregarInfo(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.GUARDADO_CORRECTO), "");
            buscar();
        } catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_POPUP, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void eliminar() {
        try {
            restClient.eliminar(entidadEnEdicion.getMfdPk());
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

    public String historial(Long id) {
        try {
            historialModuloFormacionDocente = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }

    
    public ModuloFormacionDocenteRestClient getRestClient() {
        return restClient;
    }

    public void setRestClient(ModuloFormacionDocenteRestClient restClient) {
        this.restClient = restClient;
    }

    public SgModuloFormacionDocente getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgModuloFormacionDocente entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public FiltroModuloFormacionDocente getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroModuloFormacionDocente filtro) {
        this.filtro = filtro;
    }

    public LazyModuloFormacionDocenteDataModel getModuloFormacionDocenteLazyModel() {
        return moduloFormacionDocenteLazyModel;
    }

    public void setModuloFormacionDocenteLazyModel(LazyModuloFormacionDocenteDataModel moduloFormacionDocenteLazyModel) {
        this.moduloFormacionDocenteLazyModel = moduloFormacionDocenteLazyModel;
    }

    public List<RevHistorico> getHistorialModuloFormacionDocente() {
        return historialModuloFormacionDocente;
    }

    public String getFormatoSeleccionado() {
        return formatoSeleccionado;
    }

    public void setFormatoSeleccionado(String formatoSeleccionado) {
        this.formatoSeleccionado = formatoSeleccionado;
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

}
