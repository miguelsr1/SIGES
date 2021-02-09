/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelAulaEspacio;

/**
 *
 * @author Sofis Solutions
 */
public class RelAulaEspacioValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param rae SgRelAulaEspacio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelAulaEspacio rae) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (rae==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (rae.getRaeAula()== null){
                ge.addError("raeAula", Errores.ERROR_AULA_VACIO);
            }
            if (rae.getRaeEspacioComun()== null){
                ge.addError("raeEspacio", Errores.ERROR_ESPACIO_COMUN_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

   
}
