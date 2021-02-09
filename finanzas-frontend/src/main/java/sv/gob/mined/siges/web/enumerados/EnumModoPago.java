/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

/**
 * Tipo de movimiento de las cuentas bancarias y la caja chica
 *
 * @author jgiron
 */
public enum EnumModoPago {
    CHEQUE("Cheque"),
    EFECTIVO("Efectivo");

    private final String text;

    private EnumModoPago(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
