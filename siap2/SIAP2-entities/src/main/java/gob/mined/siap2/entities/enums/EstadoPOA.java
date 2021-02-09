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
public enum EstadoPOA {
    EDITANDO_UT("EstadoPOA.EDITANDO_UT"),
    VALIDANDO_POA("EstadoPOA.VALIDANDO_POA"),
    CONSOLIDANDO_POA("EstadoPOA.CONSOLIDANDO_POA"),
    TERMINO_CONSOLIDACION("EstadoPOA.TERMINO_CONSOLIDACION");
    
    
    
    private String label;

    private EstadoPOA(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
