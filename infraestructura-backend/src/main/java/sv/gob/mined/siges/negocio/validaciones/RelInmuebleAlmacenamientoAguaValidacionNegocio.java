/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleAlmacenamientoAgua;

/**
 *
 * @author Sofis Solutions
 */
public class RelInmuebleAlmacenamientoAguaValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ial SgRelInmuebleAlmacenamientoAgua
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelInmuebleAlmacenamientoAgua ial) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ial==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
          
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    
}
