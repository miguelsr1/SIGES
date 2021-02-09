/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ImpuestoDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la página
 * de retenciones de impuesto por proveedor
 * 
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "retencionDeImpuestoPorProveedor")
public class RetencionesDeImpuestoPorProveedor implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    ReporteDelegate reporteDelegate;
    @Inject
    ImpuestoDelegate impuestoDelegate;
    @Inject
    TextMB textMB;

    private boolean update = false;

    
    private Integer idImpuesto;
    private Integer anio;
    private Integer mes;

    
    @PostConstruct
    public void init() {
    }


    public String cerrar() {
        return "inicio.xhtml?faces-redirect=true";
    }

   
   /**
    * Este método es el encargado de generar las retenciones de impuesto por proveedor
    */ 
    public void generarRetencionDeImpuestos() {
        try {
            if(mes == -1){
               mes = null; 
            }
            byte[] reporte = reporteDelegate.generarRetencionDeImpuestosPorProveedor(anio,mes,idImpuesto);
            String nombreReporte = "retencionImpuestoPorProveedor_"+anio+".pdf";
            ArchivoUtils.downloadPdfFromBytes(reporte, nombreReporte);

            String texto = textMB.obtenerTexto("labels.ReporteGeneradoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    
    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }

    public Integer getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }
   

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
    
    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }
    
    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
