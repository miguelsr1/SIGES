/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumActividadCalendario {
    ACADEMICA("Académica"),
    NO_ACADEMICA("No académica");

    private final String text;

    private EnumActividadCalendario(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}

