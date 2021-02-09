/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgModalidad; 

public class ModalidadValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgModalidad
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgModalidad entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(entity.getModCodigo())){
                ge.addError("modCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getModCodigo().length() > 4){
                ge.addError("modCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(entity.getModNombre())){
                ge.addError("modNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getModNombre().length() > 500){
                ge.addError("modNombre", Errores.ERROR_LARGO_NOMBRE_500);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}