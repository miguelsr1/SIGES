/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumEstadosProceso {
    PENDIENTE("PENDIENTE"),
    EN_PROCESO("EN PROCESO"),
    PROCESADO("PROCESADO"),
    PROCESADO_CON_ERROR("PROCESADO_CON_ERROR"); 

    private final String text;

    private EnumEstadosProceso(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
