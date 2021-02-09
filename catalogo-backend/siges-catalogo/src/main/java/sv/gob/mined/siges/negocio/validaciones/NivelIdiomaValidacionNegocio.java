/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgNivelIdioma; 
import sv.gob.mined.siges.utils.BooleanUtils;


public class NivelIdiomaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param nid SgNivelIdioma
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgNivelIdioma nid) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (nid==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(nid.getNidCodigo())){
                ge.addError("nidCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (nid.getNidCodigo().length() > 45){
                ge.addError("nidCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(nid.getNidNombre())){
                ge.addError("nidNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (nid.getNidNombre().length() > 255){
                ge.addError("nidNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgNivelIdioma
     * @param c2 SgNivelIdioma
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgNivelIdioma c1, SgNivelIdioma c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getNidCodigo(), c2.getNidCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getNidNombre(), c2.getNidNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getNidHabilitado(), c2.getNidHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
