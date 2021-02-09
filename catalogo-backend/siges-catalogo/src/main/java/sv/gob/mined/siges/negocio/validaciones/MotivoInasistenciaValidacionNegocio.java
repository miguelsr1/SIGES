/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMotivoInasistencia; 


public class MotivoInasistenciaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param min SgMotivoInasistencia
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgMotivoInasistencia min) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (min==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (StringUtils.isBlank(min.getMinCodigo())){
                ge.addError("minCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (min.getMinCodigo().length() > 4){
                ge.addError("minCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(min.getMinNombre())){
                ge.addError("minNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (min.getMinNombre().length() > 255){
                ge.addError("minNombre", Errores.ERROR_LARGO_NOMBRE_255);
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
     * @param c1 SgMotivoInasistencia
     * @param c2 SgMotivoInasistencia
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgMotivoInasistencia c1, SgMotivoInasistencia c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
   
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
}
