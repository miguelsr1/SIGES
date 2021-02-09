/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoHabilitacionPeriodoMatricula {
    PENDIENTE("Pendiente"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada");

    private final String text;

    private EnumEstadoHabilitacionPeriodoMatricula(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}