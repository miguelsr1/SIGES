/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.GanttStatus;
import gob.mined.siap2.entities.enums.TipoTarea;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_gantt_task")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class GanttTask implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gantt_task")
    @SequenceGenerator(name = "seq_gantt_task", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_gantt_task", allocationSize = 1)
    @Column(name = "gan_id")
    //id: task id
    private Integer id;

    @Column(name = "gan_js_id_tmp")
    private String jsId;

    //name: task name
    @Column(name = "gan_nom")
    private String name;

    @Column(name = "gan_progress")
    private Integer progress;

    @Column(name = "gan_description")
    private String description;

    
    //tipo tarea del metodo de adquisicion  se usa el atributo code para guardar el tipo de tarea   
    @Enumerated(EnumType.STRING)
    @Column(name = "met_tipo_tarea")
    private TipoTarea tipoTarea;

    //level: task indentation level. Root task is 0 (zero)
    @Column(name = "gan_level")
    private Integer level;

    @Enumerated(EnumType.STRING)
    @Column(name = "gan_status")
    private GanttStatus status;

    @Column(name = "gan_depends")
    private String depends;
  
    //start, end: are expressed in milliseconds. “start” is set to the first millisecond of the day, “end” is set to the last millisecond of the day.
    @Column(name = "gan_start")
    private Long start;
    @Column(name = "gan_end")
    private Long end;

    //duration: task duration in days
    @Column(name = "gan_duration")
    private Integer duration;

    //startIsMilestone, endIsMilestone: booleans. Once set to true, task’ start/end can’t move accidentally. You always can change dates directly on the task, but not by acting on children or predecessors.
    @Column(name = "gan_start_in_ms")
    private Boolean startIsMilestone;
    @Column(name = "gan_end_in_ms")
    private Boolean endIsMilestone;

    //assigs: array of  assignment. Each assignment has the following structure:
    @Column(name = "gan_haschild")
    private Boolean hasChild;

    @Column(name = "gan_indice")
    private Integer posicion;

    @Column(name = "gan_hora_fin")
    private String horaFin;
    
    
    //Auditoria
    @Column(name = "gan_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "gan_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "gan_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public Integer getVersion() {
        return version;
    }

    public String getJsId() {
        return jsId;
    }

    public void setJsId(String jsId) {
        this.jsId = jsId;
    }

    public TipoTarea getTipoTarea() {
        return tipoTarea;
    }

    public void setTipoTarea(TipoTarea tipoTarea) {
        this.tipoTarea = tipoTarea;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public GanttStatus getStatus() {
        return status;
    }

    public void setStatus(GanttStatus status) {
        this.status = status;
    }

    public Long getStart() {
        return start;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepends() {
        return depends;
    }

    public void setDepends(String depends) {
        this.depends = depends;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getStartIsMilestone() {
        return startIsMilestone;
    }

    public void setStartIsMilestone(Boolean startIsMilestone) {
        this.startIsMilestone = startIsMilestone;
    }

    public Boolean getEndIsMilestone() {
        return endIsMilestone;
    }

    public void setEndIsMilestone(Boolean endIsMilestone) {
        this.endIsMilestone = endIsMilestone;
    }

    public Boolean getHasChild() {
        return hasChild;
    }

    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
    
    
    // </editor-fold>
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GanttTask other = (GanttTask) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
