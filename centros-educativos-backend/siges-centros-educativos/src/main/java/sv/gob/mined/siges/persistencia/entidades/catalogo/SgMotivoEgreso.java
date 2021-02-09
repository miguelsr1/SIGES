/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_motivos_egreso", uniqueConstraints = {
    @UniqueConstraint(name = "meg_codigo_uk", columnNames = {"meg_codigo"})
    ,
    @UniqueConstraint(name = "meg_nombre_uk", columnNames = {"meg_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "megPk", scope = SgMotivoEgreso.class)
public class SgMotivoEgreso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "meg_pk", nullable = false)
    private Long megPk;

    @Size(max = 4)
    @Column(name = "meg_codigo", length = 4)
    @AtributoCodigo
    private String megCodigo;

    @Size(max = 255)
    @Column(name = "meg_nombre", length = 255)
    @AtributoNormalizable
    private String megNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "meg_nombre_busqueda", length = 255)
    private String megNombreBusqueda;

    @Column(name = "meg_habilitado")
    @AtributoHabilitado
    private Boolean megHabilitado;

    @Column(name = "meg_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime megUltModFecha;

    @Size(max = 45)
    @Column(name = "meg_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String megUltModUsuario;

    @Column(name = "meg_version")
    @Version
    private Integer megVersion;

    public SgMotivoEgreso() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.megNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.megNombre);
    }

    public Long getMegPk() {
        return megPk;
    }

    public void setMegPk(Long megPk) {
        this.megPk = megPk;
    }

    public String getMegCodigo() {
        return megCodigo;
    }

    public void setMegCodigo(String megCodigo) {
        this.megCodigo = megCodigo;
    }

    public String getMegNombre() {
        return megNombre;
    }

    public void setMegNombre(String megNombre) {
        this.megNombre = megNombre;
    }

    public String getMegNombreBusqueda() {
        return megNombreBusqueda;
    }

    public void setMegNombreBusqueda(String megNombreBusqueda) {
        this.megNombreBusqueda = megNombreBusqueda;
    }

    public Boolean getMegHabilitado() {
        return megHabilitado;
    }

    public void setMegHabilitado(Boolean megHabilitado) {
        this.megHabilitado = megHabilitado;
    }

    public LocalDateTime getMegUltModFecha() {
        return megUltModFecha;
    }

    public void setMegUltModFecha(LocalDateTime megUltModFecha) {
        this.megUltModFecha = megUltModFecha;
    }

    public String getMegUltModUsuario() {
        return megUltModUsuario;
    }

    public void setMegUltModUsuario(String megUltModUsuario) {
        this.megUltModUsuario = megUltModUsuario;
    }

    public Integer getMegVersion() {
        return megVersion;
    }

    public void setMegVersion(Integer megVersion) {
        this.megVersion = megVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.megPk);
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
        final SgMotivoEgreso other = (SgMotivoEgreso) obj;
        if (!Objects.equals(this.megPk, other.megPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesMotivoEgreso[ megPk=" + megPk + " ]";
    }

}
