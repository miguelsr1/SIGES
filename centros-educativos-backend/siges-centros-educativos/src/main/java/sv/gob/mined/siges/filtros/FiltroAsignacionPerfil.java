/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroAsignacionPerfil implements Serializable {
    
    private Long apeSedeFk;
    private Long apeDepartamentoFk;
    private Boolean incluirAsignacionPerfilPersonal;
    private Boolean agregarSede;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    public FiltroAsignacionPerfil() {
    }

    public Long getApeSedeFk() {
        return apeSedeFk;
    }

    public void setApeSedeFk(Long apeSedeFk) {
        this.apeSedeFk = apeSedeFk;
    }

    public Long getApeDepartamentoFk() {
        return apeDepartamentoFk;
    }

    public void setApeDepartamentoFk(Long apeDepartamentoFk) {
        this.apeDepartamentoFk = apeDepartamentoFk;
    }

    public Boolean getIncluirAsignacionPerfilPersonal() {
        return incluirAsignacionPerfilPersonal;
    }

    public void setIncluirAsignacionPerfilPersonal(Boolean incluirAsignacionPerfilPersonal) {
        this.incluirAsignacionPerfilPersonal = incluirAsignacionPerfilPersonal;
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

    public Boolean getAgregarSede() {
        return agregarSede;
    }

    public void setAgregarSede(Boolean agregarSede) {
        this.agregarSede = agregarSede;
    }
       
}
