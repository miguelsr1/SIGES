/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.converters;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.web.dto.SgChequera;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroChequera;
import sv.gob.mined.siges.web.restclient.ChequeraRestClient;

@Named
@ApplicationScoped
public class ChequerasConverter implements Converter {

    @Inject
    private ChequeraRestClient restClient;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            FiltroChequera chequera = new FiltroChequera();
            chequera.setIncluirCampos(new String[]{"chePk",
                "cheCuentaBancariaFk.cbcPk",
                "cheCuentaBancariaFk.cbcNumeroCuenta",
                "cheCuentaBancariaFk.cbcVersion",
                "cheFechaChequera",
                "cheSerie",
                "cheNumeroInicial",
                "cheNumeroFinal",
                "cheVersion"
            });
            chequera.setChePk(Long.valueOf(value));
            List<SgChequera> chequeras = restClient.buscar(chequera);
            if (!chequeras.isEmpty()) {
                return chequeras.get(0);
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw new ConverterException("Chequera inválida");

        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof SgChequera) {
            return String.valueOf(((SgChequera) o).getChePk());
        } else if (o instanceof String) {
            if (StringUtils.isBlank((String) o)) {
                return null;
            }
        }
        throw new ConverterException(new FacesMessage(o + " is not a valid Chequera"));
    }
}
