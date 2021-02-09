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
import java.time.LocalTime;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumCeldaDiaHora;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_celda_dia_hora_curso", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cdcPk", scope = SgCeldaDiaHoraCurso.class)
public class SgCeldaDiaHoraCurso implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cdc_pk")
    private Long cdcPk;
    
    @JoinColumn(name = "cdc_curso_fk", referencedColumnName = "cds_pk")
    @ManyToOne
    private SgCursoDocente cdcCurso;
    
    @Column(name = "cdc_dia")
    private EnumCeldaDiaHora cdcDia;
    
    @Column(name = "cdc_fila")
    private Integer cdcFila;
    
    @Column(name = "cdc_hora")
    private LocalTime cdcHora;
    
    @Column(name = "cdc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cdcUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cdc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cdcUltModUsuario;
    
    @Column(name = "cdc_version")
    @Version
    private Integer cdcVersion;

    public SgCeldaDiaHoraCurso() {
    }

    public SgCeldaDiaHoraCurso(Long cdcPk) {
        this.cdcPk = cdcPk;
    }

    public Long getCdcPk() {
        return cdcPk;
    }

    public void setCdcPk(Long cdcPk) {
        this.cdcPk = cdcPk;
    }

    public SgCursoDocente getCdcCurso() {
        return cdcCurso;
    }

    public void setCdcCurso(SgCursoDocente cdcCurso) {
        this.cdcCurso = cdcCurso;
    }

    public EnumCeldaDiaHora getCdcDia() {
        return cdcDia;
    }

    public void setCdcDia(EnumCeldaDiaHora cdcDia) {
        this.cdcDia = cdcDia;
    }

    public Integer getCdcFila() {
        return cdcFila;
    }

    public void setCdcFila(Integer cdcFila) {
        this.cdcFila = cdcFila;
    }

    public LocalTime getCdcHora() {
        return cdcHora;
    }

    public void setCdcHora(LocalTime cdcHora) {
        this.cdcHora = cdcHora;
    }

    public LocalDateTime getCdcUltModFecha() {
        return cdcUltModFecha;
    }

    public void setCdcUltModFecha(LocalDateTime cdcUltModFecha) {
        this.cdcUltModFecha = cdcUltModFecha;
    }

    public String getCdcUltModUsuario() {
        return cdcUltModUsuario;
    }

    public void setCdcUltModUsuario(String cdcUltModUsuario) {
        this.cdcUltModUsuario = cdcUltModUsuario;
    }

    public Integer getCdcVersion() {
        return cdcVersion;
    }

    public void setCdcVersion(Integer cdcVersion) {
        this.cdcVersion = cdcVersion;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cdcPk != null ? cdcPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgCeldaDiaHoraCurso)) {
            return false;
        }
        SgCeldaDiaHoraCurso other = (SgCeldaDiaHoraCurso) object;
        if ((this.cdcPk == null && other.cdcPk != null) || (this.cdcPk != null && !this.cdcPk.equals(other.cdcPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgCeldaDiaHoraCurso[ cdcPk=" + cdcPk + " ]";
    }
    
}
