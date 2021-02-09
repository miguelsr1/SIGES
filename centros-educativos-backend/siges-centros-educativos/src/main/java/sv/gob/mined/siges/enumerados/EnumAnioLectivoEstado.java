/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumAnioLectivoEstado {
    ABIERTO("Abierto"),
    CERRADO("Cerrado");

    private final String text;

    private EnumAnioLectivoEstado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
