/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgTipoInstitucionPaga; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class TipoInstitucionPagaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param tip SgTipoInstitucionPaga
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgTipoInstitucionPaga tip) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (tip==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(tip.getTipCodigo())){
                ge.addError("tipCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (tip.getTipCodigo().length() > 4){
                ge.addError("tipCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(tip.getTipNombre())){
                ge.addError("tipNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (tip.getTipNombre().length() > 255){
                ge.addError("tipNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgTipoInstitucionPaga
     * @param c2 SgTipoInstitucionPaga
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgTipoInstitucionPaga c1, SgTipoInstitucionPaga c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getTipCodigo(), c2.getTipCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getTipNombre(), c2.getTipNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getTipHabilitado(), c2.getTipHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
    
}
