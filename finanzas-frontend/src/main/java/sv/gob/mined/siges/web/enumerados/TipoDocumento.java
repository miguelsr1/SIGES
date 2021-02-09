/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

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
