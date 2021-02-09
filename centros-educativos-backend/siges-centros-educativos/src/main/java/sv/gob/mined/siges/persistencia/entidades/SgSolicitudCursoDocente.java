/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudCurso;

@Entity
@Table(name = "sg_solicitud_curso_docente", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "scdPk", scope = SgServicioEducativo.class)
public class SgSolicitudCursoDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "scd_pk")
    private Long scdPk;
    
    @JoinColumn(name = "scd_curso_fk", referencedColumnName = "cds_pk")
    @ManyToOne
    private SgCursoDocente scdCurso;
    
    @JoinColumn(name = "scd_personal_fk", referencedColumnName = "pse_pk")
    @ManyToOne
    private SgPersonalSedeEducativa scdPersonal;
    
    @Column(name = "scd_estado")
    @Enumerated(value = EnumType.STRING)
    private EnumEstadoSolicitudCurso scdEstado;
    
    @Column(name = "scd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime scdUltModFecha;
    
    @Size(max = 45)
    @Column(name = "scd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String scdUltModUsuario;
    
    @Column(name = "scd_version")
    @Version
    private Integer scdVersion;

    public SgSolicitudCursoDocente() {
    }

    public SgSolicitudCursoDocente(Long scdPk) {
        this.scdPk = scdPk;
    }

    public Long getScdPk() {
        return scdPk;
    }

    public void setScdPk(Long scdPk) {
        this.scdPk = scdPk;
    }

    public SgCursoDocente getScdCurso() {
        return scdCurso;
    }

    public void setScdCurso(SgCursoDocente scdCurso) {
        this.scdCurso = scdCurso;
    }

    public SgPersonalSedeEducativa getScdPersonal() {
        return scdPersonal;
    }

    public void setScdPersonal(SgPersonalSedeEducativa scdPersonal) {
        this.scdPersonal = scdPersonal;
    }

    public EnumEstadoSolicitudCurso getScdEstado() {
        return scdEstado;
    }

    public void setScdEstado(EnumEstadoSolicitudCurso scdEstado) {
        this.scdEstado = scdEstado;
    }

    public LocalDateTime getScdUltModFecha() {
        return scdUltModFecha;
    }

    public void setScdUltModFecha(LocalDateTime scdUltModFecha) {
        this.scdUltModFecha = scdUltModFecha;
    }

    public String getScdUltModUsuario() {
        return scdUltModUsuario;
    }

    public void setScdUltModUsuario(String scdUltModUsuario) {
        this.scdUltModUsuario = scdUltModUsuario;
    }

    public Integer getScdVersion() {
        return scdVersion;
    }

    public void setScdVersion(Integer scdVersion) {
        this.scdVersion = scdVersion;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scdPk != null ? scdPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgSolicitudCursoDocente)) {
            return false;
        }
        SgSolicitudCursoDocente other = (SgSolicitudCursoDocente) object;
        if ((this.scdPk == null && other.scdPk != null) || (this.scdPk != null && !this.scdPk.equals(other.scdPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSolicitudCursoDocente[ scdPk=" + scdPk + " ]";
    }
    
}
