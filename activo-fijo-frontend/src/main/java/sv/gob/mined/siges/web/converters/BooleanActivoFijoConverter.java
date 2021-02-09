/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import sv.gob.mined.siges.web.beans.ApplicationBean;

@Named
@ApplicationScoped
public class BooleanActivoFijoConverter implements Converter {

    @Inject
    private ApplicationBean applicationBean;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value != null && value.equals(Boolean.TRUE)) ? "Mayores de ".concat(applicationBean.getValorActivoFijoLimite().toString()) : "Menores de ".concat(applicationBean.getValorActivoFijoLimite().toString());
    }

}
