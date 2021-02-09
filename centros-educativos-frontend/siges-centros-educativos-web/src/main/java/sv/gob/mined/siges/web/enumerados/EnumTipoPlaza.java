/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumTipoPlaza {
    D("Docente"),   
    A("Administrativo");

    private final String text;

    private EnumTipoPlaza(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static class Codes {
        public static final String ADMINISTRATIVO = "A";
        public static final String DOCENTE = "D";
    }
}
