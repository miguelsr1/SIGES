/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;
import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.web.enumerados.EnumInmueblePertenece;

@Named
@ApplicationScoped
public class PerteneceInmuebleConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value!=null){
            Boolean perteneceA = (Boolean) value;
            if (BooleanUtils.isTrue(perteneceA)) {
                return EnumInmueblePertenece.SEDE.getText();
            } else {
                if (BooleanUtils.isFalse(perteneceA)) {
                    return EnumInmueblePertenece.UNIDAD_ADM.getText();
                }
            }
        }
        return EnumInmueblePertenece.OTROS.getText();
    }

}
