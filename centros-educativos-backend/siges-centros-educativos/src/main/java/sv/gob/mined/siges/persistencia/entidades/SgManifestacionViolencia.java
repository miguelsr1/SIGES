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
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoManifestacion;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.persistencia.utilidades.Lookup;

@Entity
@Table(name = "sg_manifestacion_violencia", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "mviPk", scope = SgManifestacionViolencia.class)
public class SgManifestacionViolencia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mvi_pk")
    private Long mviPk;
    
    @Column(name = "mvi_fecha")
    private LocalDate mviFecha;
    
    @Size(max = 255)
    @Column(name = "mvi_observaciones")
    private String mviObservaciones;
    
    @Size(max = 500)
    @Column(name = "mvi_tratamiento")
    private String mviTratamiento;
    
    @Column(name = "mvi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mviUltModFecha;
    
    @Size(max = 45)
    @Column(name = "mvi_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String mviUltModUsuario;
    
    @Column(name = "mvi_version")
    @Version
    private Integer mviVersion;
    
    @JoinColumn(name = "mvi_estudiante", referencedColumnName = "est_pk")
    @ManyToOne
    private SgEstudiante mviEstudiante;
    
    @JoinColumn(name = "mvi_tipo_manifestacion", referencedColumnName = "tma_pk")
    @ManyToOne
    private SgTipoManifestacion mviTipoManifestacion;
    
    @Size(max = 45)
    @Column(name = "mvi_creacion_usuario")
    private String mviCreacionUsuario;

    public SgManifestacionViolencia() {
    }
    
    @PrePersist
    public void prePersist() throws Exception {
        this.mviCreacionUsuario = Lookup.obtenerJWT().getSubject();
    }
    
    public Long getMviPk() {
        return mviPk;
    }

    public void setMviPk(Long mviPk) {
        this.mviPk = mviPk;
    }

    public SgTipoManifestacion getMviTipoManifestacion() {
        return mviTipoManifestacion;
    }

    public void setMviTipoManifestacion(SgTipoManifestacion mviTipoManifestacion) {
        this.mviTipoManifestacion = mviTipoManifestacion;
    }

    public LocalDate getMviFecha() {
        return mviFecha;
    }

    public void setMviFecha(LocalDate mviFecha) {
        this.mviFecha = mviFecha;
    }

    public String getMviObservaciones() {
        return mviObservaciones;
    }

    public void setMviObservaciones(String mviObservaciones) {
        this.mviObservaciones = mviObservaciones;
    }

    public String getMviTratamiento() {
        return mviTratamiento;
    }

    public void setMviTratamiento(String mviTratamiento) {
        this.mviTratamiento = mviTratamiento;
    }

    public LocalDateTime getMviUltModFecha() {
        return mviUltModFecha;
    }

    public void setMviUltModFecha(LocalDateTime mviUltModFecha) {
        this.mviUltModFecha = mviUltModFecha;
    }

    public String getMviUltModUsuario() {
        return mviUltModUsuario;
    }

    public void setMviUltModUsuario(String mviUltModUsuario) {
        this.mviUltModUsuario = mviUltModUsuario;
    }

    public Integer getMviVersion() {
        return mviVersion;
    }

    public void setMviVersion(Integer mviVersion) {
        this.mviVersion = mviVersion;
    }

    public SgEstudiante getMviEstudiante() {
        return mviEstudiante;
    }

    public void setMviEstudiante(SgEstudiante mviEstudiante) {
        this.mviEstudiante = mviEstudiante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mviPk != null ? mviPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgManifestacionViolencia)) {
            return false;
        }
        SgManifestacionViolencia other = (SgManifestacionViolencia) object;
        if ((this.mviPk == null && other.mviPk != null) || (this.mviPk != null && !this.mviPk.equals(other.mviPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgManifestacionViolencia[ mviPk=" + mviPk + " ]";
    }

    public String getMviCreacionUsuario() {
        return mviCreacionUsuario;
    }

    public void setMviCreacionUsuario(String mviCreacionUsuario) {
        this.mviCreacionUsuario = mviCreacionUsuario;
    }
    
}
