/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumEstadoHabilitacionPeriodoMatricula;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgHabilitacionPeriodoMatricula;

/**
 *
 * @author Sofis Solutions
 */
public class HabilitacionPeriodoMatriculaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param hmp SgHabilitacionPeriodoMatricula
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgHabilitacionPeriodoMatricula hmp) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (hmp == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (hmp.getHmpSedeFk() == null) {
                ge.addError("hmpCodigo", Errores.ERROR_SEDE_VACIO);
            }
            if (hmp.getHmpEstado() == null) {
                ge.addError("hmpCodigo", Errores.ERROR_ESTADO_VACIO);
            } else {
                if (EnumEstadoHabilitacionPeriodoMatricula.APROBADA.equals(hmp.getHmpEstado())) {
                    if (hmp.getHmpFechaDesde() == null) {
                        ge.addError("acaFechaDesde", Errores.ERROR_FECHA_DESDE_VACIO);
                    }
                    if (hmp.getHmpFechaHasta() == null) {
                        ge.addError("acaFechaDesde", Errores.ERROR_FECHA_HASTA_VACIO);
                    }

                    if (hmp.getHmpFechaDesde() != null && hmp.getHmpFechaHasta() != null) {
                        if (hmp.getHmpFechaDesde().isAfter(hmp.getHmpFechaHasta())) {
                            ge.addError("acaFechaDesde", Errores.ERROR_FECHA_DESDE_MAYOR_HASTA);
                        }
                    }
                }
                if (EnumEstadoHabilitacionPeriodoMatricula.RECHAZADA.equals(hmp.getHmpEstado())) {
                    if(StringUtils.isBlank(hmp.getHmpObservacionAprobacionRechazo())){
                         ge.addError("acaFechaDesde", Errores.ERROR_OBSERVACION_VACIO);
                    }
                }
            }
            if(hmp.getHmpRelHabilitacionMatriculaNivel()==null || hmp.getHmpRelHabilitacionMatriculaNivel().isEmpty()){
                 ge.addError("acaFechaDesde", Errores.ERROR_NIVEL_NO_SELECCIONADO);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
