/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfSolvencias;

public class SolvenciasValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAfSolvencias
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfSolvencias entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
           if (entity.getSolUnidadAdministrativaFk() == null && entity.getSolSedeFk() == null) {
               ge.addError("bdeUnidadesAdministrativas", Errores.ERROR_UNIDAD_ADMINISTRATIVA_VACIO);
            }
            if (entity.getSolAnio()== null) {
                ge.addError("depAnio", Errores.ERROR_ANIO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}