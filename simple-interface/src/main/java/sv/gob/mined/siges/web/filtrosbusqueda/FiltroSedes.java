/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */


package sv.gob.mined.siges.web.filtrosbusqueda;
import java.io.Serializable;
import sv.gob.mined.siges.web.enumerados.TipoSede;

public class FiltroSedes implements Serializable {
    
    private Long sedPk;
    private String sedNombre;
    private String sedCodigo;  
    private Boolean sedLegalizada;
    private Long sedDepartamentoId;
    private Long sedMunicipioId;
    private Long sedZonaId;
    private TipoSede sedTipo;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroSedes() {
        this.first = 0L;
    }
     

    public Long getSedPk() {
        return sedPk;
    }

    public void setSedPk(Long sedPk) {
        this.sedPk = sedPk;
    }

    public String getSedCodigo() {
        return sedCodigo;
    }

    public void setSedCodigo(String sedCodigo) {
        this.sedCodigo = sedCodigo;
    }

    public String getSedNombre() {
        return sedNombre;
    }

    public void setSedNombre(String sedNombre) {
        this.sedNombre = sedNombre;
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

    public Boolean getSedLegalizada() {
        return sedLegalizada;
    }

    public void setSedLegalizada(Boolean sedLegalizada) {
        this.sedLegalizada = sedLegalizada;
    }

    public Long getSedDepartamentoId() {
        return sedDepartamentoId;
    }

    public void setSedDepartamentoId(Long sedDepartamentoId) {
        this.sedDepartamentoId = sedDepartamentoId;
    }

    public Long getSedMunicipioId() {
        return sedMunicipioId;
    }

    public void setSedMunicipioId(Long sedMunicipioId) {
        this.sedMunicipioId = sedMunicipioId;
    }

    public Long getSedZonaId() {
        return sedZonaId;
    }

    public void setSedZonaId(Long sedZonaId) {
        this.sedZonaId = sedZonaId;
    }

    public TipoSede getSedTipo() {
        return sedTipo;
    }

    public void setSedTipo(TipoSede sedTipo) {
        this.sedTipo = sedTipo;
    }

     
}
