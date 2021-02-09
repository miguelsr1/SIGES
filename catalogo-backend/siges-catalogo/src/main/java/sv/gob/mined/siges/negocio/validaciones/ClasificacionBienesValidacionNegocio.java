/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfClasificacionBienes;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author bruno
 */
public class ClasificacionBienesValidacionNegocio {
     /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgAfClasificacionBienes
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfClasificacionBienes etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(etn.getCbiCodigo())){
                ge.addError("cbiCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getCbiCodigo().length() > 4){
                ge.addError("cbiCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(etn.getCbiNombre())){
                ge.addError("cbiNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getCbiNombre().length() > 255){
                ge.addError("cbiNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgAfClasificacionBienes
     * @param c2 SgAfClasificacionBienes
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAfClasificacionBienes c1, SgAfClasificacionBienes c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getCbiCodigo(), c2.getCbiCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getCbiNombre(), c2.getCbiNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCbiHabilitado(), c2.getCbiHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
    
}
