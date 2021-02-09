/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtilsBean;

/**
 * Gestión de objetos nulos.
 *
 * @author jgiron
 */
public class NullAwareBeanUtilsBean extends BeanUtilsBean {

    @Override
    /**
     * Reescritura del mètodo.
     */
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if (value == null) {
            return;
        }
        super.copyProperty(dest, name, value);
    }

}
