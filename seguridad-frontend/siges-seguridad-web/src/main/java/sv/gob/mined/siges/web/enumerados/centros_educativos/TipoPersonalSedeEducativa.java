/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados.centros_educativos;

public enum TipoPersonalSedeEducativa {
    ADM("Administrativo"),   
    ATPI("Asistente Técnico Primera Infancia "),
    DOC("Docente");

    private final String text;

    private TipoPersonalSedeEducativa(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static class Codes {
        public static final String ADMINISTRATIVO = "ADM";
        public static final String ASISTENTE_TECNICO_PRIMERA_INFANCIA = "ATPI";
        public static final String DOCENTE = "DOC";
    }
}
