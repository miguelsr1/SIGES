/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.enums.TipoMontoPorAnio;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_con_monto_por_anio")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "con_tipo", discriminatorType = DiscriminatorType.STRING, length = 25)
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public abstract class ConMontoPorAnio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_con_m_por_anio_gen")
    @SequenceGenerator(name = "seq_con_m_por_anio_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_con_m_por_anio", allocationSize = 1)
    @Column(name = "con_id")
    protected Integer id;

    @Column(name = "con_nombre")
    private String nombre;

    @Lob
    @Column(name = "con_descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "con_pla_est")
    private PlanificacionEstrategica planificacionEstrategica;
    @OneToMany
    @JoinTable(
        schema = Constantes.SCHEMA_SIAP2,
        name="ss_rel_con_linea",
        joinColumns={ @JoinColumn(name="pro_id") },
        inverseJoinColumns={ @JoinColumn(name="lin_id") }
    )
    private Set<LineaEstrategica> lineasEstrategicas;
  
    
    @ManyToOne
    @JoinColumn(name="con_clasificador_funcional")
    private ClasificadorFuncional clasificadorFuncional;
    
    
        
    @ManyToOne
    @JoinColumn(name="con_unidad_tecnica")
    private UnidadTecnica unidadTecnica;
    
    
    
    // update/insert is managed by discriminator mechanics
    @Column(name = "con_tipo", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TipoMontoPorAnio tipo;

    @OneToMany(mappedBy = "conMontoPorAnio", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<POAConActividades> poas;
    
    @Column(name = "con_codigo")
    private Integer codigo;
    

    //Auditoria
    @Column(name = "con_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "con_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "con_version")
    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "con_pac")
    private PAC pac;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ClasificadorFuncional getClasificadorFuncional() {
        return clasificadorFuncional;
    }

    public void setClasificadorFuncional(ClasificadorFuncional clasificadorFuncional) {
        this.clasificadorFuncional = clasificadorFuncional;
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

    public TipoMontoPorAnio getTipo() {
        return tipo;
    }

    public void setTipo(TipoMontoPorAnio tipo) {
        this.tipo = tipo;
    }

    public PAC getPac() {
        return pac;
    }

    public void setPac(PAC pac) {
        this.pac = pac;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public Set<POAConActividades> getPoas() {
        return poas;
    }

    public void setPoas(Set<POAConActividades> poas) {
        this.poas = poas;
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
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final ConMontoPorAnio other = (ConMontoPorAnio) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
