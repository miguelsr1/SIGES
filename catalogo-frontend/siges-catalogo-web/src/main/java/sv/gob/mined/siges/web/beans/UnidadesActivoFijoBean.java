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
import org.primefaces.event.TabChangeEvent;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.dto.SgAfComisionDescargo;
import sv.gob.mined.siges.web.dto.SgAfUnidadesActivoFijo;
import sv.gob.mined.siges.web.dto.SgDepartamento;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroUnidadesActivoFijo;
import sv.gob.mined.siges.web.lazymodels.LazyUnidadesActivoFijoDataModel;
import sv.gob.mined.siges.web.mensajes.Etiquetas;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DepartamentoRestClient;
import sv.gob.mined.siges.web.restclient.UnidadesActivoFijoRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;
import sv.gob.mined.siges.web.utilidades.SofisComboG;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ViewScoped
public class UnidadesActivoFijoBean implements Serializable {
     private static final Logger LOGGER = Logger.getLogger(UnidadesActivoFijoBean.class.getName());

    @Inject
    private UnidadesActivoFijoRestClient restClient;
    
    @Inject
    private DepartamentoRestClient departamentoRestClient;
    
    private FiltroUnidadesActivoFijo filtro = new FiltroUnidadesActivoFijo();
    private SgAfUnidadesActivoFijo entidadEnEdicion = new SgAfUnidadesActivoFijo();
    private List<SgAfUnidadesActivoFijo> historialUnidadesAF = new ArrayList();
    private Integer paginado = 10;
    private Long totalResultados;
    private LazyUnidadesActivoFijoDataModel unidadesAFLazyModel;
    private SofisComboG<SgDepartamento> comboDepartamento;
    private SofisComboG<SgDepartamento> comboDepartamentoInsert;
    private SgAfComisionDescargo miembroComision;
    private String tabActiveId;
    
    public UnidadesActivoFijoBean() {
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
            filtro.setDepartamentoId(comboDepartamento.getSelectedT()  != null ? comboDepartamento.getSelectedT().getDepPk(): null);
            totalResultados = restClient.buscarTotal(filtro);
            unidadesAFLazyModel = new LazyUnidadesActivoFijoDataModel(restClient, filtro, totalResultados, paginado);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void cargarCombos() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        FiltroCodiguera fc = new FiltroCodiguera();
        fc.setHabilitado(Boolean.TRUE);
        fc.setAscending(new boolean[]{true});
        fc.setOrderBy(new String[]{"depCodigo"});
        fc.setIncluirCampos(new String[]{"depNombre","depVersion"});
            
        List<SgDepartamento> departamentos = departamentoRestClient.buscar(fc);
        
        comboDepartamento = new SofisComboG(departamentos, "depNombre");
        comboDepartamento.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
        
        comboDepartamentoInsert = new SofisComboG(departamentos, "depNombre");
        comboDepartamentoInsert.addEmptyItem(Etiquetas.getValue("comboEmptyItem"));
    }
    
    public void changeTab(TabChangeEvent event) {
        PrimeFaces.current().executeScript("PF('tabsBlocker').hide()");
    }
    
    public void agregarMiembroComision() {
        JSFUtils.limpiarMensajesError();
        miembroComision = new SgAfComisionDescargo();
    }
    public void guardarMiembroComision() {
        entidadEnEdicion.getSgAfComisionDescargoList().add(miembroComision);
    }
    
    public void limpiar() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        filtro = new FiltroUnidadesActivoFijo();
        cargarCombos();
        buscar();
    }

    public void agregar() {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = new SgAfUnidadesActivoFijo();
    }

    public void actualizar(SgAfUnidadesActivoFijo var) {
        JSFUtils.limpiarMensajesError();
        entidadEnEdicion = (SgAfUnidadesActivoFijo) SerializationUtils.clone(var);
        comboDepartamentoInsert.setSelectedT(entidadEnEdicion.getUafDepartamento());
    }

    public void guardar() {
        try {
            entidadEnEdicion.setUafDepartamento(comboDepartamentoInsert.getSelectedT() != null ? comboDepartamentoInsert.getSelectedT() : null);
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
            restClient.eliminar(entidadEnEdicion.getUafPk());
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
            historialUnidadesAF = restClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }

    public FiltroUnidadesActivoFijo getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroUnidadesActivoFijo filtro) {
        this.filtro = filtro;
    }

    public SgAfUnidadesActivoFijo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfUnidadesActivoFijo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public List<SgAfUnidadesActivoFijo> getHistorialUnidadesAF() {
        return historialUnidadesAF;
    }

    public void setHistorialUnidadesAF(List<SgAfUnidadesActivoFijo> historialUnidadesAF) {
        this.historialUnidadesAF = historialUnidadesAF;
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

    public LazyUnidadesActivoFijoDataModel getUnidadesAFLazyModel() {
        return unidadesAFLazyModel;
    }

    public void setUnidadesAFLazyModel(LazyUnidadesActivoFijoDataModel unidadesAFLazyModel) {
        this.unidadesAFLazyModel = unidadesAFLazyModel;
    }

    public SofisComboG<SgDepartamento> getComboDepartamento() {
        return comboDepartamento;
    }

    public void setComboDepartamento(SofisComboG<SgDepartamento> comboDepartamento) {
        this.comboDepartamento = comboDepartamento;
    }

    public SofisComboG<SgDepartamento> getComboDepartamentoInsert() {
        return comboDepartamentoInsert;
    }

    public void setComboDepartamentoInsert(SofisComboG<SgDepartamento> comboDepartamentoInsert) {
        this.comboDepartamentoInsert = comboDepartamentoInsert;
    }

    public SgAfComisionDescargo getMiembroComision() {
        return miembroComision;
    }

    public void setMiembroComision(SgAfComisionDescargo miembroComision) {
        this.miembroComision = miembroComision;
    }

    public String getTabActiveId() {
        return tabActiveId;
    }

    public void setTabActiveId(String tabActiveId) {
        this.tabActiveId = tabActiveId;
    }
}
