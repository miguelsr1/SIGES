/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCarrera; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class CarreraValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param crr SgCarrera
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCarrera crr) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (crr==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(crr.getCrrCodigo())) {
                ge.addError("crrCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (crr.getCrrCodigo().length() > 45){
                ge.addError("crrCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(crr.getCrrNombre())) {
                ge.addError("crrNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (crr.getCrrNombre().length() > 255){
                ge.addError("crrNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgCarrera
     * @param c2 SgCarrera
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgCarrera c1, SgCarrera c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getCrrCodigo(), c2.getCrrCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getCrrNombre(), c2.getCrrNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCrrHabilitado(), c2.getCrrHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
