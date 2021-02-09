/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumFuenteFinanciamiento {
    MINED("MINED"),
    DEPARTAMENTAL("Departamental"),
    CENTRO_EDUCATIVO("Centro educativo"); 

    private final String text;

    private EnumFuenteFinanciamiento(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static class Codes {
        public static final String MINED = "MINED";
        public static final String DEPARTAMENTAL = "DEPARTAMENTAL";
        public static final String CENTRO_EDUCATIVO = "CED_EDU";
    }
}