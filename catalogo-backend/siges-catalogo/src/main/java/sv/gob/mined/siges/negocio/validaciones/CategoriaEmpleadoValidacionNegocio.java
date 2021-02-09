/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCategoriaEmpleado; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class CategoriaEmpleadoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgCategoriaEmpleado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCategoriaEmpleado etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(etn.getCemCodigo())){
                ge.addError("cemCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getCemCodigo().length() > 45){
                ge.addError("cemCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(etn.getCemNombre())){
                ge.addError("cemNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getCemNombre().length() > 255){
                ge.addError("cemNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgCategoriaEmpleado
     * @param c2 SgCategoriaEmpleado
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgCategoriaEmpleado c1, SgCategoriaEmpleado c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getCemCodigo(), c2.getCemCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getCemNombre(), c2.getCemNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCemHabilitado(), c2.getCemHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
    
}
