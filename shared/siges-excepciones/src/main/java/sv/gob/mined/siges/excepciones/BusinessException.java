/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.excepciones;

import java.util.ArrayList;
import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BusinessException extends GeneralException {

    public BusinessException() {
    }

    public void addError(String campo, String error) {
        if (this.getErrores() == null) {
            this.setErrores(new ArrayList());
        }   
        if (error != null) {
            this.getErrores().add(new Error(error, campo));
        }
    }
    
    public void addError(String campo, String error, Object[] variables) {
        if (this.getErrores() == null) {
            this.setErrores(new ArrayList());
        }   
        if (error != null) {
            this.getErrores().add(new Error(error, campo, variables));
        }
    }

    @Override
    public String getMessage() {
        return "Excepción de negocio";
    }
    
    
}
