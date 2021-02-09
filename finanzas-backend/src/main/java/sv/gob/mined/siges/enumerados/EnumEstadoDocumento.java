/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

/**
 * Estado de los documentos generados por el módulo
 *
 * @author jgiron
 */
public enum EnumEstadoDocumento {
    PENDIENTE("Pendiente"),
    GENERADO("Generado"),
    FIRMADO("Firmado");

    private final String text;

    private EnumEstadoDocumento(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }
}
