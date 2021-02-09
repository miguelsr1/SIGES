/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSistemaIntegrado; 

public class SistemaIntegradoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgSistemaIntegrado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSistemaIntegrado entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if(StringUtils.isBlank(entity.getSinCodigo())){
                ge.addError("sinCodigo", Errores.ERROR_CODIGO_VACIO);
            }else if(entity.getSinCodigo().length() > 15){
                ge.addError("sinCodigo", Errores.ERROR_LARGO_CODIGO_15);
            }
            
            if (StringUtils.isBlank(entity.getSinNombre())){
                ge.addError("sinNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getSinNombre().length() > 100){
                ge.addError("sinNombre", Errores.ERROR_LARGO_NOMBRE_100);
            }       
            if (!StringUtils.isBlank(entity.getSinDescripcion()) && entity.getSinDescripcion().length() > 4000){
                ge.addError("sinDescripcion", Errores.ERROR_LARGO_DESCRIPCION_4000);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
