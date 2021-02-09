/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgInstitucion; 
import sv.gob.mined.siges.utils.BooleanUtils;


public class InstitucionValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param ins SgInstitucion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgInstitucion ins) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ins==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(ins.getInsCodigo())){
                ge.addError("insCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ins.getInsCodigo().length() > 45){
                ge.addError("insCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(ins.getInsNombre())){
                ge.addError("insNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ins.getInsNombre().length() > 255){
                ge.addError("insNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgInstitucion
     * @param c2 SgInstitucion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgInstitucion c1, SgInstitucion c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getInsCodigo(), c2.getInsCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getInsNombre(), c2.getInsNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getInsHabilitado(), c2.getInsHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
