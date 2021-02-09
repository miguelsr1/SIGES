/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumEstadoImportado {
    NO_PROCESADO("No procesado"),
    PROCESADO_EXITO("Proceso de forma correcta"),
    PROCESADO_ERROR("Proceso con error"),
    PROCESAMIENTO_DIRECTO("Procesamiento directo"),
    VALIDACION("Validación");

    private final String text;

    private EnumEstadoImportado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}

