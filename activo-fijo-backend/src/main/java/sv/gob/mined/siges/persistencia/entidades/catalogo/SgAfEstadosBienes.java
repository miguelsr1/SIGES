/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_af_estados_bienes", uniqueConstraints = {
    @UniqueConstraint(name = "ebi_codigo_uk", columnNames = {"ebi_codigo"}),
    @UniqueConstraint(name = "ebi_nombre_uk", columnNames = {"ebi_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ebiPk", resolver = JsonIdentityResolver.class, scope = SgAfEstadosBienes.class)
public class SgAfEstadosBienes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ebi_pk", nullable = false)
    private Long ebiPk;

    @Size(max = 4)
    @Column(name = "ebi_codigo", length = 4)
    @AtributoCodigo
    private String ebiCodigo;

    @Size(max = 255)
    @Column(name = "ebi_nombre", length = 255)
    @AtributoNormalizable
    private String ebiNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ebi_nombre_busqueda", length = 255)
    private String ebiNombreBusqueda;

    @Column(name = "ebi_habilitado")
    @AtributoHabilitado
    private Boolean ebiHabilitado;

    @Size(max = 255)
    @Column(name = "ebi_estilo", length = 255)
    private String ebiEstilo;
    
    @Column(name = "ebi_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ebiUltModFecha;

    @Size(max = 45)
    @Column(name = "ebi_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ebiUltModUsuario;

    @Column(name = "ebi_version")
    @Version
    private Integer ebiVersion;

    public SgAfEstadosBienes() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ebiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ebiNombre);
    }

    public Long getEbiPk() {
        return ebiPk;
    }

    public void setEbiPk(Long ebiPk) {
        this.ebiPk = ebiPk;
    }

    public String getEbiCodigo() {
        return ebiCodigo;
    }

    public void setEbiCodigo(String ebiCodigo) {
        this.ebiCodigo = ebiCodigo;
    }

    public String getEbiNombre() {
        return ebiNombre;
    }

    public void setEbiNombre(String ebiNombre) {
        this.ebiNombre = ebiNombre;
    }

    public String getEbiNombreBusqueda() {
        return ebiNombreBusqueda;
    }

    public void setEbiNombreBusqueda(String ebiNombreBusqueda) {
        this.ebiNombreBusqueda = ebiNombreBusqueda;
    }

    public Boolean getEbiHabilitado() {
        return ebiHabilitado;
    }

    public void setEbiHabilitado(Boolean ebiHabilitado) {
        this.ebiHabilitado = ebiHabilitado;
    }

    public LocalDateTime getEbiUltModFecha() {
        return ebiUltModFecha;
    }

    public void setEbiUltModFecha(LocalDateTime ebiUltModFecha) {
        this.ebiUltModFecha = ebiUltModFecha;
    }

    public String getEbiUltModUsuario() {
        return ebiUltModUsuario;
    }

    public void setEbiUltModUsuario(String ebiUltModUsuario) {
        this.ebiUltModUsuario = ebiUltModUsuario;
    }

    public Integer getEbiVersion() {
        return ebiVersion;
    }

    public void setEbiVersion(Integer ebiVersion) {
        this.ebiVersion = ebiVersion;
    }

    public String getEbiEstilo() {
        return ebiEstilo;
    }

    public void setEbiEstilo(String ebiEstilo) {
        this.ebiEstilo = ebiEstilo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.ebiPk);
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
        final SgAfEstadosBienes other = (SgAfEstadosBienes) obj;
        if (!Objects.equals(this.ebiPk, other.ebiPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstadosBienes[ ebiPk=" + ebiPk + " ]";
    }

}

