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
public enum TipoEstructura {
    COMPONENTE(Values.COMPONENTE),
    MACROACTIVIDAD(Values.MACROACTIVIDAD);

    private String label;

    private TipoEstructura(String val) {
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
        public static final String COMPONENTE = "COMPONENTE";
        public static final String MACROACTIVIDAD = "MACROACTIVIDAD";
    }
}
