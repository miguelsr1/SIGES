/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoArchivoCalificacionPAES {
    SIN_PROCESAR("Sin procesar"),
    PROCESADO("Procesado"),
    PROCESADO_ERROR("Procesado con errores");

    private final String text;

    private EnumEstadoArchivoCalificacionPAES(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
