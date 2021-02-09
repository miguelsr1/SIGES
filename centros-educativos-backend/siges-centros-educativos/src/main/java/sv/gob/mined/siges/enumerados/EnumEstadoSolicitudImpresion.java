/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumEstadoSolicitudImpresion {
    ENVIADA("Enviada"),
    RECHAZADA("Rechazada"),
    CONFIRMADA("Confirmada"),
    CON_EXCEPCIONES("Con excepciones"),
    PENDIENTE_IMPRESION("Pendiente impresion"),
    ANULADA("Anulada"),
    IMPRESA("Impresa");

    private final String text;

    private EnumEstadoSolicitudImpresion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
