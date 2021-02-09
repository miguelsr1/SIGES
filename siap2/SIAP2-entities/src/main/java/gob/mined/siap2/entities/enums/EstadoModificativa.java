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
public enum EstadoModificativa {
    EN_DIGITACION("EstadoModificativa.EN_DIGITACION"),
    APROBADA("EstadoModificativa.APROBADA"),
    NO_APROBADA("EstadoModificativa.NO_APROBADA");

    private String label;
    private String abrev;

    private EstadoModificativa(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    


}
