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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_programa")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "pro_tipo", discriminatorType = DiscriminatorType.STRING, length = 40)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class Programa implements Serializable {

    /**
     * maximo 31 caractres para nombre de la secuencia *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_programa_gen")
    @SequenceGenerator(name = "seq_programa_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_programa", allocationSize = 1)
    @Column(name = "pro_id")
    private Integer id;

    @Column(name = "pro_nombre")
    private String nombre;

    @Column(name = "pro_nombre_busqueda")
    private String nombreBusqueda;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "pro_estado")
    private EstadoComun estado;

    @ManyToOne
    @JoinColumn(name = "pro_pla_est")
    private PlanificacionEstrategica planificacionEstrategica;
    @OneToMany
    @JoinTable(
        schema = Constantes.SCHEMA_SIAP2,
        name="ss_rel_pro_linea",
        joinColumns={ @JoinColumn(name="pro_id") },
        inverseJoinColumns={ @JoinColumn(name="lin_id") }
    )
    private Set<LineaEstrategica> lineasEstrategicas;
    
    @OneToMany(mappedBy = "programa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Supuesto> supuestos;

    @OneToMany(mappedBy = "programa")
    private List<ProgramaIndicador> indicadoresAsociados;
    
    @Column(name = "pro_codigo")
    private Integer codigo;
    
    //Auditoria
    @Column(name = "pro_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "pro_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "pro_version")
    @Version
    private Integer version;

    
    @PrePersist
    @PreUpdate
    public void beforeSave() {
        this.nombreBusqueda = this.nombre != null ? this.nombre.toLowerCase() : "";
    }
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }
    
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

    public List<ProgramaIndicador> getIndicadoresAsociados() {
        return indicadoresAsociados;
    }

    public void setIndicadoresAsociados(List<ProgramaIndicador> indicadoresAsociados) {
        this.indicadoresAsociados = indicadoresAsociados;
    }

    public List<Supuesto> getSupuestos() {
        return supuestos;
    }

    public PlanificacionEstrategica getPlanificacionEstrategica() {
        return planificacionEstrategica;
    }

    public void setPlanificacionEstrategica(PlanificacionEstrategica planificacionEstrategica) {
        this.planificacionEstrategica = planificacionEstrategica;
    }

    public Set<LineaEstrategica> getLineasEstrategicas() {
        return lineasEstrategicas;
    }

    public void setLineasEstrategicas(Set<LineaEstrategica> lineasEstrategicas) {
        this.lineasEstrategicas = lineasEstrategicas;
    }

    public void setSupuestos(List<Supuesto> supuestos) {
        this.supuestos = supuestos;
    }

    public EstadoComun getEstado() {
        return estado;
    }

    public void setEstado(EstadoComun estado) {
        this.estado = estado;
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

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    // </editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final Programa other = (Programa) obj;
        if (this.id!= null && other.getId()!=null) {
            return Objects.equals(this.id, other.getId());
        }
        return this == other;
    }

}
