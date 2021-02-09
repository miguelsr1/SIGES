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
public enum TipoMontoEstructura {    
    PORCENTAJE(Values.PORCENTAJE),
    IMPORTE(Values.IMPORTE);

    private String label;

    private TipoMontoEstructura(String val) {
        // force equality between name of enum instance, and value of constant
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Uso incorrecto de TipoMontoEstructura");
        }
        this.label = val;
    }

    public String getLabel() {
        return label;
    }

    public static class Values {
        public static final String IMPORTE = "IMPORTE";
        public static final String PORCENTAJE = "PORCENTAJE";
    }
}
