/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumTipoBeneficio {
    SEDE_EDUCATIVA("Sede educativa"),
    ESTUDIANTE("Estudiante");

    private final String text;

    private EnumTipoBeneficio(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}