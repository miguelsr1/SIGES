/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author usuario
 */

public class SgPorcentajeAsistenciasRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long pinSeccion;
    
    private List<SgEstudiante> pinEstudiantes;
    
    private LocalDate pinFechaDesde;
    
    private LocalDate pinFechaHasta;
    
    private Long pinAnioLectivo;
    
    
    public SgPorcentajeAsistenciasRequest(){        
    }

    public Long getPinSeccion() {
        return pinSeccion;
    }

    public void setPinSeccion(Long pinSeccion) {
        this.pinSeccion = pinSeccion;
    }

    public List<SgEstudiante> getPinEstudiantes() {
        return pinEstudiantes;
    }

    public void setPinEstudiantes(List<SgEstudiante> pinEstudiantes) {
        this.pinEstudiantes = pinEstudiantes;
    }

    public LocalDate getPinFechaDesde() {
        return pinFechaDesde;
    }

    public void setPinFechaDesde(LocalDate pinFechaDesde) {
        this.pinFechaDesde = pinFechaDesde;
    }

    public LocalDate getPinFechaHasta() {
        return pinFechaHasta;
    }

    public void setPinFechaHasta(LocalDate pinFechaHasta) {
        this.pinFechaHasta = pinFechaHasta;
    }

    public Long getPinAnioLectivo() {
        return pinAnioLectivo;
    }

    public void setPinAnioLectivo(Long pinAnioLectivo) {
        this.pinAnioLectivo = pinAnioLectivo;
    }
  
   
    
}
