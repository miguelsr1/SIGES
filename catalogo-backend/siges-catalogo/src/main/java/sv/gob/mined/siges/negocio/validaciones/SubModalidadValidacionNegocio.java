/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSubModalidadAtencion; 
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

public class SubModalidadValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param smo SgSubModalidadAtencion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSubModalidadAtencion smo) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (smo==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(smo.getSmoCodigo())){
                ge.addError("smoCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (smo.getSmoCodigo().length() > 10){
                ge.addError("smoCodigo", Errores.ERROR_LARGO_CODIGO_10);
            }
            
            if (StringUtils.isBlank(smo.getSmoNombre())){
                ge.addError("smoNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (smo.getSmoNombre().length() > 255){
                ge.addError("smoNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if (smo.getSmoModalidadFk()==null){
                ge.addError("smoModalidad", Errores.ERROR_MODALIDAD_ATENCION_VACIO);
            }
            
            if (!StringUtils.isBlank(smo.getSmoDescripcion()) ){
                if(smo.getSmoDescripcion().length()> 500){
                    ge.addError("smoDescripcion", Errores.ERROR_LARGO_DESCRIPCION_500);
                }                
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
     * @param c1 SgSubModalidadAtencion
     * @param c2 SgSubModalidadAtencion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgSubModalidadAtencion c1, SgSubModalidadAtencion c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getSmoCodigo(), c2.getSmoCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getSmoNombre(), c2.getSmoNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getSmoHabilitado(), c2.getSmoHabilitado());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getSmoIntegrada(), c2.getSmoIntegrada());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getSmoModalidadFk(), c2.getSmoModalidadFk());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
