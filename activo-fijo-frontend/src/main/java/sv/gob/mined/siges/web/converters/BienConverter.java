/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;
import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;
import sv.gob.mined.siges.web.dto.SgAfBienDepreciable;

@Named
@ApplicationScoped
public class BienConverter implements Converter {

     private static final Logger LOGGER = Logger.getLogger(BienConverter.class.getName());
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        
        DualListModel<SgAfBienDepreciable> model = (DualListModel<SgAfBienDepreciable>) ((PickList) component).getValue();
        
        for (SgAfBienDepreciable bien : model.getSource()) {
            if (bien.getBdePk().equals(Long.parseLong(value))) {
                return bien;
            }
        }
        for (SgAfBienDepreciable bien : model.getTarget()) {
            if (bien.getBdePk().equals(Long.parseLong(value))) {
                return bien;
            }
        }
        return value;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((SgAfBienDepreciable) value).getBdePk().toString();
    }
    
}
