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
import sv.gob.mined.siges.web.dto.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.SgAfUnidadesAdministrativas;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesAdministrativas;
import sv.gob.mined.siges.web.lazymodels.LazyUnidadesAdministrativasDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesAdministrativasRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class UnidadesAdministrativasBean implements Serializable {
     private static final Logger LOGGER = Logger.getLogger(UnidadesAdministrativasBean.class.getName());

    @Inject
    private UnidadesAdministrativasRestClient restClient;
    
    @Inject
    private UnidadesActivoFijoRestClient unidadesActivoFijoRestClient;
    
    
    private FiltroUnidadesAdministrativas filtro = new FiltroUnidadesAdministrativas();
    private SgAfUnidadesAdministrativas entidadEnEdicion = new SgAfUnidadesAdministrativas();
    private List<SgAfUnidadesAdministrativas> historialUnidadesAD = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyUnidadesAdministrativasDataModel unidadesADLazyModel;
    
    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesAF;
    private SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesAFInsert;
    
    public UnidadesAdministrativasBean() {
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
            filtro.setUnidadActivoFijoId(comboUnidadesAF.getSelectedT()  != null ? comboUnidadesAF.getSelectedT().getUafPk(): null);
            totalResultados = restClient.buscarTotal(filtro);
            unidadesADLazyModel = new LazyUnidadesAdministrativasDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public void cargarCombos() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        FiltroUnidadesActivoFijo fuaf = new FiltroUnidadesActivoFijo();
        fuaf.setHabilitado(Boolean.TRUE);
        List<SgAfUnidadesActivoFijo> unidadesAF = unidadesActivoFijoRestClient.buscar(fuaf);
        
        comboUnidadesAF = new SofisComboG(unidadesAF, "uafNombre");
        comboUnidadesAF.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        
        comboUnidadesAFInsert = new SofisComboG(unidadesAF, "uafNombre");
        comboUnidadesAFInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
                
    }
    
    public void limpiar() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        filtro = new FiltroUnidadesAdministrativas();
        cargarCombos();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgAfUnidadesAdministrativas();
        comboUnidadesAFInsert.setSelected(-1);
    }

    public void actualizar(SgAfUnidadesAdministrativas var) {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = (SgAfUnidadesAdministrativas) SerializationUtils.clone(var);
        comboUnidadesAFInsert.setSelectedT(entidadEnEdicion.getUadUnidadActivoFijoFk());
    }

    public void guardar() {
        try {
            entidadEnEdicion.setUadUnidadActivoFijoFk(comboUnidadesAFInsert.getSelectedT() != null ? comboUnidadesAFInsert.getSelectedT() : null);
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
            restClient.eliminar(entidadEnEdicion.getUadPk());
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
            historialUnidadesAD = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroUnidadesAdministrativas getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroUnidadesAdministrativas filtro) {
        this.filtro = filtro;
    }

    public SgAfUnidadesAdministrativas getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfUnidadesAdministrativas entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgAfUnidadesAdministrativas> getHistorialUnidadesAD() {
        return historialUnidadesAD;
    }

    public void setHistorialUnidadesAD(List<SgAfUnidadesAdministrativas> historialUnidadesAD) {
        this.historialUnidadesAD = historialUnidadesAD;
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

    public LazyUnidadesAdministrativasDataModel getUnidadesADLazyModel() {
        return unidadesADLazyModel;
    }

    public void setUnidadesADLazyModel(LazyUnidadesAdministrativasDataModel unidadesADLazyModel) {
        this.unidadesADLazyModel = unidadesADLazyModel;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadesAF() {
        return comboUnidadesAF;
    }

    public void setComboUnidadesAF(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesAF) {
        this.comboUnidadesAF = comboUnidadesAF;
    }

    public SofisComboG<SgAfUnidadesActivoFijo> getComboUnidadesAFInsert() {
        return comboUnidadesAFInsert;
    }

    public void setComboUnidadesAFInsert(SofisComboG<SgAfUnidadesActivoFijo> comboUnidadesAFInsert) {
        this.comboUnidadesAFInsert = comboUnidadesAFInsert;
    }

}
