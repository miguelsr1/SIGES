/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumInmueblePertenece {
    SEDE("Sede"),
    UNIDAD_ADM("U. Adm."),
    OTROS("Otros");

    private final String text;

    private EnumInmueblePertenece(final String text) {
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
