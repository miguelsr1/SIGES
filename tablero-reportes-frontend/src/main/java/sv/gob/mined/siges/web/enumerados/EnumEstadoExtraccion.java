/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.enumerados;


public enum EnumEstadoExtraccion {
    PENDIENTE("Pendiente"),
    ERROR("Error"),
    FINALIZADA("Finalizada");
    
    private final String text;

    private EnumEstadoExtraccion(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getText() {
        return text;
    }
}
