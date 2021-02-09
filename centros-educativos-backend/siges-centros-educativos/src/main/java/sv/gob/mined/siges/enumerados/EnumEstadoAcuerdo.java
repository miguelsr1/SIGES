/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumEstadoAcuerdo {
    VIGENTE("Vigente"),
    NO_VIGENTE("No vigente");

    private final String text;

    private EnumEstadoAcuerdo(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
