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
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_etiquetas", uniqueConstraints = {
    @UniqueConstraint(name = "eti_codigo_uk", columnNames = {"eti_codigo"})}, schema = Constantes.SCHEMA_CATALOGO)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "etiPk", scope = SgEtiqueta.class)
public class SgEtiqueta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "eti_pk")
    private Long etiPk;

    @Size(max = 45)
    @Column(name = "eti_codigo", length = 45)
    @AtributoCodigo
    private String etiCodigo;

    @Size(max = 255)
    @Column(name = "eti_valor", length = 255)
    private String etiValor;

    @Column(name = "eti_habilitado")
    @AtributoHabilitado
    private Boolean etiHabilitado;

    @Column(name = "eti_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime etiUltModFecha;

    @Size(max = 45)
    @Column(name = "eti_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String etiUltModUsuario;

    @Column(name = "eti_version")
    @Version
    private Integer etiVersion;

    public SgEtiqueta() {
    }

    public Long getEtiPk() {
        return etiPk;
    }

    public void setEtiPk(Long etiPk) {
        this.etiPk = etiPk;
    }

    public String getEtiCodigo() {
        return etiCodigo;
    }

    public void setEtiCodigo(String etiCodigo) {
        this.etiCodigo = etiCodigo;
    }

    public String getEtiValor() {
        return etiValor;
    }

    public void setEtiValor(String etiValor) {
        this.etiValor = etiValor;
    }

    public Boolean getEtiHabilitado() {
        return etiHabilitado;
    }

    public void setEtiHabilitado(Boolean etiHabilitado) {
        this.etiHabilitado = etiHabilitado;
    }

    public LocalDateTime getEtiUltModFecha() {
        return etiUltModFecha;
    }

    public void setEtiUltModFecha(LocalDateTime etiUltModFecha) {
        this.etiUltModFecha = etiUltModFecha;
    }

    public String getEtiUltModUsuario() {
        return etiUltModUsuario;
    }

    public void setEtiUltModUsuario(String etiUltModUsuario) {
        this.etiUltModUsuario = etiUltModUsuario;
    }

    public Integer getEtiVersion() {
        return etiVersion;
    }

    public void setEtiVersion(Integer etiVersion) {
        this.etiVersion = etiVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (etiPk != null ? etiPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgEtiqueta)) {
            return false;
        }
        SgEtiqueta other = (SgEtiqueta) object;
        if ((this.etiPk == null && other.etiPk != null) || (this.etiPk != null && !this.etiPk.equals(other.etiPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SigesEtiqueta[ etiPk=" + etiPk + " ]";
    }

}
