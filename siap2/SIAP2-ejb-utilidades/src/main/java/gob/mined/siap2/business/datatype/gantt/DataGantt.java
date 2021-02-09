/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype.gantt;

import java.util.LinkedList;
import java.util.List;

/**
 * Datos para el diagrama de Gantt
 *
 * @author Sofis Solutions
 */
public class DataGantt {

    private Integer databaseId;
    private Integer databaseVersion;

    private Boolean canWrite = true;

    private Boolean canWriteOnParent = true;
    private List deletedTaskIds = new LinkedList();

    private Integer selectedRow = 0;

    private List<DataGanttTask> tasks;

     // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    /**
     * Conjunto de tareas del diagrama
     *
     * @return
     */
    public List<DataGanttTask> getTasks() {
        return tasks;
    }

    /**
     * Conjunto de tareas del diagrama
     *
     * @param tasks
     */
    public void setTasks(List<DataGanttTask> tasks) {
        this.tasks = tasks;
    }

    /**
     * Fila que esta en edicion
     *
     * @return
     */
    public Integer getSelectedRow() {
        return selectedRow;
    }

    /**
     * Fila que esta en edicion.
     *
     * @param selectedRow
     */
    public void setSelectedRow(Integer selectedRow) {
        this.selectedRow = selectedRow;
    }

    /**
     *
     * Es un valor booleano que indica si se tiene permisos de
     * escritura/modificación/creación o eliminación de tareas en el proyecto.
     *
     * @return
     */
    public Boolean getCanWrite() {
        return canWrite;
    }

    /**
     *
     * Es un valor booleano que indica si se tiene permisos de
     * escritura/modificación/creación o eliminación de tareas en el proyecto.
     *
     * @param canWrite
     */
    public void setCanWrite(Boolean canWrite) {
        this.canWrite = canWrite;
    }

    /**
     *
     * Este valor es usado para evitar la propagación hacia arriba de una
     * edición.
     *
     * @param canWriteOnParent
     */
    public Boolean getCanWriteOnParent() {
        return canWriteOnParent;
    }

    /**
     *
     * Este valor es usado para evitar la propagación hacia arriba de una
     * edición.
     *
     * @param canWriteOnParent
     */
    public void setCanWriteOnParent(Boolean canWriteOnParent) {
        this.canWriteOnParent = canWriteOnParent;
    }

    /**
     * Valor usado para que no genere siempre una nueva entidad en la base de
     * datos
     *
     * @return
     */
    public Integer getDatabaseId() {
        return databaseId;
    }

    /**
     * Valor usado para que no genere siempre una nueva entidad en la base de
     * datos
     *
     * @param databaseId
     */
    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;
    }

    /**
     * Versión en la base de datos
     *
     * @return
     */
    public Integer getDatabaseVersion() {
        return databaseVersion;
    }

    /**
     * Version en la base de datos
     *
     * @param databaseVersion
     */
    public void setDatabaseVersion(Integer databaseVersion) {
        this.databaseVersion = databaseVersion;
    }

    /**
     * Lista de tareas eliminadas
     *
     * @return
     */
    public List getDeletedTaskIds() {
        return deletedTaskIds;
    }

    /**
     * Lista de tareas eliminadas
     *
     * @param deletedTaskIds
     */
    public void setDeletedTaskIds(List deletedTaskIds) {
        this.deletedTaskIds = deletedTaskIds;
    }

 // </editor-fold>
}
