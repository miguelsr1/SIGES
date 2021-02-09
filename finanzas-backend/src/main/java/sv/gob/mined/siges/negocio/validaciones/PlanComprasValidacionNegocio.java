/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import sv.gob.mined.siges.enumerados.EnumPlanCompraEstado;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgEncabezadoPlanCompras;
import sv.gob.mined.siges.persistencia.entidades.SgPlanCompras;

/**
 * Validación de las reglas de negocio del plan de compras
 *
 * @author jgiron
 */
public class PlanComprasValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param com SgPlanCompras
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgPlanCompras com) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (com == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (com.getComMovimientosFk() == null) {
                ge.addError(Errores.ERROR_COMPRA_DESCRIPCION_VACIA);
            }
            if (com.getComInsumoFk() == null) {
                ge.addError(Errores.ERROR_COMPRA_INSUMO_VACIO);
            }
            if (com.getComMetodoFk() == null) {
                ge.addError(Errores.ERROR_COMPRA_METEDO_VACIO);
            }
            if (com.getComCantidad() == null) {
                ge.addError(Errores.ERROR_COMPRA_CANTIDAD_VACIA);
            }
            if (com.getComPrecioUnitario() == null) {
                ge.addError(Errores.ERROR_COMPRA_PRECIO_VACIO);
            }
            if (com.getComFechaEstimadaCompra() == null) {
                ge.addError(Errores.ERROR_COMPRA_FECHA_VACIA);
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
     * @param com SgPlanCompras
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgEncabezadoPlanCompras pla) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (pla == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (pla.getPlaEstado().equals(EnumPlanCompraEstado.OBSERVADO) && pla.getPlaComentario() == null) {
                ge.addError(Errores.ERROR_PLAN_COMPRA_OBSERVACION_VACIA);
            }
        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
