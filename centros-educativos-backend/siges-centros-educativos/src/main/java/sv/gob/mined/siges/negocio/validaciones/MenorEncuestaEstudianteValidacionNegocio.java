/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.ConfiguracionBean;
import sv.gob.mined.siges.persistencia.entidades.SgMenorEncuestaEstudiante;

public class MenorEncuestaEstudianteValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgPersona
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMenorEncuestaEstudiante entity, String emailPattern, ConfiguracionBean configuracionBean) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_PERSONA_VACIA);
        } else {
            
            if (entity.getMenMatriculadoSiges() == null){
                 ge.addError("enMatriculadoSiges", Errores.ERROR_MATRICULADO_SIGES_VACIO);
            }
            
            if (entity.getMenTieneNie() == null){
                 ge.addError("menTieneNie", Errores.ERROR_TIENE_NIE_VACIO);
            }
            
            if (entity.getMenEsFamiliar() == null){
                 ge.addError("menEsFamiliar", Errores.ERROR_ES_FAMILIAR_VACIO);
            }
            
            if (entity.getMenEstudia() == null){
                 ge.addError("menEstudia", Errores.ERROR_ESTUDIA_VACIO);
            }

            if (BooleanUtils.isTrue(entity.getMenTieneNie())) {
                if (entity.getMenNie() == null) {
                    ge.addError("menNie", Errores.ERROR_NIE_VACIO);
                }
            }
            
            if (StringUtils.isBlank(entity.getMenPrimerNombre())) {
                ge.addError("menPrimerNombre", Errores.ERROR_PRIMER_NOMBRE_VACIO);
            } else if (entity.getMenPrimerNombre().length() > 100) {
                ge.addError("menPrimerNombre", Errores.ERROR_PRIMER_NOMBRE_100);
            }

            if (StringUtils.isBlank(entity.getMenPrimerApellido())) {
                ge.addError("menPrimerApellido", Errores.ERROR_PRIMER_APELLIDO_VACIO);
            } else if (entity.getMenPrimerApellido().length() > 100) {
                ge.addError("menPrimerApellido", Errores.ERROR_PRIMER_APELLIDO_100);
            }

            if (!StringUtils.isBlank(entity.getMenSegundoNombre()) && entity.getMenSegundoNombre().length() > 100) {
                ge.addError("menSegundoNombre", Errores.ERROR_SEGUNDO_NOMBRE_100);
            }


            if (!StringUtils.isBlank(entity.getMenSegundoApellido()) && entity.getMenSegundoApellido().length() > 100) {
                ge.addError("menSegundoApellido", Errores.ERROR_SEGUNDO_APELLIDO_100);
            }

            if (entity.getMenFechaNacimiento() == null) {
                ge.addError("menFechaNacimiento", Errores.ERROR_FECHA_NACIMIENTO_VACIO);
            } else if (entity.getMenFechaNacimiento().compareTo(LocalDate.now()) > 0) {
                ge.addError("menFechaNacimiento", Errores.ERROR_FECHA_NACIMIENTO_MAYOR_ACTUAL);
            }

            if (entity.getMenSexo() == null) {
                ge.addError("menSexo", Errores.ERROR_SEXO_VACIO);
            }
            if (entity.getMenEstadoCivil() == null) {
                ge.addError("menEstadoCivil", Errores.ERROR_ESTADO_CIVIL_VACIO);
            }
            
            if (entity.getMenNacionalidad() == null) {
                ge.addError("menNacionalidad", Errores.ERROR_NACIONALIDAD_VACIA);
            }

        }

        if (ge.getErrores().size() > 0) {
            throw ge;
        }

        return respuesta;
    }

}
