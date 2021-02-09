/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Tipo de movimiento de las cuentas bancarias y la caja chica
 *
 * @author jgiron
 */
public enum TipoMovimiento {
    DEBE("Egreso"),
    HABER("Ingreso");

    private final String text;

    private TipoMovimiento(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
