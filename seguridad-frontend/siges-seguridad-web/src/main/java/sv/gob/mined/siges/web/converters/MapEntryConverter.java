/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.converters;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;
import sv.gob.mined.siges.web.beans.UsuariosBean;

/**
 *
 * @author mef
 */


@Named
@ApplicationScoped
public class MapEntryConverter implements Converter, Serializable{
    
    private static final Logger LOGGER = Logger.getLogger(UsuariosBean.class.getName());
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) {
            return null;
        }
        try {
            return new AbstractMap.SimpleEntry<Long, String>(Long.parseLong(value.split("=")[0]), value.split("=")[1]);
        } catch (Exception e) {
            throw new ConverterException("Map entry inválida");
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (value != null) ? String.valueOf(value) : null;
    }
    
}
