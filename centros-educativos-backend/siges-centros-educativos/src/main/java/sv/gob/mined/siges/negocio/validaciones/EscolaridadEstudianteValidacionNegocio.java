/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEscolaridadEstudiante;

/**
 *
 * @author Sofis Solutions
 */
public class EscolaridadEstudianteValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param esc SgEscolaridadEstudiante
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEscolaridadEstudiante esc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (esc == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (esc.getEscEstudiante() == null) {
                ge.addError("escCodigo", Errores.ERROR_ESTUDIANTE_VACIO);
            }

            if (esc.getEscResultado() == null) {
                ge.addError("escResultado", Errores.ERROR_RESULTADO_VACIO);
            }

            if (BooleanUtils.isTrue(esc.getEscGeneradaPorEquivalencia())) {

                if (StringUtils.isBlank(esc.getEscEqNumeroTramite())) {
                    ge.addError("numTramite", Errores.ERROR_NUMERO_TRAMITE_VACIO);
                } else if (esc.getEscEqNumeroTramite().length() > 255) {
                    ge.addError("numTramite", Errores.ERROR_LARGO_NUMERO_TRAMITE_255);
                }

                if (esc.getEscEqAnio() == null) {
                    ge.addError("escAnio", Errores.ERROR_ANIO_LECTIVO_VACIO);
                }
                if (esc.getEscEqGrado() == null) {
                    ge.addError("escGrado", Errores.ERROR_GRADO_VACIO);
                }
                if (esc.getEscEqPlanEstudio() == null){
                    ge.addError("escPlanEstudio", Errores.ERROR_PLAN_ESTUDIO_VACIO);
                }

            } else {
                if (esc.getEscServicioEducativo() == null) {
                    ge.addError("escServicioEducativo", Errores.ERROR_SERVICIO_EDUCATIVO_VACIO);
                }

                if (esc.getEscAnio() == null) {
                    ge.addError("escAnio", Errores.ERROR_ANIO_LECTIVO_VACIO);
                }

            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
