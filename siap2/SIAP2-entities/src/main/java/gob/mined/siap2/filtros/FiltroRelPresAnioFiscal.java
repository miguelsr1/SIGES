package gob.mined.siap2.filtros;

import gob.mined.siap2.entities.enums.TipoPresupuestoAnio;
import java.io.Serializable;

public class FiltroRelPresAnioFiscal implements Serializable {

    private Integer id;
    private Integer componentePresupuestoEscolarId;
    private Integer categoriaPresupuestoEscolarId;
    private Integer anioFiscalId;
    private Integer subCuentaId;
    private TipoPresupuestoAnio tipo;
    private String nombreComplemento;
    private String descripcion;
    //Campos para filtros de: FiltroRelPresAnioFinanciamiento
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

    public Integer getComponentePresupuestoEscolarId() {
        return componentePresupuestoEscolarId;
    }

    public void setComponentePresupuestoEscolarId(Integer componentePresupuestoEscolarId) {
        this.componentePresupuestoEscolarId = componentePresupuestoEscolarId;
    }

    public Integer getAnioFiscalId() {
        return anioFiscalId;
    }

    public void setAnioFiscalId(Integer anioFiscalId) {
        this.anioFiscalId = anioFiscalId;
    }

    public Integer getCategoriaPresupuestoEscolarId() {
        return categoriaPresupuestoEscolarId;
    }

    public void setCategoriaPresupuestoEscolarId(Integer categoriaPresupuestoEscolarId) {
        this.categoriaPresupuestoEscolarId = categoriaPresupuestoEscolarId;
    }

    public Integer getSubCuentaId() {
        return subCuentaId;
    }

    public void setSubCuentaId(Integer subCuentaId) {
        this.subCuentaId = subCuentaId;
    }

    public TipoPresupuestoAnio getTipo() {
        return tipo;
    }

    public void setTipo(TipoPresupuestoAnio tipo) {
        this.tipo = tipo;
    }

    public String getNombreComplemento() {
        return nombreComplemento;
    }

    public void setNombreComplemento(String nombreComplemento) {
        this.nombreComplemento = nombreComplemento;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
