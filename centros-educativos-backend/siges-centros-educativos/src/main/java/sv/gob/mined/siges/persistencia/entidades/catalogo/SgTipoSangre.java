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
@Table(name = "sg_tipos_sangre", uniqueConstraints = {
    @UniqueConstraint(name = "tsa_codigo_uk", columnNames = {"tsa_codigo"})
    ,
    @UniqueConstraint(name = "tsa_nombre_uk", columnNames = {"tsa_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tsaPk", scope = SgTipoSangre.class)
public class SgTipoSangre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tsa_pk")
    private Long tsaPk;

    @Size(max = 4)
    @Column(name = "tsa_codigo", length = 4)
    @AtributoCodigo
    private String tsaCodigo;

    @Size(max = 255)
    @Column(name = "tsa_nombre", length = 20)
    @AtributoNormalizable
    private String tsaNombre;

    @Size(max = 255)
    @Column(name = "tsa_nombre_busqueda", length = 20)
    @AtributoNombre
    private String tsaNombreBusqueda;

    @Column(name = "tsa_habilitado")
    @AtributoHabilitado
    private Boolean tsaHabilitado;

    @Column(name = "tsa_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime tsaUltModFecha;

    @Size(max = 45)
    @Column(name = "tsa_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String tsaUltModUsuario;

    @Column(name = "tsa_version")
    @Version
    private Integer tsaVersion;

    public SgTipoSangre() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.tsaNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.tsaNombre);
    }

    public String getTsaNombreBusqueda() {
        return tsaNombreBusqueda;
    }

    public void setTsaNombreBusqueda(String tsaNombreBusqueda) {
        this.tsaNombreBusqueda = tsaNombreBusqueda;
    }

    public Long getTsaPk() {
        return tsaPk;
    }

    public void setTsaPk(Long tsaPk) {
        this.tsaPk = tsaPk;
    }

    public String getTsaCodigo() {
        return tsaCodigo;
    }

    public void setTsaCodigo(String tsaCodigo) {
        this.tsaCodigo = tsaCodigo;
    }

    public String getTsaNombre() {
        return tsaNombre;
    }

    public void setTsaNombre(String tsaNombre) {
        this.tsaNombre = tsaNombre;
    }

    public Boolean getTsaHabilitado() {
        return tsaHabilitado;
    }

    public void setTsaHabilitado(Boolean tsaHabilitado) {
        this.tsaHabilitado = tsaHabilitado;
    }

    public LocalDateTime getTsaUltModFecha() {
        return tsaUltModFecha;
    }

    public void setTsaUltModFecha(LocalDateTime tsaUltModFecha) {
        this.tsaUltModFecha = tsaUltModFecha;
    }

    public String getTsaUltModUsuario() {
        return tsaUltModUsuario;
    }

    public void setTsaUltModUsuario(String tsaUltModUsuario) {
        this.tsaUltModUsuario = tsaUltModUsuario;
    }

    public Integer getTsaVersion() {
        return tsaVersion;
    }

    public void setTsaVersion(Integer tsaVersion) {
        this.tsaVersion = tsaVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tsaPk != null ? tsaPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgTipoSangre)) {
            return false;
        }
        SgTipoSangre other = (SgTipoSangre) object;
        if ((this.tsaPk == null && other.tsaPk != null) || (this.tsaPk != null && !this.tsaPk.equals(other.tsaPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesTipoSangre[ tsaPk=" + tsaPk + " ]";
    }

}
