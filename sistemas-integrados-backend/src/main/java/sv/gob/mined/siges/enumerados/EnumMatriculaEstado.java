/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumMatriculaEstado {
    ABIERTO("Abierto"),
    CERRADO("Cerrado");

    private final String text;

    private EnumMatriculaEstado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
