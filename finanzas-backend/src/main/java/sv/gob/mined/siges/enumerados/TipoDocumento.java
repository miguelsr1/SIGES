/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Estado de los tipos de documento
 *
 * @author jgiron
 */
public enum TipoDocumento {
    CONVENIO("Convenio de Transferencia"),
    CONGELACION("Carta de congelación de fondos");

    private final String text;

    private TipoDocumento(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
