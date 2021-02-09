/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAplicacionPlaza;

public class AplicacionPlazaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAplicacionPlaza
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAplicacionPlaza entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (!StringUtils.isBlank(entity.getAplObservacion()) && entity.getAplObservacion().length() > 500){
                ge.addError("aplDescripcion", Errores.ERROR_LARGO_OBSERVACIONES_500);
            }
            if (entity.getAplPlaza() == null){
                ge.addError("aplPlaza", Errores.ERROR_PLAZA_VACIO);
            } 
            if (entity.getAplPersonal()==null){
                ge.addError("aplPersonal", Errores.ERROR_PERSONAL_VACIO);
            }
            if (BooleanUtils.isTrue(entity.getAplSeleccionadoEnMatriz()) && (entity.getAplMotivosSeleccionPLaza() == null || entity.getAplMotivosSeleccionPLaza().isEmpty())){
                ge.addError("aplPersonal", Errores.ERROR_MOTIVO_SELECCION_VACIO);
            }
            if (entity.getAplCalidadAplicantes()==null){
                ge.addError("aplCalidadAplicantes", Errores.ERROR_CALIDAD_APLICANTES_VACIA);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
