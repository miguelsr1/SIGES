/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.ejb.SessionBean;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.persistencia.entidades.SgRequerimientoFondo;

/**
 * Filtro de Compromiso Presupuestario
 * @author Sofis Solutions
 */
public class FiltroCompromisoPresupuestario implements Serializable {

    private FilterConfig filterConfig = null;

    private Long cprPk;
    private SgRequerimientoFondo cprRequerimientoFondoFk;
    private String cprNumeroPresupuestario;
    private LocalDateTime cprUltMod;
    private String cprUltUsuario;
    private Integer cprVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    @Inject
    private SessionBean sessionBean;

    public FiltroCompromisoPresupuestario() {

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

    public Long getCprPk() {
        return cprPk;
    }

    public void setCprPk(Long cprPk) {
        this.cprPk = cprPk;
    }

    public SgRequerimientoFondo getCprRequerimientoFondoFk() {
        return cprRequerimientoFondoFk;
    }

    public void setCprRequerimientoFondoFk(SgRequerimientoFondo cprRequerimientoFondoFk) {
        this.cprRequerimientoFondoFk = cprRequerimientoFondoFk;
    }

    public String getCprNumeroPresupuestario() {
        return cprNumeroPresupuestario;
    }

    public void setCprNumeroPresupuestario(String cprNumeroPresupuestario) {
        this.cprNumeroPresupuestario = cprNumeroPresupuestario;
    }

    public LocalDateTime getCprUltMod() {
        return cprUltMod;
    }

    public void setCprUltMod(LocalDateTime cprUltMod) {
        this.cprUltMod = cprUltMod;
    }

    public String getCprUltUsuario() {
        return cprUltUsuario;
    }

    public void setCprUltUsuario(String cprUltUsuario) {
        this.cprUltUsuario = cprUltUsuario;
    }

    public Integer getCprVersion() {
        return cprVersion;
    }

    public void setCprVersion(Integer cprVersion) {
        this.cprVersion = cprVersion;
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
