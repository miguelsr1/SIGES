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
public enum TipoPOA {
    POA_PROYECTO(Values.POA_PROYECTO),
    POA_CON_ACTIVIDADES(Values.POA_CON_ACTIVIDADES);

    private String label;

    private TipoPOA(String val) {
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
        public static final String POA_CON_ACTIVIDADES = "POA_CON_ACTIVIDADES";
    }
}
