/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoNombre;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "ss_categoria_presupuesto_escolar", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cpeId", scope = SsTransferenciaComponente.class)
@Audited
/**
 * Entidad correspondiente a los componentes para las transferencias escolares
 * (anteriormente, categorìas del presupuesto escolar).
 */
public class SsCategoriaPresupuestoEscolar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cpe_id")
    private Long cpeId;

    @Column(name = "cpe_codigo")
    @AtributoCodigo
    @AtributoNormalizable
    private String cpeCodigo;

    @Column(name = "cpe_nombre")
    @AtributoNombre
    @AtributoNormalizable
    private String cpeNombre;

    @Column(name = "cpe_habilitado")
    private Boolean cpeHabilitado;

    //Auditoria
    @Column(name = "cpe_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date cpeUltMod;

    @Column(name = "cpe_ult_usuario")
    @AtributoUltimoUsuario
    private String cpeUltUsuario;

    @Column(name = "cpe_version")
    @Version
    private Integer cpeVersion;

    public Long getCpeId() {
        return cpeId;
    }

    public void setCpeId(Long cpeId) {
        this.cpeId = cpeId;
    }

    public String getCpeCodigo() {
        return cpeCodigo;
    }

    public void setCpeCodigo(String cpeCodigo) {
        this.cpeCodigo = cpeCodigo;
    }

    public String getCpeNombre() {
        return cpeNombre;
    }

    public void setCpeNombre(String cpeNombre) {
        this.cpeNombre = cpeNombre;
    }

    public Boolean getCpeHabilitado() {
        return cpeHabilitado;
    }

    public void setCpeHabilitado(Boolean cpeHabilitado) {
        this.cpeHabilitado = cpeHabilitado;
    }

    public Date getCpeUltMod() {
        return cpeUltMod;
    }

    public void setCpeUltMod(Date cpeUltMod) {
        this.cpeUltMod = cpeUltMod;
    }

    public String getCpeUltUsuario() {
        return cpeUltUsuario;
    }

    public void setCpeUltUsuario(String cpeUltUsuario) {
        this.cpeUltUsuario = cpeUltUsuario;
    }

    public Integer getCpeVersion() {
        return cpeVersion;
    }

    public void setCpeVersion(Integer cpeVersion) {
        this.cpeVersion = cpeVersion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.cpeId);
        return hash;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SsCategoriaPresupuestoEscolar other = (SsCategoriaPresupuestoEscolar) obj;
        if (this.getCpeId() != null && other.getCpeId() != null) {
            return Objects.equals(this.getCpeId(), other.getCpeId());
        }
        return this == other;
    }

    @Override
    public String toString() {
        return "CategoriaPresupuestoEscolar{" + "cpeId=" + cpeId + ", cpeCodigo=" + cpeCodigo + ", cpeNombre=" + cpeNombre + ", cpeHabilitado=" + cpeHabilitado + ", cpeUltMod=" + cpeUltMod + ", cpeUltUsuario=" + cpeUltUsuario + ", cpeVersion=" + cpeVersion + '}';
    }

}
