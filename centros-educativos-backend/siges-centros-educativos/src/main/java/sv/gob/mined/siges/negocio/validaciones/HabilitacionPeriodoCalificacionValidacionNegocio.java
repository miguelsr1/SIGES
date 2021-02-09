/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumEstadoHabilitacionPeriodoCalificacion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgHabilitacionPeriodoCalificacion;

/**
 *
 * @author Sofis Solutions
 */
public class HabilitacionPeriodoCalificacionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param hpc SgHabilitacionPeriodoCalificacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgHabilitacionPeriodoCalificacion hpc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (hpc == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (hpc.getHpcMatriculaFk()== null) {
                ge.addError("hpcCodigo", Errores.ERROR_MATRICULA_VACIO);
            }
            if (hpc.getHpcEstudianteFk() == null) {
                ge.addError("hpcCodigo", Errores.ERROR_ESTUDIANTE_VACIO);
            }

            if (hpc.getRelPeriodosHabilitacionPeriodo()== null || hpc.getRelPeriodosHabilitacionPeriodo().isEmpty()) {
                ge.addError("acaFechaDesde", Errores.ERROR_CALIFICACIONES_VACIO);
            }
            
            if(StringUtils.isBlank(hpc.getHpcObservacion())){
                ge.addError("acaFechaDesde", Errores.ERROR_OBSERVACION_MOTIVO_VACIO);
            }
            if (hpc.getHpcEstado()!=null && EnumEstadoHabilitacionPeriodoCalificacion.RECHAZADA.equals(hpc.getHpcEstado())) {
                if(StringUtils.isBlank(hpc.getHpcObservacionAprobacionRechazo())){
                     ge.addError("acaFechaDesde", Errores.ERROR_OBSERVACION_VACIO);
                }
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
