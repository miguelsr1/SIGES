/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.mensajes;

import java.util.Locale;
import java.util.ResourceBundle;

public class Etiquetas {

    private static final String ETIQUETAS = "sv.gob.mined.siges.web.mensajes.EtiquetasMultipleResourceBundle";
    private static ResourceBundle bundle = ResourceBundle.getBundle(ETIQUETAS, new Locale("es"));

    public static String getValue(String key) {
        return bundle.getString(key);
    }

    public static String getValue(String key, Locale locale) {
        return bundle.getString(key);
    }

}
