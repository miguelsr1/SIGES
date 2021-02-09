/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPlanEscolarAnual; 

public class PlanEscolarAnualValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgPlanEscolarAnual
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPlanEscolarAnual entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (entity.getPeaSede() == null){
                ge.addError("peaSede", Errores.ERROR_SEDE_VACIO);
            }
            
            if (entity.getPeaPropuestaPedagogica() == null){
                ge.addError("peaPropuestaPedagogica", Errores.ERROR_PROPUESTA_PEDAGOGICA_VACIO);
            }
            
            if(entity.getPeaAnioLectivo() == null){
                ge.addError("peaAnioLectivo", Errores.ERROR_ANIO_VACIO);
            }
            
            if(!StringUtils.isBlank(entity.getPeaEvaluacionUno()) && entity.getPeaEvaluacionUno().length()> 500 ){
                ge.addError("peaEvaluacionUno", Errores.ERROR_LARGO_EVALUACION_UNO_500);
            }
            
            if(!StringUtils.isBlank(entity.getPeaEvaluacionDos()) && entity.getPeaEvaluacionDos().length()> 500 ){
                ge.addError("peaEvaluacionDos", Errores.ERROR_LARGO_EVALUACION_DOS_500);
            }
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}