/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAsignacionPerfilPersonal;

/**
 *
 * @author Sofis Solutions
 */
public class AsignacionPerfilPersonalValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param app SgAsignacionPerfilPersonal
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAsignacionPerfilPersonal app) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (app==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (app.getAppPersonalFk()==null){
                ge.addError("apeCodigo",Errores.ERROR_PERSONAL_VACIO);
            }
            if (app.getAppPerfilFk()==null){
                ge.addError("apeCodigo",Errores.ERROR_PERFIL_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
