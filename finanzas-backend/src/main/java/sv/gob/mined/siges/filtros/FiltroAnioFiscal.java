/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Clase para los filtros del año fiscal.
 *
 * @author jgiron
 */
public class FiltroAnioFiscal implements Serializable {

    private Long aniPk;

    private Integer aniAnio;

    private LocalDateTime aniDesde;

    private Boolean aniEjecucion;

    private Boolean aniPlanificacion;

    private LocalDateTime aniHasta;

    private String aniNombre;

    private LocalDateTime aniUltModFecha;

    private String aniUltModUsuario;

    private Integer aniVersion;

    private Boolean aniCerrado;

    private Boolean aniFormulacionCe;

    private Boolean aniAjusteCe;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    //Auxiliar
    private Boolean seNecesitaDistinct;

    public FiltroAnioFiscal() {
        this.seNecesitaDistinct = Boolean.FALSE;
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

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }

    public Long getAniPk() {
        return aniPk;
    }

    public void setAniPk(Long aniPk) {
        this.aniPk = aniPk;
    }

    public Integer getAniAnio() {
        return aniAnio;
    }

    public void setAniAnio(Integer aniAnio) {
        this.aniAnio = aniAnio;
    }

    public LocalDateTime getAniDesde() {
        return aniDesde;
    }

    public void setAniDesde(LocalDateTime aniDesde) {
        this.aniDesde = aniDesde;
    }

    public Boolean getAniEjecucion() {
        return aniEjecucion;
    }

    public void setAniEjecucion(Boolean aniEjecucion) {
        this.aniEjecucion = aniEjecucion;
    }

    public Boolean getAniPlanificacion() {
        return aniPlanificacion;
    }

    public void setAniPlanificacion(Boolean aniPlanificacion) {
        this.aniPlanificacion = aniPlanificacion;
    }

    public LocalDateTime getAniHasta() {
        return aniHasta;
    }

    public void setAniHasta(LocalDateTime aniHasta) {
        this.aniHasta = aniHasta;
    }

    public String getAniNombre() {
        return aniNombre;
    }

    public void setAniNombre(String aniNombre) {
        this.aniNombre = aniNombre;
    }

    public LocalDateTime getAniUltModFecha() {
        return aniUltModFecha;
    }

    public void setAniUltModFecha(LocalDateTime aniUltModFecha) {
        this.aniUltModFecha = aniUltModFecha;
    }

    public String getAniUltModUsuario() {
        return aniUltModUsuario;
    }

    public void setAniUltModUsuario(String aniUltModUsuario) {
        this.aniUltModUsuario = aniUltModUsuario;
    }

    public Integer getAniVersion() {
        return aniVersion;
    }

    public void setAniVersion(Integer aniVersion) {
        this.aniVersion = aniVersion;
    }

    public Boolean getAniCerrado() {
        return aniCerrado;
    }

    public void setAniCerrado(Boolean aniCerrado) {
        this.aniCerrado = aniCerrado;
    }

    public Boolean getAniFormulacionCe() {
        return aniFormulacionCe;
    }

    public void setAniFormulacionCe(Boolean aniFormulacionCe) {
        this.aniFormulacionCe = aniFormulacionCe;
    }

    public Boolean getAniAjusteCe() {
        return aniAjusteCe;
    }

    public void setAniAjusteCe(Boolean aniAjusteCe) {
        this.aniAjusteCe = aniAjusteCe;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

}
