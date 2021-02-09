/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgActividadEspecialSeccion;

public class ActividadEspecialSeccionValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgActividadEspecialSeccion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {         
            
            try {
                if (StringUtils.isBlank(entity.getCpeCodigo())){
                ge.addError("cpeCodigo", Errores.ERROR_CODIGO_ACTIVIDAD_VACIO);
            } else if (entity.getCpeCodigo().length() > 10){
                ge.addError("cpeCodigo", Errores.ERROR_LARGO_CODIGO_10);
            }
            
            if (StringUtils.isBlank(entity.getCpeNombre())){
                ge.addError("cpeNombre", Errores.ERROR_NOMBRE_ACTIVIDAD_VACIO);
            } else if (entity.getCpeNombre().length() > 500){
                ge.addError("cpeNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
                
            } catch (BusinessException be){
                ge.getErrores().addAll(be.getErrores());
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
