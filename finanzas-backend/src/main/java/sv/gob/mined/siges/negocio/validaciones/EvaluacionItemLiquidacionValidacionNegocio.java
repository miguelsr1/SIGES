/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEvaluacionItemLiquidacion;

/**
 * Validación de las reglas de negocio de la evaluación de items de una liquidación
 * @author Sofis Solutions
 */
public class EvaluacionItemLiquidacionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param eil SgEvaluacionItemLiquidacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEvaluacionItemLiquidacion eil) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (eil == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (eil.getEilLiqFk()==null) {
                ge.addError("eilCodigo", Errores.ERROR_EVALUACION_LIQUIDACION_VACIA);
            }
            if (eil.getEilItemFk()==null) {
                ge.addError("eilNombre", Errores.ERROR_EVALUACION_ITEM_VACIA);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

  
}
