package sv.gob.mined.siges.web.filtrosbusqueda;

/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */


import java.io.Serializable;
import java.time.LocalDate;
import sv.gob.mined.siges.web.enumerados.EnumEstadoCurso;

public class FiltroCursoDocente implements Serializable {
       
    private String cdsNombreCurso;
    private Long cdsModulo;
    private Long cdsCategoria;
    private Long cdsNivel;
    private LocalDate cdsFechaInicio;
    private LocalDate cdsFechaFin;
    private Long cdsEspecialidad;
    private Long cdsDepartamento;
    private EnumEstadoCurso cdsEstado;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroCursoDocente() {
    }

    public String getCdsNombreCurso() {
        return cdsNombreCurso;
    }

    public void setCdsNombreCurso(String cdsNombreCurso) {
        this.cdsNombreCurso = cdsNombreCurso;
    }

    public Long getCdsModulo() {
        return cdsModulo;
    }

    public void setCdsModulo(Long cdsModulo) {
        this.cdsModulo = cdsModulo;
    }

    public Long getCdsCategoria() {
        return cdsCategoria;
    }

    public void setCdsCategoria(Long cdsCategoria) {
        this.cdsCategoria = cdsCategoria;
    }

    public Long getCdsNivel() {
        return cdsNivel;
    }

    public void setCdsNivel(Long cdsNivel) {
        this.cdsNivel = cdsNivel;
    }

    public LocalDate getCdsFechaInicio() {
        return cdsFechaInicio;
    }

    public void setCdsFechaInicio(LocalDate cdsFechaInicio) {
        this.cdsFechaInicio = cdsFechaInicio;
    }

    public LocalDate getCdsFechaFin() {
        return cdsFechaFin;
    }

    public void setCdsFechaFin(LocalDate cdsFechaFin) {
        this.cdsFechaFin = cdsFechaFin;
    }

    public Long getCdsEspecialidad() {
        return cdsEspecialidad;
    }

    public void setCdsEspecialidad(Long cdsEspecialidad) {
        this.cdsEspecialidad = cdsEspecialidad;
    }

    public Long getCdsDepartamento() {
        return cdsDepartamento;
    }

    public void setCdsDepartamento(Long cdsDepartamento) {
        this.cdsDepartamento = cdsDepartamento;
    }

    public EnumEstadoCurso getCdsEstado() {
        return cdsEstado;
    }

    public void setCdsEstado(EnumEstadoCurso cdsEstado) {
        this.cdsEstado = cdsEstado;
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
