/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.web.enumerados.EnumServicioEducativoEstado;

public class FiltroServicioEducativo extends FiltroSeccion implements Serializable {

    private Long sduPk;
    private EnumServicioEducativoEstado sduEstado;
    private LocalDate sduFechaDesde;
    private LocalDate sduFechaHasta;
    private Long sduSedeAdscripta;
    private String sduNumeroTramite;
    private Boolean tieneOpcion;
    private Boolean sduExisteSeccion;
    
    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private Boolean incluirSecciones;
    private String securityOperation;

    public FiltroServicioEducativo() {
        super();
        this.incluirSecciones = Boolean.FALSE;
        this.first = 0L;
    }

    public Long getSduPk() {
        return sduPk;
    }

    public void setSduPk(Long sduPk) {
        this.sduPk = sduPk;
    }

    public EnumServicioEducativoEstado getSduEstado() {
        return sduEstado;
    }

    public void setSduEstado(EnumServicioEducativoEstado sduEstado) {
        this.sduEstado = sduEstado;
    }

    public LocalDate getSduFechaDesde() {
        return sduFechaDesde;
    }

    public void setSduFechaDesde(LocalDate sduFechaDesde) {
        this.sduFechaDesde = sduFechaDesde;
    }

    public LocalDate getSduFechaHasta() {
        return sduFechaHasta;
    }

    public void setSduFechaHasta(LocalDate sduFechaHasta) {
        this.sduFechaHasta = sduFechaHasta;
    }

    public Long getSduSedeAdscripta() {
        return sduSedeAdscripta;
    }

    public void setSduSedeAdscripta(Long sduSedeAdscripta) {
        this.sduSedeAdscripta = sduSedeAdscripta;
    }

    public String getSduNumeroTramite() {
        return sduNumeroTramite;
    }

    public void setSduNumeroTramite(String sduNumeroTramite) {
        this.sduNumeroTramite = sduNumeroTramite;
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

    public Boolean getIncluirSecciones() {
        return incluirSecciones;
    }

    public void setIncluirSecciones(Boolean incluirSecciones) {
        this.incluirSecciones = incluirSecciones;
    }

    public String getSecurityOperation() {
        return securityOperation;
    }

    public void setSecurityOperation(String securityOperation) {
        this.securityOperation = securityOperation;
    }

    public Boolean getTieneOpcion() {
        return tieneOpcion;
    }

    public void setTieneOpcion(Boolean tieneOpcion) {
        this.tieneOpcion = tieneOpcion;
    }

    public Boolean getSduExisteSeccion() {
        return sduExisteSeccion;
    }

    public void setSduExisteSeccion(Boolean sduExisteSeccion) {
        this.sduExisteSeccion = sduExisteSeccion;
    }


    
}
