/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;


import java.io.Serializable;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;

/**
 *
 * @author Sofis Solutions
 */
//@WebFilter()
public class FiltroCuentasBancariasDD  implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());

    private FilterConfig filterConfig = null;
    private Long first;
    private Long cbdPk;
    private String cbdNumeroCuenta;
    private String cbdTitular;
    private Long cbdBancoFk;
    private Long cbdDirDepFk;
    private Boolean cbdHabilitado;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    private SgDepartamento cbdDepartamento;
    
    private String securityOperation;
    private Boolean seNecesitaDistinct;
    
    public FiltroCuentasBancariasDD() {
        
        super();
        this.first = 0L;
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
        return cbdPk;
    }

    public void setPesPk(Long cbdPk) {
        this.cbdPk = cbdPk;
    }

    public Long getCbdPk() {
        return cbdPk;
    }

    public void setCbdPk(Long cbdPk) {
        this.cbdPk = cbdPk;
    }

    public String getCbdNumeroCuenta() {
        return cbdNumeroCuenta;
    }

    public void setCbdNumeroCuenta(String cbdNumeroCuenta) {
        this.cbdNumeroCuenta = cbdNumeroCuenta;
    }

    public String getCbdTitular() {
        return cbdTitular;
    }

    public void setCbdTitular(String cbdTitular) {
        this.cbdTitular = cbdTitular;
    }

    public Long getCbdBancoFk() {
        return cbdBancoFk;
    }

    public void setCbdBancoFk(Long cbdBancoFk) {
        this.cbdBancoFk = cbdBancoFk;
    }

    public Boolean getCbdHabilitado() {
        return cbdHabilitado;
    }

    public void setCbdHabilitado(Boolean cbdHabilitado) {
        this.cbdHabilitado = cbdHabilitado;
    }

    public Boolean getPesHabilitado() {
        return cbdHabilitado;
    }

    public void setPesHabilitado(Boolean cbdHabilitado) {
        this.cbdHabilitado = cbdHabilitado;
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

    public Long getCbdDirDepFk() {
        return cbdDirDepFk;
    }

    public void setCbdDirDepFk(Long cbdDirDepFk) {
        this.cbdDirDepFk = cbdDirDepFk;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public SgDepartamento getCbdDepartamento() {
        return cbdDepartamento;
    }

    public void setCbdDepartamento(SgDepartamento cbdDepartamento) {
        this.cbdDepartamento = cbdDepartamento;
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
    
    
    
}
