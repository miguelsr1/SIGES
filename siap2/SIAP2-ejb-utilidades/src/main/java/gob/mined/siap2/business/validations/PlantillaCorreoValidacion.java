/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.validations;

import gob.mined.siap2.entities.constantes.ConstantesErrores;
import gob.mined.siap2.entities.data.impl.SsPlantillasCorreo;
import gob.mined.siap2.exceptions.BusinessException;
import gob.mined.siap2.utils.generalutils.StringsUtils;

/**
 *
 * @author Sofis Solutions
 */
public class PlantillaCorreoValidacion  {
    
    public static boolean validar(SsPlantillasCorreo cnf) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
         if (cnf==null) {
            ge.addError(ConstantesErrores.ERROR_DATO_VACIO);
        } else {
             if (StringsUtils.isEmpty(cnf.getPlcCodigo())) {
                ge.addError(ConstantesErrores.ERROR_CODIGO_VACIO);
            }
             if (StringsUtils.isEmpty(cnf.getPlcDescripcion())) {
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
    public static boolean compararParaGrabar(SsPlantillasCorreo c1, SsPlantillasCorreo c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringsUtils.sonStringIguales(c1.getPlcCodigo(), c2.getPlcContenido());
                respuesta = respuesta && StringsUtils.sonStringIguales(c1.getPlcContenido(), c2.getPlcContenido());
                respuesta = respuesta && StringsUtils.sonStringIguales(c1.getPlcDescripcion(), c2.getPlcDescripcion());
            
            }  
        } else {
            respuesta = c2==null;
        }
        
        return respuesta;
    }
    
}
