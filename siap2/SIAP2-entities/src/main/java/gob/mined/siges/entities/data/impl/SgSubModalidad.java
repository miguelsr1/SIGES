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
import javax.persistence.Transient;
import javax.persistence.Version;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Table(schema = Constantes.SCHEMA_CATALOGO, name = "sg_sub_modalidades")
@Cache(expiry = 150000)
public class SgSubModalidad implements Serializable{
    
    @Id
    @Column(name = "smo_pk")
    private Integer id;
    
    @Column(name = "smo_codigo")
    private String codigo;
    
    @ManyToOne
    @JoinColumn(name = "smo_modalidad_fk")
    private SgModalidadAtencion modalidadFk;
    
    @Column(name = "smo_nombre")
    private String nombre;
    
    @Column(name = "smo_nombre_busqueda")
    private String nombreBusqueda;
    
    @Column(name = "smo_descripcion")
    private String descripcion;
    
    @Column(name = "smo_habilitado")
    private Boolean habilitado;
    
    @Column(name = "smo_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultModFecha;
    
    @Column(name = "smo_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultModUsuario;
    
    @Column(name = "smo_version")
    @Version
    private Integer version;
    
    @Column(name = "smo_integrada")
    private Boolean integrada;

    @Transient
    private boolean cheque;
    
    
    public SgSubModalidad() {
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

    public SgModalidadAtencion getModalidadFk() {
        return modalidadFk;
    }

    public void setModalidadFk(SgModalidadAtencion modalidadFk) {
        this.modalidadFk = modalidadFk;
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

    public Boolean getIntegrada() {
        return integrada;
    }

    public void setIntegrada(Boolean integrada) {
        this.integrada = integrada;
    }

    public boolean isCheque() {
        return cheque;
    }

    public void setCheque(boolean cheque) {
        this.cheque = cheque;
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
        if (!(object instanceof SgSubModalidad)) {
            return false;
        }
        SgSubModalidad other = (SgSubModalidad) object;
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
