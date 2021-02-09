/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "ss_rubro", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners({EntidadListener.class})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "ruId", scope = SgRubros.class)
/**
 * Entidad correspondiente a los rubros de otros ingresos del presupuesto
 * escolar.
 */
public class SgRubros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ru_id", nullable = false)
    private Long ruId;

    @Size(max = 255)
    @Column(name = "ru_nombre", length = 255)
    private String ruNombre;

    @Size(max = 45)
    @Column(name = "ru_codigo", length = 45)
    private String ruCodigo;

    @Column(name = "ru_habilitado")
    private Boolean ruHabilitado;

    @AtributoUltimaModificacion
    @Column(name = "ru_ult_mod")
    private LocalDateTime ruUltmodFecha;

    @Size(max = 45)
    @AtributoUltimoUsuario
    @Column(name = "ru_ult_usuario", length = 45)
    private String ruUltmodUsuario;

    @Column(name = "ru_version ")
    @Version
    private Integer ruVersion;

    public Long getRuId() {
        return ruId;
    }

    public void setRuId(Long ruId) {
        this.ruId = ruId;
    }

    public String getRuNombre() {
        return ruNombre;
    }

    public void setRuNombre(String ruNombre) {
        this.ruNombre = ruNombre;
    }

    public String getRuCodigo() {
        return ruCodigo;
    }

    public void setRuCodigo(String ruCodigo) {
        this.ruCodigo = ruCodigo;
    }

    public Boolean getRuHabilitado() {
        return ruHabilitado;
    }

    public void setRuHabilitado(Boolean ruHabilitado) {
        this.ruHabilitado = ruHabilitado;
    }

    public LocalDateTime getRuUltmodFecha() {
        return ruUltmodFecha;
    }

    public void setRuUltmodFecha(LocalDateTime ruUltmodFecha) {
        this.ruUltmodFecha = ruUltmodFecha;
    }

    public String getRuUltmodUsuario() {
        return ruUltmodUsuario;
    }

    public void setRuUltmodUsuario(String ruUltmodUsuario) {
        this.ruUltmodUsuario = ruUltmodUsuario;
    }

    public Integer getRuVersion() {
        return ruVersion;
    }

    public void setRuVersion(Integer ruVersion) {
        this.ruVersion = ruVersion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ruId != null ? ruId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgRubros)) {
            return false;
        }
        SgRubros other = (SgRubros) object;
        if ((this.ruId == null && other.ruId != null) || (this.ruId != null && !this.ruId.equals(other.ruId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRubros[ ruId=" + ruId + " ]";
    }
}
