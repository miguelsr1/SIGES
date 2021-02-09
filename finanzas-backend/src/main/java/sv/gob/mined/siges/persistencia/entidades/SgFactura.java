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
import java.time.LocalDate;
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
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.enumerados.EnumAmbito;
import sv.gob.mined.siges.enumerados.EnumFacturaEstado;
import sv.gob.mined.siges.enumerados.EnumTipoDocumentoPago;
import sv.gob.mined.siges.enumerados.EnumTipoItemFactura;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsProveedor;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 * Entidad correspondiente a las facturas
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_facturas", schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "facPk", scope = SgFactura.class)
@Audited
public class SgFactura implements Serializable, DataSecurity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "fac_pk", nullable = false)
    private Long facPk;

    @Size(max = 45)
    @Column(name = "fac_numero", length = 45)
    private String facNumero;

    @JoinColumn(name = "fac_sede_fk", referencedColumnName = "sed_pk")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgSede facSedeFk;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @JoinColumn(name = "fac_proveedor_fk", referencedColumnName = "pro_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SsProveedor facProveedorFk;

    @Column(name = "fac_fecha_factura")
    private LocalDate facFechaFactura;

    @Column(name = "fac_sub_total", nullable = true)
    private BigDecimal facSubTotal;

    @Column(name = "fac_deducciones", nullable = true)
    private BigDecimal facDeducciones;

    @Column(name = "fac_tipo_item", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumTipoItemFactura facTipoItem;

    @JoinColumn(name = "fac_item_plan_compra", referencedColumnName = "com_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgPlanCompras facItemPlanCompra;

    @JoinColumn(name = "fac_item_movimiento", referencedColumnName = "mov_pk")
    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgMovimientos facItemMovimiento;

    @OneToOne(mappedBy = "pgsFactura", fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private SgPago pago;

    @Column(name = "fac_estado", nullable = false, insertable = true, updatable = true)
    @Enumerated(EnumType.STRING)
    private EnumFacturaEstado facEstado;

    @Column(name = "fac_tipo_documento")
    @Enumerated(EnumType.STRING)
    private EnumTipoDocumentoPago facTipoDocumento;

    @Column(name = "fac_total", nullable = true)
    private BigDecimal facTotal;

    @Size(max = 45)
    @Column(name = "fac_nota_credito", length = 45)
    private String facNotaCredito;

    @Column(name = "fac_fecha_nota_credito")
    private LocalDate facFechaNotaCredito;

    @Column(name = "fac_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime facUltModFecha;

    @Size(max = 45)
    @Column(name = "fac_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String facUltModUsuario;

    @Column(name = "fac_version")
    @Version
    private Integer facVersion;

    @PrePersist
    @PreUpdate
    public void calcularTotales() {
        if (facSubTotal != null && facDeducciones != null) {
            facTotal = facSubTotal.add(facDeducciones);
        }
    }

    public SgFactura() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getFacPk() {
        return facPk;
    }

    public void setFacPk(Long facPk) {
        this.facPk = facPk;
    }

    public String getFacNumero() {
        return facNumero;
    }

    public void setFacNumero(String facNumero) {
        this.facNumero = facNumero;
    }

    public LocalDate getFacFechaFactura() {
        return facFechaFactura;
    }

    public void setFacFechaFactura(LocalDate facFechaFactura) {
        this.facFechaFactura = facFechaFactura;
    }

    public BigDecimal getFacSubTotal() {
        return facSubTotal;
    }

    public void setFacSubTotal(BigDecimal facSubTotal) {
        this.facSubTotal = facSubTotal;
    }

    public BigDecimal getFacDeducciones() {
        return facDeducciones;
    }

    public void setFacDeducciones(BigDecimal facDeducciones) {
        this.facDeducciones = facDeducciones;
    }

    public EnumTipoItemFactura getFacTipoItem() {
        return facTipoItem;
    }

    public void setFacTipoItem(EnumTipoItemFactura facTipoItem) {
        this.facTipoItem = facTipoItem;
    }

    public SgPlanCompras getFacItemPlanCompra() {
        return facItemPlanCompra;
    }

    public void setFacItemPlanCompra(SgPlanCompras facItemPlanCompra) {
        this.facItemPlanCompra = facItemPlanCompra;
    }

    public SgMovimientos getFacItemMovimiento() {
        return facItemMovimiento;
    }

    public void setFacItemMovimiento(SgMovimientos facItemMovimiento) {
        this.facItemMovimiento = facItemMovimiento;
    }

    public EnumFacturaEstado getFacEstado() {
        return facEstado;
    }

    public void setFacEstado(EnumFacturaEstado facEstado) {
        this.facEstado = facEstado;
    }

    public BigDecimal getFacTotal() {
        return facTotal;
    }

    public void setFacTotal(BigDecimal facTotal) {
        this.facTotal = facTotal;
    }

    public LocalDateTime getFacUltModFecha() {
        return facUltModFecha;
    }

    public void setFacUltModFecha(LocalDateTime facUltModFecha) {
        this.facUltModFecha = facUltModFecha;
    }

    public String getFacUltModUsuario() {
        return facUltModUsuario;
    }

    public void setFacUltModUsuario(String facUltModUsuario) {
        this.facUltModUsuario = facUltModUsuario;
    }

    public SgSede getFacSedeFk() {
        return facSedeFk;
    }

    public void setFacSedeFk(SgSede facSedeFk) {
        this.facSedeFk = facSedeFk;
    }

    public Integer getFacVersion() {
        return facVersion;
    }

    public void setFacVersion(Integer facVersion) {
        this.facVersion = facVersion;
    }

    public EnumTipoDocumentoPago getFacTipoDocumento() {
        return facTipoDocumento;
    }

    public void setFacTipoDocumento(EnumTipoDocumentoPago facTipoDocumento) {
        this.facTipoDocumento = facTipoDocumento;
    }

    public String getFacNotaCredito() {
        return facNotaCredito;
    }

    public void setFacNotaCredito(String facNotaCredito) {
        this.facNotaCredito = facNotaCredito;
    }

    public LocalDate getFacFechaNotaCredito() {
        return facFechaNotaCredito;
    }

    public void setFacFechaNotaCredito(LocalDate facFechaNotaCredito) {
        this.facFechaNotaCredito = facFechaNotaCredito;
    }

    public SsProveedor getFacProveedorFk() {
        return facProveedorFk;
    }

    public void setFacProveedorFk(SsProveedor facProveedorFk) {
        this.facProveedorFk = facProveedorFk;
    }

    public SgPago getPago() {
        return pago;
    }

    public void setPago(SgPago pago) {
        this.pago = pago;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.facPk);
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
        final SgFactura other = (SgFactura) obj;
        if (!Objects.equals(this.facPk, other.facPk)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "com.sofis.entidades.SgFactura[ facPk=" + facPk + " ]";
    }
    // </editor-fold>

    @Override
    public String securityAmbitCreate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CriteriaTO securityAmbitNavigation(OperationSecurity o, DaoOperation d) {
        if (o.getAmbit().equalsIgnoreCase(EnumAmbito.DEPARTAMENTAL.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "facSedeFk.sedDireccion.dirDepartamento.depPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.SEDE.name())) {
            return CriteriaTOUtils.createMatchCriteriaTO(MatchCriteriaTO.types.IN, "facSedeFk.sedPk", o.getContext());
        } else if (o.getAmbit().equalsIgnoreCase(EnumAmbito.MINED.name())) {
            return null;
        } else {
            return null;
        }
    }
}
