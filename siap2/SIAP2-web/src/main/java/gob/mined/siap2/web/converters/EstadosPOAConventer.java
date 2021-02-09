/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.converters;


import gob.mined.siap2.entities.enums.EstadosPOA;
import gob.mined.siap2.utils.generalutils.TextUtils;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;
/**
 * Esta clase implementa un converter para unidad técnica.
 * @author Sofis Solutions
 */
@Named
public class EstadosPOAConventer implements Converter, Serializable{
    List<EstadosPOA> estados = Arrays.asList(EstadosPOA.values());
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        try {
            for(EstadosPOA estado: estados){
                if(estado.equals(value)) {
                    return estado;
                }
            }
            return "";
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) {
        if (o instanceof EstadosPOA) {
            return String.valueOf(((EstadosPOA) o).getLabel());
        } else if (o == null) {
            return null;
        } if (o instanceof String) {
            if (TextUtils.isEmpty((String) o)){
                return null;
            }
        }
        throw new ConverterException(new FacesMessage(o + " is not a valid Estado"));
    }
}
