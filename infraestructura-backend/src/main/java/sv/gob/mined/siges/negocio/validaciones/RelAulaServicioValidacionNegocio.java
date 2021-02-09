/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgRelAulaServicio;

/**
 *
 * @author Sofis Solutions
 */
public class RelAulaServicioValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ras SgRelAulaServicio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgRelAulaServicio ras) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ras==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
             if (ras.getRasAula()== null){
                ge.addError("rasAula", Errores.ERROR_AULA_VACIO);
            }
            if (ras.getRasServicio()== null){
                ge.addError("resServicio", Errores.ERROR_SERVICIO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
