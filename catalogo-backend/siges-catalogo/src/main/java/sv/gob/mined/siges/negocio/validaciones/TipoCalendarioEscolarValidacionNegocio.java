/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoCalendarioEscolar; 
import sv.gob.mined.siges.utils.BooleanUtils;


public class TipoCalendarioEscolarValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tce SgTipoCalendarioEscolar
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoCalendarioEscolar tce) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tce==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(tce.getTceCodigo())){
                ge.addError("tceCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tce.getTceCodigo().length() > 4){
                ge.addError("tceCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(tce.getTceNombre())){
                ge.addError("tceNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tce.getTceNombre().length() > 255){
                ge.addError("tceNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoCalendarioEscolar
     * @param c2 SgTipoCalendarioEscolar
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoCalendarioEscolar c1, SgTipoCalendarioEscolar c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTceCodigo(), c2.getTceCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTceNombre(), c2.getTceNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTceHabilitado(), c2.getTceHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
