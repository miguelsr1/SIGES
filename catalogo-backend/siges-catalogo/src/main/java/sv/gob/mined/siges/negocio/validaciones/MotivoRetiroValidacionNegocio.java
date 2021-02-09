/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoRetiro; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class MotivoRetiroValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param mre SgMotivoRetiro
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMotivoRetiro mre) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mre==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(mre.getMreCodigo())){
                ge.addError("mreCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mre.getMreCodigo().length() > 4){
                ge.addError("mreCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(mre.getMreNombre())){
                ge.addError("mreNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mre.getMreNombre().length() > 255){
                ge.addError("mreNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgMotivoRetiro
     * @param c2 SgMotivoRetiro
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMotivoRetiro c1, SgMotivoRetiro c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMreCodigo(), c2.getMreCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMreNombre(), c2.getMreNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMreDefinitivo(), c2.getMreDefinitivo());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMreTraslado(), c2.getMreTraslado());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMreCambioSecc(), c2.getMreCambioSecc());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMreHabilitado(), c2.getMreHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
