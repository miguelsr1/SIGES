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
@Table(name = "sg_af_estados_traslado", uniqueConstraints = {
    @UniqueConstraint(name = "etr_codigo_uk", columnNames = {"etr_codigo"})
    ,
    @UniqueConstraint(name = "etr_nombre_uk", columnNames = {"etr_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "etrPk", resolver = JsonIdentityResolver.class, scope = SgAfEstadosTraslado.class)
public class SgAfEstadosTraslado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "etr_pk", nullable = false)
    private Long etrPk;

    @Size(max = 4)
    @Column(name = "etr_codigo", length = 4)
    @AtributoCodigo
    private String etrCodigo;

    @Size(max = 255)
    @Column(name = "etr_nombre", length = 255)
    @AtributoNormalizable
    private String etrNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "etr_nombre_busqueda", length = 255)
    private String etrNombreBusqueda;

    @Column(name = "etr_habilitado")
    @AtributoHabilitado
    private Boolean etrHabilitado;

    @Column(name = "etr_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime etrUltModFecha;

    @Size(max = 45)
    @Column(name = "etr_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String etrUltModUsuario;

    @Column(name = "etr_version")
    @Version
    private Integer etrVersion;

    public SgAfEstadosTraslado() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.etrNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.etrNombre);
    }

    public Long getEtrPk() {
        return etrPk;
    }

    public void setEtrPk(Long etrPk) {
        this.etrPk = etrPk;
    }

    public String getEtrCodigo() {
        return etrCodigo;
    }

    public void setEtrCodigo(String etrCodigo) {
        this.etrCodigo = etrCodigo;
    }

    public String getEtrNombre() {
        return etrNombre;
    }

    public void setEtrNombre(String etrNombre) {
        this.etrNombre = etrNombre;
    }

    public String getEtrNombreBusqueda() {
        return etrNombreBusqueda;
    }

    public void setEtrNombreBusqueda(String etrNombreBusqueda) {
        this.etrNombreBusqueda = etrNombreBusqueda;
    }

    public Boolean getEtrHabilitado() {
        return etrHabilitado;
    }

    public void setEtrHabilitado(Boolean etrHabilitado) {
        this.etrHabilitado = etrHabilitado;
    }

    public LocalDateTime getEtrUltModFecha() {
        return etrUltModFecha;
    }

    public void setEtrUltModFecha(LocalDateTime etrUltModFecha) {
        this.etrUltModFecha = etrUltModFecha;
    }

    public String getEtrUltModUsuario() {
        return etrUltModUsuario;
    }

    public void setEtrUltModUsuario(String etrUltModUsuario) {
        this.etrUltModUsuario = etrUltModUsuario;
    }

    public Integer getEtrVersion() {
        return etrVersion;
    }

    public void setEtrVersion(Integer etrVersion) {
        this.etrVersion = etrVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.etrPk);
        hash = 97 * hash + Objects.hashCode(this.etrCodigo);
        hash = 97 * hash + Objects.hashCode(this.etrNombre);
        hash = 97 * hash + Objects.hashCode(this.etrNombreBusqueda);
        hash = 97 * hash + Objects.hashCode(this.etrHabilitado);
        hash = 97 * hash + Objects.hashCode(this.etrUltModFecha);
        hash = 97 * hash + Objects.hashCode(this.etrUltModUsuario);
        hash = 97 * hash + Objects.hashCode(this.etrVersion);
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
        final SgAfEstadosTraslado other = (SgAfEstadosTraslado) obj;
        if (!Objects.equals(this.etrPk, other.etrPk)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstadosTraslado[ etrPk=" + etrPk + " ]";
    }

}
