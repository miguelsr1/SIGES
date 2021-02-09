/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.servlet.FilterConfig;
import sv.gob.mined.siges.web.dto.catalogo.SgDepartamento;

/**
 *
 * @author Sofis Solutions
 */
//@WebFilter()
public class FiltroDireccionDepartamental implements Serializable {

    //private static final Logger LOGGER = Logger.getLogger(FiltroPresupuestoEscolar.class.getName());
    private FilterConfig filterConfig = null;

    private Long dedPk;
    
    private Long departamentoPk;

    private String dedNombre;

    private String dedNit;

    private String dedTelefono;

    private String dedFax;

    private String dedIpAutorizada;

    private Boolean dedHabilitado;

    private LocalDateTime dedUltModFecha;

    private String dedUltModUsuario;

    private Integer dedVersion;

    private SgDepartamento dedDepartamentoFk;

    private String decDirectorCargo;
    private String decDirectorNombre;
    private String decPagadorCargo;
    private String decPagadorNombre;
    private String decRefrendarioCargo;
    private String decRefrendarioNombre;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroDireccionDepartamental() {

        super();
        this.first = 0L;
    }

    public Long getDepartamentoPk() {
        return departamentoPk;
    }

    public void setDepartamentoPk(Long departamentoPk) {
        this.departamentoPk = departamentoPk;
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

    public Long getDedPk() {
        return dedPk;
    }

    public void setDedPk(Long dedPk) {
        this.dedPk = dedPk;
    }

    public String getDedNombre() {
        return dedNombre;
    }

    public void setDedNombre(String dedNombre) {
        this.dedNombre = dedNombre;
    }

    public String getDedNit() {
        return dedNit;
    }

    public void setDedNit(String dedNit) {
        this.dedNit = dedNit;
    }

    public String getDedTelefono() {
        return dedTelefono;
    }

    public void setDedTelefono(String dedTelefono) {
        this.dedTelefono = dedTelefono;
    }

    public String getDedFax() {
        return dedFax;
    }

    public void setDedFax(String dedFax) {
        this.dedFax = dedFax;
    }

    public String getDedIpAutorizada() {
        return dedIpAutorizada;
    }

    public void setDedIpAutorizada(String dedIpAutorizada) {
        this.dedIpAutorizada = dedIpAutorizada;
    }

    public Boolean getDedHabilitado() {
        return dedHabilitado;
    }

    public void setDedHabilitado(Boolean dedHabilitado) {
        this.dedHabilitado = dedHabilitado;
    }

    public LocalDateTime getDedUltModFecha() {
        return dedUltModFecha;
    }

    public void setDedUltModFecha(LocalDateTime dedUltModFecha) {
        this.dedUltModFecha = dedUltModFecha;
    }

    public String getDedUltModUsuario() {
        return dedUltModUsuario;
    }

    public void setDedUltModUsuario(String dedUltModUsuario) {
        this.dedUltModUsuario = dedUltModUsuario;
    }

    public Integer getDedVersion() {
        return dedVersion;
    }

    public void setDedVersion(Integer dedVersion) {
        this.dedVersion = dedVersion;
    }

    public SgDepartamento getDedDepartamentoFk() {
        return dedDepartamentoFk;
    }

    public void setDedDepartamentoFk(SgDepartamento dedDepartamentoFk) {
        this.dedDepartamentoFk = dedDepartamentoFk;
    }

    public String getDecDirectorCargo() {
        return decDirectorCargo;
    }

    public void setDecDirectorCargo(String decDirectorCargo) {
        this.decDirectorCargo = decDirectorCargo;
    }

    public String getDecDirectorNombre() {
        return decDirectorNombre;
    }

    public void setDecDirectorNombre(String decDirectorNombre) {
        this.decDirectorNombre = decDirectorNombre;
    }

    public String getDecPagadorCargo() {
        return decPagadorCargo;
    }

    public void setDecPagadorCargo(String decPagadorCargo) {
        this.decPagadorCargo = decPagadorCargo;
    }

    public String getDecPagadorNombre() {
        return decPagadorNombre;
    }

    public void setDecPagadorNombre(String decPagadorNombre) {
        this.decPagadorNombre = decPagadorNombre;
    }

    public String getDecRefrendarioCargo() {
        return decRefrendarioCargo;
    }

    public void setDecRefrendarioCargo(String decRefrendarioCargo) {
        this.decRefrendarioCargo = decRefrendarioCargo;
    }

    public String getDecRefrendarioNombre() {
        return decRefrendarioNombre;
    }

    public void setDecRefrendarioNombre(String decRefrendarioNombre) {
        this.decRefrendarioNombre = decRefrendarioNombre;
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
