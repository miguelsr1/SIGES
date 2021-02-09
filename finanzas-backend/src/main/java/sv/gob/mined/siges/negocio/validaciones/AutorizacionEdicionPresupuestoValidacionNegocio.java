/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAutorizacionEdicionPresupuesto;

/**
 * Validación de las reglas de negocio de autorización de presupuesto
 * @author jgiron
 */
public class AutorizacionEdicionPresupuestoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param pro SgAutorizacionEdicionPresupuesto
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAutorizacionEdicionPresupuesto pro) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pro == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
