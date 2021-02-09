/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoSolicitudTraslado {
    PENDIENTE("Pendiente"),
    PENDIENTE_RESOLUCION("Pendiente resolución"),
    AUTORIZADA("Autorizada"),
    RECHAZADA("Rechazada"),
    CONFIRMADO("Confirmado"),
    ANULADO("Anulado");

    private final String text;

    private EnumEstadoSolicitudTraslado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
