/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Objects;
import javax.servlet.FilterConfig;

/**
 * Filtro de los movimientos del presupuesto escolar
 *
 * @author jgiron
 */
public class FiltroMovimientos implements Serializable {

    private FilterConfig filterConfig = null;
    private Long first;
    private Long movPk;
    private Integer movVersion;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private Integer movNumMoviento;
    private String[] incluirCampos;
    private Integer movTechoPresupuestal;
    private Long componentePk;
    private Long sedePk;

    private Integer adiVersion;

    public FiltroMovimientos() {
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

    public Integer getAdiVersion() {
        return adiVersion;
    }

    public void setAdiVersion(Integer adiVersion) {
        this.adiVersion = adiVersion;
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public Long getMovPk() {
        return movPk;
    }

    public void setMovPk(Long movPk) {
        this.movPk = movPk;
    }

    public Integer getMovVersion() {
        return movVersion;
    }

    public void setMovVersion(Integer movVersion) {
        this.movVersion = movVersion;
    }

    public Integer getMovNumMoviento() {
        return movNumMoviento;
    }

    public void setMovNumMoviento(Integer movNumMoviento) {
        this.movNumMoviento = movNumMoviento;
    }

    public Integer getMovTechoPresupuestal() {
        return movTechoPresupuestal;
    }

    public void setMovTechoPresupuestal(Integer movTechoPresupuestal) {
        this.movTechoPresupuestal = movTechoPresupuestal;
    }

    public Long getComponentePk() {
        return componentePk;
    }

    public void setComponentePk(Long componentePk) {
        this.componentePk = componentePk;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.filterConfig);
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
        final FiltroMovimientos other = (FiltroMovimientos) obj;
        return true;
    }

}
