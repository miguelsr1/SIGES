/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados.centros_educativos;

public enum EnumServicioEducativo {
    HABILITADO("Habilitado"),
    NO_HABILITADO("No habilitado"),
    EN_PROCESO("En proceso"),
    PENDIENTE("Pendiente");

    private final String text;

    private EnumServicioEducativo(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
