/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.enumerados.EnumModeloContrato;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ApplicationScoped
public class ModeloContratoConverter implements Converter {
    
    private static final Logger LOGGER = Logger.getLogger(EstadoTrasladoConverter.class.getName());
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return EnumModeloContrato.getByValue(value);
        } catch (Exception ex) {
            throw new ConverterException("Id inválida");
            
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof EnumModeloContrato) {
            return EnumModeloContrato.valueOf(o.toString()).getText();
        } else if (o instanceof String) {
            if (StringUtils.isBlank((String) o)) {
                return null;
            }
        }
        throw new ConverterException(new FacesMessage(o + " is not a valid modelo de contrato"));
    }
}
