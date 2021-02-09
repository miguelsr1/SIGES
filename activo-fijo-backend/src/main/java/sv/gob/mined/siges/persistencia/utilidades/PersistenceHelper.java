/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.utilidades;

import javax.persistence.OptimisticLockException;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.ConstraintViolationException;
import sv.gob.mined.siges.excepciones.BusinessException;
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

    public static BusinessException getCodigoInventarioViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessException be = new BusinessException();
            if (exception != null) {
                if (exception.contains("codigo_inventario_uk")) {
                    be.addError(Errores.ERROR_CODIGO_INVENTARIO_DUPLICADO);
                } else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }
            }

            return be;
        }
        return null;
    }
    
    public static BusinessException getCodigoDescargoViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessException be = new BusinessException();
            if (exception != null) {
                if (exception.contains("des_codigo_descargo_uk")) {
                    be.addError(Errores.ERROR_CODIGO_DESCARGO_DUPLICADO);
                } else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }
            }

            return be;
        }
        return null;
    }
    
    public static BusinessException getCodigoTrasladoViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessException be = new BusinessException();
            if (exception != null) {
                if (exception.contains("codigo_traslado_sede_uk") || exception.contains("codigo_traslado_unidad_uk")) {
                    be.addError(Errores.ERROR_CODIGO_TRASLADO_DUPLICADO);
                } else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }
            }

            return be;
        }
        return null;
    }
}
