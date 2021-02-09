/**
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumEstadoSolicitudesTransferencia;
import sv.gob.mined.siges.persistencia.annotations.AtributoHabilitado;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

@Entity
@Table(name = "sg_requerimientos_fondo", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "strPk", scope = SgRequerimientoFondo.class)
@Audited
/**
 * Entidad correspondiente a las solicitudes de transferencias de fondos.
 */
public class SgRequerimientoFondo implements Serializable,DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "str_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long strPk;

    @Column(name = "str_habilitado")
    @AtributoHabilitado
    private Boolean strHabilitado;

    @Column(name = "str_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime strUltModFecha;

    @Size(max = 45)
    @Column(name = "str_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String strUltModUsuario;

    @Column(name = "str_version")
    @Version
    private Integer strVersion;

    @Size(max = 45)
    @Column(name = "str_sac_goes", length = 45)
    private String strSacGOES;

    @Size(max = 45)
    @Column(name = "str_sac_ufi", length = 45)
    private String strSacUFI;

    @Size(max = 45)
    @Column(name = "str_compromiso_presupuestario", length = 45)
    private String strCompromisoPresupuestario;

    @Column(name = "str_estado", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumEstadoSolicitudesTransferencia strEstado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "str_transf_global_dep_fk", referencedColumnName = "tgd_pk")
    @NotAudited
    private SgTransferenciaGDep strTransferenciaGDepFk;

    @JoinColumn(name = "str_cuenta_banc_dd_fk", referencedColumnName = "cbd_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgCuentasBancariasDD strCuentaBancDdFk;

    @Column(name = "str_importe_total")
    private BigDecimal strImporteTotal;

    @OneToMany(mappedBy = "rfcSolTransferenciaFk")
    @Fetch(FetchMode.SUBSELECT)
    private List<SgReqFondoCed> reqsFondo;

//    @OneToOne(mappedBy = "dedReqFondoFk")
//    private SgDetalleDesembolso detalleDesembolso;
//
    public SgRequerimientoFondo() {
    }

    public SgRequerimientoFondo(Long strPk) {
        this.strPk = strPk;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {

    }

    public Long getStrPk() {
        return strPk;
    }

    public void setStrPk(Long strPk) {
        this.strPk = strPk;
    }

    public Boolean getStrHabilitado() {
        return strHabilitado;
    }

    public void setStrHabilitado(Boolean strHabilitado) {
        this.strHabilitado = strHabilitado;
    }

    public LocalDateTime getStrUltModFecha() {
        return strUltModFecha;
    }

    public void setStrUltModFecha(LocalDateTime strUltModFecha) {
        this.strUltModFecha = strUltModFecha;
    }

    public String getStrUltModUsuario() {
        return strUltModUsuario;
    }

    public void setStrUltModUsuario(String strUltModUsuario) {
        this.strUltModUsuario = strUltModUsuario;
    }

    public Integer getStrVersion() {
        return strVersion;
    }

    public void setStrVersion(Integer strVersion) {
        this.strVersion = strVersion;
    }

    public String getStrSacGOES() {
        return strSacGOES;
    }

    public void setStrSacGOES(String strSacGOES) {
        this.strSacGOES = strSacGOES;
    }

    public String getStrSacUFI() {
        return strSacUFI;
    }

    public void setStrSacUFI(String strSacUFI) {
        this.strSacUFI = strSacUFI;
    }

    public String getStrCompromisoPresupuestario() {
        return strCompromisoPresupuestario;
    }

    public void setStrCompromisoPresupuestario(String strCompromisoPresupuestario) {
        this.strCompromisoPresupuestario = strCompromisoPresupuestario;
    }

    public EnumEstadoSolicitudesTransferencia getStrEstado() {
        return strEstado;
    }

    public void setStrEstado(EnumEstadoSolicitudesTransferencia strEstado) {
        this.strEstado = strEstado;
    }

    public SgTransferenciaGDep getStrTransferenciaGDepFk() {
        return strTransferenciaGDepFk;
    }

    public void setStrTransferenciaGDepFk(SgTransferenciaGDep strTransferenciaGDepFk) {
        this.strTransferenciaGDepFk = strTransferenciaGDepFk;
    }

    public BigDecimal getStrImporteTotal() {
        return strImporteTotal;
    }

    public void setStrImporteTotal(BigDecimal strImporteTotal) {
        this.strImporteTotal = strImporteTotal;
    }

    public SgCuentasBancariasDD getStrCuentaBancDdFk() {
        return strCuentaBancDdFk;
    }

    public void setStrCuentaBancDdFk(SgCuentasBancariasDD strCuentaBancDdFk) {
        this.strCuentaBancDdFk = strCuentaBancDdFk;
    }

    public List<SgReqFondoCed> getReqsFondo() {
        return reqsFondo;
    }

    public void setReqsFondo(List<SgReqFondoCed> reqsFondo) {
        this.reqsFondo = reqsFondo;
    }

//    public SgDetalleDesembolso getDetalleDesembolso() {
//        return detalleDesembolso;
//    }
//
//    public void setDetalleDesembolso(SgDetalleDesembolso detalleDesembolso) {
//        this.detalleDesembolso = detalleDesembolso;
//    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.strPk);
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
        final SgRequerimientoFondo other = (SgRequerimientoFondo) obj;
        if (!Objects.equals(this.strPk, other.strPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sv.gob.mined.siges.persistencia.entidades.SgSolicitudesTransferencia[ bncPk=" + strPk + " ]";
    }

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "strTransferenciaGDepFk.tgdDepartamentoFk.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return null;
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return null;
        }
    }

}
