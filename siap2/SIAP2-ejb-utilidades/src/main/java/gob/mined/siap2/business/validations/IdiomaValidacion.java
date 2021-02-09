/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.validations;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.Idioma;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.BooleanUtils;
import gob.mined.siap2.utils.generalutils.StringsUtils;

/**
 *
 * @author Sofis Solutions
 */
public class IdiomaValidacion {
     public static boolean validar(Idioma tho) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tho==null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
            if (StringsUtils.isEmpty(tho.getIdiCodigo())) {
                ge.addError(ConstantesErrores.ERROR_CODIGO_VACIO);
            }
            if (StringsUtils.isEmpty(tho.getIdiDescripcion())) {
                ge.addError(ConstantesErrores.ERROR_DESCRIPCION_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
    public static boolean compararParaGrabar(Idioma c1, Idioma c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringsUtils.sonStringIguales(c1.getIdiCodigo(), c2.getIdiCodigo());
                respuesta = respuesta && StringsUtils.sonStringIguales(c1.getIdiDescripcion(), c2.getIdiDescripcion());
                respuesta = respuesta && BooleanUtils.sonBooleanIguales(c1.getIdiHabilitado(), c2.getIdiHabilitado());
             
                
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }
}
