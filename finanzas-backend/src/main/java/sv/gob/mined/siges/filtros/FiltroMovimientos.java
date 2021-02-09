/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.enumerados.EnumMovimientosTipo;

/**
 * Filtro de los movimientos del presupuesto escolar
 *
 * @author jgiron
 */
public class FiltroMovimientos implements Serializable {

    private FilterConfig filterConfig = null;
    private Long first;
    private Long movPk;
    private String movCodigo;
    private Long movPresupuestoFk;
    private String movFuenteRecursos;
    private EnumMovimientosTipo movTipo;
    private Double movMonto;
    private BigDecimal movMontoAprobado;
    private LocalDate movUltmodFecha;
    private String movUltmodUsuario;
    private Integer movVersion;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private Integer movNumMoviento;
    private EnumMovimientosOrigen movOrigen;
    private Long movActividadPk;
    private Long movFuenteIngresos;
    private String[] incluirCampos;
    private Long movSubAreaInversionPk;
    private Long movAreaInversionPk;
    private Integer movTechoPresupuestal;
    private Long componentePk;
    private Long sedePk;
    private Boolean movActividadAplicaPlanCompra;
    private Boolean movEditado;

    private Integer adiVersion;

    public FiltroMovimientos() {
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

    public Integer getAdiVersion() {
        return adiVersion;
    }

    public void setAdiVersion(Integer adiVersion) {
        this.adiVersion = adiVersion;
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public Long getMovPk() {
        return movPk;
    }

    public void setMovPk(Long movPk) {
        this.movPk = movPk;
    }

    public String getMovCodigo() {
        return movCodigo;
    }

    public void setMovCodigo(String movCodigo) {
        this.movCodigo = movCodigo;
    }

    public Long getMovPresupuestoFk() {
        return movPresupuestoFk;
    }

    public void setMovPresupuestoFk(Long movPresupuestoFk) {
        this.movPresupuestoFk = movPresupuestoFk;
    }

    public String getMovFuenteRecursos() {
        return movFuenteRecursos;
    }

    public void setMovFuenteRecursos(String movFuenteRecursos) {
        this.movFuenteRecursos = movFuenteRecursos;
    }

    public EnumMovimientosTipo getMovTipo() {
        return movTipo;
    }

    public void setMovTipo(EnumMovimientosTipo movTipo) {
        this.movTipo = movTipo;
    }

    public Double getMovMonto() {
        return movMonto;
    }

    public void setMovMonto(Double movMonto) {
        this.movMonto = movMonto;
    }

    public LocalDate getMovUltmodFecha() {
        return movUltmodFecha;
    }

    public void setMovUltmodFecha(LocalDate movUltmodFecha) {
        this.movUltmodFecha = movUltmodFecha;
    }

    public String getMovUltmodUsuario() {
        return movUltmodUsuario;
    }

    public void setMovUltmodUsuario(String movUltmodUsuario) {
        this.movUltmodUsuario = movUltmodUsuario;
    }

    public Integer getMovVersion() {
        return movVersion;
    }

    public void setMovVersion(Integer movVersion) {
        this.movVersion = movVersion;
    }

    public Integer getMovNumMoviento() {
        return movNumMoviento;
    }

    public void setMovNumMoviento(Integer movNumMoviento) {
        this.movNumMoviento = movNumMoviento;
    }

    public EnumMovimientosOrigen getMovOrigen() {
        return movOrigen;
    }

    public void setMovOrigen(EnumMovimientosOrigen movOrigen) {
        this.movOrigen = movOrigen;
    }

    public Long getMovActividadPk() {
        return movActividadPk;
    }

    public void setMovActividadPk(Long movActividadPk) {
        this.movActividadPk = movActividadPk;
    }

    public Long getMovFuenteIngresos() {
        return movFuenteIngresos;
    }

    public void setMovFuenteIngresos(Long movFuenteIngresos) {
        this.movFuenteIngresos = movFuenteIngresos;
    }

    public Long getMovSubAreaInversionPk() {
        return movSubAreaInversionPk;
    }

    public void setMovSubAreaInversionPk(Long movSubAreaInversionPk) {
        this.movSubAreaInversionPk = movSubAreaInversionPk;
    }

    public BigDecimal getMovMontoAprobado() {
        return movMontoAprobado;
    }

    public void setMovMontoAprobado(BigDecimal movMontoAprobado) {
        this.movMontoAprobado = movMontoAprobado;
    }

    public Integer getMovTechoPresupuestal() {
        return movTechoPresupuestal;
    }

    public void setMovTechoPresupuestal(Integer movTechoPresupuestal) {
        this.movTechoPresupuestal = movTechoPresupuestal;
    }

    public Long getMovAreaInversionPk() {
        return movAreaInversionPk;
    }

    public void setMovAreaInversionPk(Long movAreaInversionPk) {
        this.movAreaInversionPk = movAreaInversionPk;
    }

    public Long getComponentePk() {
        return componentePk;
    }

    public void setComponentePk(Long componentePk) {
        this.componentePk = componentePk;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Boolean getMovActividadAplicaPlanCompra() {
        return movActividadAplicaPlanCompra;
    }

    public void setMovActividadAplicaPlanCompra(Boolean movActividadAplicaPlanCompra) {
        this.movActividadAplicaPlanCompra = movActividadAplicaPlanCompra;
    }

    public Boolean getMovEditado() {
        return movEditado;
    }

    public void setMovEditado(Boolean movEditado) {
        this.movEditado = movEditado;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.filterConfig);
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
        final FiltroMovimientos other = (FiltroMovimientos) obj;
        return true;
    }

}
