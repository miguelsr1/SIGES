/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumRiesgoAlerta {
    MUY_ALTO("Muy alto"),
    ALTO("Alto"),
    MEDIO("Medio"),
    SIN_RIESGO("No aplica");
        
    private final String text;

    private EnumRiesgoAlerta(final String text) {
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
