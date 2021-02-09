/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumTipoSolicitudImpresion {
    IMP("Impresión"),   
    REI("Reimpresión"),
    REP("Reposición"),
    SIM("Sin Imprimir");

    private final String text;

    private EnumTipoSolicitudImpresion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

   
}
