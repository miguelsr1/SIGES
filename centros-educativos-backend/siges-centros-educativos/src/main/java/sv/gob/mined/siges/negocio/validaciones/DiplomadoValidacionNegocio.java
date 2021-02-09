/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomado; 

public class DiplomadoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgDiplomado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDiplomado entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(entity.getDipCodigo())){
                ge.addError("dipCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getDipCodigo().length() > 4){
                ge.addError("dipCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(entity.getDipNombre())){
                ge.addError("dipNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getDipNombre().length() > 255){
                ge.addError("dipNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(StringUtils.isBlank(entity.getDipDescripcion())){
                ge.addError("dipDescripcion", Errores.ERROR_DESCRIPCION_VACIO);
            }else if(entity.getDipDescripcion().length()>255){
                ge.addError("dipDescripcion", Errores.ERROR_LARGO_DESCRIPCION_500);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
