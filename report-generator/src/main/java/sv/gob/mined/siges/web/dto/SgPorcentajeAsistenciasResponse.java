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
    
    private Integer pinCantidadInasistenciasJustificadas;
    
    private Integer pinCantidadInasistenciasSinJustificar;
    
    private Integer pinCantidadAsistencias;
    
    private Integer pinPorcentajeAsistencias;
    
    private SgEstudiante pinEstudiante;
    
    
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

    public Integer getPinCantidadInasistenciasJustificadas() {
        return pinCantidadInasistenciasJustificadas;
    }

    public void setPinCantidadInasistenciasJustificadas(Integer pinCantidadInasistenciasJustificadas) {
        this.pinCantidadInasistenciasJustificadas = pinCantidadInasistenciasJustificadas;
    }

    public Integer getPinCantidadInasistenciasSinJustificar() {
        return pinCantidadInasistenciasSinJustificar;
    }

    public void setPinCantidadInasistenciasSinJustificar(Integer pinCantidadInasistenciasSinJustificar) {
        this.pinCantidadInasistenciasSinJustificar = pinCantidadInasistenciasSinJustificar;
    }
    
    
    
}
