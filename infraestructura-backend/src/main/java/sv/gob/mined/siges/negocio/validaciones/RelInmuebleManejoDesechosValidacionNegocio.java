/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleManejoDesechos;

/**
 *
 * @author Sofis Solutions
 */
public class RelInmuebleManejoDesechosValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param imd SgRelInmuebleManejoDesechos
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelInmuebleManejoDesechos imd) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (imd==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
          
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    
}
