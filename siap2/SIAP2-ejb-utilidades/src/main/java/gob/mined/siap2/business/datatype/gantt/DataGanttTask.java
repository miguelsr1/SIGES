/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.business.datatype.gantt;

import gob.mined.siap2.entities.enums.GanttStatus;
import java.util.LinkedList;
import java.util.List;

/**
 * Datos de una tarea del diagrama de Gantt.
 *
 * @author Sofis Solutions
 */
public class DataGanttTask {

    //agregado para que no genere siempre una nueva entidad en la base de datos

    private Integer databaseId;
    private Integer databaseVersion;

    private String id;

    private String name;
    private Integer progress = 0;
    private String description;

    private String code;

    private Integer level;
    private GanttStatus status;
    private String depends = "";
    private Boolean canWrite = true;
    private Long start;
    private Integer duration;
    private Long end;
    private Boolean startIsMilestone = true;
    private Boolean endIsMilestone = false;
    private Boolean collapsed = false;

    private List assigs = new LinkedList();
    private Boolean hasChild;
    private String horaFin;

    // <editor-fold defaultstate="collapsed" desc="getter-setter"> 
    /**
     * Id de la tarea
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Id de la tarea
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Nombre de la tarea
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Nombre de la tarea
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Código de la tarea
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * Código de la tarea
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Nivel de anidamiento de la tarea
     *
     * @return
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * Nivel de anidamiento de la tarea 0 es el raiz
     *
     * @param level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * Estado de la tarea
     *
     * @return
     */
    public GanttStatus getStatus() {
        return status;
    }

    public void setStatus(GanttStatus status) {
        this.status = status;
    }

    /**
     * Ver {
     *
     * @see #DataGantt}
     * @return
     */
    public Boolean getCanWrite() {
        return canWrite;
    }

    /**
     * Ver {
     *
     * @see #DataGantt}
     * @param canWrite
     */
    public void setCanWrite(Boolean canWrite) {
        this.canWrite = canWrite;
    }

    /**
     * Fecha de inicio
     *
     * @return
     */
    public Long getStart() {
        return start;
    }

    /**
     * Fecha de inicio
     *
     * @param start
     */
    public void setStart(Long start) {
        this.start = start;
    }

    /**
     * Fecha de finalización
     *
     * @return
     */
    public Long getEnd() {
        return end;
    }

    /**
     * Fecha de finalización
     *
     * @param end
     */
    public void setEnd(Long end) {
        this.end = end;
    }

    /**
     * Duración de la tarea
     *
     * @return
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Duración de la tarea
     *
     * @param duration
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Hito
     *
     * @return
     */
    public Boolean getStartIsMilestone() {
        return startIsMilestone;
    }

    /**
     * Hito
     *
     * @param startIsMilestone
     */
    public void setStartIsMilestone(Boolean startIsMilestone) {
        this.startIsMilestone = startIsMilestone;
    }

    /**
     * Hito
     *
     * @return
     */
    public Boolean getEndIsMilestone() {
        return endIsMilestone;
    }

    /**
     * Hito
     *
     * @param endIsMilestone
     */
    public void setEndIsMilestone(Boolean endIsMilestone) {
        this.endIsMilestone = endIsMilestone;
    }

    /**
     * Indica si está colapsada la tarea
     *
     * @return
     */
    public Boolean getCollapsed() {
        return collapsed;
    }

    /**
     * Indica si está colapsada la tarea
     *
     * @param collapsed
     */
    public void setCollapsed(Boolean collapsed) {
        this.collapsed = collapsed;
    }

    /**
     * array of assignment. Each assignment has the following structure:
     *
     * @return
     */
    public List getAssigs() {
        return assigs;
    }

    /**
     * array of assignment. Each assignment has the following structure:
     *
     * @return
     */
    public void setAssigs(List assigs) {
        this.assigs = assigs;
    }

    /**
     * Indica si tiene subtareas
     *
     * @return
     */
    public Boolean getHasChild() {
        return hasChild;
    }

    /**
     * Indica si tiene subtareas
     *
     * @param hasChild
     */
    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

    /**
     * Progreso de la tarea
     *
     * @return
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * Progreso o avance de la tarea
     *
     * @param progress
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    /**
     * Descripción de la tarea
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Descripción de la tarea
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Dependiente
     *
     * @return
     */
    public String getDepends() {
        return depends;
    }

    /**
     * Dependiente
     *
     * @param depends
     */
    public void setDepends(String depends) {
        this.depends = depends;
    }

    /**
     * Ver {
     *
     * @see #DataGantt}
     * @return
     */
    public Integer getDatabaseId() {
        return databaseId;
    }

    /**
     * Ver {
     *
     * @see #DataGantt}
     * @return
     */
    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;
    }

    /**
     * Ver {
     *
     * @see #DataGantt}
     * @return
     */
    public Integer getDatabaseVersion() {
        return databaseVersion;
    }

    /**
     * Ver {
     *
     * @see #DataGantt}
     * @return
     */
    public void setDatabaseVersion(Integer databaseVersion) {
        this.databaseVersion = databaseVersion;
    }
    
    
    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

     // </editor-fold>

}

