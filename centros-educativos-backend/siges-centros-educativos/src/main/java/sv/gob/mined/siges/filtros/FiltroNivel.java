/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroNivel implements Serializable {

    private Long sedPk;
    private Long organizacionCurricularPk;
    private Boolean nivHabilitado;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private Boolean inicializarCiclos;
    
    private String[] incluirCampos;
    
    //Auxiliar
    private Boolean seNecesitaDistinct;

    public FiltroNivel() {
        seNecesitaDistinct = Boolean.FALSE;
        inicializarCiclos = Boolean.FALSE;
    }

    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
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

    public Boolean getInicializarCiclos() {
        return inicializarCiclos;
    }

    public void setInicializarCiclos(Boolean inicializarCiclos) {
        this.inicializarCiclos = inicializarCiclos;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Boolean getSeNecesitaDistinct() {
        return seNecesitaDistinct;
    }

    public void setSeNecesitaDistinct(Boolean seNecesitaDistinct) {
        this.seNecesitaDistinct = seNecesitaDistinct;
    }
    
    
    
}
