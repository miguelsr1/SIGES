/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoSangre; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class TipoSangreValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tsa SgTipoSangre
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoSangre tsa) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tsa==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(tsa.getTsaCodigo())){
                ge.addError("tsaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tsa.getTsaCodigo().length() > 4){
                ge.addError("tsaCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(tsa.getTsaNombre())){
                ge.addError("tsaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tsa.getTsaNombre().length() > 20){
                ge.addError("tsaNombre", Errores.ERROR_LARGO_NOMBRE_20);
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
     * @param c1 SgTipoSangre
     * @param c2 SgTipoSangre
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoSangre c1, SgTipoSangre c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTsaCodigo(), c2.getTsaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTsaNombre(), c2.getTsaNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTsaHabilitado(), c2.getTsaHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}


