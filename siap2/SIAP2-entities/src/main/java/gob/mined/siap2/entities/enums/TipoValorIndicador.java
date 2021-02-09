/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.enums;

/**
 *
 * @author Sofis Solutions
 */
public enum TipoValorIndicador {
    PRODUCTO(Values.PRODUCTO),
    INDICADOR_PROYECTO(Values.INDICADOR_PROYECTO);

    private String label;

    private TipoValorIndicador(String val) {
        // force equality between name of enum instance, and value of constant
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Uso incorrecto de TipoValorIndicador");
        }
        this.label = val;
    }

    public String getLabel() {
        return label;
    }

    public static class Values {
        public static final String PRODUCTO = "PRODUCTO";
        public static final String INDICADOR_PROYECTO = "INDICADOR_PROYECTO";
    }
}
