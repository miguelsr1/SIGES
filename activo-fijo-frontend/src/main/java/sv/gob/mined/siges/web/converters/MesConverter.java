/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.enumerados.EnumMes;

@Named
@ApplicationScoped
public class MesConverter implements Converter {

    private static final Logger LOGGER = Logger.getLogger(MesConverter.class.getName());
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String mes = StringUtils.EMPTY;
        try {
            if(value == null) {
                return "";
            }
            Integer mesValue = Integer.parseInt(value.toString());
            EnumMes mesEnum = EnumMes.getByValue(mesValue);
            if(mesEnum != null) {
                mes = mesEnum.getText();
            }
        } catch (Exception e) {
           LOGGER.log(Level.SEVERE, e.getMessage());
        }
        return mes;
    }
    
}