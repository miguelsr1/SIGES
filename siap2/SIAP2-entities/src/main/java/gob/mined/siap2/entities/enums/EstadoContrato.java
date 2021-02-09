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
public enum EstadoContrato {
    EN_CREACION_DENTRO_PROCESO("EstadoContrato.EN_CREACION_DENTRO_PROCESO"),
    PENDIENTE("EstadoContrato.PENDIENTE"),
    EN_EJECUCION("EstadoContrato.EN_EJECUCION"),
    CERRADO("EstadoContrato.CERRADO"),
    RESCINDIDO("EstadoContrato.RESCINDIDO");
   

    private String label;

    private EstadoContrato(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }


}
