package gob.mined.siap2.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Sofis Solutions
 */
public class ValidarIntegerPositivo implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        try {
            System.out.println("***********value " + value);
            int valor = Integer.parseInt((String)value);;

            if ((valor < 0)) {
               throw new ValidatorException(new FacesMessage("Valor debe ser un número entero positivo"));
            }
        } catch (NumberFormatException nfe) {
            System.out.println("} catch (NumberFormatException nfe) {");
            throw new ValidatorException(new FacesMessage("Valor debe ser un número entero positivo"));
        }

    }

}
