/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDiplomadosEstudiante;

/**
 *
 * @author Sofis Solutions
 */
public class DiplomadosEstudianteValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgDiplomadosEstudiante
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDiplomadosEstudiante entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (entity.getDepEstudiante() == null && (entity.getDepNieSeleccionado()==null) ){
                ge.addError("depEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
            }else{
                if(entity.getDepPk()!=null && entity.getDepEstudiante() == null){
                    ge.addError("depEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
                }
            } 
            
            if (entity.getDepAnioLectivo() == null){
                ge.addError("depAnioLectivo", Errores.ERROR_ANIO_LECTIVO_VACIO);
            }
            
            if (entity.getDepDiplomado()== null){
                ge.addError("depDiplomado", Errores.ERROR_DIPLOMADO_VACIO);
            }
            if (entity.getDepSedeFk()== null){
                ge.addError("depDiplomado", Errores.ERROR_SEDE_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
