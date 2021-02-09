/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.business.ejbs.DatosUsuario;
import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Sofis Solutions
 */
@Named(value = "cambioContraseniaMB")
@ViewScoped
public class CambioContraseniaMB implements Serializable {
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Creates a new instance of CargoMB
     */
    public CambioContraseniaMB() {
    }
    private String contraseniaActual = "";
    private String nuevaContrasenia = "";
    private String confirmacionContrasenia = "";
    @Inject
    private UsuarioDelegate uDelegate;
    @Inject
    private DatosUsuario datUsu;
    @Inject
    JSFUtils jSFUtils;
     @Inject
    TextMB textMB;

     
     /**
      * Guarda el objeto en edición
      * 
      * @return 
      */
    public String guardar() {
        if (nuevaContrasenia != null && nuevaContrasenia.equals(confirmacionContrasenia)) {
            uDelegate.cambiarContrasenia(datUsu.getCodigoUsuario(), nuevaContrasenia);
        } else {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage("", "Las contraseñas no coinciden"));
        }
        return null;
    }
    
    /**
     * Cambia la contraseña anterior por una nueva
     * 
     * @return 
     */
    public String guardarMiContrasenia() {
          try {
            if (TextUtils.isEmpty(nuevaContrasenia)) {
                BusinessException bs = new BusinessException();
                bs.addError(ConstantesErrores.ERR_CONTRASENIA_VACIA);
                throw bs;
            }
            if (!nuevaContrasenia.equals(confirmacionContrasenia)) {
                BusinessException bs = new BusinessException();
                bs.addError(ConstantesErrores.ERR_CONTRASENIA_NO_COINCIDEN);
                throw bs;
            }

            uDelegate.cambiarContrasenia(datUsu.getCodigoUsuario(), nuevaContrasenia, contraseniaActual);
            String texto = textMB.obtenerTexto("labels.CambiosGuardadosCorrectamente");
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
        return null;
    }
    
    
    /**
     * Retorna al menú anterior
     * 
     * @return 
     */
    public String cancelar() {
        return "IR_A_INICIO";
    }

    public String getContraseniaActual() {
        return contraseniaActual;
    }

    public void setContraseniaActual(String contraseniaActual) {
        this.contraseniaActual = contraseniaActual;
    }

    public String getNuevaContrasenia() {
        return nuevaContrasenia;
    }

    public void setNuevaContrasenia(String nuevaContrasenia) {
        this.nuevaContrasenia = nuevaContrasenia;
    }

    public String getConfirmacionContrasenia() {
        return confirmacionContrasenia;
    }

    public void setConfirmacionContrasenia(String confirmacionContrasenia) {
        this.confirmacionContrasenia = confirmacionContrasenia;
    }
}
