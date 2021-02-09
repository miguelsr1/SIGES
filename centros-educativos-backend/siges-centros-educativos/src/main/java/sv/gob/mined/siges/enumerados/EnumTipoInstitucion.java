/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumTipoInstitucion {
    PRIVADO("Privado"),
    PUBLICO("Publico");

    private final String text;

    private EnumTipoInstitucion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
