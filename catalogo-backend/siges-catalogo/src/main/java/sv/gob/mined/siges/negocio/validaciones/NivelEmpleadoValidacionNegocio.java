/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgNivelEmpleado; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class NivelEmpleadoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgNivelEmpleado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgNivelEmpleado etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(etn.getNemCodigo())){
                ge.addError("nemCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getNemCodigo().length() > 45){
                ge.addError("nemCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(etn.getNemNombre())){
                ge.addError("nemNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getNemNombre().length() > 255){
                ge.addError("nemNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgNivelEmpleado
     * @param c2 SgNivelEmpleado
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgNivelEmpleado c1, SgNivelEmpleado c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getNemCodigo(), c2.getNemCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getNemNombre(), c2.getNemNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getNemHabilitado(), c2.getNemHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
    
}
