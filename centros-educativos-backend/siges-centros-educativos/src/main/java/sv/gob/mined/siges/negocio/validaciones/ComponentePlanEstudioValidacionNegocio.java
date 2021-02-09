/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanEstudio; 

public class ComponentePlanEstudioValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgComponentePlanEstudio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgComponentePlanEstudio entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {   
            if (StringUtils.isBlank(entity.getCpeCodigo())){
                ge.addError("cpeCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getCpeCodigo().length() > 10){
                ge.addError("cpeCodigo", Errores.ERROR_LARGO_CODIGO_10);
            }
            
            if (StringUtils.isBlank(entity.getCpeNombre())){
                ge.addError("cpeNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getCpeNombre().length() > 500){
                ge.addError("cpeNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if (BooleanUtils.isTrue(entity.getCpeEsPaes())){
                
                if (entity.getCpeTipoPaes() == null){
                    ge.addError("cpeTipoPaes", Errores.ERROR_TIPO_PAES_VACIO);
                    
                }
            
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }    
}
