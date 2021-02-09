/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgIdentificacion; 

public class IdentificacionValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgIdentificacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgIdentificacion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {          
            if (entity.getIdeTipoDocumento() == null){
                ge.addError("ideTipoDocumento", Errores.ERROR_TIPO_IDENTIFICACION_VACIO);
            }   
            if (entity.getIdePaisEmisor() == null){
                ge.addError("idePaisEmisor", Errores.ERROR_PAIS_EMISOR_VACIO);
            }     
            if (StringUtils.isBlank(entity.getIdeNumeroDocumento())){
                ge.addError("ideNumeroDocumento", Errores.ERROR_NUMERO_DOCUMENTO_VACIO);
            }
            else if(entity.getIdeNumeroDocumento().length()>50){
                ge.addError("ideNumeroDocumento", Errores.ERROR_NUMERO_DOCUMENTO_50);
            }  
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }    
    
}
