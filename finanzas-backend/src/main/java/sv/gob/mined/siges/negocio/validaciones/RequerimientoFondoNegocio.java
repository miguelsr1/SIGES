/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRequerimientoFondo;

/**
 * Validación de las reglas de negocio de las solicitudes de transferencia
 *
 * @author jgiron
 */
public class RequerimientoFondoNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param sol SgPresupuestoEscolar
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRequerimientoFondo sol) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (sol == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if(sol.getStrCuentaBancDdFk()==null){
                ge.addError("strCuentaBancDdFk", Errores.ERROR_MCB_CUENTA_BANCARIA);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
