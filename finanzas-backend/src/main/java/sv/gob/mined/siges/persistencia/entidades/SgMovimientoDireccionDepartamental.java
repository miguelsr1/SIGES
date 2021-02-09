/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.persistencia.annotations.AtributoNormalizable;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_movimientos_direccion_departamental", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mddPk", scope = SgMovimientoDireccionDepartamental.class)
@Audited
/**
 * Entidad correspondiente a los movimientos de las cuentas bancarias de las
 * direcciones departamentales.
 */
public class SgMovimientoDireccionDepartamental implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mdd_pk", nullable = false)
    private Long mddPk;

    @JoinColumn(name = "mdd_cuenta_fk", referencedColumnName = "cbd_pk", nullable = false)
    @ManyToOne
    private SgCuentasBancariasDD mddCuentaFK;
    
    @JoinColumn(name = "mdd_det_desembolso_fk", referencedColumnName = "ded_pk", nullable = false)
    @OneToOne
    private SgDetalleDesembolso mddDetDesembolsoFk;
    
    @Size(max = 45)
    @Column(name = "mdd_transaccion", length = 45)
    private String mddTransaccion;

    @Column(name = "mdd_fecha", nullable = false)
    private LocalDateTime mddFecha;

    @Size(max = 255)
    @Column(name = "mdd_detalle", length = 255, nullable = false)
    @AtributoNormalizable
    private String mddDetalle;

    @Column(name = "mdd_monto", nullable = false)
    private BigDecimal mddMonto;

    @Column(name = "mdd_saldo", nullable = false)
    private BigDecimal mddSaldo;

    @Column(name = "mdd_tipo", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private TipoMovimiento mddTipoMovimiento;

    @Column(name = "mdd_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime mddUltModFecha;

    @Size(max = 45)
    @Column(name = "mdd_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String mddUltModUsuario;

    @Column(name = "mdd_version")
    @Version
    private Integer mddVersion;

    public SgMovimientoDireccionDepartamental() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {
        mddSaldo = (mddSaldo == null) ? BigDecimal.ZERO : mddSaldo;
    }

    public Long getMddPk() {
        return mddPk;
    }

    public void setMddPk(Long mddPk) {
        this.mddPk = mddPk;
    }

    public SgCuentasBancariasDD getMddCuentaFK() {
        return mddCuentaFK;
    }

    public void setMddCuentaFK(SgCuentasBancariasDD mddCuentaFK) {
        this.mddCuentaFK = mddCuentaFK;
    }

    public SgDetalleDesembolso getMddDetDesembolsoFk() {
        return mddDetDesembolsoFk;
    }

    public void setMddDetDesembolsoFk(SgDetalleDesembolso mddDetDesembolsoFk) {
        this.mddDetDesembolsoFk = mddDetDesembolsoFk;
    }

    public String getMddTransaccion() {
        return mddTransaccion;
    }

    public void setMddTransaccion(String mddTransaccion) {
        this.mddTransaccion = mddTransaccion;
    }
    
    
    public LocalDateTime getMddFecha() {
        return mddFecha;
    }

    public void setMddFecha(LocalDateTime mddFecha) {
        this.mddFecha = mddFecha;
    }

    public String getMddDetalle() {
        return mddDetalle;
    }

    public void setMddDetalle(String mddDetalle) {
        this.mddDetalle = mddDetalle;
    }

    public BigDecimal getMddMonto() {
        mddMonto = mddMonto == null ? BigDecimal.ZERO : mddMonto;
        return mddMonto;
    }

    public void setMddMonto(BigDecimal mddMonto) {
        this.mddMonto = mddMonto;
    }

    public BigDecimal getMddSaldo() {
        return mddSaldo;
    }

    public void setMddSaldo(BigDecimal mddSaldo) {
        this.mddSaldo = mddSaldo;
    }

    public TipoMovimiento getMddTipoMovimiento() {
        return mddTipoMovimiento;
    }

    public void setMddTipoMovimiento(TipoMovimiento mddTipoMovimiento) {
        this.mddTipoMovimiento = mddTipoMovimiento;
    }

    public LocalDateTime getMddUltModFecha() {
        return mddUltModFecha;
    }

    public void setMddUltModFecha(LocalDateTime mddUltModFecha) {
        this.mddUltModFecha = mddUltModFecha;
    }

    public String getMddUltModUsuario() {
        return mddUltModUsuario;
    }

    public void setMddUltModUsuario(String mddUltModUsuario) {
        this.mddUltModUsuario = mddUltModUsuario;
    }

    public Integer getMddVersion() {
        return mddVersion;
    }

    public void setMddVersion(Integer mddVersion) {
        this.mddVersion = mddVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.mddPk);
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
        final SgMovimientoDireccionDepartamental other = (SgMovimientoDireccionDepartamental) obj;
        if (!Objects.equals(this.mddPk, other.mddPk)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SgMovimientoCuentaBancaria{" + "mddPk=" + mddPk + ", mddUltModFecha=" + mddUltModFecha + ", mddUltModUsuario=" + mddUltModUsuario + ", mddVersion=" + mddVersion + '}';
    }

}
