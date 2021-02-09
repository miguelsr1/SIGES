/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRecibos;

/**
 * Validación de las reglas de negocio de los recibos para las transferencias.
 *
 * @author Sofis Solutions
 */
public class RecibosValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rec SgRecibos
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRecibos rec) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rec == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (rec.getRecFecha() == null) {
                ge.addError("recFecha", Errores.ERROR_MCB_FECHA);
            }
            if (rec.getRecTransferencia() == null) {
                ge.addError("recTransferencia", Errores.ERROR_TRANSFERENCIA_CED);
            }
            if (rec.getRecMonto() == null) {
                ge.addError("recMonto", Errores.ERROR_MCB_MONTO);
            }
            if (rec.getRecArchivo() == null) {
                ge.addError("recArchivo", Errores.ERROR_ARCHIVO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
