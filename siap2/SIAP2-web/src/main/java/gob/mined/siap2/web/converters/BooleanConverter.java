/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.converters;

import gob.mined.siap2.utils.generalutils.TextUtils;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
/**
 * Este 
 * @author Sofis
 */
@FacesConverter("booleanConverter")
public class BooleanConverter implements Converter {
    
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        try {
            if (TextUtils.isEmpty(string)){
                return null;
            }
            return Boolean.parseBoolean(string);
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(string + " is not a valid Boolean value"), e);
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o == null){
            return "";
        }
        Boolean b = (Boolean) o;
        if (b){
            return "SI";
        }else{
            return "NO";
        }
    }
    
}