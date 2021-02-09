/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum TipoUnidad {
    UNIDAD_ADMINISTRATIVA("Unidad Administrativa"),
    CENTRO_ESCOLAR("Centro Escolar");

    private final String text;

    private TipoUnidad(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
    public static class Codes {
        public static final String UNIDAD_ADMINISTRATIVA = "UA";
        public static final String CENTRO_ESCOLAR = "CE";
    }
}
