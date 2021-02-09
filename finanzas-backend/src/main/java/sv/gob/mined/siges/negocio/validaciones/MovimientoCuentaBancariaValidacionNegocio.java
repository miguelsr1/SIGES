/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.math.BigDecimal;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumTipoRetiroMovimientoCB;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.negocio.servicios.MovimientoCuentaBancariaBean;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoCuentaBancaria;

/**
 * Validación de los movimientos de las cuentas bancarias
 *
 * @author Sofis Solutions
 */
public class MovimientoCuentaBancariaValidacionNegocio {

    private static final Logger LOGGER = Logger.getLogger(MovimientoCuentaBancariaBean.class.getName());

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mcb SgMovimientoCuentaBancaria
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMovimientoCuentaBancaria mcb) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mcb == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (mcb.getMcbCuentaFK() == null) {
                ge.addError("mcbCuentaFK", Errores.ERROR_MCB_CUENTA_BANCARIA);
            }
            if (mcb.getMcbFecha() == null) {
                ge.addError("mcbFecha", Errores.ERROR_MCB_FECHA);
            }
            if (!StringUtils.isBlank(mcb.getMcbDetalle()) && mcb.getMcbDetalle().length() > 255) {
                ge.addError("mcbDetalle", Errores.ERROR_MCB_DETALLE_255);
            }
            if (mcb.getMcbMonto() == BigDecimal.ZERO) {
                ge.addError("mcbMonto", Errores.ERROR_MCB_MONTO);
            }
            if (mcb.getMcbTipoMovimiento() == null) {
                ge.addError("mcbTipoMovimiento", Errores.ERROR_MCB_TIPO_MOVIMIENTO);
            }

//            if (mcb.getMcbTipoRetiro() == null && mcb.getMcbTipoMovimiento() != null && mcb.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE)) {
//                ge.addError("mcbTipoRetiro", Errores.ERROR_TIPO_RETIRO_VACIO);
//            }
        }
        if (mcb.getMcbTipoMovimiento() != null) {
            if (mcb.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE)) {
                if (mcb.getMcbTipoRetiro() != null) {
                    if (mcb.getMcbTipoRetiro().equals(EnumTipoRetiroMovimientoCB.CHEQUE)) {
                        if (mcb.getMcbChequeCb() == null && mcb.getMcbTipoMovimiento().equals(TipoMovimiento.DEBE) && mcb.getMcbTipoRetiro().equals(EnumTipoRetiroMovimientoCB.CHEQUE)) {
                            ge.addError("mcbChequeCb", Errores.ERROR_CHEQUE_RETIRO_VACIO);
                        }
                    }
                } else if (mcb.getMcbTipoRetiro() == null) {
                    ge.addError("mcbTipoRetiro", Errores.ERROR_TIPO_RETIRO_VACIO);
                }
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
