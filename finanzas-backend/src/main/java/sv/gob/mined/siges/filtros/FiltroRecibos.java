/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import javax.servlet.FilterConfig;

/**
 * Filtro de los documnetos de recibos
 *
 * @author Sofis Solutions
 */
public class FiltroRecibos implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;
    private Long first;

    private Long recPk;
    private LocalDate recFecha;
    private Long recTransferencia;
    private Long sedePk;
    private Integer anioFiscal;
    
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroRecibos() {

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

    public Long getRecPk() {
        return recPk;
    }

    public void setRecPk(Long recPk) {
        this.recPk = recPk;
    }

    public LocalDate getRecFecha() {
        return recFecha;
    }

    public void setRecFecha(LocalDate recFecha) {
        this.recFecha = recFecha;
    }

    public Long getRecTransferencia() {
        return recTransferencia;
    }

    public void setRecTransferencia(Long recTransferencia) {
        this.recTransferencia = recTransferencia;
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

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Integer getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(Integer anioFiscal) {
        this.anioFiscal = anioFiscal;
    }
    
}
