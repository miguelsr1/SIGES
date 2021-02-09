/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoLiquidacionOtro;

/**
 * Validación de las reglas de negocio de los movimientos de liquidación de otros ingresos
 * @author Sofis Solutions
 */
public class MovimientoLiquidacionOtroValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mlo SgMovimientoLiquidacionOtro
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMovimientoLiquidacionOtro mlo) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mlo == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
//            if (mlq.getMlqLiquidacionPk()==null) {
//                ge.addError("mlqLiquidacionPk", Errores.ERROR_CODIGO_VACIO);
//            }
//            if (mlq.getMlqMovimientoPk()==null) {
//                ge.addError("mlqMovimientoPk", Errores.ERROR_NOMBRE_VACIO);
//            }
            if(mlo.getMloEvaluado()!=null){
                if(mlo.getMloEvaluado().equals(Boolean.FALSE) && StringUtils.isBlank(mlo.getMloComentario())){
                    ge.addError("mlqComentario", Errores.ERROR_MOV_LIQ_MOTIVO_VACIO);
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }


}
