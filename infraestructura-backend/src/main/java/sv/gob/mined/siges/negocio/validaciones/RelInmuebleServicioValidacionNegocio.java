/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleServicioBasico;

public class RelInmuebleServicioValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgInmueblesSedes
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelInmuebleServicioBasico entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getRisServicio()== null){
                ge.addError("rieEspacioComun", Errores.ERROR_SERVICIO_VACIO);
            }
            if (entity.getRisInmuebleSede()== null){
                ge.addError("rieInmuebleSede", Errores.ERROR_INMUEBLE_VACIO);
            }         

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
