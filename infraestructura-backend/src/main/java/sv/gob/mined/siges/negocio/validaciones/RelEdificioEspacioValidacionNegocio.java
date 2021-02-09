/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelEdificioEspacio;

/**
 *
 * @author Sofis Solutions
 */
public class RelEdificioEspacioValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ree SgRelEdificioEspacio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelEdificioEspacio ree) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ree==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (ree.getReeEdificio()== null){
                ge.addError("reeEdificio", Errores.ERROR_EDIFICIO_VACIO);
            }
            if (ree.getReeEspacioComun()== null){
                ge.addError("reeEspacioComun", Errores.ERROR_ESPACIO_COMUN_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

 
}
