/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Sofis Solutions
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "comPk", scope = SgPlanCompras.class)
public class SgPlanCompras implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long comPk;

    private SgMovimientos comMovimientosFk;

    private String comCodigo;

    //private SgInsumo comInsumoFk;

    private BigDecimal comCantidad;

    private String comUnidadMedida;

    private BigDecimal comPrecioUnitario;

    private BigDecimal comMontoTotal;

    //private SsMetodoAdq comMetodoFk;

    private LocalDate comFechaEstimadaCompra;

    private SgPresupuestoEscolar comPresupuestoFk;

    private LocalDateTime comUltModFecha;

    private String comUltModUsuario;

    private Integer comVersion;

    //private List<SsMetodoAdq> metodosNombre;

    private Long comInsumoId;

    public SgPlanCompras() {

    }

    

    public SgPresupuestoEscolar getComPresupuestoFk() {
        return comPresupuestoFk;
    }

    public void setComPresupuestoFk(SgPresupuestoEscolar comPresupuestoFk) {
        this.comPresupuestoFk = comPresupuestoFk;
    }

//    public SsMetodoAdq getComMetodoFk() {
//        return comMetodoFk;
//    }
//
//    public void setComMetodoFk(SsMetodoAdq comMetodoFk) {
//        this.comMetodoFk = comMetodoFk;
//    }

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

//    public SgInsumo getComInsumoFk() {
//        return comInsumoFk;
//    }
//
//    public void setComInsumoFk(SgInsumo comInsumoFk) {
//        this.comInsumoFk = comInsumoFk;
//    }
//
//    public String getIdInsumo() {
//        if (comInsumoFk != null) {
//            return comInsumoFk.getInsNombre();
//        }
//        return null;
//    }

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

    public Long getComInsumoId() {
        return comInsumoId;
    }

    public void setComInsumoId(Long comInsumoId) {
        this.comInsumoId = comInsumoId;
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
        int hash = 0;
        hash += (comPk != null ? comPk.hashCode() : 0);
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
        return "com.sofis.entidades.SgPlanCompras[ comPk=" + comPk + " ]";
    }
    // </editor-fold>

}
