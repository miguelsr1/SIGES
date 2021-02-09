/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoAsociacion; 
import sv.gob.mined.siges.utils.BooleanUtils;


public class TipoAsociacionValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tas SgTipoAsociacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoAsociacion tas) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tas==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(tas.getTasCodigo())){
                ge.addError("tasCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tas.getTasCodigo().length() > 4){
                ge.addError("tasCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(tas.getTasNombre())){
                ge.addError("tasNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tas.getTasNombre().length() > 100){
                ge.addError("tasNombre", Errores.ERROR_LARGO_NOMBRE_100);
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
     * @param c1 SgTipoAsociacion
     * @param c2 SgTipoAsociacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoAsociacion c1, SgTipoAsociacion c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTasCodigo(), c2.getTasCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTasNombre(), c2.getTasNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTasHabilitado(), c2.getTasHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
