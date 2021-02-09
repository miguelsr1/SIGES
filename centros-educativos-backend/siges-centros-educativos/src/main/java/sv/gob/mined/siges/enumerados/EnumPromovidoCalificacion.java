/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumPromovidoCalificacion {
    PROMOVIDO("Promovido"),
    PENDIENTE("Pendiente"),
    NO_PROMOVIDO("No promovido");

    private final String text;

    private EnumPromovidoCalificacion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}

