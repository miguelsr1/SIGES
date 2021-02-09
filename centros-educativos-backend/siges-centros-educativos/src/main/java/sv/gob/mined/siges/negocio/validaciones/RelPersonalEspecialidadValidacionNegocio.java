/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelPersonalEspecialidad;

public class RelPersonalEspecialidadValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgRelPersonalEspecialidad
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelPersonalEspecialidad entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (entity.getRpeEspecialidad()==null){
                ge.addError("rpeEspecialidad", Errores.ERROR_ESPECIALIDAD_VACIO);
            }
            if (entity.getRpePersonal()==null){
                ge.addError("rpePersonal", Errores.ERROR_PERSONAL_VACIO);
            }
            
            if (entity.getRpeCum()==null){
                ge.addError("rpeCum", Errores.ERROR_CUM_NULO);
            }
            
            if (entity.getRpeFechaGraduacion()==null){
                ge.addError("rpeEspecialidad", Errores.ERROR_FECHA_GRADUACION_VACIA);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}