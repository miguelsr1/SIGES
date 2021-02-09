/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda.catalogo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroMotivoRetiro implements Serializable {

    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String descripcion;
    private Boolean habilitado;

    private Boolean definitivo;
    private Boolean traslado;
    private Boolean cambioSeccion;
    private String excluirCodigo;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroMotivoRetiro() {
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

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getDefinitivo() {
        return definitivo;
    }

    public void setDefinitivo(Boolean definitivo) {
        this.definitivo = definitivo;
    }

    public Boolean getTraslado() {
        return traslado;
    }

    public void setTraslado(Boolean traslado) {
        this.traslado = traslado;
    }

    public Boolean getCambioSeccion() {
        return cambioSeccion;
    }

    public void setCambioSeccion(Boolean cambioSeccion) {
        this.cambioSeccion = cambioSeccion;
    }

    public String getExcluirCodigo() {
        return excluirCodigo;
    }

    public void setExcluirCodigo(String excluirCodigo) {
        this.excluirCodigo = excluirCodigo;
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

    // Equals y hashcode utilizados por jcache
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.codigo);
        hash = 29 * hash + Objects.hashCode(this.codigoExacto);
        hash = 29 * hash + Objects.hashCode(this.nombre);
        hash = 29 * hash + Objects.hashCode(this.descripcion);
        hash = 29 * hash + Objects.hashCode(this.habilitado);
        hash = 29 * hash + Objects.hashCode(this.definitivo);
        hash = 29 * hash + Objects.hashCode(this.traslado);
        hash = 29 * hash + Objects.hashCode(this.cambioSeccion);
        hash = 29 * hash + Objects.hashCode(this.excluirCodigo);
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
        final FiltroMotivoRetiro other = (FiltroMotivoRetiro) obj;
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
        if (!Objects.equals(this.excluirCodigo, other.excluirCodigo)) {
            return false;
        }
        if (!Objects.equals(this.habilitado, other.habilitado)) {
            return false;
        }
        if (!Objects.equals(this.definitivo, other.definitivo)) {
            return false;
        }
        if (!Objects.equals(this.traslado, other.traslado)) {
            return false;
        }
        if (!Objects.equals(this.cambioSeccion, other.cambioSeccion)) {
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
