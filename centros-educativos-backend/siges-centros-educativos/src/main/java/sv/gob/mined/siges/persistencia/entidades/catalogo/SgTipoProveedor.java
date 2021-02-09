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
@Table(name = "sg_tipos_proveedor", uniqueConstraints = {
    @UniqueConstraint(name = "tpr_codigo_uk", columnNames = {"tpr_codigo"})
    ,
    @UniqueConstraint(name = "tpr_nombre_uk", columnNames = {"tpr_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tprPk", scope = SgTipoProveedor.class)
@Audited
public class SgTipoProveedor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tpr_pk", nullable = false)
    private Long tprPk;

    @Size(max = 45)
    @Column(name = "tpr_codigo", length = 45)
    @AtributoCodigo
    private String tprCodigo;

    @Size(max = 255)
    @Column(name = "tpr_nombre", length = 255)
    @AtributoNormalizable
    private String tprNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "tpr_nombre_busqueda", length = 255)
    private String tprNombreBusqueda;

    @Column(name = "tpr_habilitado")
    @AtributoHabilitado
    private Boolean tprHabilitado;

    @Column(name = "tpr_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tprUltModFecha;

    @Size(max = 45)
    @Column(name = "tpr_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String tprUltModUsuario;

    @Column(name = "tpr_version")
    @Version
    private Integer tprVersion;

    public SgTipoProveedor() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tprNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tprNombre);
    }

    public Long getTprPk() {
        return tprPk;
    }

    public void setTprPk(Long tprPk) {
        this.tprPk = tprPk;
    }

    public String getTprCodigo() {
        return tprCodigo;
    }

    public void setTprCodigo(String tprCodigo) {
        this.tprCodigo = tprCodigo;
    }

    public String getTprNombre() {
        return tprNombre;
    }

    public void setTprNombre(String tprNombre) {
        this.tprNombre = tprNombre;
    }

    public String getTprNombreBusqueda() {
        return tprNombreBusqueda;
    }

    public void setTprNombreBusqueda(String tprNombreBusqueda) {
        this.tprNombreBusqueda = tprNombreBusqueda;
    }

    public Boolean getTprHabilitado() {
        return tprHabilitado;
    }

    public void setTprHabilitado(Boolean tprHabilitado) {
        this.tprHabilitado = tprHabilitado;
    }

    public LocalDateTime getTprUltModFecha() {
        return tprUltModFecha;
    }

    public void setTprUltModFecha(LocalDateTime tprUltModFecha) {
        this.tprUltModFecha = tprUltModFecha;
    }

    public String getTprUltModUsuario() {
        return tprUltModUsuario;
    }

    public void setTprUltModUsuario(String tprUltModUsuario) {
        this.tprUltModUsuario = tprUltModUsuario;
    }

    public Integer getTprVersion() {
        return tprVersion;
    }

    public void setTprVersion(Integer tprVersion) {
        this.tprVersion = tprVersion;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.tprPk);
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
        final SgTipoProveedor other = (SgTipoProveedor) obj;
        if (!Objects.equals(this.tprPk, other.tprPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgTipoProveedor{" + "tprPk=" + tprPk + ", tprCodigo=" + tprCodigo + ", tprNombre=" + tprNombre + ", tprNombreBusqueda=" + tprNombreBusqueda + ", tprHabilitado=" + tprHabilitado + ", tprUltModFecha=" + tprUltModFecha + ", tprUltModUsuario=" + tprUltModUsuario + ", tprVersion=" + tprVersion + '}';
    }
    
    

}
