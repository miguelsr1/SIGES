/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.excepciones;

import java.util.ArrayList;
import java.util.List;


public class BusinessException extends Exception {
    
    private List<BusinessError> errores = new ArrayList<>();
    
    public BusinessException() {
    }
    
    public BusinessException(String msg) {
        super(msg);
        this.getErrores().add(new BusinessError(msg));
    }
        
    public BusinessException(Exception ex){
        super(ex);
    }
    
    public void addError(String campo, String error) {
        if (campo != null && error != null) {
            this.getErrores().add(new BusinessError(error, campo));
        }
    }
    
    public void addError(String error) {
        if (error != null) {
            this.getErrores().add(new BusinessError(error));
        }
    }
    
    public List<BusinessError> getErrores() {
        return errores;
    }

    public void setErrores(List<BusinessError> errores) {
        this.errores = errores;
    }
       
}

