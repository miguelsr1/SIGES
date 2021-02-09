/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoCalle; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class TipoCalleValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tll SgTipoCalle
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoCalle tll) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tll==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(tll.getTllCodigo())) {
                ge.addError("tllCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tll.getTllCodigo().length() > 45){
                ge.addError("tllCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            if (StringUtils.isBlank(tll.getTllNombre())) {
                ge.addError("tllNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tll.getTllNombre().length() > 255){
                ge.addError("tllNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoCalle
     * @param c2 SgTipoCalle
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoCalle c1, SgTipoCalle c2) {
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTllCodigo(), c2.getTllCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTllNombre(), c2.getTllNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTllHabilitado(), c2.getTllHabilitado());     
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
