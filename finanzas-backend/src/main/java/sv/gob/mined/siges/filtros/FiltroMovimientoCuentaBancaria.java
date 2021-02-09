/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.SessionBean;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.enumerados.EnumMovimientosOrigen;
import sv.gob.mined.siges.enumerados.EnumTipoRetiroMovimientoCB;
import sv.gob.mined.siges.enumerados.TipoMovimiento;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsGesPresEs;

/**
 * Filtro de los movimientos de las cuentas bancarias
 *
 * @author Sofis Solutions
 */
public class FiltroMovimientoCuentaBancaria implements Serializable {

    private FilterConfig filterConfig = null;
    private Long first;
    private Long mcbPk;
    private Long sedePk;
    private Long sedePresupuesto;
    private Long mcbCuentaFK;
    private Long subComponente;
    private Long componenteIngresoPk;
    private Integer anioFiscal;
    private Integer anioFiscalId;
    private Boolean mcbChequeAnulado;
    private SsCategoriaPresupuestoEscolar componentePk;
    private SsGesPresEs subComponentePk;
    private TipoMovimiento mcbTipoMovimiento;
    private LocalDateTime mcbFecha;
    private LocalDateTime mcbFechaDesde;
    private LocalDateTime mcbFechaHasta;
    private Boolean mcbChequeCobrado;
    private Boolean mcbAplicaConciliacion;
    private List<Long> movPks;
    private List<Long> sedesIds;
    private EnumMovimientosOrigen movimientoOrigen;
    private EnumTipoRetiroMovimientoCB mcbTipoRetiro;

    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    @Inject
    private SessionBean sessionBean;

    public FiltroMovimientoCuentaBancaria() {

        super();
        this.first = 0L;
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getMcbPk() {
        return mcbPk;
    }

    public void setMcbPk(Long mcbPk) {
        this.mcbPk = mcbPk;
    }

    public Long getMcbCuentaFK() {
        return mcbCuentaFK;
    }

    public void setMcbCuentaFK(Long mcbCuentaFK) {
        this.mcbCuentaFK = mcbCuentaFK;
    }

    public TipoMovimiento getMcbTipoMovimiento() {
        return mcbTipoMovimiento;
    }

    public void setMcbTipoMovimiento(TipoMovimiento mcbTipoMovimiento) {
        this.mcbTipoMovimiento = mcbTipoMovimiento;
    }

    public LocalDateTime getMcbFecha() {
        return mcbFecha;
    }

    public void setMcbFecha(LocalDateTime mcbFecha) {
        this.mcbFecha = mcbFecha;
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

    public LocalDateTime getMcbFechaDesde() {
        return mcbFechaDesde;
    }

    public void setMcbFechaDesde(LocalDateTime mcbFechaDesde) {
        this.mcbFechaDesde = mcbFechaDesde;
    }

    public LocalDateTime getMcbFechaHasta() {
        return mcbFechaHasta;
    }

    public void setMcbFechaHasta(LocalDateTime mcbFechaHasta) {
        this.mcbFechaHasta = mcbFechaHasta;
    }

    public SsCategoriaPresupuestoEscolar getComponentePk() {
        return componentePk;
    }

    public void setComponentePk(SsCategoriaPresupuestoEscolar componentePk) {
        this.componentePk = componentePk;
    }

    public Boolean getMcbChequeCobrado() {
        return mcbChequeCobrado;
    }

    public void setMcbChequeCobrado(Boolean mcbChequeCobrado) {
        this.mcbChequeCobrado = mcbChequeCobrado;
    }

    public SsGesPresEs getSubComponentePk() {
        return subComponentePk;
    }

    public void setSubComponentePk(SsGesPresEs subComponentePk) {
        this.subComponentePk = subComponentePk;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Long getComponenteIngresoPk() {
        return componenteIngresoPk;
    }

    public void setComponenteIngresoPk(Long componenteIngresoPk) {
        this.componenteIngresoPk = componenteIngresoPk;
    }

    public List<Long> getMovPks() {
        return movPks;
    }

    public void setMovPks(List<Long> movPks) {
        this.movPks = movPks;
    }

    public Long getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(Long subComponente) {
        this.subComponente = subComponente;
    }

    public Integer getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(Integer anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public Long getSedePresupuesto() {
        return sedePresupuesto;
    }

    public void setSedePresupuesto(Long sedePresupuesto) {
        this.sedePresupuesto = sedePresupuesto;
    }

    public Boolean getMcbAplicaConciliacion() {
        return mcbAplicaConciliacion;
    }

    public void setMcbAplicaConciliacion(Boolean mcbAplicaConciliacion) {
        this.mcbAplicaConciliacion = mcbAplicaConciliacion;
    }

    public Integer getAnioFiscalId() {
        return anioFiscalId;
    }

    public void setAnioFiscalId(Integer anioFiscalId) {
        this.anioFiscalId = anioFiscalId;
    }

    public Boolean getMcbChequeAnulado() {
        return mcbChequeAnulado;
    }

    public void setMcbChequeAnulado(Boolean mcbChequeAnulado) {
        this.mcbChequeAnulado = mcbChequeAnulado;
    }

    public List<Long> getSedesIds() {
        return sedesIds;
    }

    public void setSedesIds(List<Long> sedesIds) {
        this.sedesIds = sedesIds;
    }

    public EnumMovimientosOrigen getMovimientoOrigen() {
        return movimientoOrigen;
    }

    public void setMovimientoOrigen(EnumMovimientosOrigen movimientoOrigen) {
        this.movimientoOrigen = movimientoOrigen;
    }

    public EnumTipoRetiroMovimientoCB getMcbTipoRetiro() {
        return mcbTipoRetiro;
    }

    public void setMcbTipoRetiro(EnumTipoRetiroMovimientoCB mcbTipoRetiro) {
        this.mcbTipoRetiro = mcbTipoRetiro;
    }

}
