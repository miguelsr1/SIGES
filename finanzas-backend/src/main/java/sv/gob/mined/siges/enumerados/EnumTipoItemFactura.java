/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Tipos de Item en una Factura
 *
 * @author
 */
public enum EnumTipoItemFactura {
    PLAN_COMPRAS("Plan Compras"),
    MOVIMIENTOS("Movimiento del Presupuesto sin Plan de Compras");

    private final String text;

    private EnumTipoItemFactura(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
