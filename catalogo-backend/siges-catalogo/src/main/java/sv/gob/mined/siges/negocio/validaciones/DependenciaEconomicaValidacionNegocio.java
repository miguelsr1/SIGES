/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDependenciaEconomica; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class DependenciaEconomicaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param dec SgDependenciaEconomica
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDependenciaEconomica dec) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (dec==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(dec.getDecCodigo())){
                ge.addError("decCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (dec.getDecCodigo().length() > 4){
                ge.addError("decCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(dec.getDecNombre())){
                ge.addError("decNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (dec.getDecNombre().length() > 255){
                ge.addError("decNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgDependenciaEconomica
     * @param c2 SgDependenciaEconomica
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgDependenciaEconomica c1, SgDependenciaEconomica c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getDecCodigo(), c2.getDecCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getDecNombre(), c2.getDecNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getDecHabilitado(), c2.getDecHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
