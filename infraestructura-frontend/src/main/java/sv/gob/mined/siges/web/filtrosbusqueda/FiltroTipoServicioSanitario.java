/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroTipoServicioSanitario implements Serializable {
    
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String descripcion;
    private Boolean habilitado;
    
    private String auxiliar;
    
    private Boolean tssAplicaEstudiante;    

    private Boolean tssAplicaInmueble;

    private Boolean tssAplicaEdificio;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroTipoServicioSanitario() {
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

    public String getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(String auxiliar) {
        this.auxiliar = auxiliar;
    }

    public Boolean getTssAplicaEstudiante() {
        return tssAplicaEstudiante;
    }

    public void setTssAplicaEstudiante(Boolean tssAplicaEstudiante) {
        this.tssAplicaEstudiante = tssAplicaEstudiante;
    }

    public Boolean getTssAplicaInmueble() {
        return tssAplicaInmueble;
    }

    public void setTssAplicaInmueble(Boolean tssAplicaInmueble) {
        this.tssAplicaInmueble = tssAplicaInmueble;
    }

    public Boolean getTssAplicaEdificio() {
        return tssAplicaEdificio;
    }

    public void setTssAplicaEdificio(Boolean tssAplicaEdificio) {
        this.tssAplicaEdificio = tssAplicaEdificio;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.codigo);
        hash = 53 * hash + Objects.hashCode(this.codigoExacto);
        hash = 53 * hash + Objects.hashCode(this.nombre);
        hash = 53 * hash + Objects.hashCode(this.descripcion);
        hash = 53 * hash + Objects.hashCode(this.habilitado);
        hash = 53 * hash + Objects.hashCode(this.auxiliar);
        hash = 53 * hash + Objects.hashCode(this.tssAplicaEstudiante);
        hash = 53 * hash + Objects.hashCode(this.tssAplicaInmueble);
        hash = 53 * hash + Objects.hashCode(this.tssAplicaEdificio);
        hash = 53 * hash + Objects.hashCode(this.first);
        hash = 53 * hash + Objects.hashCode(this.maxResults);
        hash = 53 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 53 * hash + Arrays.hashCode(this.ascending);
        hash = 53 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroTipoServicioSanitario other = (FiltroTipoServicioSanitario) obj;
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
        if (!Objects.equals(this.auxiliar, other.auxiliar)) {
            return false;
        }
        if (!Objects.equals(this.habilitado, other.habilitado)) {
            return false;
        }
        if (!Objects.equals(this.tssAplicaEstudiante, other.tssAplicaEstudiante)) {
            return false;
        }
        if (!Objects.equals(this.tssAplicaInmueble, other.tssAplicaInmueble)) {
            return false;
        }
        if (!Objects.equals(this.tssAplicaEdificio, other.tssAplicaEdificio)) {
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
