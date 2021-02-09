/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.ejb.SessionBean;
import javax.inject.Inject;
import javax.servlet.FilterConfig;

/**
 * Filtro autorización edición presupuesto
 * @author Sofis Solutions
 */
public class FiltroAutorizacionEdicionPresupuesto implements Serializable {

    private FilterConfig filterConfig = null;

    private Long proId;

    private Long autPk;
    private Long autPresupuestoFk;
    private Long autUsuarioValidadoFk;
    private LocalDateTime autUltMod;
    private String autUltUsuario;
    private Integer autVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    @Inject
    private SessionBean sessionBean;

    public FiltroAutorizacionEdicionPresupuesto() {

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

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public Long getAutPk() {
        return autPk;
    }

    public void setAutPk(Long autPk) {
        this.autPk = autPk;
    }

    public Long getAutPresupuestoFk() {
        return autPresupuestoFk;
    }

    public void setAutPresupuestoFk(Long autPresupuestoFk) {
        this.autPresupuestoFk = autPresupuestoFk;
    }

    public Long getAutUsuarioValidadoFk() {
        return autUsuarioValidadoFk;
    }

    public void setAutUsuarioValidadoFk(Long autUsuarioValidadoFk) {
        this.autUsuarioValidadoFk = autUsuarioValidadoFk;
    }

    public LocalDateTime getAutUltMod() {
        return autUltMod;
    }

    public void setAutUltMod(LocalDateTime autUltMod) {
        this.autUltMod = autUltMod;
    }

    public String getAutUltUsuario() {
        return autUltUsuario;
    }

    public void setAutUltUsuario(String autUltUsuario) {
        this.autUltUsuario = autUltUsuario;
    }

    public Integer getAutVersion() {
        return autVersion;
    }

    public void setAutVersion(Integer autVersion) {
        this.autVersion = autVersion;
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.filterConfig);
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
        final FiltroAutorizacionEdicionPresupuesto other = (FiltroAutorizacionEdicionPresupuesto) obj;
        if (!Objects.equals(this.filterConfig, other.filterConfig)) {
            return false;
        }
        return true;
    }

}
