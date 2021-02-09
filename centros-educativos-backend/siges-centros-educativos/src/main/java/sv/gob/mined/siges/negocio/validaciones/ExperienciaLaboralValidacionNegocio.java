/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgExperienciaLaboral; 
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

public class ExperienciaLaboralValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgExperienciaLaboral
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgExperienciaLaboral entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {   
            if (StringUtils.isBlank(entity.getElaInstitucion())){
                ge.addError("elaInstitucion", Errores.ERROR_INSTITUCION_VACIO);
            } else if (entity.getElaInstitucion().length() > 255) {
                ge.addError("elaInstitucion", Errores.ERROR_INSTITUCION_255);
            }
            
            if (entity.getElaTipoInstitucion() == null){
                ge.addError("elaTipoInstitucion", Errores.ERROR_TIPO_INSTITUCION_VACIO);
            }
            
            if (StringUtils.isBlank(entity.getElaDireccion())){
                ge.addError("elaDireccion", Errores.ERROR_DIRECCION_VACIA);
            } else if (entity.getElaDireccion().length() > 500) {
                ge.addError("elaDireccion", Errores.ERROR_LARGO_DIRECCION_500);
            }
            
            if (StringUtils.isBlank(entity.getElaCargo())){
                ge.addError("elaCargo", Errores.ERROR_CARGO_VACIO);
            }     
            if(!StringUtils.isEmpty(entity.getElaDireccion())){
                if(entity.getElaDireccion().length()>255){
                    ge.addError("elaDireccion", Errores.ERROR_LARGO_DIRECCION_255);
                }
            }   else if (entity.getElaCargo().length() > 100) {
                ge.addError("elaCargo", Errores.ERROR_LARGO_CARGO_100);
            }  

            if(entity.getElaDesde()==null){
                ge.addError("elaDesde", Errores.ERROR_FECHA_DESDE_VACIO);
            }else{
                if(entity.getElaDesde().isAfter(LocalDate.now())){
                    ge.addError("elaDesde",Errores.ERROR_FECHA_DESDE_MAYOR);    
                }
            }
            if(entity.getElaHasta()== null){
                ge.addError("elaHasta", Errores.ERROR_FECHA_HASTA_VACIO);
            }else {
                if(entity.getElaHasta().isAfter(LocalDate.now())){
                    ge.addError("elaHasta",Errores.ERROR_FECHA_HASTA_MAYOR);
                }
            }
            if(entity.getElaDesde()!=null && entity.getElaHasta() != null){
                if(entity.getElaDesde().isAfter(entity.getElaHasta())){
                    ge.addError("elaDesde", Errores.ERROR_FECHA_DESDE_MAYOR_HASTA);
                }
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
     * @param c1 SgExperienciaLaboral
     * @param c2 SgExperienciaLaboral
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgExperienciaLaboral c1, SgExperienciaLaboral c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getElaDesde(), c2.getElaDesde());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getElaHasta(), c2.getElaHasta());  
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getElaTipoInstitucion(), c2.getElaTipoInstitucion()); 
                respuesta = respuesta && StringUtils.equals(c1.getElaCargo(), c2.getElaCargo());  
                respuesta = respuesta && StringUtils.equals(c1.getElaDireccion(), c2.getElaDireccion());  
                respuesta = respuesta && StringUtils.equals(c1.getElaInstitucion(), c2.getElaInstitucion());  
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getElaValidada(), c2.getElaValidada());       
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    
    
}
