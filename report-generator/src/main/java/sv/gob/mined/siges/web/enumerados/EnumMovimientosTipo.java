/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumMovimientosTipo {
    I("Ingresos"),
    E("Egresos");

    private final String text;

    private EnumMovimientosTipo(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
