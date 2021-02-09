/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.Year;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEstudioSuperior;
import sv.gob.mined.siges.utils.BooleanUtils;
import sv.gob.mined.siges.utils.ObjectUtils;

public class EstudioSuperiorValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgEstudioSuperior
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEstudioSuperior entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getEsuTipo() == null){
                ge.addError("esuTipo", Errores.ERROR_TIPO_VACIO);
            } 
            if (entity.getEsuPais() == null){
                ge.addError("esuPais", Errores.ERROR_PAIS_VACIO);
            }
            if (entity.getEsuCarrera() == null && StringUtils.isBlank(entity.getEsuCarreraTxt())){
                if (entity.getEsuCarrera() == null){
                    ge.addError("esuCarrera", Errores.ERROR_CARRERA_VACIO);
                }
            }
            if (entity.getEsuEspecialidad() == null){
                ge.addError("esuEspecialidad", Errores.ERROR_ESPECIALIDAD_VACIO);
            }
            if (entity.getEsuInstitucionEstudio() == null && StringUtils.isBlank(entity.getEsuInstitucionEstudioTxt())){
                if(entity.getEsuInstitucionEstudio() == null){
                    ge.addError("esuInstitucionEstudio", Errores.ERROR_INSTITUCION_ESTUDIO_VACIO);
                }
            }
            if (entity.getEsuDesde() == null){
                ge.addError("esuDesde", Errores.ERROR_FECHA_DESDE_VACIO);
            }
            if (entity.getEsuHasta() == null){
                ge.addError("esuHasta", Errores.ERROR_FECHA_HASTA_VACIO);
            }
            if(entity.getEsuDesde()!=null && entity.getEsuHasta()!=null){
                if (entity.getEsuDesde().isAfter(entity.getEsuHasta())){
                    ge.addError("esuDesde", Errores.ERROR_FECHA_DESDE_MAYOR_HASTA);
                }
            }
            if (entity.getEsuModalidad() == null){
                ge.addError("esuModalidad", Errores.ERROR_MODALIDAD_VACIO);
            }
            if (entity.getEsuAnioTitulacion() == null){
                ge.addError("esuAnioTitulacion", Errores.ERROR_ANIO_TITULACION_VACIO);
            }else if (entity.getEsuAnioTitulacion() > Year.now().getValue()){
                ge.addError("esuAnioTitulacion", Errores.ERROR_ANIO_TITULACION_MAYOR_ACTUAL);
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
     * @param c1 SgEstudioSuperior
     * @param c2 SgEstudioSuperior
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgEstudioSuperior c1, SgEstudioSuperior c2) {
 
        boolean respuesta = true;
        if (c1!=null) {
            if (c2!=null) {
                respuesta = ObjectUtils.sonIguales(c1.getEsuCarrera(), c2.getEsuCarrera());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getEsuAnioTitulacion(), c2.getEsuAnioTitulacion());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getEsuDesde(), c2.getEsuDesde());   
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getEsuEspecialidad(), c2.getEsuEspecialidad()); 
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getEsuHasta(), c2.getEsuHasta());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getEsuInstitucionEstudio(), c2.getEsuInstitucionEstudio());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getEsuModalidad(), c2.getEsuModalidad());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getEsuPais(), c2.getEsuPais());
                respuesta = respuesta && ObjectUtils.sonIguales(c1.getEsuTipo(), c2.getEsuTipo());
                respuesta = respuesta && BooleanUtils.sonIguales(c1.getEsuValidado(), c2.getEsuValidado());       
                respuesta = respuesta && StringUtils.equals(c1.getEsuCarreraTxt(), c2.getEsuCarreraTxt());  
                respuesta = respuesta && StringUtils.equals(c1.getEsuInstitucionEstudioTxt(), c2.getEsuInstitucionEstudioTxt());      
            }  
        } else {
            respuesta = c2==null;
        }
        return respuesta;
    }    

}
