/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumSituacionPlaza {
    ACTIVA("Activa"),
    INACTIVA("Inactiva");

    private final String text;

    private EnumSituacionPlaza(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
