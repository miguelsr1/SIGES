/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.seguridad;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_contextos", schema = Constantes.SCHEMA_SEGURIDAD)
@XmlRootElement
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "conPk", scope = SgContexto.class)
@Audited
/**
 * Entidad correspondiente a un contexto de seguridad.
 */
public class SgContexto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "con_pk")
    private Long conPk;

    @Column(name = "con_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime conUltModFecha;

    @Size(max = 45)
    @Column(name = "con_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String conUltModUsuario;

    @Column(name = "con_version")
    @Version
    private Integer conVersion;

    @Column(name = "con_ambito", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumAmbito conAmbito;

    @Column(name = "con_contexto_id")
    private Long contextoId;

    public SgContexto() {
    }

    public SgContexto(Long conPk) {
        this.conPk = conPk;
    }

    public Long getConPk() {
        return conPk;
    }

    public void setConPk(Long conPk) {
        this.conPk = conPk;
    }

    public LocalDateTime getConUltModFecha() {
        return conUltModFecha;
    }

    public void setConUltModFecha(LocalDateTime conUltModFecha) {
        this.conUltModFecha = conUltModFecha;
    }

    public String getConUltModUsuario() {
        return conUltModUsuario;
    }

    public void setConUltModUsuario(String conUltModUsuario) {
        this.conUltModUsuario = conUltModUsuario;
    }

    public Integer getConVersion() {
        return conVersion;
    }

    public void setConVersion(Integer conVersion) {
        this.conVersion = conVersion;
    }

    public EnumAmbito getConAmbito() {
        return conAmbito;
    }

    public void setConAmbito(EnumAmbito conAmbito) {
        this.conAmbito = conAmbito;
    }

    public Long getContextoId() {
        return contextoId;
    }

    public void setContextoId(Long contextoId) {
        this.contextoId = contextoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (conPk != null ? conPk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SgContexto)) {
            return false;
        }
        SgContexto other = (SgContexto) object;
        if ((this.conPk == null && other.conPk != null) || (this.conPk != null && !this.conPk.equals(other.conPk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgContexto[ conPk=" + conPk + " ]";
    }

}
