/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;


import java.io.Serializable;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.dto.SgPresupuestoEscolar;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroDocumentos  implements Serializable {

    private FilterConfig filterConfig = null;
    private Long first;
    private Long docPk;
    private SgPresupuestoEscolar docPresupuestoFk;
    
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    @Inject
    private SessionBean sessionBean;

    public FiltroDocumentos() {
        
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

    public Long getDocPk() {
        return docPk;
    }

    public void setDocPk(Long docPk) {
        this.docPk = docPk;
    }

    public SgPresupuestoEscolar getDocPresupuestoFk() {
        return docPresupuestoFk;
    }

    public void setDocPresupuestoFk(SgPresupuestoEscolar docPresupuestoFk) {
        this.docPresupuestoFk = docPresupuestoFk;
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

}
