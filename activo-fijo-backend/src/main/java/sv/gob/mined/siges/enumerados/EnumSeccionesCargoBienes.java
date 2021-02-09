/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumSeccionesCargoBienes {
    SECCION_BIENES_MUEBLES("BIENES MUEBLES"),
    SECCION_VEHICULOS("VEHICULOS"),
    SECCION_ID_EXTERNO("ID EXTERNO"); 

    private final String text;

    private EnumSeccionesCargoBienes(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
