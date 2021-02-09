/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.web.dto.siap2.SsCategoriaPresupuestoEscolar;
import sv.gob.mined.siges.web.dto.siap2.SsGesPresEs;
import sv.gob.mined.siges.web.enumerados.TipoMovimiento;

/**
 *
 * @author Sofis Solutions
 */
//@WebFilter()
public class FiltroMovimientoCuentaBancaria implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;
    private Long first;
    private Long mcbPk;
    private Long mcbCuentaFK;
    private Long sedePk;
    private Long sedePresupuesto;
    private Long componenteIngresoPk;
    private Long subComponente;
    private Integer anioFiscal;

    private Integer anioFiscalId;
    private SsCategoriaPresupuestoEscolar componentePk;
    private SsGesPresEs subComponentePk;
    private TipoMovimiento mcbTipoMovimiento;
    private LocalDateTime mcbFecha;
    private LocalDateTime mcbFechaDesde;
    private LocalDateTime mcbFechaHasta;
    private LocalDate mcbFechaDesdeS;
    private LocalDate mcbFechaHastaS;
    private Integer mcbFiltroFecha;
    private Boolean mcbChequeCobrado;
    private Boolean mcbAplicaConciliacion;

    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

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

    public Integer getMcbFiltroFecha() {
        return mcbFiltroFecha;
    }

    public void setMcbFiltroFecha(Integer mcbFiltroFecha) {
        this.mcbFiltroFecha = mcbFiltroFecha;
    }

    public LocalDate getMcbFechaDesdeS() {
        return mcbFechaDesdeS;
    }

    public void setMcbFechaDesdeS(LocalDate mcbFechaDesdeS) {
        this.mcbFechaDesdeS = mcbFechaDesdeS;
    }

    public LocalDate getMcbFechaHastaS() {
        return mcbFechaHastaS;
    }

    public void setMcbFechaHastaS(LocalDate mcbFechaHastaS) {
        this.mcbFechaHastaS = mcbFechaHastaS;
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

    public Long getSubComponente() {
        return subComponente;
    }

    public void setSubComponente(Long subComponente) {
        this.subComponente = subComponente;
    }

    public Long getSedePresupuesto() {
        return sedePresupuesto;
    }

    public void setSedePresupuesto(Long sedePresupuesto) {
        this.sedePresupuesto = sedePresupuesto;
    }

    public Integer getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(Integer anioFiscal) {
        this.anioFiscal = anioFiscal;
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

}
