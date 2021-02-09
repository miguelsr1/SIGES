/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteValoracion;

public class EstudianteValoracionValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgPlanEstudio
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEstudianteValoracion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);            
        } else {
            LocalDate hoy = LocalDate.now();
            if (StringUtils.isBlank(entity.getEsvValoracion())){
                ge.addError("esvValoracion", Errores.ERROR_VALORACION_VACIO);
            } else if (entity.getEsvValoracion().length() > 500){
                ge.addError("esvValoracion", Errores.ERROR_VALORACION_500);
            }       
            if (entity.getEsvTipoValoracion() == null){
                ge.addError("esvTipoValoracion", Errores.ERROR_TIPO_VALORACION_VACIO);
            }          
            if (entity.getEsvFechaPublicacion() == null){
                ge.addError("esvFechaPublicacion", Errores.ERROR_FECHA_VALORACION_VACIO);
            }else if(entity.getEsvFechaPublicacion().isAfter(hoy)){
                ge.addError("esvFechaPublicacion", Errores.ERROR_FECHA_MAYOR_HOY);
            }
        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}
