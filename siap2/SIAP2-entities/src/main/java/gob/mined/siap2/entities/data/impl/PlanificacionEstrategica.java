/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.EstadoComun;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_pla_estrategica")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class PlanificacionEstrategica implements Serializable {

    /**
     * maximo 31 caractres para nombre de la secuencia *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_plan_est_gen")
    @SequenceGenerator(name = "seq_plan_est_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_plan_est", allocationSize = 1)
    @Column(name = "pla_id")
    private Integer id;

    @Column(name = "pla_nombre")
    private String nombre;

    @Lob
    @Column(name = "pla_objetivo")
    private String objetivo;

    @Column(name = "pla_desde")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date desde;

    @Column(name = "pla_hasta")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date hasta;

    @Enumerated(EnumType.STRING)
    @Column(name = "pla_estado")
    private EstadoComun estado;

    @ManyToMany
    @JoinTable(
        schema = Constantes.SCHEMA_SIAP2,
        name="ss_rel_pla_linea",
        joinColumns={ @JoinColumn(name="pla_id") },
        inverseJoinColumns={ @JoinColumn(name="lin_id") }
    )
    private Set<LineaEstrategica> lineasEstrategicas;
    
    
    @OneToMany(mappedBy = "planificacion", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "indice")
    private List<TechoPlanificacionAccionCentral> techosAccionCentral;
  
    
    //Auditoria
    @Column(name = "pla_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pla_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pla_version")
    @Version
    private Integer version;

    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    
    public EstadoComun getEstado() {
        return estado;
    }

    public void setEstado(EstadoComun estado) {
        this.estado = estado;
    }

    public List<TechoPlanificacionAccionCentral> getTechosAccionCentral() {
        return techosAccionCentral;
    }

    public void setTechosAccionCentral(List<TechoPlanificacionAccionCentral> techosAccionCentral) {
        this.techosAccionCentral = techosAccionCentral;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
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

    public Set<LineaEstrategica> getLineasEstrategicas() {
        return lineasEstrategicas;
    }

    public void setLineasEstrategicas(Set<LineaEstrategica> lineasEstrategicas) {
        this.lineasEstrategicas = lineasEstrategicas;
    }
    
    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    // </editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PlanificacionEstrategica other = (PlanificacionEstrategica) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
