/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfEstadosInventario;
import sv.gob.mined.siges.utils.BooleanUtils;

public class EstadosInventarioValidacionNegocio {
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param etn SgAfEstadosInventario
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfEstadosInventario etn) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (etn==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (StringUtils.isBlank(etn.getEinCodigo())){
                ge.addError("einCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (etn.getEinCodigo().length() > 4){
                ge.addError("einCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(etn.getEinNombre())){
                ge.addError("einNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (etn.getEinNombre().length() > 255){
                ge.addError("einNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgAfEstadosInventario
     * @param c2 SgAfEstadosInventario
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgAfEstadosInventario c1, SgAfEstadosInventario c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getEinCodigo(), c2.getEinCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getEinNombre(), c2.getEinNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEinHabilitado(), c2.getEinHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }  
}
