/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.List;
import javax.ejb.SessionBean;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.constantes.ConstantesOperaciones;

/**
 * Filtro de las cuentas bancarias de los centros educativos
 *
 * @author Sofis Solutions
 */
public class FiltroCajaChica implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;
    private Long first;
    private Long bccPk;
    private Long subComponenteFk;
    private String bccNumeroCuenta;
    private String bccTitular;
    private Long bccSedeFk;
    private Boolean otrosIngresos;
    private List<Long> sedesIds;
    
    private Boolean bccHabilitado;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private String securityOperation;
    private Boolean seNecesitaDistinct;
    
    @Inject
    private SessionBean sessionBean;

    public FiltroCajaChica() {

        super();
        this.first = 0L;
        this.seNecesitaDistinct = Boolean.FALSE;
        this.securityOperation = ConstantesOperaciones.BUSCAR_CUENTASBANCARIASCC;
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
        return bccPk;
    }

    public void setPesPk(Long bccPk) {
        this.bccPk = bccPk;
    }

    public Long getBccPk() {
        return bccPk;
    }

    public void setBccPk(Long bccPk) {
        this.bccPk = bccPk;
    }

    public String getBccNumeroCuenta() {
        return bccNumeroCuenta;
    }

    public void setBccNumeroCuenta(String bccNumeroCuenta) {
        this.bccNumeroCuenta = bccNumeroCuenta;
    }

    public String getBccTitular() {
        return bccTitular;
    }

    public void setBccTitular(String bccTitular) {
        this.bccTitular = bccTitular;
    }

    public Boolean getBccHabilitado() {
        return bccHabilitado;
    }

    public void setBccHabilitado(Boolean bccHabilitado) {
        this.bccHabilitado = bccHabilitado;
    }

    public Boolean getPesHabilitado() {
        return bccHabilitado;
    }

    public void setPesHabilitado(Boolean bccHabilitado) {
        this.bccHabilitado = bccHabilitado;
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

    public Long getBccSedeFk() {
        return bccSedeFk;
    }

    public void setBccSedeFk(Long bccSedeFk) {
        this.bccSedeFk = bccSedeFk;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
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

    public Boolean getOtrosIngresos() {
        return otrosIngresos;
    }

    public void setOtrosIngresos(Boolean otrosIngresos) {
        this.otrosIngresos = otrosIngresos;
    }

    public Long getSubComponenteFk() {
        return subComponenteFk;
    }

    public void setSubComponenteFk(Long subComponenteFk) {
        this.subComponenteFk = subComponenteFk;
    }
    
    
    
}
