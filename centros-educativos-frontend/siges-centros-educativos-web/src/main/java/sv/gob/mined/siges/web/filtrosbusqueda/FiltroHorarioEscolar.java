/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

/**
 *
 * @author Sofis Solutions
 */
public class FiltroHorarioEscolar implements Serializable {

    private Long hesCentroEducativo;
    private Long hesOrganizacionCurricular;
    private Long hesNivel;
    private Long hesGrado;
    private Long hesPlanEstudio;
    private Long hesSeccion;
    private Boolean hesLevantarListaDocentes;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

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

    public Long getHesCentroEducativo() {
        return hesCentroEducativo;
    }

    public void setHesCentroEducativo(Long hesCentroEducativo) {
        this.hesCentroEducativo = hesCentroEducativo;
    }

    public Long getHesOrganizacionCurricular() {
        return hesOrganizacionCurricular;
    }

    public void setHesOrganizacionCurricular(Long hesOrganizacionCurricular) {
        this.hesOrganizacionCurricular = hesOrganizacionCurricular;
    }

    public Long getHesNivel() {
        return hesNivel;
    }

    public void setHesNivel(Long hesNivel) {
        this.hesNivel = hesNivel;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }

    public Long getHesGrado() {
        return hesGrado;
    }

    public void setHesGrado(Long hesGrado) {
        this.hesGrado = hesGrado;
    }

    public Long getHesPlanEstudio() {
        return hesPlanEstudio;
    }

    public void setHesPlanEstudio(Long hesPlanEstudio) {
        this.hesPlanEstudio = hesPlanEstudio;
    }

    public Long getHesSeccion() {
        return hesSeccion;
    }

    public void setHesSeccion(Long hesSeccion) {
        this.hesSeccion = hesSeccion;
    }

    public Boolean getHesLevantarListaDocentes() {
        return hesLevantarListaDocentes;
    }

    public void setHesLevantarListaDocentes(Boolean hesLevantarListaDocentes) {
        this.hesLevantarListaDocentes = hesLevantarListaDocentes;
    }

}
