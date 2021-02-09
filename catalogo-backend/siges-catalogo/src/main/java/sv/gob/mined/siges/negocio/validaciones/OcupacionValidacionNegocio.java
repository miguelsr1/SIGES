/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgOcupacion; 
import sv.gob.mined.siges.utils.BooleanUtils;

/**
 *
 * @author Sofis Solutions
 */  
public class OcupacionValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param ocu SgOcupacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgOcupacion ocu) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ocu==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(ocu.getOcuCodigo())) {
                ge.addError("ocuCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ocu.getOcuCodigo().length() > 10){
                ge.addError("ocuCodigo", Errores.ERROR_LARGO_CODIGO_10);
            }
            if (StringUtils.isBlank(ocu.getOcuNombre())) {
                ge.addError("ocuNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ocu.getOcuNombre().length() > 255){
                ge.addError("ocuNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgOcupacion
     * @param c2 SgOcupacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgOcupacion c1, SgOcupacion c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getOcuCodigo(), c2.getOcuCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getOcuNombre(), c2.getOcuNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getOcuHabilitado(), c2.getOcuHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
