/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum TipoComponentePlanEstudio {
    ASI("Asignatura"),
    AES("Actividad Especial"),
    AESS("Actividad Especial Sección"),
    ATE("Actividad Tiempo Extendido"),
    IND("Indicadores"),
    MOD("Módulo"),
    PRU("Prueba");

    private final String text;
 
    private TipoComponentePlanEstudio(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static class Codes {

        public static final String ASIGNATURA = "ASI";
        public static final String ACTIVIDAD_ESPECIAL = "AES";
        public static final String ACTIVIDAD_ESPECIAL_SECCION = "AESS";
        public static final String ACTIVIDAD_TIEMPO_EXTENDIDO = "ATE";
        public static final String INDICADORES = "IND";
        public static final String MODULO = "MOD";
        public static final String PRUEBA = "PRU";
    }

    
    
}

