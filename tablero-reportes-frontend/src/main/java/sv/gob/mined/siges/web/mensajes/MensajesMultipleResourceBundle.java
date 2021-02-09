/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.mensajes;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MensajesMultipleResourceBundle extends ResourceBundle {

    private ResourceBundle properties;

    public MensajesMultipleResourceBundle() {
        properties = ResourceBundle.getBundle("sv.gob.mined.siges.web.presentacion.mensajes.mensajes");
    }

    @Override
    public Enumeration<String> getKeys() {
        return null;
    }

    @Override
    protected Object handleGetObject(String key) {
        Object result = null;

        if (result == null) {
            try {
                result = properties.getString(key);
            } catch (MissingResourceException exc) {
            }
        }

        if (result == null) {
            result = "??_" + key + "_??";
        }

        return result;
    }
}
