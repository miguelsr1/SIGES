/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.dto.SgCuentasBancarias;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroConciliacionBancaria implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;

    private Long conPk;
    private SgCuentasBancarias conCuentaFk;
    private String cuenta;
    private LocalDate conFechaDesde;
    private LocalDate conFechaHasta;
    private BigDecimal conMonto;
    private LocalDateTime conUltModFecha;
    private String conUltModUsuario;
    private Integer conVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    @Inject
    private SessionBean sessionBean;

    public FiltroConciliacionBancaria() {

        super();
        this.first = 0L;
    }

    // <editor-fold defaultstate="collapsed" desc="Getter's-Setter's">
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
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

    public Long getConPk() {
        return conPk;
    }

    public void setConPk(Long conPk) {
        this.conPk = conPk;
    }

    public SgCuentasBancarias getConCuentaFk() {
        return conCuentaFk;
    }

    public void setConCuentaFk(SgCuentasBancarias conCuentaFk) {
        this.conCuentaFk = conCuentaFk;
    }

    public LocalDate getConFechaDesde() {
        return conFechaDesde;
    }

    public void setConFechaDesde(LocalDate conFechaDesde) {
        this.conFechaDesde = conFechaDesde;
    }

    public LocalDate getConFechaHasta() {
        return conFechaHasta;
    }

    public void setConFechaHasta(LocalDate conFechaHasta) {
        this.conFechaHasta = conFechaHasta;
    }

    public BigDecimal getConMonto() {
        return conMonto;
    }

    public void setConMonto(BigDecimal conMonto) {
        this.conMonto = conMonto;
    }

    public LocalDateTime getConUltModFecha() {
        return conUltModFecha;
    }

    public void setConUltModFecha(LocalDateTime conUltModFecha) {
        this.conUltModFecha = conUltModFecha;
    }

    public String getConUltModUsuario() {
        return conUltModUsuario;
    }

    public void setConUltModUsuario(String conUltModUsuario) {
        this.conUltModUsuario = conUltModUsuario;
    }

    public Integer getConVersion() {
        return conVersion;
    }

    public void setConVersion(Integer conVersion) {
        this.conVersion = conVersion;
    }

    // </editor-fold>
}
