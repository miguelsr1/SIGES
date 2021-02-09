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
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumModoPago;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 * Entidad correspondiente de pago.
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_pagos", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "pgsPk", scope = SgPago.class)
@Audited
public class SgPago implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pgs_pk", nullable = false)
    private Long pgsPk;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pgs_factura_fk", referencedColumnName = "fac_pk")
    @LazyToOne(value = LazyToOneOption.NO_PROXY)
    private SgFactura pgsFactura;

    @Column(name = "pgs_modo_pago", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumModoPago pgsModoPago;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pgs_movimiento_cb_fk", referencedColumnName = "mcb_pk")
    private SgMovimientoCuentaBancaria pgsMovimientoCBFk;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pgs_movimiento_cc_fk", referencedColumnName = "mcc_pk")
    private SgMovimientoCajaChica pgsMovimientoCCFk;

    @JoinColumn(name = "pgs_chequera_fk", referencedColumnName = "che_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    private SgChequera pgsChequeraFk;

    @Column(name = "pgs_numero_cheque")
    private Integer pgsNumeroCheque;

    @Column(name = "pgs_fecha_pago")
    private LocalDateTime pgsFechaPago;

    @Column(name = "pgs_importe")
    private BigDecimal pgsImporte;

    @Size(max = 255)
    @Column(name = "pgs_comentario", length = 255)
    @AtributoNormalizable
    private String pgsComentario;

    @Column(name = "pgs_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime pgsUltModFecha;

    @Size(max = 45)
    @Column(name = "pgs_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String pgsUltModUsuario;

    @Column(name = "pgs_version")
    @Version
    private Integer pgsVersion;

    @Transient
    private SgCuentasBancarias pgsCuentaBancaria;

    @Transient
    private SgCajaChica pgsCuentaBancariaCC;

    public SgPago() {
    }

    public SgPago(SgPago pago) {
        this.pgsPk = pago.pgsPk;
        this.pgsFactura = pago.pgsFactura;
        this.pgsComentario = pago.pgsComentario;
        this.pgsCuentaBancaria = pago.pgsCuentaBancaria;
        this.pgsCuentaBancariaCC = pago.pgsCuentaBancariaCC;
        this.pgsFechaPago = pago.pgsFechaPago;
        this.pgsImporte = pago.pgsImporte;
        this.pgsModoPago = pago.pgsModoPago;
        this.pgsNumeroCheque = pago.pgsNumeroCheque;
        this.pgsUltModFecha = pago.pgsUltModFecha;
        this.pgsUltModUsuario = pago.pgsUltModUsuario;
    }

    @PrePersist
    @PreUpdate
    public void preSave() {

    }
    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">

    public Long getPgsPk() {
        return pgsPk;
    }

    public void setPgsPk(Long pgsPk) {
        this.pgsPk = pgsPk;
    }

    public SgFactura getPgsFactura() {
        return pgsFactura;
    }

    public void setPgsFactura(SgFactura pgsFactura) {
        this.pgsFactura = pgsFactura;
    }

    public EnumModoPago getPgsModoPago() {
        return pgsModoPago;
    }

    public void setPgsModoPago(EnumModoPago pgsModoPago) {
        this.pgsModoPago = pgsModoPago;
    }

    public SgMovimientoCuentaBancaria getPgsMovimientoCBFk() {
        return pgsMovimientoCBFk;
    }

    public void setPgsMovimientoCBFk(SgMovimientoCuentaBancaria pgsMovimientoCBFk) {
        this.pgsMovimientoCBFk = pgsMovimientoCBFk;
    }

    public SgMovimientoCajaChica getPgsMovimientoCCFk() {
        return pgsMovimientoCCFk;
    }

    public void setPgsMovimientoCCFk(SgMovimientoCajaChica pgsMovimientoCCFk) {
        this.pgsMovimientoCCFk = pgsMovimientoCCFk;
    }

    public Integer getPgsNumeroCheque() {
        return pgsNumeroCheque;
    }

    public void setPgsNumeroCheque(Integer pgsNumeroCheque) {
        this.pgsNumeroCheque = pgsNumeroCheque;
    }

    public LocalDateTime getPgsFechaPago() {
        return pgsFechaPago;
    }

    public void setPgsFechaPago(LocalDateTime pgsFechaPago) {
        this.pgsFechaPago = pgsFechaPago;
    }

    public BigDecimal getPgsImporte() {
        return pgsImporte;
    }

    public void setPgsImporte(BigDecimal pgsImporte) {
        this.pgsImporte = pgsImporte;
    }

    public String getPgsComentario() {
        return pgsComentario;
    }

    public void setPgsComentario(String pgsComentario) {
        this.pgsComentario = pgsComentario;
    }

    public LocalDateTime getPgsUltModFecha() {
        return pgsUltModFecha;
    }

    public void setPgsUltModFecha(LocalDateTime pgsUltModFecha) {
        this.pgsUltModFecha = pgsUltModFecha;
    }

    public String getPgsUltModUsuario() {
        return pgsUltModUsuario;
    }

    public void setPgsUltModUsuario(String pgsUltModUsuario) {
        this.pgsUltModUsuario = pgsUltModUsuario;
    }

    public Integer getPgsVersion() {
        return pgsVersion;
    }

    public void setPgsVersion(Integer pgsVersion) {
        this.pgsVersion = pgsVersion;
    }

    public SgCuentasBancarias getPgsCuentaBancaria() {
        return pgsCuentaBancaria;
    }

    public void setPgsCuentaBancaria(SgCuentasBancarias pgsCuentaBancaria) {
        this.pgsCuentaBancaria = pgsCuentaBancaria;
    }

    public SgChequera getPgsChequeraFk() {
        return pgsChequeraFk;
    }

    public void setPgsChequeraFk(SgChequera pgsChequeraFk) {
        this.pgsChequeraFk = pgsChequeraFk;
    }

    public SgCajaChica getPgsCuentaBancariaCC() {
        return pgsCuentaBancariaCC;
    }

    public void setPgsCuentaBancariaCC(SgCajaChica pgsCuentaBancariaCC) {
        this.pgsCuentaBancariaCC = pgsCuentaBancariaCC;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">

    @Override
    public int hashCode() {
        return Objects.hashCode(this.pgsPk);
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
        final SgPago other = (SgPago) obj;
        if (!Objects.equals(this.pgsPk, other.pgsPk)) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toSTring">

    @Override
    public String toString() {
        return "SgPago{" + "pgsPk=" + pgsPk + ", pgsCodigo=" + ", pgsUltModFecha=" + pgsUltModFecha + ", pgsUltModUsuario=" + pgsUltModUsuario + ", pgsVersion=" + pgsVersion + '}';
    }
    // </editor-fold>

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "pgsFactura.facSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "pgsFactura.facSedeFk.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return null;
        }
    }

}
