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
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgTipoCuentaBancaria;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_rel_cbc_tcb", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "relPk", scope = SgRelCuentaTipoCuenta.class)
@Audited
/**
 * Entidad de relaciòn que vincula qué tipo de transferencias se podrán hacer a
 * qué cuenta bancaria.
 */
public class SgRelCuentaTipoCuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "rel_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relPk;

    @JoinColumn(name = "rel_tcb_pk", referencedColumnName = "tcb_pk", nullable = false)
    @ManyToOne
    private SgTipoCuentaBancaria cbcTipoCuentaBacFk;

    @JoinColumn(name = "rel_cbc_pk", referencedColumnName = "cbc_pk", nullable = false)
    @ManyToOne
    private SgCuentasBancarias relCuentaBancariaFk;

    @Column(name = "rel_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime relUltModFecha;

    @Size(max = 45)
    @Column(name = "rel_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String relUltModUsuario;

    @Column(name = "rel_version")
    @Version
    private Integer relVersion;

    public SgRelCuentaTipoCuenta() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {

    }
// <editor-fold defaultstate="collapsed" desc="Getter-Setters">

    public SgRelCuentaTipoCuenta(Long relPk) {
        this.relPk = relPk;
    }

    public SgRelCuentaTipoCuenta(Long relPk, Integer relVersion) {
        this.relPk = relPk;
        this.relVersion = relVersion;
    }

    public Long getRelPk() {
        return relPk;
    }

    public void setRelPk(Long relPk) {
        this.relPk = relPk;
    }

    public SgTipoCuentaBancaria getCbcTipoCuentaBacFk() {
        return cbcTipoCuentaBacFk;
    }

    public void setCbcTipoCuentaBacFk(SgTipoCuentaBancaria cbcTipoCuentaBacFk) {
        this.cbcTipoCuentaBacFk = cbcTipoCuentaBacFk;
    }
    

    public SgCuentasBancarias getRelCuentaBancariaFk() {
        return relCuentaBancariaFk;
    }

    public void setRelCuentaBancariaFk(SgCuentasBancarias relCuentaBancariaFk) {
        this.relCuentaBancariaFk = relCuentaBancariaFk;
    }

    public LocalDateTime getRelUltModFecha() {
        return relUltModFecha;
    }

    public void setRelUltModFecha(LocalDateTime relUltModFecha) {
        this.relUltModFecha = relUltModFecha;
    }

    public String getRelUltModUsuario() {
        return relUltModUsuario;
    }

    public void setRelUltModUsuario(String relUltModUsuario) {
        this.relUltModUsuario = relUltModUsuario;
    }

    public Integer getRelVersion() {
        return relVersion;
    }

    public void setRelVersion(Integer relVersion) {
        this.relVersion = relVersion;
    }
    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Hash-Equals">

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.relPk);
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
        final SgRelCuentaTipoCuenta other = (SgRelCuentaTipoCuenta) obj;
        if (!Objects.equals(this.relPk, other.relPk)) {
            return false;
        }
        return true;
    }
    // </editor-fold>
// <editor-fold defaultstate="collapsed" desc="toString">

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgRelCuentaTipoCuenta[ relPk=" + relPk + " ]";
    }
    // </editor-fold>

}
