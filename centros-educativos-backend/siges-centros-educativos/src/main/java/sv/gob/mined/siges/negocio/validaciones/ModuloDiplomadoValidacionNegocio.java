/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumCalculoNotaInstitucional;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgModuloDiplomado; 

public class ModuloDiplomadoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgModuloDiplomado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgModuloDiplomado entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {      
            if (StringUtils.isBlank(entity.getMdiCodigo())){
                ge.addError("mdiCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (entity.getMdiCodigo().length() > 4){
                ge.addError("mdiCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            
            if (StringUtils.isBlank(entity.getMdiNombre())){
                ge.addError("mdiNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getMdiNombre().length() > 255){
                ge.addError("mdiNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }            
         
            if(entity.getMdiEscalaCalificacion()==null){
                ge.addError("mdiCodigo", Errores.ERROR_ESCALA_CALIFICACION_VACIO);
            }
            if(entity.getMdiCalculoNotaInstitucional()==null){
                ge.addError("mdiCodigo", Errores.ERROR_CALCULO_NOTA_INSTITUCIONAL_VACIO);
            }
            else{
                if(EnumCalculoNotaInstitucional.PROM.equals(entity.getMdiCalculoNotaInstitucional())){
                    if(entity.getMdiFuncionRedondeo()==null){
                        ge.addError("mdiCodigo", Errores.ERROR_FUNCION_REDONDEO_VACIO);
                    }
                }
            }
                
            
            
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
