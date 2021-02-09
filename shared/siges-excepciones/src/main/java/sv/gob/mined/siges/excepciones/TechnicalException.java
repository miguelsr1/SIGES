/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.excepciones;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class TechnicalException extends GeneralException{
    
    public TechnicalException() {
        
    }
    
    public TechnicalException(Exception ex) {
        super(ex);
    }
    
    public TechnicalException(Exception ex, String mensaje) {
        super(mensaje, ex);
        super.addError(mensaje);
    }
    
    public TechnicalException(String mensaje) {
        super(mensaje);
        super.addError(mensaje);
    }
}
