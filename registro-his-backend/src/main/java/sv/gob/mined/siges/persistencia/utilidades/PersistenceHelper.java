/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import javax.persistence.OptimisticLockException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.ConstraintViolationException;
import sv.gob.mined.siges.excepciones.BusinessReturnedException;
import sv.gob.mined.siges.negocio.mensajes.Errores;

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

    public static BusinessReturnedException getIncorporacionViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessReturnedException be = new BusinessReturnedException();
            if (exception != null) {
                if (exception.contains("inc_dui_uk")) {
                    be.addError(Errores.ERROR_IDENTIFICACION_DUPLICADA);
                } else if (exception.contains("inc_carne_residente_uk")) {
                    be.addError(Errores.ERROR_IDENTIFICACION_DUPLICADA);
                } else if (exception.contains("inc_pasaporte_pais_uk")) {
                    be.addError(Errores.ERROR_IDENTIFICACION_DUPLICADA);
                } else if (exception.contains("inc_numero_resolucion_uk")) {
                    be.addError(Errores.ERROR_NUMERO_RESOLUCION_DUPLICADO);
                } else if (exception.contains("inc_numero_tramite_uk")) {
                    be.addError(Errores.ERROR_NUMERO_TRAMITE_DUPLICADO);
                } else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }
            }

            return be;
        }
        return null;
    }

}
