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
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_sustitucion_miembro_oae", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rsmPk", scope = SgRelSustitucionMiembroOAE.class)
@Audited
public class SgRelSustitucionMiembroOAE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rsm_pk", nullable = false)
    private Long rsmPk;

    @JoinColumn(name = "rsm_sustitucion_miembro_fk")
    @ManyToOne
    private SgSustitucionMiembroOAE rsmSustitucionMiembroFk;
            
    @JoinColumn(name = "rsm_miembro_a_sustituir_fk")
    @ManyToOne
    private SgPersonaOrganismoAdministracion rsmMiembroaSustituirFk;
    
    
    @JoinColumn(name = "rsm_miembro_sustituyente_fk")
    @ManyToOne
    private SgPersonaOrganismoAdministracion rsmMiembroSustituyenteFk;
    
    @Column(name = "rsm_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rsmUltModFecha;

    @Size(max = 45)
    @Column(name = "rsm_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rsmUltModUsuario;

    @Column(name = "rsm_version")
    @Version
    private Integer rsmVersion;

    public SgRelSustitucionMiembroOAE() {
    }

 

    public Long getRsmPk() {
        return rsmPk;
    }

    public void setRsmPk(Long rsmPk) {
        this.rsmPk = rsmPk;
    }

    public SgSustitucionMiembroOAE getRsmSustitucionMiembroFk() {
        return rsmSustitucionMiembroFk;
    }

    public void setRsmSustitucionMiembroFk(SgSustitucionMiembroOAE rsmSustitucionMiembroFk) {
        this.rsmSustitucionMiembroFk = rsmSustitucionMiembroFk;
    }

    public SgPersonaOrganismoAdministracion getRsmMiembroaSustituirFk() {
        return rsmMiembroaSustituirFk;
    }

    public void setRsmMiembroaSustituirFk(SgPersonaOrganismoAdministracion rsmMiembroaSustituirFk) {
        this.rsmMiembroaSustituirFk = rsmMiembroaSustituirFk;
    }

    public SgPersonaOrganismoAdministracion getRsmMiembroSustituyenteFk() {
        return rsmMiembroSustituyenteFk;
    }

    public void setRsmMiembroSustituyenteFk(SgPersonaOrganismoAdministracion rsmMiembroSustituyenteFk) {
        this.rsmMiembroSustituyenteFk = rsmMiembroSustituyenteFk;
    }

    public LocalDateTime getRsmUltModFecha() {
        return rsmUltModFecha;
    }

    public void setRsmUltModFecha(LocalDateTime rsmUltModFecha) {
        this.rsmUltModFecha = rsmUltModFecha;
    }

    public String getRsmUltModUsuario() {
        return rsmUltModUsuario;
    }

    public void setRsmUltModUsuario(String rsmUltModUsuario) {
        this.rsmUltModUsuario = rsmUltModUsuario;
    }

    public Integer getRsmVersion() {
        return rsmVersion;
    }

    public void setRsmVersion(Integer rsmVersion) {
        this.rsmVersion = rsmVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.rsmPk);
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
        final SgRelSustitucionMiembroOAE other = (SgRelSustitucionMiembroOAE) obj;
        if (!Objects.equals(this.rsmPk, other.rsmPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelSustitucionMiembroOAE{" + "rsmPk=" + rsmPk + ", rsmUltModFecha=" + rsmUltModFecha + ", rsmUltModUsuario=" + rsmUltModUsuario + ", rsmVersion=" + rsmVersion + '}';
    }
    
    

}
