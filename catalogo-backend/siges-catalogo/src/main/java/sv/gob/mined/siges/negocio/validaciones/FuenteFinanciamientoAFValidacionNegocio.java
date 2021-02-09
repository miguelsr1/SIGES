/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfFuenteFinanciamiento;

public class FuenteFinanciamientoAFValidacionNegocio {
    
     /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param ffi SgAfFuenteFinanciamiento
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAfFuenteFinanciamiento ff) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ff==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(ff.getFfiCodigo())){
                ge.addError("ffICodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ff.getFfiCodigo().length() > 20){
                ge.addError("ffICodigo", Errores.ERROR_LARGO_CODIGO_20);
            }
            
            if (StringUtils.isBlank(ff.getFfiNombre())){
                ge.addError("ffINombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ff.getFfiNombre().length() > 255){
                ge.addError("ffINombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}
