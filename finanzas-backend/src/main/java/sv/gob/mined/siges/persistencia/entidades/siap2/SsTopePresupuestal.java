/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades.siap2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.SgMovimientos;
import sv.gob.mined.siges.persistencia.entidades.SgSede;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;
import sv.gob.mined.siges.persistencia.utilidades.JsonIdentityResolver;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "ss_tope_presupestal", schema = Constantes.SCHEMA_SIAP2)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = JsonIdentityResolver.class, property = "tpId", scope = SsTopePresupuestal.class)
@Audited
/**
 * Entidad correspondiente a los techos presupuestales.
 */
public class SsTopePresupuestal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tp_id", nullable = false)
    private Integer tpId;

    @ManyToOne
    @JoinColumn(name = "tp_componente", referencedColumnName = "ges_id")
    private SsGesPresEs subComponente;

    @ManyToOne
    @JoinColumn(name = "tp_sub_cuenta", referencedColumnName = "cu_id")
    private SsCuenta subCuenta;

    @ManyToOne
    @JoinColumn(name = "tp_sede", referencedColumnName = "sed_pk")
    private SgSede sede;

    @Column(name = "tp_monto_formulacion")
    private BigDecimal monto;

    @Column(name = "tp_monto_aprobado")
    private BigDecimal montoAprobado;

    @ManyToOne
    @JoinColumn(name = "tp_movimiento", referencedColumnName = "mov_pk")
    private SgMovimientos tpMovimiento;

//    @ManyToOne
//    @JoinColumn(name = "tp_anio_fiscal")
//    private AnioFiscal anioFiscal;
//
//    @Column(name = "tp_estado")
//    @Enumerated(EnumType.ORDINAL)
//    private EstadoTopePresupuestal estado;
    //Auditoria
    @Column(name = "tp_ult_mod")
    @Temporal(TemporalType.TIMESTAMP)
    @AtributoUltimaModificacion
    private Date ultMod;

    @Column(name = "tp_ult_usuario")
    @AtributoUltimoUsuario
    private String ultUsuario;

    @Column(name = "tp_version")
    @Version
    private Integer version;

    public SsTopePresupuestal() {
    }

    @PrePersist
    @PreUpdate
    public void preSave() {

    }

    public Integer getTpId() {
        return tpId;
    }

    public SgMovimientos getTpMovimiento() {
        return tpMovimiento;
    }

    public void setTpMovimiento(SgMovimientos tpMovimiento) {
        this.tpMovimiento = tpMovimiento;
    }

    public SsGesPresEs getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(SsGesPresEs subComponente) {
        this.subComponente = subComponente;
    }

    public SsCuenta getSubCuenta() {
        return subCuenta;
    }

    public void setSubCuenta(SsCuenta subCuenta) {
        this.subCuenta = subCuenta;
    }

    public SgSede getSede() {
        return sede;
    }

    public void setSede(SgSede sede) {
        this.sede = sede;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }

    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }

    public Date getUltMod() {
        return ultMod;
    }

    public void setUltMod(Date ultMod) {
        this.ultMod = ultMod;
    }

    public String getUltUsuario() {
        return ultUsuario;
    }

    public void setUltUsuario(String ultUsuario) {
        this.ultUsuario = ultUsuario;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.tpId);
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
        final SsTopePresupuestal other = (SsTopePresupuestal) obj;
        if (!Objects.equals(this.tpId, other.tpId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SsTopePresupuestal{" + "tpId=" + tpId + ", subComponente=" + subComponente + ", subCuenta=" + subCuenta + ", sede=" + sede + ", monto=" + monto + ", montoAprobado=" + montoAprobado + ", tpMovimiento=" + tpMovimiento + ", ultMod=" + ultMod + ", ultUsuario=" + ultUsuario + ", version=" + version + '}';
    }

}
