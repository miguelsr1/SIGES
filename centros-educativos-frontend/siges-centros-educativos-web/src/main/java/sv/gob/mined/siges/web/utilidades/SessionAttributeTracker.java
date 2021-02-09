package sv.gob.mined.siges.web.utilidades;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class SessionAttributeTracker implements HttpSessionAttributeListener {

    private static final Logger LOGGER = Logger.getLogger(SessionAttributeTracker.class.getName());

    public void attributeAdded(HttpSessionBindingEvent event) {
        Object obj = event.getValue();
        LOGGER.log(Level.FINE, "**************** SessionAttributeTracker *********** Agregando objeto : " + obj.getClass());
        if (!isSerializable(obj)) {
            LOGGER.log(Level.SEVERE, "***********************Agregando objeto no serializable: " + obj.toString() + "," + obj.getClass());
        }
    }

    public void attributeRemoved(HttpSessionBindingEvent event) {
        Object obj = event.getValue();
        LOGGER.log(Level.FINE, "**************** SessionAttributeTracker *********** Removiendo objeto : " + obj);
    }

    public void attributeReplaced(HttpSessionBindingEvent event) {
        Object obj = event.getValue();
        LOGGER.log(Level.FINE, "**************** SessionAttributeTracker *********** Reemplazando objeto : " + obj + ", " + obj.getClass());
    }

    private boolean isSerializable(Object obj) {
        boolean ret = false;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(out);
            oos.writeObject(obj);
            return true;
        } catch (Exception ex) {
            return ret;
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception ex) {
            }
        }
    }
}
