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
public enum EstadoProyecto {
    EN_FORMULACION("EstadoProyecto.EN_FORMULACION"),
    BLOQUEADO("EstadoProyecto.BLOQUEADO");

    private String label;

    private EstadoProyecto(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
     
}
