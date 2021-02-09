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
@Table(name = "sg_diplomas_estudiante", schema= Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "diePk", scope = SgDiplomaEstudiante.class)
@Audited
public class SgDiplomaEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "die_pk", nullable = false)
    private Long diePk;

    @Column(name = "die_confirmado")
    private Boolean dieConfirmado;
    
    @JoinColumn(name = "die_estudiante_fk", referencedColumnName = "est_pk")
    @ManyToOne
    private SgEstudiante dieEstudianteFk;
    
    @JoinColumn(name = "die_diploma_fk", referencedColumnName = "dil_pk")
    @ManyToOne
    private SgDiploma dieDiplomaFk;

    @Column(name = "die_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dieUltModFecha;

    @Size(max = 45)
    @Column(name = "die_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String dieUltModUsuario;

    @Column(name = "die_version")
    @Version
    private Integer dieVersion;

    public SgDiplomaEstudiante() {
    }

    public Long getDiePk() {
        return diePk;
    }

    public void setDiePk(Long diePk) {
        this.diePk = diePk;
    }

    public Boolean getDieConfirmado() {
        return dieConfirmado;
    }

    public void setDieConfirmado(Boolean dieConfirmado) {
        this.dieConfirmado = dieConfirmado;
    }

    public SgEstudiante getDieEstudianteFk() {
        return dieEstudianteFk;
    }

    public void setDieEstudianteFk(SgEstudiante dieEstudianteFk) {
        this.dieEstudianteFk = dieEstudianteFk;
    }

    public SgDiploma getDieDiplomaFk() {
        return dieDiplomaFk;
    }

    public void setDieDiplomaFk(SgDiploma dieDiplomaFk) {
        this.dieDiplomaFk = dieDiplomaFk;
    }

    public LocalDateTime getDieUltModFecha() {
        return dieUltModFecha;
    }

    public void setDieUltModFecha(LocalDateTime dieUltModFecha) {
        this.dieUltModFecha = dieUltModFecha;
    }

    public String getDieUltModUsuario() {
        return dieUltModUsuario;
    }

    public void setDieUltModUsuario(String dieUltModUsuario) {
        this.dieUltModUsuario = dieUltModUsuario;
    }

    public Integer getDieVersion() {
        return dieVersion;
    }

    public void setDieVersion(Integer dieVersion) {
        this.dieVersion = dieVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.diePk);
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
        final SgDiplomaEstudiante other = (SgDiplomaEstudiante) obj;
        if (!Objects.equals(this.diePk, other.diePk)) {
            return false;
        }
        return true;
    }

    

}
