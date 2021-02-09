/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.SolicitudTraslado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfMotivoRechazoTraslado;
import sv.gob.mined.siges.persistencia.entidades.SgAfTraslado;
import sv.gob.mined.siges.persistencia.entidades.SgAfTrasladosDetalle;

public class SolicitudTrasladoValidacionNegocio {
    
    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SolicitudTraslado
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SolicitudTraslado solicitud) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        SgAfTraslado entity = solicitud.getTraslado();
        if (solicitud == null) {
            ge.addError(Errores.ERROR_SOLICITUD_VACIO);
        } else {
            if (solicitud.getEstado() == null || entity == null) {
                if (entity == null) {
                    ge.addError(Errores.ERROR_TRASLADO_VACIO);
                }
                
                if(solicitud.getEstado() == null) {
                    ge.addError(Errores.ERROR_ESTADO_VACIO);
                }
                
            } else {
                if (entity.getTraTipoTrasladoFk()== null) {
                    ge.addError("traTipoTrasladoFk", Errores.ERROR_ESTADO_TIPO_TRASLADO);
                } else {
                    if(Constantes.CODIGO_TIPO_TRASLADO_DEFINITIVO.equals(entity.getTraTipoTrasladoFk().getEtrCodigo().trim())) {
                        if(entity.getTraUnidadAdmOrigenFk() != null && entity.getTraUnidadAdmDestinoFk() != null) {
                            if(entity.getTraUnidadAdmOrigenFk().getUadPk().equals(entity.getTraUnidadAdmDestinoFk().getUadPk())) {
                                ge.addError("traUnidadAdmOrigenDestinoFk", Errores.ERROR_DESTINO_UNIDAD_IGUAL_ORIGEN);
                            }
                        } else {
                            if (entity.getTraUnidadAdmOrigenFk()== null && entity.getTraSedeOrigenFk() == null) {
                                ge.addError("traUnidadAdmOrigenFk", Errores.ERROR_UNIDAD_ADMINISTRATIVA_ORIGEN_VACIO);
                            }
                            if (entity.getTraUnidadAdmDestinoFk()== null && entity.getTraSedeDestinoFk() == null) {
                                ge.addError("traUnidadAdmDestinoFk", Errores.ERROR_UNIDAD_ADMINISTRATIVA_DESTINO_VACIO);
                            }
                        }
                    } else {
                        if (entity.getTraUnidadAdmOrigenFk()== null && entity.getTraSedeOrigenFk() == null) {
                            ge.addError("traUnidadAdmOrigenFk", Errores.ERROR_UNIDAD_ADMINISTRATIVA_ORIGEN_VACIO);
                        }
                        
                        if(StringUtils.isBlank(entity.getTraUnidadDestino())) {
                            ge.addError("traUnidadDestino", Errores.ERROR_UNIDAD_DESTINO_VACIO);
                        }
                        
                        if(entity.getTraFechaRetono() == null) {
                            ge.addError("traFechaRetono", Errores.ERROR_FECHA_RETORNO_VACIO);
                        } else {
                            if(entity.getTraFechaRetono().isBefore(entity.getTraFechaTraslado())) {
                                ge.addError("traFechaRetono", Errores.ERROR_FECHA_RETORNO_MENOR_FECHA_TRASLADO);
                            } 
                            //La fecha de retorno no puede superar el mes despues de la fecha de traslado
                            if(entity.getTraFechaRetono().isAfter(entity.getTraFechaTraslado().plusMonths(1))) {
                                ge.addError("traFechaRetono", Errores.ERROR_FECHA_RETORNO_MAYOR_FECHA_TRASLADO_1_MES); 
                            }
                        }
                    }
                }

                if(entity.getSgAfTrasladosDetalle() == null || entity.getSgAfTrasladosDetalle().isEmpty()) {
                    ge.addError("trasladosDetalle", Errores.ERROR_LISTA_BIENES_TRASLADAR_VACIO);
                } else {
                    for(SgAfTrasladosDetalle det : entity.getSgAfTrasladosDetalle()) {
                        if(entity.getTraUnidadAdmOrigenFk() != null) {
                            if(!det.getTdeBienesDepreciablesFk().getBdeUnidadesAdministrativas().getUadPk().equals(entity.getTraUnidadAdmOrigenFk().getUadPk())) {
                                ge.addError("descargosDetalle", Errores.ERROR_BIEN_NO_PERTENECE_UNIDAD);
                                break;
                            }
                        } else if(entity.getTraSedeOrigenFk() != null) {
                            if(!det.getTdeBienesDepreciablesFk().getBdeSede().getSedPk().equals(entity.getTraSedeOrigenFk().getSedPk())) {
                                 ge.addError("descargosDetalle", Errores.ERROR_BIEN_NO_PERTENECE_UNIDAD);
                                 break;
                            }
                        }
                    }
                }
                
                if (entity.getTraEstadoFk()== null) {
                    ge.addError("traEstadoFk", Errores.ERROR_ESTADO_BIEN_VACIO);
                } else {
                    if(entity.getTraEstadoFk().getEbiCodigo() != null) {
                        if(entity.getTraEstadoFk().getEbiCodigo().equals(Constantes.CODIGO_ESTADO_PROCESO_TRASLADO)) {
                             if(entity.getTraNombreAutoriza()== null) {
                                 ge.addError("traNombreAutoriza", Errores.ERROR_NOMBRE_AUTORIZA_VACIO);  
                             } else if(entity.getTraNombreAutoriza().length() > 100 ) {
                                 ge.addError("traNombreAutoriza", Errores.ERROR_LARGO_NOMBRE_AUTORIZA_100);  
                             }
                             
                             if(entity.getTraCargoAutoriza()== null) {
                                 ge.addError("traCargoAutoriza", Errores.ERROR_CARGO_AUTORIZA_VACIO);  
                             } else if(entity.getTraNombreAutoriza().length() > 100 ) {
                                 ge.addError("traCargoAutoriza", Errores.ERROR_LARGO_CARGO_AUTORIZA_100);  
                             }
                             
                             if(entity.getTraNombreRecibe()== null) {
                                 ge.addError("traNombreRecibe", Errores.ERROR_NOMBRE_RECIBE_VACIO);  
                             } else if(entity.getTraNombreRecibe().length() > 100 ) {
                                 ge.addError("traNombreRecibe", Errores.ERROR_LARGO_NOMBRE_RECIBE_100);  
                             }
                             
                             if(entity.getTraCargoRecibe()== null) {
                                 ge.addError("traCargoRecibe", Errores.ERROR_CARGO_RECIBE_VACIO);  
                             } else if(entity.getTraCargoRecibe().length() > 100 ) {
                                 ge.addError("traCargoRecibe", Errores.ERROR_LARGO_CARGO_RECIBE_100);  
                             }
                        } else if(entity.getTraEstadoFk().getEbiCodigo().equals(Constantes.CODIGO_ESTADO_TRASLADADO)) {
                             if(Constantes.CODIGO_TIPO_TRASLADO_DEFINITIVO.equals(entity.getTraTipoTrasladoFk().getEtrCodigo())) {
                                if(entity.getTraEmpleadoBienes() == null && entity.getTraAsignadoABienes() == null) {
                                    ge.addError("trasAsignadoA", Errores.ERROR_ASIGNADOA_VACIO);
                                }
                            }
                        } else if(entity.getTraEstadoFk().getEbiCodigo().equals(Constantes.CODIGO_ESTADO_SOLICITUD_RECHAZADA)) {
                            if (entity.getSgAfMotivosRechazoTraslado() == null || entity.getSgAfMotivosRechazoTraslado().isEmpty()) {
                                 ge.addError("motivosRechazoTraslado", Errores.ERROR_MOTIVO_RECHAZO_VACIO);
                            } else {
                                 for(SgAfMotivoRechazoTraslado motivo : entity.getSgAfMotivosRechazoTraslado()) {
                                    if(motivo.getMrtMotivoRechazo() != null && motivo.getMrtMotivoRechazo().length() > 500) {
                                        ge.addError("mrdMotivoRechazo", Errores.ERROR_LARGO_MOTIVO_RECHAZO_500);
                                        break;
                                    }
                                 }
                            }
                         }
                     } else {
                         ge.addError("ebiCodigo", Errores.ERROR_ESTADO_BIEN_CODIGO_VACIO);
                     }
                 }
                 
                if(entity.getTraCodigoTraslado() == null) {
                    ge.addError("traCodigoTraslado", Errores.ERROR_CODIGO_TRASLADO_VACIO);  
                }
                if(entity.getTraFechaTraslado() == null) {
                    ge.addError("traFechaTraslado", Errores.ERROR_FECHA_TRASLADO_VACIO);   
                } else {
                    //La fecha de traslado no puede ser mayor a la fecha actual y tampoco ser menor que el año de 1960
                    if(entity.getTraFechaTraslado().isAfter(LocalDate.now())) {
                        ge.addError("traFechaTraslado", Errores.ERROR_FECHA_TRASLADO_MAYOR_FECHA_ACTUAL);
                    } else if(entity.getTraFechaTraslado().getYear() < 1960) {
                        ge.addError("traFechaTraslado", Errores.ERROR_FECHA_TRASLADO_MENOR_FECHA_LIMITE_MENOR);
                    }
                }
            }  
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

     public static boolean validarSolicitud(SolicitudTraslado solicitud) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        SgAfTraslado entity = solicitud.getTraslado();
        if (solicitud == null) {
            ge.addError(Errores.ERROR_SOLICITUD_VACIO);
        } else {
            if (solicitud.getEstado() == null || entity == null) {
                if (entity == null) {
                    ge.addError(Errores.ERROR_TRASLADO_VACIO);
                }
                
                if(solicitud.getEstado() == null) {
                    ge.addError(Errores.ERROR_ESTADO_VACIO);
                }
                
            } 
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
     }
}