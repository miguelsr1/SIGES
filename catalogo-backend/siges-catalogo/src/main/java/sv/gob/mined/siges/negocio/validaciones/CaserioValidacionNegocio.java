/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCaserio; 
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

public class CaserioValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param cas
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCaserio cas) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cas==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(cas.getCasCodigo())){
                ge.addError("casCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (cas.getCasCodigo().length() > 8){
                ge.addError("casCodigo", Errores.ERROR_LARGO_CODIGO_8);
            }
            
            if (StringUtils.isBlank(cas.getCasNombre())){
                ge.addError("casNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (cas.getCasNombre().length() > 255){
                ge.addError("casNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if (cas.getCasCanton()== null){
                ge.addError("casCanton", Errores.ERROR_CANTON_VACIO);
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
     * @param c1 SgCaserio
     * @param c2 SgCaserio
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgCaserio c1, SgCaserio c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getCasCodigo(), c2.getCasCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getCasNombre(), c2.getCasNombre());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getCasCanton(), c2.getCasCanton());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCasHabilitado(), c2.getCasHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
