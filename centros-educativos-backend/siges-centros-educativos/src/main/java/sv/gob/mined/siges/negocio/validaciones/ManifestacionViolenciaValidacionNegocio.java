/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgManifestacionViolencia; 

public class ManifestacionViolenciaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgManifestacionViolencia entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getMviEstudiante() == null){
                ge.addError("mviEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
            }
            if (entity.getMviTipoManifestacion() == null){
                ge.addError("mviTipoManifestacion", Errores.ERROR_TIPO_MANIFESTACIONO_VACIO);
            } 
            if (entity.getMviFecha() == null){
                ge.addError("mviFecha", Errores.ERROR_FECHA_VACIO);
            }
            if (entity.getMviTratamiento() != null && entity.getMviTratamiento().length() > 500){
                ge.addError("mviTratamiento", Errores.ERROR_LARGO_TRATAMIENTO_500);
            }
            if (StringUtils.isBlank(entity.getMviObservaciones())){
                ge.addError("mviObservaciones", Errores.ERROR_OBSERVACIONES_VACIO);
            } else if (entity.getMviObservaciones().length() > 255){
                ge.addError("mviObservaciones", Errores.ERROR_LARGO_OBSERVACIONES_255);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
