/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumResolucionCalificacion {
    APROBADO("Aprobado"),
    NO_APROBADO("No aprobado"),
    PENDIENTE("Pendiente");

    private final String text;

    private EnumResolucionCalificacion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}