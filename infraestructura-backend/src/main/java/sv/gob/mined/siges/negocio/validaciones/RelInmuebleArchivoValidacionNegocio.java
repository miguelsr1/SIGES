/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelInmuebleArchivo;

/**
 *
 * @author Sofis Solutions
 */
public class RelInmuebleArchivoValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ria SgRelInmuebleArchivo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelInmuebleArchivo ria) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ria==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (ria.getRiaArchivo()== null){
                ge.addError("riaArchivo", Errores.ERROR_ARCHIVO_VACIO);
            }
            if (ria.getRiaInmuebleSede()== null){
                ge.addError("riaInmuebleSede", Errores.ERROR_INMUEBLE_VACIO);
            }
          
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

   
}
