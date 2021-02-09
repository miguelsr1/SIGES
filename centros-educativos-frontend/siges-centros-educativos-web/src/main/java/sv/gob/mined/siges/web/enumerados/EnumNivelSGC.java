/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumNivelSGC {
    BAJO("Bajo"),
    MEDIO("Medio"),
    ALTO("Alto");

    private final String text;

    private EnumNivelSGC(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
