/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoApoyo;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author bruno
 */
@Entity
@Table(name = "sg_datos_salud_estudiante", schema = Constantes.SCHEMA_CENTRO_EDUCATIVO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "dsePk", scope = SgDatoSaludEstudiante.class)
@Audited
public class SgDatoSaludEstudiante implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dse_pk")
    private Long dsePk;
    
    @JoinColumn(name = "dse_ale_fk", referencedColumnName = "ale_pk")
    @ManyToOne
    private SgAnioLectivo dseAnioLectivo;
    
    @JoinColumn(name = "dse_tap_fk", referencedColumnName = "tap_pk")
    @ManyToOne
    private SgTipoApoyo dseTipoApoyo;
    
    @JoinColumn(name = "dse_est_fk", referencedColumnName = "est_pk")
    @ManyToOne
    private SgEstudiante dseEstudiante;
    
    @Size(max = 255)
    @Column(name = "dse_descripcion", length = 255)
    private String dseDescripcion;
    
    @Column(name = "dse_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime dseUltModFecha;
    
    @Size(max = 45)
    @Column(name = "dse_ult_mod_usuario",length = 45)
    @AtributoUltimoUsuario
    private String dseUltModUsuario;
    
    @Column(name = "dse_version")
    @Version
    private Integer dseVersion;

    public Long getDsePk() {
        return dsePk;
    }

    public void setDsePk(Long dsePk) {
        this.dsePk = dsePk;
    }

    public SgAnioLectivo getDseAnioLectivo() {
        return dseAnioLectivo;
    }

    public void setDseAnioLectivo(SgAnioLectivo dseAnioLectivo) {
        this.dseAnioLectivo = dseAnioLectivo;
    }

    public SgTipoApoyo getDseTipoApoyo() {
        return dseTipoApoyo;
    }

    public void setDseTipoApoyo(SgTipoApoyo dseTipoApoyo) {
        this.dseTipoApoyo = dseTipoApoyo;
    }

    public SgEstudiante getDseEstudiante() {
        return dseEstudiante;
    }

    public void setDseEstudiante(SgEstudiante dseEstudiante) {
        this.dseEstudiante = dseEstudiante;
    }

    public String getDseDescripcion() {
        return dseDescripcion;
    }

    public void setDseDescripcion(String dseDescripcion) {
        this.dseDescripcion = dseDescripcion;
    }

    public LocalDateTime getDseUltModFecha() {
        return dseUltModFecha;
    }

    public void setDseUltModFecha(LocalDateTime dseUltModFecha) {
        this.dseUltModFecha = dseUltModFecha;
    }

    public String getDseUltModUsuario() {
        return dseUltModUsuario;
    }

    public void setDseUltModUsuario(String dseUltModUsuario) {
        this.dseUltModUsuario = dseUltModUsuario;
    }

    public Integer getDseVersion() {
        return dseVersion;
    }

    public void setDseVersion(Integer dseVersion) {
        this.dseVersion = dseVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.dsePk);
        hash = 97 * hash + Objects.hashCode(this.dseAnioLectivo);
        hash = 97 * hash + Objects.hashCode(this.dseTipoApoyo);
        hash = 97 * hash + Objects.hashCode(this.dseEstudiante);
        hash = 97 * hash + Objects.hashCode(this.dseDescripcion);
        hash = 97 * hash + Objects.hashCode(this.dseUltModFecha);
        hash = 97 * hash + Objects.hashCode(this.dseUltModUsuario);
        hash = 97 * hash + Objects.hashCode(this.dseVersion);
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
        final SgDatoSaludEstudiante other = (SgDatoSaludEstudiante) obj;
        if (!Objects.equals(this.dsePk, other.dsePk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgDatoSaludEstudiante{" + "dsePk=" + dsePk + '}';
    }
}
