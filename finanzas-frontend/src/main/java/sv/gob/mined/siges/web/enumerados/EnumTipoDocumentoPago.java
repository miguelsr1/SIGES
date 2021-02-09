/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumTipoDocumentoPago {
    FACTURA("Factura"),
    RECIBO("Recibo"),
    PLANILLA("Planilla");

    private final String text;

    private EnumTipoDocumentoPago(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
