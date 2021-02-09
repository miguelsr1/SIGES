/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAcuerdoSistema; 

public class AcuerdoSistemaValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgAcuerdoSistema
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAcuerdoSistema entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (entity.getAsiSistemaIntegrado()==null){
                ge.addError("asiSistemaIntegrado", Errores.ERROR_SISTEMA_INTEGRADO_VACIO);
            }
                
            if (entity.getAsiTipoAcuerdo()==null){
                ge.addError("asiTipoAcuerdo", Errores.ERROR_TIPO_ACUERDO_VACIO);
            }
            
            if (entity.getAsiNumeroAcuerdo()==null){
                ge.addError("asiNumeroAcuerdo", Errores.ERROR_NUMERO_ACUERDO_VACIO);
            } 
            
            if (entity.getAsiFechaCreacion()==null){
                ge.addError("asiFechaCreacion", Errores.ERROR_FECHA_CREACION_VACIO);
            } 
            
            if (!StringUtils.isBlank(entity.getAsiObservaciones()) && entity.getAsiObservaciones().length() > 4000){
                ge.addError("asiObservaciones", Errores.ERROR_LARGO_OBSERVACIONES_4000);
            }  
            
            if (entity.getAsiEstado()==null){
                ge.addError("asiEstado", Errores.ERROR_ESTADO_ACUERDO_VACIO);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
