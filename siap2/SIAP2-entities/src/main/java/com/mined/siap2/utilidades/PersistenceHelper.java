/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package com.mined.siap2.utilidades;

import javax.persistence.OptimisticLockException;
import javax.persistence.UniqueConstraint;
import javax.validation.ConstraintViolationException;
import org.eclipse.persistence.exceptions.DatabaseException;

public class PersistenceHelper {

    public static Boolean isConstraintViolation(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof UniqueConstraint) && !(t instanceof ConstraintViolationException) ) {
            t = t.getCause();
            if (t instanceof ConstraintViolationException || t instanceof DatabaseException) {
                return true;
            }
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
