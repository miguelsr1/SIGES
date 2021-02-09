/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtros;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroCanton implements Serializable {

    private String canCodigo;
    private String canCodigoExacto;

    private String canNombre;
    private String canNombreExacto;

    private Boolean canHabilitado;

    private String canMunicipioNombre;
    private String canMunicipioNombreExacto;

    private Long canMunicipioFk;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroCanton() {
    }

    public String getCanCodigo() {
        return canCodigo;
    }

    public void setCanCodigo(String canCodigo) {
        this.canCodigo = canCodigo;
    }

    public String getCanCodigoExacto() {
        return canCodigoExacto;
    }

    public void setCanCodigoExacto(String canCodigoExacto) {
        this.canCodigoExacto = canCodigoExacto;
    }

    public String getCanNombre() {
        return canNombre;
    }

    public void setCanNombre(String canNombre) {
        this.canNombre = canNombre;
    }

    public String getCanNombreExacto() {
        return canNombreExacto;
    }

    public void setCanNombreExacto(String canNombreExacto) {
        this.canNombreExacto = canNombreExacto;
    }

    public Boolean getCanHabilitado() {
        return canHabilitado;
    }

    public void setCanHabilitado(Boolean canHabilitado) {
        this.canHabilitado = canHabilitado;
    }

    public String getCanMunicipioNombre() {
        return canMunicipioNombre;
    }

    public void setCanMunicipioNombre(String canMunicipioNombre) {
        this.canMunicipioNombre = canMunicipioNombre;
    }

    public String getCanMunicipioNombreExacto() {
        return canMunicipioNombreExacto;
    }

    public void setCanMunicipioNombreExacto(String canMunicipioNombreExacto) {
        this.canMunicipioNombreExacto = canMunicipioNombreExacto;
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

    public Long getCanMunicipioFk() {
        return canMunicipioFk;
    }

    public void setCanMunicipioFk(Long canMunicipioFk) {
        this.canMunicipioFk = canMunicipioFk;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }
    
    // Equals y hashcode utilizados por jcache

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.canCodigo);
        hash = 71 * hash + Objects.hashCode(this.canCodigoExacto);
        hash = 71 * hash + Objects.hashCode(this.canNombre);
        hash = 71 * hash + Objects.hashCode(this.canNombreExacto);
        hash = 71 * hash + Objects.hashCode(this.canHabilitado);
        hash = 71 * hash + Objects.hashCode(this.canMunicipioNombre);
        hash = 71 * hash + Objects.hashCode(this.canMunicipioNombreExacto);
        hash = 71 * hash + Objects.hashCode(this.canMunicipioFk);
        hash = 71 * hash + Objects.hashCode(this.first);
        hash = 71 * hash + Objects.hashCode(this.maxResults);
        hash = 71 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 71 * hash + Arrays.hashCode(this.ascending);
        hash = 71 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroCanton other = (FiltroCanton) obj;
        if (!Objects.equals(this.canCodigo, other.canCodigo)) {
            return false;
        }
        if (!Objects.equals(this.canCodigoExacto, other.canCodigoExacto)) {
            return false;
        }
        if (!Objects.equals(this.canNombre, other.canNombre)) {
            return false;
        }
        if (!Objects.equals(this.canNombreExacto, other.canNombreExacto)) {
            return false;
        }
        if (!Objects.equals(this.canMunicipioNombre, other.canMunicipioNombre)) {
            return false;
        }
        if (!Objects.equals(this.canMunicipioNombreExacto, other.canMunicipioNombreExacto)) {
            return false;
        }
        if (!Objects.equals(this.canHabilitado, other.canHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.canMunicipioFk, other.canMunicipioFk)) {
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
    
    

}
