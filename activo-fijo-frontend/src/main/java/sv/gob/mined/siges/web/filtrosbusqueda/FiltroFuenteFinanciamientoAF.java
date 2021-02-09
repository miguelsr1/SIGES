/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroFuenteFinanciamientoAF implements Serializable{
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String descripcion;
    private Boolean habilitado;
    private Boolean esMined;
    private Boolean esDepartamental;
    private Boolean esCentroEducativo;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroFuenteFinanciamientoAF() {
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

    public Boolean getEsMined() {
        return esMined;
    }

    public void setEsMined(Boolean esMined) {
        this.esMined = esMined;
    }

    public Boolean getEsDepartamental() {
        return esDepartamental;
    }

    public void setEsDepartamental(Boolean esDepartamental) {
        this.esDepartamental = esDepartamental;
    }

    public Boolean getEsCentroEducativo() {
        return esCentroEducativo;
    }

    public void setEsCentroEducativo(Boolean esCentroEducativo) {
        this.esCentroEducativo = esCentroEducativo;
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
        hash = 97 * hash + Objects.hashCode(this.codigo);
        hash = 97 * hash + Objects.hashCode(this.codigoExacto);
        hash = 97 * hash + Objects.hashCode(this.nombre);
        hash = 97 * hash + Objects.hashCode(this.descripcion);
        hash = 97 * hash + Objects.hashCode(this.habilitado);
        hash = 97 * hash + Objects.hashCode(this.esMined);
        hash = 97 * hash + Objects.hashCode(this.esDepartamental);
        hash = 97 * hash + Objects.hashCode(this.esCentroEducativo);
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.maxResults);
        hash = 97 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 97 * hash + Arrays.hashCode(this.ascending);
        hash = 97 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroFuenteFinanciamientoAF other = (FiltroFuenteFinanciamientoAF) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        return true;
    }
    

}
