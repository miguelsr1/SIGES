/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelEdificioServicioSanitario;

/**
 *
 * @author Sofis Solutions
 */
public class RelEdificioServicioSanitarioValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ret SgRelEdificioServicioSanitario
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelEdificioServicioSanitario ret) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ret==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
             if (ret.getRetEdificio()== null){
                ge.addError("resEdificio", Errores.ERROR_EDIFICIO_VACIO);
            }
            if (ret.getRetServicioSanitario()== null){
                ge.addError("resServicio", Errores.ERROR_SERVICIO_VACIO);
            }
        }
        
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

  
}
