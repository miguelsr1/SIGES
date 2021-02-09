/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.List;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionCE;
import sv.gob.mined.siges.persistencia.entidades.SgEscolaridadEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgSeccion;

/**
 *
 * @author usuario
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SgCabezalEscolaridadEstudiante implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private List<SgEscolaridadEstudiante> escolaridadEstudianteList;
    
    private Boolean cerrarSeccion;
    
    private SgSeccion seccion;
    
     private SgCalificacionCE calificacion;

    public SgCabezalEscolaridadEstudiante() {
    }

    public List<SgEscolaridadEstudiante> getEscolaridadEstudianteList() {
        return escolaridadEstudianteList;
    }

    public void setEscolaridadEstudianteList(List<SgEscolaridadEstudiante> escolaridadEstudianteList) {
        this.escolaridadEstudianteList = escolaridadEstudianteList;
    }

    public Boolean getCerrarSeccion() {
        return cerrarSeccion;
    }

    public void setCerrarSeccion(Boolean cerrarSeccion) {
        this.cerrarSeccion = cerrarSeccion;
    }

    public SgSeccion getSeccion() {
        return seccion;
    }

    public void setSeccion(SgSeccion seccion) {
        this.seccion = seccion;
    }

    public SgCalificacionCE getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(SgCalificacionCE calificacion) {
        this.calificacion = calificacion;
    }

    
}
