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
    
    public static BusinessReturnedException getDiplomadoEstudianteViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessReturnedException be = new BusinessReturnedException();
            if (exception != null) {
                if (exception.contains("estudiante_diplomado_est_anio")) {
                    be.addError(Errores.ERROR_ESTUDIANTE_TIENE_DIPLOMADO_SEDE);              
                } else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }
            }

                return be;
            }            
        return null;
    }

    public static BusinessReturnedException getPersonaViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessReturnedException be = new BusinessReturnedException();
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
                } else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }
            }

                return be;
            }            
        return null;
    }

    public static BusinessReturnedException getMatriculaViolationBusinessException(Exception ex) {
        return PersistenceHelper.getPersonaViolationBusinessException(ex);
    }

    public static BusinessReturnedException getEstudianteViolationBusinessException(Exception ex) {
        return PersistenceHelper.getPersonaViolationBusinessException(ex);
    }
    
    public static BusinessReturnedException getPersonalViolationBusinessException(Exception ex) {
        return PersistenceHelper.getPersonaViolationBusinessException(ex);
    }

    public static BusinessReturnedException getSelloFirmaViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessReturnedException be = new BusinessReturnedException();
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

    public static BusinessReturnedException getCalendarioViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessReturnedException be = new BusinessReturnedException();
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
    

    public static BusinessReturnedException getServicioEducativoBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessReturnedException be = new BusinessReturnedException();
            if (exception != null) {
                if (exception.contains("sg_servicio_educativo_sede_grado_opcion_programa_uk")) {
                    be.addError(Errores.ERROR_SERVICIO_SEDE_GRADO_OPCION_PROGRAMA_EXISTENTE);
                } else if (exception.contains("sg_servicio_educativo_sede_sin_opcion_programa_uk")) {
                    be.addError(Errores.ERROR_SERVICIO_SEDE_GRADO_EXISTENTE);
                } else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }
            }

            return be;
        }
        return null;
    }
    
    public static BusinessReturnedException getReglaEquivalenciaViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessReturnedException be = new BusinessReturnedException();
            if (exception != null) {
            if (exception.contains("sg_regla_equivalencia_detalle_gra_plan_ope_uk")) {
                    be.addError(Errores.ERROR_GRADO_PLAN_ESTUDIO_OPERACION_DUPLICADOS);
                } else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }    
            }

            return be;
        }
        return null;
    }
    
    public static BusinessReturnedException getControlAsistenciaViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessReturnedException be = new BusinessReturnedException();
            if (exception != null) {
            if (exception.contains("cac_fecha_seccion_uk")) {
                    be.addError(Errores.ERROR_CONTROL_ASISTENCIA_EXISTENTE_FECHA_SECCION);
                } else {
                    be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                }    
            }

            return be;
        }
        return null;
    }
    
    public static BusinessReturnedException getlCalificacionViolationBusinessException(Exception ex) {
        Throwable t = ex.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException) && !(t instanceof NonUniqueObjectException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException || t instanceof NonUniqueObjectException) {
            String exception = t.getCause().getLocalizedMessage();
            BusinessReturnedException be = new BusinessReturnedException();
            if (exception != null) {
            if (exception.contains("cae_estudiante_fk_calificacion_fk")) {
                    be.addError(Errores.ERROR_CALIFICACION_ESTUDIANTE_EXISTENTE_CABEZAL);
                } else {
                    if(exception.contains("sg_calificacion_nota_institucional_aprobacion_uk")){
                        be.addError(Errores.ERROR_CALIFICACION_NOTIN_APR_EXISTENTE);
                    }else{
                        be.addError(Errores.ERROR_CONSTRAINT_EXCEPTION);
                    }
                }    
            }
            

            return be;
        }
        return null;
    }
}
