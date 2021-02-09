/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.mensajes;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class EtiquetasMultipleResourceBundle extends ResourceBundle {

    private ResourceBundle database;
    private ResourceBundle properties;

    public EtiquetasMultipleResourceBundle() {
        properties = ResourceBundle.getBundle("sv.gob.mined.siges.web.presentacion.mensajes.etiquetas");
    }

    @Override
    public Enumeration<String> getKeys() {
        return null;
    }

    @Override
    protected Object handleGetObject(String key) {
        Object result = null;

        try {
            result = properties.getString(key);
        } catch (MissingResourceException exc) {
        }

        if (result == null) {
            result = "??_" + key + "_??";
        }

        return result;
    }
}
