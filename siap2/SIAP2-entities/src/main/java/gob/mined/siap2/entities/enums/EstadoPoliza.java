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
public enum EstadoPoliza {
    INGRESADA("EstadoPoliza.INGRESADA"),
    APLICADA("EstadoPoliza.APLICADA"),
    REVERTIDA("EstadoPoliza.REVERTIDA");

    private String label;

    
    private EstadoPoliza(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
}
