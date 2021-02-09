/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

/**
 * Filtro del plan escolar anual
 *
 * @author jgiron
 */
public class FiltroPlanEscolarAnual implements Serializable {

    private Long peaSede;
    private Long peaAnioLectivo;

    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroPlanEscolarAnual() {
    }

    public Long getPeaSede() {
        return peaSede;
    }

    public void setPeaSede(Long peaSede) {
        this.peaSede = peaSede;
    }

    public Long getPeaAnioLectivo() {
        return peaAnioLectivo;
    }

    public void setPeaAnioLectivo(Long peaAnioLectivo) {
        this.peaAnioLectivo = peaAnioLectivo;
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

}
