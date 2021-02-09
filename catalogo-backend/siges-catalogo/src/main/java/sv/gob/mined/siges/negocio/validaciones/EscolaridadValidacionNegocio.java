/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEscolaridad; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class EscolaridadValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param esc SgEscolaridad
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEscolaridad esc) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (esc==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(esc.getEscCodigo())){
                ge.addError("escCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (esc.getEscCodigo().length() > 6){
                ge.addError("escCodigo", Errores.ERROR_LARGO_CODIGO_6);
            }
            
            if (StringUtils.isBlank(esc.getEscNombre())){
                ge.addError("escNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (esc.getEscNombre().length() > 255){
                ge.addError("escNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgEscolaridad
     * @param c2 SgEscolaridad
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgEscolaridad c1, SgEscolaridad c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEscCodigo(), c2.getEscCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEscNombre(), c2.getEscNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEscHabilitado(), c2.getEscHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
