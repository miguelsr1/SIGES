/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumConsultaGuardada {
    ESTUDIANTE("estudiante");

    private final String text;

    private EnumConsultaGuardada(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
