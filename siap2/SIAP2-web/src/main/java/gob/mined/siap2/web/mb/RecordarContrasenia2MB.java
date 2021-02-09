/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.constantes.ConstantesEstandares;
import gob.mined.siap2.entities.data.SsTokenPassword;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.UsuarioDelegate;
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
@Named(value = "recordarContrasenia2MB")
@ViewScoped
public class RecordarContrasenia2MB implements Serializable {

    @Inject
    JSFUtils jSFUtils;
    @Inject
    TextMB textMB;
    private static final Logger logger = Logger.getLogger(ConstantesEstandares.LOGGER);

    /**
     * Creates a new instance of CargoMB
     */
    public RecordarContrasenia2MB() {
    }

    private String nuevaContrasenia;
    private String confirmacionContrasenia;
    private Boolean tokenOK = false;
    private SsTokenPassword ssToken;

    @Inject
    private UsuarioDelegate uDelegate;

    @PostConstruct
    public void init() {
        String token = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("lt");
        ssToken = uDelegate.obtenerToken(token);
        if (ssToken != null) {
            tokenOK = true;
        } else {
            tokenOK = false;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("formRecordarContrasenia");
    }

    /**
     * Este método guarda la nueva contraseña
     * 
     * @return 
     */
    public String guardar() {
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

            uDelegate.cambiarContrasenia(ssToken.getUsuario(), nuevaContrasenia);

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

    public Boolean getTokenOK() {
        return tokenOK;
    }

    public void setTokenOK(Boolean tokenOK) {
        this.tokenOK = tokenOK;
    }

}
