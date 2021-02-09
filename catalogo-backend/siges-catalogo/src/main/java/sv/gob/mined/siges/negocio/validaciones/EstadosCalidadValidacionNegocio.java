/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfEstadosCalidad;
import sv.gob.mined.siges.utils.BooleanUtils;


public class EstadosCalidadValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgAfEstadosCalidad
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfEstadosCalidad etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(etn.getEcaCodigo())){
                ge.addError("ecaCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getEcaCodigo().length() > 4){
                ge.addError("ecaCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(etn.getEcaNombre())){
                ge.addError("ecaNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getEcaNombre().length() > 255){
                ge.addError("ecaNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgFormaAdquisicion
     * @param c2 SgFormaAdquisicion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAfEstadosCalidad c1, SgAfEstadosCalidad c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEcaCodigo(), c2.getEcaCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEcaNombre(), c2.getEcaNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEcaHabilitado(), c2.getEcaHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }  
}
