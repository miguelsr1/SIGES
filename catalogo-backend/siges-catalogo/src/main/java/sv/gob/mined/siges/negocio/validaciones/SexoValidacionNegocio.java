/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSexo; 
import sv.gob.mined.siges.utils.BooleanUtils;


public class SexoValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param sex SgSexo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSexo sex) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (sex==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(sex.getSexCodigo())){
                ge.addError("sexCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (sex.getSexCodigo().length() > 4){
                ge.addError("sexCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(sex.getSexNombre())){
                ge.addError("sexNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (sex.getSexNombre().length() > 255){
                ge.addError("sexNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgSexo
     * @param c2 SgSexo
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgSexo c1, SgSexo c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getSexCodigo(), c2.getSexCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getSexNombre(), c2.getSexNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getSexHabilitado(), c2.getSexHabilitado());       
            }
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
