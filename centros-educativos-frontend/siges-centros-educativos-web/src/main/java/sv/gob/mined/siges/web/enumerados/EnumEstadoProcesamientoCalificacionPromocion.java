/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoProcesamientoCalificacionPromocion {
    SIN_PROCESAR("Sin procesar"),
    PROCESADO("Procesado"),
    PROCESADO_ERROR("Procesado con errores");

    private final String text;

    private EnumEstadoProcesamientoCalificacionPromocion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}

