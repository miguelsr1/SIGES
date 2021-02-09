package sv.gob.mined.siges.web.filtrosbusqueda;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
import java.io.Serializable;
import java.time.LocalDate;

public class FiltroControlAsistenciaCabezal extends FiltroSeccion implements Serializable {

    private Long cacPk;
    private LocalDate cacFecha;
    private LocalDate cacDesde;
    private LocalDate cacHasta;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos; //Si se utiliza incluirCampos, no se utiliza incluirSeccion
    private Boolean incluirAsistencias;
    private Boolean incluirSeccion; 

    public FiltroControlAsistenciaCabezal() {
        super();
    }

    public Long getCacPk() {
        return cacPk;
    }

    public void setCacPk(Long cacPk) {
        this.cacPk = cacPk;
    }

    public LocalDate getCacFecha() {
        return cacFecha;
    }

    public void setCacFecha(LocalDate cacFecha) {
        this.cacFecha = cacFecha;
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

    public LocalDate getCacDesde() {
        return cacDesde;
    }

    public void setCacDesde(LocalDate cacDesde) {
        this.cacDesde = cacDesde;
    }

    public LocalDate getCacHasta() {
        return cacHasta;
    }

    public void setCacHasta(LocalDate cacHasta) {
        this.cacHasta = cacHasta;
    }

    public Boolean getIncluirAsistencias() {
        return incluirAsistencias;
    }

    public void setIncluirAsistencias(Boolean incluirAsistencias) {
        this.incluirAsistencias = incluirAsistencias;
    }

    public Boolean getIncluirSeccion() {
        return incluirSeccion;
    }

    public void setIncluirSeccion(Boolean incluirSeccion) {
        this.incluirSeccion = incluirSeccion;
    }
    
    
    

}
