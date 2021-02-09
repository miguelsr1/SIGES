/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
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
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */

@Entity
@Table(name = "sg_componentes_docentes", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cdoPk", scope = SgComponenteDocente.class)
@Audited
public class SgComponenteDocente implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cdo_pk")
    private Long cdoPk;
    
    @JoinColumn(name = "cdo_horario_escolar_fk", referencedColumnName = "hes_pk")
    @ManyToOne
    private SgHorarioEscolar cdoHorarioEscolar;
    
    @JoinColumn(name = "cdo_componente_fk", referencedColumnName = "cpe_pk")
    @ManyToOne
    private SgComponentePlanEstudio cdoComponente;
    
    @JoinColumn(name = "cdo_docente_fk", referencedColumnName = "pse_pk")
    @ManyToOne
    private SgPersonalSedeEducativaLite cdoDocente;
    
    @Column(name = "cdo_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cdoUltModFecha;
    
    @Size(max = 45)
    @Column(name = "cdo_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cdoUltModUsuario;
    
    @Column(name = "cdo_version")
    @Version
    private Integer cdoVersion;

    
    @Transient
    private Long eliminarContextoDePersonalPk;

    public SgComponenteDocente() {
    }

    public SgComponenteDocente(Long cdoPk) {
        this.cdoPk = cdoPk;
    }

    public Long getCdoPk() {
        return cdoPk;
    }

    public void setCdoPk(Long cdoPk) {
        this.cdoPk = cdoPk;
    }

    public SgHorarioEscolar getCdoHorarioEscolar() {
        return cdoHorarioEscolar;
    }

    public void setCdoHorarioEscolar(SgHorarioEscolar cdoHorarioEscolar) {
        this.cdoHorarioEscolar = cdoHorarioEscolar;
    }

    public SgComponentePlanEstudio getCdoComponente() {
        return cdoComponente;
    }

    public void setCdoComponente(SgComponentePlanEstudio cdoComponente) {
        this.cdoComponente = cdoComponente;
    }

    public SgPersonalSedeEducativaLite getCdoDocente() {
        return cdoDocente;
    }

    public void setCdoDocente(SgPersonalSedeEducativaLite cdoDocente) {
        this.cdoDocente = cdoDocente;
    }

    public LocalDateTime getCdoUltModFecha() {
        return cdoUltModFecha;
    }

    public void setCdoUltModFecha(LocalDateTime cdoUltModFecha) {
        this.cdoUltModFecha = cdoUltModFecha;
    }

    public String getCdoUltModUsuario() {
        return cdoUltModUsuario;
    }

    public void setCdoUltModUsuario(String cdoUltModUsuario) {
        this.cdoUltModUsuario = cdoUltModUsuario;
    }

    public Integer getCdoVersion() {
        return cdoVersion;
    }

    public void setCdoVersion(Integer cdoVersion) {
        this.cdoVersion = cdoVersion;
    }

    public Long getEliminarContextoDePersonalPk() {
        return eliminarContextoDePersonalPk;
    }

    public void setEliminarContextoDePersonalPk(Long eliminarContextoDePersonalPk) {
        this.eliminarContextoDePersonalPk = eliminarContextoDePersonalPk;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cdoPk != null ? cdoPk.hashCode() : 0);
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
        final SgComponenteDocente other = (SgComponenteDocente) obj;
        if (!Objects.equals(this.cdoPk, other.cdoPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgComponenteDocente[ cdoPk=" + cdoPk + " ]";
    }
    
}
