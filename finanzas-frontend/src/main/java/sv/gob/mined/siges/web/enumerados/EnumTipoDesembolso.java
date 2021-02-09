/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumTipoDesembolso {
    MONTO("Monto Aplicar"),
    PORCENTAJE("Porcentaje Aplicar"),
    REMANENTE("Remanente");

    private final String text;

    private EnumTipoDesembolso(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
