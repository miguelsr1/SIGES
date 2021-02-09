/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.constantes.Constantes;
import gob.mined.siap2.entities.enums.EstadosPOA;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_poa")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class POA implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "poa_seq_gen")
    @SequenceGenerator(name = "poa_seq_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".poa_seq", allocationSize = 1)
    @Column(name = "poa_id")
    private Integer id;
    
    @Column(name = "poa_nombre")
    private String nombre;
    
    @Column(name = "poa_nombre_busqueda")
    private String nombreBusqueda;
    
    
    @ManyToOne
    @JoinColumn(name = "poa_unidad_tecnica")
    private UnidadTecnica unidadTecnica;
     
    @ManyToOne
    @JoinColumn(name = "poa_prog_institucional")
    private Programa programaInstitucional;
      
    @ManyToOne
    @JoinColumn(name = "poa_anio")
    private AnioFiscal anio;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "poa_estado")
    private EstadosPOA estado;
    
    @Column(name = "poa_fecha_ult_mod_seguimiento")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ultFechaModSeguimiento;
    
    //Auditoria
    @Column(name = "poa_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "poa_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "poa_version")
    @Version
    private Integer version;

    @OneToMany(mappedBy = "poa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<POAMetaAnual> metasAnuales;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            schema = Constantes.SCHEMA_SIAP2,
            name = "ss_rel_poa_riesgos",
            joinColumns = {
                @JoinColumn(name = "poa")},
            inverseJoinColumns = {
                @JoinColumn(name = "poa_riesgo")}
    )
    private List<POARiesgoPOA> riesgos;
    
    private transient String estadoNombre;

    
    @PrePersist
    @PreUpdate
    public void beforeSave() {
        this.nombre = this.nombre != null ? this.nombre.trim() : "";
        this.nombreBusqueda = this.nombre != null ? this.nombre.toLowerCase() : "";
    }
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UnidadTecnica getUnidadTecnica() {
        return unidadTecnica;
    }

    public void setUnidadTecnica(UnidadTecnica unidadTecnica) {
        this.unidadTecnica = unidadTecnica;
    }

    public Programa getProgramaInstitucional() {
        return programaInstitucional;
    }

    public void setProgramaInstitucional(Programa programaInstitucional) {
        this.programaInstitucional = programaInstitucional;
    }

    public AnioFiscal getAnio() {
        return anio;
    }

    public void setAnio(AnioFiscal anio) {
        this.anio = anio;
    }

    public EstadosPOA getEstado() {
        return estado;
    }

    public void setEstado(EstadosPOA estado) {
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

    public String getEstadoNombre() {
        return estadoNombre;
    }

    public void setEstadoNombre(String estadoNombre) {
        this.estadoNombre = estadoNombre;
    }

    public List<POAMetaAnual> getMetasAnuales() {
        return metasAnuales;
    }

    public void setMetasAnuales(List<POAMetaAnual> metasAnuales) {
        this.metasAnuales = metasAnuales;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }
    
     /**
     * Set my transient property at load time based on a calculation,
     * note that a native Hibernate formula mapping is better for this purpose.
     */
    @PostLoad
    public void cargarDatosDefault() {
        if(this.getEstado() != null) {
            this.estadoNombre = this.getEstado().getLabel();
        }
    }

    public List<POARiesgoPOA> getRiesgos() {
        return riesgos;
    }

    public void setRiesgos(List<POARiesgoPOA> riesgos) {
        this.riesgos = riesgos;
    }

    public Date getUltFechaModSeguimiento() {
        return ultFechaModSeguimiento;
    }

    public void setUltFechaModSeguimiento(Date ultFechaModSeguimiento) {
        this.ultFechaModSeguimiento = ultFechaModSeguimiento;
    }

    // </editor-fold>

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final POA other = (POA) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    
    

}
