/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum AmbitoFuenteFinanciamiento {
    MINED("MINED"),
    MINED_CEDU("MINED - Centro educativo"),
    CED_EDU("Centro educativo"); 

    private final String text;

    private AmbitoFuenteFinanciamiento(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static class Codes {
        public static final String MINED = "MINED";
        public static final String MINED_CENTRO_EDUCATIVO = "MINED_CEDU";
        public static final String CENTRO_EDUCATIVO = "CED_EDU";
    }
}