/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumTipoPeriodoSeccion {
    ANUAL("Anual"),
    COHORTE("Cohorte");

    private final String text;

    private EnumTipoPeriodoSeccion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
