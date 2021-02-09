/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.dto.centroseducativos.SgPersona;
import sv.gob.mined.siges.web.restclient.PersonaRestClient;

@Named
@ApplicationScoped
public class PersonaConverter implements Converter {

    private static final Logger LOGGER = Logger.getLogger(PersonaConverter.class.getName());
    
    @Inject
    private PersonaRestClient restClient;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            return restClient.obtenerPorId(Long.valueOf(value));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener la persona.", ex);
            throw new ConverterException("Id inválida");

        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof SgPersona) {
            return String.valueOf(((SgPersona) o).getPerPk());
        } else if (o instanceof String) {
            if (StringUtils.isBlank((String) o)) {
                return null;
            }
        }
        throw new ConverterException(new FacesMessage(o + " is not a valid Persona"));
    }
}