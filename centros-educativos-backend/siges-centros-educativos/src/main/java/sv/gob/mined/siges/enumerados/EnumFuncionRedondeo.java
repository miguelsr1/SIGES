/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumFuncionRedondeo {
        RED("Redondeo"),
        PIS("Piso"),
        TEC("Techo");

    private final String text;

    private EnumFuncionRedondeo(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
