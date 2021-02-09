/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoIdentificacion; 
import sv.gob.mined.siges.utils.BooleanUtils;


public class TipoIdentificacionValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tin SgTipoIdentificacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoIdentificacion tin) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tin==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(tin.getTinCodigo())){
                ge.addError("tinCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tin.getTinCodigo().length() > 4){
                ge.addError("tinCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(tin.getTinNombre())){
                ge.addError("tinNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tin.getTinNombre().length() > 255){
                ge.addError("tinNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoIdentificacion
     * @param c2 SgTipoIdentificacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoIdentificacion c1, SgTipoIdentificacion c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTinCodigo(), c2.getTinCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTinNombre(), c2.getTinNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTinHabilitado(), c2.getTinHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
