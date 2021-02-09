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
public enum EstadoItem {

    ADJUDICADO("EstadoItem.ADJUDICADO"),
    PAUSA("EstadoItem.PAUSA"),
    DESIERTO("EstadoItem.DESIERTO"),
    SIN_EFECTO("EstadoItem.SIN_EFECTO");

    private String label;

    private EstadoItem(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }


}
