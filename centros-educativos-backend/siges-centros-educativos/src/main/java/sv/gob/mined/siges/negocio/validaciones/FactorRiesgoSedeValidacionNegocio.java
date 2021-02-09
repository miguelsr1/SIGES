/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgFactorRiesgoSede; 

public class FactorRiesgoSedeValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgFactorRiesgoSede
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgFactorRiesgoSede entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
                 
            if (entity.getFriTipoRiesgo()==null){
                ge.addError("friTipoRiesgo", Errores.ERROR_TIPO_RIESGO_VACIO);
            }
                 
            if (entity.getFriGradoRiesgo()==null){
                ge.addError("friGradoRiesgo", Errores.ERROR_GRADO_RIESGO_VACIO);
            }
            
            if(entity.getFriFechaInicio()!=null && entity.getFriFechaFin()!=null){
                if(entity.getFriFechaInicio().isAfter(entity.getFriFechaFin())){
                    ge.addError("friFechaInicio", Errores.ERROR_FECHA_INICIO_MAYOR_FIN);
                }
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}