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
import java.time.LocalDate;
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
@Table(name = "sg_alcance_extraccion", schema = Constantes.SCHEMA_ESTADISTICAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "alcPk", scope = SgAlcanceExtraccion.class)
@Audited
public class SgAlcanceExtraccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "alc_pk", nullable = false)
    private Long alcPk;

    @Column(name = "alc_organizacion")
    private Long alcOrganizacionPk;

    @Column(name = "alc_nivel")
    private Long alcNivelPk;
    
    @Column(name = "alc_ciclo")
    private Long alcCicloPk;
    
    @Column(name = "alc_modalidad")
    private Long alcModalidadPk;
    
    @Column(name = "alc_modalidad_atencion")
    private Long alcModalidadAtencionPk;
    
    @Column(name = "alc_submodalidad_atencion")
    private Long alcSubModalidadAtencionPk;
    
    @Column(name = "alc_grado")
    private Long alcGradoPk;
    
    @Column(name = "alc_fecha_matriculas")
    private LocalDate alcFechaMatriculas;
    
    @JoinColumn(name = "alc_extraccion")
    @ManyToOne
    private SgExtraccion alcExtraccion;
   
    @Column(name = "alc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime alcUltModFecha;

    @Size(max = 45)
    @Column(name = "alc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String alcUltModUsuario;

    @Column(name = "alc_version")
    @Version
    private Integer alcVersion;

    public SgAlcanceExtraccion() {
    }

    public Long getAlcPk() {
        return alcPk;
    }

    public void setAlcPk(Long alcPk) {
        this.alcPk = alcPk;
    }

    public Long getAlcOrganizacionPk() {
        return alcOrganizacionPk;
    }

    public void setAlcOrganizacionPk(Long alcOrganizacionPk) {
        this.alcOrganizacionPk = alcOrganizacionPk;
    }

    public Long getAlcNivelPk() {
        return alcNivelPk;
    }

    public void setAlcNivelPk(Long alcNivelPk) {
        this.alcNivelPk = alcNivelPk;
    }

    public Long getAlcCicloPk() {
        return alcCicloPk;
    }

    public void setAlcCicloPk(Long alcCicloPk) {
        this.alcCicloPk = alcCicloPk;
    }

    public Long getAlcModalidadPk() {
        return alcModalidadPk;
    }

    public void setAlcModalidadPk(Long alcModalidadPk) {
        this.alcModalidadPk = alcModalidadPk;
    }

    public Long getAlcModalidadAtencionPk() {
        return alcModalidadAtencionPk;
    }

    public void setAlcModalidadAtencionPk(Long alcModalidadAtencionPk) {
        this.alcModalidadAtencionPk = alcModalidadAtencionPk;
    }

    public Long getAlcSubModalidadAtencionPk() {
        return alcSubModalidadAtencionPk;
    }

    public void setAlcSubModalidadAtencionPk(Long alcSubModalidadAtencionPk) {
        this.alcSubModalidadAtencionPk = alcSubModalidadAtencionPk;
    }

    public LocalDate getAlcFechaMatriculas() {
        return alcFechaMatriculas;
    }

    public void setAlcFechaMatriculas(LocalDate alcFechaMatriculas) {
        this.alcFechaMatriculas = alcFechaMatriculas;
    }

    public LocalDateTime getAlcUltModFecha() {
        return alcUltModFecha;
    }

    public void setAlcUltModFecha(LocalDateTime alcUltModFecha) {
        this.alcUltModFecha = alcUltModFecha;
    }

    public String getAlcUltModUsuario() {
        return alcUltModUsuario;
    }

    public void setAlcUltModUsuario(String alcUltModUsuario) {
        this.alcUltModUsuario = alcUltModUsuario;
    }

    public Integer getAlcVersion() {
        return alcVersion;
    }

    public void setAlcVersion(Integer alcVersion) {
        this.alcVersion = alcVersion;
    }

    public SgExtraccion getAlcExtraccion() {
        return alcExtraccion;
    }

    public void setAlcExtraccion(SgExtraccion alcExtraccion) {
        this.alcExtraccion = alcExtraccion;
    }

    public Long getAlcGradoPk() {
        return alcGradoPk;
    }

    public void setAlcGradoPk(Long alcGradoPk) {
        this.alcGradoPk = alcGradoPk;
    }

    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.alcPk);
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
        final SgAlcanceExtraccion other = (SgAlcanceExtraccion) obj;
        if (!Objects.equals(this.alcPk, other.alcPk)) {
            return false;
        }
        return true;
    }


}
