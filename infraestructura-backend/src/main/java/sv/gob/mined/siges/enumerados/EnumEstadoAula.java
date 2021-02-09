/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;


public enum EnumEstadoAula {
    MALO("Malo"),
    REGULAR("Regular"),
    BUENO("Bueno");
    
    private final String text;

    private EnumEstadoAula(final String text) {
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
