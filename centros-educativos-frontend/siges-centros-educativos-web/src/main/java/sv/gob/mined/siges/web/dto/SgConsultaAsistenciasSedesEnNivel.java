/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;

public class SgConsultaAsistenciasSedesEnNivel implements Serializable {

    private String departamento;
    private String municipio;
    private String sedeCodigo;
    private Long sedePk;
    private String sedeNombre;
    private String tipoCalendarioNombre;
    private String desagregacion;
    private Long asistencias;
    private Long inasistencias;
    

    public SgConsultaAsistenciasSedesEnNivel() {
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

    public Long getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(Long asistencias) {
        this.asistencias = asistencias;
    }

    public Long getInasistencias() {
        return inasistencias;
    }

    public void setInasistencias(Long inasistencias) {
        this.inasistencias = inasistencias;
    }

    public String getTipoCalendarioNombre() {
        return tipoCalendarioNombre;
    }

    public void setTipoCalendarioNombre(String tipoCalendarioNombre) {
        this.tipoCalendarioNombre = tipoCalendarioNombre;
    }

    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }
    
    

   
}
