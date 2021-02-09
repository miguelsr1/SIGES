/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;

/**
 *
 * @author Sofis Solutions
 */


public class HistoricResultsAnnotationIntrospector extends JacksonAnnotationIntrospector {
    //Disable JsonIdentityInfo
    @Override
    public ObjectIdInfo findObjectIdInfo(Annotated ann) {
        return null;
    }
}