/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgArchivoCalificacionPAES;

public class ArchivoCalificacionPAESValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity
     * @return
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgArchivoCalificacionPAES entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
