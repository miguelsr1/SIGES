/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.math.BigDecimal;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgConciliacionesBancarias;

/**
 * Validación de las reglas de negocio de conciliaciones bancarias
 * @author jgiron
 */
public class ConciliacionBancariaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param con SgConciliacionesBancarias
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgConciliacionesBancarias con) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (con == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (con.getConCuentaFk() == null) {
                ge.addError("conCuentaFk", Errores.ERROR_MCB_CUENTA_BANCARIA);
            }
            if (con.getConFechaDesde() == null) {
                ge.addError("conFechaDesde", Errores.ERROR_FECHA_DESDE_VACIO);
            }
            if (con.getConFechaHasta() == null) {
                ge.addError("conFechaHasta", Errores.ERROR_FECHA_HASTA_VACIO);
            }
            if (con.getConMonto() == BigDecimal.ZERO) {
                ge.addError("conMonto", Errores.ERROR_MCB_MONTO);
            }
            if (con.getConMonto() == null) {
                ge.addError("conMonto", Errores.ERROR_MONTO_VACIO);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
