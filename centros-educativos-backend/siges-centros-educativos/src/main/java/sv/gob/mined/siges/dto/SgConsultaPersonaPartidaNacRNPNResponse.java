/*
 *  SIGES
 *  Desarrollado por Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class SgConsultaPersonaPartidaNacRNPNResponse implements Serializable {
    
    private String apellido1;
    private String apellido2;
    private String apellido3;
    private String nombre1;
    private String nombre2;
    private String nombre3;
    
    private String nombre1Madre;
    private String nombre2Madre;
    private String nombre3Madre;
    private String apellido1Madre;
    private String apellido2Madre;
    private String apellido3Madre;
      
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaRegistro; 
    
    
    //"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDateTime fechaHoraNacimiento;

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public String getApellido3() {
        return apellido3;
    }

    public void setApellido3(String apellido3) {
        this.apellido3 = apellido3;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getNombre3() {
        return nombre3;
    }

    public void setNombre3(String nombre3) {
        this.nombre3 = nombre3;
    }

    public String getNombre1Madre() {
        return nombre1Madre;
    }

    public void setNombre1Madre(String nombre1Madre) {
        this.nombre1Madre = nombre1Madre;
    }

    public String getNombre2Madre() {
        return nombre2Madre;
    }

    public void setNombre2Madre(String nombre2Madre) {
        this.nombre2Madre = nombre2Madre;
    }

    public String getNombre3Madre() {
        return nombre3Madre;
    }

    public void setNombre3Madre(String nombre3Madre) {
        this.nombre3Madre = nombre3Madre;
    }

    public String getApellido1Madre() {
        return apellido1Madre;
    }

    public void setApellido1Madre(String apellido1Madre) {
        this.apellido1Madre = apellido1Madre;
    }

    public String getApellido2Madre() {
        return apellido2Madre;
    }

    public void setApellido2Madre(String apellido2Madre) {
        this.apellido2Madre = apellido2Madre;
    }

    public String getApellido3Madre() {
        return apellido3Madre;
    }

    public void setApellido3Madre(String apellido3Madre) {
        this.apellido3Madre = apellido3Madre;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDateTime getFechaHoraNacimiento() {
        return fechaHoraNacimiento;
    }

    public void setFechaHoraNacimiento(LocalDateTime fechaHoraNacimiento) {
        this.fechaHoraNacimiento = fechaHoraNacimiento;
    }
    
    


    
}
