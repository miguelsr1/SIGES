/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelEdificioServicio;

/**
 *
 * @author Sofis Solutions
 */
public class RelEdificioServicioValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param res SgRelEdificioServicio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelEdificioServicio res) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (res==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
             if (res.getResEdificio()== null){
                ge.addError("resEdificio", Errores.ERROR_EDIFICIO_VACIO);
            }
            if (res.getResServicio()== null){
                ge.addError("resServicio", Errores.ERROR_SERVICIO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }


}
