/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumVariableAlerta {
    EMBARAZO("Embarazo"),
    MADRE("Madre/Padre"),
    ACOMPANIADO("Acompañado"),
    MATRIMONIO("Matrimonio"),
    ASISTENCIA("Asistencia"),
    SOBREEDAD("Sobreedad"),
    TRABAJO("Trabajo"),
    MANIFESTACION_VIOLENCIA("Manifestación Violencia"),
    DESEMPENIO("Desempeño");
    
    private final String text;

    private EnumVariableAlerta(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }
}
