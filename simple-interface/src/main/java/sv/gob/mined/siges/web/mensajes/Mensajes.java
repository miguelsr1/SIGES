/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.mensajes;

import java.util.HashMap;
import java.util.ResourceBundle;

public class Mensajes {

    private static HashMap<String, String> MENSAJES = new HashMap();
    private static ResourceBundle bundle = ResourceBundle.getBundle("sv.gob.mined.siges.web.presentacion.mensajes.mensajes");

    public static String obtenerMensaje(String key) {
        if (bundle.containsKey(key)) {
            return bundle.getString(key);
        } else {
            return "?" + key + "?";
        }
    }

    public static boolean containsKey(String key) {
        return bundle.containsKey(key);
    }

    public static final String GUARDADO_CORRECTO = "GUARDADO_CORRECTO";
    public static final String ELIMINADO_CORRECTO = "ELIMINADO_CORRECTO";
    public static final String ENVIADO_CORRECTO = "ENVIADO_CORRECTO";
    public static final String ERROR_GENERAL = "ERROR_GENERAL";
    public static final String USUARIO_INCORRECTO = "USUARIO_INCORRECTO";

}
