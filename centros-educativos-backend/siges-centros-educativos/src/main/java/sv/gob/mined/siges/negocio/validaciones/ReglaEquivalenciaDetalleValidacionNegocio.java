/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgReglaEquivalenciaDetalle;

public class ReglaEquivalenciaDetalleValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity
     * @return true/false
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgReglaEquivalenciaDetalle entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(entity.getRedGrado() == null) {
                 ge.addError("redGrado", Errores.ERROR_GRADO_VACIO);
            }
            
            if(entity.getRedPlanEstudio() == null) {
                 ge.addError("redPlanEstudio", Errores.ERROR_PLAN_ESTUDIO_VACIO);
            } 
            
            if(entity.getRedReglaEquivalencia() == null) {
                 ge.addError("redReglaEquivalencia", Errores.ERROR_REGLA_EQUIVALENCIA_VACIO);
            }
            
            if(entity.getRedOperacion() == null ) {
                ge.addError("redOperacion", Errores.ERROR_OPERACION_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}
