/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfp; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class AfpValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param afp SgAfp
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfp afp) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (afp==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(afp.getAfpCodigo())) {
                ge.addError("afpCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (afp.getAfpCodigo().length() > 45){
                ge.addError("afpCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(afp.getAfpNombre())) {
                ge.addError("afpNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (afp.getAfpNombre().length() > 255){
                ge.addError("afpNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgAfp
     * @param c2 SgAfp
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAfp c1, SgAfp c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getAfpCodigo(), c2.getAfpCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getAfpNombre(), c2.getAfpNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getAfpHabilitado(), c2.getAfpHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
