package sv.gob.mined.siges.persistencia.entidades;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.seguridad.SgUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 * Entidad correspondiente a la autorización de edición de presupuestos
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_autorizacion_edicion_presupuestos", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@Audited
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "autPk", scope = SgAutorizacionEdicionPresupuesto.class)
public class SgAutorizacionEdicionPresupuesto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aut_pk")
    private Long autPk;

    @JoinColumn(name = "aut_presupuesto_fk", referencedColumnName = "pes_pk")
    @ManyToOne
    private SgPresupuestoEscolar autPresupuestoFk;

    @JoinColumn(name = "aut_usuario_validado_fk", referencedColumnName = "usu_pk")
    @ManyToOne
    private SgUsuario autUsuarioValidadoFk;

    //Auditoria
    @Column(name = "aut_ult_mod_fecha ")
    @AtributoUltimaModificacion
    private LocalDateTime autUltMod;

    @Column(name = "aut_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String autUltUsuario;

    @Column(name = "aut_version")
    @Version
    private Integer autVersion;

    public Long getAutPk() {
        return autPk;
    }

    public void setAutPk(Long autPk) {
        this.autPk = autPk;
    }

    public SgPresupuestoEscolar getAutPresupuestoFk() {
        return autPresupuestoFk;
    }

    public void setAutPresupuestoFk(SgPresupuestoEscolar autPresupuestoFk) {
        this.autPresupuestoFk = autPresupuestoFk;
    }

    public SgUsuario getAutUsuarioValidadoFk() {
        return autUsuarioValidadoFk;
    }

    public void setAutUsuarioValidadoFk(SgUsuario autUsuarioValidadoFk) {
        this.autUsuarioValidadoFk = autUsuarioValidadoFk;
    }

    public LocalDateTime getAutUltMod() {
        return autUltMod;
    }

    public void setAutUltMod(LocalDateTime autUltMod) {
        this.autUltMod = autUltMod;
    }

    public String getAutUltUsuario() {
        return autUltUsuario;
    }

    public void setAutUltUsuario(String autUltUsuario) {
        this.autUltUsuario = autUltUsuario;
    }

    public Integer getAutVersion() {
        return autVersion;
    }

    public void setAutVersion(Integer autVersion) {
        this.autVersion = autVersion;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.autPk);
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
        final SgAutorizacionEdicionPresupuesto other = (SgAutorizacionEdicionPresupuesto) obj;
        if (!Objects.equals(this.autPk, other.autPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgAtorizacionEdicionPresupuesto{" + "autPk=" + autPk + '}';
    }
}
