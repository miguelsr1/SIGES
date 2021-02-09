/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDatoSaludEstudiante;

/**
 *
 * @author bruno
 */
public class DatoSaludEstudianteValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgDatoEmpleado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDatoSaludEstudiante entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             

            
            if (entity.getDseAnioLectivo() == null){
                ge.addError("dseAnioLectivo", Errores.ERROR_ANIO_LECTIVO_VACIO);
            }
            
            if (entity.getDseTipoApoyo()==null){
                ge.addError("dseTipoApoyo", Errores.ERROR_TIPO_APOYO_VACIO);
            }
            
            if (entity.getDseEstudiante()==null){
                ge.addError("dseEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
            }
            if(!StringUtils.isBlank(entity.getDseDescripcion()) && entity.getDseDescripcion().length()>255){
                ge.addError("dseEstudiante", Errores.ERROR_DESCRIPCION_255);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}
