/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.utilidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 *
 * @author Sofis Solutions
 */


public class HistoricResultsAnnotationIntrospector extends JacksonAnnotationIntrospector {
    @Override
    protected boolean _isIgnorable(Annotated a) {
        boolean isIgnorable = super._isIgnorable(a);
        if (!isIgnorable) {
            JsonIdentityInfo ann = a.getAnnotation(JsonIdentityInfo.class);
            isIgnorable = ann != null;
        }
        return isIgnorable;
    }
}