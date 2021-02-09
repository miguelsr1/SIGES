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
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.utils.SofisStringUtils;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_paises", uniqueConstraints = {
    @UniqueConstraint(name = "pai_codigo_uk", columnNames = {"pai_codigo"})
    ,
    @UniqueConstraint(name = "pai_nombre_uk", columnNames = {"pai_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "paiPk", scope = SgPais.class)
public class SgPais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pai_pk", nullable = false)
    private Long paiPk;

    @Size(max = 4)
    @Column(name = "pai_codigo", length = 4)
    @AtributoCodigo
    private String paiCodigo;

    @Size(max = 255)
    @Column(name = "pai_nombre", length = 255)
    @AtributoNormalizable
    private String paiNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "pai_nombre_busqueda", length = 255)
    private String paiNombreBusqueda;

    @Column(name = "pai_habilitado")
    @AtributoHabilitado
    private Boolean paiHabilitado;

    @Column(name = "pai_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime paiUltModFecha;

    @Size(max = 45)
    @Column(name = "pai_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String paiUltModUsuario;

    @Column(name = "pai_version")
    @Version
    private Integer paiVersion;

    public SgPais() {
    }

    public SgPais(Long paiPk, Integer paiVersion) {
        this.paiPk = paiPk;
        this.paiVersion = paiVersion;
    }
    
    

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.paiNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.paiNombre);
    }

    public Long getPaiPk() {
        return paiPk;
    }

    public void setPaiPk(Long paiPk) {
        this.paiPk = paiPk;
    }

    public String getPaiCodigo() {
        return paiCodigo;
    }

    public void setPaiCodigo(String paiCodigo) {
        this.paiCodigo = paiCodigo;
    }

    public String getPaiNombre() {
        return paiNombre;
    }

    public void setPaiNombre(String paiNombre) {
        this.paiNombre = paiNombre;
    }

    public String getPaiNombreBusqueda() {
        return paiNombreBusqueda;
    }

    public void setPaiNombreBusqueda(String paiNombreBusqueda) {
        this.paiNombreBusqueda = paiNombreBusqueda;
    }

    public Boolean getPaiHabilitado() {
        return paiHabilitado;
    }

    public void setPaiHabilitado(Boolean paiHabilitado) {
        this.paiHabilitado = paiHabilitado;
    }

    public LocalDateTime getPaiUltModFecha() {
        return paiUltModFecha;
    }

    public void setPaiUltModFecha(LocalDateTime paiUltModFecha) {
        this.paiUltModFecha = paiUltModFecha;
    }

    public String getPaiUltModUsuario() {
        return paiUltModUsuario;
    }

    public void setPaiUltModUsuario(String paiUltModUsuario) {
        this.paiUltModUsuario = paiUltModUsuario;
    }

    public Integer getPaiVersion() {
        return paiVersion;
    }

    public void setPaiVersion(Integer paiVersion) {
        this.paiVersion = paiVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.paiPk);
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
        final SgPais other = (SgPais) obj;
        if (!Objects.equals(this.paiPk, other.paiPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SigesTipoCalendarioEscolar{" + "paiPk=" + paiPk + ", paiCodigo=" + paiCodigo + ", paiNombre=" + paiNombre + ", paiNombreBusqueda=" + paiNombreBusqueda + ", paiHabilitado=" + paiHabilitado + ", paiUltModFecha=" + paiUltModFecha + ", paiUltModUsuario=" + paiUltModUsuario + ", paiVersion=" + paiVersion + '}';
    }

}
