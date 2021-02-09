/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumVariableAlerta {
    EMBARAZO("Embarazo"),
    MADRE("Madre"),
    ACOMPANIADO("Acompañado"),
    MATRIMONIO("Matrimonio");
    
    private final String text;

    private EnumVariableAlerta(final String text) {
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
