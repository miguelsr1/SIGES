/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCategoriaOperacion; 

public class CategoriaOperacionValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgCategoriaOperacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCategoriaOperacion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(entity.getCopCodigo())){
                ge.addError("copCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getCopCodigo().length() > 10){
                ge.addError(Errores.ERROR_LARGO_CODIGO_10);
            }       
            if (StringUtils.isBlank(entity.getCopNombre())){
                ge.addError("copNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getCopNombre().length() > 255){
                ge.addError(Errores.ERROR_LARGO_NOMBRE_255);
            }     
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
