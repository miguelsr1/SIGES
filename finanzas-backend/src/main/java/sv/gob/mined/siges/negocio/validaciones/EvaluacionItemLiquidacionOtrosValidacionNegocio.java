/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEvaluacionItemLiquidacionOtros;

/**
 * @author Sofis Solutions
 */
public class EvaluacionItemLiquidacionOtrosValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param elo SgEvaluacionItemLiquidacionOtros
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEvaluacionItemLiquidacionOtros elo) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (elo == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
//            if (StringUtils.isBlank(elo.getEloCodigo())) {
//                ge.addError("eloCodigo", Errores.ERROR_CODIGO_VACIO);
//            } else if (elo.getEloCodigo().length() > 45) {
//                ge.addError("eloCodigo", Errores.ERROR_LARGO_CODIGO_45);
//            }
//            if (StringUtils.isBlank(elo.getEloNombre())) {
//                ge.addError("eloNombre", Errores.ERROR_NOMBRE_VACIO);
//            } else if (elo.getEloNombre().length() > 255) {
//                ge.addError("eloNombre", Errores.ERROR_LARGO_NOMBRE_255);
//            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
