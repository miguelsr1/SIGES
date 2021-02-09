/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

public class FiltroMunicipio implements Serializable {
    

    private String munCodigo;
    private String munCodigoExacto;

    private String munNombre;
    private String munNombreExacto;
    
    private String  munDepartamento;
    private String  munDepartamentoExacto;

    private Boolean munHabilitado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroMunicipio() {
    }

    public String getMunCodigo() {
        return munCodigo;
    }

    public void setMunCodigo(String munCodigo) {
        this.munCodigo = munCodigo;
    }

    public String getMunNombre() {
        return munNombre;
    }

    public void setMunNombre(String munNombre) {
        this.munNombre = munNombre;
    }

    public String getMunDepartamento() {
        return munDepartamento;
    }

    public void setMunDepartamento(String munDepartamento) {
        this.munDepartamento = munDepartamento;
    }

    public String getMunDepartamentoExacto() {
        return munDepartamentoExacto;
    }

    public void setMunDepartamentoExacto(String munDepartamentoExacto) {
        this.munDepartamentoExacto = munDepartamentoExacto;
    }


    public Boolean getMunHabilitado() {
        return munHabilitado;
    }

    public void setMunHabilitado(Boolean munHabilitado) {
        this.munHabilitado = munHabilitado;
    }

    public String getMunCodigoExacto() {
        return munCodigoExacto;
    }

    public void setMunCodigoExacto(String munCodigoExacto) {
        this.munCodigoExacto = munCodigoExacto;
    }

    public String getMunNombreExacto() {
        return munNombreExacto;
    }

    public void setMunNombreExacto(String munNombreExacto) {
        this.munNombreExacto = munNombreExacto;
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
