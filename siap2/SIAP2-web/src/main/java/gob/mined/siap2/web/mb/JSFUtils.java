/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.mb;

import gob.mined.siap2.exceptions.GeneralException;
import gob.mined.siap2.web.delegates.TextoDelegate;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@Named(value = "jSFUtils")
@SessionScoped
public class JSFUtils implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(JSFUtils.class.getName());

    @Inject
    private TextoDelegate textoDelegate;
    @Inject
    private TextMB textMB;

    /**
     * Retorna el texto a partir de la clave
     *
     * @param key
     * @return
     */
    public String getMensajeByKey(String key) {
        try {
            return textMB.obtenerTexto(key);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[getMensajeByKey] Error " + e.getMessage(), e);
            return key;
        }
    }

    /**
     * Muestra un mensaje con el tipo pasado por parámetro
     *
     * @param mensaje
     * @param sev
     */
    public void mostrarMensaje(String mensaje, FacesMessage.Severity sev) {
        FacesMessage msg = new FacesMessage(sev, mensaje, mensaje);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Muestra una lista de mensajes con el tipo pasado por parámetro
     *
     * @param mensajes
     * @param sev
     */
    public void mostrarMensajes(List<String> mensajes, FacesMessage.Severity sev) {
        for (String mensaje : mensajes) {
            FacesMessage msg = new FacesMessage(sev, mensaje, mensaje);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * Muestra un mensaje de error
     *
     * @param mensaje
     */
    public void agregarError(String mensaje) {
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, mensaje, mensaje));
    }

    /**
     * Muestra un mensaje de info
     *
     * @param mensaje
     */
    public void agregarInfo(String mensaje) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, mensaje, mensaje));
    }

    /**
     * Muestra un error por código de mensaje
     *
     * @param msj
     */
    public void mostrarErrorByPropertieCode(String msj) {
        try {
            String[] params = GeneralException.getParams(msj);
            String mensaje = msj;
            if (params != null) {//El mensaje tiene parametros
                mensaje = GeneralException.getTextError(msj);
            }
            String texto = getMensajeByKey(mensaje);
            if (params != null && texto.indexOf("{") != -1) {//Hay parametros para sustituir en el texto
                texto = MessageFormat.format(texto, params);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, texto));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[mostrarErrorByPropertieCode] Error " + e.getMessage(), e);
        }
    }

     /**
     * Muestra un error por código de mensaje
     *
     * @param msj
     */
    public String obtenerMensajeByProperty(String msj) {
        try {
            String[] params = GeneralException.getParams(msj);
            String mensaje = msj;
            if (params != null) {//El mensaje tiene parametros
                mensaje = GeneralException.getTextError(msj);
            }
            String texto = getMensajeByKey(mensaje);
            if (params != null && texto.indexOf("{") != -1) {//Hay parametros para sustituir en el texto
                texto = MessageFormat.format(texto, params);
            }
            return texto;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[mostrarErrorByPropertieCode] Error " + e.getMessage(), e);
        }
        return "";
    }
    
    /**
     * Muestra una advertencia por código
     *
     * @param msj
     */
    public void mostrarAdvertenciaByPropertieCode(String msj) {
        try {
            String[] params = GeneralException.getParams(msj);
            String mensaje = msj;
            if (params != null) {//El mensaje tiene parametros
                mensaje = GeneralException.getTextError(msj);
            }
            String texto = getMensajeByKey(mensaje);
            if (params != null && texto.indexOf("{") != -1) {//Hay parametros para sustituir en el texto
                texto = MessageFormat.format(texto, params);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, texto, texto));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[mostrarErrorByPropertieCode] Error " + e.getMessage(), e);
        }
    }

    /**
     * Muestra un mensaje de error asociado al clientID pasado por parámetro
     *
     * @param id
     * @param msj
     */
    public void mostrarErrorByPropertieCode(String id, String msj) {
        try {
            String[] params = GeneralException.getParams(msj);
            String mensaje = msj;
            if (params != null) {//El mensaje tiene parametros
                mensaje = GeneralException.getTextError(msj);
            }
            String texto = getMensajeByKey(mensaje);
            if (params != null && texto.indexOf("{") != -1) {//Hay parametros para sustituir en el texto
                texto = MessageFormat.format(texto, params);
            }
            FacesContext.getCurrentInstance().addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR, texto, texto));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[mostrarErrorByPropertieCode] Error " + e.getMessage(), e);
        }
    }

    /**
     * Muestra una lista de mensajes de errores por código
     *
     * @param mensajes
     */
    public void mostrarErrorByPropertieCode(List<String> mensajes) {
        try {
            if (mensajes == null || mensajes.isEmpty()) {
                logger.log(Level.SEVERE, "la exepcion no tiene nada en la lista de mensajes");
            }

            for (String mensaje : mensajes) {
                logger.log(Level.SEVERE, "va a mostrar con el codigo " + mensaje);

                mostrarErrorByPropertieCode(mensaje);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "[mostrarErrorByPropertieCode] Error " + e.getMessage(), e);
        }
    }

    public static Object getBean(String name) {
        return FacesContext.getCurrentInstance().getApplication().getVariableResolver().resolveVariable(FacesContext.getCurrentInstance(), name);
    }
}
