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
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.filtrosbusqueda.FiltroCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.restclient.CategoriaPresupuestoEscolarRestClient;

@Named
@ApplicationScoped
public class ComponenteConverter implements Converter {

    
    @Inject
    private CategoriaPresupuestoEscolarRestClient restClient;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        try {
            FiltroCategoriaPresupuestoEscolar filtroComponente = new FiltroCategoriaPresupuestoEscolar();
            filtroComponente.setCpeId(Long.valueOf(value));
            filtroComponente.setIncluirCampos(new String[]{"cpeId","cpeVersion", "cpeNombre", "cpeCodigo"});
            List<SsCategoriaPresupuestoEscolar> list = restClient.buscarComponente(filtroComponente);
            if(!list.isEmpty()){
                return list.get(0);
            }
            else{
                return null;
            }
        } catch (Exception ex) {
            throw new ConverterException("Id inválida");

        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof SsCategoriaPresupuestoEscolar) {
            return String.valueOf(((SsCategoriaPresupuestoEscolar) o).getCpeId());
        } else if (o instanceof String) {
            if (StringUtils.isBlank((String) o)) {
                return null;
            }
        }
        throw new ConverterException(new FacesMessage(o + " is not a valid Sede"));
    }
}
