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
public enum EstadoMovimientos {
    TRANSFERENCIA(Values.TRANSFERENCIA),
    INGRESO(Values.INGRESO),
    INGRESO_MODIFICADO(Values.INGRESO_MODIFICADO),
    EGRESO(Values.EGRESO);

    private final String label;

    private EstadoMovimientos(String val) {
        this.label = val;
    }

    public String getLabel() {
        return label;
    }

    public static class Values {
        public static final String TRANSFERENCIA = "T";
        public static final String INGRESO = "I";
        public static final String INGRESO_MODIFICADO = "IM";
        public static final String EGRESO = "E";
    }
}
