/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroTelefono implements Serializable {
    
    private Long    telPk;
    private Long  telPersona;
    private String  telTelefono;
    private Long  telTipoTelefono;
    
    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroTelefono() {
    }

    public Long getTelPk() {
        return telPk;
    }

    public void setTelPk(Long telPk) {
        this.telPk = telPk;
    }

    public Long getTelPersona() {
        return telPersona;
    }

    public void setTelPersona(Long telPersona) {
        this.telPersona = telPersona;
    }

    public String getTelTelefono() {
        return telTelefono;
    }

    public void setTelTelefono(String telTelefono) {
        this.telTelefono = telTelefono;
    }

    public Long getTelTipoTelefono() {
        return telTipoTelefono;
    }

    public void setTelTipoTelefono(Long telTipoTelefono) {
        this.telTipoTelefono = telTipoTelefono;
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
