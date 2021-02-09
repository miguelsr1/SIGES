package sv.gob.mined.siges.web.filtrosbusqueda;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */


import java.io.Serializable;

public class FiltroDocenteDocumento implements Serializable {
    
    private Long ddoPersonal;    
    private Long ddoTipoDocumento;
    private Long ddoArchivo;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroDocenteDocumento() {
    }

    public Long getDdoPersonal() {
        return ddoPersonal;
    }

    public void setDdoPersonal(Long ddoPersonal) {
        this.ddoPersonal = ddoPersonal;
    }

    public Long getDdoTipoDocumento() {
        return ddoTipoDocumento;
    }

    public void setDdoTipoDocumento(Long ddoTipoDocumento) {
        this.ddoTipoDocumento = ddoTipoDocumento;
    }

    public Long getDdoArchivo() {
        return ddoArchivo;
    }

    public void setDdoArchivo(Long ddoArchivo) {
        this.ddoArchivo = ddoArchivo;
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
