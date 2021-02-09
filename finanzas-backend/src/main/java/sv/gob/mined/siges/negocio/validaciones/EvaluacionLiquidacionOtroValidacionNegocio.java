/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEvaluacionLiquidacionOtro;

/**
 * @author Sofis Solutions
 */
public class EvaluacionLiquidacionOtroValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param eli SgEvaluacionLiquidacionOtro
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEvaluacionLiquidacionOtro eli) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (eli == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
//            if (StringUtils.isBlank(eli.getEliCodigo())) {
//                ge.addError("eliCodigo", Errores.ERROR_CODIGO_VACIO);
//            } else if (eli.getEliCodigo().length() > 45) {
//                ge.addError("eliCodigo", Errores.ERROR_LARGO_CODIGO_45);
//            }
//            if (StringUtils.isBlank(eli.getEliNombre())) {
//                ge.addError("eliNombre", Errores.ERROR_NOMBRE_VACIO);
//            } else if (eli.getEliNombre().length() > 255) {
//                ge.addError("eliNombre", Errores.ERROR_LARGO_NOMBRE_255);
//            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
