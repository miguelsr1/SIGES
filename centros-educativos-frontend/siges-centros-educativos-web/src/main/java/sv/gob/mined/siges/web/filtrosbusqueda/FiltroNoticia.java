/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.web.filtrosbusqueda;

import java.io.Serializable;
import java.time.LocalDate;

public class FiltroNoticia implements Serializable {

    private Long notPk;
    private String notCodigo;
    private String notTitulo;
    private Boolean notHabilitado;
    private LocalDate notDesde;
    private LocalDate notHasta;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroNoticia() {
        this.first = 0L;
    }
    
   
    public Long getNotPk() {
        return notPk;
    }

    public void setNotPk(Long notPk) {
        this.notPk = notPk;
    }

    public String getNotCodigo() {
        return notCodigo;
    }

    public void setNotCodigo(String notCodigo) {
        this.notCodigo = notCodigo;
    }

    public String getNotTitulo() {
        return notTitulo;
    }

    public void setNotTitulo(String notTitulo) {
        this.notTitulo = notTitulo;
    }

    public Boolean getNotHabilitado() {
        return notHabilitado;
    }

    public void setNotHabilitado(Boolean notHabilitado) {
        this.notHabilitado = notHabilitado;
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

    public LocalDate getNotDesde() {
        return notDesde;
    }

    public void setNotDesde(LocalDate notDesde) {
        this.notDesde = notDesde;
    }

    public LocalDate getNotHasta() {
        return notHasta;
    }

    public void setNotHasta(LocalDate notHasta) {
        this.notHasta = notHasta;
    }

}
