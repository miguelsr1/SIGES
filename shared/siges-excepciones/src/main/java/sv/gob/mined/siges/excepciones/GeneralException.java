/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.excepciones;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class GeneralException extends RuntimeException {

    private List<Error> errores = new ArrayList();
    private List<Error> erroresTextoPlano = new ArrayList();

    public GeneralException() {
        super("GeneralException");
    }

    public GeneralException(String msg) {
        super(msg);
    }
    
    public GeneralException(Exception ex){
        super(ex);
    }
    
    public GeneralException(String mensaje, Exception ex){
        super(mensaje, ex);
    }

    public List<Error> getErrores() {
        return errores;
    }

    public void setErrores(List<Error> errores) {
        this.errores = errores;
    }

    public List<Error> getErroresTextoPlano() {
        return erroresTextoPlano;
    }

    public void setErroresTextoPlano(List<Error> erroresTextoPlano) {
        this.erroresTextoPlano = erroresTextoPlano;
    }
    
    public void addError(String error) {
        Error e = new Error(error);
        if (this.errores == null) {
            this.errores = new ArrayList();
        }
        this.errores.add(e);
    }
    
    public void addErrorTextoPlano(String error) {
        Error e = new Error(error);
        if (this.erroresTextoPlano == null) {
            this.erroresTextoPlano = new ArrayList();
        }
        this.erroresTextoPlano.add(e);
    }


}
