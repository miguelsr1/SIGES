/**
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;

@Entity
@Table(name = "sg_contextos", schema = Constantes.SCHEMA_SEGURIDAD)
@XmlRootElement
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "conPk", scope = SgContexto.class)
@Audited
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
    
    @JoinColumn(name = "con_regla_fk")
    @ManyToOne
    private SgReglaContexto conRegla;
    
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

    public SgReglaContexto getConRegla() {
        return conRegla;
    }

    public void setConRegla(SgReglaContexto conRegla) {
        this.conRegla = conRegla;
    }


    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.conPk);
        return hash;
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
        final SgContexto other = (SgContexto) obj;
        if (!Objects.equals(this.conPk, other.conPk)) {
            return false;
        }
        return true;
    }


    

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgContexto[ conPk=" + conPk + " ]";
    }

}
