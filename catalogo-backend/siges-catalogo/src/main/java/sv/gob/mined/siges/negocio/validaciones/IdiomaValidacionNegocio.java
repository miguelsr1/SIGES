/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgIdioma; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class IdiomaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param idi SgIdioma
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgIdioma idi) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (idi==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(idi.getIdiCodigo())){
                ge.addError("idiCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (idi.getIdiCodigo().length() > 45){
                ge.addError("idiCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(idi.getIdiNombre())){
                ge.addError("idiNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (idi.getIdiNombre().length() > 255){
                ge.addError("idiNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgIdioma
     * @param c2 SgIdioma
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgIdioma c1, SgIdioma c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getIdiCodigo(), c2.getIdiCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getIdiNombre(), c2.getIdiNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getIdiHabilitado(), c2.getIdiHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}


