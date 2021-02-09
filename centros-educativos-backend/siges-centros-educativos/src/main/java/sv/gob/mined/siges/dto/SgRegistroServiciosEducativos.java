package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class SgRegistroServiciosEducativos implements Serializable {

    private Long sedePk;
    private String numTramite;
    private List<List<String>> listadoServicios;
    
    private String numAcuerdo;
    private String observacion; 
    private String numResolucion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaResolucion;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaGeneracion;
    
//    listadoServicios = [["4","6","7","1","-1","-1","-1","1"],  ["4","6","8","1","-1","-1","-1","20"]]
//    0 - nivel
//    1 - ciclo
//    2 - modalidad educativa
//    3 - modalidad de atención
//    4 - submodalidad
//    5 - opción
//    6 - programa educativo
//    7 - grado
    public SgRegistroServiciosEducativos() {
    }

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

    public List<List<String>> getListadoServicios() {
        return listadoServicios;
    }

    public void setListadoServicios(List<List<String>> listadoServicios) {
        this.listadoServicios = listadoServicios;
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
