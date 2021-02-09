/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;


import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoLiquidacion;

/**
 * Validación de las reglas de negocio de movimientos de liquidación
 * @author Sofis Solutions
 */
public class MovimientoLiquidacionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mlq SgMovimientoLiquidacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMovimientoLiquidacion mlq) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mlq == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
//            if (mlq.getMlqLiquidacionPk()==null) {
//                ge.addError("mlqLiquidacionPk", Errores.ERROR_CODIGO_VACIO);
//            }
//            if (mlq.getMlqMovimientoPk()==null) {
//                ge.addError("mlqMovimientoPk", Errores.ERROR_NOMBRE_VACIO);
//            }
            if(mlq.getMlqEvaluado()!=null){
                if(mlq.getMlqEvaluado().equals(Boolean.FALSE) && StringUtils.isBlank(mlq.getMlqComentario())){
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
