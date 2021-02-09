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
@Table(name = "sg_categoria_violencia", uniqueConstraints = {
    @UniqueConstraint(name = "cav_codigo_uk", columnNames = {"cav_codigo"})
    ,
    @UniqueConstraint(name = "cav_nombre_uk", columnNames = {"cav_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cavPk", scope = SgCategoriaViolencia.class)
@Audited
public class SgCategoriaViolencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cav_pk", nullable = false)
    private Long cavPk;

    @Size(max = 45)
    @Column(name = "cav_codigo", length = 45)
    @AtributoCodigo
    private String cavCodigo;

    @Size(max = 255)
    @Column(name = "cav_nombre", length = 255)
    @AtributoNormalizable
    private String cavNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "cav_nombre_busqueda", length = 255)
    private String cavNombreBusqueda;

    @Column(name = "cav_habilitado")
    @AtributoHabilitado
    private Boolean cavHabilitado;

    @Column(name = "cav_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cavUltModFecha;

    @Size(max = 45)
    @Column(name = "cav_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cavUltModUsuario;

    @Column(name = "cav_version")
    @Version
    private Integer cavVersion;

    public SgCategoriaViolencia() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.cavNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.cavNombre);
    }

    public Long getCavPk() {
        return cavPk;
    }

    public void setCavPk(Long cavPk) {
        this.cavPk = cavPk;
    }

    public String getCavCodigo() {
        return cavCodigo;
    }

    public void setCavCodigo(String cavCodigo) {
        this.cavCodigo = cavCodigo;
    }

    public String getCavNombre() {
        return cavNombre;
    }

    public void setCavNombre(String cavNombre) {
        this.cavNombre = cavNombre;
    }

    public String getCavNombreBusqueda() {
        return cavNombreBusqueda;
    }

    public void setCavNombreBusqueda(String cavNombreBusqueda) {
        this.cavNombreBusqueda = cavNombreBusqueda;
    }

    public Boolean getCavHabilitado() {
        return cavHabilitado;
    }

    public void setCavHabilitado(Boolean cavHabilitado) {
        this.cavHabilitado = cavHabilitado;
    }

    public LocalDateTime getCavUltModFecha() {
        return cavUltModFecha;
    }

    public void setCavUltModFecha(LocalDateTime cavUltModFecha) {
        this.cavUltModFecha = cavUltModFecha;
    }

    public String getCavUltModUsuario() {
        return cavUltModUsuario;
    }

    public void setCavUltModUsuario(String cavUltModUsuario) {
        this.cavUltModUsuario = cavUltModUsuario;
    }

    public Integer getCavVersion() {
        return cavVersion;
    }

    public void setCavVersion(Integer cavVersion) {
        this.cavVersion = cavVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.cavPk);
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
        final SgCategoriaViolencia other = (SgCategoriaViolencia) obj;
        if (!Objects.equals(this.cavPk, other.cavPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCategoriaViolencia{" + "cavPk=" + cavPk + ", cavCodigo=" + cavCodigo + ", cavNombre=" + cavNombre + ", cavNombreBusqueda=" + cavNombreBusqueda + ", cavHabilitado=" + cavHabilitado + ", cavUltModFecha=" + cavUltModFecha + ", cavUltModUsuario=" + cavUltModUsuario + ", cavVersion=" + cavVersion + '}';
    }
    
    

}
