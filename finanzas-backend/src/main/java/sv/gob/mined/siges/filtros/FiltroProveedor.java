/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.ejb.SessionBean;
import javax.inject.Inject;
import javax.servlet.FilterConfig;

/**
 * Filtro de Proveedor
 * @author Sofis Solutions
 */
public class FiltroProveedor implements Serializable {

    private FilterConfig filterConfig = null;

    private Long proId;
    private String proCodigo;
    private String proEstado;
    private String proNombre;
    private Boolean proActivo;
    private String proNit;
    private LocalDateTime proUltMod;
    private String proUltUsuario;
    private Integer proVersion;
    private String proNitNombre;
    private Boolean proveedorMined;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    @Inject
    private SessionBean sessionBean;

    public FiltroProveedor() {

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

    public String getProCodigo() {
        return proCodigo;
    }

    public void setProCodigo(String proCodigo) {
        this.proCodigo = proCodigo;
    }

    public String getProEstado() {
        return proEstado;
    }

    public void setProEstado(String proEstado) {
        this.proEstado = proEstado;
    }

    public String getProNombre() {
        return proNombre;
    }

    public void setProNombre(String proNombre) {
        this.proNombre = proNombre;
    }

    public Boolean getProActivo() {
        return proActivo;
    }

    public void setProActivo(Boolean proActivo) {
        this.proActivo = proActivo;
    }

    public String getProNit() {
        return proNit;
    }

    public void setProNit(String proNit) {
        this.proNit = proNit;
    }

    public LocalDateTime getProUltMod() {
        return proUltMod;
    }

    public void setProUltMod(LocalDateTime proUltMod) {
        this.proUltMod = proUltMod;
    }

    public String getProUltUsuario() {
        return proUltUsuario;
    }

    public void setProUltUsuario(String proUltUsuario) {
        this.proUltUsuario = proUltUsuario;
    }

    public Integer getProVersion() {
        return proVersion;
    }

    public void setProVersion(Integer proVersion) {
        this.proVersion = proVersion;
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

    public String getProNitNombre() {
        return proNitNombre;
    }

    public void setProNitNombre(String proNitNombre) {
        this.proNitNombre = proNitNombre;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Boolean getProveedorMined() {
        return proveedorMined;
    }

    public void setProveedorMined(Boolean proveedorMined) {
        this.proveedorMined = proveedorMined;
    }

}
