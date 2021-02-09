/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCuentasBancarias;

/**
 * Validación de las reglas de negocio de cuentas bancarias (genérico)
 *
 * @author Sofis Solutions
 */
public class CuentasBancariasValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cbc SgCuentasBancarias
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCuentasBancarias cbc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cbc == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(cbc.getCbcNumeroCuenta())) {
                ge.addError("cbcNumeroCuenta", Errores.ERROR_NUMERO_CUENTA_VACIO);
            }
            if (StringUtils.isBlank(cbc.getCbcTitular())) {
                ge.addError("cbcTitular", Errores.ERROR_TITULAR_VACIO);
            } else if (cbc.getCbcTitular().length() > 250) {
                ge.addError("cbcTitular", Errores.ERROR_TITULAR_250);
            }
            if (cbc.getCbcBancoFk() == null) {
                ge.addError("cbcBancoFk", Errores.ERROR_BANCO_VACIO);
            }
            if (!StringUtils.isBlank(cbc.getCbcComentario()) && cbc.getCbcComentario().length() > 4000) {
                ge.addError("cbcComentario", Errores.ERROR_CUENTA_COMENTARIO_4000);
            }
            if (cbc.getCbcSedeFk() == null) {
                ge.addError("cbcSede", Errores.ERROR_SEDE_VACIO);
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
