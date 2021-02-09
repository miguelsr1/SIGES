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
public enum TipoTarea {
    INICIALIZACION("TipoTarea.INICIALIZACION"),
    RECEPCION_DE_TDR_O_ESPECIFICACIONES_TECNICAS("TipoTarea.RECEPCION_DE_TDR-ESPECIFICACIONES_TECNICAS"),
    REVISION_DE_TDR_O_ET("TipoTarea.REVISION_DE_TDR-ET"),//Este no es un estado del proceso de adquisición
    REVISION_JEFE_UACI("TipoTarea.REVISION_JEFE_DEPARTAMENTO"),
    REVISION_TECNICO_UACI("TipoTarea.REVISION_TECNICO_UACI"),
    INVITACION_O_PUBLICACION("TipoTarea.INVITACION-PUBLICACION"),
    RECEPCION_DE_OFERTAS("TipoTarea.RECEPCION_DE_OFERTAS"),
    EVALUACION("TipoTarea.EVALUACION"),
    ADJUDICACION("TipoTarea.ADJUDICACION"),
    CONTRATACION("TipoTarea.CONTRATACION"),//Este no es un estado del proceso de adquisición
    CONTRATADO("TipoTarea.CONTRATADO"),//Este no es un estado del proceso de adquisición
    COMPROMISO_PRESUPUESTARIO("TipoTarea.COMPROMISO_PRESUPUESTARIO"),
    CONTRATO_ORDEN_DE_COMPRA("TipoTarea.CONTRATO_ORDEN_DE_COMPRA"),
    ORDEN_DE_INICIO("TipoTarea.ORDEN_DE_INICIO"),
    CERRADO_PROCESO("TipoTarea.CERRADO_PROCESO");//Este no es un estado del proceso de adquisición

    private String label;

    private TipoTarea(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
