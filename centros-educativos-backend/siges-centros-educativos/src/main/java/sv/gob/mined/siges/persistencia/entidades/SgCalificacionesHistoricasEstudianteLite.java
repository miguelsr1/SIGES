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
@Table(name = "sg_calificaciones_historicas_estudiante", schema = Constantes.SCHEMA_ACREDITACION)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "chePk", scope = SgCalificacionesHistoricasEstudianteLite.class)
@Audited
public class SgCalificacionesHistoricasEstudianteLite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "che_pk", nullable = false)
    private Long chePk;
    
    @JoinColumn(name = "che_estudiante_fk")
    @ManyToOne(optional = false)
    private SgEstudiante cheEstudianteFk;
       
    @Column(name = "che_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cheUltModFecha;

    @Size(max = 45)
    @Column(name = "che_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cheUltModUsuario;

    @Column(name = "che_version")
    @Version
    private Integer cheVersion;
    
    @Column(name = "che_estudiante_nie")
    private Long cheEstudianteNie;
    
    @JoinColumn(name = "che_estudiante_per_pk")
    @ManyToOne(optional = false)
    private SgPersona chePersonaFk;


    public SgCalificacionesHistoricasEstudianteLite() {
    }

    public Long getChePk() {
        return chePk;
    }

    public void setChePk(Long chePk) {
        this.chePk = chePk;
    }

    public SgEstudiante getCheEstudianteFk() {
        return cheEstudianteFk;
    }

    public void setCheEstudianteFk(SgEstudiante cheEstudianteFk) {
        this.cheEstudianteFk = cheEstudianteFk;
    }

    public LocalDateTime getCheUltModFecha() {
        return cheUltModFecha;
    }

    public void setCheUltModFecha(LocalDateTime cheUltModFecha) {
        this.cheUltModFecha = cheUltModFecha;
    }

    public String getCheUltModUsuario() {
        return cheUltModUsuario;
    }

    public void setCheUltModUsuario(String cheUltModUsuario) {
        this.cheUltModUsuario = cheUltModUsuario;
    }

    public Integer getCheVersion() {
        return cheVersion;
    }

    public void setCheVersion(Integer cheVersion) {
        this.cheVersion = cheVersion;
    }

    public Long getCheEstudianteNie() {
        return cheEstudianteNie;
    }

    public void setCheEstudianteNie(Long cheEstudianteNie) {
        this.cheEstudianteNie = cheEstudianteNie;
    }

    public SgPersona getChePersonaFk() {
        return chePersonaFk;
    }

    public void setChePersonaFk(SgPersona chePersonaFk) {
        this.chePersonaFk = chePersonaFk;
    }
    
    

    @Override
    public int hashCode() {
        return Objects.hashCode(this.chePk);
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
        final SgCalificacionesHistoricasEstudianteLite other = (SgCalificacionesHistoricasEstudianteLite) obj;
        if (!Objects.equals(this.chePk, other.chePk)) {
            return false;
        }
        return true;
    }
   

    @Override
    public String toString() {
        return "SgCalificacionesHistoricasEstudiante{" + "chePk=" + chePk +  ", cheUltModFecha=" + cheUltModFecha + ", cheUltModUsuario=" + cheUltModUsuario + ", cheVersion=" + cheVersion + '}';
    }
    
    

}
