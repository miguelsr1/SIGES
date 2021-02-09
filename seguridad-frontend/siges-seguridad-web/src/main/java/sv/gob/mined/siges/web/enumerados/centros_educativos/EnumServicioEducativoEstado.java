/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados.centros_educativos;

public enum EnumServicioEducativoEstado {
    HABILITADO("Habilitado"),
    NO_HABILITADO("No habilitado"),
    EN_PROCESO("En proceso"),
    PENDIENTE("Pendiente");

    private final String text;

    private EnumServicioEducativoEstado(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    
}
