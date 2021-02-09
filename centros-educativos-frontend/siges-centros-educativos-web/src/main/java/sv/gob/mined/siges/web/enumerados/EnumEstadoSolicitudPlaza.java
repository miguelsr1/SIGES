/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoSolicitudPlaza {
    EN_CREACION("En creación"),
    ENVIADA("Enviada"),
    EN_ANALISIS("En análisis"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada"),
    PUBLICADA("Publicada"),
    DESIERTA("Desierta");

    private final String text;

    private EnumEstadoSolicitudPlaza(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
