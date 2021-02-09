/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMensaje; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class MensajeValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param msj SgMensaje
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMensaje msj) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (msj==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(msj.getMsjCodigo())){
                ge.addError("msjCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (msj.getMsjCodigo().length() > 4){
                ge.addError("msjCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (!StringUtils.isBlank(msj.getMsjDescripcion()) && msj.getMsjDescripcion().length() > 255){
                ge.addError("msjDescripcion", Errores.ERROR_LARGO_DESCRIPCION_255);
            }
            
            if (StringUtils.isBlank(msj.getMsjValor())){
                ge.addError("msjValor", Errores.ERROR_VALOR_VACIO);
            } else if (msj.getMsjValor().length() > 500){
                ge.addError("msjValor", Errores.ERROR_LARGO_VALOR_500);
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
     * @param c1 SgMensaje
     * @param c2 SgMensaje
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMensaje c1, SgMensaje c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMsjCodigo(), c2.getMsjCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMsjDescripcion(), c2.getMsjDescripcion());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMsjHabilitado(), c2.getMsjHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
    
}
