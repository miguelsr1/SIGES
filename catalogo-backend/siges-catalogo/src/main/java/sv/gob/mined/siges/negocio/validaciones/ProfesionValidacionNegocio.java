/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgProfesion; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class ProfesionValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param pro SgProfesion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgProfesion pro) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pro==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(pro.getProCodigo())){
                ge.addError("proCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (pro.getProCodigo().length() > 4){
                ge.addError("proCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(pro.getProNombre())){
                ge.addError("proNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (pro.getProNombre().length() > 255){
                ge.addError("proNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgProfesion
     * @param c2 SgProfesion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgProfesion c1, SgProfesion c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getProCodigo(), c2.getProCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getProNombre(), c2.getProNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getProHabilitado(), c2.getProHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
