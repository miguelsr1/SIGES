/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfEstadosDescargo;
import sv.gob.mined.siges.utils.BooleanUtils;

public class EstadosDescargoValidacionNegocio {
     /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgAfEstadosDescargo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfEstadosDescargo etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(etn.getEdeCodigo())){
                ge.addError("edeCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getEdeCodigo().length() > 4){
                ge.addError("edeCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(etn.getEdeNombre())){
                ge.addError("edeNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getEdeNombre().length() > 255){
                ge.addError("edeNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgAfEstadosDescargo
     * @param c2 SgAfEstadosDescargo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAfEstadosDescargo c1, SgAfEstadosDescargo c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEdeCodigo(), c2.getEdeCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEdeNombre(), c2.getEdeNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEdeHabilitado(), c2.getEdeHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }  
}
