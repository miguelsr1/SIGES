/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.Objects;
import javax.ejb.SessionBean;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.persistencia.entidades.catalogo.SgDepartamento;

/**
 * Filtro de las cuentas bancarias de las direcciones departamentales
 *
 * @author Sofis Solutions
 */
public class FiltroCuentasBancariasDD implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;
    private Long first;
    private Long cbdPk;
    private Long cbdBancoFk;
    private Long cbdDirDepFk;
    private String cbdNumeroCuenta;
    private String cbdTitular;
    private Boolean cbdHabilitado;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    private String securityOperation;
    private Boolean seNecesitaDistinct;
    
    @Inject
    private SessionBean sessionBean;
    
    private SgDepartamento cbdDepartamento;

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

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
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
    
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.first);
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
        final FiltroCuentasBancariasDD other = (FiltroCuentasBancariasDD) obj;
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        return true;
    }
    
    

}
