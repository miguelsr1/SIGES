/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.seguridad;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_roles_operacion", schema = Constantes.SCHEMA_SEGURIDAD)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ropPk", scope = SgRolOperacion.class)
@Audited
public class SgRolOperacion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rop_pk")
    private Long ropPk;
    
    @Column(name = "rop_cascada")
    private Boolean ropCascada;
    
    @Column(name = "rop_habilitado")
    @AtributoHabilitado
    private Boolean ropHabilitado;
    
    @Column(name = "rop_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ropUltModFecha;
    
    @Size(max = 45)
    @Column(name = "rop_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String ropUltModUsuario;
    
    @Column(name = "rop_version")
    @Version
    private Integer ropVersion;
    
    @JoinColumn(name = "rop_operacion_fk", referencedColumnName = "ope_pk")
    @ManyToOne
    private SgOperacion ropOperacion;
    
    @JoinColumn(name = "rop_rol_fk", referencedColumnName = "rol_pk")
    @ManyToOne
    private SgRol ropRol;

    public SgRolOperacion() {
    }

    public Long getRopPk() {
        return ropPk;
    }

    public void setRopPk(Long ropPk) {
        this.ropPk = ropPk;
    }

    public Boolean getRopCascada() {
        return ropCascada;
    }

    public void setRopCascada(Boolean ropCascada) {
        this.ropCascada = ropCascada;
    }

    public Boolean getRopHabilitado() {
        return ropHabilitado;
    }

    public void setRopHabilitado(Boolean ropHabilitado) {
        this.ropHabilitado = ropHabilitado;
    }

    public LocalDateTime getRopUltModFecha() {
        return ropUltModFecha;
    }

    public void setRopUltModFecha(LocalDateTime ropUltModFecha) {
        this.ropUltModFecha = ropUltModFecha;
    }

    public String getRopUltModUsuario() {
        return ropUltModUsuario;
    }

    public void setRopUltModUsuario(String ropUltModUsuario) {
        this.ropUltModUsuario = ropUltModUsuario;
    }

    public Integer getRopVersion() {
        return ropVersion;
    }

    public void setRopVersion(Integer ropVersion) {
        this.ropVersion = ropVersion;
    }

    public SgOperacion getRopOperacion() {
        return ropOperacion;
    }

    public void setRopOperacion(SgOperacion ropOperacion) {
        this.ropOperacion = ropOperacion;
    }

    public SgRol getRopRol() {
        return ropRol;
    }

    public void setRopRol(SgRol ropRol) {
        this.ropRol = ropRol;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ropPk != null ? ropPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRolOperacion)) {
            return false;
        }
        SgRolOperacion other = (SgRolOperacion) object;
        if ((this.ropPk == null && other.ropPk != null) || (this.ropPk != null && !this.ropPk.equals(other.ropPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRolOperacion[ ropPk=" + ropPk + " ]";
    }
    
}
