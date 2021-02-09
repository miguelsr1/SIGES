/*
 * SIGES
* Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Estado de las transferencias
 *
 * @author sofis-iquezada
 */
public enum EnumTransferenciaEstado {
    AUTORIZADO("Autorizado"),
    SOLICITADO("Solicitado"),
    TRANSFERIDO("Transferido"),
    CERRADO("Cerrado");

    private final String text;

    private EnumTransferenciaEstado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
