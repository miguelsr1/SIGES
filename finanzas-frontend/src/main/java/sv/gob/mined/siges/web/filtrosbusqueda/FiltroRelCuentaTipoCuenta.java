/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

/**
 * Filtro de las cuentas bancarias
 *
 * @author Sofis Solutions
 */
public class FiltroRelCuentaTipoCuenta implements Serializable {

    
    private Long first;
    private Long cuentaBancPk;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    

    public FiltroRelCuentaTipoCuenta() {

        super();
        this.first = 0L;
    }


    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getCuentaBancPk() {
        return cuentaBancPk;
    }

    public void setCuentaBancPk(Long cuentaBancPk) {
        this.cuentaBancPk = cuentaBancPk;
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
