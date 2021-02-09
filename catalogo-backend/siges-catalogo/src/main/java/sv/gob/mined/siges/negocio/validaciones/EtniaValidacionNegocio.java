/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEtnia; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class EtniaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgEtnia
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEtnia etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(etn.getEtnCodigo())){
                ge.addError("etnCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getEtnCodigo().length() > 4){
                ge.addError("etnCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(etn.getEtnNombre())){
                ge.addError("etnNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getEtnNombre().length() > 255){
                ge.addError("etnNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgEtnia
     * @param c2 SgEtnia
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgEtnia c1, SgEtnia c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEtnCodigo(), c2.getEtnCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEtnNombre(), c2.getEtnNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEtnHabilitado(), c2.getEtnHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
    
}
