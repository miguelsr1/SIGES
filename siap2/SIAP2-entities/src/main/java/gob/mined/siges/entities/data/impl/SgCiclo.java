package gob.mined.siges.entities.data.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(schema = Constantes.SCHEMA_CENTRO_EDUCATIVO, name = "sg_ciclos")
@Cache(expiry = 150000)
public class SgCiclo implements Serializable{
    
    @Id
    @Column(name = "cic_pk")
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cic_nivel") 
    private SgNivel nivel;
    
    @Column(name = "cic_codigo")
    private String codigo;
    
    @Column(name = "cic_nombre")
    private String nombre;
    
    @Column(name = "cic_orden")
    private Integer orden;
    
    @Column(name = "cic_obligatorio")
    private Boolean obligatorio;
    
    @Column(name = "cic_habilitado")
    private Boolean habilitado;
    
    @Column(name = "cic_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultModFecha;
    
    @Column(name = "cic_ult_mod_usuario")
    private String ultModUsuario;
    
    @Column(name = "cic_version")
    @Version
    private Integer version;
    
    @OneToMany(mappedBy = "ciclo")
    private List<SgModalidad> listaModalidadesHijos;
    
    public SgCiclo() {
    }

    
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SgNivel getNivel() {
        return nivel;
    }

    public void setNivel(SgNivel nivel) {
        this.nivel = nivel;
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

    public Boolean getObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(Boolean obligatorio) {
        this.obligatorio = obligatorio;
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

    public List<SgModalidad> getListaModalidadesHijos() {
        return listaModalidadesHijos;
    }

    public void setListaModalidadesHijos(List<SgModalidad> listaModalidadesHijos) {
        this.listaModalidadesHijos = listaModalidadesHijos;
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
        if (!(object instanceof SgCiclo)) {
            return false;
        }
        SgCiclo other = (SgCiclo) object;
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
