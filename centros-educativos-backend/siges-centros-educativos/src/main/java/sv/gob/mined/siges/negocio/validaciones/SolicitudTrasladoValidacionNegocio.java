/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudTraslado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudTraslado; 

public class SolicitudTrasladoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     * 
     * @param entity SgSolicitudTraslado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSolicitudTraslado entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity==null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (entity.getSotSedeSolicitante() == null){
                ge.addError("sotSedeSolicitante", Errores.ERROR_SEDE_SOLICITANTE_VACIO);
            }
            if (entity.getSotEstudiante() == null){
                ge.addError("sotEstudiante", Errores.ERROR_ESTUDIANTE_VACIO);
            }
            if (entity.getSotServicioEducativoSolicitado() == null){
                ge.addError("sotServicioEducativoSolicitado", Errores.ERROR_SERVICIO_EDUCATIVO_SOLICITADO_VACIO);
            }
            if (entity.getSotSedeOrigen() == null){
                ge.addError("sotSedeOrigen", Errores.ERROR_SEDE_ORIGEN_VACIO);
            }
            if (entity.getSotServicioEducativoOrigen() == null){
                ge.addError("sotServicioEducativoOrigen", Errores.ERROR_SERVICIO_EDUCATIVO_ORIGEN_VACIO);
            }
            if (entity.getSotEstado() == null){
                ge.addError("sotEstado", Errores.ERROR_ESTADO_SOLICITUD_TRASLADO_VACIO);
            }
            if (entity.getSotUsuarioSolicitud() == null){
                ge.addError("sotUsuarioSolicitud", Errores.ERROR_USUARIO_SOLICITUD_TRASLADO_VACIO);
            }
            if (entity.getSotFechaSolicitud() == null){
                ge.addError("sotFechaSolicitud", Errores.ERROR_FECHA_VACIO);
            }
                     
            if (StringUtils.isBlank(entity.getSotObservacion())){
                ge.addError("sotObservacion", Errores.ERROR_OBSERVACION_VACIO);
            }else if(entity.getSotObservacion().length()>250){
                ge.addError("sotObservacion", Errores.ERROR_LARGO_OBSERVACIONES_250);
            }
            if (entity.getSotMotivoTraslado() == null){
                ge.addError("sotMotivoTraslado", Errores.ERROR_MOTIVO_TRASLADO_VACIO);
            }
            
            if (EnumEstadoSolicitudTraslado.CONFIRMADO.equals(entity.getSotEstado())){
                if (entity.getSotSeccion() == null || entity.getSotSeccion().getSecPk() == null){
                    ge.addError("sotSeccion", Errores.ERROR_SECCION_VACIO);
                }
                if (entity.getSotFechaTraslado() == null){
                    ge.addError("sotFechaTraslado", Errores.ERROR_FECHA_TRASLADO_VACIA);
                }
            }

        }
        if (ge.getErrores().size()>0) {
            throw ge;
        }
        return respuesta;
    }
    
}