/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Origen de los movimientos de transferencia
 *
 * @author jgiron
 */
public enum EnumMovimientosOrigen {
    T("Transferencias"),
    P("Propios");

    private final String text;

    private EnumMovimientosOrigen(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
