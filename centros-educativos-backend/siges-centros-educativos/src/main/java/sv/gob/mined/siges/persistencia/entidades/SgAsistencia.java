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
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgMotivoInasistencia;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_asistencias", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "asiPk", scope = SgAsistencia.class)
public class SgAsistencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "asi_pk")
    private Long asiPk;

    @Column(name = "asi_inasistencia")
    private Boolean asiInasistencia;

    @Size(max = 255)
    @Column(name = "asi_observacion", nullable = true)
    private String asiObservacion;

    @Column(name = "asi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime asiUltModFecha;

    @Size(max = 45)
    @Column(name = "asi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String asiUltModUsuario;

    @Column(name = "asi_version")
    @Version
    private Integer asiVersion;

    @JoinColumn(name = "asi_estudiante", referencedColumnName = "est_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgEstudiante asiEstudiante;

    @JoinColumn(name = "asi_motivo_inasistencia", referencedColumnName = "min_pk", nullable = true)
    @ManyToOne
    private SgMotivoInasistencia asiMotivoInasistencia;

    @JoinColumn(name = "asi_control_fk", referencedColumnName = "cac_pk")
    @ManyToOne
    private SgControlAsistenciaCabezal asiControl;

    public SgAsistencia() {
    }

    public Long getAsiPk() {
        return asiPk;
    }

    public void setAsiPk(Long asiPk) {
        this.asiPk = asiPk;
    }

    public Boolean getAsiInasistencia() {
        return asiInasistencia;
    }

    public void setAsiInasistencia(Boolean asiInasistencia) {
        this.asiInasistencia = asiInasistencia;
    }

    public String getAsiObservacion() {
        return asiObservacion;
    }

    public void setAsiObservacion(String asiObservacion) {
        this.asiObservacion = asiObservacion;
    }

    public LocalDateTime getAsiUltModFecha() {
        return asiUltModFecha;
    }

    public void setAsiUltModFecha(LocalDateTime asiUltModFecha) {
        this.asiUltModFecha = asiUltModFecha;
    }

    public String getAsiUltModUsuario() {
        return asiUltModUsuario;
    }

    public void setAsiUltModUsuario(String asiUltModUsuario) {
        this.asiUltModUsuario = asiUltModUsuario;
    }

    public Integer getAsiVersion() {
        return asiVersion;
    }

    public void setAsiVersion(Integer asiVersion) {
        this.asiVersion = asiVersion;
    }

    public SgEstudiante getAsiEstudiante() {
        return asiEstudiante;
    }

    public void setAsiEstudiante(SgEstudiante asiEstudiante) {
        this.asiEstudiante = asiEstudiante;
    }

    public SgMotivoInasistencia getAsiMotivoInasistencia() {
        return asiMotivoInasistencia;
    }

    public void setAsiMotivoInasistencia(SgMotivoInasistencia asiMotivoInasistencia) {
        this.asiMotivoInasistencia = asiMotivoInasistencia;
    }

    public SgControlAsistenciaCabezal getAsiControl() {
        return asiControl;
    }

    public void setAsiControl(SgControlAsistenciaCabezal asiControl) {
        this.asiControl = asiControl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (asiPk != null ? asiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgAsistencia)) {
            return false;
        }
        SgAsistencia other = (SgAsistencia) object;
        if ((this.asiPk == null && other.asiPk != null) || (this.asiPk != null && !this.asiPk.equals(other.asiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAsistencia{" + "asiPk=" + asiPk + ", asiInasistencia=" + asiInasistencia + ", asiObservacion=" + asiObservacion + ", asiUltModFecha=" + asiUltModFecha + ", asiUltModUsuario=" + asiUltModUsuario + ", asiVersion=" + asiVersion + ", asiEstudiante=" + asiEstudiante + ", asiMotivoInasistencia=" + asiMotivoInasistencia + '}';
    }

    

}
