/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.component.datatable.DataTable;
import javax.inject.Named;
import org.apache.commons.lang3.SerializationUtils;
import org.primefaces.component.api.UIColumn;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.Constantes;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.RevHistorico;
import sv.gob.mined.siges.web.dto.SolicitudDescargo;
import sv.gob.mined.siges.web.dto.SgAfDescargo;
import sv.gob.mined.siges.web.dto.catalogo.SgAfEstadosBienes;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.excepciones.HttpClientException;
import sv.gob.mined.siges.web.excepciones.HttpServerException;
import sv.gob.mined.siges.web.excepciones.HttpServerUnavailableException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyDescargosDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.CatalogosRestClient;
import sv.gob.mined.siges.web.restclient.DescargosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class DescargosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DescargosBean.class.getName());
    
    @Inject
    private SessionBean sessionBean;

    @Inject 
    private FiltrosBienesBean filtrosBienesBean;   

    @Inject
    private CatalogosRestClient catalogosRestClient;
    
    private SgAfDescargo entidadEnEdicion;
    private LazyDescargosDataModel descargosLazyModel;
    private Integer paginado = 10;
    private Long totalResultados = 0L;
    private FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();
    private List<RevHistorico> historial = new ArrayList();
    private SgAfEstadosBienes estadoExistente;
    private SgAfEstadosBienes estadoEnSolicitud;
    private SgAfEstadosBienes estadoRechazado;
    private FiltroCodiguera filtro;
    
    @Inject
    private DescargosRestClient descargosRestClient;
    
    private final Map<String, Boolean> colVisibilityMap = new HashMap();
    private final Map<Integer, String> colIndexMap = new HashMap();
    
    @PostConstruct
    public void init() {
        try {
            validarAcceso();
            cargar();
            cargarColumnas();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    public Map<String, Boolean> getColVisibilityMap() {
        return Collections.unmodifiableMap(colVisibilityMap);
    }

    private String getColumnId(String fullId) {
        String[] idParts = fullId.split(":");
        return idParts[idParts.length - 1];
    }
    
    public void cargar() throws HttpServerException, HttpClientException, HttpServerUnavailableException, BusinessException {
        filtro = new FiltroCodiguera();
            filtro.setHabilitado(Boolean.TRUE);
            filtro.setOrderBy(new String[]{"ebiCodigo"});
            filtro.setIncluirCampos(new String[]{"ebiNombre","ebiCodigo","ebiVersion"});
            List<SgAfEstadosBienes> estadosBien = catalogosRestClient.buscarEstadosBienes(filtro);
            if(estadosBien != null && !estadosBien.isEmpty()) {
                for(SgAfEstadosBienes estado : estadosBien) {
                    if(Constantes.CODIGO_ESTADO_EXISTENTE.equals(estado.getEbiCodigo().trim())) {
                        estadoExistente = estado;
                    } else if(Constantes.CODIGO_ESTADO_SOLICITUD_DESCARGO.equals(estado.getEbiCodigo().trim())) {
                        estadoEnSolicitud = estado;
                    } else if(Constantes.CODIGO_ESTADO_SOLICITUD_RECHAZADA.equals(estado.getEbiCodigo().trim())) {
                        estadoRechazado = estado;
                    }
                }
                
            }
    }
    
    
    public void cargarColumnas() {
        FacesContext context = FacesContext.getCurrentInstance();
        DataTable table = (DataTable) context.getViewRoot().findComponent(":form:basicDT");
        List<UIColumn> columns = table.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            final String columnId = this.getColumnId(columns.get(i).getClientId());
            colIndexMap.put(i, columnId);
            colVisibilityMap.put(columnId, true);
        }
    }
    
    public void onColumnReorder(AjaxBehaviorEvent e) {
        List<UIColumn> columns = ((DataTable) e.getSource()).getColumns();
        for (int i = 0; i < columns.size(); i++) {
            this.colIndexMap.put(i, this.getColumnId(columns.get(i).getClientId()));
        }
    }

    public void onToggle(ToggleEvent e) {
        this.colVisibilityMap.put(this.colIndexMap.get((Integer) e.getData()), e.getVisibility() == Visibility.VISIBLE);
    }
    
    public void buscar() {
        try {
            filtroBienes = filtrosBienesBean.getFiltroBienes();
            filtroBienes.setIncluirCampos(new String[]{"desPk","desVersion","desCodigoDescargo","desFechaSolicitud","desFechaDescargo","desTipoDescargoFk.edeNombre","desTipoDescargoFk.edeVersion",
                                            "desOpcionDescargoFk.odeNombre","desOpcionDescargoFk.odeVersion","desUnidadAdministrativaFk.uadNombre","desUnidadAdministrativaFk.uadVersion",
                                            "desSedeFk.sedTipo","desSedeFk.sedNombre","desSedeFk.sedVersion","desEstadoFk.ebiPk","desEstadoFk.ebiCodigo","desEstadoFk.ebiNombre","desEstadoFk.ebiVersion"});
            filtroBienes.setObtenerCantidad(Boolean.TRUE);
            filtroBienes.setMunicipioId(filtrosBienesBean.getComboMunicipios() != null && filtrosBienesBean.getComboMunicipios().getSelectedT() != null ? filtrosBienesBean.getComboMunicipios().getSelectedT().getMunPk(): null);

            filtroBienes.setTipoUnidad(filtrosBienesBean.getComboTiposUnidad() != null ? filtrosBienesBean.getComboTiposUnidad().getSelectedT() : null);
     
            filtroBienes.setEstadoId(filtrosBienesBean.getComboEstadosBienes() != null && filtrosBienesBean.getComboEstadosBienes().getSelectedT() != null ? filtrosBienesBean.getComboEstadosBienes().getSelectedT().getEbiPk() : null );
            
            filtroBienes.setValidarEstadoSolicitudVacio(Boolean.TRUE); //Para que filtre tambien por el estado de la solicitud vacio(NULL), es decir que no esté en ninguna solicitud, este filtro va de la mano con el estadoId
            
            if(filtrosBienesBean.getUnidadAFSeleccionada() != null) {
                filtroBienes.setUnidadActivoFijoId(filtrosBienesBean.getUnidadAFSeleccionada().getUafPk());
                if(filtrosBienesBean.getUnidadAFSeleccionada().getUafDepartamento() != null 
                    && filtrosBienesBean.getUnidadAFSeleccionada().getUafDepartamento().getDepPk() != null) {
                    filtroBienes.setDepartamentoId(filtrosBienesBean.getUnidadAFSeleccionada().getUafDepartamento().getDepPk());
                }
            }

            if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtrosBienesBean.getComboTiposUnidad())) {
                filtroBienes.setUnidadAdministrativaId(filtrosBienesBean.getUnidadSeleccionada() != null ? filtrosBienesBean.getUnidadSeleccionada().getUadPk() : null);
            } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtrosBienesBean.getComboTiposUnidad())) {
                filtroBienes.setUnidadAdministrativaId(filtrosBienesBean.getSedeSeleccionada() != null ? filtrosBienesBean.getSedeSeleccionada().getSedPk() : null);
            }
            totalResultados = descargosRestClient.buscarTotal(filtroBienes);
            
            descargosLazyModel = new LazyDescargosDataModel(descargosRestClient, filtroBienes, totalResultados, paginado);

        }  catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void actualizar(SgAfDescargo var) {
        try {
            JSFUtils.limpiarMensajesError();
            entidadEnEdicion = (SgAfDescargo) SerializationUtils.clone(var);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    public void limpiar() {
        filtroBienes = new FiltroBienesDepreciables();
        filtrosBienesBean.limpiar();
        descargosLazyModel = null;
        totalResultados = 0L;
    }
    public void eliminar() {
        try {
            SolicitudDescargo solicitud = new SolicitudDescargo();
            //solicitud.setDescargo(entidadEnEdicion);
            solicitud.setId(entidadEnEdicion.getDesPk());
            solicitud.setEstado(estadoExistente);
            descargosRestClient.eliminar(solicitud);
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
    
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_DESCARGO_BIENES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public String cargarHistorial(Long id) {
        try {
            historial = descargosRestClient.obtenerHistorialPorId(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
        return null;
    }
    
    public Boolean solicitudDescargoPuedeEliminarse(SgAfDescargo descargo) {
        if(sessionBean.getOperaciones().contains(ConstantesOperaciones.ELIMINAR_SOLICITUD_PROCESO_DESCARGO)) { 
            if(descargo != null && descargo.getDesEstadoFk() != null && descargo.getDesEstadoFk().getEbiPk() != null 
                    && ((estadoEnSolicitud != null && estadoEnSolicitud.getEbiPk() != null && descargo.getDesEstadoFk().getEbiPk().equals(estadoEnSolicitud.getEbiPk())))
                    || (estadoRechazado != null && estadoRechazado.getEbiPk() != null && descargo.getDesEstadoFk().getEbiPk().equals(estadoRechazado.getEbiPk()))) {
                return Boolean.TRUE;
            }
        }
        
        
        return Boolean.FALSE;
    }
    
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
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

    public LazyDescargosDataModel getDescargosLazyModel() {
        return descargosLazyModel;
    }

    public void setDescargosLazyModel(LazyDescargosDataModel descargosLazyModel) {
        this.descargosLazyModel = descargosLazyModel;
    }

    public FiltroBienesDepreciables getFiltroBienes() {
        return filtroBienes;
    }

    public void setFiltroBienes(FiltroBienesDepreciables filtroBienes) {
        this.filtroBienes = filtroBienes;
    }

    public List<RevHistorico> getHistorial() {
        return historial;
    }

    public void setHistorial(List<RevHistorico> historial) {
        this.historial = historial;
    }

    public SgAfDescargo getEntidadEnEdicion() {
        return entidadEnEdicion;
    }

    public void setEntidadEnEdicion(SgAfDescargo entidadEnEdicion) {
        this.entidadEnEdicion = entidadEnEdicion;
    }

    public SgAfEstadosBienes getEstadoExistente() {
        return estadoExistente;
    }

    public void setEstadoExistente(SgAfEstadosBienes estadoExistente) {
        this.estadoExistente = estadoExistente;
    }

    public SgAfEstadosBienes getEstadoEnSolicitud() {
        return estadoEnSolicitud;
    }

    public void setEstadoEnSolicitud(SgAfEstadosBienes estadoEnSolicitud) {
        this.estadoEnSolicitud = estadoEnSolicitud;
    }

    public SgAfEstadosBienes getEstadoRechazado() {
        return estadoRechazado;
    }

    public void setEstadoRechazado(SgAfEstadosBienes estadoRechazado) {
        this.estadoRechazado = estadoRechazado;
    }
    
    
}