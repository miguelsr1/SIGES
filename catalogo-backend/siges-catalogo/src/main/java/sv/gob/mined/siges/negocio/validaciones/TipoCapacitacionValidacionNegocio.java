/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoCapacitacion; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class TipoCapacitacionValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tca SgTipoCapacitacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoCapacitacion tca) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tca==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(tca.getTcaCodigo())){
                ge.addError("tcaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tca.getTcaCodigo().length() > 45){
                ge.addError("tcaCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(tca.getTcaNombre())){
                ge.addError("tcaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tca.getTcaNombre().length() > 255){
                ge.addError("tcaNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoCapacitacion
     * @param c2 SgTipoCapacitacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoCapacitacion c1, SgTipoCapacitacion c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTcaCodigo(), c2.getTcaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTcaNombre(), c2.getTcaNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTcaHabilitado(), c2.getTcaHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}

