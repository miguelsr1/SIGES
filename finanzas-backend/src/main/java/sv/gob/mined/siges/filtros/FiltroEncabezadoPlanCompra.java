/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.ejb.SessionBean;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.enumerados.EnumPlanCompraEstado;

/**
 * Filtro del plan de compras
 *
 * @author Sofis Solutions
 */
public class FiltroEncabezadoPlanCompra implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;

    private Long plaPk;

    private Long plaPresupuestoFk;

    private EnumPlanCompraEstado plaEstado;

    private String plaComentario;

    private Long sedeFk;

    private Integer anioFiscalFk;

    private LocalDateTime plaUltModFecha;

    private String plaUltModUsuario;

    private Integer plaVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    @Inject
    private SessionBean sessionBean;

    public FiltroEncabezadoPlanCompra() {

        super();
        this.first = 0L;
    }

    public Long getPlaPk() {
        return plaPk;
    }

    public void setPlaPk(Long plaPk) {
        this.plaPk = plaPk;
    }

    public Long getPlaPresupuestoFk() {
        return plaPresupuestoFk;
    }

    public void setPlaPresupuestoFk(Long plaPresupuestoFk) {
        this.plaPresupuestoFk = plaPresupuestoFk;
    }

    public EnumPlanCompraEstado getPlaEstado() {
        return plaEstado;
    }

    public void setPlaEstado(EnumPlanCompraEstado plaEstado) {
        this.plaEstado = plaEstado;
    }

    public String getPlaComentario() {
        return plaComentario;
    }

    public void setPlaComentario(String plaComentario) {
        this.plaComentario = plaComentario;
    }

    public LocalDateTime getPlaUltModFecha() {
        return plaUltModFecha;
    }

    public void setPlaUltModFecha(LocalDateTime plaUltModFecha) {
        this.plaUltModFecha = plaUltModFecha;
    }

    public String getPlaUltModUsuario() {
        return plaUltModUsuario;
    }

    public void setPlaUltModUsuario(String plaUltModUsuario) {
        this.plaUltModUsuario = plaUltModUsuario;
    }

    public Integer getPlaVersion() {
        return plaVersion;
    }

    public void setPlaVersion(Integer plaVersion) {
        this.plaVersion = plaVersion;
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

    public Long getSedeFk() {
        return sedeFk;
    }

    public void setSedeFk(Long sedeFk) {
        this.sedeFk = sedeFk;
    }

    public Integer getAnioFiscalFk() {
        return anioFiscalFk;
    }

    public void setAnioFiscalFk(Integer anioFiscalFk) {
        this.anioFiscalFk = anioFiscalFk;
    }

}
