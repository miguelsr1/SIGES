/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.centros;

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
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoTelefono;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_telefonos", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "telPk", scope = SgTelefono.class)
@Audited
public class SgTelefono implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tel_pk")
    private Long telPk;
    
    @Size(max = 15)
    @Column(name = "tel_telefono",length = 15)
    private String telTelefono;
    
    @JoinColumn(name = "tel_tipo_telefono_fk", referencedColumnName = "tto_pk")
    @ManyToOne
    private SgTipoTelefono telTipoTelefono;
    
    @Column(name = "tel_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime telUltModFecha;
    
    @Size(max = 45)
    @Column(name = "tel_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String telUltModUsuario;
    
    @Column(name = "tel_version")
    @Version
    private Integer telVersion;
    

    public SgTelefono() {
    }

    public SgTelefono(Long telPk) {
        this.telPk = telPk;
    }
    
    public Long getTelPk() {
        return telPk;
    }

    public void setTelPk(Long telPk) {
        this.telPk = telPk;
    }

    public String getTelTelefono() {
        return telTelefono;
    }

    public void setTelTelefono(String telTelefono) {
        this.telTelefono = telTelefono;
    }

    public SgTipoTelefono getTelTipoTelefono() {
        return telTipoTelefono;
    }

    public void setTelTipoTelefono(SgTipoTelefono telTipoTelefono) {
        this.telTipoTelefono = telTipoTelefono;
    }

    public LocalDateTime getTelUltModFecha() {
        return telUltModFecha;
    }

    public void setTelUltModFecha(LocalDateTime telUltModFecha) {
        this.telUltModFecha = telUltModFecha;
    }

    public String getTelUltModUsuario() {
        return telUltModUsuario;
    }

    public void setTelUltModUsuario(String telUltModUsuario) {
        this.telUltModUsuario = telUltModUsuario;
    }

    public Integer getTelVersion() {
        return telVersion;
    }

    public void setTelVersion(Integer telVersion) {
        this.telVersion = telVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (telPk != null ? telPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTelefono)) {
            return false;
        }
        SgTelefono other = (SgTelefono) object;
        if ((this.telPk == null && other.telPk != null) || (this.telPk != null && !this.telPk.equals(other.telPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesTelefono[ telPk=" + telPk + " ]";
    }
    
}
