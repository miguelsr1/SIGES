/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb.impl;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.impl.PoliticaContrasenia;
import gob.mined.siap2.entities.helper.PoliticaContraseniaHelper;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
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
@Named(value = "politicaContraseniaCE")
public class PoliticaContraseniaCrearEditar implements Serializable {

    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);
    
    @Inject
    EntityManagementDelegate emd;
    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;
    
    private PoliticaContrasenia pol;

    @PostConstruct
    public void init() {
        pol = (PoliticaContrasenia) emd.getEntityById(PoliticaContrasenia.class.getName(), 1);
        if (pol == null){
            pol = PoliticaContraseniaHelper.obtenerPoliticaContraseniaPorDefecto();
        }
    }
    
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar()  {
        try {
            pol = (PoliticaContrasenia) emd.saveOrUpdate(pol);
            String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, texto, texto));
            RequestContext.getCurrentInstance().execute("$('html,body').animate({scrollTop: 0})");
            return null;
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
     * Dirige el sitio a la página de inicio
     * @return 
     */
    public String cerrar() {
        return "inicio.xhtml";
    }



    // </editor-fold>
    public PoliticaContrasenia getPol() {
        return pol;
    }

    public void setPol(PoliticaContrasenia pol) {
        this.pol = pol;
    }
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

}
