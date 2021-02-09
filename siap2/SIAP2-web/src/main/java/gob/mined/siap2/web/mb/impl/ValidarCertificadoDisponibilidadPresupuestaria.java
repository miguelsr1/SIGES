/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.AccionCentral;
import gob.mined.siap2.entities.data.impl.AsignacionNoProgramable;
import gob.mined.siap2.entities.data.impl.CertificadoDisponibilidadPresupuestaria;
import gob.mined.siap2.entities.data.impl.POAConActividades;
import gob.mined.siap2.entities.data.impl.POAProyecto;
import gob.mined.siap2.entities.data.impl.POInsumos;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.enums.TipoPOA;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.CertificadoPresupuestarioDelegate;
import gob.mined.siap2.web.delegates.impl.GeneralPODelegate;
import gob.mined.siap2.web.delegates.impl.InsumoDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.utils.ArchivoUtils;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import org.omnifaces.cdi.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.primefaces.context.RequestContext;

/**
 * Este backing bean implementa los eventos y lógica de presentación de la
 * página que valida el certificado de disponibilidad presupuestaria.
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "validarCertificadoDisponibilidadPresupuestaria")
public class ValidarCertificadoDisponibilidadPresupuestaria implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    CertificadoPresupuestarioDelegate certificadoPresupuestarioDelegate;

    private CertificadoDisponibilidadPresupuestaria objeto;

    private String contenidoInfo;
    
    private String volverAConsulta;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        objeto = certificadoPresupuestarioDelegate.getCertificadoPresupuestario(Integer.valueOf(id));
        volverAConsulta = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("volverAConsulta");
    }

    /**
     * Aprueba el objeto en edición
     *
     * @return
     */
    public String aprobar() {
        try {
            objeto = certificadoPresupuestarioDelegate.aprobarCertificadoDispPresupuestario(objeto.getId());
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBackConParametros').modal('hide');");
            return "consultaCertificadosDisponibilidadPresupuestariaEmitidos.xhtml?faces-redirect=true";
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

    /**
     * Rechaza el certificado
     *
     * @return
     */
    public String rechazar() {
        try {
            objeto = certificadoPresupuestarioDelegate.rechazarCertificadoDispPresupuestario(objeto);
            RequestContext.getCurrentInstance().execute("$('#confirmModalCallBackConParametros').modal('hide');");
            return "consultaCertificadosDisponibilidadPresupuestariaEmitidos.xhtml?faces-redirect=true";
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
    
    
    
    
    /**
     * Aprueba el objeto en edición
     *
     * @return
     */
    public String reenviar() {
        try {
            objeto = certificadoPresupuestarioDelegate.reenviarCertificadoParaEnviar(objeto);
            return "consultaCertificadosDisponibilidadPresupuestariaEmitidos.xhtml?faces-redirect=true";
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

    
    

    /**
     * Solicitar información el certificado
     *
     * @return
     */
    public void solicitarInfo() {
        try {
            objeto =certificadoPresupuestarioDelegate.agregarComentarioCertificado(objeto.getId(), contenidoInfo);
            contenidoInfo= "";
            //return "consultaCertificadosDisponibilidadPresupuestariaEmitidos.xhtml?faces-redirect=true";
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
     * Dirige el sitio a la página de consulta de notificaciones
     *
     * @return
     */
    public String cerrar() {        
        volverAConsulta = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("volverAConsulta");
        if(volverAConsulta!=null && volverAConsulta.equals("true")){
            return "consultaCertificadosDisponibilidadPresupuestariaEmitidos.xhtml?faces-redirect=true";
        } else {
            return "consultaNotificaciones.xhtml?faces-redirect=true";
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

    public CertificadoPresupuestarioDelegate getCertificadoPresupuestarioDelegate() {
        return certificadoPresupuestarioDelegate;
    }

    public void setCertificadoPresupuestarioDelegate(CertificadoPresupuestarioDelegate certificadoPresupuestarioDelegate) {
        this.certificadoPresupuestarioDelegate = certificadoPresupuestarioDelegate;
    }

    public String getContenidoInfo() {
        return contenidoInfo;
    }

    public void setContenidoInfo(String contenidoInfo) {
        this.contenidoInfo = contenidoInfo;
    }

    public CertificadoDisponibilidadPresupuestaria getObjeto() {
        return objeto;
    }

    public void setObjeto(CertificadoDisponibilidadPresupuestaria objeto) {
        this.objeto = objeto;
    }

    public String getVolverAConsulta() {
        return volverAConsulta;
    }

    public void setVolverAConsulta(String volverAConsulta) {
        this.volverAConsulta = volverAConsulta;
    }
    // </editor-fold>
}
