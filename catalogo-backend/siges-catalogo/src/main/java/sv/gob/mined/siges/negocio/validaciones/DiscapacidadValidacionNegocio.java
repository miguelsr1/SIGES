/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDiscapacidad; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class DiscapacidadValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param dis SgDiscapacidad
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDiscapacidad dis) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (dis==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(dis.getDisCodigo())){
                ge.addError("disCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (dis.getDisCodigo().length() > 4){
                ge.addError("disCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(dis.getDisNombre())){
                ge.addError("disNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (dis.getDisNombre().length() > 255){
                ge.addError("disNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgDiscapacidad
     * @param c2 SgDiscapacidad
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgDiscapacidad c1, SgDiscapacidad c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getDisCodigo(), c2.getDisCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getDisNombre(), c2.getDisNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getDisHabilitado(), c2.getDisHabilitado());
                respuesta = respuesta && StringUtils.equals(c1.getDisClasificacion(), c2.getDisClasificacion());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
