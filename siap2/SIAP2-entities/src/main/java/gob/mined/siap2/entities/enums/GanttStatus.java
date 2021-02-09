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
/*
STATUS_ACTIVE >> todas las tareas
STATUS_DONE >> inicio de contratación
STATUS_FAILED >> TDR
*/
public enum GanttStatus {
    STATUS_ACTIVE,
    STATUS_DONE,
    STATUS_FAILED,
    STATUS_SUSPENDED,
    STATUS_UNDEFINED;
}
