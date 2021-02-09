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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgModalidadAtencion;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgSubModalidadAtencion;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_rel_habilitacion_matricula_nivel",schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "rhnPk", scope = SgRelHabilitacionMatriculaNivel.class)
@Audited
public class SgRelHabilitacionMatriculaNivel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rhn_pk", nullable = false)
    private Long rhnPk;

    
    @JoinColumn(name = "rhn_nivel_fk", referencedColumnName = "niv_pk")
    @ManyToOne
    private SgNivel rhnNivelFk;
    
    @JoinColumn(name = "rhn_modalidad_atencion_fk", referencedColumnName = "mat_pk")
    @ManyToOne
    private SgModalidadAtencion rhnModalidadAtencionFk;
    
    @JoinColumn(name = "rhn_submodalidad_fk", referencedColumnName = "smo_pk")
    @ManyToOne
    private SgSubModalidadAtencion rhnSubmodaliadadFk;
    
    @JoinColumn(name = "rhn_habilitacion_periodo_matricula_fk", referencedColumnName = "hmp_pk")
    @ManyToOne
    private SgHabilitacionPeriodoMatricula rhnHabilitacionPeriodoMatriculaFk;


    @Column(name = "rhn_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime rhnUltModFecha;

    @Size(max = 45)
    @Column(name = "rhn_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String rhnUltModUsuario;

    @Column(name = "rhn_version")
    @Version
    private Integer rhnVersion;

    public SgRelHabilitacionMatriculaNivel() {
    }



    public Long getRhnPk() {
        return rhnPk;
    }

    public void setRhnPk(Long rhnPk) {
        this.rhnPk = rhnPk;
    }


    public LocalDateTime getRhnUltModFecha() {
        return rhnUltModFecha;
    }

    public void setRhnUltModFecha(LocalDateTime rhnUltModFecha) {
        this.rhnUltModFecha = rhnUltModFecha;
    }

    public String getRhnUltModUsuario() {
        return rhnUltModUsuario;
    }

    public void setRhnUltModUsuario(String rhnUltModUsuario) {
        this.rhnUltModUsuario = rhnUltModUsuario;
    }

    public Integer getRhnVersion() {
        return rhnVersion;
    }

    public void setRhnVersion(Integer rhnVersion) {
        this.rhnVersion = rhnVersion;
    }

    public SgNivel getRhnNivelFk() {
        return rhnNivelFk;
    }

    public void setRhnNivelFk(SgNivel rhnNivelFk) {
        this.rhnNivelFk = rhnNivelFk;
    }

    public SgModalidadAtencion getRhnModalidadAtencionFk() {
        return rhnModalidadAtencionFk;
    }

    public void setRhnModalidadAtencionFk(SgModalidadAtencion rhnModalidadAtencionFk) {
        this.rhnModalidadAtencionFk = rhnModalidadAtencionFk;
    }

    public SgSubModalidadAtencion getRhnSubmodaliadadFk() {
        return rhnSubmodaliadadFk;
    }

    public void setRhnSubmodaliadadFk(SgSubModalidadAtencion rhnSubmodaliadadFk) {
        this.rhnSubmodaliadadFk = rhnSubmodaliadadFk;
    }

    public SgHabilitacionPeriodoMatricula getRhnHabilitacionPeriodoMatriculaFk() {
        return rhnHabilitacionPeriodoMatriculaFk;
    }

    public void setRhnHabilitacionPeriodoMatriculaFk(SgHabilitacionPeriodoMatricula rhnHabilitacionPeriodoMatriculaFk) {
        this.rhnHabilitacionPeriodoMatriculaFk = rhnHabilitacionPeriodoMatriculaFk;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.rhnPk);
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
        final SgRelHabilitacionMatriculaNivel other = (SgRelHabilitacionMatriculaNivel) obj;
        if (!Objects.equals(this.rhnPk, other.rhnPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgRelHabilitacionMatriculaNivel{" + "rhnPk=" + rhnPk + ", rhnNivelFk=" + rhnNivelFk + ", rhnModalidadAtencionFk=" + rhnModalidadAtencionFk + ", rhnSubmodaliadadFk=" + rhnSubmodaliadadFk + ", rhnHabilitacionPeriodoMatriculaFk=" + rhnHabilitacionPeriodoMatriculaFk + ", rhnUltModFecha=" + rhnUltModFecha + ", rhnUltModUsuario=" + rhnUltModUsuario + ", rhnVersion=" + rhnVersion + '}';
    }

  

}
