/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.time.LocalDate;
import java.util.List;

public class FiltroEspecialidadesPersonalAlAplicar {

    private Long aplicacionPlazaFk;
    private List<Long> aplicacionesPlazaFk;
    private Double epaCum;
    private LocalDate epaFechaGraduacionDesde;  
    private LocalDate epaFechaGraduacionHasta;  
    private List<Long> especialidades;

    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    private String[] incluirCampos;

    public FiltroEspecialidadesPersonalAlAplicar() {
    }

    public Long getAplicacionPlazaFk() {
        return aplicacionPlazaFk;
    }

    public void setAplicacionPlazaFk(Long aplicacionPlazaFk) {
        this.aplicacionPlazaFk = aplicacionPlazaFk;
    }

    public List<Long> getAplicacionesPlazaFk() {
        return aplicacionesPlazaFk;
    }

    public void setAplicacionesPlazaFk(List<Long> aplicacionesPlazaFk) {
        this.aplicacionesPlazaFk = aplicacionesPlazaFk;
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

    public Double getEpaCum() {
        return epaCum;
    }

    public void setEpaCum(Double epaCum) {
        this.epaCum = epaCum;
    }

    public LocalDate getEpaFechaGraduacionDesde() {
        return epaFechaGraduacionDesde;
    }

    public void setEpaFechaGraduacionDesde(LocalDate epaFechaGraduacionDesde) {
        this.epaFechaGraduacionDesde = epaFechaGraduacionDesde;
    }

    public LocalDate getEpaFechaGraduacionHasta() {
        return epaFechaGraduacionHasta;
    }

    public void setEpaFechaGraduacionHasta(LocalDate epaFechaGraduacionHasta) {
        this.epaFechaGraduacionHasta = epaFechaGraduacionHasta;
    }

    public List<Long> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<Long> especialidades) {
        this.especialidades = especialidades;
    }

    
}
