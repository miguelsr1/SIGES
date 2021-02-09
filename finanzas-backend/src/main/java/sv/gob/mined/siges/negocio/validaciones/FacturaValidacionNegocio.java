/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.negocio.validaciones;

import org.apache.commons.lang3.StringUtils;
import sv.gob.mined.siges.enumerados.EnumTipoDocumentoPago;
import sv.gob.mined.siges.enumerados.EnumTipoItemFactura;
import sv.gob.mined.siges.excepciones.BusinessException;
import sv.gob.mined.siges.negocio.mensajes.Errores;
import sv.gob.mined.siges.persistencia.entidades.SgFactura;

/**
 * Validación de las reglas de negocio de factura
 *
 * @author jgiron
 */
public class FacturaValidacionNegocio {

    /**
     * Valida que los datos del elemento sean correctos
     *
     * @param fac SgFactura
     * @return Boolean
     * @throws BusinessException Devuelve los códigos de error de la validación
     */
    public static boolean validar(SgFactura fac) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (fac == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

            if (fac.getFacTipoDocumento() == null) {
                ge.addError("facTipoDocumento", Errores.ERROR_FACTURA_TIPO_DOC_VACIO);
            }
            if (fac.getFacFechaFactura() == null) {
                ge.addError("facFechaFactura", Errores.ERROR_FACTURA_FECHA_VACIO);
            }
            if (StringUtils.isBlank(fac.getFacNumero())) {
                ge.addError("facNumero", Errores.ERROR_NUMERO_FACTURA_VACIO);
            } else if (fac.getFacNumero().length() > 255) {
                ge.addError("facNumero", Errores.ERROR_NUMERO_FACTURA_45);
            }
            if (fac.getFacSedeFk() == null) {
                ge.addError("facSedeFk", Errores.ERROR_FACTURA_SEDE_VACIO);
            }

            if (fac.getFacTipoItem() == null) {
                ge.addError("facTipoItem", Errores.ERROR_FACTURA_ITEM_VACIO);
            }
            if (fac.getFacTipoItem() != null && fac.getFacTipoItem().equals(EnumTipoItemFactura.MOVIMIENTOS)) {
                if (fac.getFacItemMovimiento() == null) {
                    ge.addError("facItemMovimiento", Errores.ERROR_FACTURA_ITEM_MOVIMIENTOS_VACIO);
                }
            }
            if (fac.getFacTipoItem() != null && fac.getFacTipoItem().equals(EnumTipoItemFactura.PLAN_COMPRAS)) {
                if (fac.getFacItemPlanCompra() == null) {
                    ge.addError("facItemPlanCompra", Errores.ERROR_FACTURA_ITEM_PLAN_VACIO);
                }
            }

            if (fac.getFacSubTotal() == null) {
                ge.addError("facSubTotal", Errores.ERROR_FACTURA_SUBTOTAL_VACIO);
            }
            if (fac.getFacTipoDocumento() != null) {
                if (fac.getFacProveedorFk() == null && !fac.getFacTipoDocumento().equals(EnumTipoDocumentoPago.PLANILLA)) {
                    ge.addError("facProveedorFk", Errores.ERROR_FACTURA_PROVEEDOR_VACIO);
                }
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Valida que los datos del elemento sean correctos antes de anular la
     * factura
     *
     * @param fac SgFactura
     */
    public static boolean anular(SgFactura fac) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (fac == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {
            if (StringUtils.isBlank(fac.getFacNotaCredito())) {
                ge.addError("facNotaCredito", Errores.ERROR_NOTA_CREDITO_VACIA);
            } else if (fac.getFacNumero().length() > 255) {
                ge.addError("facNotaCredito", Errores.ERROR_NOTA_CREDITO_45);
            }
            if (fac.getFacFechaNotaCredito() == null) {
                ge.addError("facFechaNotaCredito", Errores.ERROR_NOTA_CREDITO_FECHA_VACIO);
            }

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }

    /**
     * Valida que los datos del elemento sean correctos antes de pagar la
     * factura
     *
     * @param fac SgFactura
     */
    public static boolean pagar(SgFactura fac) throws BusinessException {
        boolean respuesta = true;
        BusinessException ge = new BusinessException();
        if (fac == null) {
            ge.addError(Errores.ERROR_DATO_VACIO);
        } else {

        }
        if (ge.getErrores().size() > 0) {
            throw ge;
        }
        return respuesta;
    }
}
