/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoTelefono; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class TipoTelefonoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tto SgTipoTelefono
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoTelefono tto) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tto==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(tto.getTtoCodigo())){
                ge.addError("ttoCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tto.getTtoCodigo().length() > 4){
                ge.addError("ttoCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(tto.getTtoNombre())){
                ge.addError("ttoNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tto.getTtoNombre().length() > 255){
                ge.addError("ttoNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoTelefono
     * @param c2 SgTipoTelefono
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoTelefono c1, SgTipoTelefono c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTtoCodigo(), c2.getTtoCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTtoNombre(), c2.getTtoNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTtoHabilitado(), c2.getTtoHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
