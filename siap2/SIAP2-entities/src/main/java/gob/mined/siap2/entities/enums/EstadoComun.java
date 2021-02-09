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
public enum EstadoComun {
    NO_VIGENTE("EstadoComun.NO_VIGENTE"),
    VIGENTE("EstadoComun.VIGENTE");

    private String label;

    private EstadoComun(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
