/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumMovimientosTipo;
import sv.gob.mined.siges.enumerados.EnumPresupuestoEscolarEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientosEdicion;

/**
 * Validación de los movimientos del presupuesto escolar.
 *
 * @author Sofis Solutions
 */
public class MovimientosEdicionValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param mov SgMovimientosEdicion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validarEgresos(SgMovimientosEdicion mov) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mov == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (mov.getMovSubAreaInversionPk() == null) {
                ge.addError("movSubAreaInversionPk", Errores.ERROR_SUBAREA_INVERSION_VACIO);
            }
            if (mov.getMovFuenteIngresosOriginal() == null && mov.getMovTipo().equals(EnumMovimientosTipo.I)) {
                ge.addError("movFuenteIngresos", Errores.ERROR_FUENTE_INGRESO_VACIO);
            }

            if (mov.getMovFuenteIngresos() == null && mov.getMovTipo().equals(EnumMovimientosTipo.IM)) {
                ge.addError("movFuenteIngresos", Errores.ERROR_FUENTE_INGRESO_VACIO);
            }
            if (mov.getMovActividadPk() == null) {
                ge.addError("movActividadPk", Errores.ERROR_ACTIVIDAD_VACIO);
            }
            if (mov.getMovMonto() == null && !mov.getMovPresupuestoFk().getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE)) {
                ge.addError("movMonto", Errores.ERROR_MONTO_VACIO);
            }
            if (mov.getMovMontoAprobado() == BigDecimal.ZERO && mov.getMovPresupuestoFk().getPesEstado().equals(EnumPresupuestoEscolarEstado.EN_AJUSTE)) {
                ge.addError("movMontoAprobado", Errores.ERROR_MONTO_APROBADO_CERO);
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
     * @param mov SgMovimientosEdicion
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validarIngresos(SgMovimientosEdicion mov) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (mov == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (mov.getMovMonto() == null) {
                ge.addError("movMonto", Errores.ERROR_MONTO_VACIO);
            }
            if (mov.getMovRubroPk() == null) {
                ge.addError("movRubroPk", Errores.ERROR_RUBRO_VACIO);
            }
            if (StringUtils.isBlank(mov.getMovFuenteRecursos())) {
                ge.addError("movFuenteRecursos", Errores.ERROR_TITULAR_VACIO);
            } else if (mov.getMovFuenteRecursos().length() > 255) {
                ge.addError("movFuenteRecursos", Errores.ERROR_TITULAR_250);
            }
        }

        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

}
