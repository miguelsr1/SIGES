/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumServiciosRest {
    SERVICIO_CATALOGO("catalogo"),
    SEGURIDAD("seguridad"),    
    CENTROS_EDUCATIVOS("centros-educativos");

    private final String text;

    private EnumServiciosRest(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
