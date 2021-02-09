/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.enumerados;

public enum EnumTipoNotificacion {
    CENTRO("Centro"),
    SECCION("Sección"),
    ESTUDIANTE("Estudiante");

    private final String text;

    private EnumTipoNotificacion(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
