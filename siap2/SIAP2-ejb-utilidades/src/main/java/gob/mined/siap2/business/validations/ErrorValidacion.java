/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.validations;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.Error;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.StringsUtils;

/**
 *
 * @author Sofis Solutions
 */
public class ErrorValidacion {
     public static boolean validar(Error cgo) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cgo==null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
            if (StringsUtils.isEmpty(cgo.getErrCodigo())) {
                ge.addError(ConstantesErrores.ERROR_CODIGO_VACIO);
            }
            if (StringsUtils.isEmpty(cgo.getErrDescripcion())) {
                ge.addError(ConstantesErrores.ERROR_DESCRIPCION_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
    public static boolean compararParaGrabar(Error c1, Error c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringsUtils.sonStringIguales(c1.getErrCodigo(), c2.getErrCodigo());
                respuesta = respuesta && StringsUtils.sonStringIguales(c1.getErrDescripcion(), c2.getErrDescripcion());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
