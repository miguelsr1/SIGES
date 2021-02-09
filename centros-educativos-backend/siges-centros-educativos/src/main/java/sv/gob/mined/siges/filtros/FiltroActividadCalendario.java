/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.filtros;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.enumerados.EnumAmbito;

public class FiltroActividadCalendario implements Serializable {
    
    private Long calendarioPk;
    private Long sedePk;
    private Long departamentoPk;
    private List<EnumAmbito> ambitos;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private String nombre;
    private Boolean esLectivo;
    private Boolean tomarEnCuentaTodosAmbitos;
    private Long anioFk;
    private Long tipoCalendarioFk;
    
    private Boolean noUsarDataSecurity;
    private Long first;
    private Long maxResults;
    private String[] orderBy;
    private boolean[] ascending;
    
    private String[] incluirCampos;

    public FiltroActividadCalendario() {
    }

    public Boolean getEsLectivo() {
        return esLectivo;
    }

    public void setEsLectivo(Boolean esLectivo) {
        this.esLectivo = esLectivo;
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

    public Long getCalendarioPk() {
        return calendarioPk;
    }

    public void setCalendarioPk(Long calendarioPk) {
        this.calendarioPk = calendarioPk;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public Long getDepartamentoPk() {
        return departamentoPk;
    }

    public void setDepartamentoPk(Long departamentoPk) {
        this.departamentoPk = departamentoPk;
    }

    public List<EnumAmbito> getAmbitos() {
        return ambitos;
    }

    public void setAmbitos(List<EnumAmbito> ambitos) {
        this.ambitos = ambitos;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getTomarEnCuentaTodosAmbitos() {
        return tomarEnCuentaTodosAmbitos;
    }

    public void setTomarEnCuentaTodosAmbitos(Boolean tomarEnCuentaTodosAmbitos) {
        this.tomarEnCuentaTodosAmbitos = tomarEnCuentaTodosAmbitos;
    }

    public Long getAnioFk() {
        return anioFk;
    }

    public void setAnioFk(Long anioFk) {
        this.anioFk = anioFk;
    }

    public Long getTipoCalendarioFk() {
        return tipoCalendarioFk;
    }

    public void setTipoCalendarioFk(Long tipoCalendarioFk) {
        this.tipoCalendarioFk = tipoCalendarioFk;
    }

    public Boolean getNoUsarDataSecurity() {
        return noUsarDataSecurity;
    }

    public void setNoUsarDataSecurity(Boolean noUsarDataSecurity) {
        this.noUsarDataSecurity = noUsarDataSecurity;
    }

    
}
