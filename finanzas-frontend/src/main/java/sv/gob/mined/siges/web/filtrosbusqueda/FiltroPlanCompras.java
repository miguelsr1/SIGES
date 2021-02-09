package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import sv.gob.mined.siges.web.dto.SgMovimientos;
import sv.gob.mined.siges.web.dto.siap2.SgInsumo;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroPlanCompras implements Serializable {

    private Long comPk;

    private SgMovimientos comMovimientosFk;

    private String comCodigo;

    private SgInsumo comInsumoFk;

    private BigDecimal comCantidad;

    private BigDecimal comUnidadMedida;

    private BigDecimal comPrecioUnitario;

    private BigDecimal comMontoTotal;

    private LocalDate comFechaEstimadaCompra;

    private Integer comMetodoFk;

    private Long comPresupuestoFk;

    private LocalDateTime comUltModFecha;

    private String comUltModUsuario;

    private Integer comVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroPlanCompras() {

    }
    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">

    public Long getComPresupuestoFk() {
        return comPresupuestoFk;
    }

    public void setComPresupuestoFk(Long comPresupuestoFk) {
        this.comPresupuestoFk = comPresupuestoFk;
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

    public BigDecimal getComUnidadMedida() {
        return comUnidadMedida;
    }

    public void setComUnidadMedida(BigDecimal comUnidadMedida) {
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

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(Long maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String[] orderBy) {
        this.orderBy = orderBy;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Integer getComMetodoFk() {
        return comMetodoFk;
    }

    public void setComMetodoFk(Integer comMetodoFk) {
        this.comMetodoFk = comMetodoFk;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.comPk);
        hash = 53 * hash + Objects.hashCode(this.comMovimientosFk);
        hash = 53 * hash + Objects.hashCode(this.comCodigo);
        hash = 53 * hash + Objects.hashCode(this.comInsumoFk);
        hash = 53 * hash + Objects.hashCode(this.comCantidad);
        hash = 53 * hash + Objects.hashCode(this.comUnidadMedida);
        hash = 53 * hash + Objects.hashCode(this.comPrecioUnitario);
        hash = 53 * hash + Objects.hashCode(this.comMontoTotal);
        hash = 53 * hash + Objects.hashCode(this.comFechaEstimadaCompra);
        hash = 53 * hash + Objects.hashCode(this.comMetodoFk);
        hash = 53 * hash + Objects.hashCode(this.comPresupuestoFk);
        hash = 53 * hash + Objects.hashCode(this.comUltModFecha);
        hash = 53 * hash + Objects.hashCode(this.comUltModUsuario);
        hash = 53 * hash + Objects.hashCode(this.comVersion);
        hash = 53 * hash + Objects.hashCode(this.first);
        hash = 53 * hash + Objects.hashCode(this.maxResults);
        hash = 53 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 53 * hash + Arrays.hashCode(this.ascending);
        hash = 53 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroPlanCompras other = (FiltroPlanCompras) obj;
        if (!Objects.equals(this.comCodigo, other.comCodigo)) {
            return false;
        }
        if (!Objects.equals(this.comUltModUsuario, other.comUltModUsuario)) {
            return false;
        }
        if (!Objects.equals(this.comPk, other.comPk)) {
            return false;
        }
        if (!Objects.equals(this.comMovimientosFk, other.comMovimientosFk)) {
            return false;
        }
        if (!Objects.equals(this.comInsumoFk, other.comInsumoFk)) {
            return false;
        }
        if (!Objects.equals(this.comCantidad, other.comCantidad)) {
            return false;
        }
        if (!Objects.equals(this.comUnidadMedida, other.comUnidadMedida)) {
            return false;
        }
        if (!Objects.equals(this.comPrecioUnitario, other.comPrecioUnitario)) {
            return false;
        }
        if (!Objects.equals(this.comMontoTotal, other.comMontoTotal)) {
            return false;
        }
        if (!Objects.equals(this.comFechaEstimadaCompra, other.comFechaEstimadaCompra)) {
            return false;
        }
        if (!Objects.equals(this.comMetodoFk, other.comMetodoFk)) {
            return false;
        }
        if (!Objects.equals(this.comPresupuestoFk, other.comPresupuestoFk)) {
            return false;
        }
        if (!Objects.equals(this.comUltModFecha, other.comUltModFecha)) {
            return false;
        }
        if (!Objects.equals(this.comVersion, other.comVersion)) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
}
