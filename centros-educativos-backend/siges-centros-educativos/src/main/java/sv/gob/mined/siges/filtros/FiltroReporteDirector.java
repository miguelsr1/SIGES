/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;

public class FiltroReporteDirector implements Serializable {
    
    private Long ridSede;
    
    private Boolean rdiDatosSede;
    private Boolean rdiDatosPersonal;
    private Boolean rdiDatosEstudiante;
    private String sedCodigo;
    private String sedNombre;
    private Long sedDepartamentoFk;
    private Long sedMunicipioFk;
    private Boolean rdiDatosGeneral;
    
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    private String[] incluirCampos;
    
    public FiltroReporteDirector() {
    }

    public Long getRidSede() {
        return ridSede;
    }

    public void setRidSede(Long ridSede) {
        this.ridSede = ridSede;
    }

    public Boolean getRdiDatosSede() {
        return rdiDatosSede;
    }

    public void setRdiDatosSede(Boolean rdiDatosSede) {
        this.rdiDatosSede = rdiDatosSede;
    }

    public Boolean getRdiDatosPersonal() {
        return rdiDatosPersonal;
    }

    public void setRdiDatosPersonal(Boolean rdiDatosPersonal) {
        this.rdiDatosPersonal = rdiDatosPersonal;
    }

    public Boolean getRdiDatosEstudiante() {
        return rdiDatosEstudiante;
    }

    public void setRdiDatosEstudiante(Boolean rdiDatosEstudiante) {
        this.rdiDatosEstudiante = rdiDatosEstudiante;
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

    public Boolean getRdiDatosGeneral() {
        return rdiDatosGeneral;
    }

    public void setRdiDatosGeneral(Boolean rdiDatosGeneral) {
        this.rdiDatosGeneral = rdiDatosGeneral;
    }

    public Long getSedDepartamentoFk() {
        return sedDepartamentoFk;
    }

    public void setSedDepartamentoFk(Long sedDepartamentoFk) {
        this.sedDepartamentoFk = sedDepartamentoFk;
    }

    public Long getSedMunicipioFk() {
        return sedMunicipioFk;
    }

    public void setSedMunicipioFk(Long sedMunicipioFk) {
        this.sedMunicipioFk = sedMunicipioFk;
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
