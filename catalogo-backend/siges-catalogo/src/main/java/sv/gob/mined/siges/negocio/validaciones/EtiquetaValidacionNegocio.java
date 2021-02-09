/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEtiqueta; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class EtiquetaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param eti SgEtiqueta
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEtiqueta eti) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (eti==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            if (StringUtils.isBlank(eti.getEtiCodigo())){
                ge.addError("etiCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (eti.getEtiCodigo().length() > 45){
                ge.addError("etiCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
                  
            if (StringUtils.isBlank(eti.getEtiValor())){
                ge.addError("etiValor", Errores.ERROR_VALOR_VACIO);
            } else if (eti.getEtiValor().length() > 255){
                ge.addError("etiValor", Errores.ERROR_LARGO_VALOR_255);
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
     * @param c1 SgEtiqueta
     * @param c2 SgEtiqueta
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgEtiqueta c1, SgEtiqueta c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEtiCodigo(), c2.getEtiCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEtiValor(), c2.getEtiValor());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEtiHabilitado(), c2.getEtiHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
    
}
