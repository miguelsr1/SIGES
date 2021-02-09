/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.excepciones;

import java.util.ArrayList;
import java.util.List;

public class BusinessReturnedException {

    private List<Error> errores = new ArrayList<>();
    private List<Error> erroresTextoPlano = new ArrayList<>();


    public BusinessReturnedException() {
    }
    
    public BusinessReturnedException(BusinessException be) {
        this.errores = be.getErrores();
        this.erroresTextoPlano = be.getErroresTextoPlano();
    }
    
    public BusinessReturnedException(List<Error> errores) {
        this.errores = errores;
    }


    public BusinessReturnedException(String error) {
        this.errores.add(new Error(error));
    }

    public List<Error> getErrores() {
        return errores;
    }

    public List<Error> getErroresTextoPlano() {
        return erroresTextoPlano;
    }

    
    public void addError(String error) {
        Error e = new Error(error);
        if (this.errores == null) {
            this.errores = new ArrayList();
        }
        this.errores.add(e);
    }

}
