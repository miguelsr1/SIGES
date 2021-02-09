/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sofis.security.DaoOperation;
import com.sofis.security.DataSecurity;
import com.sofis.security.OperationSecurity;
import com.sofis.sofisform.to.CriteriaTO;
import com.sofis.sofisform.to.MatchCriteriaTO;
import com.sofis.utils.CriteriaTOUtils;
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
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_cuenta_bancaria_dd", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cbdPk", scope = SgCuentasBancarias.class)
@Audited
/**
 * Entidad correspondiente a las cuentas bancarias de las DDE
 */
public class SgCuentasBancariasDD implements Serializable,DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cbd_pk", nullable = false)
    private Long cbdPk;

    @Size(max = 50)
    @Column(name = "cbd_numero_cuenta", length = 50)
    private String cbdNumeroCuenta;

    @Size(max = 250)
    @Column(name = "cbd_titular", length = 250)
    private String cbdTitular;

    @JoinColumn(name = "cbd_banco_fk", referencedColumnName = "bnc_pk")
    @ManyToOne
    private SgBancos cbdBancoFk;

    @Column(name = "cbd_habilitado")
    @AtributoHabilitado
    private Boolean cbdHabilitado;

    @Size(max = 4000)
    @Column(name = "cbd_comentarios", length = 4000)
    @AtributoNormalizable
    private String cbdComentario;

    @JoinColumn(name = "cbd_dde_fk", referencedColumnName = "ded_pk")
    @ManyToOne
    private SgDireccionDepartamental cbdDirDepFk;

    @Column(name = "cbd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cbdUltModFecha;

    @Size(max = 45)
    @Column(name = "cbd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cbdUltModUsuario;

    @Column(name = "cbd_version")
    @Version
    private Integer cbdVersion;

    public SgCuentasBancariasDD() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
    }

    public Long getCbdPk() {
        return cbdPk;
    }

    public void setCbdPk(Long cbdPk) {
        this.cbdPk = cbdPk;
    }

    public String getCbdNumeroCuenta() {
        return cbdNumeroCuenta;
    }

    public void setCbdNumeroCuenta(String cbdNumeroCuenta) {
        this.cbdNumeroCuenta = cbdNumeroCuenta;
    }

    public String getCbdTitular() {
        return cbdTitular;
    }

    public void setCbdTitular(String cbdTitular) {
        this.cbdTitular = cbdTitular;
    }

    public SgBancos getCbdBancoFk() {
        return cbdBancoFk;
    }

    public void setCbdBancoFk(SgBancos cbdBancoFk) {
        this.cbdBancoFk = cbdBancoFk;
    }

    public SgDireccionDepartamental getCbdDirDepFk() {
        return cbdDirDepFk;
    }

    public void setCbdDirDepFk(SgDireccionDepartamental cbdDirDepFk) {
        this.cbdDirDepFk = cbdDirDepFk;
    }

    public Boolean getCbdHabilitado() {
        return cbdHabilitado;
    }

    public void setCbdHabilitado(Boolean cbdHabilitado) {
        this.cbdHabilitado = cbdHabilitado;
    }

    public String getCbdComentario() {
        return cbdComentario;
    }

    public void setCbdComentario(String cbdComentario) {
        this.cbdComentario = cbdComentario;
    }

    public LocalDateTime getCbdUltModFecha() {
        return cbdUltModFecha;
    }

    public void setCbdUltModFecha(LocalDateTime cbdUltModFecha) {
        this.cbdUltModFecha = cbdUltModFecha;
    }

    public String getCbdUltModUsuario() {
        return cbdUltModUsuario;
    }

    public void setCbdUltModUsuario(String cbdUltModUsuario) {
        this.cbdUltModUsuario = cbdUltModUsuario;
    }

    public Integer getCbdVersion() {
        return cbdVersion;
    }

    public void setCbdVersion(Integer cbdVersion) {
        this.cbdVersion = cbdVersion;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.cbdPk);
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
        final SgCuentasBancariasDD other = (SgCuentasBancariasDD) obj;
        if (!Objects.equals(this.cbdPk, other.cbdPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCuentasBancariasDD{" + "cbdPk=" + cbdPk + ", cbdNumeroCuenta=" + cbdNumeroCuenta + ", cbdTitular=" + cbdTitular + ", cbdHabilitado=" + cbdHabilitado + ", cbdComentario=" + cbdComentario + ", cbdUltModFecha=" + cbdUltModFecha + ", cbdUltModUsuario=" + cbdUltModUsuario + ", cbdVersion=" + cbdVersion + '}';
    }

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "cbdDirDepFk.dedDepartamentoFk.depPk", o.getContext());
        } else {
            return null;
        }
    }

    

}
