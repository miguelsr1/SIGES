/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.util.List;

public class FiltroComponenteDocente implements Serializable {

    private Long cdoHorarioEscolar;
    private Long cdoPk;
    private Long cdoDocente;
    private List<Long> cdoDocentes;
    private Integer cdoAnio;
    private Long cdoSede;
    private Long cdoSeccion;
    
    private Boolean buscarEnSedeAdscrita;

    private String[] incluirCampos;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;

    public FiltroComponenteDocente() {
    }

    public Long getCdoHorarioEscolar() {
        return cdoHorarioEscolar;
    }

    public void setCdoHorarioEscolar(Long cdoHorarioEscolar) {
        this.cdoHorarioEscolar = cdoHorarioEscolar;
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

    public Long getCdoPk() {
        return cdoPk;
    }

    public void setCdoPk(Long cdoPk) {
        this.cdoPk = cdoPk;
    }

    public Long getCdoDocente() {
        return cdoDocente;
    }

    public void setCdoDocente(Long cdoDocente) {
        this.cdoDocente = cdoDocente;
    }

    public List<Long> getCdoDocentes() {
        return cdoDocentes;
    }

    public void setCdoDocentes(List<Long> cdoDocentes) {
        this.cdoDocentes = cdoDocentes;
    }

    public Integer getCdoAnio() {
        return cdoAnio;
    }

    public void setCdoAnio(Integer cdoAnio) {
        this.cdoAnio = cdoAnio;
    }

    public Long getCdoSede() {
        return cdoSede;
    }

    public void setCdoSede(Long cdoSede) {
        this.cdoSede = cdoSede;
    }

    public Boolean getBuscarEnSedeAdscrita() {
        return buscarEnSedeAdscrita;
    }

    public void setBuscarEnSedeAdscrita(Boolean buscarEnSedeAdscrita) {
        this.buscarEnSedeAdscrita = buscarEnSedeAdscrita;
    }

    public Long getCdoSeccion() {
        return cdoSeccion;
    }

    public void setCdoSeccion(Long cdoSeccion) {
        this.cdoSeccion = cdoSeccion;
    }
    
    
}
