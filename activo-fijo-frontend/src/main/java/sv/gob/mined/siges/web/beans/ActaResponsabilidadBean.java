/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.beans;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
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
import sv.gob.mined.siges.web.excepciones.BusinessException;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCodiguera;
import sv.gob.mined.siges.web.lazymodels.LazyBienesDepreciablesDataModel;
import sv.gob.mined.siges.web.mensajes.Mensajes;
import sv.gob.mined.siges.web.restclient.BienesDepreciablesRestClient;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class ActaResponsabilidadBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ActaResponsabilidadBean.class.getName());

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private BienesDepreciablesRestClient bienesRestClient;
    
    @Inject 
    private FiltrosBienesBean filtrosBienesBean; 
    
    @Inject
    private ApplicationBean applicationBean;
    
    FiltroBienesDepreciables filtroBienes;
     
    TipoUnidad tiposUnidad = TipoUnidad.UNIDAD_ADMINISTRATIVA;
    
    private FiltroCodiguera filtro;
    
    private LazyBienesDepreciablesDataModel bienesDepreciablesLazyModel;
    private Long totalResultados = 0L;
    private Integer paginado = 10;
    private String reponsableAF;
    private String cargoReponsable;
    private String urlReporte="";
    private String tipoUnidad="";
    public ActaResponsabilidadBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_ACTA_RESPONSABILIDAD)) {
            JSFUtils.redirectToIndex();
        }
    }
    
    public void buscar() {
        try {
            filtroBienes = filtrosBienesBean.getFiltroBienes();
            
            filtroBienes.setAscending(new boolean[]{true});
            filtroBienes.setOrderBy(new String[]{"bdeCodigoInventario"});
            filtroBienes.setIncluirCampos(new String[]{"bdeCodigoInventario","bdeDescripcion","bdeMarca","bdeNoSerie","bdeFechaAdquisicion","bdeValorAdquisicion","bdeEstadoCalidad.ecaNombre","bdeVersion"});

            filtroBienes.setTipoUnidad(TipoUnidad.UNIDAD_ADMINISTRATIVA);

            filtroBienes.setUnidadAdministrativaId(filtrosBienesBean.getUnidadSeleccionada() != null ? filtrosBienesBean.getUnidadSeleccionada().getUadPk() : null);

            filtroBienes.setEmpleadoId(filtrosBienesBean.getEmpleadoSeleccionado() != null ? filtrosBienesBean.getEmpleadoSeleccionado().getEmpPk() : null);
            if(validar(filtroBienes)) {
                totalResultados = bienesRestClient.buscarTotal(filtroBienes);

                bienesDepreciablesLazyModel = new LazyBienesDepreciablesDataModel(bienesRestClient, filtroBienes, totalResultados, paginado);
            }
            
        }  catch (BusinessException be) {
            LOGGER.log(Level.SEVERE, be.getMessage(), be);
            JSFUtils.agregarMensajes(ConstantesComponentesId.ID_MSG_TEMPLATE, FacesMessage.SEVERITY_ERROR, be.getErrores());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, LoggingUtils.getMessage(this.sessionBean.getUser(), ex), ex);
            JSFUtils.agregarError(ConstantesComponentesId.ID_MSG_TEMPLATE, Mensajes.obtenerMensaje(Mensajes.ERROR_GENERAL), "");
        }
    }
    public  Boolean validar(FiltroBienesDepreciables filtro) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (filtro == null) {
            ge.addError(Mensajes.ERROR_FILTRO_DATO_VACIO);
        } else {    
            if (filtro.getUnidadActivoFijoId()== null) {
               ge.addError("unidadActivoFijoId", Mensajes.ERROR_FILTRO_UNIDAD_ACTIVO_FIJO_VACIO);
            }
            
            if (filtro.getUnidadAdministrativaId()== null) {
               ge.addError("unidadAdministrativaId", Mensajes.ERROR_FILTRO_UNIDAD_ADMINISTRATIVA_VACIO);
            }
            
            if (filtro.getEmpleadoId()== null) {
               ge.addError("empleadoId", Mensajes.ERROR_FILTRO_EMPLEADO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    public void limpiar() {
        filtrosBienesBean.limpiar();
        filtrosBienesBean.getComboTiposUnidad().setSelectedT(TipoUnidad.UNIDAD_ADMINISTRATIVA);
        filtrosBienesBean.setTipoUnidadSeleccionada(TipoUnidad.UNIDAD_ADMINISTRATIVA);
        bienesDepreciablesLazyModel = null;
        totalResultados = null;
    }

    public void generarReporte() {
        filtroBienes = filtrosBienesBean.getFiltroBienes();
        this.urlReporte = applicationBean.getReportGeneratorUrl() + ConstantesPaginas.REPORTE_ACTA_RESPONSABILIDAD_R+ "?";
        try {
            this.urlReporte += "uaf=" + (filtroBienes.getUnidadActivoFijoId() != null ? filtroBienes.getUnidadActivoFijoId() : "");
        
            if(filtroBienes.getTipoUnidad() != null) {
                if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtroBienes.getTipoUnidad())) {
                     this.urlReporte += "&uad=" + (filtroBienes.getUnidadAdministrativaId() != null ? filtroBienes.getUnidadAdministrativaId() : "") + "&sed=" ;
                } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtroBienes.getTipoUnidad())) {
                     this.urlReporte += "&uad=";
                }
            } else {
                 this.urlReporte += "&uad=";
            }
            this.urlReporte += "&empId=" + (filtrosBienesBean.getEmpleadoSeleccionado() != null ? filtrosBienesBean.getEmpleadoSeleccionado().getEmpPk() : "") +
                    "&resAf=" + (this.reponsableAF != null ? this.reponsableAF.trim() : "") +
                    "&cargo=" + (this.cargoReponsable != null ? this.cargoReponsable.trim() : "");
            
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(this.urlReporte);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    public void nuevoReporte() {
        cargoReponsable = "";
        reponsableAF = "";
    }
    
    public void generarNuevo() {
        this.tipoUnidad="";
        filtroBienes = filtrosBienesBean.getFiltroBienes();
        if(filtroBienes.getTipoUnidad() != null) {
            if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtroBienes.getTipoUnidad())) {
                  this.tipoUnidad="ua";
            } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtroBienes.getTipoUnidad())) {
                  this.tipoUnidad="ce";
            }
        } 
    }
    
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public FiltroBienesDepreciables getFiltroBienes() {
        return filtroBienes;
    }

    public void setFiltroBienes(FiltroBienesDepreciables filtroBienes) {
        this.filtroBienes = filtroBienes;
    }
    
    public FiltroCodiguera getFiltro() {
        return filtro;
    }

    public void setFiltro(FiltroCodiguera filtro) {
        this.filtro = filtro;
    }

    public LazyBienesDepreciablesDataModel getBienesDepreciablesLazyModel() {
        return bienesDepreciablesLazyModel;
    }

    public void setBienesDepreciablesLazyModel(LazyBienesDepreciablesDataModel bienesDepreciablesLazyModel) {
        this.bienesDepreciablesLazyModel = bienesDepreciablesLazyModel;
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

    public TipoUnidad getTiposUnidad() {
        return tiposUnidad;
    }

    public void setTiposUnidad(TipoUnidad tiposUnidad) {
        this.tiposUnidad = tiposUnidad;
    }

    public String getReponsableAF() {
        return reponsableAF;
    }

    public void setReponsableAF(String reponsableAF) {
        this.reponsableAF = reponsableAF;
    }

    public String getCargoReponsable() {
        return cargoReponsable;
    }

    public void setCargoReponsable(String cargoReponsable) {
        this.cargoReponsable = cargoReponsable;
    }

    public FiltrosBienesBean getFiltrosBienesBean() {
        return filtrosBienesBean;
    }

    public void setFiltrosBienesBean(FiltrosBienesBean filtrosBienesBean) {
        this.filtrosBienesBean = filtrosBienesBean;
    }

    public String getUrlReporte() {
        return urlReporte;
    }

    public void setUrlReporte(String urlReporte) {
        this.urlReporte = urlReporte;
    }

    public String getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(String tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }
    
    
}
