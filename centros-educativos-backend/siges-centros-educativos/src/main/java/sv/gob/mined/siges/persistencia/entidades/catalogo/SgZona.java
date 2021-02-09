/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
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
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_zonas", uniqueConstraints = {
    @UniqueConstraint(name = "zon_codigo_uk", columnNames = {"zon_codigo"})
    ,
    @UniqueConstraint(name = "zon_nombre_uk", columnNames = {"zon_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "zonPk", scope = SgZona.class)
public class SgZona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "zon_pk", nullable = false)
    private Long zonPk;

    @Size(max = 4)
    @Column(name = "zon_codigo", length = 4)
    @AtributoCodigo
    private String zonCodigo;

    @Size(max = 255)
    @Column(name = "zon_nombre", length = 255)
    @AtributoNormalizable
    private String zonNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "zon_nombre_busqueda", length = 255)
    private String zonNombreBusqueda;

    @Column(name = "zon_habilitado")
    @AtributoHabilitado
    private Boolean zonHabilitado;

    @Column(name = "zon_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime zonUltModFecha;

    @Size(max = 45)
    @Column(name = "zon_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String zonUltModUsuario;

    @Column(name = "zon_version")
    @Version
    private Integer zonVersion;

    public SgZona() {
    }

    public SgZona(Long zonPk, Integer zonVersion) {
        this.zonPk = zonPk;
        this.zonVersion = zonVersion;
    }
    
    

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.zonNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.zonNombre);
    }

    public Long getZonPk() {
        return zonPk;
    }

    public void setZonPk(Long zonPk) {
        this.zonPk = zonPk;
    }

    public String getZonCodigo() {
        return zonCodigo;
    }

    public void setZonCodigo(String zonCodigo) {
        this.zonCodigo = zonCodigo;
    }

    public String getZonNombre() {
        return zonNombre;
    }

    public void setZonNombre(String zonNombre) {
        this.zonNombre = zonNombre;
    }

    public String getZonNombreBusqueda() {
        return zonNombreBusqueda;
    }

    public void setZonNombreBusqueda(String zonNombreBusqueda) {
        this.zonNombreBusqueda = zonNombreBusqueda;
    }

    public Boolean getZonHabilitado() {
        return zonHabilitado;
    }

    public void setZonHabilitado(Boolean zonHabilitado) {
        this.zonHabilitado = zonHabilitado;
    }

    public LocalDateTime getZonUltModFecha() {
        return zonUltModFecha;
    }

    public void setZonUltModFecha(LocalDateTime zonUltModFecha) {
        this.zonUltModFecha = zonUltModFecha;
    }

    public String getZonUltModUsuario() {
        return zonUltModUsuario;
    }

    public void setZonUltModUsuario(String zonUltModUsuario) {
        this.zonUltModUsuario = zonUltModUsuario;
    }

    public Integer getZonVersion() {
        return zonVersion;
    }

    public void setZonVersion(Integer zonVersion) {
        this.zonVersion = zonVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.zonPk);
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
        final SgZona other = (SgZona) obj;
        if (!Objects.equals(this.zonPk, other.zonPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SigesZona{" + "zonPk=" + zonPk + '}';
    }

}
