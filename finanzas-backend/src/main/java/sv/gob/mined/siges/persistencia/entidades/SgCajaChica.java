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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
import sv.gob.mined.siges.persistencia.entidades.siap2.SsGesPresEs;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 * Entidad correspondiente a las cajas chicas
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_cajas_chicas", uniqueConstraints = {
    @UniqueConstraint(name = "bcc_numero_cuenta_uk", columnNames = {"bcc_numero_cuenta"})
}, schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bccPk", scope = SgCuentasBancarias.class)
@Audited
public class SgCajaChica implements Serializable,DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "bcc_pk", nullable = false)
    private Long bccPk;

    @Size(max = 50)
    @Column(name = "bcc_numero_cuenta", length = 50)
    private String bccNumeroCuenta;

    @Size(max = 250)
    @Column(name = "bcc_titular", length = 250)
    private String bccTitular;

    @Column(name = "bcc_habilitado")
    @AtributoHabilitado
    private Boolean bccHabilitado;

    @Size(max = 4000)
    @Column(name = "bcc_comentarios", length = 4000)
    @AtributoNormalizable
    private String bccComentario;

    @JoinColumn(name = "bcc_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne
    private SgSede bccSedeFk;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bcc_subcomponente_fk", referencedColumnName = "ges_id")
    private SsGesPresEs bccSubcomponenteFk;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bcc_anio_fk", referencedColumnName = "ale_pk")
    private SgAnioLectivo bccAnioFk;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bcc_cuenta_banc_fk", referencedColumnName = "cbc_pk")
    private SgCuentasBancarias bccCuentaBancFk;
    
    @Column(name = "bcc_otro_ingreso")
    private Boolean bccOtroIngreso;

    @Column(name = "bcc_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime bccUltModFecha;

    @Size(max = 45)
    @Column(name = "bcc_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String bccUltModUsuario;

    @Column(name = "bcc_version")
    @Version
    private Integer bccVersion;

    public SgCajaChica() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
    }

    public Long getBccPk() {
        return bccPk;
    }

    public void setBccPk(Long bccPk) {
        this.bccPk = bccPk;
    }

    public String getBccNumeroCuenta() {
        return bccNumeroCuenta;
    }

    public void setBccNumeroCuenta(String bccNumeroCuenta) {
        this.bccNumeroCuenta = bccNumeroCuenta;
    }

    public String getBccTitular() {
        return bccTitular;
    }

    public void setBccTitular(String bccTitular) {
        this.bccTitular = bccTitular;
    }

    public String getBccComentario() {
        return bccComentario;
    }

    public void setBccComentario(String bccComentario) {
        this.bccComentario = bccComentario;
    }

    public SgSede getBccSedeFk() {
        return bccSedeFk;
    }

    public void setBccSedeFk(SgSede bccSedeFk) {
        this.bccSedeFk = bccSedeFk;
    }

    public SgAnioLectivo getBccAnioFk() {
        return bccAnioFk;
    }

    public void setBccAnioFk(SgAnioLectivo bccAnioFk) {
        this.bccAnioFk = bccAnioFk;
    }
    
    public SsGesPresEs getBccSubcomponenteFk() {
        return bccSubcomponenteFk;
    }

    public void setBccSubcomponenteFk(SsGesPresEs bccSubcomponenteFk) {
        this.bccSubcomponenteFk = bccSubcomponenteFk;
    }

    public Boolean getBccOtroIngreso() {
        return bccOtroIngreso;
    }

    public void setBccOtroIngreso(Boolean bccOtroIngreso) {
        this.bccOtroIngreso = bccOtroIngreso;
    }
    
        
    public Boolean getBccHabilitado() {
        return bccHabilitado;
    }

    public void setBccHabilitado(Boolean bccHabilitado) {
        this.bccHabilitado = bccHabilitado;
    }

    public LocalDateTime getBccUltModFecha() {
        return bccUltModFecha;
    }

    public void setBccUltModFecha(LocalDateTime bccUltModFecha) {
        this.bccUltModFecha = bccUltModFecha;
    }

    public String getBccUltModUsuario() {
        return bccUltModUsuario;
    }

    public void setBccUltModUsuario(String bccUltModUsuario) {
        this.bccUltModUsuario = bccUltModUsuario;
    }

    public SgCuentasBancarias getBccCuentaBancFk() {
        return bccCuentaBancFk;
    }

    public void setBccCuentaBancFk(SgCuentasBancarias bccCuentaBancFk) {
        this.bccCuentaBancFk = bccCuentaBancFk;
    }
    
    public Integer getBccVersion() {
        return bccVersion;
    }

    public void setBccVersion(Integer bccVersion) {
        this.bccVersion = bccVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.bccPk);
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
        final SgCajaChica other = (SgCajaChica) obj;
        if (!Objects.equals(this.bccPk, other.bccPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgCuentasBancariasCC{" + "bccPk=" + bccPk + ", bccHabilitado=" + bccHabilitado + ", bccUltModFecha=" + bccUltModFecha + ", bccUltModUsuario=" + bccUltModUsuario + ", bccVersion=" + bccVersion + '}';
    }

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
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "bccSedeFk.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return null;
        }
    }

}
