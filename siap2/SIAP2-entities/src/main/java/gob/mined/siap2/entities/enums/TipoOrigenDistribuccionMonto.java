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
public enum TipoOrigenDistribuccionMonto {
    FUENTE_RECURSO(Values.FUENTE_RECURSO),
    PROYECTO(Values.PROYECTO);

    private String label;

    private TipoOrigenDistribuccionMonto(String val) {
        // force equality between name of enum instance, and value of constant
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Uso incorrecto de TipoOrigenDistribuccionMonto");
        }
        this.label = val;
    }

    public String getLabel() {
        return label;
    }

    public static class Values {
        public static final String FUENTE_RECURSO = "FUENTE_RECURSO";
        public static final String PROYECTO = "PROYECTO";
    }
}
