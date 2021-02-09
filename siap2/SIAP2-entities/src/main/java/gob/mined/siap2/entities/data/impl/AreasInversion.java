/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import gob.mined.siap2.annotations.AtributoNormalizable;
import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, name = "ss_areas_inversion")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
public class AreasInversion implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "seq_areas_inversion", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_areas_inversion", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_areas_inversion")
    @Column(name = "ai_id")
    private Integer id;
    
    @Column(name = "ai_codigo")
    @AtributoNormalizable
    private String codigo;
    
    @Column(name = "ai_nombre")
    @AtributoNormalizable
    private String nombre;
    
    @Column(name = "ai_nombre_busqueda")
    private String nombreBusqueda;
    
    @Column(name = "ai_descripcion")
    private String descripcion;
    
    @Column(name = "ai_habilitado")
    private Boolean habilitado;
    
    @Column(name = "ai_aplica_plan_compras")
    private Boolean aplicaPlanCompras;
    
    @Column(name = "ai_orden")
    private Integer orden;

    @ManyToOne
    @JoinColumn(name = "ai_area_padre") 
    private AreasInversion areaPadre;
    
    @OneToMany(mappedBy = "areaPadre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AreasInversion> areasInversiones;


    @Column(name = "ai_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "ai_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;
    
    @Column(name = "ai_version")
    @Version
    private Integer version;
    
    
    @PrePersist
    @PreUpdate
    @PostLoad
    public void beforeSave() {
        this.nombre = this.nombre != null ? this.nombre.trim() : "";
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
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

    public List<AreasInversion> getAreasInversiones() {
        return areasInversiones;
    }

    public void setAreasInversiones(List<AreasInversion> areasInversiones) {
        this.areasInversiones = areasInversiones;
    }

    public AreasInversion getAreaPadre() {
        return areaPadre;
    }

    public void setAreaPadre(AreasInversion areaPadre) {
        this.areaPadre = areaPadre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        final AreasInversion other = (AreasInversion) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public Boolean getAplicaPlanCompras() {
        return aplicaPlanCompras;
    }

    public void setAplicaPlanCompras(Boolean aplicaPlanCompras) {
        this.aplicaPlanCompras = aplicaPlanCompras;
    }

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }
    
}
