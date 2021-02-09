/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroModalidad implements Serializable {

    private Long cicPk;
    private Long orgCurricularPk;
    private Boolean modHabilitado;
    private Long sedePk;

    private Long nivel;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;
    private Boolean inicializarRel;

    public FiltroModalidad() {
        inicializarRel = Boolean.FALSE;
    }

    public Long getCicPk() {
        return cicPk;
    }

    public void setCicPk(Long cicPk) {
        this.cicPk = cicPk;
    }

    public Long getOrgCurricularPk() {
        return orgCurricularPk;
    }

    public void setOrgCurricularPk(Long orgCurricularPk) {
        this.orgCurricularPk = orgCurricularPk;
    }

    public Boolean getModHabilitado() {
        return modHabilitado;
    }

    public void setModHabilitado(Boolean modHabilitado) {
        this.modHabilitado = modHabilitado;
    }

    public Long getNivel() {
        return nivel;
    }

    public void setNivel(Long nivel) {
        this.nivel = nivel;
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

    public Boolean getInicializarRel() {
        return inicializarRel;
    }

    public void setInicializarRel(Boolean inicializarRel) {
        this.inicializarRel = inicializarRel;
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
        hash = 97 * hash + Objects.hashCode(this.cicPk);
        hash = 97 * hash + Objects.hashCode(this.orgCurricularPk);
        hash = 97 * hash + Objects.hashCode(this.modHabilitado);
        hash = 97 * hash + Objects.hashCode(this.nivel);
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.maxResults);
        hash = 97 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 97 * hash + Arrays.hashCode(this.ascending);
        hash = 97 * hash + Arrays.deepHashCode(this.incluirCampos);
        hash = 97 * hash + Objects.hashCode(this.inicializarRel);
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
        final FiltroModalidad other = (FiltroModalidad) obj;
        if (!Objects.equals(this.cicPk, other.cicPk)) {
            return false;
        }
        if (!Objects.equals(this.orgCurricularPk, other.orgCurricularPk)) {
            return false;
        }
        if (!Objects.equals(this.modHabilitado, other.modHabilitado)) {
            return false;
        }
        if (!Objects.equals(this.nivel, other.nivel)) {
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
        if (!Objects.equals(this.inicializarRel, other.inicializarRel)) {
            return false;
        }
        return true;
    }
    
    
}
