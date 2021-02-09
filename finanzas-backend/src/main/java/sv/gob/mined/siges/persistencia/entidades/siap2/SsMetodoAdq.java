/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "ss_metodo_adq", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "metId", scope = SsMetodoAdq.class)
@Audited
/**
 * Entidad correspondiente a los métodos de adquisición de las compras.
 */
public class SsMetodoAdq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "met_id", nullable = false)
    private Long metId;

    @Size(max = 45)
    @Column(name = "met_nombre", length = 45)
    @AtributoNombre
    private String metNombre;

    @Column(name = "met_habilitado")
    private Boolean metHabilitado;

    @Column(name = "met_ult_mod")
    @AtributoUltimaModificacion
    private LocalDateTime metUltMod;

    @Size(max = 45)
    @Column(name = "met_ult_usuario", length = 45)
    @AtributoUltimoUsuario
    private String metUltUsuario;

    @Column(name = "met_version")
    @Version
    private Integer meVersion;

    public SsMetodoAdq() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {

    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getMetId() {
        return metId;
    }

    public void setMetId(Long metId) {
        this.metId = metId;
    }

    public String getMetNombre() {
        return metNombre;
    }

    public void setMetNombre(String metNombre) {
        this.metNombre = metNombre;
    }

    public Boolean getMetHabilitado() {
        return metHabilitado;
    }

    public void setMetHabilitado(Boolean metHabilitado) {
        this.metHabilitado = metHabilitado;
    }

    public LocalDateTime getMetUltMod() {
        return metUltMod;
    }

    public void setMetUltMod(LocalDateTime metUltMod) {
        this.metUltMod = metUltMod;
    }

    public String getMetUltUsuario() {
        return metUltUsuario;
    }

    public void setMetUltUsuario(String metUltUsuario) {
        this.metUltUsuario = metUltUsuario;
    }

    public Integer getMeVersion() {
        return meVersion;
    }

    public void setMeVersion(Integer meVersion) {
        this.meVersion = meVersion;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Hahs-Equals">
    @Override
    public int hashCode() {
        return Objects.hashCode(this.metId);
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
        final SsMetodoAdq other = (SsMetodoAdq) obj;
        if (!Objects.equals(this.metId, other.metId)) {
            return false;
        }
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "SsMetodoAdq{" + "metId=" + metId + ", metNombre=" + metNombre + ", metHabilitado=" + metHabilitado + ", metUltMod=" + metUltMod + ", metUltUsuario=" + metUltUsuario + ", meVersion=" + meVersion + '}';
    }
    // </editor-fold>

}
