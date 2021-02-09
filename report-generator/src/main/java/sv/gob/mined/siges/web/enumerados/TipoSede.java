/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum TipoSede {
    CED_OFI("Centro educativo oficial"),
    CED_PRI("Centro educativo privado"),
    CIR_INF("Círculo de primera infancia"),
    CIR_ALF("Círculo de alfabetización"),
    UBI_EDUC("Sede EDUCAME");

    private final String text;

    private TipoSede(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static class Codes {

        public static final String CENTRO_EDUCATIVO_OFICIAL = "CED_OFI";
        public static final String CENTRO_EDUCATIVO_PRIVADO = "CED_PRI";
        public static final String CIRCULO_PRIMERA_INFANCIA = "CIR_INF";
        public static final String CIRCULO_ALFABETIZACION = "CIR_ALF";
        public static final String EDUCAME = "UBI_EDUC";
    }
}
