/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.web.beans.SessionBean;
import sv.gob.mined.siges.web.constantes.ConstantesOperaciones;
import sv.gob.mined.siges.web.dto.SgSede;
import sv.gob.mined.siges.web.enumerados.EnumPresupuestoEscolarEstado;

/**
 *
 * @author Sofis Solutions
 */
//@WebFilter()
public class FiltroPresupuestoEscolar implements Serializable {

    
    private FilterConfig filterConfig = null;
    private Long first;
    private Long pesPk;
    private String pesNombre;
    private String pesNombreBusqueda;
    private String pesCodigo;
    private String pesDescripcion;
    private Boolean pesHabilitado;
    private LocalDate pesUltmodFecha;
    private String pesUltmodUsuario;
    private Integer pesVersion;
    private SgSede pesSedeFk;
    private Long idSede;
    private Integer anioFiscal;
    private EnumPresupuestoEscolarEstado pesEstado;
    private Boolean pesEdicion;
    private List<Long> sedesIds;

    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    private String securityOperation;
    private Boolean seNecesitaDistinct;

    @Inject
    private SessionBean sessionBean;

    public FiltroPresupuestoEscolar() {
        super();
        this.first = 0L;
        this.securityOperation = ConstantesOperaciones.BUSCAR_PRESUPUESTOS_ESCOLARES;
        this.seNecesitaDistinct=Boolean.FALSE;
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
        return pesPk;
    }

    public void setPesPk(Long pesPk) {
        this.pesPk = pesPk;
    }

    public String getPesNombre() {
        return pesNombre;
    }

    public void setPesNombre(String pesNombre) {
        this.pesNombre = pesNombre;
    }

    public String getPesNombreBusqueda() {
        return pesNombreBusqueda;
    }

    public void setPesNombreBusqueda(String pesNombreBusqueda) {
        this.pesNombreBusqueda = pesNombreBusqueda;
    }

    public String getPesCodigo() {
        return pesCodigo;
    }

    public void setPesCodigo(String pesCodigo) {
        this.pesCodigo = pesCodigo;
    }

    public String getPesDescripcion() {
        return pesDescripcion;
    }

    public void setPesDescripcion(String pesDescripcion) {
        this.pesDescripcion = pesDescripcion;
    }

    public Boolean getPesHabilitado() {
        return pesHabilitado;
    }

    public void setPesHabilitado(Boolean pesHabilitado) {
        this.pesHabilitado = pesHabilitado;
    }

    public LocalDate getPesUltmodFecha() {
        return pesUltmodFecha;
    }

    public void setPesUltmodFecha(LocalDate pesUltmodFecha) {
        this.pesUltmodFecha = pesUltmodFecha;
    }

    public String getPesUltmodUsuario() {
        return pesUltmodUsuario;
    }

    public void setPesUltmodUsuario(String pesUltmodUsuario) {
        this.pesUltmodUsuario = pesUltmodUsuario;
    }

    public Integer getPesVersion() {
        return pesVersion;
    }

    public void setPesVersion(Integer pesVersion) {
        this.pesVersion = pesVersion;
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

    public Long getIdSede() {
        return idSede;
    }

    public void setIdSede(Long idSede) {
        this.idSede = idSede;
    }

    public Integer getAnioFiscal() {
        return anioFiscal;
    }

    public void setAnioFiscal(Integer anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    public EnumPresupuestoEscolarEstado getPesEstado() {
        return pesEstado;
    }

    public void setPesEstado(EnumPresupuestoEscolarEstado pesEstado) {
        this.pesEstado = pesEstado;
    }

    public SgSede getPesSedeFk() {
        return pesSedeFk;
    }

    public void setPesSedeFk(SgSede pesSedeFk) {
        this.pesSedeFk = pesSedeFk;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Boolean getPesEdicion() {
        return pesEdicion;
    }

    public void setPesEdicion(Boolean pesEdicion) {
        this.pesEdicion = pesEdicion;
    }

    public List<Long> getSedesIds() {
        return sedesIds;
    }

    public void setSedesIds(List<Long> sedesIds) {
        this.sedesIds = sedesIds;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }
    
    
    
}   
