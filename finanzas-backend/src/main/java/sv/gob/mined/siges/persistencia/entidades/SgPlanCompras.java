/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.persistencia.entidades;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.envers.Audited;
import sv.gob.mined.siges.constantes.Constantes;
import sv.gob.mined.siges.persistencia.annotations.AtributoCodigo;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimaModificacion;
import sv.gob.mined.siges.persistencia.annotations.AtributoUltimoUsuario;
import sv.gob.mined.siges.persistencia.entidades.siap2.SgInsumo;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsMetodoAdq;
import sv.gob.mined.siges.persistencia.utilidades.EntidadListener;

/**
 *
 * @author Sofis Solutions
 */
@Entity
@Table(name = "sg_plan_compra", uniqueConstraints = {
    @UniqueConstraint(name = "com_codigo_uk", columnNames = {"com_codigo"})
}, schema = Constantes.SCHEMA_FINANZAS)
@XmlRootElement
@EntityListeners(EntidadListener.class)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "comPk", scope = SgPlanCompras.class)
@Audited
/**
 * Entidad correspondiente al plan de compras de los centros educativos.
 */
public class SgPlanCompras implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "com_pk", nullable = false)
    private Long comPk;

    @JoinColumn(name = "com_movimiento_fk", referencedColumnName = "mov_pk")
    @ManyToOne
    private SgMovimientos comMovimientosFk;

    @Size(max = 45)
    @AtributoCodigo
    @Column(name = "com_codigo", length = 45)
    private String comCodigo;

    @JoinColumn(name = "com_insumo_fk", referencedColumnName = "ins_id")
    @ManyToOne
    private SgInsumo comInsumoFk;

    @Column(name = "com_cantidad", nullable = true)
    private BigDecimal comCantidad;

    @Column(name = "com_unidad_medida", nullable = true)
    private String comUnidadMedida;

    @Column(name = "com_precio_unitario", nullable = true)
    private BigDecimal comPrecioUnitario;

    @Column(name = "com_monto_total", nullable = true)
    private BigDecimal comMontoTotal;

    @JoinColumn(name = "com_metodo_fk", referencedColumnName = "met_id")
    @ManyToOne
    private SsMetodoAdq comMetodoFk;

    @JoinColumn(name = "com_presupuesto_fk", referencedColumnName = "pes_pk")
    @ManyToOne
    private SgPresupuestoEscolar comPresupuestoFk;

    @Column(name = "com_fecha_estimada_compra")
    private LocalDate comFechaEstimadaCompra;

    @Column(name = "com_ult_mod_fecha")
    @AtributoUltimaModificacion
    private LocalDateTime comUltModFecha;

    @Size(max = 45)
    @Column(name = "com_ult_mod_usuario", length = 45)
    @AtributoUltimoUsuario
    private String comUltModUsuario;

    @Column(name = "com_version")
    @Version
    private Integer comVersion;

    public SgPlanCompras() {
    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public SgPresupuestoEscolar getComPresupuestoFk() {
        return comPresupuestoFk;
    }

    public void setComPresupuestoFk(SgPresupuestoEscolar comPresupuestoFk) {
        this.comPresupuestoFk = comPresupuestoFk;
    }

    public SsMetodoAdq getComMetodoFk() {
        return comMetodoFk;
    }

    public void setComMetodoFk(SsMetodoAdq comMetodoFk) {
        this.comMetodoFk = comMetodoFk;
    }

    public Long getComPk() {
        return comPk;
    }

    public void setComPk(Long comPk) {
        this.comPk = comPk;
    }

    public SgMovimientos getComMovimientosFk() {
        return comMovimientosFk;
    }

    public void setComMovimientosFk(SgMovimientos comMovimientosFk) {
        this.comMovimientosFk = comMovimientosFk;
    }

    public String getComCodigo() {
        return comCodigo;
    }

    public void setComCodigo(String comCodigo) {
        this.comCodigo = comCodigo;
    }

    public SgInsumo getComInsumoFk() {
        return comInsumoFk;
    }

    public void setComInsumoFk(SgInsumo comInsumoFk) {
        this.comInsumoFk = comInsumoFk;
    }

    public BigDecimal getComCantidad() {
        return comCantidad;
    }

    public void setComCantidad(BigDecimal comCantidad) {
        this.comCantidad = comCantidad;
    }

    public String getComUnidadMedida() {
        return comUnidadMedida;
    }

    public void setComUnidadMedida(String comUnidadMedida) {
        this.comUnidadMedida = comUnidadMedida;
    }

    public BigDecimal getComPrecioUnitario() {
        return comPrecioUnitario;
    }

    public void setComPrecioUnitario(BigDecimal comPrecioUnitario) {
        this.comPrecioUnitario = comPrecioUnitario;
    }

    public BigDecimal getComMontoTotal() {
        return comMontoTotal;
    }

    public void setComMontoTotal(BigDecimal comMontoTotal) {
        this.comMontoTotal = comMontoTotal;
    }

    public LocalDate getComFechaEstimadaCompra() {
        return comFechaEstimadaCompra;
    }

    public void setComFechaEstimadaCompra(LocalDate comFechaEstimadaCompra) {
        this.comFechaEstimadaCompra = comFechaEstimadaCompra;
    }

    public LocalDateTime getComUltModFecha() {
        return comUltModFecha;
    }

    public void setComUltModFecha(LocalDateTime comUltModFecha) {
        this.comUltModFecha = comUltModFecha;
    }

    public String getComUltModUsuario() {
        return comUltModUsuario;
    }

    public void setComUltModUsuario(String comUltModUsuario) {
        this.comUltModUsuario = comUltModUsuario;
    }

    public Integer getComVersion() {
        return comVersion;
    }

    public void setComVersion(Integer comVersion) {
        this.comVersion = comVersion;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        return Objects.hashCode(this.comPk);
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
        final SgPlanCompras other = (SgPlanCompras) obj;
        if (!Objects.equals(this.comPk, other.comPk)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="toString">
    @Override
    public String toString() {
        return "SgPlanCompras{" + "comPk=" + comPk + ", comUltModFecha=" + comUltModFecha + ", comUltModUsuario=" + comUltModUsuario + ", comVersion=" + comVersion + '}';
    }
    // </editor-fold>

    @PrePersist
    @PreUpdate
    public void calcularTotales() {
        if (comPrecioUnitario != null && comCantidad != null) {
            comMontoTotal = comPrecioUnitario.multiply(comCantidad);
        }
    }

    public String getIdInsumo() {
        if (comInsumoFk != null) {
            return comInsumoFk.getInsNombre();
        }
        return null;
    }
}
