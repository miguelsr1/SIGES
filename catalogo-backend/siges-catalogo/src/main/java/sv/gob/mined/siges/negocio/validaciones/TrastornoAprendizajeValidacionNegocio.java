/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTrastornoAprendizaje; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class TrastornoAprendizajeValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tra SgTrastornoAprendizaje
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTrastornoAprendizaje tra) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tra==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(tra.getTraCodigo())){
                ge.addError("traCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tra.getTraCodigo().length() > 4){
                ge.addError("traCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(tra.getTraNombre())){
                ge.addError("traNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tra.getTraNombre().length() > 255){
                ge.addError("traNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTrastornoAprendizaje
     * @param c2 SgTrastornoAprendizaje
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTrastornoAprendizaje c1, SgTrastornoAprendizaje c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTraCodigo(), c2.getTraCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTraNombre(), c2.getTraNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTraHabilitado(), c2.getTraHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
