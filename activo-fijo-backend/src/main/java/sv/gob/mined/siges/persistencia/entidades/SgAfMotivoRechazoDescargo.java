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
@Table(name = "sg_af_motivos_rechazo_descargo", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mrdId", scope = SgAfMotivoRechazoDescargo.class)
public class SgAfMotivoRechazoDescargo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mrd_id")
    private Long mrdId;
    
    @Size(max = 500)
    @Column(name = "mrd_motivo_rechazo", length = 500)
    private String mrdMotivoRechazo;
    
    @Column(name = "mrd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mrdUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mrd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mrdUltModUsuario;
    
    @Column(name = "mrd_version")
    @Version
    private Integer mrdVersion;
    
    @JoinColumn(name = "mrd_descargo_fk", referencedColumnName = "des_pk")
    @ManyToOne
    private SgAfDescargo mrdDescargoFk;

    public SgAfMotivoRechazoDescargo() {
    }

    public Long getMrdId() {
        return mrdId;
    }

    public void setMrdId(Long mrdId) {
        this.mrdId = mrdId;
    }

    public String getMrdMotivoRechazo() {
        return mrdMotivoRechazo;
    }

    public void setMrdMotivoRechazo(String mrdMotivoRechazo) {
        this.mrdMotivoRechazo = mrdMotivoRechazo;
    }

    public LocalDateTime getMrdUltModFecha() {
        return mrdUltModFecha;
    }

    public void setMrdUltModFecha(LocalDateTime mrdUltModFecha) {
        this.mrdUltModFecha = mrdUltModFecha;
    }

    public String getMrdUltModUsuario() {
        return mrdUltModUsuario;
    }

    public void setMrdUltModUsuario(String mrdUltModUsuario) {
        this.mrdUltModUsuario = mrdUltModUsuario;
    }

    public Integer getMrdVersion() {
        return mrdVersion;
    }

    public void setMrdVersion(Integer mrdVersion) {
        this.mrdVersion = mrdVersion;
    }

    public SgAfDescargo getMrdDescargoFk() {
        return mrdDescargoFk;
    }

    public void setMrdDescargoFk(SgAfDescargo mrdDescargoFk) {
        this.mrdDescargoFk = mrdDescargoFk;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mrdId != null ? mrdId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAfMotivoRechazoDescargo)) {
            return false;
        }
        SgAfMotivoRechazoDescargo other = (SgAfMotivoRechazoDescargo) object;
        if ((this.mrdId == null && other.mrdId != null) || (this.mrdId != null && !this.mrdId.equals(other.mrdId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgAfMotivoRechazoDescargo[ mrdId=" + mrdId + " ]";
    }
    
}
