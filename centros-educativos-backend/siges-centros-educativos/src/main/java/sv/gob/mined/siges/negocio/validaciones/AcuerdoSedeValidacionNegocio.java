/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAcuerdoSede;

public class AcuerdoSedeValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgAcuerdoSede
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgAcuerdoSede entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            
            if (entity.getAseSede() == null){
                ge.addError("aseSede", Errores.ERROR_SEDE_VACIO);
            }
            
            if (entity.getAseTipoAcuerdo()==null) {
                ge.addError("aseTipoAcuerdo", Errores.ERROR_TIPO_ACUERDO_VACIO);
            }
            
            if(BooleanUtils.isNotTrue(entity.getAseOrigenExterno()) && entity.getAseNumeroResolucion()==null){
                ge.addError("aseIdResolucion", Errores.ERROR_ID_RESOLUCION_VACIO);
            }
            
            if(BooleanUtils.isNotTrue(entity.getAseOrigenExterno()) && StringUtils.isBlank(entity.getAseNumeroAcuerdo())){
                ge.addError("aseNumeroAcuerdo", Errores.ERROR_NUMERO_ACUERDO_VACIO);
            }else if(entity.getAseNumeroAcuerdo() != null && entity.getAseNumeroAcuerdo().length() > 10){
                ge.addError("aseNumeroAcuerdo", Errores.ERROR_LARGO_NUMERO_ACUERDO_10);
            }
            
            if(StringUtils.isBlank(entity.getAseNombreResponsable())){
                ge.addError("aseNombreResponsable", Errores.ERROR_NOMBRE_RESPONSABLE_VACIO);
            }else if(entity.getAseNombreResponsable().length()>100){
                ge.addError("aseNombreResponsable", Errores.ERROR_LARGO_RESPONSABLE_100);
            }
            
            if ((entity.getAseFechaGeneracion() != null)){
                if(entity.getAseFechaGeneracion().isAfter(LocalDate.now())){
                    ge.addError("aseFechaGeneracion", Errores.ERROR_FECHA_MAYOR_HOY);
                }
            }      
            
            if(!StringUtils.isBlank(entity.getAseObservacion()) && entity.getAseObservacion().length()>4000){
                ge.addError("aseObservacion", Errores.ERROR_LARGO_OBSERVACIONES_4000);
            }    
            
            if(!StringUtils.isBlank(entity.getAseNumeroSolicitud()) && entity.getAseNumeroSolicitud().length()>10){
                ge.addError("aseNumeroSolicitud", Errores.ERROR_LARGO_NUMERO_SOLICITUD_10);
            }   
            
            if(!StringUtils.isBlank(entity.getAseCodigoNominacion()) && entity.getAseCodigoNominacion().length()>10){
                ge.addError("aseCodigoNominacion", Errores.ERROR_LARGO_CODIGO_NOMINACION_10);
            }
            

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
