/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;

public class SgConsultaCalificacionesPendientesSedesEnNivel implements Serializable {
    
    private String departamento;
    private String municipio;
    private String sedeCodigo;
    private String sedeNombre;
    private String tipoCalendarioNombre;
    private String desagregacion;
    private Long posibles;
    private Long realizadas;
    private Long pendientes;
    

    public SgConsultaCalificacionesPendientesSedesEnNivel() {
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getSedeCodigo() {
        return sedeCodigo;
    }

    public void setSedeCodigo(String sedeCodigo) {
        this.sedeCodigo = sedeCodigo;
    }

    public String getSedeNombre() {
        return sedeNombre;
    }

    public void setSedeNombre(String sedeNombre) {
        this.sedeNombre = sedeNombre;
    }
    

    public String getDesagregacion() {
        return desagregacion;
    }

    public void setDesagregacion(String desagregacion) {
        this.desagregacion = desagregacion;
    }

    public Long getPosibles() {
        return posibles;
    }

    public void setPosibles(Long posibles) {
        this.posibles = posibles;
    }

    public Long getRealizadas() {
        return realizadas;
    }

    public void setRealizadas(Long realizadas) {
        this.realizadas = realizadas;
    }

    public Long getPendientes() {
        return pendientes;
    }

    public void setPendientes(Long pendientes) {
        this.pendientes = pendientes;
    }

    public String getTipoCalendarioNombre() {
        return tipoCalendarioNombre;
    }

    public void setTipoCalendarioNombre(String tipoCalendarioNombre) {
        this.tipoCalendarioNombre = tipoCalendarioNombre;
    }

     
}
