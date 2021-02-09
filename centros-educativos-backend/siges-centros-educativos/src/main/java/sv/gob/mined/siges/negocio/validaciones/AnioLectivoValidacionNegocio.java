/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAnioLectivo;

public class AnioLectivoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAnioLectivo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAnioLectivo entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(entity.getAleDescripcion())){
                ge.addError("aleDescripcion", Errores.ERROR_DESCRIPCION_VACIO);
            } else if (entity.getAleDescripcion().length() > 255){
                ge.addError("aleDescripcion", Errores.ERROR_LARGO_DESCRIPCION_255);
            }
            if (entity.getAleAnio() == null){
                ge.addError("aleAnio", Errores.ERROR_ANIO_VACIO);
            } 
            if (entity.getAleEstado()==null){
                ge.addError("aleEstado", Errores.ERROR_ESTADO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
