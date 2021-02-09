/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgFuenteFinanciamiento;

public class FuenteFinanciamientoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param ffi SgFuenteFinanciamiento
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgFuenteFinanciamiento ffi) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (ffi==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            
            
            if (StringUtils.isBlank(ffi.getFfiCodigo())){
                ge.addError("ffiCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (ffi.getFfiCodigo().length() > 4){
                ge.addError("ffiCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(ffi.getFfiNombre())){
                ge.addError("ffiNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (ffi.getFfiNombre().length() > 255){
                ge.addError("ffiNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            
            if(BooleanUtils.isFalse(ffi.getFfiAplicaAcuerdo()) && BooleanUtils.isFalse(ffi.getFfiAplicaContrato()) && BooleanUtils.isFalse(ffi.getFfiAplicaOtros())){
                ge.addError("ffiAcuerdo", Errores.ERROR_APLICA_VACIO);
            }
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
