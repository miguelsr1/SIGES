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
public enum EstadoActividadPOA {
    FINALIZADA("EstadoActividadPOA.FINALIZADA"),
    NO_FINALIZADA("EstadoActividadPOA.NO_FINALIZADA");

    private String label;

    private EstadoActividadPOA(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
