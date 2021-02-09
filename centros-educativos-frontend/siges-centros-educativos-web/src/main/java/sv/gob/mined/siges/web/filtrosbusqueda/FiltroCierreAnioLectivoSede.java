package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;

/**
 *
 * @author bruno
 */
public class FiltroCierreAnioLectivoSede implements Serializable {
    
    private Long cierreAnioPk;
    private Integer anioLectivo;
    private Long sedeId;
    private Long anioLectivoId;
    private Long departamentoId;
    private Long municipioId;
    
    private Boolean incluirPreguntas;
    
    private Boolean firmado;
    private Boolean procesoCierreCompleto; //Quiere decir que tiene fecha de cierre distinto de null
    private String firmaTransactionId;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public Long getCierreAnioPk() {
        return cierreAnioPk;
    }

    public void setCierreAnioPk(Long cierreAnioPk) {
        this.cierreAnioPk = cierreAnioPk;
    }

    public Integer getAnioLectivo() {
        return anioLectivo;
    }

    public void setAnioLectivo(Integer anioLectivo) {
        this.anioLectivo = anioLectivo;
    }

    public Long getSedeId() {
        return sedeId;
    }

    public void setSedeId(Long sedeId) {
        this.sedeId = sedeId;
    }

    public Long getAnioLectivoId() {
        return anioLectivoId;
    }

    public void setAnioLectivoId(Long anioLectivoId) {
        this.anioLectivoId = anioLectivoId;
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

    public Boolean getIncluirPreguntas() {
        return incluirPreguntas;
    }

    public void setIncluirPreguntas(Boolean incluirPreguntas) {
        this.incluirPreguntas = incluirPreguntas;
    }

    public Long getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    public Long getMunicipioId() {
        return municipioId;
    }

    public void setMunicipioId(Long municipioId) {
        this.municipioId = municipioId;
    }

    public Boolean getFirmado() {
        return firmado;
    }

    public void setFirmado(Boolean firmado) {
        this.firmado = firmado;
    }

    public Boolean getProcesoCierreCompleto() {
        return procesoCierreCompleto;
    }

    public void setProcesoCierreCompleto(Boolean procesoCierreCompleto) {
        this.procesoCierreCompleto = procesoCierreCompleto;
    }

    public String getFirmaTransactionId() {
        return firmaTransactionId;
    }

    public void setFirmaTransactionId(String firmaTransactionId) {
        this.firmaTransactionId = firmaTransactionId;
    }
    
   
    
}
