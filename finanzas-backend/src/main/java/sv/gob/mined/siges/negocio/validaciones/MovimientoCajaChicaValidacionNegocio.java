/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoCajaChica;

/**
 * Validación de las reglas de negocio de la caja chica
 *
 * @author Sofis Solutions
 */
public class MovimientoCajaChicaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mcc SgMovimientoCajaChica
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMovimientoCajaChica mcc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mcc == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (mcc.getMccCuentaFK() == null) {
                ge.addError("mccCuentaFK", Errores.ERROR_MCB_CUENTA_BANCARIA);
            }
            if (mcc.getMccFecha() == null) {
                ge.addError("mccFecha", Errores.ERROR_MCB_FECHA);
            }
            if (!StringUtils.isBlank(mcc.getMccDetalle()) && mcc.getMccDetalle().length() > 255) {
                ge.addError("mccDetalle", Errores.ERROR_MCB_DETALLE_255);
            }
            if (mcc.getMccMonto() == null) {
                ge.addError("mccMonto", Errores.ERROR_MCB_MONTO);
            }
            if (mcc.getMccTipoMovimiento() == null) {
                ge.addError("mccTipoMovimiento", Errores.ERROR_MCB_TIPO_MOVIMIENTO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
