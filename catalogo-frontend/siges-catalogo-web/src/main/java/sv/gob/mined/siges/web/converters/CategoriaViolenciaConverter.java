/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.dto.SgCategoriaViolencia;
import sv.gob.mined.siges.web.rest.CategoriaViolenciaRestClient;

/**
 *
 * @author bruno
 */
@Named
@ApplicationScoped
public class CategoriaViolenciaConverter implements Converter {
    
    @Inject
    private CategoriaViolenciaRestClient restClient;
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return restClient.obtenerPorId(Long.valueOf(value));
        } catch (Exception ex) {
            throw new ConverterException("Id inválida");
            
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof SgCategoriaViolencia) {
            return String.valueOf(((SgCategoriaViolencia) o).getCavPk());
        } else if (o instanceof String) {
            if (StringUtils.isBlank((String) o)) {
                return null;
            }
        }
        throw new ConverterException(new FacesMessage(o + " is not a valid Calificación"));
    }
}
