package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroMetodoAdq implements Serializable {

    private Long metId;

    private String metNombre;

    private Boolean metHabilitado;

    private LocalDateTime metUltMod;

    private String metUltUsuario;

    private Integer meVersion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroMetodoAdq() {

    }

    // <editor-fold defaultstate="collapsed" desc="Getters-Setters">
    public Long getMetId() {
        return metId;
    }

    public void setMetId(Long metId) {
        this.metId = metId;
    }

    public String getMetNombre() {
        return metNombre;
    }

    public void setMetNombre(String metNombre) {
        this.metNombre = metNombre;
    }

    public Boolean getMetHabilitado() {
        return metHabilitado;
    }

    public void setMetHabilitado(Boolean metHabilitado) {
        this.metHabilitado = metHabilitado;
    }

    public LocalDateTime getMetUltMod() {
        return metUltMod;
    }

    public void setMetUltMod(LocalDateTime metUltMod) {
        this.metUltMod = metUltMod;
    }

    public String getMetUltUsuario() {
        return metUltUsuario;
    }

    public void setMetUltUsuario(String metUltUsuario) {
        this.metUltUsuario = metUltUsuario;
    }

    public Integer getMeVersion() {
        return meVersion;
    }

    public void setMeVersion(Integer meVersion) {
        this.meVersion = meVersion;
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

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Hash-Equals">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.metId);
        hash = 29 * hash + Objects.hashCode(this.metNombre);
        hash = 29 * hash + Objects.hashCode(this.metHabilitado);
        hash = 29 * hash + Objects.hashCode(this.metUltMod);
        hash = 29 * hash + Objects.hashCode(this.metUltUsuario);
        hash = 29 * hash + Objects.hashCode(this.meVersion);
        hash = 29 * hash + Objects.hashCode(this.first);
        hash = 29 * hash + Objects.hashCode(this.maxResults);
        hash = 29 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 29 * hash + Arrays.hashCode(this.ascending);
        hash = 29 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroMetodoAdq other = (FiltroMetodoAdq) obj;
        if (!Objects.equals(this.metNombre, other.metNombre)) {
            return false;
        }
        if (!Objects.equals(this.metUltUsuario, other.metUltUsuario)) {
            return false;
        }
        if (!Objects.equals(this.metId, other.metId)) {
            return false;
        }
        if (!Objects.equals(this.metHabilitado, other.metHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.metUltMod, other.metUltMod)) {
            return false;
        }
        if (!Objects.equals(this.meVersion, other.meVersion)) {
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
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        return true;
    }

    // </editor-fold>
}
