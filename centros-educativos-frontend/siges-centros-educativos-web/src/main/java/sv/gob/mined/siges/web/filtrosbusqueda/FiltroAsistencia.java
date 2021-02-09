package sv.gob.mined.siges.web.filtrosbusqueda;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
import java.io.Serializable;
import java.time.LocalDate;

public class FiltroAsistencia implements Serializable {

    private Long asiPk;
    private Boolean asiInasistencia;
    private Long asiEstudiante;
    private Long asiSeccion;
    private Long asiMotivoInasistencia;
    private LocalDate asiFecha;
    private Long asiCabezalPk;
    
   
    private LocalDate asiFechaDesde;
    private LocalDate asiFechaHasta;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;

    public FiltroAsistencia() {
    }

    public Long getAsiPk() {
        return asiPk;
    }

    public void setAsiPk(Long asiPk) {
        this.asiPk = asiPk;
    }

    public Boolean isAsiInasistencia() {
        return asiInasistencia;
    }

    public void setAsiInasistencia(Boolean asiInasistencia) {
        this.asiInasistencia = asiInasistencia;
    }

    public Long getAsiEstudiante() {
        return asiEstudiante;
    }

    public void setAsiEstudiante(Long asiEstudiante) {
        this.asiEstudiante = asiEstudiante;
    }

    public Long getAsiSeccion() {
        return asiSeccion;
    }

    public void setAsiSeccion(Long asiSeccion) {
        this.asiSeccion = asiSeccion;
    }

    public Long getAsiMotivoInasistencia() {
        return asiMotivoInasistencia;
    }

    public void setAsiMotivoInasistencia(Long asiMotivoInasistencia) {
        this.asiMotivoInasistencia = asiMotivoInasistencia;
    }

    public LocalDate getAsiFecha() {
        return asiFecha;
    }

    public void setAsiFecha(LocalDate asiFecha) {
        this.asiFecha = asiFecha;
    }

    public LocalDate getAsiFechaDesde() {
        return asiFechaDesde;
    }

    public void setAsiFechaDesde(LocalDate asiFechaDesde) {
        this.asiFechaDesde = asiFechaDesde;
    }

    public LocalDate getAsiFechaHasta() {
        return asiFechaHasta;
    }

    public void setAsiFechaHasta(LocalDate asiFechaHasta) {
        this.asiFechaHasta = asiFechaHasta;
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

    public Long getAsiCabezalPk() {
        return asiCabezalPk;
    }

    public void setAsiCabezalPk(Long asiCabezalPk) {
        this.asiCabezalPk = asiCabezalPk;
    }

    public String[] getIncluirCampos() {
        return incluirCampos;
    }

    public void setIncluirCampos(String[] incluirCampos) {
        this.incluirCampos = incluirCampos;
    }
    

}
