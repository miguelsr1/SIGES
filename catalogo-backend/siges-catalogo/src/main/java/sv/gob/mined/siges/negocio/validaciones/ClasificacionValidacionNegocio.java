/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgClasificacion; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class ClasificacionValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param  cla SgClasificacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgClasificacion cla) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if ( cla==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank( cla.getClaCodigo())){
                ge.addError(Errores.ERROR_CODIGO_VACIO);
            } else if ( cla.getClaCodigo().length() > 4){
                ge.addError(Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank( cla.getClaNombre())){
                ge.addError(Errores.ERROR_NOMBRE_VACIO);
            } else if ( cla.getClaNombre().length() > 255){
                ge.addError(Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Compara dos elementos del tipo. 
     * Indica si los elementos contienen la misma información.
     * 
     * @param c1 SgClasificacion
     * @param c2 SgClasificacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgClasificacion c1, SgClasificacion c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getClaCodigo(), c2.getClaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getClaNombre(), c2.getClaNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getClaHabilitado(), c2.getClaHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
