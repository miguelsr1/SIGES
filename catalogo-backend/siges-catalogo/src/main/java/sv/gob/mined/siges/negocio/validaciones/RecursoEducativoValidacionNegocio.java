/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRecursoEducativo; 
import sv.gob.mined.siges.utils.BooleanUtils;


public class RecursoEducativoValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param red SgRecursoEducativo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRecursoEducativo red) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (red==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(red.getRedCodigo())){
                ge.addError("redCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (red.getRedCodigo().length() > 4){
                ge.addError("redCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(red.getRedNombre())){
                ge.addError("redNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (red.getRedNombre().length() > 255){
                ge.addError("redNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgRecursoEducativo
     * @param c2 SgRecursoEducativo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgRecursoEducativo c1, SgRecursoEducativo c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getRedCodigo(), c2.getRedCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getRedNombre(), c2.getRedNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getRedHabilitado(), c2.getRedHabilitado());       
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
