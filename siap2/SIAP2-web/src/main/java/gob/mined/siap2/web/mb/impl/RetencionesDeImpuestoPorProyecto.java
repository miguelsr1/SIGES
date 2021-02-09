/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.Proyecto;
import gob.mined.siap2.entities.data.impl.QuedanEmitido;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ImpuestoDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
 * de retenciones de impuestos por proyecto.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "retencionesDeImpuestoPorProyecto")
public class RetencionesDeImpuestoPorProyecto implements Serializable {

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

    private Date fechaDesde;
    private Date fechaHasta;
    private Integer idImpuesto;

    private Proyecto proyectoSelecionado;

    private List<QuedanEmitido> qudanEmitidos;

    @PostConstruct
    public void init() {
        filterPagos();
    }

    /***
     * Este método es el encargado de filtrar la tabla según los datos en los pagos
     */
    public void filterPagos() {
        try {
            if (proyectoSelecionado != null) {
                qudanEmitidos = impuestoDelegate.getActasEnProyecto(proyectoSelecionado.getId(), fechaDesde, fechaHasta, idImpuesto);
            } else {
                //qudanEmitidos = new LinkedList<>();
                //Busca en todos los proyectos
                qudanEmitidos = impuestoDelegate.getActasEnProyecto(null, fechaDesde, fechaHasta, idImpuesto);
                
            }

        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
        }
    }

    /**
     * Dirige a la página de inicio
     * @return 
     */
    public String cerrar() {
        return "inicio.xhtml?faces-redirect=true";
    }

   
    /**
     * Este método es el encargado de generar los reportes de retención de impuestos por proyecto
     */
    public void generarRetencionDeImpuestosPorProyecto() {
        try {
            Integer idProyecto = proyectoSelecionado!=null?proyectoSelecionado.getId():null;
            byte[] reporte = reporteDelegate.generarRetencionDeImpuestosPorProyecto(idProyecto, fechaDesde, fechaHasta, idImpuesto);
            ArchivoUtils.downloadPdfFromBytes(reporte, "retencionImpuestoPorProyecto.pdf");

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
    
    /**
     * Este método es el encargado de calcular el monto retenido para el impuesto
     * @param idActa
     * @param idImpuesto
     * @return 
     */
    public BigDecimal obtenerMontoRetenidoParaImpuesto(Integer idActa, Integer idImpuesto){
      try {
            return impuestoDelegate.obtenerMontoRetenidoParaImpuesto(idActa, idImpuesto);
        } catch (GeneralException ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ex.getErrores());
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            jSFUtils.mostrarErrorByPropertieCode(ConstantesErrores.ERROR_GENERAL);
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
        }  
        return null;
    }
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public JSFUtils getjSFUtils() {
        return jSFUtils;
    }

    public void setjSFUtils(JSFUtils jSFUtils) {
        this.jSFUtils = jSFUtils;
    }

    public List<QuedanEmitido> getQudanEmitidos() {
        return qudanEmitidos;
    }

    public void setQudanEmitidos(List<QuedanEmitido> qudanEmitidos) {
        this.qudanEmitidos = qudanEmitidos;
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

    public Proyecto getProyectoSelecionado() {
        return proyectoSelecionado;
    }

    public void setProyectoSelecionado(Proyecto proyectoSelecionado) {
        this.proyectoSelecionado = proyectoSelecionado;
    }

    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    // </editor-fold>
}
