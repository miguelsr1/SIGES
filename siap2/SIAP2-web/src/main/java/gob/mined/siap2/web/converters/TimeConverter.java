/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Esta clase implementa un converter para una fecha en formato hora, minutos
 * @author Sofis Solutions
 */
@FacesConverter("timeConverter")
public class TimeConverter  implements Converter {
    
    private  DateFormat df;

    public TimeConverter() {
        df = new SimpleDateFormat("HH:mm");
    }
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        try {
            return df.parse(string);
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(string + " is not a valid time HH:mm"), e);
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        Date d = (Date) o;
        return df.format(d);
    }
    
}
