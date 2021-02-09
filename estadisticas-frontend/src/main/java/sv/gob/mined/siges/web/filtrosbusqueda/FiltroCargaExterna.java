/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.EnumDesagregacion;

public class FiltroCargaExterna implements Serializable {
        
    private Long indicadorPk;
    private Long nombrePk;
    private Integer anio;
    private EnumDesagregacion desagregacion;
    private Boolean sinDesagregacion;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroCargaExterna() {
        this.first = 0L;
    }

    public Long getIndicadorPk() {
        return indicadorPk;
    }

    public void setIndicadorPk(Long indicadorPk) {
        this.indicadorPk = indicadorPk;
    }
    
    public Long getNombrePk() {
        return nombrePk;
    }

    public void setNombrePk(Long nombrePk) {
        this.nombrePk = nombrePk;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
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

    public EnumDesagregacion getDesagregacion() {
        return desagregacion;
    }

    public void setDesagregacion(EnumDesagregacion desagregacion) {
        this.desagregacion = desagregacion;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Boolean getSinDesagregacion() {
        return sinDesagregacion;
    }

    public void setSinDesagregacion(Boolean sinDesagregacion) {
        this.sinDesagregacion = sinDesagregacion;
    }  
}
