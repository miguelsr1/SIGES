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

/**
 * Clase para la gestión de algunos elementos vinculados a la persistencia.
 *
 * @author jgiron
 */
public class PersistenceHelper {

    /**
     * Determina si la excepción es una violación de restricción.
     *
     * @param ex
     * @return
     */
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

    /**
     * Determina si esun optimistic exception.
     *
     * @param ex
     * @return
     */
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

    /**
     * Determina si la excepción es por una regla de negocio.
     *
     * @param ex
     * @return
     */
    public static BusinessException getPersonaViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessException be = new BusinessException();
            if (exception != null) {
                if (exception.contains("personas_cun")) {
                    be.addError(Errores.ERROR_CUN_DUPLICADO);
                } else if (exception.contains("personas_dui")) {
                    be.addError(Errores.ERROR_DUI_DUPLICADO);
                } else if (exception.contains("personas_nie")) {
                    be.addError(Errores.ERROR_NIE_DUPLICADO);
                } else if (exception.contains("personas_nip")) {
                    be.addError(Errores.ERROR_NIP_DUPLICADO);
                } else if (exception.contains("personas_nup")) {
                    be.addError(Errores.ERROR_NUP_DUPLICADO);
                } else if (exception.contains("personas_nit")) {
                    be.addError(Errores.ERROR_NIT_DUPLICADO);
                } else if (exception.contains("personas_isss")) {
                    be.addError(Errores.ERROR_ISSS_DUPLICADO);
                } else if (exception.contains("ide_numero_tipo_pais_uk")) {
                    be.addError(Errores.ERROR_IDENTIFICACION_DUPLICADA);
                } else if (exception.contains("sg_persona_partida_nacimiento_uk")) {
                    be.addError(Errores.ERROR_PARTIDA_NACIMIENTO_DUPLICADA);
                } else if (exception.contains("personas_inpep")) {
                    be.addError(Errores.ERROR_INPEP_DUPLICADO);
                } else if (exception.contains("estudiante_persona_uk")) {
                    be.addError(Errores.ERROR_ESTUDIANTE_PERSONA_DUPLICADA);
                }
                else if (exception.contains("num_cuenta_unique")) {
                    be.addError(Errores.ERROR_NUM_CUENTA_DUPLICADO);
                }
                else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }
            }

            return be;
        }
        return null;
    }

    /**
     * Determina si es una excepción de negocio por violación en la matrícula.
     *
     * @param ex
     * @return
     */
    public static BusinessException getMatriculaViolationBusinessException(Exception ex) {
        return PersistenceHelper.getPersonaViolationBusinessException(ex);
    }

    /**
     * Determina si es una excepción de negocio por violación en el estudiante.
     *
     * @param ex
     * @return
     */
    public static BusinessException getEstudianteViolationBusinessException(Exception ex) {
        return PersistenceHelper.getPersonaViolationBusinessException(ex);
    }

    /**
     * Determina si es una excepción de negocio por violación en la persona.
     *
     * @param ex
     * @return
     */
    public static BusinessException getPersonalViolationBusinessException(Exception ex) {
        return PersistenceHelper.getPersonaViolationBusinessException(ex);
    }

    /**
     * Determina si es una excepción de negocio por violación en el sello y
     * firma.
     *
     * @param ex
     * @return
     */
    public static BusinessException getSelloFirmaViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessException be = new BusinessException();
            if (exception != null) {
                if (exception.contains("sg_sello_firma_sede_hab_uk_idx")) {
                    be.addError(Errores.ERROR_SELLO_FIRMA_SEDE_TIPO_EXISTENTE);
                } else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }
            }

            return be;
        }
        return null;
    }

    /**
     * Determina si es una excepción de negocio por violación en el calendario.
     *
     * @param ex
     * @return
     */
    public static BusinessException getCalendarioViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessException be = new BusinessException();
            if (exception != null) {
                if (exception.contains("sg_calendario_tipo_anio_uk_idx")) {
                    be.addError(Errores.ERROR_CALENDARIO_ANIO_TIPO_EXISTENTE);
                } else if (exception.contains("sg_calendario_codigo_uk")) {
                    be.addError(Errores.ERROR_CODIGO_O_NOMBRE_DUPLICADOS);
                } else if (exception.contains("sg_calendario_nombre_uk")) {
                    be.addError(Errores.ERROR_CODIGO_O_NOMBRE_DUPLICADOS);
                } else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }
            }

            return be;
        }
        return null;
    }

}
