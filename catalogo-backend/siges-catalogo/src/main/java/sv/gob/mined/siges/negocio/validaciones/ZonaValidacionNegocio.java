/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgZona; 
import sv.gob.mined.siges.utils.BooleanUtils;


public class ZonaValidacionNegocio  {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param zon SgZona
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgZona zon) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (zon==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(zon.getZonCodigo())){
                ge.addError("zonCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (zon.getZonCodigo().length() > 4){
                ge.addError("zonCodigo", Errores.ERROR_LARGO_CODIGO_45);
            }
            
            if (StringUtils.isBlank(zon.getZonNombre())){
                ge.addError("zonNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (zon.getZonNombre().length() > 255){
                ge.addError("zonNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgZona
     * @param c2 SgZona
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgZona c1, SgZona c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getZonCodigo(), c2.getZonCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getZonNombre(), c2.getZonNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getZonHabilitado(), c2.getZonHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
