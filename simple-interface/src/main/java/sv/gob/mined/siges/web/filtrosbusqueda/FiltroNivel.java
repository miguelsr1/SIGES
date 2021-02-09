/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class FiltroNivel implements Serializable {

    private Long organizacionCurricularPk;
    private Boolean nivHabilitado;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private Boolean inicializarCiclos;
    private String[] incluirCampos;

    public FiltroNivel() {
        inicializarCiclos = Boolean.FALSE;
    }

    public Boolean getInicializarCiclos() {
        return inicializarCiclos;
    }

    public void setInicializarCiclos(Boolean inicializarCiclos) {
        this.inicializarCiclos = inicializarCiclos;
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

    public Long getOrganizacionCurricularPk() {
        return organizacionCurricularPk;
    }

    public void setOrganizacionCurricularPk(Long organizacionCurricularPk) {
        this.organizacionCurricularPk = organizacionCurricularPk;
    }

    public Boolean getNivHabilitado() {
        return nivHabilitado;
    }

    public void setNivHabilitado(Boolean nivHabilitado) {
        this.nivHabilitado = nivHabilitado;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.organizacionCurricularPk);
        hash = 73 * hash + Objects.hashCode(this.nivHabilitado);
        hash = 73 * hash + Objects.hashCode(this.first);
        hash = 73 * hash + Objects.hashCode(this.maxResults);
        hash = 73 * hash + Arrays.deepHashCode(this.orderBy);
        hash = 73 * hash + Arrays.hashCode(this.ascending);
        hash = 73 * hash + Objects.hashCode(this.inicializarCiclos);
        hash = 73 * hash + Arrays.deepHashCode(this.incluirCampos);
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
        final FiltroNivel other = (FiltroNivel) obj;
        if (!Objects.equals(this.organizacionCurricularPk, other.organizacionCurricularPk)) {
            return false;
        }
        if (!Objects.equals(this.nivHabilitado, other.nivHabilitado)) {
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
        if (!Objects.equals(this.inicializarCiclos, other.inicializarCiclos)) {
            return false;
        }
        if (!Arrays.deepEquals(this.incluirCampos, other.incluirCampos)) {
            return false;
        }
        return true;
    }
    
    
    
}
