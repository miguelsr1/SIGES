/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumProcesoCreacion {
    IMP("Importación"),
    MOV("Movil"),
    MIG("Migrado");

    private final String text;

    private EnumProcesoCreacion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
