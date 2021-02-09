/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.lazymodels.LazyDepreciacionSubcuentasContablesDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DepreciacionReportesRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class DepreciacionReportesBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DepreciacionReportesBean.class.getName());

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    DepreciacionReportesRestClient reportesRestClient;
    
    
    @Inject 
    private FiltrosBienesBean filtrosBienesBean; 
    
    private FiltroBienesDepreciables filtroBienes;

    private LazyDepreciacionSubcuentasContablesDataModel depreciacionSubcuentasContablesDataModel;
    private Long totalResultados = 0L;
    private Integer paginado = 10;
    private String tipoUnidad = "";
    
    public DepreciacionReportesBean() {
    }

    @PostConstruct
    public void init() {
        try {
            validarAcceso();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
            
        }
    }
    
    public void validarAcceso() {
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_BIENES_SUBCUENTAS_CONTABLES)) {
            JSFUtils.redirectToIndex();
        }
    }
    
     public void limpiar() {
        filtroBienes = new FiltroBienesDepreciables();
        filtrosBienesBean.limpiar();
        depreciacionSubcuentasContablesDataModel = null;
        totalResultados = 0L;
    }
    
    public void buscar() {
        try {
            filtroBienes = new FiltroBienesDepreciables();
            
            filtroBienes.setTipoUnidad(filtrosBienesBean.getComboTiposUnidad() != null ? filtrosBienesBean.getComboTiposUnidad().getSelectedT() : null);
            if(filtrosBienesBean.getUnidadAFSeleccionada() != null) {
                filtroBienes.setUnidadActivoFijoId(filtrosBienesBean.getUnidadAFSeleccionada().getUafPk());
                if(filtrosBienesBean.getUnidadAFSeleccionada().getUafDepartamento() != null 
                    && filtrosBienesBean.getUnidadAFSeleccionada().getUafDepartamento().getDepPk() != null) {
                    filtroBienes.setDepartamentoId(filtrosBienesBean.getUnidadAFSeleccionada().getUafDepartamento().getDepPk());
                }
            }
            
            if(filtrosBienesBean.getComboTiposUnidad() != null && filtrosBienesBean.getComboTiposUnidad().getSelectedT() != null) {
                if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtrosBienesBean.getComboTiposUnidad().getSelectedT())) {
                    filtroBienes.setUnidadAdministrativaId(filtrosBienesBean.getUnidadSeleccionada() != null ? filtrosBienesBean.getUnidadSeleccionada().getUadPk() : null);
                    this.tipoUnidad = "ua";
                } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtrosBienesBean.getComboTiposUnidad().getSelectedT())) {
                    filtroBienes.setUnidadAdministrativaId(filtrosBienesBean.getSedeSeleccionada() != null ? filtrosBienesBean.getSedeSeleccionada().getSedPk() : null);
                    this.tipoUnidad = "ce" ;
                }
            }
            
            
            filtroBienes.setClasificacionId(filtrosBienesBean.getComboClasificacionBienes() != null && filtrosBienesBean.getComboClasificacionBienes().getSelectedT() != null ? filtrosBienesBean.getComboClasificacionBienes().getSelectedT().getCbiPk(): null);
            filtroBienes.setCategoriaId(filtrosBienesBean.getComboCategoriaBienes() != null && filtrosBienesBean.getComboCategoriaBienes().getSelectedT() != null ? filtrosBienesBean.getComboCategoriaBienes().getSelectedT().getCabPk() : null);

            filtroBienes.setFuenteId(filtrosBienesBean.getComboFuenteFinanciamiento() != null && filtrosBienesBean.getComboFuenteFinanciamiento().getSelectedT() != null ? filtrosBienesBean.getComboFuenteFinanciamiento().getSelectedT().getFfiPk(): null);
            filtroBienes.setProyectoId(filtrosBienesBean.getComboProyectos()!= null && filtrosBienesBean.getComboProyectos().getSelectedT() != null ? filtrosBienesBean.getComboProyectos().getSelectedT().getProPk() : null);

            
            filtroBienes.setFechaAdquisicionDesde(filtrosBienesBean.getFiltroBienes().getFechaAdquisicionDesde());
            filtroBienes.setFechaAdquisicionHasta(filtrosBienesBean.getFiltroBienes().getFechaAdquisicionHasta());
            
            filtroBienes.setValorAdquisicionDesde(filtrosBienesBean.getFiltroBienes().getValorAdquisicionDesde());
            filtroBienes.setValorAdquisicionHasta(filtrosBienesBean.getFiltroBienes().getValorAdquisicionHasta());
            filtroBienes.setActivos(filtrosBienesBean.getFiltroBienes().getActivos());

            totalResultados = reportesRestClient.buscarTotal(filtroBienes);

            depreciacionSubcuentasContablesDataModel = new LazyDepreciacionSubcuentasContablesDataModel(reportesRestClient, filtroBienes, totalResultados, paginado);
        }  catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public DepreciacionReportesRestClient getReportesRestClient() {
        return reportesRestClient;
    }

    public void setReportesRestClient(DepreciacionReportesRestClient reportesRestClient) {
        this.reportesRestClient = reportesRestClient;
    }

    public FiltrosBienesBean getFiltrosBienesBean() {
        return filtrosBienesBean;
    }

    public void setFiltrosBienesBean(FiltrosBienesBean filtrosBienesBean) {
        this.filtrosBienesBean = filtrosBienesBean;
    }

    public FiltroBienesDepreciables getFiltroBienes() {
        return filtroBienes;
    }

    public void setFiltroBienes(FiltroBienesDepreciables filtroBienes) {
        this.filtroBienes = filtroBienes;
    }

    public LazyDepreciacionSubcuentasContablesDataModel getDepreciacionSubcuentasContablesDataModel() {
        return depreciacionSubcuentasContablesDataModel;
    }

    public void setDepreciacionSubcuentasContablesDataModel(LazyDepreciacionSubcuentasContablesDataModel depreciacionSubcuentasContablesDataModel) {
        this.depreciacionSubcuentasContablesDataModel = depreciacionSubcuentasContablesDataModel;
    }

    public Long getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(Long totalResultados) {
        this.totalResultados = totalResultados;
    }

    public Integer getPaginado() {
        return paginado;
    }

    public void setPaginado(Integer paginado) {
        this.paginado = paginado;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }
}
