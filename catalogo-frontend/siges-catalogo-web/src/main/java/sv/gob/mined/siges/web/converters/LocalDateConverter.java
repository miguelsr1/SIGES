/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.beans.ApplicationBean;

@Named
@ApplicationScoped
public class LocalDateConverter implements javax.faces.convert.Converter {

    private DateTimeFormatter formatter;
    
    @Inject
    private ApplicationBean aplicacionBean;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) {
            return null;
        }
        try {
            checkFormatter();
            return LocalDate.parse(value, formatter);
        } catch (Exception e) {
            throw new ConverterException("Fecha inválida");
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }
        checkFormatter();
        LocalDate dateValue = (LocalDate) value;
        return dateValue.format(formatter);
    }

    private void checkFormatter() {
        if (formatter == null) { 
            formatter = DateTimeFormatter.ofPattern(aplicacionBean.getPatternFecha());
        }
    }
    

}
