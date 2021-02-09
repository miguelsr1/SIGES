/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;

/**
 *
 * @author usuario
 */

public class SgPorcentajeAsistenciasResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer pinCantidadInasistencias;
    
    private Integer pinCantidadAsistencias;
    
    private Integer pinPorcentajeAsistencias;
    
    private SgEstudiante pinEstudiante;
    
    private Integer pinCantidadNoAprobado;
    
    private String pinNotaInstitucional;
    
    private Boolean pinEstudianteConTodosLosPeriodosCalificados;
    
    private String pinNotaInstitucionalPrueba;
    
    private String pinMayorNota;
    
    private Double pinPpe;
    
    private Double pinPpeps;
    
    private Double pinSpe;
    
    private Double pinSppe;
    
    
    public SgPorcentajeAsistenciasResponse(){        
    }

    public Integer getPinCantidadInasistencias() {
        return pinCantidadInasistencias;
    }

    public void setPinCantidadInasistencias(Integer pinCantidadInasistencias) {
        this.pinCantidadInasistencias = pinCantidadInasistencias;
    }

    public Integer getPinCantidadAsistencias() {
        return pinCantidadAsistencias;
    }

    public void setPinCantidadAsistencias(Integer pinCantidadAsistencias) {
        this.pinCantidadAsistencias = pinCantidadAsistencias;
    }

    public Integer getPinPorcentajeAsistencias() {
        return pinPorcentajeAsistencias;
    }

    public void setPinPorcentajeAsistencias(Integer pinPorcentajeAsistencias) {
        this.pinPorcentajeAsistencias = pinPorcentajeAsistencias;
    }

    public SgEstudiante getPinEstudiante() {
        return pinEstudiante;
    }

    public void setPinEstudiante(SgEstudiante pinEstudiante) {
        this.pinEstudiante = pinEstudiante;
    }

    public Integer getPinCantidadNoAprobado() {
        return pinCantidadNoAprobado;
    }

    public void setPinCantidadNoAprobado(Integer pinCantidadNoAprobado) {
        this.pinCantidadNoAprobado = pinCantidadNoAprobado;
    }

    public String getPinNotaInstitucional() {
        return pinNotaInstitucional;
    }

    public void setPinNotaInstitucional(String pinNotaInstitucional) {
        this.pinNotaInstitucional = pinNotaInstitucional;
    }  

    public String getPinNotaInstitucionalPrueba() {
        return pinNotaInstitucionalPrueba;
    }

    public void setPinNotaInstitucionalPrueba(String pinNotaInstitucionalPrueba) {
        this.pinNotaInstitucionalPrueba = pinNotaInstitucionalPrueba;
    }

    public String getPinMayorNota() {
        return pinMayorNota;
    }

    public void setPinMayorNota(String pinMayorNota) {
        this.pinMayorNota = pinMayorNota;
    }

    public Boolean getPinEstudianteConTodosLosPeriodosCalificados() {
        return pinEstudianteConTodosLosPeriodosCalificados;
    }

    public void setPinEstudianteConTodosLosPeriodosCalificados(Boolean pinEstudianteConTodosLosPeriodosCalificados) {
        this.pinEstudianteConTodosLosPeriodosCalificados = pinEstudianteConTodosLosPeriodosCalificados;
    }

    public Double getPinPpe() {
        return pinPpe;
    }

    public void setPinPpe(Double pinPpe) {
        this.pinPpe = pinPpe;
    }

    public Double getPinPpeps() {
        return pinPpeps;
    }

    public void setPinPpeps(Double pinPpeps) {
        this.pinPpeps = pinPpeps;
    }


    public Double getPinSpe() {
        return pinSpe;
    }

    public void setPinSpe(Double pinSpe) {
        this.pinSpe = pinSpe;
    }

    public Double getPinSppe() {
        return pinSppe;
    }

    public void setPinSppe(Double pinSppe) {
        this.pinSppe = pinSppe;
    }

    

    @Override
    public String toString() {
        return "SgPorcentajeAsistenciasResponse{" + "pinCantidadAsistencias=" + pinCantidadAsistencias + ", pinEstudiante=" + pinEstudiante.getEstPk() + ", pinCantidadNoAprobado=" + pinCantidadNoAprobado + ", pinNotaInstitucional=" + pinNotaInstitucional + '}';
    }


   
    
    
    
}
