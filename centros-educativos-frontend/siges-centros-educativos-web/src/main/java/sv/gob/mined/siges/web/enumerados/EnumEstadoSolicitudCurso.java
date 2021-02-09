/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumEstadoSolicitudCurso {
    CREADA("Creada"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada");

    private final String text;

    private EnumEstadoSolicitudCurso(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}