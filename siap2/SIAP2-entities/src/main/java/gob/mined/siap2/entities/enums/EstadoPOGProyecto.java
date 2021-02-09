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
public enum EstadoPOGProyecto {
    EDICION_UT("EstadoPOGProyecto.EDICION_UT"),
    CERRADO("EstadoPOGProyecto.CERRADO");

    private String label;

    private EstadoPOGProyecto(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
