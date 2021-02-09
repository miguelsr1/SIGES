package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(schema = Constantes.SCHEMA_CENTRO_EDUCATIVO, name = "sg_organizaciones_curricular")
@Cache(expiry = 150000)
public class SgOrganizacionCurricular implements Serializable{

   
    
    @Id
    @Column(name = "ocu_pk")
    private Integer id;
    
    @Column(name = "ocu_codigo")
    private String codigo;
    
    @Column(name = "ocu_nombre")
    private String nombre;
    
    @Column(name = "ocu_nombre_busqueda")
    private String nombreBusqueda;
    
    @Column(name = "ocu_descripcion")
    private String descripcion;
    
    @Column(name = "ocu_habilitado")
    private Boolean habilitado;
    
    @Column(name = "ocu_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultModFecha;
    
    @Column(name = "ocu_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultModUsuario;
    
    @Column(name = "ocu_version")
    @Version
    private Integer version;
    
    @Column(name = "ocu_aplica_alertas_tempranas")
    private Boolean aplicaAlertasTempranas;

    @OneToMany(mappedBy = "organizacionCurricular")
    private List<SgNivel> niveles;
    
    
    
    public SgOrganizacionCurricular() {
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

    public String getNombreBusqueda() {
        return nombreBusqueda;
    }

    public void setNombreBusqueda(String nombreBusqueda) {
        this.nombreBusqueda = nombreBusqueda;
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

    public Boolean getAplicaAlertasTempranas() {
        return aplicaAlertasTempranas;
    }

    public void setAplicaAlertasTempranas(Boolean aplicaAlertasTempranas) {
        this.aplicaAlertasTempranas = aplicaAlertasTempranas;
    }

    public List<SgNivel> getNiveles() {
        return niveles;
    }

    public void setNiveles(List<SgNivel> niveles) {
        this.niveles = niveles;
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
        if (!(object instanceof SgOrganizacionCurricular)) {
            return false;
        }
        SgOrganizacionCurricular other = (SgOrganizacionCurricular) object;
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
