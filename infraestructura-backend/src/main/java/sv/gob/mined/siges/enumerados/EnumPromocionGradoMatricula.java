/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumPromocionGradoMatricula {
    PROMOVIDO("Promovido"),
    NO_PROMOVIDO("No promovido");

    private final String text;

    private EnumPromocionGradoMatricula(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}

