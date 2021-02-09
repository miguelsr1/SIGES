/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCanton; 
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

public class CantonValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tca SgCanton
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCanton tca) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tca==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(tca.getCanCodigo())){
                ge.addError("canCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tca.getCanCodigo().length() > 6){
                ge.addError("canCodigo", Errores.ERROR_LARGO_CODIGO_6);
            }
            
            if (StringUtils.isBlank(tca.getCanNombre())){
                ge.addError("canNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tca.getCanNombre().length() > 255){
                ge.addError("canNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if (tca.getCanMunicipio() == null){
                ge.addError("canMunicipio", Errores.ERROR_MUNICIPIO_VACIO);            
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
     * @param c1 SgCanton
     * @param c2 SgCanton
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgCanton c1, SgCanton c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getCanCodigo(), c2.getCanCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getCanNombre(), c2.getCanNombre());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getCanMunicipio(), c2.getCanMunicipio());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCanHabilitado(), c2.getCanHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
