/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.validations;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.Configuracion;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.BooleanUtils;
import gob.mined.siap2.utils.generalutils.StringsUtils;

/**
 *
 * @author Sofis Solutions
 */
public class ConfiguracionValidacion  {
    
    public static boolean validar(Configuracion cnf) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
         if (cnf==null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
             if (StringsUtils.isEmpty(cnf.getCnfCodigo())) {
                ge.addError(ConstantesErrores.ERROR_CODIGO_VACIO);
            }
             if (StringsUtils.isEmpty(cnf.getCnfValor())) {
                ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
            }
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Este método devuelve true si los dos elementos son iguales como para grabar
     * @param c1
     * @param c2
     * @return 
     */
    public static boolean compararParaGrabar(Configuracion c1, Configuracion c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringsUtils.sonStringIguales(c1.getCnfCodigo(), c2.getCnfCodigo());
                respuesta = respuesta && StringsUtils.sonStringIguales(c1.getCnfDescripcion(), c2.getCnfDescripcion());
                respuesta = respuesta && StringsUtils.sonStringIguales(c1.getCnfValor(), c2.getCnfValor());
                respuesta = respuesta && BooleanUtils.sonBooleanIguales(c1.isCnfProtegido(), c2.isCnfProtegido());
                respuesta = respuesta && BooleanUtils.sonBooleanIguales(c1.getCnfHtml(), c1.getCnfHtml());
            }  
        } else {
            respuesta = c2==null;
        }
        
        return respuesta;
    }
    
}
