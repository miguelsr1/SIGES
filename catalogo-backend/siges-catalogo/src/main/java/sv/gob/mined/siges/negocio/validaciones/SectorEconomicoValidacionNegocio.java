/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSectorEconomico; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class SectorEconomicoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param sec SgSectorEconomico
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSectorEconomico sec) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (sec==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(sec.getSecCodigo())){
                ge.addError("secCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (sec.getSecCodigo().length() > 10){
                ge.addError("secCodigo", Errores.ERROR_LARGO_CODIGO_10);
            }
            
            if (StringUtils.isBlank(sec.getSecNombre())){
                ge.addError("secNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (sec.getSecNombre().length() > 255){
                ge.addError("secNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgSectorEconomico
     * @param c2 SgSectorEconomico
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgSectorEconomico c1, SgSectorEconomico c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getSecCodigo(), c2.getSecCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getSecNombre(), c2.getSecNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getSecHabilitado(), c2.getSecHabilitado());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}

