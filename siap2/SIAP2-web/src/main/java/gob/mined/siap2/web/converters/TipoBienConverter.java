/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.converters;


import gob.mined.siges.entities.data.impl.SgAfTipoBienes;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
/**
 * Esta clase implementa un converter para la presentación de los convenios
 * @author Sofis Solutions
 */
@Named
public class TipoBienConverter implements Converter {
@Inject
    EntityManagementDelegate emd;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        try {
            SgAfTipoBienes tb = (SgAfTipoBienes) emd.getEntityById(SgAfTipoBienes.class.getCanonicalName(), Long.valueOf(value));
            return tb;
        } catch (NumberFormatException e) {
            return null;
        }
    
    
    
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof SgAfTipoBienes) {
            return String.valueOf(((SgAfTipoBienes) o).getTbiPk());
        } else if (o instanceof String) {
            if (TextUtils.isEmpty((String) o)){
                return null;
            }
        }
        throw new ConverterException(new FacesMessage(o + " is not a valid Tipo de bien"));
    }

    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }
    

}