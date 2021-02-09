/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import java.text.DecimalFormat;
import java.time.LocalDate;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.beans.ApplicationBean;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ApplicationScoped
public class DecimalConverter implements javax.faces.convert.Converter {

    private DecimalFormat formatter;
    
    @Inject
    private ApplicationBean aplicacionBean;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) {
            return null;
        }
        try {
            setFormatter();
            return formatter.format(value);
        } catch (Exception e) {
            throw new ConverterException("Valor númerico inválido");
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }

        if (!(value instanceof LocalDate)) {
            return null;
        }

        setFormatter();
        return formatter.format(value);
    }

    private void setFormatter() {
        if (formatter == null) {
            formatter = new DecimalFormat(aplicacionBean.getFormatValoresDecimales());
        }
    }

}
