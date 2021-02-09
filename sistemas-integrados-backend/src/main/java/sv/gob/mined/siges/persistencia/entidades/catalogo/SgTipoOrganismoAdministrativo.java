/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.catalogo;

import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;
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
import javax.persistence.UniqueConstraint;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_tipos_organismo_administrativo", uniqueConstraints = {
    @UniqueConstraint(name = "toa_codigo_uk", columnNames = {"toa_codigo"})
    ,
    @UniqueConstraint(name = "toa_nombre_uk", columnNames = {"toa_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "toaPk", scope = SgTipoOrganismoAdministrativo.class)
@Audited
public class SgTipoOrganismoAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "toa_pk", nullable = false)
    private Long toaPk;

    @Size(max = 45)
    @Column(name = "toa_codigo", length = 45)
    @AtributoCodigo
    private String toaCodigo;

    @Size(max = 255)
    @Column(name = "toa_nombre", length = 255)
    @AtributoNormalizable
    private String toaNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "toa_nombre_busqueda", length = 255)
    private String toaNombreBusqueda;

    @Column(name = "toa_habilitado")
    @AtributoHabilitado
    private Boolean toaHabilitado;

    @Column(name = "toa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime toaUltModFecha;

    @Size(max = 45)
    @Column(name = "toa_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String toaUltModUsuario;

    @Column(name = "toa_version")
    @Version
    private Integer toaVersion;

    public SgTipoOrganismoAdministrativo() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.toaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.toaNombre);
    }

    public Long getToaPk() {
        return toaPk;
    }

    public void setToaPk(Long toaPk) {
        this.toaPk = toaPk;
    }

    public String getToaCodigo() {
        return toaCodigo;
    }

    public void setToaCodigo(String toaCodigo) {
        this.toaCodigo = toaCodigo;
    }

    public String getToaNombre() {
        return toaNombre;
    }

    public void setToaNombre(String toaNombre) {
        this.toaNombre = toaNombre;
    }

    public String getToaNombreBusqueda() {
        return toaNombreBusqueda;
    }

    public void setToaNombreBusqueda(String toaNombreBusqueda) {
        this.toaNombreBusqueda = toaNombreBusqueda;
    }

    public Boolean getToaHabilitado() {
        return toaHabilitado;
    }

    public void setToaHabilitado(Boolean toaHabilitado) {
        this.toaHabilitado = toaHabilitado;
    }

    public LocalDateTime getToaUltModFecha() {
        return toaUltModFecha;
    }

    public void setToaUltModFecha(LocalDateTime toaUltModFecha) {
        this.toaUltModFecha = toaUltModFecha;
    }

    public String getToaUltModUsuario() {
        return toaUltModUsuario;
    }

    public void setToaUltModUsuario(String toaUltModUsuario) {
        this.toaUltModUsuario = toaUltModUsuario;
    }

    public Integer getToaVersion() {
        return toaVersion;
    }

    public void setToaVersion(Integer toaVersion) {
        this.toaVersion = toaVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.toaPk);
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
        final SgTipoOrganismoAdministrativo other = (SgTipoOrganismoAdministrativo) obj;
        if (!Objects.equals(this.toaPk, other.toaPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoOrganismoAdministrativo{" + "toaPk=" + toaPk + ", toaCodigo=" + toaCodigo + ", toaNombre=" + toaNombre + ", toaNombreBusqueda=" + toaNombreBusqueda + ", toaHabilitado=" + toaHabilitado + ", toaUltModFecha=" + toaUltModFecha + ", toaUltModUsuario=" + toaUltModUsuario + ", toaVersion=" + toaVersion + '}';
    }
    
    

}
