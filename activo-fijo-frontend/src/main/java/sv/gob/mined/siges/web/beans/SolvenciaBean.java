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
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.enumerados.TipoUnidad;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroBienesDepreciables;
import sv.gob.mined.siges.web.utilidades.JSFUtils;

@Named
@ViewScoped
public class SolvenciaBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(SolvenciaBean.class.getName());

    @Inject
    private SessionBean sessionBean;
    
    @Inject
    private ApplicationBean applicationBean;
    
    @Inject 
    private FiltrosBienesBean filtrosBienesBean; 
    private FiltroBienesDepreciables filtroBienes;

    private Integer periodo;
    private String urlReporte="";
    private String tipoUnidad="";
    public SolvenciaBean() {
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
        if (!sessionBean.getOperaciones().contains(ConstantesOperaciones.MENU_REPORTE_SOLVENCIA)) {
            JSFUtils.redirectToIndex();
        }
    }

    public void generarNuevo() throws Exception {
        filtroBienes = filtrosBienesBean.getFiltroBienes();
        this.tipoUnidad="";
        this.periodo = null;
        if(filtroBienes.getTipoUnidad() != null) {
            if(TipoUnidad.UNIDAD_ADMINISTRATIVA.equals(filtroBienes.getTipoUnidad())) {
                  this.tipoUnidad="ua";
            } else if(TipoUnidad.CENTRO_ESCOLAR.equals(filtroBienes.getTipoUnidad())) {
                  this.tipoUnidad="ce";
            }
        }  
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

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
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
