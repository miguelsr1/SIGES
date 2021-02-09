/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda.catalogo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroAsociacion implements Serializable {
    
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String descripcion;
    private Boolean habilitado;
    
    private Long tipoAsociacion;
    private Boolean extranjera;
    private Boolean fondosMined;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroAsociacion() {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Long getTipoAsociacion() {
        return tipoAsociacion;
    }

    public void setTipoAsociacion(Long tipoAsociacion) {
        this.tipoAsociacion = tipoAsociacion;
    }

    public Boolean getExtranjera() {
        return extranjera;
    }

    public void setExtranjera(Boolean extranjera) {
        this.extranjera = extranjera;
    }

    public Boolean getFondosMined() {
        return fondosMined;
    }

    public void setFondosMined(Boolean fondosMined) {
        this.fondosMined = fondosMined;
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
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.codigo);
        hash = 13 * hash + Objects.hashCode(this.codigoExacto);
        hash = 13 * hash + Objects.hashCode(this.nombre);
        hash = 13 * hash + Objects.hashCode(this.descripcion);
        hash = 13 * hash + Objects.hashCode(this.habilitado);
        hash = 13 * hash + Objects.hashCode(this.tipoAsociacion);
        hash = 13 * hash + Objects.hashCode(this.extranjera);
        hash = 13 * hash + Objects.hashCode(this.fondosMined);
        hash = 13 * hash + Objects.hashCode(this.first);
        hash = 13 * hash + Objects.hashCode(this.maxResults);
        hash = 13 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 13 * hash + Arrays.hashCode(this.ascending);
        hash = 13 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroAsociacion other = (FiltroAsociacion) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.codigoExacto, other.codigoExacto)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.habilitado, other.habilitado)) {
            return false;
        }
        if (!Objects.equals(this.tipoAsociacion, other.tipoAsociacion)) {
            return false;
        }
        if (!Objects.equals(this.extranjera, other.extranjera)) {
            return false;
        }
        if (!Objects.equals(this.fondosMined, other.fondosMined)) {
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
