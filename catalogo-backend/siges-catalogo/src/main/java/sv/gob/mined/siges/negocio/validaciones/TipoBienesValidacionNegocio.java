/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfTipoBienes;

public class TipoBienesValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param  dep SgAfTipoBienes
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfTipoBienes  cat) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if ( cat==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(cat.getTbiCodigo())){
                ge.addError("tbiCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (cat.getTbiCodigo().length() > 4){
                ge.addError("tbiCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(cat.getTbiNombre())){
                ge.addError("tbiNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (cat.getTbiNombre().length() > 255){
                ge.addError("tbiNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(cat.getTbiCategoriaBienes()== null) {
               ge.addError("tbiCategoriaBienes", Errores.ERROR_CATEGORIA_BIENES_VACIO); 
            }
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }   
}