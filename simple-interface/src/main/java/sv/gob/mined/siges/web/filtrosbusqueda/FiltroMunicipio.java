/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroMunicipio implements Serializable {
    

    private String munCodigo;
    private String munCodigoExacto;

    private String munNombre;
    private String munNombreExacto;
    private String  munDepartamento;
    private String  munDepartamentoExacto;
    private Boolean munHabilitado;
    private Long munDepartamentoFk;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroMunicipio() {
    }

    public String getMunCodigo() {
        return munCodigo;
    }

    public void setMunCodigo(String munCodigo) {
        this.munCodigo = munCodigo;
    }

    public String getMunNombre() {
        return munNombre;
    }

    public void setMunNombre(String munNombre) {
        this.munNombre = munNombre;
    }

    public String getMunDepartamento() {
        return munDepartamento;
    }

    public void setMunDepartamento(String munDepartamento) {
        this.munDepartamento = munDepartamento;
    }

    public String getMunDepartamentoExacto() {
        return munDepartamentoExacto;
    }

    public void setMunDepartamentoExacto(String munDepartamentoExacto) {
        this.munDepartamentoExacto = munDepartamentoExacto;
    }

    public Long getMunDepartamentoFk() {
        return munDepartamentoFk;
    }

    public void setMunDepartamentoFk(Long munDepartamentoFk) {
        this.munDepartamentoFk = munDepartamentoFk;
    }

    public Boolean getMunHabilitado() {
        return munHabilitado;
    }

    public void setMunHabilitado(Boolean munHabilitado) {
        this.munHabilitado = munHabilitado;
    }

    public String getMunCodigoExacto() {
        return munCodigoExacto;
    }

    public void setMunCodigoExacto(String munCodigoExacto) {
        this.munCodigoExacto = munCodigoExacto;
    }

    public String getMunNombreExacto() {
        return munNombreExacto;
    }

    public void setMunNombreExacto(String munNombreExacto) {
        this.munNombreExacto = munNombreExacto;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.munCodigo);
        hash = 71 * hash + Objects.hashCode(this.munCodigoExacto);
        hash = 71 * hash + Objects.hashCode(this.munNombre);
        hash = 71 * hash + Objects.hashCode(this.munNombreExacto);
        hash = 71 * hash + Objects.hashCode(this.munDepartamento);
        hash = 71 * hash + Objects.hashCode(this.munDepartamentoExacto);
        hash = 71 * hash + Objects.hashCode(this.munHabilitado);
        hash = 71 * hash + Objects.hashCode(this.munDepartamentoFk);
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
        final FiltroMunicipio other = (FiltroMunicipio) obj;
        if (!Objects.equals(this.munCodigo, other.munCodigo)) {
            return false;
        }
        if (!Objects.equals(this.munCodigoExacto, other.munCodigoExacto)) {
            return false;
        }
        if (!Objects.equals(this.munNombre, other.munNombre)) {
            return false;
        }
        if (!Objects.equals(this.munNombreExacto, other.munNombreExacto)) {
            return false;
        }
        if (!Objects.equals(this.munDepartamento, other.munDepartamento)) {
            return false;
        }
        if (!Objects.equals(this.munDepartamentoExacto, other.munDepartamentoExacto)) {
            return false;
        }
        if (!Objects.equals(this.munHabilitado, other.munHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.munDepartamentoFk, other.munDepartamentoFk)) {
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
