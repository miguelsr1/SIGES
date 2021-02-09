/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;

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
    
    
    
    
    
    
    
}
