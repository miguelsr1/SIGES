/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumSeccionEstado {
    ABIERTA("Abierta"),
    CERRADA("Cerrada");

    private final String text;

    private EnumSeccionEstado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
