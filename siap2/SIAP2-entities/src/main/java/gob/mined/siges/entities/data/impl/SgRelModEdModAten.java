package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import gob.mined.siap2.entities.constantes.Constantes;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(schema = Constantes.SCHEMA_CENTRO_EDUCATIVO, name = "sg_rel_mod_ed_mod_aten")
@Cache(expiry = 150000)
public class SgRelModEdModAten implements Serializable{
    
    @Id
    @Column(name = "rea_pk")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "rea_modalidad_educativa_fk", nullable = false)
    private SgModalidad modalidadEducativaFk;
    
    @ManyToOne
    @JoinColumn(name = "rea_modalidad_atencion_fk", nullable = false)
private SgModalidadAtencion modalidadAtencionFk;
    
    @ManyToOne
    @JoinColumn(name = "rea_sub_modalidad_atencion_fk", nullable = true)
    private SgSubModalidad subModalidadAtencionFk;
    
    @Column(name = "rea_habilitado")
    private Boolean habilitado;
    
    @Column(name = "rea_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultModFecha;
    
    @Column(name = "rea_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultModUsuario;
    
    @Column(name = "rea_version")
    @Version
    private Integer version;

    
    
    public SgRelModEdModAten() {
    }

    
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SgModalidad getModalidadEducativaFk() {
        return modalidadEducativaFk;
    }

    public void setModalidadEducativaFk(SgModalidad modalidadEducativaFk) {
        this.modalidadEducativaFk = modalidadEducativaFk;
    }

    public SgModalidadAtencion getModalidadAtencionFk() {
        return modalidadAtencionFk;
    }

    public void setModalidadAtencionFk(SgModalidadAtencion modalidadAtencionFk) {
        this.modalidadAtencionFk = modalidadAtencionFk;
    }

    public SgSubModalidad getSubModalidadAtencionFk() {
        return subModalidadAtencionFk;
    }

    public void setSubModalidadAtencionFk(SgSubModalidad subModalidadAtencionFk) {
        this.subModalidadAtencionFk = subModalidadAtencionFk;
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

    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRelModEdModAten)) {
            return false;
        }
        SgRelModEdModAten other = (SgRelModEdModAten) object;
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
