/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
@Table(name = "sg_rel_grado_precedente", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "rgpPk", scope = SgRelGradoPrecedencia.class)
@Audited
public class SgRelGradoPrecedencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rgp_pk", nullable = false)
    private Long rgpPk;
    
    @JoinColumn(name = "rgp_grado_destino_fk")
    @ManyToOne
    private SgGrado rgpGradoDestinoFk;
    
    @JoinColumn(name = "rgp_grado_origen_fk")
    @ManyToOne
    private SgGrado rgpGradoOrigenFk;
    
    @Column(name = "rgp_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rgpUltModFecha;

    @Size(max = 45)
    @Column(name = "rgp_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rgpUltModUsuario;

    @Column(name = "rgp_version")
    @Version
    private Integer rgpVersion;
    
    public SgRelGradoPrecedencia() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        
    }

    public Long getRgpPk() {
        return rgpPk;
    }

    public void setRgpPk(Long rgpPk) {
        this.rgpPk = rgpPk;
    }

    public SgGrado getRgpGradoDestinoFk() {
        return rgpGradoDestinoFk;
    }

    public void setRgpGradoDestinoFk(SgGrado rgpGradoDestinoFk) {
        this.rgpGradoDestinoFk = rgpGradoDestinoFk;
    }

    public SgGrado getRgpGradoOrigenFk() {
        return rgpGradoOrigenFk;
    }

    public void setRgpGradoOrigenFk(SgGrado rgpGradoOrigenFk) {
        this.rgpGradoOrigenFk = rgpGradoOrigenFk;
    }
    

    public LocalDateTime getRgpUltModFecha() {
        return rgpUltModFecha;
    }

    public void setRgpUltModFecha(LocalDateTime rgpUltModFecha) {
        this.rgpUltModFecha = rgpUltModFecha;
    }

    public String getRgpUltModUsuario() {
        return rgpUltModUsuario;
    }

    public void setRgpUltModUsuario(String rgpUltModUsuario) {
        this.rgpUltModUsuario = rgpUltModUsuario;
    }

    public Integer getRgpVersion() {
        return rgpVersion;
    }

    public void setRgpVersion(Integer rgpVersion) {
        this.rgpVersion = rgpVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.rgpPk);
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
        final SgRelGradoPrecedencia other = (SgRelGradoPrecedencia) obj;
        if (!Objects.equals(this.rgpPk, other.rgpPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelGradoPlanEstudio{" + "rgpPk=" + rgpPk + ", rgpUltModFecha=" + rgpUltModFecha + ", rgpUltModUsuario=" + rgpUltModUsuario + ", rgpVersion=" + rgpVersion + '}';
    }
    
    

}
