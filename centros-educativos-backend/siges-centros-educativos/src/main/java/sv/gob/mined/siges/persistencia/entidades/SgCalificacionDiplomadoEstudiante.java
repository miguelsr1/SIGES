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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgCalificacion;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_calificacion_diplomado_estudiante", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cdePk", scope = SgCalificacionDiplomadoEstudiante.class)
@Audited
public class SgCalificacionDiplomadoEstudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cde_pk", nullable = false)
    private Long cdePk;

    @JoinColumn(name = "cde_estudiante_fk", referencedColumnName = "est_pk")
    @ManyToOne
    private SgEstudiante cdeEstudianteFk;
    
    @JoinColumn(name = "cde_calificacion_diplomado_fk", referencedColumnName = "cad_pk")
    @ManyToOne
    private SgCalificacionDiplomado cdeCalificacionDiplomadoFk;
    
    @Column(name = "cde_calificacion_numerica")
    private String cdeCalificacionNumerica;
    
    @JoinColumn(name = "cde_calificacion_conceptual_fk", referencedColumnName = "cal_pk")
    @ManyToOne
    private SgCalificacion cdeCalificacionConceptualFk;
    
    @Column(name = "cde_observacion")
    private String cdeObservacion;
    
    @Column(name = "cde_fecha_realizado")
    private LocalDate cdeFechaRealizado;

    @Column(name = "cde_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cdeUltModFecha;

    @Size(max = 45)
    @Column(name = "cde_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cdeUltModUsuario;

    @Column(name = "cde_version")
    @Version
    private Integer cdeVersion;

    public SgCalificacionDiplomadoEstudiante() {
    }

 

    public Long getCdePk() {
        return cdePk;
    }

    public void setCdePk(Long cdePk) {
        this.cdePk = cdePk;
    }

    public SgEstudiante getCdeEstudianteFk() {
        return cdeEstudianteFk;
    }

    public void setCdeEstudianteFk(SgEstudiante cdeEstudianteFk) {
        this.cdeEstudianteFk = cdeEstudianteFk;
    }

    public SgCalificacionDiplomado getCdeCalificacionDiplomadoFk() {
        return cdeCalificacionDiplomadoFk;
    }

    public void setCdeCalificacionDiplomadoFk(SgCalificacionDiplomado cdeCalificacionDiplomadoFk) {
        this.cdeCalificacionDiplomadoFk = cdeCalificacionDiplomadoFk;
    }

    public String getCdeCalificacionNumerica() {
        return cdeCalificacionNumerica;
    }

    public void setCdeCalificacionNumerica(String cdeCalificacionNumerica) {
        this.cdeCalificacionNumerica = cdeCalificacionNumerica;
    }

    public SgCalificacion getCdeCalificacionConceptualFk() {
        return cdeCalificacionConceptualFk;
    }

    public void setCdeCalificacionConceptualFk(SgCalificacion cdeCalificacionConceptualFk) {
        this.cdeCalificacionConceptualFk = cdeCalificacionConceptualFk;
    }

    public String getCdeObservacion() {
        return cdeObservacion;
    }

    public void setCdeObservacion(String cdeObservacion) {
        this.cdeObservacion = cdeObservacion;
    }

    public LocalDate getCdeFechaRealizado() {
        return cdeFechaRealizado;
    }

    public void setCdeFechaRealizado(LocalDate cdeFechaRealizado) {
        this.cdeFechaRealizado = cdeFechaRealizado;
    }


    public LocalDateTime getCdeUltModFecha() {
        return cdeUltModFecha;
    }

    public void setCdeUltModFecha(LocalDateTime cdeUltModFecha) {
        this.cdeUltModFecha = cdeUltModFecha;
    }

    public String getCdeUltModUsuario() {
        return cdeUltModUsuario;
    }

    public void setCdeUltModUsuario(String cdeUltModUsuario) {
        this.cdeUltModUsuario = cdeUltModUsuario;
    }

    public Integer getCdeVersion() {
        return cdeVersion;
    }

    public void setCdeVersion(Integer cdeVersion) {
        this.cdeVersion = cdeVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cdePk);
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
        final SgCalificacionDiplomadoEstudiante other = (SgCalificacionDiplomadoEstudiante) obj;
        if (!Objects.equals(this.cdePk, other.cdePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCalificacionDiplomadoEstudiante{" + "cdePk=" + cdePk +  ", cdeUltModFecha=" + cdeUltModFecha + ", cdeUltModUsuario=" + cdeUltModUsuario + ", cdeVersion=" + cdeVersion + '}';
    }
    
    

}
