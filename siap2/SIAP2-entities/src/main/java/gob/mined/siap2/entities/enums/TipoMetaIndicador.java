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
public enum TipoMetaIndicador {
    META_INDICADOR(Values.META_INDICADOR),
    META_PRODUCTO(Values.META_PRODUCTO);

    private String label;

    private TipoMetaIndicador(String val) {
        // force equality between name of enum instance, and value of constant
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Uso incorrecto de TipoMetaIndicador");
        }
        this.label = val;
    }

    public String getLabel() {
        return label;
    }

    public static class Values {
        public static final String META_INDICADOR = "META_INDICADOR";
        public static final String META_PRODUCTO = "META_PRODUCTO";
    }
}
