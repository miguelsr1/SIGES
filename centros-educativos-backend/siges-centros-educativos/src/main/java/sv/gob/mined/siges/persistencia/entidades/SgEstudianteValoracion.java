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
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.enumerados.TipoValoracion;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

@Entity
@Table(name = "sg_estudiantes_valoracion", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "esvPk", scope = SgEstudianteValoracion.class)
@Audited
public class SgEstudianteValoracion implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "esv_pk")
    private Long esvPk;
    
    @Column(name = "esv_enum_visible",length = 20)
    private TipoValoracion esvTipoValoracion;
    
    @Column(name = "esv_fecha_publicacion")
    private LocalDate esvFechaPublicacion;

    @Column(name = "esv_valoracion", length = 500)
    private String esvValoracion;
    
    @JoinColumn(name = "esv_estudiante_fk")
    @ManyToOne
    private SgEstudiante esvEstudiante;
    
    @Column(name = "esv_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime esvUltModFecha;
    
    @Size(max = 45)
    @Column(name = "esv_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String esvUltModUsuario;
    
    @Column(name = "esv_version")
    @Version
    private Integer esvVersion;
    
    @Size(max = 45)
    @Column(name = "esv_creacion_usuario")
    private String esvCreacionUsuario;
    
    public SgEstudianteValoracion() {
    }

    @PrePersist
    public void prePersist() throws Exception {
        this.esvCreacionUsuario = Lookup.obtenerJWT().getSubject();
    }
    
    public Long getEsvPk() {
        return esvPk;
    }

    public void setEsvPk(Long esvPk) {
        this.esvPk = esvPk;
    }

    public LocalDate  getEsvFechaPublicacion() {
        return esvFechaPublicacion;
    }

    public void setEsvFechaPublicacion(LocalDate  esvFechaPublicacion) {
        this.esvFechaPublicacion = esvFechaPublicacion;
    }

    public String getEsvValoracion() {
        return esvValoracion;
    }

    public void setEsvValoracion(String esvValoracion) {
        this.esvValoracion = esvValoracion;
    }

    public LocalDateTime getEsvUltModFecha() {
        return esvUltModFecha;
    }

    public void setEsvUltModFecha(LocalDateTime esvUltModFecha) {
        this.esvUltModFecha = esvUltModFecha;
    }

    public String getEsvUltModUsuario() {
        return esvUltModUsuario;
    }

    public void setEsvUltModUsuario(String esvUltModUsuario) {
        this.esvUltModUsuario = esvUltModUsuario;
    }

    public Integer getEsvVersion() {
        return esvVersion;
    }

    public void setEsvVersion(Integer esvVersion) {
        this.esvVersion = esvVersion;
    }

    public TipoValoracion getEsvTipoValoracion() {
        return esvTipoValoracion;
    }

    public void setEsvTipoValoracion(TipoValoracion esvTipoValoracion) {
        this.esvTipoValoracion = esvTipoValoracion;
    }

    public SgEstudiante getEsvEstudiante() {
        return esvEstudiante;
    }

    public void setEsvEstudiante(SgEstudiante esvEstudiante) {
        this.esvEstudiante = esvEstudiante;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.esvPk);
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
        final SgEstudianteValoracion other = (SgEstudianteValoracion) obj;
        if (!Objects.equals(this.esvPk, other.esvPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgEstudianteValidacion{" + "esvPk=" + esvPk + '}';
    }

    public String getEsvCreacionUsuario() {
        return esvCreacionUsuario;
    }

    public void setEsvCreacionUsuario(String esvCreacionUsuario) {
        this.esvCreacionUsuario = esvCreacionUsuario;
    }
    
}
