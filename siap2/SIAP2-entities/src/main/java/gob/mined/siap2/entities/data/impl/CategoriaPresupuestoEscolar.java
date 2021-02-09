package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import java.util.List;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_categoria_presupuesto_escolar")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
@Cacheable(false)
public class CategoriaPresupuestoEscolar implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_cat_presupuesto_escolar", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_cat_presupuesto_escolar", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cat_presupuesto_escolar")
    @Column(name = "cpe_id")
    private Integer id;
    
    @Column(name = "cpe_codigo")
    private String codigo;
    
    @Column(name = "cpe_nombre")
    private String nombre;
    
    @Column(name = "cpe_nombre_busqueda")
    private String nombreBusqueda;
    
    @Column(name = "cpe_habilitado")
    private Boolean habilitado;

    //Auditoria
    @Column(name = "cpe_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "cpe_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;
    
    @Column(name = "cpe_version")
    @Version
    private Integer version;
    
    private transient String codigoNombre;
    
    @OneToMany(mappedBy = "categoriaPresupuestoEscolar")
    private List<ComponentePresupuestoEscolar> subComponentes;

    public CategoriaPresupuestoEscolar() {
        if(this.id != null) {
            this.codigo = StringUtils.leftPad(String.valueOf(this.id), 3, "0");
        }
    }
    
    
    
    @PrePersist
    @PreUpdate
    @PostLoad
    public void saveChanges() {
        if(this.id != null) {
            this.codigo = StringUtils.leftPad(String.valueOf(this.id), 3, "0");
        }
        this.nombreBusqueda = this.nombre != null ? this.nombre.toLowerCase().trim() : "";
    }
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

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
        final CategoriaPresupuestoEscolar other = (CategoriaPresupuestoEscolar) obj;
        if (this.id!= null && other.getId()!=null) {
            return Objects.equals(this.id, other.getId());
        }
        return this == other;
    }

    @Override
    public String toString() {
        return "CategoriaPresupuestoEscolar{" + "id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + ", nombreBusqueda=" + nombreBusqueda + ", habilitado=" + habilitado + ", ultMod=" + ultMod + ", ultUsuario=" + ultUsuario + ", version=" + version + ", codigoNombre=" + codigoNombre + ", subComponentes=" + subComponentes + '}';
    }

    

    public List<ComponentePresupuestoEscolar> getSubComponentes() {
        return subComponentes;
    }

    public void setSubComponentes(List<ComponentePresupuestoEscolar> subComponentes) {
        this.subComponentes = subComponentes;
    }

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }

    public String getCodigoNombre() {
        if(StringUtils.isBlank(codigoNombre)) {
            this.codigoNombre = this.codigo;
            if(StringUtils.isNotBlank(this.nombre)) {
                if(StringUtils.isNotBlank(codigoNombre)) {
                   this.codigoNombre += "-" + this.nombre; 
                } else {
                    this.codigoNombre = this.nombre;
                }
                
            }
            this.codigoNombre = this.codigoNombre.trim();
        }
        return codigoNombre;
    }

    public void setCodigoNombre(String codigoNombre) {
        this.codigoNombre = codigoNombre;
    }
}
