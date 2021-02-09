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
public enum TipoAporteProyecto {
    POR_PORCENTAJE(Values.POR_PORCENTAJE),
    POR_TRAMOS(Values.POR_TRAMOS);

    private String label;

    private TipoAporteProyecto(String val) {
        // force equality between name of enum instance, and value of constant
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Uso incorrecto de TipoAporteProyecto");
        }
        this.label = val;
    }

    public String getLabel() {
        return label;
    }

    public static class Values {
        public static final String POR_PORCENTAJE = "POR_PORCENTAJE";
        public static final String POR_TRAMOS = "POR_TRAMOS";
    }
}
