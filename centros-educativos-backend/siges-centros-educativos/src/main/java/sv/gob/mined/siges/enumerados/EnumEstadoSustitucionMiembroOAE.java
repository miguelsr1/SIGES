/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumEstadoSustitucionMiembroOAE {
    PENDIENTE("Pendiente"),
    APROBADO("Aprobado"),
    RECHAZADO("Rechazado");
    
    private final String text;

    private EnumEstadoSustitucionMiembroOAE(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
