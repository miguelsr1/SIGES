/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientos;

/**
 * Validación de los movimientos del presupuesto escolar.
 *
 * @author Sofis Solutions
 */
public class MovimientosValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mov SgMovimientos
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validarEgresos(SgMovimientos mov) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mov == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (mov.getMovActividadPk() == null) {
                ge.addError("movActividadPk", Errores.ERROR_ACTIVIDAD_VACIO);
            }
            if (StringUtils.isBlank(mov.getMovFuenteRecursos())) {
                ge.addError("movFuenteRecursos", Errores.ERROR_DESCRIPCION_VACIO);
            } else if (mov.getMovFuenteRecursos().length() > 255) {
                ge.addError("movFuenteRecursos", Errores.ERROR_LARGO_DESCRIPCION_255);
            }
            if (mov.getMovFuenteIngresos() == null) {
                ge.addError("movFuenteIngresos", Errores.ERROR_FUENTE_INGRESO_VACIO);
            }
            if (mov.getMovSubAreaInversionPk() == null) {
                ge.addError("movSubAreaInversionPk", Errores.ERROR_SUBAREA_INVERSION_VACIO);
            }
            if (mov.getMovMonto() == null && !mov.getMovPresupuestoFk().getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE)) {
                ge.addError("movMonto", Errores.ERROR_MONTO_VACIO);
            }

            if (mov.getMovMontoAprobado() != null && mov.getMovMontoAprobado().compareTo(BigDecimal.ZERO) < 0) {
                ge.addError("movMontoAprobado", Errores.ERROR_MONTO_NEGATIVO);
            }

            if (mov.getMovMonto() != null && mov.getMovMonto().compareTo(BigDecimal.ZERO) < 0) {
                ge.addError("movMonto", Errores.ERROR_MONTO_NEGATIVO);
            }
        }

        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mov SgMovimientos
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validarIngresos(SgMovimientos mov) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mov == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (mov.getMovRubroPk() == null) {
                ge.addError("movRubroPk", Errores.ERROR_RUBRO_VACIO);
            }
            if (StringUtils.isBlank(mov.getMovFuenteRecursos())) {
                ge.addError("movFuenteRecursos", Errores.ERROR_DESCRIPCION_VACIO);
            } else if (mov.getMovFuenteRecursos().length() > 255) {
                ge.addError("movFuenteRecursos", Errores.ERROR_LARGO_DESCRIPCION_255);
            }
            if (mov.getMovMonto() == null) {
                ge.addError("movMonto", Errores.ERROR_MONTO_VACIO);
            }

        }

        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
