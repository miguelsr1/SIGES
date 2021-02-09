/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientoDireccionDepartamental;

/**
 * Validación de las reglas de negocio de los movimietnos de las cuentas
 * bancarias direcciones departamentales
 *
 * @author Sofis Solutions
 */
public class MovimientoDireccionDepartamentalValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mcb SgMovimientoDireccionDepartamental
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMovimientoDireccionDepartamental mcb) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mcb == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (mcb.getMddCuentaFK() == null) {
                ge.addError("mcbCuentaFK", Errores.ERROR_MCB_CUENTA_BANCARIA);
            }
            if (mcb.getMddFecha() == null) {
                ge.addError("mcbFecha", Errores.ERROR_MCB_FECHA);
            }
            if (!StringUtils.isBlank(mcb.getMddDetalle()) && mcb.getMddDetalle().length() > 255) {
                ge.addError("mcbDetalle", Errores.ERROR_MCB_DETALLE_255);
            }
            if (mcb.getMddMonto() == null) {
                ge.addError("mcbMonto", Errores.ERROR_MCB_MONTO);
            }
            if (mcb.getMddMonto() == BigDecimal.ZERO) {
                ge.addError("mddMonto", Errores.ERROR_MONTO_CERO);
            }
            if (mcb.getMddTipoMovimiento() == null) {
                ge.addError("mcbTipoMovimiento", Errores.ERROR_MCB_TIPO_MOVIMIENTO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
