/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumTipoComponentePentaho {
    TABLERO("Tableros"),
    REPORTE("Reportes"), 
    CUBO("Cubos"); 
    private final String text;

    private EnumTipoComponentePentaho(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
