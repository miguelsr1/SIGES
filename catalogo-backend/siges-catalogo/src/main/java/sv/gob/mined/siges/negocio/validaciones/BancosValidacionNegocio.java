/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgBancos;

/**
 *
 * @author Sofis Solutions
 */
public class BancosValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param bnc SgAreaIndicador
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgBancos bnc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (bnc==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(bnc.getBncCodigo())) {
                ge.addError("bncCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (bnc.getBncCodigo().length() > 45){
                ge.addError("bncCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(bnc.getBncNombre())) {
                ge.addError("bncNombre",Errores.ERROR_NOMBRE_VACIO);
            } else if (bnc.getBncNombre().length() > 255){
                ge.addError("bncNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if (!StringUtils.isBlank(bnc.getBncTelefono()) && bnc.getBncTelefono().length() > 20){
                ge.addError("bncTelefono", Errores.ERROR_TELEFONO_20);
            }
            if (!StringUtils.isBlank(bnc.getBncCorreoElectronico()) && bnc.getBncCorreoElectronico().length() > 50){
                ge.addError("bncCorreoElectronico", Errores.ERROR_LARGO_CORREO_50);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
