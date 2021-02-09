/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.primefaces.convert.ClientConverter;

@Named
@ApplicationScoped
public class IntegerConverter extends javax.faces.convert.IntegerConverter implements ClientConverter {

    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }

    @Override
    public String getConverterId() {
        return IntegerConverter.CONVERTER_ID;
    }
}
