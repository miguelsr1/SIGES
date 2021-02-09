/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoSolicitudOAE {
    FINALIZADA("Finalizada"),
    ENVIADA("Enviada");

    private final String text;

    private EnumEstadoSolicitudOAE(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
