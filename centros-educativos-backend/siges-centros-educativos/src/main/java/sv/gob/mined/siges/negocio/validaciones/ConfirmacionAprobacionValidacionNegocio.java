/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgConfirmacionAprobacion;

/**
 *
 * @author Sofis Solutions
 */
public class ConfirmacionAprobacionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cma SgConfirmacionMatriculas
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgConfirmacionAprobacion cma) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cma == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (cma.getCprSeccion() == null || cma.getCprSeccion().getSecPk() == null) {
                ge.addError("cprSeccion", Errores.ERROR_SECCION_VACIO);
            }

            if (cma.getCprComponentePlanEstudio() == null) {
                ge.addError("cprComponentePlanEstudio", Errores.ERROR_COMPONENTE_PLAN_ESTUDIO_VACIO);

            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
