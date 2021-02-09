/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.ActaContrato;
import gob.mined.siap2.entities.data.impl.Factura;
import gob.mined.siap2.entities.data.impl.FacturaActaContratoOC;
import gob.mined.siap2.entities.enums.TipoDestinoFactura;
import gob.mined.siap2.entities.enums.TipoFactura;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ContratoDelegate;
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
 * Esta clase corresponde al backing bean de Actas de Contrato
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "actaContratoMB")
public class ActaContratoMB implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;
    @Inject
    ReporteDelegate reporteDelegate;

    @Inject
    EntityManagementDelegate emd;

    @Inject
    ContratoDelegate contratoDelegate;

    private ActaContrato objeto;
    private FacturaActaContratoOC tempFactura;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            reloadActa(Integer.valueOf(id));
        }
    }

    /**
     * Este método permite recargar un acta según su id.
     * @param id 
     */
    public void reloadActa(Integer id) {
        objeto = contratoDelegate.cargarPago(id);
    }

    /**
     * Este método inicializa una factura.
     * @param factura 
     */
    public void initTempFactura(FacturaActaContratoOC factura) {
        tempFactura = factura;
        if (tempFactura == null) {
            tempFactura = new FacturaActaContratoOC();
            tempFactura.setTipo(TipoFactura.FACTURA);
            tempFactura.setDestinoFactura(TipoDestinoFactura.ACTA_CONTRATO_OC);
        }
    }

    /**
     * Este método corresponde al evento para guardar una factura.
     */
    public void guardarFactura() {
        try {
            contratoDelegate.guardarFactura(objeto.getId(), tempFactura);
            reloadActa(objeto.getId());
            RequestContext.getCurrentInstance().execute("$('#addFactura').modal('hide');");
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
     * Este método corresponde al evento de eliminación de una factura.
     * @param idFactura 
     */
    public void eliminarFactura(Integer idFactura) {
        try {
            contratoDelegate.eliminarFactura(idFactura);
            reloadActa(objeto.getId());

            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
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
     * Este método corresponde al evento para crear un número de acta de pago.
     */
    public void crearNumeroActaPago() {
        try {
            contratoDelegate.crearNumeroActaPago(objeto.getId());
            reloadActa(objeto.getId());

            String texto = textMB.obtenerTexto("labels.NumeroActaGeneradoCorrectamente");
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
     * Este método corresponde al evento de generación de un acta.
     */
    public void generarActa() {
        try {
            byte[] reporte = null;
            String fileName = null;            
            if (null != objeto.getTipo()) {
                String nroActa = objeto.getContratoOC().getId() + "_" + objeto.getNroActa(); 
                switch (objeto.getTipo()) {
                    case RECEPCION:
                        reporte = reporteDelegate.generarActaDeRecepcion(objeto.getId());
                        fileName = "actaRecepecion_"+nroActa+".pdf";
                        break;
                    case ANTICIPO:
                        reporte = reporteDelegate.generarActaDeAnticipo(objeto.getId());
                        fileName = "actaAnticipo_"+nroActa+".pdf";
                        break;
                    case DEVOLUCION:
                        reporte = reporteDelegate.generarActaDeDevolucion(objeto.getId());
                        fileName = "actaDevolucion_"+nroActa+".pdf";
                        break;
                    default:
                        break;
                }
            }
            ArchivoUtils.downloadPdfFromBytes(reporte, fileName);

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
     * Este método corresponde al evento de generación de un quedan.
     */
    public void generarQUEDAN() {
        try {
            contratoDelegate.generarQuedan(objeto.getId());
            reloadActa(objeto.getId());

            String texto = textMB.obtenerTexto("labels.QUEDANGeneradoCorrectamente");
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
     * Este método corresponde al evento de generación de un quedan.
     */
    public void eliminarQUEDAN() {
        try {
            contratoDelegate.eliminarQuedan(objeto.getId());

            reloadActa(objeto.getId());

            String texto = textMB.obtenerTexto("labels.QUEDANEliminadoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
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
     * Este método corresponde al evento de generación del comprobante de recepción de expediente de pago.
     */
    public void generarComprobanteRecepcionExpedienteDepago() {
        try {
            contratoDelegate.generarComprobanteRecepcionExpedienteDepago(objeto.getId());

            reloadActa(objeto.getId());

            String texto = textMB.obtenerTexto("labels.ComprobanteRecepcionExpedienteDepagoGeneradoCorrectamente");
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
     * Este método corresponde al evento de eliminación del comprobante de recepción de expediente de pago.
     */
    public void eliminarComprobanteRecepcionExpedienteDepago() {
        try {
            contratoDelegate.eliminarComprobanteRecepcionExpedienteDepago(objeto.getId());

            reloadActa(objeto.getId());

            String texto = textMB.obtenerTexto("labels.ComprobanteRecepcionExpedienteDepagoEliminadoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            
            
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBack').modal('hide');");
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

    public ContratoDelegate getContratoDelegate() {
        return contratoDelegate;
    }

    public void setContratoDelegate(ContratoDelegate contratoDelegate) {
        this.contratoDelegate = contratoDelegate;
    }

    public FacturaActaContratoOC getTempFactura() {
        return tempFactura;
    }

    public void setTempFactura(FacturaActaContratoOC tempFactura) {
        this.tempFactura = tempFactura;
    }

    public ActaContrato getObjeto() {
        return objeto;
    }

    public void setObjeto(ActaContrato objeto) {
        this.objeto = objeto;
    }

    // </editor-fold>
}
