/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgModalidadEstudio; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class ModalidadEstudioValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param mes SgModalidadEstudio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgModalidadEstudio mes) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mes==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(mes.getMesCodigo())) {
                ge.addError("mesCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mes.getMesCodigo().length() > 45){
                ge.addError("mesCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(mes.getMesNombre())) {
                ge.addError("mesNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mes.getMesNombre().length() > 255){
                ge.addError("mesNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgModalidadEstudio
     * @param c2 SgModalidadEstudio
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgModalidadEstudio c1, SgModalidadEstudio c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMesCodigo(), c2.getMesCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMesNombre(), c2.getMesNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMesHabilitado(), c2.getMesHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}