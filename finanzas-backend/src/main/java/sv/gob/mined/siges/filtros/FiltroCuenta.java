/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.SessionBean;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.persistencia.entidades.siap2.SsCuenta;

/**
 * Filtro de Cuenta de SIAP2
 * @author Sofis Solutions
 */
public class FiltroCuenta implements Serializable {

    private FilterConfig filterConfig = null;

    private Long cuId;
    private String cuCodigo;
    private String cuNombre;
    private String cuDescripcion;
    private Boolean cuHabilitado;
    private Integer cuOrden;
    private SsCuenta cuCuentaPadre;
    private Date cuUltMod;
    private String cuUltUsuario;
    private Integer cuVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    @Inject
    private SessionBean sessionBean;

    public FiltroCuenta() {

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

    public Long getCuId() {
        return cuId;
    }

    public void setCuId(Long cuId) {
        this.cuId = cuId;
    }

    public String getCuCodigo() {
        return cuCodigo;
    }

    public void setCuCodigo(String cuCodigo) {
        this.cuCodigo = cuCodigo;
    }

    public String getCuNombre() {
        return cuNombre;
    }

    public void setCuNombre(String cuNombre) {
        this.cuNombre = cuNombre;
    }

    public String getCuDescripcion() {
        return cuDescripcion;
    }

    public void setCuDescripcion(String cuDescripcion) {
        this.cuDescripcion = cuDescripcion;
    }

    public Boolean getCuHabilitado() {
        return cuHabilitado;
    }

    public void setCuHabilitado(Boolean cuHabilitado) {
        this.cuHabilitado = cuHabilitado;
    }

    public Integer getCuOrden() {
        return cuOrden;
    }

    public void setCuOrden(Integer cuOrden) {
        this.cuOrden = cuOrden;
    }

    public SsCuenta getCuCuentaPadre() {
        return cuCuentaPadre;
    }

    public void setCuCuentaPadre(SsCuenta cuCuentaPadre) {
        this.cuCuentaPadre = cuCuentaPadre;
    }

    public Date getCuUltMod() {
        return cuUltMod;
    }

    public void setCuUltMod(Date cuUltMod) {
        this.cuUltMod = cuUltMod;
    }

    public String getCuUltUsuario() {
        return cuUltUsuario;
    }

    public void setCuUltUsuario(String cuUltUsuario) {
        this.cuUltUsuario = cuUltUsuario;
    }

    public Integer getCuVersion() {
        return cuVersion;
    }

    public void setCuVersion(Integer cuVersion) {
        this.cuVersion = cuVersion;
    }

}
