package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import java.util.Set;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(schema = Constantes.SCHEMA_CENTRO_EDUCATIVO, name = "sg_modalidades")
@Cache(expiry = 150000)
public class SgModalidad implements Serializable{
    
    @Id
    @Column(name = "mod_pk")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "mod_ciclo") 
    private SgCiclo ciclo;
    
    @Column(name = "mod_codigo")
    private String codigo;
    
    @Column(name = "mod_nombre")
    private String nombre;
    
    @Column(name = "mod_orden")
    private Integer orden;
    
    @Column(name = "mod_admite_opcion")
    private Boolean admiteOpcion;
    
    @Column(name = "mod_habilitado")
    private Boolean habilitado;
    
    @Column(name = "mod_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultModFecha;
    
    @Column(name = "mod_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultModUsuario;
    
    @Column(name = "mod_version")
    @Version
    private Integer version;
    
    @Column(name = "mod_aplica_diplomado")
    private Boolean aplica_diplomado;
    
    @ManyToMany
    @JoinTable(
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            name = "sg_rel_mod_ed_mod_aten",
            joinColumns = {
                @JoinColumn(name = "rea_modalidad_educativa_fk", referencedColumnName = "mod_pk")},
            inverseJoinColumns = {
                @JoinColumn(name = "rea_modalidad_atencion_fk", referencedColumnName = "mat_pk")})
    private Set<SgModalidadAtencion> modalidadesAtencion;
    
    @ManyToMany
    @JoinTable(
            schema = Constantes.SCHEMA_CENTRO_EDUCATIVO,
            name = "sg_rel_mod_ed_mod_aten",
            joinColumns = {
                @JoinColumn(name = "rea_modalidad_educativa_fk", referencedColumnName = "mod_pk")},
            inverseJoinColumns = {
                @JoinColumn(name = "rea_sub_modalidad_atencion_fk", referencedColumnName = "smo_pk")})
    private Set<SgSubModalidad> subModalidadesAtencion;
    
    
    public SgModalidad() {
    }

    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SgCiclo getCiclo() {
        return ciclo;
    }

    public void setCiclo(SgCiclo ciclo) {
        this.ciclo = ciclo;
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

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getAdmiteOpcion() {
        return admiteOpcion;
    }

    public void setAdmiteOpcion(Boolean admiteOpcion) {
        this.admiteOpcion = admiteOpcion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Date getUltModFecha() {
        return ultModFecha;
    }

    public void setUltModFecha(Date ultModFecha) {
        this.ultModFecha = ultModFecha;
    }

    public String getUltModUsuario() {
        return ultModUsuario;
    }

    public void setUltModUsuario(String ultModUsuario) {
        this.ultModUsuario = ultModUsuario;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getAplica_diplomado() {
        return aplica_diplomado;
    }

    public void setAplica_diplomado(Boolean aplica_diplomado) {
        this.aplica_diplomado = aplica_diplomado;
    }

    public Set<SgModalidadAtencion> getModalidadesAtencion() {
        return modalidadesAtencion;
    }

    public void setModalidadesAtencion(Set<SgModalidadAtencion> modalidadesAtencion) {
        this.modalidadesAtencion = modalidadesAtencion;
    }

    public Set<SgSubModalidad> getSubModalidadesAtencion() {
        return subModalidadesAtencion;
    }

    public void setSubModalidadesAtencion(Set<SgSubModalidad> subModalidadesAtencion) {
        this.subModalidadesAtencion = subModalidadesAtencion;
    }
     
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgModalidad)) {
            return false;
        }
        SgModalidad other = (SgModalidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "[ id =" + id + " ]";
    }
}
