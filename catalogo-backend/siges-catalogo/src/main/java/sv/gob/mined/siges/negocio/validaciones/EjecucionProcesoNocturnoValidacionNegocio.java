/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEjecucionProcesoNocturno;

/**
 *
 * @author Sofis Solutions
 */
public class EjecucionProcesoNocturnoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param epr SgEjecucionProcesoNocturno
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEjecucionProcesoNocturno epr) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (epr==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
           
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
