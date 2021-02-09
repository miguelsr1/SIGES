/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumSeccionesCargoBienes {
    SECCION_BIENES_MUEBLES("BIENES MUEBLES"),
    SECCION_VEHICULOS("VEHICULOS"); 

    private final String text;

    private EnumSeccionesCargoBienes(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
    public static class Codes {

        public static final String SECCION_BM = "SECCION_BIENES_MUEBLES";
        public static final String SECCION_VEH = "SECCION_VEHICULOS";
    }
}
