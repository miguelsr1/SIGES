/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.enumerados.EnumAmbito;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ApplicationScoped
public class AmbitoConverter implements Converter {
    
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return EnumAmbito.getByValue(value);
        } catch (Exception ex) {
            throw new ConverterException("Id inválida");
            
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof EnumAmbito) {
            return ((EnumAmbito) o).getText();
        } else if (o instanceof String) {
            if (StringUtils.isBlank((String) o)) {
                return null;
            }
        }
        throw new ConverterException(new FacesMessage(o + " is not a valid Estado de Ambito"));
    }
}
