package gob.mined.siap2.filtros;

import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import java.io.Serializable;

public class FiltroTransferencias implements Serializable {

    private Integer id;
    private Integer idAnioFiscal;
    private Integer idSubcomponente;
    private Integer idLineaPresupuestaria;
    
    private TipoPresupuestoAnio tipo;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;



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

    public Integer getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(Integer idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public Integer getIdSubcomponente() {
        return idSubcomponente;
    }

    public void setIdSubcomponente(Integer idSubcomponente) {
        this.idSubcomponente = idSubcomponente;
    }

    public Integer getIdLineaPresupuestaria() {
        return idLineaPresupuestaria;
    }

    public void setIdLineaPresupuestaria(Integer idLineaPresupuestaria) {
        this.idLineaPresupuestaria = idLineaPresupuestaria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoPresupuestoAnio getTipo() {
        return tipo;
    }

    public void setTipo(TipoPresupuestoAnio tipo) {
        this.tipo = tipo;
    }

   

    
    
}
