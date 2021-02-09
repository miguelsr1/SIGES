/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumTipoRetiroMovimientoCB {
    TRANSFERENCIA("Transferencia"),
    CHEQUE("Cheque");

    private final String text;

    private EnumTipoRetiroMovimientoCB(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
