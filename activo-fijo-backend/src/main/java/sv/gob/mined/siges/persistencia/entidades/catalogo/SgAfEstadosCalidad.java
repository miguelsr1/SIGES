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
@Table(name = "sg_af_estados_calidad", uniqueConstraints = {
    @UniqueConstraint(name = "eca_codigo_uk", columnNames = {"eca_codigo"})
    ,
    @UniqueConstraint(name = "eca_nombre_uk", columnNames = {"eca_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "ecaPk",resolver = JsonIdentityResolver.class,  scope = SgAfEstadosCalidad.class)
public class SgAfEstadosCalidad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eca_pk", nullable = false)
    private Long ecaPk;

    @Size(max = 4)
    @Column(name = "eca_codigo", length = 4)
    @AtributoCodigo
    private String ecaCodigo;

    @Size(max = 255)
    @Column(name = "eca_nombre", length = 255)
    @AtributoNormalizable
    private String ecaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "eca_nombre_busqueda", length = 255)
    private String ecaNombreBusqueda;

    @Column(name = "eca_habilitado")
    @AtributoHabilitado
    private Boolean ecaHabilitado;

    @Column(name = "eca_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ecaUltModFecha;

    @Size(max = 45)
    @Column(name = "eca_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ecaUltModUsuario;

    @Column(name = "eca_version")
    @Version
    private Integer ecaVersion;

    public SgAfEstadosCalidad() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ecaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ecaNombre);
    }

    public Long getEcaPk() {
        return ecaPk;
    }

    public void setEcaPk(Long ecaPk) {
        this.ecaPk = ecaPk;
    }

    public String getEcaCodigo() {
        return ecaCodigo;
    }

    public void setEcaCodigo(String ecaCodigo) {
        this.ecaCodigo = ecaCodigo;
    }

    public String getEcaNombre() {
        return ecaNombre;
    }

    public void setEcaNombre(String ecaNombre) {
        this.ecaNombre = ecaNombre;
    }

    public String getEcaNombreBusqueda() {
        return ecaNombreBusqueda;
    }

    public void setEcaNombreBusqueda(String ecaNombreBusqueda) {
        this.ecaNombreBusqueda = ecaNombreBusqueda;
    }

    public Boolean getEcaHabilitado() {
        return ecaHabilitado;
    }

    public void setEcaHabilitado(Boolean ecaHabilitado) {
        this.ecaHabilitado = ecaHabilitado;
    }

    public LocalDateTime getEcaUltModFecha() {
        return ecaUltModFecha;
    }

    public void setEcaUltModFecha(LocalDateTime ecaUltModFecha) {
        this.ecaUltModFecha = ecaUltModFecha;
    }

    public String getEcaUltModUsuario() {
        return ecaUltModUsuario;
    }

    public void setEcaUltModUsuario(String ecaUltModUsuario) {
        this.ecaUltModUsuario = ecaUltModUsuario;
    }

    public Integer getEcaVersion() {
        return ecaVersion;
    }

    public void setEcaVersion(Integer ecaVersion) {
        this.ecaVersion = ecaVersion;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgEstadosCalidad[ ecaPk=" + ecaPk + " ]";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.ecaPk);
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
        final SgAfEstadosCalidad other = (SgAfEstadosCalidad) obj;
        if (!Objects.equals(this.ecaPk, other.ecaPk)) {
            return false;
        }
        return true;
    }

}
