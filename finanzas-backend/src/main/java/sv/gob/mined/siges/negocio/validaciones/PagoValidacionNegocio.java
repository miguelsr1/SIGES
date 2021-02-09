/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.util.logging.Logger;
import sv.gob.mined.siges.enumerados.EnumModoPago;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPago;

/**
 * Validación de las reglas de negocio de pago
 * @author Sofis Solutions
 */
public class PagoValidacionNegocio {

    private static final Logger LOGGER = Logger.getLogger(PagoValidacionNegocio.class.getName());

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param pgs SgPago
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPago pgs) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pgs == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (pgs.getPgsModoPago() == null) {
                ge.addError("pgsModoPago", Errores.EROR_MODO_PAGO_VACIO);
            }
            if (pgs.getPgsFactura() == null) {
                ge.addError("pgsFactura", Errores.ERROR_FACTURA_VACIA);
            }
            if (pgs.getPgsModoPago() != null && pgs.getPgsModoPago().equals(EnumModoPago.CHEQUE) && pgs.getPgsCuentaBancaria() == null) {
                ge.addError("pgsCuentaBancaria", Errores.ERROR_MCB_CUENTA_BANCARIA);
            }
            if (pgs.getPgsModoPago() != null && pgs.getPgsModoPago().equals(EnumModoPago.EFECTIVO) && pgs.getPgsCuentaBancariaCC() == null) {
                ge.addError("pgsCuentaBancariaCC", Errores.ERROR_MCC_CAJA_CHICA);
            }
            if (pgs.getPgsModoPago() != null && pgs.getPgsModoPago().equals(EnumModoPago.CHEQUE) && pgs.getPgsNumeroCheque() == null) {
                ge.addError("pgsNumeroCheque", Errores.ERROR_NUMERO_CHEQUE);
            }
            if (pgs.getPgsFechaPago() == null) {
                ge.addError("pgsFechaPago", Errores.ERROR_FECHA_PAGO);
            }
            if (pgs.getPgsImporte() == null) {
                ge.addError("pgsImporte", Errores.ERROR_MCB_MONTO);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Compara dos elementos del tipo. Indica si los elementos contienen la
     * misma información.
     *
     * @param c1 SgPago Anterior
     * @param c2 SgPago Actual
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgPago c1, SgPago c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {
                respuesta = c1.getPgsModoPago().equals(c2.getPgsModoPago());
                if (c1.getPgsModoPago().equals(EnumModoPago.CHEQUE)) {
                    respuesta = respuesta && (c1.getPgsMovimientoCBFk() != null && c2.getPgsCuentaBancaria() != null) && c1.getPgsMovimientoCBFk().getMcbCuentaFK().equals(c2.getPgsCuentaBancaria());
                }
                if (c1.getPgsModoPago().equals(EnumModoPago.EFECTIVO)) {
                    respuesta = respuesta && (c1.getPgsMovimientoCCFk() != null && c2.getPgsCuentaBancariaCC() != null) && c1.getPgsMovimientoCCFk().getMccCuentaFK().equals(c2.getPgsCuentaBancariaCC());
                }
                respuesta = respuesta && (c1.getPgsImporte().compareTo(c2.getPgsImporte()) == 0);
                respuesta = respuesta && c1.getPgsFechaPago().equals(c2.getPgsFechaPago());
            }
        } else {
            respuesta = c2 == null;
        }
        return respuesta;
    }

}
