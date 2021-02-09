/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoEgreso; 
import sv.gob.mined.siges.utils.BooleanUtils;

public class MotivoEgresoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param meg SgMotivoEgreso
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMotivoEgreso meg) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (meg==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(meg.getMegCodigo())){
                ge.addError("megCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (meg.getMegCodigo().length() > 4){
                ge.addError("megCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(meg.getMegNombre())){
                ge.addError("megNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (meg.getMegNombre().length() > 255){
                ge.addError("megNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgMotivoEgreso
     * @param c2 SgMotivoEgreso
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMotivoEgreso c1, SgMotivoEgreso c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = StringUtils.equals(c1.getMegCodigo(), c2.getMegCodigo());
                respuesta = respuesta && StringUtils.equals(c1.getMegNombre(), c2.getMegNombre());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getMegHabilitado(), c2.getMegHabilitado());
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }  
}
