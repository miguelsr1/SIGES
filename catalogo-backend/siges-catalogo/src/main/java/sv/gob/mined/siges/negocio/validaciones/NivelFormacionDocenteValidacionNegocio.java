/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgNivelFormacionDocente; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class NivelFormacionDocenteValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param nfd SgNivelFormacionDocente
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgNivelFormacionDocente nfd) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (nfd==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(nfd.getNfdCodigo())) {
                ge.addError("nfdCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (nfd.getNfdCodigo().length() > 45){
                ge.addError("nfdCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(nfd.getNfdNombre())) {
                ge.addError("nfdNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (nfd.getNfdNombre().length() > 255){
                ge.addError("nfdNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgNivelFormacionDocente
     * @param c2 SgNivelFormacionDocente
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgNivelFormacionDocente c1, SgNivelFormacionDocente c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getNfdCodigo(), c2.getNfdCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getNfdNombre(), c2.getNfdNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getNfdHabilitado(), c2.getNfdHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
