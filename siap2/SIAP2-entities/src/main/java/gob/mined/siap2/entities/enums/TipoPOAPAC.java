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
public enum TipoPOAPAC {
    POA_PROYECTO(Values.POA_PROYECTO),
    POA_ACCION_CENTRAL(Values.POA_ACCION_CENTRAL),
    POA_ASIGNACION_NO_PROGRAMABLE(Values.POA_ASIGNACION_NO_PROGRAMABLE);

    private String label;

    private TipoPOAPAC(String val) {
        // force equality between name of enum instance, and value of constant
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Uso incorrecto de TipoPOA");
        }
        this.label = val;
    }

    public String getLabel() {
        return label;
    }

    public static class Values {
        public static final String POA_PROYECTO = "POA_PROYECTO";
        public static final String POA_ACCION_CENTRAL = "POA_ACCION_CENTRAL";
        public static final String POA_ASIGNACION_NO_PROGRAMABLE = "POA_ASIGNACION_NO_PROGRAMABLE";
    }
}
