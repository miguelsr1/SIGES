/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum TipoValoracion {
    PUBLICO("Público"),
    PRIVADO("Privado");

    private final String text;

    private TipoValoracion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
