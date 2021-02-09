/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgImpresora;

public class ImpresoraValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgImpresora
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
        public static boolean validar(SgImpresora etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(etn.getImpCodigo())){
                ge.addError("impCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getImpCodigo().length() > 4){
                ge.addError("impCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(etn.getImpNombre())){
                ge.addError("impNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getImpNombre().length() > 255){
                ge.addError("impNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}
