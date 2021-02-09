package sv.gob.mined.siges.filtros;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */


import java.io.Serializable;

public class FiltroDireccion implements Serializable {
    
    private Long dirPk;    
    private String dirDireccion;
    private Long dirDepartamento;
    private Long dirMunicipio;
    private Long dirCanton;
    private Long dirCaserio;
    private Long dirZona;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroDireccion() {
    }

    public Long getDirPk() {
        return dirPk;
    }

    public void setDirPk(Long dirPk) {
        this.dirPk = dirPk;
    }

    public String getDirDireccion() {
        return dirDireccion;
    }

    public void setDirDireccion(String dirDireccion) {
        this.dirDireccion = dirDireccion;
    }

    public Long getDirDepartamento() {
        return dirDepartamento;
    }

    public void setDirDepartamento(Long dirDepartamento) {
        this.dirDepartamento = dirDepartamento;
    }

    public Long getDirMunicipio() {
        return dirMunicipio;
    }

    public void setDirMunicipio(Long dirMunicipio) {
        this.dirMunicipio = dirMunicipio;
    }

    public Long getDirCanton() {
        return dirCanton;
    }

    public void setDirCanton(Long dirCanton) {
        this.dirCanton = dirCanton;
    }

    public Long getDirCaserio() {
        return dirCaserio;
    }

    public void setDirCaserio(Long dirCaserio) {
        this.dirCaserio = dirCaserio;
    }

    public Long getDirZona() {
        return dirZona;
    }

    public void setDirZona(Long dirZona) {
        this.dirZona = dirZona;
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
    
    

    
}
