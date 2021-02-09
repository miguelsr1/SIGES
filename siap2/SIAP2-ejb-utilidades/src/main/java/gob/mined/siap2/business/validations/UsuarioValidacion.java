/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.validations;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.SsUsuario;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.StringsUtils;

/**
 *
 * @author Sofis Solutions
 */
public class UsuarioValidacion {
     public static boolean validar(SsUsuario usu) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (usu==null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
            if (StringsUtils.isEmpty(usu.getUsuCod())) {
                ge.addError(ConstantesErrores.ERROR_CODIGO_VACIO);
            }
            if (StringsUtils.isEmpty(usu.getUsuPrimerNombre())) {
                ge.addError(ConstantesErrores.ERROR_DESCRIPCION_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
    public static boolean compararParaGrabar(SsUsuario c1, SsUsuario c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta=false;
                 
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
