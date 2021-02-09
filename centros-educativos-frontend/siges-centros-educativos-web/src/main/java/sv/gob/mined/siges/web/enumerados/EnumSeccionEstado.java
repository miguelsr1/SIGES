/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumSeccionEstado {
    ABIERTA("Abierta"),
    CERRADA("Cerrada");

    private final String text;

    private EnumSeccionEstado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
    public static class Codes {
        public static final String ABIERTA = "ABIERTA";
        public static final String CERRADA = "CERRADA";
    }

}
