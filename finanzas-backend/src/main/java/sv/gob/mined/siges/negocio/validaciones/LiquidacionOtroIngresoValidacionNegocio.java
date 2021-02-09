/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgLiquidacionOtroIngreso;

/** 
 * Validación de las reglas de negocio de liquidación de otros ingresos
 * @author Sofis Solutions
 */
public class LiquidacionOtroIngresoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param loi SgLiquidacionOtroIngreso
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgLiquidacionOtroIngreso loi) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (loi == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (loi.getLoiSedePk()==null) {
                ge.addError("liqSedePk", Errores.ERROR_LIQUIDACION_SEDE_VACIO);
            }
            
            if (loi.getLoiAnioPk()==null) {
                ge.addError("liqAnioPk", Errores.ERROR_LIQUIDACION_ANIO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
