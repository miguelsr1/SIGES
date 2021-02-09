/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudPlaza;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudPlaza;

public class SolicitudPlazaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param entity SgPlaza
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSolicitudPlaza entity) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (entity == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (entity.getSplTipoPlaza() == null) {
                ge.addError("splTipoPlaza", Errores.ERROR_TIPO_PLAZA_VACIO);
            } else {
                switch (entity.getSplTipoPlaza()) {
                    case D:
                        //Validaciones docente

                        if (entity.getSplFechaHasta() != null && entity.getSplFechaDesde() != null) {
                            if (entity.getSplFechaDesde().isAfter(entity.getSplFechaHasta())) {
                                ge.addError("splFechaDesde", Errores.ERROR_FECHA_DESDE_MAYOR_HASTA);
                            }
                        }
                        if (entity.getSplNivel() == null) {
                            ge.addError("splFechaDesde", Errores.ERROR_NIVEL_VACIO);
                        }
                        if (entity.getSplModalidadEducativa() == null) {
                            ge.addError("splFechaDesde", Errores.ERROR_MODALIDAD_VACIO);
                        }
                        if (entity.getSplModalidadAtencion() == null) {
                            ge.addError("splFechaDesde", Errores.ERROR_MODALIDAD_ATENCION_VACIO);
                        }
                        if (entity.getSplComponentePlanEstudio() == null) {
                            ge.addError("splFechaDesde", Errores.ERROR_COMPONENTE_PLAN_ESTUDIO_VACIO);
                        }

                        break;
                    default:
                        //Validaciones administrativo
                        if (entity.getSplTipoNombramiento() == null) {
                            ge.addError("splTipoNombramiento", Errores.ERROR_TIPO_NOMBRAMIENTO_VACIO);
                        }
                        break;
                }
            }

            if (entity.getSplSedeSolicitante() == null) {
                ge.addError("splSedeSolicitante", Errores.ERROR_SEDE_VACIO);
            }

            if (entity.getSplJornada() == null) {
                ge.addError("splJornada", Errores.ERROR_JORNADA_VACIO);
            }

            if (!StringUtils.isBlank(entity.getSplDescripcion()) && entity.getSplDescripcion().length() > 400) {
                ge.addError("splDescripcion", Errores.ERROR_LARGO_DESCRIPCION_400);
            }
            if (BooleanUtils.isTrue(entity.getSplPlazaExistente())) {
                if (entity.getSplPlazaFk() == null) {
                    ge.addError("splNombre", Errores.ERROR_PLAZA_VACIO);
                }
                if (entity.getSplEstadoPlaza() == null) {
                    ge.addError("splNombre", Errores.ERROR_ESTADO_VACIO);
                }
            }

            if (entity.getSplEstado().equals(EnumEstadoSolicitudPlaza.APROBADA)) {
                
                
                if (BooleanUtils.isFalse(entity.getSplPlazaExistente())) {
                    // 12/05/2020 - Rp indica que si no existe se debe poder aprobar de todas maneras
                    //ge.addError("splPostulacionFin", Errores.ERROR_PLAZA_EXISTIENTE_FALSE);
                } else {
                    if (!StringUtils.isBlank(entity.getSplNombre()) && entity.getSplNombre().length() > 255) {
                        ge.addError("splNombre", Errores.ERROR_LARGO_NOMBRE_255);
                    }

                    if (entity.getSplCaracterizacion() == null) {
                        ge.addError("splCaracterizacion", Errores.ERROR_CARACTERIZACION_VACIO);
                    }
                    if (entity.getSplEstadoPlaza() == null) {
                        ge.addError("splEstadoPlaza", Errores.ERROR_ESTADO_PLAZA_VACIO);
                    }
                    if (entity.getSplPostulacionInicio() == null) {
                        ge.addError("splPostulacionInicio", Errores.ERROR_FECHA_INICIO_VACIA);
                    }
                    if (entity.getSplPostulacionFin() == null) {
                        ge.addError("splPostulacionFin", Errores.ERROR_FECHA_FIN_VACIA);
                    }
                    if (entity.getSplPostulacionInicio() != null && entity.getSplPostulacionFin() != null) {
                        if (entity.getSplPostulacionInicio().isAfter(entity.getSplPostulacionFin())) {
                            ge.addError("splPostulacionFin", Errores.ERROR_FECHA_INICIO_MAYOR_FECHA_FIN);
                        }
                    }
                }

            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
