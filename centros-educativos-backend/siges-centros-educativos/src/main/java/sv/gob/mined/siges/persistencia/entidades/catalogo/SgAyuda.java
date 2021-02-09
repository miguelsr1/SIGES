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
@Table(name = "sg_ayudas", uniqueConstraints = {
    @UniqueConstraint(name = "ayu_codigo_uk", columnNames = {"ayu_codigo"})
    ,
    @UniqueConstraint(name = "ayu_nombre_uk", columnNames = {"ayu_nombre"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ayuPk", scope = SgAyuda.class)
public class SgAyuda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ayu_pk", nullable = false)
    private Long ayuPk;

    @Size(max = 45)
    @Column(name = "ayu_codigo", length = 45)
    @AtributoCodigo
    private String ayuCodigo;

    @Size(max = 4000)
    @Column(name = "ayu_texto_uso", length = 4000)
    private String ayuTextoUso;

    @Size(max = 4000)
    @Column(name = "ayu_texto_normativa", length = 4000)
    private String ayuTextoNormativa;

    @Size(max = 4000)
    @Column(name = "ayu_vinculos", length = 4000)
    private String ayuVinculos;

    @Size(max = 255)
    @Column(name = "ayu_nombre", length = 255)
    @AtributoNormalizable
    private String ayuNombre;

    @Size(max = 255)
    @AtributoNombre
    @Column(name = "ayu_nombre_busqueda", length = 255)
    private String ayuNombreBusqueda;

    @Column(name = "ayu_habilitado")
    @AtributoHabilitado
    private Boolean ayuHabilitado;

    @Column(name = "ayu_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime ayuUltModFecha;

    @Size(max = 45)
    @Column(name = "ayu_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String ayuUltModUsuario;

    @Column(name = "ayu_version")
    @Version
    private Integer ayuVersion;

    public SgAyuda() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        this.ayuNombreBusqueda = SofisStringUtils.normalizarParaBusqueda(this.ayuNombre);
    }

    public Long getAyuPk() {
        return ayuPk;
    }

    public void setAyuPk(Long ayuPk) {
        this.ayuPk = ayuPk;
    }

    public String getAyuCodigo() {
        return ayuCodigo;
    }

    public void setAyuCodigo(String ayuCodigo) {
        this.ayuCodigo = ayuCodigo;
    }

    public String getAyuTextoUso() {
        return ayuTextoUso;
    }

    public void setAyuTextoUso(String ayuTextoUso) {
        this.ayuTextoUso = ayuTextoUso;
    }

    public String getAyuTextoNormativa() {
        return ayuTextoNormativa;
    }

    public void setAyuTextoNormativa(String ayuTextoNormativa) {
        this.ayuTextoNormativa = ayuTextoNormativa;
    }

    public String getAyuVinculos() {
        return ayuVinculos;
    }

    public void setAyuVinculos(String ayuVinculos) {
        this.ayuVinculos = ayuVinculos;
    }
    

    public String getAyuNombre() {
        return ayuNombre;
    }

    public void setAyuNombre(String ayuNombre) {
        this.ayuNombre = ayuNombre;
    }

    public String getAyuNombreBusqueda() {
        return ayuNombreBusqueda;
    }

    public void setAyuNombreBusqueda(String ayuNombreBusqueda) {
        this.ayuNombreBusqueda = ayuNombreBusqueda;
    }

    public Boolean getAyuHabilitado() {
        return ayuHabilitado;
    }

    public void setAyuHabilitado(Boolean ayuHabilitado) {
        this.ayuHabilitado = ayuHabilitado;
    }

    public LocalDateTime getAyuUltModFecha() {
        return ayuUltModFecha;
    }

    public void setAyuUltModFecha(LocalDateTime ayuUltModFecha) {
        this.ayuUltModFecha = ayuUltModFecha;
    }

    public String getAyuUltModUsuario() {
        return ayuUltModUsuario;
    }

    public void setAyuUltModUsuario(String ayuUltModUsuario) {
        this.ayuUltModUsuario = ayuUltModUsuario;
    }

    public Integer getAyuVersion() {
        return ayuVersion;
    }

    public void setAyuVersion(Integer ayuVersion) {
        this.ayuVersion = ayuVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.ayuPk);
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
        final SgAyuda other = (SgAyuda) obj;
        if (!Objects.equals(this.ayuPk, other.ayuPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAyuda{" + "ayuPk=" + ayuPk + ", ayuCodigo=" + ayuCodigo + ", ayuNombre=" + ayuNombre + ", ayuNombreBusqueda=" + ayuNombreBusqueda + ", ayuHabilitado=" + ayuHabilitado + ", ayuUltModFecha=" + ayuUltModFecha + ", ayuUltModUsuario=" + ayuUltModUsuario + ", ayuVersion=" + ayuVersion + '}';
    }

}
