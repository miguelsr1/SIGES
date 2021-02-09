package gob.mined.siap2.filtros;

import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import java.io.Serializable;

public class FiltroRelPresAnioFinanciamiento implements Serializable {

    //Campos para filtros de: RelacionGesPresEsAnioFiscal
    private Integer idSubcomponente;
    private Integer idAnioFiscal;
    private Integer idLineaPresupuestaria;
    private TipoPresupuestoAnio tipoPresupuestoAnio;
    private String nombreComplemento;
    
    private Integer id;
    private Integer relAnioPresupuesto;
    private Integer idFuenteFinanciamiento;
    private Integer idFuenteRecursos;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getRelAnioPresupuesto() {
        return relAnioPresupuesto;
    }

    public void setRelAnioPresupuesto(Integer relAnioPresupuesto) {
        this.relAnioPresupuesto = relAnioPresupuesto;
    }

    public Integer getIdSubcomponente() {
        return idSubcomponente;
    }

    public void setIdSubcomponente(Integer idSubcomponente) {
        this.idSubcomponente = idSubcomponente;
    }

    public Integer getIdAnioFiscal() {
        return idAnioFiscal;
    }

    public void setIdAnioFiscal(Integer idAnioFiscal) {
        this.idAnioFiscal = idAnioFiscal;
    }

    public Integer getIdLineaPresupuestaria() {
        return idLineaPresupuestaria;
    }

    public void setIdLineaPresupuestaria(Integer idLineaPresupuestaria) {
        this.idLineaPresupuestaria = idLineaPresupuestaria;
    }

    public Integer getIdFuenteFinanciamiento() {
        return idFuenteFinanciamiento;
    }

    public void setIdFuenteFinanciamiento(Integer idFuenteFinanciamiento) {
        this.idFuenteFinanciamiento = idFuenteFinanciamiento;
    }

    public Integer getIdFuenteRecursos() {
        return idFuenteRecursos;
    }

    public void setIdFuenteRecursos(Integer idFuenteRecursos) {
        this.idFuenteRecursos = idFuenteRecursos;
    }

    public String getNombreComplemento() {
        return nombreComplemento;
    }

    public void setNombreComplemento(String nombreComplemento) {
        this.nombreComplemento = nombreComplemento;
    }

    public TipoPresupuestoAnio getTipoPresupuestoAnio() {
        return tipoPresupuestoAnio;
    }

    public void setTipoPresupuestoAnio(TipoPresupuestoAnio tipoPresupuestoAnio) {
        this.tipoPresupuestoAnio = tipoPresupuestoAnio;
    }
    
    
}
