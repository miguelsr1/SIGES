/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda.centros_educativos;

import java.io.Serializable;

public class FiltroModalidad implements Serializable {

    private Long cicPk;
    private Long orgCurricularPk;
    private Boolean modHabilitado;

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
    
}
