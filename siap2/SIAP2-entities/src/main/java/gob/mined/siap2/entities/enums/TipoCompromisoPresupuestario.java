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
public enum TipoCompromisoPresupuestario {
    PROCESO(Values.PROCESO),
    MODIFICATIVA(Values.MODIFICATIVA);

    private String label;

    private TipoCompromisoPresupuestario(String val) {
        // force equality between name of enum instance, and value of constant
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Uso incorrecto de TipoCompromisoPresupuestario");
        }
        this.label = val;
    }

    public String getLabel() {
        return label;
    }

    public static class Values {
        public static final String PROCESO = "PROCESO";
        public static final String MODIFICATIVA = "MODIFICATIVA";
    }
}
