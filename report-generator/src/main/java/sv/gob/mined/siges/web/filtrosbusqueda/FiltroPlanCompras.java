package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import sv.gob.mined.siges.web.dto.SgMovimientos;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroPlanCompras implements Serializable {

    private Long comPk;

    private SgMovimientos comMovimientosFk;

    private String comCodigo;

    //private SgInsumo comInsumoFk;

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
}
