package gob.mined.siges.entities.data.impl;

import gob.mined.siap2.annotations.AtributoUltimaModificacion;
import gob.mined.siap2.annotations.AtributoUltimoUsuario;
import gob.mined.siap2.entities.listener.DecoratorEntityListener;
import java.io.Serializable;
import java.util.Objects;
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
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(schema = Constantes.SCHEMA_CATALOGO, name = "sg_tipo_cuenta_bancaria")
@EntityListeners({DecoratorEntityListener.class})
public class SgTipoCuentaBancaria implements Serializable{
    
    @Id
    @Basic(optional = false)
    @SequenceGenerator(name = "sg_tipo_cuenta_bancaria_tcb_pk_seq", sequenceName = Constantes.SCHEMA_CATALOGO + ".sg_tipo_cuenta_bancaria_tcb_pk_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sg_tipo_cuenta_bancaria_tcb_pk_seq")
    @Column(name = "tcb_pk")
    private Integer pk;
    
    @Column(name = "tcb_codigo") 
    private String codigo;
    
    @Column(name = "tcb_nombre") 
    private String nombre;
    
    @Column(name = "tcb_nombre_busqueda") 
    private String nombreBusqueda;
    
    @Column(name = "tcb_habilitado") 
    private Boolean habilitado;
    
    @Column(name = "tcb_ult_mod_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "tcb_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;
    
    @Column(name = "tcb_version")
    @Version
    private Integer version;

    
    
    
    
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
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
        hash = 79 * hash + Objects.hashCode(this.pk);
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
        final SgTipoCuentaBancaria other = (SgTipoCuentaBancaria) obj;
        if (!Objects.equals(this.pk, other.pk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoCuentaBancaria{" + "pk=" + pk + ", codigo=" + codigo + ", nombre=" + nombre + ", nombreBusqueda=" + nombreBusqueda + ", habilitado=" + habilitado + ", ultMod=" + ultMod + ", ultUsuario=" + ultUsuario + ", version=" + version + '}';
    }
    
    
    
    
    
}
