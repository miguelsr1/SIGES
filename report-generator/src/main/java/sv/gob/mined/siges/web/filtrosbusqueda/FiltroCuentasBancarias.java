/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;


import java.io.Serializable;
import javax.servlet.FilterConfig;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroCuentasBancarias  implements Serializable {

    private FilterConfig filterConfig = null;
    private Long first;
    private Long cbcPk;
    private String cbcNumeroCuenta;
    private Long componentePk;
    private String cbcTitular;
    private Long cbcBancoFk;
    private Long cbcSedeFk;
    private Long cbcCuentaTipoCuenta;
    private Boolean cbcHabilitado;
    private Boolean sinComponente;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroCuentasBancarias() {
        
        super();
        this.first = 0L;
        this.sinComponente = Boolean.FALSE;
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

    public Long getPesPk() {
        return cbcPk;
    }

    public void setPesPk(Long cbcPk) {
        this.cbcPk = cbcPk;
    }

    public Long getCbcPk() {
        return cbcPk;
    }

    public void setCbcPk(Long cbcPk) {
        this.cbcPk = cbcPk;
    }

    public String getCbcNumeroCuenta() {
        return cbcNumeroCuenta;
    }

    public void setCbcNumeroCuenta(String cbcNumeroCuenta) {
        this.cbcNumeroCuenta = cbcNumeroCuenta;
    }

    public String getCbcTitular() {
        return cbcTitular;
    }

    public void setCbcTitular(String cbcTitular) {
        this.cbcTitular = cbcTitular;
    }

    public Long getCbcBancoFk() {
        return cbcBancoFk;
    }

    public void setCbcBancoFk(Long cbcBancoFk) {
        this.cbcBancoFk = cbcBancoFk;
    }

    public Long getCbcCuentaTipoCuenta() {
        return cbcCuentaTipoCuenta;
    }

    public void setCbcCuentaTipoCuenta(Long cbcCuentaTipoCuenta) {
        this.cbcCuentaTipoCuenta = cbcCuentaTipoCuenta;
    }

    public Boolean getCbcHabilitado() {
        return cbcHabilitado;
    }

    public void setCbcHabilitado(Boolean cbcHabilitado) {
        this.cbcHabilitado = cbcHabilitado;
    }

    public Boolean getPesHabilitado() {
        return cbcHabilitado;
    }

    public void setPesHabilitado(Boolean cbcHabilitado) {
        this.cbcHabilitado = cbcHabilitado;
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

    public Long getCbcSedeFk() {
        return cbcSedeFk;
    }

    public void setCbcSedeFk(Long cbcSedeFk) {
        this.cbcSedeFk = cbcSedeFk;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getComponentePk() {
        return componentePk;
    }

    public void setComponentePk(Long componentePk) {
        this.componentePk = componentePk;
    }

    public Boolean getSinComponente() {
        return sinComponente;
    }

    public void setSinComponente(Boolean sinComponente) {
        this.sinComponente = sinComponente;
    }
    
    

}
