/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;


public class FiltroSubModalidad implements Serializable {
    

    private String smoCodigo;
    private String smoCodigoExacto;

    private String smoNombre;
    private String smoNombreExacto;

    private Boolean smoHabilitado;

    private String  smoModalidad;
    private String  smoModalidadExacto;
    
    private Long  smoModalidadPk;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroSubModalidad() {
    }

    public String getSmoCodigo() {
        return smoCodigo;
    }

    public void setSmoCodigo(String smoCodigo) {
        this.smoCodigo = smoCodigo;
    }

    public String getSmoCodigoExacto() {
        return smoCodigoExacto;
    }

    public void setSmoCodigoExacto(String smoCodigoExacto) {
        this.smoCodigoExacto = smoCodigoExacto;
    }

    public String getSmoNombre() {
        return smoNombre;
    }

    public void setSmoNombre(String smoNombre) {
        this.smoNombre = smoNombre;
    }

    public String getSmoNombreExacto() {
        return smoNombreExacto;
    }

    public void setSmoNombreExacto(String smoNombreExacto) {
        this.smoNombreExacto = smoNombreExacto;
    }

    public Boolean getSmoHabilitado() {
        return smoHabilitado;
    }

    public void setSmoHabilitado(Boolean smoHabilitado) {
        this.smoHabilitado = smoHabilitado;
    }

    public String getSmoModalidad() {
        return smoModalidad;
    }

    public void setSmoModalidad(String smoModalidad) {
        this.smoModalidad = smoModalidad;
    }

    public String getSmoModalidadExacto() {
        return smoModalidadExacto;
    }

    public void setSmoModalidadExacto(String smoModalidadExacto) {
        this.smoModalidadExacto = smoModalidadExacto;
    }

    public Long getSmoModalidadPk() {
        return smoModalidadPk;
    }

    public void setSmoModalidadPk(Long smoModalidadPk) {
        this.smoModalidadPk = smoModalidadPk;
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
    
    

    
}
