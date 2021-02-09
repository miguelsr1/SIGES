/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgFormacionDocente;

public class FormacionDocenteValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgFormacionDocente
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgFormacionDocente entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getFdoAprobado() == null){
                ge.addError("fdoAprobado", Errores.ERROR_APROBADO_VACIO);
            } 
            if (StringUtils.isBlank(entity.getFdoCalificacionFinal())){
                ge.addError("fdoCalificacionFinal", Errores.ERROR_CALIFICACION_VACIO);
            } else if (entity.getFdoCalificacionFinal().length() > 20){
                ge.addError("fdoCalificacionFinal", Errores.ERROR_LARGO_CALIFICACION_20);
            }
            if (entity.getFdoCategoria() == null){
                ge.addError("fdoCategoria", Errores.ERROR_CATEGORIA_VACIO);
            } 
            if (entity.getFdoDepartamento() == null){
                ge.addError("fdoDepartamento", Errores.ERROR_DEPARTAMENTO_VACIO);
            }
            if (entity.getFdoEspecialidad() == null){
                ge.addError("fdoEspecialidad", Errores.ERROR_ESPECIALIDAD_VACIO);
            }
            if (entity.getFdoModulo() == null){
                ge.addError("fdoModulo", Errores.ERROR_MODULO_VACIO);
            }
            if (entity.getFdoNivel() == null){
                ge.addError("fdoNivel", Errores.ERROR_NIVEL_VACIO);
            }
            if (entity.getFdoFechaDesde()== null){
                ge.addError("fdoFechaDesde", Errores.ERROR_FECHA_DESDE_VACIO);
            }
            if (entity.getFdoFechaHasta()== null){
                ge.addError("fdoFechaHasta", Errores.ERROR_FECHA_HASTA_VACIO);
            }
            if((entity.getFdoFechaDesde()!= null)&&(entity.getFdoFechaHasta()!= null)){
                if (entity.getFdoFechaDesde().isAfter(entity.getFdoFechaHasta())){
                    ge.addError("fdoFechaDesde", Errores.ERROR_FECHA_DESDE_MAYOR_HASTA);
                }
                
            }
            
            if (entity.getFdoSede()== null){
                ge.addError("fdoSede", Errores.ERROR_SEDE_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
