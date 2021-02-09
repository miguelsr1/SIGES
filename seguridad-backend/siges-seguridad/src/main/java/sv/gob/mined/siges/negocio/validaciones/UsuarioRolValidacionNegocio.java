/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgUsuarioRol;

public class UsuarioRolValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgPersonaUsuariaRol
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgUsuarioRol entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getPurContexto() == null) {
                ge.addError("purContexto", Errores.ERROR_CONTEXTO_VACIO);
            } else {
                if (entity.getPurContexto().getConAmbito() == null) {
                    ge.addError("purContexto", Errores.ERROR_AMBITO_VACIO);
                } else {
                    if (!entity.getPurContexto().getConAmbito().equals(EnumAmbito.MINED) && !entity.getPurContexto().getConAmbito().equals(EnumAmbito.PARTICION_SEDE)) {
                        if (entity.getPurContexto().getContextoId() == null) {
                            ge.addError("purContexto", Errores.ERROR_CONTEXTO_VACIO);
                        }
                    }
                    if (entity.getPurContexto().getConAmbito().equals(EnumAmbito.PARTICION_SEDE)){
                        if (entity.getPurContexto().getConRegla() == null) {
                            ge.addError("purRegla", Errores.ERROR_REGLA_VACIO);
                        }
                    }
                }
            }
            if (entity.getPurUsuario() == null) {
                ge.addError("purPersonaUsuaia", Errores.ERROR_PERSONA_USUARIA_VACIO);
            }
            if (entity.getPurRol() == null) {
                ge.addError("purRol", Errores.ERROR_ROL_VACIO);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
