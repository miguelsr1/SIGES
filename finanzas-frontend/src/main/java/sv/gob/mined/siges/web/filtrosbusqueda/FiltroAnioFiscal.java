package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
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

    public FiltroAnioFiscal() {

    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
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

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public boolean[] getAscending() {
        return ascending;
    }

    public void setAscending(boolean[] ascending) {
        this.ascending = ascending;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.aniPk);
        hash = 23 * hash + Objects.hashCode(this.aniAnio);
        hash = 23 * hash + Objects.hashCode(this.aniDesde);
        hash = 23 * hash + Objects.hashCode(this.aniEjecucion);
        hash = 23 * hash + Objects.hashCode(this.aniPlanificacion);
        hash = 23 * hash + Objects.hashCode(this.aniHasta);
        hash = 23 * hash + Objects.hashCode(this.aniNombre);
        hash = 23 * hash + Objects.hashCode(this.aniUltModFecha);
        hash = 23 * hash + Objects.hashCode(this.aniUltModUsuario);
        hash = 23 * hash + Objects.hashCode(this.aniVersion);
        hash = 23 * hash + Objects.hashCode(this.aniCerrado);
        hash = 23 * hash + Objects.hashCode(this.aniFormulacionCe);
        hash = 23 * hash + Objects.hashCode(this.aniAjusteCe);
        hash = 23 * hash + Objects.hashCode(this.first);
        hash = 23 * hash + Objects.hashCode(this.maxResults);
        hash = 23 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 23 * hash + Arrays.hashCode(this.ascending);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FiltroAnioFiscal other = (FiltroAnioFiscal) obj;
        if (!Objects.equals(this.aniNombre, other.aniNombre)) {
            return false;
        }
        if (!Objects.equals(this.aniUltModUsuario, other.aniUltModUsuario)) {
            return false;
        }
        if (!Objects.equals(this.aniPk, other.aniPk)) {
            return false;
        }
        if (!Objects.equals(this.aniAnio, other.aniAnio)) {
            return false;
        }
        if (!Objects.equals(this.aniDesde, other.aniDesde)) {
            return false;
        }
        if (!Objects.equals(this.aniEjecucion, other.aniEjecucion)) {
            return false;
        }
        if (!Objects.equals(this.aniPlanificacion, other.aniPlanificacion)) {
            return false;
        }
        if (!Objects.equals(this.aniHasta, other.aniHasta)) {
            return false;
        }
        if (!Objects.equals(this.aniUltModFecha, other.aniUltModFecha)) {
            return false;
        }
        if (!Objects.equals(this.aniVersion, other.aniVersion)) {
            return false;
        }
        if (!Objects.equals(this.aniCerrado, other.aniCerrado)) {
            return false;
        }
        if (!Objects.equals(this.aniFormulacionCe, other.aniFormulacionCe)) {
            return false;
        }
        if (!Objects.equals(this.aniAjusteCe, other.aniAjusteCe)) {
            return false;
        }
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.maxResults, other.maxResults)) {
            return false;
        }
        if (!Arrays.deepEquals(this.orderBy, other.orderBy)) {
            return false;
        }
        if (!Arrays.equals(this.ascending, other.ascending)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
}
