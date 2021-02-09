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
import sv.gob.mined.siges.persistencia.entidades.SgOrganismoAdministracionEscolar;

/**
 *
 * @author Sofis Solutions
 */
public class OrganismoAdministracionEscolarValidacionNegocio  {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgOrganismoAdministracionEscolar
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgOrganismoAdministracionEscolar entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
          if (entity.getOaeSede()==null){
                ge.addError("oaeSede", Errores.ERROR_SEDE_CAMPO_VACIO);
            }           
          if(StringUtils.isBlank(entity.getOaeActaIntegracion())){
                ge.addError("oaeActaIntegracion", Errores.ERROR_ACTA_INTEGRACION_VACIO);
          }
          if(entity.getOaeFechaActaIntegracion()==null){
                ge.addError("oaeFechaActaIntegracion", Errores.ERROR_FECHA_ACTA_INTEGRACION_VACIO);
          }else{
                if(entity.getOaeFechaActaIntegracion().isAfter(LocalDate.now())){
                    ge.addError("oaeFechaActaIntegracion", Errores.ERROR_FECHA_ACTA_INTEGRACION_MAYOR_HOY);
                }
          }
          
          if(entity.getOaeEstado()==null){
                ge.addError("oaeEstado", Errores.ERROR_ESTADO_VACIO);
          }else{
              switch(entity.getOaeEstado()){
                  case APROBADO:
                      if(entity.getOaeNombre()==null){
                        ge.addError("oaeNombre", Errores.ERROR_NOMBRE_VACIO);
                      }
                      if(entity.getOaeNumeroAcuerdo()==null){
                        ge.addError("oaeNumeroAcuerdo", Errores.ERROR_NUMERO_ACUERDO_VACIO);
                      }
                      if(entity.getOaeFechaLegalizacion()==null){
                        ge.addError("oaeFechaLegalizacion", Errores.ERROR_FECHA_LEGALIZACION_VACIO);
                      }
                      if(BooleanUtils.isTrue(entity.getOaeEsMiembroRenovado())){
                          if(entity.getOaeFechaResolucion()==null){
                              ge.addError("oaeFechaLegalizacion", Errores.ERROR_FECHA_RESOLUCION_VACIO);
                          }
                          if(StringUtils.isBlank(entity.getOaeNumeroResolucion())){
                              ge.addError("oaeFechaLegalizacion", Errores.ERROR_NUMERO_RESOLUCION_VACIO);
                          }
                      }
                      break;
                  case AMPLIAR_DATOS:
                      if(StringUtils.isBlank(entity.getOaeAmpliarDatos())){
                        ge.addError("oaeAmpliarDatos", Errores.ERROR_ACCION_REQUERIDA_VACIO);
                      }
                      break;
                  case RECHAZADO:
                      if(StringUtils.isBlank(entity.getOaeMotivoRechazo())){
                        ge.addError("oaeMotivoRechazo", Errores.ERROR_MOTIVO_RECHAZO_VACIO);
                      }
                      break;
                  case ENVIADO:
                      if(entity.getOaePersonasOrganismoAdministriativo() == null || entity.getOaePersonasOrganismoAdministriativo().isEmpty()){
                        ge.addError("oaePersonasOrganismoAdministriativo", Errores.ERROR_MIEMBROS_OAE_VACIA);
                      }
                      break;
              }
          }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

 
}
