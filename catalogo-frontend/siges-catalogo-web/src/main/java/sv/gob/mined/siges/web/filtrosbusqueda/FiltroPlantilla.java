/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class FiltroPlantilla implements Serializable {
    
    private String codigo;
    private String codigoExacto;
    private String nombre;
    private String descripcion;
    private Boolean habilitado;
    private LocalDate fechaHabilitado;
    private LocalDate fechaDeshabilitado;
    
    private Boolean habilitadaReemplazoOrg;
    private Long orgCurricularPk;
    private Boolean soloPlantillasPorDefecto;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroPlantilla() {
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

    public LocalDate getFechaHabilitado() {
        return fechaHabilitado;
    }

    public void setFechaHabilitado(LocalDate fechaHabilitado) {
        this.fechaHabilitado = fechaHabilitado;
    }

    public LocalDate getFechaDeshabilitado() {
        return fechaDeshabilitado;
    }

    public void setFechaDeshabilitado(LocalDate fechaDeshabilitado) {
        this.fechaDeshabilitado = fechaDeshabilitado;
    }

    public Long getOrgCurricularPk() {
        return orgCurricularPk;
    }

    public void setOrgCurricularPk(Long orgCurricularPk) {
        this.orgCurricularPk = orgCurricularPk;
    }

    public Boolean getSoloPlantillasPorDefecto() {
        return soloPlantillasPorDefecto;
    }

    public void setSoloPlantillasPorDefecto(Boolean soloPlantillasPorDefecto) {
        this.soloPlantillasPorDefecto = soloPlantillasPorDefecto;
    }

    public Boolean getHabilitadaReemplazoOrg() {
        return habilitadaReemplazoOrg;
    }

    public void setHabilitadaReemplazoOrg(Boolean habilitadaReemplazoOrg) {
        this.habilitadaReemplazoOrg = habilitadaReemplazoOrg;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.codigo);
        hash = 59 * hash + Objects.hashCode(this.codigoExacto);
        hash = 59 * hash + Objects.hashCode(this.nombre);
        hash = 59 * hash + Objects.hashCode(this.descripcion);
        hash = 59 * hash + Objects.hashCode(this.habilitado);
        hash = 59 * hash + Objects.hashCode(this.fechaHabilitado);
        hash = 59 * hash + Objects.hashCode(this.fechaDeshabilitado);
        hash = 59 * hash + Objects.hashCode(this.habilitadaReemplazoOrg);
        hash = 59 * hash + Objects.hashCode(this.orgCurricularPk);
        hash = 59 * hash + Objects.hashCode(this.soloPlantillasPorDefecto);
        hash = 59 * hash + Objects.hashCode(this.first);
        hash = 59 * hash + Objects.hashCode(this.maxResults);
        hash = 59 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 59 * hash + Arrays.hashCode(this.ascending);
        hash = 59 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroPlantilla other = (FiltroPlantilla) obj;
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
        if (!Objects.equals(this.fechaHabilitado, other.fechaHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.fechaDeshabilitado, other.fechaDeshabilitado)) {
            return false;
        }
        if (!Objects.equals(this.habilitadaReemplazoOrg, other.habilitadaReemplazoOrg)) {
            return false;
        }
        if (!Objects.equals(this.orgCurricularPk, other.orgCurricularPk)) {
            return false;
        }
        if (!Objects.equals(this.soloPlantillasPorDefecto, other.soloPlantillasPorDefecto)) {
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
