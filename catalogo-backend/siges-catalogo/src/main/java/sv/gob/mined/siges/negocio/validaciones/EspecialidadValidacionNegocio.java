/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEspecialidad; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class EspecialidadValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param esp SgEspecialidad
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEspecialidad esp) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (esp==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(esp.getEspCodigo())) {
                ge.addError("espCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (esp.getEspCodigo().length() > 45){
                ge.addError("espCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(esp.getEspNombre())) {
                ge.addError("espNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (esp.getEspNombre().length() > 255){
                ge.addError("espNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgEspecialidad
     * @param c2 SgEspecialidad
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgEspecialidad c1, SgEspecialidad c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEspCodigo(), c2.getEspCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEspNombre(), c2.getEspNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEspHabilitado(), c2.getEspHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
