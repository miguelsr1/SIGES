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
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.constantes.ConstantesPaginas;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class NotificacionIncumplimientoBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(NotificacionIncumplimientoBean.class.getName());

    @Inject
    private SessionBean sessionBean;
    
    @Inject 
    private FiltrosBienesBean filtrosBienesBean; 
    
    @Inject
    private ApplicationBean applicationBean;
    
    private Long notificacionId;
    private String tecnicoAF;
    private String urlReporte;
    private FiltroBienesDepreciables filtroBienes;
    
    public NotificacionIncumplimientoBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_NOTIFICACION_CUMPLIMIENTO)) {
            JSFUtils.redirectToIndex();
        }
    }
    
   public void limpiar() {
        filtroBienes = filtrosBienesBean.getFiltroBienes();
        filtrosBienesBean.limpiar();
        notificacionId = null;
    }
    
    public void notificar() {
        
    }
    
    public void generarReporte() {
        filtroBienes = filtrosBienesBean.getFiltroBienes();
        this.urlReporte = applicationBean.getReportGeneratorUrl() + ConstantesPaginas.REPORTE_NOTIFICACION_INCUMPLIMIENTO_R+ "?";
        try {
            this.urlReporte += "uaf=" + (filtroBienes.getUnidadActivoFijoId() != null ? filtroBienes.getUnidadActivoFijoId() : "") 
                    + "&notificacionId=" + (this.notificacionId != null ? this.notificacionId : "") 
                    + "&tecnivoAF="+ (this.tecnicoAF != null ? this.tecnicoAF.trim() : "");
            if(filtroBienes.getTipoUnidad() != null) {
                if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtroBienes.getTipoUnidad())) {
                     this.urlReporte += "&uad=" + (filtroBienes.getUnidadAdministrativaId() != null ? filtroBienes.getUnidadAdministrativaId() : "") + "&sed=" ;
                } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtroBienes.getTipoUnidad())) {
                     this.urlReporte += "&uad=&sed=" + (filtroBienes.getUnidadAdministrativaId() != null ? filtroBienes.getUnidadAdministrativaId() : "");
                }
            } else {
                 this.urlReporte += "&uad=&sed=";
            }
            
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.redirect(this.urlReporte);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            JSFUtils.redirectToIndex();
        }
    }
    
    
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public FiltrosBienesBean getFiltrosBienesBean() {
        return filtrosBienesBean;
    }

    public void setFiltrosBienesBean(FiltrosBienesBean filtrosBienesBean) {
        this.filtrosBienesBean = filtrosBienesBean;
    }

    public Long getNotificacionId() {
        return notificacionId;
    }

    public void setNotificacionId(Long notificacionId) {
        this.notificacionId = notificacionId;
    }

    public FiltroBienesDepreciables getFiltroBienes() {
        return filtroBienes;
    }

    public void setFiltroBienes(FiltroBienesDepreciables filtroBienes) {
        this.filtroBienes = filtroBienes;
    }

    public String getTecnicoAF() {
        return tecnicoAF;
    }

    public void setTecnicoAF(String tecnicoAF) {
        this.tecnicoAF = tecnicoAF;
    }

    public String getUrlReporte() {
        return urlReporte;
    }

    public void setUrlReporte(String urlReporte) {
        this.urlReporte = urlReporte;
    }
}
