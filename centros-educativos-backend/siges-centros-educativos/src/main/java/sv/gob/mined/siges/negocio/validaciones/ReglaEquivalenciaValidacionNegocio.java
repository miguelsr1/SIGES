/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgReglaEquivalencia;

public class ReglaEquivalenciaValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity
     * @return true/false
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgReglaEquivalencia entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(entity.getReqNombre())){
                ge.addError("reqNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getReqNombre().length() > 255){
                ge.addError("reqNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if (StringUtils.isNotBlank(entity.getReqDescripcion())){
                if (entity.getReqDescripcion().length() > 1000){
                    ge.addError("reqDescripcion", Errores.ERROR_LARGO_DESCRIPCION_1000);
                }
            }
            
            if (StringUtils.isBlank(entity.getReqNormativa())){
                ge.addError("reqNormativa", Errores.ERROR_NORMATIVA_VACIO);
            } else if (entity.getReqNormativa().length() > 255){
                ge.addError("reqNormativa", Errores.ERROR_LARGO_NORMATIVA_255);
            }          
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}
