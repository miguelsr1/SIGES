/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgModoPago; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class ModoPagoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param mpa SgModoPago
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgModoPago mpa) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mpa==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(mpa.getMpaCodigo())){
                ge.addError("mpaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mpa.getMpaCodigo().length() > 45){
                ge.addError("mpaCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            
            if (StringUtils.isBlank(mpa.getMpaNombre())){
                ge.addError("mpaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mpa.getMpaNombre().length() > 255){
                ge.addError("mpaNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgModoPago
     * @param c2 SgModoPago
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgModoPago c1, SgModoPago c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMpaCodigo(), c2.getMpaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMpaNombre(), c2.getMpaNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMpaHabilitado(), c2.getMpaHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
    
}
