/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCategoriaEscalafon; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class CategoriaEscalafonValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param ces SgCategoriaEscalafon
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCategoriaEscalafon ces) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ces==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(ces.getCesCodigo())) {
                ge.addError("cesCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ces.getCesCodigo().length() > 45){
                ge.addError("cesCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(ces.getCesNombre())) {
                ge.addError("cesNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ces.getCesNombre().length() > 255){
                ge.addError("cesNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgCategoriaEscalafon
     * @param c2 SgCategoriaEscalafon
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgCategoriaEscalafon c1, SgCategoriaEscalafon c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getCesCodigo(), c2.getCesCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getCesNombre(), c2.getCesNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCesHabilitado(), c2.getCesHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
