/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.time.temporal.ChronoUnit;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgCalendario;

/**
 *
 * @author Sofis Solutions
 */
public class CalendarioValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param cal SgCalendario
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgCalendario cal) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (cal == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(cal.getCalCodigo())) {
                ge.addError("calCodigo", Errores.ERROR_CODIGO_VACIO);
            } else if (cal.getCalCodigo().length() > 4) {
                ge.addError("calCodigo", Errores.ERROR_LARGO_CODIGO_4);
            }
            if (StringUtils.isBlank(cal.getCalNombre())) {
                ge.addError("calNombre", Errores.ERROR_NOMBRE_VACIO);
            } else if (cal.getCalNombre().length() > 255) {
                ge.addError("calNombre", Errores.ERROR_LARGO_NOMBRE_255);
            }
            if (cal.getCalTipoCalendario() == null) {
                ge.addError("calTipo", Errores.ERROR_TIPO_CALENDARIO_VACIO);
            }
            if (cal.getCalAnioLectivo() == null) {
                ge.addError("calAnio_lectivo", Errores.ERROR_ANIO_LECTIVO_VACIO);
            }
            if (cal.getCalFechaInicio() == null) {
                ge.addError("calDesde", Errores.ERROR_FECHA_INICIO_VACIA);
            }
            if ( cal.getCalFechaFin() == null) {
                ge.addError("calHasta", Errores.ERROR_FECHA_FIN_VACIA);
            }
            if (cal.getCalFechaFin() != null && cal.getCalFechaInicio() != null) {
                if (cal.getCalFechaInicio().isAfter(cal.getCalFechaFin())) {
                    ge.addError("calHasta", Errores.ERROR_RANGO_DE_FECHAS);
                }
                if(cal.getCalTipoCalendario()!=null && !cal.getCalTipoCalendario().getTceCodigo().equals(Constantes.CALENDARIO_ALFABETIZACION)){
                    if (ChronoUnit.DAYS.between(cal.getCalFechaInicio(), cal.getCalFechaFin()) > 367) {
                        ge.addError("calHasta", Errores.ERROR_RANGO_SUPERA_MAXIMO);
                    }
                }
                if (cal.getCalTipoCalendario()!=null && cal.getCalAnioLectivo() != null && cal.getCalCodigo() != null) {
                    if (cal.getCalTipoCalendario().getTceCodigo().equals(Constantes.CALENDARIO_NACIONAL)) {
                        if (cal.getCalFechaFin().getYear() != cal.getCalAnioLectivo().getAleAnio() || cal.getCalFechaInicio().getYear() != cal.getCalAnioLectivo().getAleAnio()) {
                            ge.addError("calHasta", Errores.ERROR_RANGO_DE_FECHAS_NO_COINCIDE_ANIO);
                        }
                    }
                    if (cal.getCalTipoCalendario().getTceCodigo().equals(Constantes.CALENDARIO_INTERNACIONAL)) {
                        if (cal.getCalFechaInicio().getYear() != cal.getCalAnioLectivo().getAleAnio() || cal.getCalFechaFin().getYear() != (cal.getCalAnioLectivo().getAleAnio() + 1)) {
                            ge.addError("calHasta", Errores.ERROR_RANGO_DE_FECHAS_NO_COINCIDE_ANIO_INTERACIONAL);
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
