/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.web.converters;


import gob.mined.siap2.entities.data.impl.ActividadPOProyecto;
import gob.mined.siap2.utils.generalutils.NumberUtils;
import gob.mined.siap2.utils.generalutils.TextUtils;
import gob.mined.siap2.web.delegates.EntityManagementDelegate;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
/**
 * Esta clase implementa un converter para un POA
 * @author Sofis Solutions
 */
@Named
public class PoActividadProyectoConverter implements Converter, Serializable{
        
    @Inject
    EntityManagementDelegate emd;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Integer id = NumberUtils.getIntegerONull(value);
        if (id == null) {
            return null;
        }
        try {
            ActividadPOProyecto dt = (ActividadPOProyecto) emd.getEntityById(ActividadPOProyecto.class.getCanonicalName(), id);
            return dt;
        } catch (NumberFormatException e) {
            return null;
        }
    
    
    
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof ActividadPOProyecto) {
            return String.valueOf(((ActividadPOProyecto) o).getId());
        } else if (o instanceof String) {
            if (TextUtils.isEmpty((String) o)){
                return null;
            }
        }
        throw new ConverterException(new FacesMessage(o + " is not a valid ActividadPOProyecto"));
    }

    
    
    
    
    public EntityManagementDelegate getEmd() {
        return emd;
    }

    public void setEmd(EntityManagementDelegate emd) {
        this.emd = emd;
    }
    

}
