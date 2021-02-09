/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroTipoBienes implements Serializable {
    
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String codigoONombre;
    private Boolean habilitado;
    private Boolean esTipoVehiculo;
    private Long clasificacionId;
    private Long categoriaId;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
   
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Boolean getEsTipoVehiculo() {
        return esTipoVehiculo;
    }

    public void setEsTipoVehiculo(Boolean esTipoVehiculo) {
        this.esTipoVehiculo = esTipoVehiculo;
    }

    public Long getClasificacionId() {
        return clasificacionId;
    }

    public void setClasificacionId(Long clasificacionId) {
        this.clasificacionId = clasificacionId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
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

    public String getCodigoONombre() {
        return codigoONombre;
    }

    public void setCodigoONombre(String codigoONombre) {
        this.codigoONombre = codigoONombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoExacto() {
        return codigoExacto;
    }

    public void setCodigoExacto(String codigoExacto) {
        this.codigoExacto = codigoExacto;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.codigo);
        hash = 41 * hash + Objects.hashCode(this.codigoExacto);
        hash = 41 * hash + Objects.hashCode(this.nombre);
        hash = 41 * hash + Objects.hashCode(this.codigoONombre);
        hash = 41 * hash + Objects.hashCode(this.habilitado);
        hash = 41 * hash + Objects.hashCode(this.esTipoVehiculo);
        hash = 41 * hash + Objects.hashCode(this.clasificacionId);
        hash = 41 * hash + Objects.hashCode(this.categoriaId);
        hash = 41 * hash + Objects.hashCode(this.first);
        hash = 41 * hash + Objects.hashCode(this.maxResults);
        hash = 41 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 41 * hash + Arrays.hashCode(this.ascending);
        hash = 41 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroTipoBienes other = (FiltroTipoBienes) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.codigoExacto, other.codigoExacto)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.codigoONombre, other.codigoONombre)) {
            return false;
        }
        if (!Objects.equals(this.habilitado, other.habilitado)) {
            return false;
        }
        if (!Objects.equals(this.esTipoVehiculo, other.esTipoVehiculo)) {
            return false;
        }
        if (!Objects.equals(this.clasificacionId, other.clasificacionId)) {
            return false;
        }
        if (!Objects.equals(this.categoriaId, other.categoriaId)) {
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
