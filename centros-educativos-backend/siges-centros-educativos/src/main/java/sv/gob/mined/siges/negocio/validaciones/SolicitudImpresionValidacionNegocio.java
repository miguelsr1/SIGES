/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.BooleanUtils;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudImpresion;
import sv.gob.mined.siges.enumerados.EnumTipoSolicitudImpresion;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEstudianteImpresion;
import sv.gob.mined.siges.persistencia.entidades.SgSolicitudImpresion;

/**
 *
 * @author Sofis Solutions
 */
public class SolicitudImpresionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param sim SgSolicitudImpresion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgSolicitudImpresion sim) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (sim == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (!sim.getSimTipoImpresion().equals(EnumTipoSolicitudImpresion.REP) && !sim.getSimTipoImpresion().equals(EnumTipoSolicitudImpresion.SIM)) {
                if (sim.getSimEstudianteImpresion() != null) {
                    if (sim.getSimEstudianteImpresion().isEmpty()) {
                        ge.addError("simEstudianteImpresion", Errores.ERROR_CANTIDAD_ESTUDIANTES_CERO);
                    }
                }
                if (sim.getSimSeccion() == null) {
                    ge.addError("simSeccion", Errores.ERROR_SECCION_SOLICITUD_IMPRESION_VACIO);
                }
            }
            if (EnumTipoSolicitudImpresion.REI.equals(sim.getSimTipoImpresion())) {
                if (sim.getSimMotivoReimpresion() == null) {
                    ge.addError("simSeccion", Errores.ERROR_MOTIVO_REIMPRESION_VACIO);
                }
            }
            /*if(sim.getSimEstado().equals(EnumEstadoSolicitudImpresion.ENVIADA)){
              if(sim.getSimInicioNumeroCorrelativo()==null){
                  ge.addError("simInicioNumeroCorrelativo", Errores.ERROR_NUMERO_CORRELATIVO);
              }
              if(sim.getSimSelloAutentica()==null){
                  ge.addError("simSelloAutentica", Errores.ERROR_TITULAR_AUTENTICA_VACIO);
              }
              if(sim.getSimSelloDirector()==null){
                  ge.addError("simSelloDirector", Errores.ERROR_DIRECTOR_SELLO_VACIO);
              }
              if(sim.getSimSelloMinistro()==null){
                  ge.addError("simSelloMinistro", Errores.ERROR_MINISTRO_SELLO_VACIO);
              }
          }*/
            if (sim.getSimDefinicionTitulo() == null) {
                ge.addError("simDefinicionTitulo", Errores.ERROR_DEFINICION_TITULO_VACIO);
            }
            if (sim.getSimEstado().equals(EnumEstadoSolicitudImpresion.CONFIRMADA) || sim.getSimEstado().equals(EnumEstadoSolicitudImpresion.CON_EXCEPCIONES)) {
                if (sim.getSimSelloAutentica() == null) {
                    ge.addError("simSelloAutentica", Errores.ERROR_TITULAR_AUTENTICA_VACIO);
                }
                if (sim.getSimFechaValidez() == null) {
                    ge.addError("simSelloAutentica", Errores.ERROR_FECHA_VALIDEZ_VACIO);
                }
                if (sim.getSimEstudianteImpresion()!=null) {
                    for (SgEstudianteImpresion estimp : sim.getSimEstudianteImpresion()) {
                        if (BooleanUtils.isTrue(estimp.getEimAnulada())) {
                            if (estimp.getEimMotivoAnulacion() == null) {
                                ge.addError("simSelloAutentica", Errores.ERROR_ANULACION_SIN_MOTIVO);
                                break;
                            }
                        }
                    }
                }

            }
            if(BooleanUtils.isTrue(sim.getSimTituloEntregadoCentroEducativo()) && sim.getSimFechaEntregadoCentroEducativo()==null){
                ge.addError("simSelloAutentica", Errores.ERROR_FECHA_ENTREGADO_CENTRO_VACIO);
            }
            if(BooleanUtils.isTrue(sim.getSimTituloEntregadoDepartamental()) && sim.getSimFechaEntregadoDepartamental()==null){
                ge.addError("simSelloAutentica", Errores.ERROR_FECHA_ENTREGADO_DEPARTAMENTO_VACIO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Compara dos elementos del tipo. Indica si los elementos contienen la
     * misma información.
     *
     * @param c1 SgSolicitudImpresion
     * @param c2 SgSolicitudImpresion
     * @return Boolean
     */
    public static boolean compararParaGrabar(SgSolicitudImpresion c1, SgSolicitudImpresion c2) {
        boolean respuesta = true;
        if (c1 != null) {
            if (c2 != null) {

            }
        } else {
            respuesta = c2 == null;
        }
        return respuesta;
    }
}
