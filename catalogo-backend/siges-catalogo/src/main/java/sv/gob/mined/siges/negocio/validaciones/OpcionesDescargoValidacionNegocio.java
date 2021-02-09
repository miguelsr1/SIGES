/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfOpcionesDescargo;
import sv.gob.mined.siges.utils.BooleanUtils;

public class OpcionesDescargoValidacionNegocio {
     /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgAfOpcionesDescargo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfOpcionesDescargo etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(etn.getOdeCodigo())){
                ge.addError("odeCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getOdeCodigo().length() > 4){
                ge.addError("odeCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(etn.getOdeNombre())){
                ge.addError("odeNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getOdeNombre().length() > 255){
                ge.addError("odeNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
    /**
     * Compara dos elementos del tipo. 
     * Indica si los elementos contienen la misma información.
     * 
     * @param c1 SgAfOpcionesDescargo
     * @param c2 SgAfOpcionesDescargo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAfOpcionesDescargo c1, SgAfOpcionesDescargo c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getOdeCodigo(), c2.getOdeCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getOdeNombre(), c2.getOdeNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getOdeHabilitado(), c2.getOdeHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }  
}