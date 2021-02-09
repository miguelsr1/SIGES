/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumOperaciones {
    INSERT("I"),
    UPDATE("U"),
    DELETE("D");
    
    private final String text;

    private EnumOperaciones(final String text) {
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
