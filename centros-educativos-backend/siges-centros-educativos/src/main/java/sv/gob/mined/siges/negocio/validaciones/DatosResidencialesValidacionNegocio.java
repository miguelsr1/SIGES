/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDatosResidencialesPersona;

public class DatosResidencialesValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgPersona
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDatosResidencialesPersona entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_PERSONA_VACIA);
        } else {

            if (entity.getPerPersona() == null || entity.getPerPersona().getPerPk() == null){
                ge.addError(Errores.ERROR_PERSONA_VACIA);
            }
          
        }

        if (ge.getErrores().size() > 0) {
            throw ge;
        }

        return respuesta;
    }

   
}
