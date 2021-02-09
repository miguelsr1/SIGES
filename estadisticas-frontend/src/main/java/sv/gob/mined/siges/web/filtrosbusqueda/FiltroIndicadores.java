/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroIndicadores implements Serializable {

    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String definicion;
    private String fuente;
    private Boolean habilitado;
    private Long categoriaPk;
    private Boolean esPublico;
    private Boolean esExterno;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroIndicadores() {
        this.first = 0L;
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

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getDefinicion() {
        return definicion;
    }

    public void setDefinicion(String definicion) {
        this.definicion = definicion;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
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

    public Long getCategoriaPk() {
        return categoriaPk;
    }

    public void setCategoriaPk(Long categoriaPk) {
        this.categoriaPk = categoriaPk;
    }

    public Boolean getEsPublico() {
        return esPublico;
    }

    public void setEsPublico(Boolean esPublico) {
        this.esPublico = esPublico;
    }

    public Boolean getEsExterno() {
        return esExterno;
    }

    public void setEsExterno(Boolean esExterno) {
        this.esExterno = esExterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.codigo);
        hash = 79 * hash + Objects.hashCode(this.codigoExacto);
        hash = 79 * hash + Objects.hashCode(this.nombre);
        hash = 79 * hash + Objects.hashCode(this.definicion);
        hash = 79 * hash + Objects.hashCode(this.fuente);
        hash = 79 * hash + Objects.hashCode(this.habilitado);
        hash = 79 * hash + Objects.hashCode(this.categoriaPk);
        hash = 79 * hash + Objects.hashCode(this.esPublico);
        hash = 79 * hash + Objects.hashCode(this.esExterno);
        hash = 79 * hash + Objects.hashCode(this.first);
        hash = 79 * hash + Objects.hashCode(this.maxResults);
        hash = 79 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 79 * hash + Arrays.hashCode(this.ascending);
        hash = 79 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroIndicadores other = (FiltroIndicadores) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.codigoExacto, other.codigoExacto)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.definicion, other.definicion)) {
            return false;
        }
        if (!Objects.equals(this.fuente, other.fuente)) {
            return false;
        }
        if (!Objects.equals(this.habilitado, other.habilitado)) {
            return false;
        }
        if (!Objects.equals(this.categoriaPk, other.categoriaPk)) {
            return false;
        }
        if (!Objects.equals(this.esPublico, other.esPublico)) {
            return false;
        }
        if (!Objects.equals(this.esExterno, other.esExterno)) {
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
   
    
    

    

    @Override
    public String toString() {
        return "FiltroCodiguera{" + "codigo=" + codigo + ", codigoExacto=" + codigoExacto + ", habilitado=" + habilitado + ", first=" + first + ", maxResults=" + maxResults + ", orderBy=" + orderBy + ", ascending=" + ascending + ", incluirCampos=" + incluirCampos + '}';
    }

}
