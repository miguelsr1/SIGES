/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgNivelEscalafon; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class NivelEscalafonValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param nes SgNivelEscalafon
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgNivelEscalafon nes) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (nes==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(nes.getNesCodigo())) {
                ge.addError("nesCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (nes.getNesCodigo().length() > 45){
                ge.addError("nesCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(nes.getNesNombre())) {
                ge.addError("nesNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (nes.getNesNombre().length() > 255){
                ge.addError("nesNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgNivelEscalafon
     * @param c2 SgNivelEscalafon
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgNivelEscalafon c1, SgNivelEscalafon c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getNesCodigo(), c2.getNesCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getNesNombre(), c2.getNesNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getNesHabilitado(), c2.getNesHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
