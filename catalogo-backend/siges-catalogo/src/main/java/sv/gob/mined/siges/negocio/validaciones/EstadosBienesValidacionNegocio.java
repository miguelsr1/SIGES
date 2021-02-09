/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfEstadosBienes;
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */
public class EstadosBienesValidacionNegocio {
     /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgAfEstadosBienes
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfEstadosBienes etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(etn.getEbiCodigo())){
                ge.addError("ebiCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getEbiCodigo().length() > 15){
                ge.addError("ebiCodigo", Errores.ERROR_LARGO_CODIGO_15);
            }
            
            if (StringUtils.isBlank(etn.getEbiNombre())){
                ge.addError("ebiNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getEbiNombre().length() > 255){
                ge.addError("ebiNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgAfEstadosBienes
     * @param c2 SgAfEstadosBienes
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAfEstadosBienes c1, SgAfEstadosBienes c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEbiCodigo(), c2.getEbiCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEbiNombre(), c2.getEbiNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEbiHabilitado(), c2.getEbiHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }  
}
