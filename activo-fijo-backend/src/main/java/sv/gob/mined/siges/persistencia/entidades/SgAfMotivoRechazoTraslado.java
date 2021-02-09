/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_motivos_rechazo_traslado", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mrtId", scope = SgAfMotivoRechazoTraslado.class)
public class SgAfMotivoRechazoTraslado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mrt_id")
    private Long mrtId;
    
    @Size(max = 500)
    @Column(name = "mrt_motivo_rechazo", length = 500)
    private String mrtMotivoRechazo;
    
    @Column(name = "mrt_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mrtUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mrt_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mrtUltModUsuario;
    
    @Column(name = "mrt_version")
    @Version
    private Integer mrtVersion;
    
    @JoinColumn(name = "mrt_traslado_fk", referencedColumnName = "tra_pk")
    @ManyToOne
    private SgAfTraslado mrtTrasladoFk;

    public SgAfMotivoRechazoTraslado() {
    }

    public Long getMrtId() {
        return mrtId;
    }

    public void setMrtId(Long mrtId) {
        this.mrtId = mrtId;
    }

    public String getMrtMotivoRechazo() {
        return mrtMotivoRechazo;
    }

    public void setMrtMotivoRechazo(String mrtMotivoRechazo) {
        this.mrtMotivoRechazo = mrtMotivoRechazo;
    }

    public LocalDateTime getMrtUltModFecha() {
        return mrtUltModFecha;
    }

    public void setMrtUltModFecha(LocalDateTime mrtUltModFecha) {
        this.mrtUltModFecha = mrtUltModFecha;
    }

    public String getMrtUltModUsuario() {
        return mrtUltModUsuario;
    }

    public void setMrtUltModUsuario(String mrtUltModUsuario) {
        this.mrtUltModUsuario = mrtUltModUsuario;
    }

    public Integer getMrtVersion() {
        return mrtVersion;
    }

    public void setMrtVersion(Integer mrtVersion) {
        this.mrtVersion = mrtVersion;
    }

    public SgAfTraslado getMrtTrasladoFk() {
        return mrtTrasladoFk;
    }

    public void setMrtTrasladoFk(SgAfTraslado mrtTrasladoFk) {
        this.mrtTrasladoFk = mrtTrasladoFk;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mrtId != null ? mrtId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfMotivoRechazoTraslado)) {
            return false;
        }
        SgAfMotivoRechazoTraslado other = (SgAfMotivoRechazoTraslado) object;
        if ((this.mrtId == null && other.mrtId != null) || (this.mrtId != null && !this.mrtId.equals(other.mrtId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfMotivoRechazoTraslado[ mrtId=" + mrtId + " ]";
    }
    
}
