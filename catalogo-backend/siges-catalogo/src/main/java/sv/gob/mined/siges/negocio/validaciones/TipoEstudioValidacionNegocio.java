/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoEstudio; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class TipoEstudioValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tes SgTipoEstudio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoEstudio tes) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tes==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tes.getTesCodigo())) {
                ge.addError("tesCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tes.getTesCodigo().length() > 45){
                ge.addError("tesCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tes.getTesNombre())) {
                ge.addError("tesNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tes.getTesNombre().length() > 255){
                ge.addError("tesNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoEstudio
     * @param c2 SgTipoEstudio
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoEstudio c1, SgTipoEstudio c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTesCodigo(), c2.getTesCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTesNombre(), c2.getTesNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTesHabilitado(), c2.getTesHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
