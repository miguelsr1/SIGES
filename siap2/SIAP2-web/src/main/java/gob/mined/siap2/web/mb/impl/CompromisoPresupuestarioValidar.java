/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestario;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioModificativa;
import gob.mined.siap2.entities.data.impl.CompromisoPresupuestarioProceso;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import gob.mined.siap2.web.delegates.impl.ProcesoAdquisicionDelegate;
import gob.mined.siap2.web.delegates.impl.ReporteDelegate;
import gob.mined.siap2.web.mb.JSFUtils;
import gob.mined.siap2.web.mb.TextMB;
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
 *
 * @author Sofis Solutions
 */
@ViewScoped
@Named(value = "compromisoPresupuestarioValidar")
public class CompromisoPresupuestarioValidar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;

    @Inject
    EntityManagementDelegate emd;
    @Inject
    ProcesoAdquisicionDelegate procesoAdquisicionDelegate;
    @Inject
    ReporteDelegate reporteDelegate;

    private CompromisoPresupuestario objeto;

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (!TextUtils.isEmpty(id)) {
            objeto = (CompromisoPresupuestario) emd.getEntityById(CompromisoPresupuestario.class.getName(), Integer.valueOf(id));

        }
    }

    /**
     * Valida un compromiso presupuestario
     */
    public void validar() {
        try {
            objeto = procesoAdquisicionDelegate.validarCompromisoPresupuestario(objeto.getId(), objeto.getNumeroSAFI());

            String texto = textMB.obtenerTexto("labels.CompromisoPresupuestarioValidadoCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");

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
     * Redirige el sitio a la consulta de compromisos presupuestarios
     * @return 
     */
    public String cerrar() {
        return "consultaCompromisoPresupuestario.xhtml?faces-redirect=true";
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

    public CompromisoPresupuestario getObjeto() {
        return objeto;
    }

    public void setObjeto(CompromisoPresupuestario objeto) {
        this.objeto = objeto;
    }

    
    
    
    
    public CompromisoPresupuestarioProceso getObjetoProceso() {
        return (CompromisoPresupuestarioProceso) objeto;
    }

    public void setObjetoProceso(CompromisoPresupuestarioProceso compromisoEnEdicion) {
        this.objeto = compromisoEnEdicion;
    }
    
    public CompromisoPresupuestarioModificativa getObjetoModificativa() {
        return (CompromisoPresupuestarioModificativa) objeto;
    }

    public void setObjetoModificativa(CompromisoPresupuestarioProceso compromisoEnEdicion) {
        this.objeto = compromisoEnEdicion;
    }
    
    
    
    
    
    
    
    
    
    
    public ReporteDelegate getReporteDelegate() {
        return reporteDelegate;
    }

    public void setReporteDelegate(ReporteDelegate reporteDelegate) {
        this.reporteDelegate = reporteDelegate;
    }

    // </editor-fold>
}
