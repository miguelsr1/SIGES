/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;


public class SgCambioDomicilioSedeRequest implements Serializable {
    
    private Long sedePk;
    private String numTramite;
    private Long zonaPk;
    private Long departamentoPk;
    private Long municipioPk;
    private Long cantonPk;
    private String caserio;
    private String direccion;
    private String telefono;
    
    private String numAcuerdo;
    private String observacion; 
    private String numResolucion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaResolucion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaGeneracion;
    
  
    public Long getSedePk() {
        return sedePk;
    }

    public void setSedePk(Long sedePk) {
        this.sedePk = sedePk;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }  

    public Long getZonaPk() {
        return zonaPk;
    }

    public void setZonaPk(Long zonaPk) {
        this.zonaPk = zonaPk;
    }

    public Long getMunicipioPk() {
        return municipioPk;
    }

    public void setMunicipioPk(Long municipioPk) {
        this.municipioPk = municipioPk;
    }

    public Long getCantonPk() {
        return cantonPk;
    }

    public void setCantonPk(Long cantonPk) {
        this.cantonPk = cantonPk;
    }

    public String getCaserio() {
        return caserio;
    }

    public void setCaserio(String caserio) {
        this.caserio = caserio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Long getDepartamentoPk() {
        return departamentoPk;
    }

    public void setDepartamentoPk(Long departamentoPk) {
        this.departamentoPk = departamentoPk;
    }

    public String getNumAcuerdo() {
        return numAcuerdo;
    }

    public void setNumAcuerdo(String numAcuerdo) {
        this.numAcuerdo = numAcuerdo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNumResolucion() {
        return numResolucion;
    }

    public void setNumResolucion(String numResolucion) {
        this.numResolucion = numResolucion;
    }

    public LocalDate getFechaResolucion() {
        return fechaResolucion;
    }

    public void setFechaResolucion(LocalDate fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }

    
    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    
    

    
}
