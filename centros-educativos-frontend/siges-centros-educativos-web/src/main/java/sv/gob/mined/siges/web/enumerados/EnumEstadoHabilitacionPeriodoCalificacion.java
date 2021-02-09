/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoHabilitacionPeriodoCalificacion {
    PENDIENTE("Pendiente"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada");

    private final String text;

    private EnumEstadoHabilitacionPeriodoCalificacion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}