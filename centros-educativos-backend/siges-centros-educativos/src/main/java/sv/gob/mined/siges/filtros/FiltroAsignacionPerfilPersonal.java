/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroAsignacionPerfilPersonal implements Serializable {
    
    private Long apeSedeFk;
    private Long apeDepartamentoFk;
    private Long apePersonalFk;
    private Long appAsignacionPerfilFk;
    private Long appPk;
    private Long appPersonaFk;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    public FiltroAsignacionPerfilPersonal() {
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

    public Long getApePersonalFk() {
        return apePersonalFk;
    }

    public void setApePersonalFk(Long apePersonalFk) {
        this.apePersonalFk = apePersonalFk;
    }  

    public Long getAppAsignacionPerfilFk() {
        return appAsignacionPerfilFk;
    }

    public void setAppAsignacionPerfilFk(Long appAsignacionPerfilFk) {
        this.appAsignacionPerfilFk = appAsignacionPerfilFk;
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

    public Long getAppPk() {
        return appPk;
    }

    public void setAppPk(Long appPk) {
        this.appPk = appPk;
    }

    public Long getAppPersonaFk() {
        return appPersonaFk;
    }

    public void setAppPersonaFk(Long appPersonaFk) {
        this.appPersonaFk = appPersonaFk;
    }
       
}
