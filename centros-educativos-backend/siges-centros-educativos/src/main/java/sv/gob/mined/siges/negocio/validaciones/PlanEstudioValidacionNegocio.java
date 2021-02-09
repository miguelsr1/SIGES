/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPlanEstudio; 

public class PlanEstudioValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgPlanEstudio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPlanEstudio entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(entity.getPesCodigo())){
                ge.addError("pesCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getPesCodigo().length() > 15){
                ge.addError("pesCodigo", Errores.ERROR_LARGO_CODIGO_15);
            }       
            if (StringUtils.isBlank(entity.getPesNombre())){
                ge.addError("pesNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getPesNombre().length() > 255){
                ge.addError("pesNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }          
            if (entity.getPesRelacionModalidad() == null){
                ge.addError("pesRelacionModalidad", Errores.ERROR_MODALIDAD_EDUCATIVA_MODALIDAD_ATENCION);
            }
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
