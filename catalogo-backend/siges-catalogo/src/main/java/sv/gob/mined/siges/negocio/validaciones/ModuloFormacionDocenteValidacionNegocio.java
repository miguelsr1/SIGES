/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgModuloFormacionDocente; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class ModuloFormacionDocenteValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param mfd SgModuloFormacionDocente
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgModuloFormacionDocente mfd) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mfd==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(mfd.getMfdCodigo())) {
                ge.addError("mfdCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (mfd.getMfdCodigo().length() > 45){
                ge.addError("mfdCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(mfd.getMfdNombre())) {
                ge.addError("mfdNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (mfd.getMfdNombre().length() > 255){
                ge.addError("mfdNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Compara dos elementos del tipo. 
     * Indica si los elementos contienen la misma información.
     * 
     * @param c1 SgModuloFormacionDocente
     * @param c2 SgModuloFormacionDocente
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgModuloFormacionDocente c1, SgModuloFormacionDocente c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMfdCodigo(), c2.getMfdCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMfdNombre(), c2.getMfdNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMfdHabilitado(), c2.getMfdHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
