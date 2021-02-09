/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.utils.LoggingUtils;
import sv.gob.mined.siges.web.constantes.ConstantesComponentesId;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroDescargosDetalle;
import sv.gob.mined.siges.web.lazymodels.LazyDetalleDescargoDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.DescargosRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class ReporteDescargosBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ConstanciaBean.class.getName());

    @Inject
    private SessionBean sessionBean;
    
    @Inject 
    private FiltrosBienesBean filtrosBienesBean;
    private FiltroDescargosDetalle filtroDetalleDescargo;
    private FiltroBienesDepreciables filtroBienes = new FiltroBienesDepreciables();
            
    @Inject
    private DescargosRestClient descargosRestClient;
    
    @Inject
    private ApplicationBean applicationBean;
    
    private LazyDetalleDescargoDataModel detalleDescargoDataModel;
    private Integer paginado = 10;
    private Long totalResultados = 0L;
    private String tipoUnidad = "";
    private String urlReporte="";
    public ReporteDescargosBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_DESCARGO_BIENES)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void buscar() {
        try {
            filtroDetalleDescargo = new FiltroDescargosDetalle();
            
            filtroDetalleDescargo.setMunicipioId(filtrosBienesBean.getComboMunicipios() != null && filtrosBienesBean.getComboMunicipios().getSelectedT() != null ? filtrosBienesBean.getComboMunicipios().getSelectedT().getMunPk(): null);

            filtroDetalleDescargo.setTipoUnidad(filtrosBienesBean.getComboTiposUnidad() != null ? filtrosBienesBean.getComboTiposUnidad().getSelectedT() : null);
     
            filtroDetalleDescargo.setEstadoId(filtrosBienesBean.getComboEstadosBienes() != null && filtrosBienesBean.getComboEstadosBienes().getSelectedT() != null ? filtrosBienesBean.getComboEstadosBienes().getSelectedT().getEbiPk() : null );
            
            filtroDetalleDescargo.setClasificacionId(filtrosBienesBean.getComboClasificacionBienes() != null && filtrosBienesBean.getComboClasificacionBienes().getSelectedT() != null ? filtrosBienesBean.getComboClasificacionBienes().getSelectedT().getCbiPk() : null );
            filtroDetalleDescargo.setCategoriaId(filtrosBienesBean.getComboCategoriaBienes() != null && filtrosBienesBean.getComboCategoriaBienes().getSelectedT() != null ? filtrosBienesBean.getComboCategoriaBienes().getSelectedT().getCabPk() : null );
            
            if(filtrosBienesBean.getUnidadAFSeleccionada() != null) {
                filtroDetalleDescargo.setUnidadActivoFijoId(filtrosBienesBean.getUnidadAFSeleccionada().getUafPk());
                if(filtrosBienesBean.getUnidadAFSeleccionada().getUafDepartamento() != null 
                    && filtrosBienesBean.getUnidadAFSeleccionada().getUafDepartamento().getDepPk() != null) {
                    filtroDetalleDescargo.setDepartamentoId(filtrosBienesBean.getUnidadAFSeleccionada().getUafDepartamento().getDepPk());
                }
            }
           if(filtrosBienesBean.getComboTiposUnidad() != null && filtrosBienesBean.getComboTiposUnidad().getSelectedT() != null) {
               if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtrosBienesBean.getComboTiposUnidad().getSelectedT())) {
                    filtroDetalleDescargo.setUnidadAdministrativaId(filtrosBienesBean.getUnidadSeleccionada() != null ? filtrosBienesBean.getUnidadSeleccionada().getUadPk() : null);
                    this.tipoUnidad = "ua";
                } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtrosBienesBean.getComboTiposUnidad().getSelectedT())) {
                    filtroDetalleDescargo.setUnidadAdministrativaId(filtrosBienesBean.getSedeSeleccionada() != null ? filtrosBienesBean.getSedeSeleccionada().getSedPk() : null);
                    this.tipoUnidad = "ce" ;
                }
           }

            filtroDetalleDescargo.setActivos(filtrosBienesBean.getFiltroBienes().getActivos());
            filtroDetalleDescargo.setCodigoInventario(filtrosBienesBean.getFiltroBienes().getCodigoInventario());
            filtroDetalleDescargo.setCodigoDescargo(filtrosBienesBean.getFiltroBienes().getCodigoDescargo());
            filtroDetalleDescargo.setFechaDescargoDesde(filtrosBienesBean.getFiltroBienes().getFechaDescargoDesde());
            filtroDetalleDescargo.setFechaDescargoHasta(filtrosBienesBean.getFiltroBienes().getFechaDescargoHasta());
            
            filtroDetalleDescargo.setFechaSolicitudDesde(filtrosBienesBean.getFiltroBienes().getFechaSolicitudDesde());
            filtroDetalleDescargo.setFechaSolicitudHasta(filtrosBienesBean.getFiltroBienes().getFechaSolicitudHasta());
            
            filtroDetalleDescargo.setAscending(new boolean[]{true});
            filtroDetalleDescargo.setOrderBy(new String[]{"codigoInventario"});
            totalResultados = descargosRestClient.buscarTotalDetalleDTO(filtroDetalleDescargo);

            detalleDescargoDataModel = new LazyDetalleDescargoDataModel(descargosRestClient, filtroDetalleDescargo, totalResultados, paginado);
        }  catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    
    public void generarReporte() {
        this.urlReporte = applicationBean.getReportGeneratorUrl() + ConstantesPaginas.REPORTE_DESCARGO + "?";
        try {
            this.urlReporte += "uaf=" + (filtroDetalleDescargo.getUnidadActivoFijoId() != null ? filtroDetalleDescargo.getUnidadActivoFijoId() : "");
        
            if(filtroDetalleDescargo.getTipoUnidad() != null) {
                if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtroDetalleDescargo.getTipoUnidad())) {
                     this.urlReporte += "&uad=" + (filtroDetalleDescargo.getUnidadAdministrativaId() != null ? filtroDetalleDescargo.getUnidadAdministrativaId() : "") + "&sed=&tu=ua";
                } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtroDetalleDescargo.getTipoUnidad())) {
                     this.urlReporte += "&uad=&sed=" + (filtroDetalleDescargo.getUnidadAdministrativaId() != null ? filtroDetalleDescargo.getUnidadAdministrativaId() : "") + "&tu=ce";
                }
            } else {
                 this.urlReporte += "&uad=&sed=&tu=";
            }
            this.urlReporte += "&depId=" + (filtroDetalleDescargo.getDepartamentoId() != null ? filtroDetalleDescargo.getDepartamentoId() : "")
                    + "&munId=" + (filtroDetalleDescargo.getMunicipioId() != null ? filtroDetalleDescargo.getMunicipioId() : "")
                    + "&estadoId=" + (filtroDetalleDescargo.getEstadoId() != null ? filtroDetalleDescargo.getEstadoId() : "")
                    
                    + "&clasId=" + (filtroDetalleDescargo.getClasificacionId() != null ? filtroDetalleDescargo.getClasificacionId() : "")
                    + "&catId=" + (filtroDetalleDescargo.getCategoriaId() != null ? filtroDetalleDescargo.getCategoriaId() : "")
                    + "&codInventario=" + (filtroDetalleDescargo.getCodigoInventario() != null ? filtroDetalleDescargo.getCodigoInventario().trim() : "")
                    + "&fsolDesde=" + (filtroDetalleDescargo.getFechaSolicitudDesde() != null ? filtroDetalleDescargo.getFechaSolicitudDesde() : "")
                    + "&fsolHasta=" + (filtroDetalleDescargo.getFechaSolicitudHasta() != null ? filtroDetalleDescargo.getFechaSolicitudHasta() : "")
                    + "&activos=" + (filtroDetalleDescargo.getActivos() != null ? filtroDetalleDescargo.getActivos() : "")
                    + "&tdesId=" + (filtroDetalleDescargo.getTipoDescargo() != null ? filtroDetalleDescargo.getTipoDescargo() : "")
                    + "&codDes=" + (filtroDetalleDescargo.getCodigoDescargo() != null ? filtroDetalleDescargo.getCodigoDescargo().trim() : "")
                    + "&nacta=" + (filtroDetalleDescargo.getCodigoDescargo() != null ? filtroDetalleDescargo.getCodigoDescargo().trim() : "")
                    + "&fdesDesde=" + (filtroDetalleDescargo.getFechaDescargoDesde() != null ? filtroDetalleDescargo.getFechaDescargoDesde() : "")
                    + "&fdesHasta=" + (filtroDetalleDescargo.getFechaDescargoHasta() != null ? filtroDetalleDescargo.getFechaDescargoHasta() : "")
                    + "&op=2";
            
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(this.urlReporte);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    public void limpiar() {
        filtroDetalleDescargo = new FiltroDescargosDetalle();
        filtrosBienesBean.limpiar();
        detalleDescargoDataModel = null;
        totalResultados = 0L;
    }

    public FiltrosBienesBean getFiltrosBienesBean() {
        return filtrosBienesBean;
    }

    public void setFiltrosBienesBean(FiltrosBienesBean filtrosBienesBean) {
        this.filtrosBienesBean = filtrosBienesBean;
    }

    public FiltroDescargosDetalle getFiltroDetalleDescargo() {
        return filtroDetalleDescargo;
    }

    public void setFiltroDetalleDescargo(FiltroDescargosDetalle filtroDetalleDescargo) {
        this.filtroDetalleDescargo = filtroDetalleDescargo;
    }

    public LazyDetalleDescargoDataModel getDetalleDescargoDataModel() {
        return detalleDescargoDataModel;
    }

    public void setDetalleDescargoDataModel(LazyDetalleDescargoDataModel detalleDescargoDataModel) {
        this.detalleDescargoDataModel = detalleDescargoDataModel;
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

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public FiltroBienesDepreciables getFiltroBienes() {
        return filtroBienes;
    }

    public void setFiltroBienes(FiltroBienesDepreciables filtroBienes) {
        this.filtroBienes = filtroBienes;
    }

    public String getUrlReporte() {
        return urlReporte;
    }

    public void setUrlReporte(String urlReporte) {
        this.urlReporte = urlReporte;
    }
    
    
}
