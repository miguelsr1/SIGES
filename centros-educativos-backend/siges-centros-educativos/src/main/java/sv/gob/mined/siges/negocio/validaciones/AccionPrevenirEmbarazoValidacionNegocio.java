/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAccionPrevenirEmbarazo; 

public class AccionPrevenirEmbarazoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgAccionPrevenirEmbarazo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAccionPrevenirEmbarazo entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {             
            if (entity.getApeAnio()==null){
                ge.addError("apeAnio", Errores.ERROR_ANIO_VACIO);
            }
            
            if (StringUtils.isBlank(entity.getApeInstitucion())){
                ge.addError("apeInstitucion", Errores.ERROR_INSTITUCION_VACIO);
            } else if (entity.getApeInstitucion().length() > 255){
                ge.addError("apeInstitucion", Errores.ERROR_LARGO_INSTITUCION_255);
            }
                 
            if (entity.getApeTipoAccion()==null){
                ge.addError("apeTipoAccion", Errores.ERROR_TIPO_ACCION_VACIO);
            }else if(BooleanUtils.isTrue(entity.getApeTipoAccion().getTacNecesitaDescripcion())){
                if(StringUtils.isBlank(entity.getApeDescripcion())){
                    ge.addError("apeDescripcion", Errores.ERROR_DESCRIPCION_VACIO);
                }else if(entity.getApeDescripcion().length()>255){
                    ge.addError("apeDescripcion", Errores.ERROR_LARGO_DESCRIPCION_255);
                }
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
}