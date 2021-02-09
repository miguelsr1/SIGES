/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.mensajes;

import java.util.Locale;
import java.util.ResourceBundle;

public class Mensajes {

    private static final String MENSAJES = "sv.gob.mined.siges.web.mensajes.MensajesMultipleResourceBundle";
    private static ResourceBundle bundle = ResourceBundle.getBundle(MENSAJES, new Locale("es"));
    
    public static String obtenerMensaje(String key) {
        return bundle.getString(key);
    }

    public static final String GUARDADO_CORRECTO = "GUARDADO_CORRECTO";
    public static final String ELIMINADO_CORRECTO = "ELIMINADO_CORRECTO";
    public static final String ENVIADO_CORRECTO = "ENVIADO_CORRECTO";
    public static final String ERROR_GENERAL = "ERROR_GENERAL";
    public static final String USUARIO_INCORRECTO = "USUARIO_INCORRECTO";
    public static final String ERROR_SEGURIDAD = "ERROR_SEGURIDAD";
    public static final String DATOS_INCORRECTOS = "DATOS_INCORRECTOS";
    public static final String ERROR_ASOCIACION_VACIO = "ERROR_ASOCIACION_VACIO";

}
