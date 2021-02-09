package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroMedidasExamenFisico implements Serializable {

    private Long fichaEstudianteFk;
    
    private String[] incluirCampos;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    
    public FiltroMedidasExamenFisico(){
     
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

    public Long getFichaEstudianteFk() {
        return fichaEstudianteFk;
    }

    public void setFichaEstudianteFk(Long fichaEstudianteFk) {
        this.fichaEstudianteFk = fichaEstudianteFk;
    }
    
    
}
