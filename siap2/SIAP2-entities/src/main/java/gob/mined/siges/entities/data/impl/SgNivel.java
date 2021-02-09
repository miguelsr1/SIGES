package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(schema = Constantes.SCHEMA_CENTRO_EDUCATIVO, name = "sg_niveles")
@Cache(expiry = 150000)
public class SgNivel implements Serializable{
    
    @Id
    @Column(name = "niv_pk")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "niv_organizacion_curricular") 
    private SgOrganizacionCurricular organizacionCurricular;
    
    @Column(name = "niv_codigo")
    private String codigo;
    
    @Column(name = "niv_nombre")
    private String nombre;
    
    @Column(name = "niv_orden")
    private Integer orden;
    
    @Column(name = "niv_obligatorio")
    private Boolean obligatorio;
    
    @Column(name = "niv_habilitado")
    private Boolean habilitado;
    
    @Column(name = "niv_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultModFecha;
    
    @Column(name = "niv_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultModUsuario;
    
    @Column(name = "niv_version")
    @Version
    private Integer version;

    @OneToMany(mappedBy = "nivel")
    private List<SgCiclo> listaCiclosHijos;
    
    
    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SgOrganizacionCurricular getOrganizacionCurricular() {
        return organizacionCurricular;
    }

    public void setOrganizacionCurricular(SgOrganizacionCurricular organizacionCurricular) {
        this.organizacionCurricular = organizacionCurricular;
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

    public List<SgCiclo> getListaCiclosHijos() {
        return listaCiclosHijos;
    }

    public void setListaCiclosHijos(List<SgCiclo> listaCiclosHijos) {
        this.listaCiclosHijos = listaCiclosHijos;
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
        if (!(object instanceof SgNivel)) {
            return false;
        }
        SgNivel other = (SgNivel) object;
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
