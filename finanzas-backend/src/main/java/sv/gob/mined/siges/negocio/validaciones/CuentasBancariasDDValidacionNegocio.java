/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCuentasBancariasDD;

/**
 * Validación de las cuentas bancarias de las direcciones departamentales
 *
 * @author Sofis Solutions
 */
public class CuentasBancariasDDValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cbd SgCuentasBancariasDD
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCuentasBancariasDD cbd) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cbd == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(cbd.getCbdNumeroCuenta())) {
                ge.addError("cbdNumeroCuenta", Errores.ERROR_NUMERO_CUENTA_VACIO);
            }
            if (StringUtils.isBlank(cbd.getCbdTitular())) {
                ge.addError("cbdTitular", Errores.ERROR_TITULAR_VACIO);
            } else if (cbd.getCbdTitular().length() > 250) {
                ge.addError("cbdTitular", Errores.ERROR_TITULAR_250);
            }
            if (cbd.getCbdBancoFk() == null) {
                ge.addError("cbdBancoFk", Errores.ERROR_BANCO_VACIO);
            }
            if (!StringUtils.isBlank(cbd.getCbdComentario()) && cbd.getCbdComentario().length() > 4000) {
                ge.addError("cbdComentario", Errores.ERROR_CUENTA_COMENTARIO_4000);
            }
            if (cbd.getCbdDirDepFk() == null) {
                ge.addError("cbdDirDepFk", Errores.ERROR_DIRECCION_DEP_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
