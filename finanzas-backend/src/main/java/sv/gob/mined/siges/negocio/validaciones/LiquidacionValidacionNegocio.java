/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgLiquidacion;

/**
 * Validación de las reglas de negocio de liquidación
 * 
 * @author Sofis Solutions
 */
public class LiquidacionValidacionNegocio{

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param liq SgLiquidacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgLiquidacion liq) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (liq == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (liq.getLiqComponentePk()==null) {
                ge.addError("liqComponentePk", Errores.ERROR_LIQUIDACION_COMPONENTE_VACIO);
            }
            
            if (liq.getLiqSedePk()==null) {
                ge.addError("liqSedePk", Errores.ERROR_LIQUIDACION_SEDE_VACIO);
            }
            
            if (liq.getLiqAnioPk()==null) {
                ge.addError("liqAnioPk", Errores.ERROR_LIQUIDACION_ANIO_VACIO);
            }
            
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    
}
