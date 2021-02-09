/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumEstados {
    CARGO("Cargo"),
    DESCARGO("Descargo"),
    TRASLADO("Traslado");
    
    private final String text;

    private EnumEstados(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }
}
