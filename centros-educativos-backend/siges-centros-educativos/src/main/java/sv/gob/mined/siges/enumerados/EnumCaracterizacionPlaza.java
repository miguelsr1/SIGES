/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumCaracterizacionPlaza {
    FIJA("Fija"),
    ROTATIVA("Rotativa");

    private final String text;

    private EnumCaracterizacionPlaza(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
