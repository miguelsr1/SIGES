/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelModEdModAten; 

public class RelModEdModAtenValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelModEdModAten entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getReaModalidadEducativa() == null){
                ge.addError("reaModalidadEducativa", Errores.ERROR_MODALIDAD_EDUCATIVA_VACIO);
            }
            if (entity.getReaModalidadAtencion() == null){
                ge.addError("reaModalidadAtencion", Errores.ERROR_MODALIDAD_ATENCION_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}

