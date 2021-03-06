/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfEstadosTraslado;
import sv.gob.mined.siges.utils.BooleanUtils;


public class SgEstadosTrasladoValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgAfEstadosTraslado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
        public static boolean validar(SgAfEstadosTraslado etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(etn.getEtrCodigo())){
                ge.addError("etrCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getEtrCodigo().length() > 4){
                ge.addError("etrCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(etn.getEtrNombre())){
                ge.addError("etrNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getEtrNombre().length() > 255){
                ge.addError("etrNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgAfEstadosTraslado
     * @param c2 SgAfEstadosTraslado
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAfEstadosTraslado c1, SgAfEstadosTraslado c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEtrCodigo(), c2.getEtrCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEtrNombre(), c2.getEtrNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEtrHabilitado(), c2.getEtrHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }  
}
