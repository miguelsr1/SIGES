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
public enum TipoMontoAccionCentral {
    ACTIVIDAD_ACCION_CENTRAL(Values.ACTIVIDAD_ACCION_CENTRAL),
    PLANIFICACION_ACCION_CENTRAL(Values.PLANIFICACION_ACCION_CENTRAL);

    private String label;

    private TipoMontoAccionCentral(String val) {
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
        public static final String ACTIVIDAD_ACCION_CENTRAL = "ACTIVIDAD_ACCION_CENTRAL";
        public static final String PLANIFICACION_ACCION_CENTRAL = "PLANIFICACION_ACCION_CENTRAL";
    }
}
