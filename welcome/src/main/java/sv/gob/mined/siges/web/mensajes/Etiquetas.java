/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.mensajes;

import java.util.Locale;
import java.util.ResourceBundle;

public class Etiquetas {

    private static final String ETIQUETAS = "sv.gob.mined.siges.web.presentacion.mensajes.etiquetas";

    private static ResourceBundle bundle = ResourceBundle.getBundle(ETIQUETAS, new Locale("es"));

    public static String getValue(String key) {
        if (bundle.containsKey(key)) {
            return bundle.getString(key);
        } else {
            return "?" + key + "?";
        }
    }

    public static String getValue(String key, Locale loc) {
            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            } else {
                return "?" + key + "?";
            }
    }

    public static boolean containsKey(String key) {
        return bundle.containsKey(key);
    }
}
