/*
 * Proyecto: Sistema Integrado de Administracion de Proyectos II
 * Organismo: Ministerio de Educacion de El Salvador
 * Desarrollado por: Sofis Solutions
 *
 */
package gob.mined.siap2.entities.data.impl;

import com.mined.siap2.interfaces.BaseCodiguera;
import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Cacheable;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Customizer;

/**
 *
 * @author Sofis Solutions
 */


@Entity
@Table(schema = Constantes.SCHEMA_SIAP2, uniqueConstraints = {
    @UniqueConstraint(name = "fuente_uk", columnNames = {"fue_codigo"}),
    @UniqueConstraint(name = "codigo_fuente_uk", columnNames = {"fue_codigo_busqueda"})},name = "ss_fuente_financiamiento")
@EntityListeners({DecoratorEntityListener.class})
@Customizer(com.mined.siap2.descriptors.HistoryCustomizer.class)
@Cacheable(false)
public class FuenteFinanciamiento implements BaseCodiguera, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_fuen_fin_gen")
    @SequenceGenerator(name = "seq_fuen_fin_gen", sequenceName = Constantes.SCHEMA_SIAP2 + ".seq_fuen_fin", allocationSize = 1)
    @Column(name = "fue_id")
    private Integer id;

    @Column(name = "fue_nombre")
    private String nombre;

    @Column(name = "fue_nombre_busqueda")
    private String nombreBusqueda;
    
    @Column(name = "fue_habilitado")
    private Boolean habilitado = Boolean.TRUE;

    @Column(name = "fue_orden")
    private Integer orden;

    @Column(name = "fue_codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "fue_codigo_busqueda", nullable = false, unique = true)
    private String codigoBusqueda;
    
    @OneToMany(mappedBy = "fuenteFinanciamiento")
    private List<FuenteRecursos> fuentesDeRecursos;

    //Auditoria
    @Column(name = "fue_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "fue_ult_mod")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "fue_version")
    @Version
    private Integer version;

    public FuenteFinanciamiento() {
        this.codigoBusqueda = this.codigo != null ? this.codigo.trim() : "";
        this.nombre = this.nombre != null ? this.nombre.trim() : "";
        this.nombreBusqueda = this.nombre != null ? this.nombre.trim() : "";
    }

    
    
    // <editor-fold defaultstate="collapsed" desc="getter-setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<FuenteRecursos> getFuentesDeRecursos() {
        return fuentesDeRecursos;
    }

    public void setFuentesDeRecursos(List<FuenteRecursos> fuentesDeRecursos) {
        this.fuentesDeRecursos = fuentesDeRecursos;
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

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
    }
    // </editor-fold>

    @PrePersist
    @PreUpdate
    private void preSave() {
        this.codigoBusqueda = this.codigo != null ? this.codigo.toLowerCase().trim() : "";
        this.nombre = this.nombre != null ? this.nombre.trim() : "";
        this.nombreBusqueda = this.nombre != null ? this.nombre.trim() : "";
    }
    @PostLoad
    private void postSave() {
        this.codigoBusqueda = this.codigo != null ? this.codigo.toLowerCase().trim() : "";
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final FuenteFinanciamiento other = (FuenteFinanciamiento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public String getCodigoBusqueda() {
        return codigoBusqueda;
    }

    public void setCodigoBusqueda(String codigoBusqueda) {
        this.codigoBusqueda = codigoBusqueda;
    }

    @Override
    public String toString() {
        return "FuenteFinanciamiento{" + "id=" + id + ", nombre=" + nombre + ", nombreBusqueda=" + nombreBusqueda + ", habilitado=" + habilitado + ", orden=" + orden + ", codigo=" + codigo + ", codigoBusqueda=" + codigoBusqueda + ", fuentesDeRecursos=" + fuentesDeRecursos + ", ultUsuario=" + ultUsuario + ", ultMod=" + ultMod + ", version=" + version + '}';
    }

}
