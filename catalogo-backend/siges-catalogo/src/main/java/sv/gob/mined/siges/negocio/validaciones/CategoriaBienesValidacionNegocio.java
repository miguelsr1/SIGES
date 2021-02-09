/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfCategoriaBienes;

public class CategoriaBienesValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param  dep SgDepartamento
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfCategoriaBienes  cat) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if ( cat==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(cat.getCabCodigo())){
                ge.addError("cabCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (cat.getCabCodigo().length() > 4){
                ge.addError("cabCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(cat.getCabNombre())){
                ge.addError("cabNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (cat.getCabNombre().length() > 255){
                ge.addError("cabNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(cat.getCabClasificacionBienes() == null) {
               ge.addError("cabClasificacionBienes", Errores.ERROR_CLASIFICACION_BIENES_VACIO); 
            }
            if(cat.getCabValorMaximo() == null) {
               ge.addError("cabValorMaximo", Errores.ERROR_VALOR_MAXIMO_VACIO); 
            }
            if(cat.getCabVidaUtil() == null) {
               ge.addError("cabVidaUtil", Errores.ERROR_VIDA_UTIL_VACIO); 
            }
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }   
}
