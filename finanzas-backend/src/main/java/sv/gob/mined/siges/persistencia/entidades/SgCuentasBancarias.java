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
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
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
@Table(name = "sg_cuentas_bancarias", uniqueConstraints = {
    @UniqueConstraint(name = "cbc_numero_cuenta_uk", columnNames = {"cbc_numero_cuenta"})
}, schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "cbcPk", scope = SgCuentasBancarias.class)
@Audited
/**
 * Entidad correspondiente a las cuentas bancarias (genérico)
 */
public class SgCuentasBancarias implements Serializable,DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cbc_pk", nullable = false)
    private Long cbcPk;

    @Size(max = 50)
    @Column(name = "cbc_numero_cuenta", length = 50)
    private String cbcNumeroCuenta;

    @Size(max = 250)
    @Column(name = "cbc_titular", length = 250)
    private String cbcTitular;

    @JoinColumn(name = "cbc_banco_fk", referencedColumnName = "bnc_pk")
    @ManyToOne
    private SgBancos cbcBancoFk;

//    @NotAudited
    @OneToMany(mappedBy = "relCuentaBancariaFk",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<SgRelCuentaTipoCuenta> cbcCuentaTipoCuenta;

    @Column(name = "cbc_habilitado")
    @AtributoHabilitado
    private Boolean cbcHabilitado;
    
    
    @Column(name = "cbc_otro_ingreso")
    private Boolean cbcOtroIngreso;

    @Size(max = 4000)
    @Column(name = "cbc_comentarios", length = 4000)
    @AtributoNormalizable
    private String cbcComentario;

    @JoinColumn(name = "cbc_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede cbcSedeFk;

    @Column(name = "cbc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime cbcUltModFecha;

    @Size(max = 45)
    @Column(name = "cbc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String cbcUltModUsuario;

    @Column(name = "cbc_version")
    @Version
    private Integer cbcVersion;

    public SgCuentasBancarias() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
    }
    // <editor-fold defaultstate="collapsed" desc="Getter-Setters">

    public Long getCbcPk() {
        return cbcPk;
    }

    public void setCbcPk(Long cbcPk) {
        this.cbcPk = cbcPk;
    }

    public String getCbcNumeroCuenta() {
        return cbcNumeroCuenta;
    }

    public void setCbcNumeroCuenta(String cbcNumeroCuenta) {
        this.cbcNumeroCuenta = cbcNumeroCuenta;
    }

    public String getCbcTitular() {
        return cbcTitular;
    }

    public void setCbcTitular(String cbcTitular) {
        this.cbcTitular = cbcTitular;
    }

    public SgBancos getCbcBancoFk() {
        return cbcBancoFk;
    }

    public void setCbcBancoFk(SgBancos cbcBancoFk) {
        this.cbcBancoFk = cbcBancoFk;
    }

    public String getCbcComentario() {
        return cbcComentario;
    }

    public void setCbcComentario(String cbcComentario) {
        this.cbcComentario = cbcComentario;
    }

    public SgSede getCbcSedeFk() {
        return cbcSedeFk;
    }

    public void setCbcSedeFk(SgSede cbcSedeFk) {
        this.cbcSedeFk = cbcSedeFk;
    }

    public List<SgRelCuentaTipoCuenta> getCbcCuentaTipoCuenta() {
        return cbcCuentaTipoCuenta;
    }

    public void setCbcCuentaTipoCuenta(List<SgRelCuentaTipoCuenta> cbcCuentaTipoCuenta) {
        this.cbcCuentaTipoCuenta = cbcCuentaTipoCuenta;
    }

    public Boolean getCbcHabilitado() {
        return cbcHabilitado;
    }

    public void setCbcHabilitado(Boolean cbcHabilitado) {
        this.cbcHabilitado = cbcHabilitado;
    }

    public LocalDateTime getCbcUltModFecha() {
        return cbcUltModFecha;
    }

    public void setCbcUltModFecha(LocalDateTime cbcUltModFecha) {
        this.cbcUltModFecha = cbcUltModFecha;
    }

    public String getCbcUltModUsuario() {
        return cbcUltModUsuario;
    }

    public void setCbcUltModUsuario(String cbcUltModUsuario) {
        this.cbcUltModUsuario = cbcUltModUsuario;
    }

    public Integer getCbcVersion() {
        return cbcVersion;
    }

    public void setCbcVersion(Integer cbcVersion) {
        this.cbcVersion = cbcVersion;
    }

    public Boolean getCbcOtroIngreso() {
        return cbcOtroIngreso;
    }

    public void setCbcOtroIngreso(Boolean cbcOtroIngreso) {
        this.cbcOtroIngreso = cbcOtroIngreso;
    }
    
    
    
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">

    @Override
    public int hashCode() {
        return Objects.hashCode(this.cbcPk);
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
        final SgCuentasBancarias other = (SgCuentasBancarias) obj;
        if (!Objects.equals(this.cbcPk, other.cbcPk)) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">

    @Override
    public String toString() {
        return "SgCuentasBancarias{" + "cbcPk=" + cbcPk + ", cbcHabilitado=" + cbcHabilitado + ", cbcUltModFecha=" + cbcUltModFecha + ", cbcUltModUsuario=" + cbcUltModUsuario + ", cbcVersion=" + cbcVersion + '}';
    }
    // </editor-fold>

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
         if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            //return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.EQUALS, "estMatriculas.matSeccion.secServicioEducativo.sduSede.sedDireccion.dirDepartamento.depPk", o.getContext());
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "cbcSedeFk.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return null;
        }
    }

}
