/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumEstadoPersona {
    VIVE("Vive"),
    FALLECIDO("Fallecido"),
    AUSENTE("Ausente");

    private final String text;

    private EnumEstadoPersona(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}