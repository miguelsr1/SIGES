/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgDocumentoSistema; 

public class DocumentoSistemaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgDocumentoSistema
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgDocumentoSistema entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
                
            if (entity.getDsiTipoDocumento()==null){
                ge.addError("dsiTipoDocumento", Errores.ERROR_TIPO_DOCUMENTO);
            }
            
            if (StringUtils.isBlank(entity.getDsiNombre())){
                ge.addError("dsiNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getDsiNombre().length() > 40){
                ge.addError("dsiNombre", Errores.ERROR_LARGO_NOMBRE_40);
            } 
            
            if (StringUtils.isBlank(entity.getDsiDescripcion())){
                ge.addError("dsiDescripcion", Errores.ERROR_DESCRIPCION_VACIO);
            } else if (entity.getDsiDescripcion().length() > 4000){
                ge.addError("dsiDescripcion", Errores.ERROR_LARGO_DESCRIPCION_4000);
            }  
            
            if (entity.getDsiDocumento()==null){
                ge.addError("dsiDocumento", Errores.ERROR_DOCUMENTO_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
