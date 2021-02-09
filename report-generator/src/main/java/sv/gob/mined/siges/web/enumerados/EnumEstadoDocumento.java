/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumEstadoDocumento {
    PENDIENTE("Pendiente"),
    FIRMADO("Firmado"),;
    
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
