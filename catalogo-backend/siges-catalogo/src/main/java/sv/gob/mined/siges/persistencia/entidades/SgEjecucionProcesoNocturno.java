/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_ejecucion_proceso_nocturno", schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eprPk", scope = SgEjecucionProcesoNocturno.class)
@Audited
public class SgEjecucionProcesoNocturno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "epr_pk", nullable = false)
    private Long eprPk;

    @Column(name = "epr_comienzo_ejecucion")
    private LocalDateTime eprComienzoEjecucion;
    
    @Column(name = "epr_fin_ejecucion")
    private LocalDateTime eprFinEjecucion;
    
    @Column(name = "epr_ejecucion_correcta")
    private Boolean eprEjecucionCorrecta;

    @JoinColumn(name = "epr_proceso_nocturno_fk", referencedColumnName = "prn_pk")
    @ManyToOne
    private SgProcesoNocturno eprProcesoNocturnoFk;

    @Column(name = "epr_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime eprUltModFecha;

    @Size(max = 45)
    @Column(name = "epr_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String eprUltModUsuario;

    @Column(name = "epr_version")
    @Version
    private Integer eprVersion;

    public SgEjecucionProcesoNocturno() {
    }


    public Long getEprPk() {
        return eprPk;
    }

    public void setEprPk(Long eprPk) {
        this.eprPk = eprPk;
    }

    public LocalDateTime getEprComienzoEjecucion() {
        return eprComienzoEjecucion;
    }

    public void setEprComienzoEjecucion(LocalDateTime eprComienzoEjecucion) {
        this.eprComienzoEjecucion = eprComienzoEjecucion;
    }

    public LocalDateTime getEprFinEjecucion() {
        return eprFinEjecucion;
    }

    public void setEprFinEjecucion(LocalDateTime eprFinEjecucion) {
        this.eprFinEjecucion = eprFinEjecucion;
    }

    public Boolean getEprEjecucionCorrecta() {
        return eprEjecucionCorrecta;
    }

    public void setEprEjecucionCorrecta(Boolean eprEjecucionCorrecta) {
        this.eprEjecucionCorrecta = eprEjecucionCorrecta;
    }

    public SgProcesoNocturno getEprProcesoNocturnoFk() {
        return eprProcesoNocturnoFk;
    }

    public void setEprProcesoNocturnoFk(SgProcesoNocturno eprProcesoNocturnoFk) {
        this.eprProcesoNocturnoFk = eprProcesoNocturnoFk;
    }

    public LocalDateTime getEprUltModFecha() {
        return eprUltModFecha;
    }

    public void setEprUltModFecha(LocalDateTime eprUltModFecha) {
        this.eprUltModFecha = eprUltModFecha;
    }

    public String getEprUltModUsuario() {
        return eprUltModUsuario;
    }

    public void setEprUltModUsuario(String eprUltModUsuario) {
        this.eprUltModUsuario = eprUltModUsuario;
    }

    public Integer getEprVersion() {
        return eprVersion;
    }

    public void setEprVersion(Integer eprVersion) {
        this.eprVersion = eprVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.eprPk);
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
        final SgEjecucionProcesoNocturno other = (SgEjecucionProcesoNocturno) obj;
        if (!Objects.equals(this.eprPk, other.eprPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEjecucionProcesoNocturno{" + "eprPk=" + eprPk +  ", eprUltModFecha=" + eprUltModFecha + ", eprUltModUsuario=" + eprUltModUsuario + ", eprVersion=" + eprVersion + '}';
    }
    
    

}
