/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.servlet.FilterConfig;

/**
 *
 * @author Sofis Solutions
 */
//@WebFilter()
public class FiltroMovimientoDireccionDepartamental  implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());

    private FilterConfig filterConfig = null;
    private Long first;
    private Long mddPk;
    private Long mddCuentaFK;
    private String mddTipoMovimiento;
    private LocalDateTime mddFecha;
    private LocalDateTime mddFechaDesde;
    private LocalDateTime mddFechaHasta;
    private LocalDate mddFechaDesdeS;
    private LocalDate mddFechaHastaS;
    private Integer mddFiltroFecha;
    
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroMovimientoDireccionDepartamental() {
        
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

    public Long getMddPk() {
        return mddPk;
    }

    public void setMddPk(Long mddPk) {
        this.mddPk = mddPk;
    }

    public Long getMddCuentaFK() {
        return mddCuentaFK;
    }

    public void setMddCuentaFK(Long mddCuentaFK) {
        this.mddCuentaFK = mddCuentaFK;
    }

    public String getMddTipoMovimiento() {
        return mddTipoMovimiento;
    }

    public void setMddTipoMovimiento(String mddTipoMovimiento) {
        this.mddTipoMovimiento = mddTipoMovimiento;
    }

    public LocalDateTime getMddFecha() {
        return mddFecha;
    }

    public void setMddFecha(LocalDateTime mddFecha) {
        this.mddFecha = mddFecha;
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

    public LocalDateTime getMddFechaDesde() {
        return mddFechaDesde;
    }

    public void setMddFechaDesde(LocalDateTime mddFechaDesde) {
        this.mddFechaDesde = mddFechaDesde;
    }

    public LocalDateTime getMddFechaHasta() {
        return mddFechaHasta;
    }

    public void setMddFechaHasta(LocalDateTime mddFechaHasta) {
        this.mddFechaHasta = mddFechaHasta;
    }

    public Integer getMddFiltroFecha() {
        return mddFiltroFecha;
    }

    public void setMddFiltroFecha(Integer mddFiltroFecha) {
        this.mddFiltroFecha = mddFiltroFecha;
    }

    public LocalDate getMddFechaDesdeS() {
        return mddFechaDesdeS;
    }

    public void setMddFechaDesdeS(LocalDate mddFechaDesdeS) {
        this.mddFechaDesdeS = mddFechaDesdeS;
    }

    public LocalDate getMddFechaHastaS() {
        return mddFechaHastaS;
    }

    public void setMddFechaHastaS(LocalDate mddFechaHastaS) {
        this.mddFechaHastaS = mddFechaHastaS;
    }

}
