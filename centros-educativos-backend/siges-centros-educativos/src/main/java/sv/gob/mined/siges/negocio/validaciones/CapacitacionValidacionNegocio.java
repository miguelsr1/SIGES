/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCapacitacion;
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

public class CapacitacionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgCapacitacion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCapacitacion entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (StringUtils.isBlank(entity.getCapNombreCapacitacion())){
                ge.addError("capNombreCapacitacion", Errores.ERROR_NOMBRE_VACIO);
            } else if (entity.getCapNombreCapacitacion().length() > 255){
                ge.addError("capNombreCapacitacion", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if (entity.getCapTipoCapacitacion() == null){
                ge.addError("capTipoCapacitacion", Errores.ERROR_TIPO_CAPACITACION_VACIO);
            } 
            if (entity.getCapAlcanceCapacitacion() == null){
                ge.addError("capAlcanceCapacitacion", Errores.ERROR_ALCANCE_CAPACITACION_VACIO);
            } 
            if (StringUtils.isBlank(entity.getCapInstitucionEstudio())){
                ge.addError("capInstitucionEstudio", Errores.ERROR_SEDE_VACIO);
            }  else if (entity.getCapInstitucionEstudio().length() > 255){
                ge.addError("capInstitucionEstudio", Errores.ERROR_LARGO_INSTITUCION_255);
            }
            if (entity.getCapDesde() == null){
                ge.addError("capDesde", Errores.ERROR_FECHA_DESDE_VACIO);
            } 
            if (entity.getCapHasta() == null){
                ge.addError("capHasta", Errores.ERROR_FECHA_HASTA_VACIO);
            }
            if(entity.getCapDesde()!=null && entity.getCapHasta()!=null){
                if (entity.getCapDesde().isAfter(entity.getCapHasta())){
                    ge.addError("capDesde", Errores.ERROR_FECHA_DESDE_MAYOR_HASTA);
                }
            }
            if (entity.getCapDuracionHoras() == null){
                ge.addError("capDuracionHoras", Errores.ERROR_DURACION_HORAS_VACIO);
            } 
            if (entity.getCapModalidad() == null){
                ge.addError("capModalidad", Errores.ERROR_MODALIDAD_VACIO);
            } 
            
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
    
            /**
     * Compara dos elementos del tipo. 
     * Indica si los elementos contienen la misma información.
     * 
     * @param c1 SgCapacitacion
     * @param c2 SgCapacitacion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgCapacitacion c1, SgCapacitacion c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = ObjectUtils.sonIguales(c1.getCapAlcanceCapacitacion(), c2.getCapAlcanceCapacitacion());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getCapDesde(), c2.getCapDesde());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getCapDuracionHoras(), c2.getCapDuracionHoras());  
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getCapHasta(), c2.getCapHasta()); 
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getCapModalidad(), c2.getCapModalidad()); 
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getCapTipoCapacitacion(), c2.getCapTipoCapacitacion()); 
                respuesta = respuesta && StringUtils.equals(c1.getCapInstitucionEstudio(), c2.getCapInstitucionEstudio());  
                respuesta = respuesta && StringUtils.equals(c1.getCapNombreCapacitacion(), c2.getCapNombreCapacitacion());  
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCapValidado(), c2.getCapValidado());       
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getCapCursoAcreditado(), c2.getCapCursoAcreditado());    
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    

}
