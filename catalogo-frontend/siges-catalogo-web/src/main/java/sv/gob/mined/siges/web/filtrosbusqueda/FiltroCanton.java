/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroCanton implements Serializable {
    

    private String canCodigo;
    private String canCodigoExacto;

    private String canNombre;
    private String canNombreExacto;

    private Boolean canHabilitado;
    
    private String canMunicipioNombre;
    private String canMunicipioNombreExacto;

    private Long canDepartamentoPk;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroCanton() {
    }

    public String getCanCodigo() {
        return canCodigo;
    }

    public void setCanCodigo(String canCodigo) {
        this.canCodigo = canCodigo;
    }

    public String getCanCodigoExacto() {
        return canCodigoExacto;
    }

    public void setCanCodigoExacto(String canCodigoExacto) {
        this.canCodigoExacto = canCodigoExacto;
    }

    public String getCanNombre() {
        return canNombre;
    }

    public void setCanNombre(String canNombre) {
        this.canNombre = canNombre;
    }

    public String getCanNombreExacto() {
        return canNombreExacto;
    }

    public void setCanNombreExacto(String canNombreExacto) {
        this.canNombreExacto = canNombreExacto;
    }

    public Boolean getCanHabilitado() {
        return canHabilitado;
    }

    public void setCanHabilitado(Boolean canHabilitado) {
        this.canHabilitado = canHabilitado;
    }

    public String getCanMunicipioNombre() {
        return canMunicipioNombre;
    }

    public void setCanMunicipioNombre(String canMunicipioNombre) {
        this.canMunicipioNombre = canMunicipioNombre;
    }

    public String getCanMunicipioNombreExacto() {
        return canMunicipioNombreExacto;
    }

    public void setCanMunicipioNombreExacto(String canMunicipioNombreExacto) {
        this.canMunicipioNombreExacto = canMunicipioNombreExacto;
    }

    public Long getCanDepartamentoPk() {
        return canDepartamentoPk;
    }

    public void setCanDepartamentoPk(Long canDepartamentoPk) {
        this.canDepartamentoPk = canDepartamentoPk;
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
