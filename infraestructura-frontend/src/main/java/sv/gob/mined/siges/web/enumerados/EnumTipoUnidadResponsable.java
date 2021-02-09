/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumTipoUnidadResponsable {
    SEDE("Sede"),
    UNIDAD_ADMINISTRATIVA("Unidad Administrativa");
    
    private final String text;

    private EnumTipoUnidadResponsable(final String text) {
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
