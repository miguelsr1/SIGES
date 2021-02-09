package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
public class FiltroModuloFormacionDocente implements Serializable {
    
    private Long mfdPk;
    private String codigo;
    private String nombre;
    private Boolean habilitado;
    private Boolean mfdPartePnfd;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    public FiltroModuloFormacionDocente(){
        
    }

    public Long getMfdPk() {
        return mfdPk;
    }

    public void setMfdPk(Long mfdPk) {
        this.mfdPk = mfdPk;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Boolean getMfdPartePnfd() {
        return mfdPartePnfd;
    }

    public void setMfdPartePnfd(Boolean mfdPartePnfd) {
        this.mfdPartePnfd = mfdPartePnfd;
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
