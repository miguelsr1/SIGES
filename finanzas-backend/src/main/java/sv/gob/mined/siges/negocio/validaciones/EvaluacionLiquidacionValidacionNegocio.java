/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEvaluacionLiquidacion;

/**
 * 
 * Validación de las reglas de negocio de la evaluación de una liquidación
 * @author Sofis Solutions
 */
public class EvaluacionLiquidacionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param elq SgEvaluacionLiquidacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEvaluacionLiquidacion elq) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (elq == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (elq.getElqLiquidacionFk()==null) {
                ge.addError("elqLiquidacionFk", Errores.ERROR_EVALUACION_LIQ_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }


}
