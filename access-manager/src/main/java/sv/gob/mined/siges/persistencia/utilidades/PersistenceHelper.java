/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import javax.persistence.OptimisticLockException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.ConstraintViolationException;

public class PersistenceHelper {

    public static Boolean isConstraintViolation(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            return true;
        }
        return false;
    }
    
    public static Boolean isOptimisticException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof OptimisticLockException)) {
            t = t.getCause();
        }
        if (t instanceof OptimisticLockException) {
            return true;
        }
        return false;
    }

}
