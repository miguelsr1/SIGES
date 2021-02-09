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
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 * Entidad correspondiente al compromiso presupuestario del requerimiento de fondo
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_compromiso_presupuestario_por_req_fondo", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "cprPk",
        scope = SgCompromisoPresupuestario.class)
public class SgCompromisoPresupuestario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cpr_pk")
    private Long cprPk;

    @JoinColumn(name = "cpr_requerimiento_fondo_fk", referencedColumnName = "str_pk")
    @ManyToOne
    private SgRequerimientoFondo cprRequerimientoFondoFk;

    @Column(name = "cpr_numero_presupuestario")
    private String cprNumeroPresupuestario;

    //Auditoria
    @Column(name = "cpr_ult_mod_fecha ")
    @AtributoUltimaModificacion
    private LocalDateTime cprUltMod;

    @Column(name = "cpr_ult_mod_usuario")
    @AtributoUltimoUsuario
    private String cprUltUsuario;

    @Column(name = "cpr_version")
    @Version
    private Integer cprVersion;

    @Column(name = "cpr_fuente_recursos_fk")
    @Version
    private Long cprFuenteRecursosFk;

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getCprPk() {
        return cprPk;
    }

    public void setCprPk(Long cprPk) {
        this.cprPk = cprPk;
    }

    public Long getCprFuenteRecursosFk() {
        return cprFuenteRecursosFk;
    }

    public void setCprFuenteRecursosFk(Long cprFuenteRecursosFk) {
        this.cprFuenteRecursosFk = cprFuenteRecursosFk;
    }

    public SgRequerimientoFondo getCprRequerimientoFondoFk() {
        return cprRequerimientoFondoFk;
    }

    public void setCprRequerimientoFondoFk(SgRequerimientoFondo cprRequerimientoFondoFk) {
        this.cprRequerimientoFondoFk = cprRequerimientoFondoFk;
    }

    public String getCprNumeroPresupuestario() {
        return cprNumeroPresupuestario;
    }

    public void setCprNumeroPresupuestario(String cprNumeroPresupuestario) {
        this.cprNumeroPresupuestario = cprNumeroPresupuestario;
    }

    public LocalDateTime getCprUltMod() {
        return cprUltMod;
    }

    public void setCprUltMod(LocalDateTime cprUltMod) {
        this.cprUltMod = cprUltMod;
    }

    public String getCprUltUsuario() {
        return cprUltUsuario;
    }

    public void setCprUltUsuario(String cprUltUsuario) {
        this.cprUltUsuario = cprUltUsuario;
    }

    public Integer getCprVersion() {
        return cprVersion;
    }

    public void setCprVersion(Integer cprVersion) {
        this.cprVersion = cprVersion;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.cprPk);
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
        final SgCompromisoPresupuestario other = (SgCompromisoPresupuestario) obj;
        if (!Objects.equals(this.cprPk, other.cprPk)) {
            return false;
        }
        return true;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "SgCompromisoPresupuestario{" + "cprPk=" + cprPk + '}';
    }

    // </editor-fold>
}
