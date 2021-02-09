/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.LocalDate;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.dto.SolicitudDescargo;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgAfDescargo;
import sv.gob.mined.siges.persistencia.entidades.SgAfDescargoDetalle;
import sv.gob.mined.siges.persistencia.entidades.SgAfMotivoRechazoDescargo;

public class SolicitudDescargoValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SolicitudDescargo
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SolicitudDescargo solicitud) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        SgAfDescargo entity = solicitud.getDescargo();
        if (solicitud == null) {
            ge.addError(Errores.ERROR_SOLICITUD_VACIO);
        } else {
            if (solicitud.getEstado() == null || entity == null) {
                if (entity == null) {
                    ge.addError(Errores.ERROR_DESCAGO_VACIO);
                }
                
                if(solicitud.getEstado() == null) {
                    ge.addError(Errores.ERROR_ESTADO_VACIO);
                }
                
            } else {
                if (entity.getDesTipoDescargoFk()== null) {
                    ge.addError("desTipoDescargoFk", Errores.ERROR_ESTADO_TIPO_DESCARGO);
                }
                if (entity.getDesOpcionDescargoFk()== null) {
                    ge.addError("desOpcionDescargoFk", Errores.ERROR_OPCION_DESCARGO);
                }
                if (entity.getDesUnidadAdministrativaFk()== null && entity.getDesSedeFk()== null) {
                    ge.addError("desUnidadAdministrativaFk", Errores.ERROR_UNIDAD_ADMINISTRATIVA_VACIO);
                }

                if(entity.getDesActivo() == null) {
                    ge.addError("desActivo", Errores.ERROR_TIPO_ACTIVO);
                }/** else { //Descomentar cuando se apruebe el cambio
                    if(entity.getDesActivo()) {
                        if(entity.getDesOpcionDescargoFk() != null && entity.getDesOpcionDescargoFk().getOdeCodigo().equals(Constantes.CODIGO_TIPO_DESCARGO_PERMUTA)) {
                            ge.addError("opcionDescargoPermuta", Errores.ERROR_OPCION_DESCARGO_PERMUTA);
                        }
                    }
                }**/
                if(entity.getSgAfDescargosDetalle() == null || entity.getSgAfDescargosDetalle().isEmpty()) {
                    ge.addError("descargosDetalle", Errores.ERROR_LISTA_BIENES_DESCARGAR_VACIO);
                } else {
                    for(SgAfDescargoDetalle det : entity.getSgAfDescargosDetalle()) {
                        if(entity.getDesUnidadAdministrativaFk() != null) {
                            if(!det.getDdeBienesDepreciablesFk().getBdeUnidadesAdministrativas().getUadPk().equals(entity.getDesUnidadAdministrativaFk().getUadPk())) {
                                ge.addError("descargosDetalle", Errores.ERROR_BIEN_NO_PERTENECE_UNIDAD);
                                break;
                            }
                        } else if(entity.getDesSedeFk() != null) {
                            if(!det.getDdeBienesDepreciablesFk().getBdeSede().getSedPk().equals(entity.getDesSedeFk().getSedPk())) {
                                 ge.addError("descargosDetalle", Errores.ERROR_BIEN_NO_PERTENECE_UNIDAD);
                                 break;
                            }
                        }
                    }
                }
                 
                 if (entity.getDesEstadoFk()== null) {
                    ge.addError("desEstadoFk", Errores.ERROR_ESTADO_BIEN_VACIO);
                 } else {
                     if(entity.getDesEstadoFk().getEbiCodigo() != null) {
                         if(entity.getDesEstadoFk().getEbiCodigo().equals(Constantes.CODIGO_ESTADO_DESCARGADO)) {
                             if(entity.getDesCodigoDescargo() == null) {
                                 ge.addError("desCodigoDescargo", Errores.ERROR_CODIGO_DESCARGO_VACIO);  
                             }
                             if(entity.getDesFechaDescargo() == null) {
                                 ge.addError("desFechaDescargo", Errores.ERROR_FECHA_DESCARGO_VACIO);   
                             } else {
                                //La fecha de traslado no puede ser mayor a la fecha actual y tampoco ser menor que el año de 1960
                                if(entity.getDesFechaDescargo().isAfter(LocalDate.now())) {
                                    ge.addError("desFechaDescargo", Errores.ERROR_FECHA_TRASLADO_MAYOR_FECHA_ACTUAL);
                                } else if(entity.getDesFechaDescargo().getYear() < 1960) {
                                    ge.addError("desFechaDescargo", Errores.ERROR_FECHA_TRASLADO_MENOR_FECHA_LIMITE_MENOR);
                                }
                            }
                         } if(entity.getDesEstadoFk().getEbiCodigo().equals(Constantes.CODIGO_ESTADO_SOLICITUD_RECHAZADA)) {
                            if (entity.getSgAfMotivosRechazoDescargo() == null || entity.getSgAfMotivosRechazoDescargo().isEmpty()) {
                                 ge.addError("afMotivosRechazoDescargo", Errores.ERROR_MOTIVO_RECHAZO_VACIO);
                            } else {
                                 for(SgAfMotivoRechazoDescargo motivo : entity.getSgAfMotivosRechazoDescargo()) {
                                    if(motivo.getMrdMotivoRechazo() == null) {
                                         ge.addError("mrdMotivoRechazo", Errores.ERROR_MOTIVO_RECHAZO_VACIO);
                                         break;
                                    } else if(motivo.getMrdMotivoRechazo().length() > 500) {
                                        ge.addError("mrdMotivoRechazo", Errores.ERROR_LARGO_MOTIVO_RECHAZO_500);
                                    }
                                 }
                            }
                         }
                     } else {
                         ge.addError("ebiCodigo", Errores.ERROR_ESTADO_BIEN_CODIGO_VACIO);
                     }
                 }
            }
            
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    
    public static boolean validarSolicitud(SolicitudDescargo solicitud) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (solicitud == null) {
            ge.addError(Errores.ERROR_SOLICITUD_VACIO);
        } else {
            if (solicitud.getEstado() == null || solicitud.getId() == null) {
                if (solicitud.getId() == null) {
                    ge.addError("solicitud",Errores.ERROR_DESCAGO_VACIO);
                }
                
                if(solicitud.getEstado() == null) {
                    ge.addError("estado",Errores.ERROR_ESTADO_VACIO);
                }
                
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
     }
    
    public static boolean validarSolicitudActaDescargo(SgAfDescargo entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DESCAGO_VACIO);
        } else {
            if (entity.getDesActaDescargo() == null) {
                ge.addError("desActaDescargo",Errores.ERROR_ACTA_DESCARGO_VACIO);
            } else {
                if(entity.getDesActaDescargo().getAdeFechaAcuerdo() == null) {
                    ge.addError("desActaDescargo",Errores.ERROR_ACTA_FECHA_ACUERDO_VACIO);
                }
                if(entity.getDesActaDescargo().getAdeNumeroAcuerdo() == null) {
                    ge.addError("desActaDescargo",Errores.ERROR_ACTA_NUMERO_ACUERDO_VACIO);
                }
                if(entity.getDesActaDescargo().getAdeSeAutoriza()== null) {
                    ge.addError("desActaDescargo",Errores.ERROR_ACTA_SE_AUTORIZA_VACIO);
                }
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
     }
}