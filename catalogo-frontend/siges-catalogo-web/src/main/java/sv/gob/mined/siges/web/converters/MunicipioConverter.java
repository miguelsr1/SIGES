/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
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
import sv.gob.mined.siges.web.dto.SgMunicipio;
import sv.gob.mined.siges.web.restclient.MunicipioRestClient;

/**
 *
 * @author Sofis Solutions
 */
@Named
@ApplicationScoped
public class MunicipioConverter implements Converter {
    
    @Inject
    private MunicipioRestClient restClient;
    
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
        } else if (o instanceof SgMunicipio) {
            return String.valueOf(((SgMunicipio) o).getMunPk());
        } else if (o instanceof String) {
            if (StringUtils.isBlank((String) o)) {
                return null;
            }
        }
        throw new ConverterException(new FacesMessage(o + " is not a valid Municipio"));
    }
}
