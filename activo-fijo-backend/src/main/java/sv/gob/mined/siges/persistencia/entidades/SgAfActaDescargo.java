/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "sg_af_actas_descargo", schema = Constantes.SCHEMA_ACTIVO_FIJO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "adePk", scope = SgAfActaDescargo.class)
public class SgAfActaDescargo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ade_pk")
    private Long adePk;
    
    @Size(max = 20)
    @Column(name = "ade_numero_acuerdo", length = 20)
    private String adeNumeroAcuerdo;  
    
    @Size(max = 500)
    @Column(name = "ade_se_autoriza", length = 500)
    private String adeSeAutoriza; 

    @Column(name = "ade_fecha_acuerdo")
    private LocalDate adeFechaAcuerdo;
    
    
    @Column(name = "ade_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime adeUltModFecha;
    
    @Size(max = 45)
    @Column(name = "ade_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String adeUltModUsuario;
    
    @Column(name = "ade_version")
    @Version
    private Integer adeVersion;

    public Long getAdePk() {
        return adePk;
    }

    public void setAdePk(Long adePk) {
        this.adePk = adePk;
    }

    public String getAdeNumeroAcuerdo() {
        return adeNumeroAcuerdo;
    }

    public void setAdeNumeroAcuerdo(String adeNumeroAcuerdo) {
        this.adeNumeroAcuerdo = adeNumeroAcuerdo;
    }

    public String getAdeSeAutoriza() {
        return adeSeAutoriza;
    }

    public void setAdeSeAutoriza(String adeSeAutoriza) {
        this.adeSeAutoriza = adeSeAutoriza;
    }

    public LocalDate getAdeFechaAcuerdo() {
        return adeFechaAcuerdo;
    }

    public void setAdeFechaAcuerdo(LocalDate adeFechaAcuerdo) {
        this.adeFechaAcuerdo = adeFechaAcuerdo;
    }

    public LocalDateTime getAdeUltModFecha() {
        return adeUltModFecha;
    }

    public void setAdeUltModFecha(LocalDateTime adeUltModFecha) {
        this.adeUltModFecha = adeUltModFecha;
    }

    public String getAdeUltModUsuario() {
        return adeUltModUsuario;
    }

    public void setAdeUltModUsuario(String adeUltModUsuario) {
        this.adeUltModUsuario = adeUltModUsuario;
    }

    public Integer getAdeVersion() {
        return adeVersion;
    }

    public void setAdeVersion(Integer adeVersion) {
        this.adeVersion = adeVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.adePk);
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
        final SgAfActaDescargo other = (SgAfActaDescargo) obj;
        if (!Objects.equals(this.adePk, other.adePk)) {
            return false;
        }
        return true;
    }
    
    
    
}

