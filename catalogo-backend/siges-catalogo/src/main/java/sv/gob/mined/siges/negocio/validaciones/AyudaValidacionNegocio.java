/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAyuda; 
import sv.gob.mined.siges.utils.BooleanUtils;


/**
 *
 * @author Sofis Solutions
 */  
public class AyudaValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param ayu SgAyuda
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAyuda ayu) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ayu==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(ayu.getAyuCodigo())) {
                ge.addError("ayuCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ayu.getAyuCodigo().length() > 45){
                ge.addError("ayuCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(ayu.getAyuNombre())) {
                ge.addError("ayuNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ayu.getAyuNombre().length() > 255){
                ge.addError("ayuNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Compara dos elementos del tipo. 
     * Indica si los elementos contienen la misma información.
     * 
     * @param c1 SgAyuda
     * @param c2 SgAyuda
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAyuda c1, SgAyuda c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getAyuCodigo(), c2.getAyuCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getAyuNombre(), c2.getAyuNombre());
                respuesta = respuesta && StringUtils.equals(c1.getAyuTextoNormativa(), c2.getAyuTextoNormativa());
                respuesta = respuesta && StringUtils.equals(c1.getAyuVinculos(), c2.getAyuVinculos());
                respuesta = respuesta && StringUtils.equals(c1.getAyuTextoUso(), c2.getAyuTextoUso());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getAyuHabilitado(), c2.getAyuHabilitado());    
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getAyuResaltada(), c2.getAyuResaltada()); 
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
