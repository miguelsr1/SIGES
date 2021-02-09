/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgPropuestaPedagogica;

public class PropuestaPedagogicaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPropuestaPedagogica entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {         
            if (entity.getPpeSede() == null){
                ge.addError("ppeSede", Errores.ERROR_SEDE_VACIO);
            }  
            if (StringUtils.isBlank(entity.getPpeNombre())){
                ge.addError("ppeNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getPpeNombre().length()>100){
                ge.addError("ppeNombre", Errores.ERROR_LARGO_NOMBRE_100);
            }
            if (!StringUtils.isBlank(entity.getPpeCaracterizacion()) && entity.getPpeCaracterizacion().length()>4000){
                ge.addError("ppeCaracterizacion", Errores.ERROR_LARGO_CARACTERIZACION_4000);
            }
            if (!StringUtils.isBlank(entity.getPpeProblemasPriorizados()) && entity.getPpeProblemasPriorizados().length()>4000){
                ge.addError("ppeProblemasPriorizados", Errores.ERROR_LARGO_PROBLEMAS_PRIORIZADOS_4000);
            }
            if (!StringUtils.isBlank(entity.getPpePerfilEstudiante()) && entity.getPpePerfilEstudiante().length()>4000){
                ge.addError("ppePerfilEstudiante", Errores.ERROR_LARGO_PERFIL_ESTUDIANTE_4000);
            }
            if (!StringUtils.isBlank(entity.getPpeRetosPedagogicos()) && entity.getPpeRetosPedagogicos().length()>4000){
                ge.addError("ppeRetosPedagogicos", Errores.ERROR_LARGO_RETOS_PEDAGOGICOS_4000);
            }
            if (!StringUtils.isBlank(entity.getPpeAcuerdosPedagogicos()) && entity.getPpeAcuerdosPedagogicos().length()>4000){
                ge.addError("ppeAcuerdosPedagogicos", Errores.ERROR_LARGO_ACUERDOS_PEDAGOGICOS_4000);
            }
            if (!StringUtils.isBlank(entity.getPpeMetas()) && entity.getPpeMetas().length()>4000){
                ge.addError("ppeMetas", Errores.ERROR_LARGO_METAS_4000);
            }
            if (!StringUtils.isBlank(entity.getPpeIndicadores()) && entity.getPpeIndicadores().length()>4000){
                ge.addError("ppeIndicadores", Errores.ERROR_LARGO_INDICADORES_4000);
            }
            
            if (entity.getPpeFechaFin() != null && entity.getPpeFechaInicio() != null) {
                if (entity.getPpeFechaInicio().isAfter(entity.getPpeFechaFin())) {
                    ge.addError("ppeFechaInicio", Errores.ERROR_FECHA_INICIO_MAYOR_FIN);
                }
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
