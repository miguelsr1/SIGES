/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.converters;

import gob.mined.siap2.utils.generalutils.DatesUtils;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
/**
 * convierte en formato dd/MM/yyyy
 * @author sofis
 */
@FacesConverter("dateConverterAceptaNull")
public class DateConverterAceptaNull implements Converter {
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        if (string == null || string.trim().length()==0){
            return null;
        }
        try {
           return  DatesUtils.stringToDate(string);
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(string + " is not a valid time HH:mm"), e);
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o == null || o instanceof String){
            return "";
        }
        Date d = (Date) o;
        return DatesUtils.dateToString(d);
    }
    
}