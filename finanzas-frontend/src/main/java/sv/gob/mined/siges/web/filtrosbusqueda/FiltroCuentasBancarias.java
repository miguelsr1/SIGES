/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.List;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgSede;

/**
 *
 * @author Sofis Solutions
 */
//@WebFilter()
public class FiltroCuentasBancarias implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;
    private Long first;
    private Long cbcPk;
    private String cbcNumeroCuenta;
    private String cbcTitular;
    private Long cbcBancoFk;
    private Long cbcSedeFk;
    private Long cbcCuentaTipoCuenta;
    private Boolean cbcHabilitado;
    private Boolean cbcOtroIngreso;
    private Long maxResults;
    private List<Long> sedesIds;
    
    
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private SgSede SedeFk;
    private Integer cbcVersion;
    private Long departamentoFk;
    
    private String securityOperation;
    private Boolean seNecesitaDistinct;

    public FiltroCuentasBancarias() {

        super();
        this.first = 0L;
        this.seNecesitaDistinct = Boolean.FALSE;
        this.securityOperation = ConstantesOperaciones.BUSCAR_CUENTAS_BANCARIAS;
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

    public Boolean getCbcHabilitado() {
        return cbcHabilitado;
    }

    public void setCbcHabilitado(Boolean cbcHabilitado) {
        this.cbcHabilitado = cbcHabilitado;
    }

    public Long getCbcSedeFk() {
        return cbcSedeFk;
    }

    public void setCbcSedeFk(Long cbcSedeFk) {
        this.cbcSedeFk = cbcSedeFk;
    }

    public SgSede getSedeFk() {
        return SedeFk;
    }

    public void setSedeFk(SgSede SedeFk) {
        this.SedeFk = SedeFk;
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

    public Integer getCbcVersion() {
        return cbcVersion;
    }

    public void setCbcVersion(Integer cbcVersion) {
        this.cbcVersion = cbcVersion;
    }

    public Long getDepartamentoFk() {
        return departamentoFk;
    }

    public void setDepartamentoFk(Long departamentoFk) {
        this.departamentoFk = departamentoFk;
    }

    public List<Long> getSedesIds() {
        return sedesIds;
    }

    public void setSedesIds(List<Long> sedesIds) {
        this.sedesIds = sedesIds;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public Long getCbcCuentaTipoCuenta() {
        return cbcCuentaTipoCuenta;
    }

    public void setCbcCuentaTipoCuenta(Long cbcCuentaTipoCuenta) {
        this.cbcCuentaTipoCuenta = cbcCuentaTipoCuenta;
    }

    public Boolean getCbcOtroIngreso() {
        return cbcOtroIngreso;
    }

    public void setCbcOtroIngreso(Boolean cbcOtroIngreso) {
        this.cbcOtroIngreso = cbcOtroIngreso;
    }
    
}
