/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDetallePlanEscolar; 

public class DetallePlanEscolarValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgDetallePlanEscolar
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDetallePlanEscolar entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (!StringUtils.isBlank(entity.getDpeActividad()) && entity.getDpeActividad().length()>500){
                ge.addError("dpeActividad", Errores.ERROR_LARGO_ACTIVIDAD_500);
            }
                         
            if (!StringUtils.isBlank(entity.getDpeResponsables()) && entity.getDpeResponsables().length()>255){
                ge.addError("dpeResponsables", Errores.ERROR_LARGO_RESPONSABLES_255);
            }
            
            if(entity.getDpeFechaInicio()!=null && entity.getDpeFechaFin()!=null){
                if(entity.getDpeFechaInicio().isAfter(entity.getDpeFechaFin())){
                    ge.addError("dpeFechaInicio", Errores.ERROR_FECHA_INICIO_MAYOR_FIN);
                }
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}