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
public class FiltroMovimientoCajaChica  implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());

    private FilterConfig filterConfig = null;
    private Long first;
    private Long mccPk;
    private Long mccCuentaFK;
    private Long sedePk;
    private SsCategoriaPresupuestoEscolar componentePk;
    private SsGesPresEs subComponentePk;
    private TipoMovimiento mccTipoMovimiento;
    private LocalDateTime mccFecha;
    private LocalDateTime mccFechaDesde;
    private LocalDateTime mccFechaHasta;
    private LocalDate mccFechaDesdeS;
    private LocalDate mccFechaHastaS;
    private Integer mccFiltroFecha;
    
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroMovimientoCajaChica() {
        
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

    public Long getMccPk() {
        return mccPk;
    }

    public void setMccPk(Long mccPk) {
        this.mccPk = mccPk;
    }

    public Long getMccCuentaFK() {
        return mccCuentaFK;
    }

    public void setMccCuentaFK(Long mccCuentaFK) {
        this.mccCuentaFK = mccCuentaFK;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }
    
    public TipoMovimiento getMccTipoMovimiento() {
        return mccTipoMovimiento;
    }

    public void setMccTipoMovimiento(TipoMovimiento mccTipoMovimiento) {
        this.mccTipoMovimiento = mccTipoMovimiento;
    }

    public LocalDateTime getMccFecha() {
        return mccFecha;
    }

    public void setMccFecha(LocalDateTime mccFecha) {
        this.mccFecha = mccFecha;
    }

    public LocalDateTime getMccFechaDesde() {
        return mccFechaDesde;
    }

    public void setMccFechaDesde(LocalDateTime mccFechaDesde) {
        this.mccFechaDesde = mccFechaDesde;
    }

    public LocalDateTime getMccFechaHasta() {
        return mccFechaHasta;
    }

    public void setMccFechaHasta(LocalDateTime mccFechaHasta) {
        this.mccFechaHasta = mccFechaHasta;
    }

    public LocalDate getMccFechaDesdeS() {
        return mccFechaDesdeS;
    }

    public void setMccFechaDesdeS(LocalDate mccFechaDesdeS) {
        this.mccFechaDesdeS = mccFechaDesdeS;
    }

    public LocalDate getMccFechaHastaS() {
        return mccFechaHastaS;
    }

    public void setMccFechaHastaS(LocalDate mccFechaHastaS) {
        this.mccFechaHastaS = mccFechaHastaS;
    }

    public Integer getMccFiltroFecha() {
        return mccFiltroFecha;
    }

    public void setMccFiltroFecha(Integer mccFiltroFecha) {
        this.mccFiltroFecha = mccFiltroFecha;
    }

    public SsCategoriaPresupuestoEscolar getComponentePk() {
        return componentePk;
    }

    public void setComponentePk(SsCategoriaPresupuestoEscolar componentePk) {
        this.componentePk = componentePk;
    }

    public SsGesPresEs getSubComponentePk() {
        return subComponentePk;
    }

    public void setSubComponentePk(SsGesPresEs subComponentePk) {
        this.subComponentePk = subComponentePk;
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

}
