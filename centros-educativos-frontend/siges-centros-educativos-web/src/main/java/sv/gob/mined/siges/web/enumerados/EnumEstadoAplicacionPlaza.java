/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoAplicacionPlaza {
    APLICADO("Aplicado"),
    ANALISIS("Análisis"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada");

    private final String text;

    private EnumEstadoAplicacionPlaza(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
