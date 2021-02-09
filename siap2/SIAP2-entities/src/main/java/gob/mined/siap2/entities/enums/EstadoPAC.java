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
public enum EstadoPAC {
    EN_FORMULACION("EstadoGrupoPAC.EN_FORMULACION"),
    CERRADO ("EstadoGrupoPAC.CERRADO");

    private String label;

    private EstadoPAC(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
}
