/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumTipoFiltro {
    SEDE("Sedes"),
    SI("Sistemas Integrados");

    private final String text;

    private EnumTipoFiltro(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
