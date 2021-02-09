/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgJornadaLaboral; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class JornadaLaboralValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param jla SgJornadaLaboral
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgJornadaLaboral jla) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (jla==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(jla.getJlaCodigo())){
                ge.addError("jlaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (jla.getJlaCodigo().length() > 4){
                ge.addError("jlaCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(jla.getJlaNombre())){
                ge.addError("jlaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (jla.getJlaNombre().length() > 255){
                ge.addError("jlaNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgJornadaLaboral
     * @param c2 SgJornadaLaboral
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgJornadaLaboral c1, SgJornadaLaboral c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getJlaCodigo(), c2.getJlaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getJlaNombre(), c2.getJlaNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getJlaHabilitado(), c2.getJlaHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
    
}
