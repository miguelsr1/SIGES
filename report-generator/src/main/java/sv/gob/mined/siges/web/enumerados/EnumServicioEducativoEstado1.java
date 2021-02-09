/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;

public enum EnumServicioEducativoEstado1 {
    HABILITADO("Habilitado"),
    NO_HABILITADO("No habilitado"),
    EN_PROCESO("En proceso");

    private final String text;

    private EnumServicioEducativoEstado1(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
