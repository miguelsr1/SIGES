/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

/**
 * Tipo de movimientos de cuenta bancaria, caja chica y dirección departamental.
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
