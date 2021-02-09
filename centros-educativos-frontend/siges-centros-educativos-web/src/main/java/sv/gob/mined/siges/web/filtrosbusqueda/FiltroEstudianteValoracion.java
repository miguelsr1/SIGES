/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.time.LocalDateTime;

public class FiltroEstudianteValoracion {

    private String esvTipoValoracion;

    private LocalDateTime esvFechaPublicacion;

    private String esvValoracion;

    private Long esvEstudiante;

    private LocalDateTime esvUltModFecha;

    private String esvUltModUsuario;

    private Integer esvVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroEstudianteValoracion() {
    }

    public String getEsvTipoValoracion() {
        return esvTipoValoracion;
    }

    public void setEsvTipoValoracion(String esvTipoValoracion) {
        this.esvTipoValoracion = esvTipoValoracion;
    }

    public LocalDateTime getEsvFechaPublicacion() {
        return esvFechaPublicacion;
    }

    public void setEsvFechaPublicacion(LocalDateTime esvFechaPublicacion) {
        this.esvFechaPublicacion = esvFechaPublicacion;
    }

    public String getEsvValoracion() {
        return esvValoracion;
    }

    public void setEsvValoracion(String esvValoracion) {
        this.esvValoracion = esvValoracion;
    }

    public Long getEsvEstudiante() {
        return esvEstudiante;
    }

    public void setEsvEstudiante(Long esvEstudiante) {
        this.esvEstudiante = esvEstudiante;
    }

    public LocalDateTime getEsvUltModFecha() {
        return esvUltModFecha;
    }

    public void setEsvUltModFecha(LocalDateTime esvUltModFecha) {
        this.esvUltModFecha = esvUltModFecha;
    }

    public String getEsvUltModUsuario() {
        return esvUltModUsuario;
    }

    public void setEsvUltModUsuario(String esvUltModUsuario) {
        this.esvUltModUsuario = esvUltModUsuario;
    }

    public Integer getEsvVersion() {
        return esvVersion;
    }

    public void setEsvVersion(Integer esvVersion) {
        this.esvVersion = esvVersion;
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

}
