/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAsignacionPerfil;

/**
 *
 * @author Sofis Solutions
 */
public class AsignacionPerfilValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param ape SgAsignacionPerfil
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAsignacionPerfil ape) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ape==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (ape.getApeSedeFk()==null){
                ge.addError("apeCodigo",Errores.ERROR_SEDE_VACIO);
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

  
}
