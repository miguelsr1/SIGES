/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

/**
 * Tipo de movimiento del presupuesto escolar
 *
 * @author jgiron
 */
public enum EnumTipoMovimientoCajaChica {
    PAGO("Pago"),
    MANUAL("Manual");

    private final String text;

    private EnumTipoMovimientoCajaChica(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
