/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDepartamento; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class DepartamentoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param  dep SgDepartamento
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDepartamento  dep) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if ( dep==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(dep.getDepCodigo())){
                ge.addError("depCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (dep.getDepCodigo().length() > 4){
                ge.addError("depCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(dep.getDepNombre())){
                ge.addError("depNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (dep.getDepNombre().length() > 255){
                ge.addError("depNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgDepartamento
     * @param c2 SgDepartamento
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgDepartamento c1, SgDepartamento c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getDepCodigo(), c2.getDepCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getDepNombre(), c2.getDepNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getDepHabilitado(), c2.getDepHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
