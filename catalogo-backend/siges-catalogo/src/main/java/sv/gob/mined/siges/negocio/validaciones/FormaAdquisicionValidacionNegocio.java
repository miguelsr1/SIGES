/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfFormaAdquisicion;
import sv.gob.mined.siges.utils.BooleanUtils;

public class FormaAdquisicionValidacionNegocio {
   /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgAfFormaAdquisicion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfFormaAdquisicion etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(etn.getFadCodigo())){
                ge.addError("fadCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getFadCodigo().length() > 4){
                ge.addError("fadCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(etn.getFadNombre())){
                ge.addError("fadNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getFadNombre().length() > 255){
                ge.addError("fadNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgAfFormaAdquisicion
     * @param c2 SgAfFormaAdquisicion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAfFormaAdquisicion c1, SgAfFormaAdquisicion c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getFadCodigo(), c2.getFadCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getFadNombre(), c2.getFadNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getFadHabilitado(), c2.getFadHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }  
}
