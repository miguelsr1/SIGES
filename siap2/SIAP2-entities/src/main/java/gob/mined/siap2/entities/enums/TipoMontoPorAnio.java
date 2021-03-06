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
public enum TipoMontoPorAnio {
    ACCION_CENTRAL(Values.ACCION_CENTRAL),
    ASIGN_NO_PROGRAMABLE(Values.ASIGN_NO_PROGRAMABLE);

    private String label;

    private TipoMontoPorAnio(String val) {
        // force equality between name of enum instance, and value of constant
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Uso incorrecto de TipoMontoPorAnio");
        }
        this.label = val;
    }

    public String getLabel() {
        return label;
    }

    public static class Values {
        public static final String ACCION_CENTRAL = "ACCION_CENTRAL";
        public static final String ASIGN_NO_PROGRAMABLE = "ASIGN_NO_PROGRAMABLE";
    }
}
