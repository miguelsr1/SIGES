/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgIdentificacion;
import sv.gob.mined.siges.persistencia.entidades.SgPersona;
import sv.gob.mined.siges.persistencia.entidades.SgTelefono;
import sv.gob.mined.siges.utils.ValidationUtils;

public class PersonaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgPersona
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPersona entity, String emailPattern) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_PERSONA_VACIA);
        } else {

            try {
                PersonaValidacionNegocio.validarIdentificaciones(entity);
            } catch (BusinessException be) {
                ge.getErrores().addAll(be.getErrores());
            }

            if (StringUtils.isBlank(entity.getPerPrimerNombre())) {
                ge.addError("perPrimerNombre", Errores.ERROR_PRIMER_NOMBRE_VACIO);
            } else if (entity.getPerPrimerNombre().length() > 100) {
                ge.addError("perPrimerNombre", Errores.ERROR_PRIMER_NOMBRE_100);
            }

            if (StringUtils.isBlank(entity.getPerPrimerApellido())) {
                ge.addError("perPrimerApellido", Errores.ERROR_PRIMER_APELLIDO_VACIO);
            } else if (entity.getPerPrimerApellido().length() > 100) {
                ge.addError("perPrimerApellido", Errores.ERROR_PRIMER_APELLIDO_100);
            }

            if (!StringUtils.isBlank(entity.getPerSegundoNombre()) && entity.getPerSegundoNombre().length() > 100) {
                ge.addError("perSegundoNombre", Errores.ERROR_SEGUNDO_NOMBRE_100);
            }

            if (!StringUtils.isBlank(entity.getPerTercerNombre()) && entity.getPerTercerNombre().length() > 100) {
                ge.addError("perTercerNombre", Errores.ERROR_TERCER_NOMBRE_100);
            }

            if (!StringUtils.isBlank(entity.getPerSegundoApellido()) && entity.getPerSegundoApellido().length() > 100) {
                ge.addError("perSegundoApellido", Errores.ERROR_SEGUNDO_APELLIDO_100);
            }

            if (!StringUtils.isBlank(entity.getPerTercerApellido()) && entity.getPerTercerApellido().length() > 100) {
                ge.addError("perTercerApellido", Errores.ERROR_TERCER_APELLIDO_100);
            }

            PersonaValidacionNegocio.validarEmail(entity, emailPattern);

            if (entity.getPerFechaNacimiento() == null) {
                ge.addError("perFechaNacimiento", Errores.ERROR_FECHA_NACIMIENTO_VACIO);
            } else if (entity.getPerFechaNacimiento().compareTo(LocalDate.now()) > 0) {
                ge.addError("perFechaNacimiento", Errores.ERROR_FECHA_NACIMIENTO_MAYOR_ACTUAL);
            }
            if (entity.getPerNacionalidad() == null) {
                ge.addError("perNacionalidad", Errores.ERROR_NACIONALIDAD_VACIA);
            }

            if (entity.getPerSexo() == null) {
                ge.addError("perSexo", Errores.ERROR_SEXO_VACIO);
            }
            if (entity.getPerEstadoCivil() == null) {
                ge.addError("perEstadoCivil", Errores.ERROR_ESTADO_CIVIL_VACIO);
            }

            if (entity.getPerTelefonos() != null) {
                for (SgTelefono tel : entity.getPerTelefonos()) {
                    TelefonoValidacionNegocio.validar(tel);
                }
            }
            
            if (BooleanUtils.isTrue(entity.getPerTieneDiscapacidades())) {
                if (entity.getPerDiscapacidades() == null || entity.getPerDiscapacidades().isEmpty()) {
                    ge.addError("perDiscapacidades", Errores.ERROR_DISCAPACIDADES_VACIO);
                }
                if (entity.getPerTieneDiagnostico() == null && BooleanUtils.isNotTrue(entity.getPerNoValidarDiagnostico())){
                    ge.addError("perTieneDiagnostico", Errores.ERROR_TIENE_DIAGNOSTICO_VACIO);
                }
            }
            
            if (BooleanUtils.isTrue(entity.getPerTieneTrastornoAprendizaje())) {
                if (entity.getPerTrastornosAprendizaje() == null || entity.getPerTrastornosAprendizaje().isEmpty()) {
                    ge.addError("perDiscapacidades", Errores.ERROR_TRASTORNOS_APRENDIZAJE_VACIO);
                }
            }

        }

        if (ge.getErrores().size() > 0) {
            throw ge;
        }

        return respuesta;
    }

    public static boolean validarEmail(SgPersona entity, String emailPattern) {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_PERSONA_VACIA);
        } else {

            try {

                if (!StringUtils.isBlank(entity.getPerEmail())) {
                    if (entity.getPerEmail().length() > 100) {
                        ge.addError("perEmail", Errores.ERROR_CORREO_ELECTRONICO_100);
                    } else {
                        if (emailPattern != null) {
                            if (!StringUtils.isBlank(emailPattern)) {
                                if (!ValidationUtils.validarEmail(entity.getPerEmail(), emailPattern)) {
                                    ge.addError("perEmail", Errores.ERROR_EMAIL_FORMATO_INVALIDO);
                                }
                            } else {
                                ge.addError("perEmail", Errores.ERROR_NO_EXISTE_PATRON_CORREO_CATALOGO);
                            }
                        } else {
                            ge.addError("perEmail", Errores.ERROR_NO_EXISTE_PATRON_CORREO_CATALOGO);
                        }

                    }
                }

            } catch (Exception ex) {
                Logger.getLogger(PersonaValidacionNegocio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }

        return respuesta;
    }

    public static boolean validarIdentificaciones(SgPersona entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_PERSONA_VACIA);
        } else {

            if (entity.getPerDui() != null && !ValidationUtils.validarDUI(entity.getPerDui())) {
                ge.addError("perDui", Errores.ERROR_DUI_INCORRECTO);
            }

            for (SgIdentificacion ide : entity.getPerIdentificaciones()) {
                IdentificacionValidacionNegocio.validar(ide);
            }

        }

        if (ge.getErrores().size() > 0) {
            throw ge;
        }

        return respuesta;
    }
}
