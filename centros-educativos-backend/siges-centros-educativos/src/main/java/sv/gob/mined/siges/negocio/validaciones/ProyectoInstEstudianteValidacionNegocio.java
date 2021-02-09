/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgProyectoInstEstudiante; 

public class ProyectoInstEstudianteValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgProyectoInstitucional
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgProyectoInstEstudiante entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (entity.getPieEstudiante()==null){
                ge.addError("pieEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
            }             
            if (entity.getPieProyectoInstitucional()==null){
                ge.addError("pieProyectoInstitucional", Errores.ERROR_PROYECTO_INSTITUCIONAL_VACIO);
            }             
            if (entity.getPieFechaOtorgado()==null){
                ge.addError("pieFechaOtorgado", Errores.ERROR_FECHA_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}