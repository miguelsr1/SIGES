/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado;
import sv.gob.mined.siges.persistencia.entidades.SgEstudiante;

/**
 *
 * @author usuario
 */

public class SgPorcentajeAsistenciasRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long pinSeccion;
    
    private Long pinAnioLectivo;
    
    private List<SgEstudiante> pinEstudiantes;
    
    private LocalDate pinFechaDesde;
    
    private LocalDate pinFechaHasta;
    
    private Long componentePlanEstudio;
    
    private SgComponentePlanGrado componentePlanGrado;
    
    //Se utiliza para la formula de habilitacion de periodo extraordinario en caso que sea de tipo prueba
    private Long componentePlanEstudioPrueba;
    
    
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

    public Long getComponentePlanEstudio() {
        return componentePlanEstudio;
    }

    public void setComponentePlanEstudio(Long componentePlanEstudio) {
        this.componentePlanEstudio = componentePlanEstudio;
    }  

    public Long getComponentePlanEstudioPrueba() {
        return componentePlanEstudioPrueba;
    }

    public void setComponentePlanEstudioPrueba(Long componentePlanEstudioPrueba) {
        this.componentePlanEstudioPrueba = componentePlanEstudioPrueba;
    }

    public Long getPinAnioLectivo() {
        return pinAnioLectivo;
    }

    public void setPinAnioLectivo(Long pinAnioLectivo) {
        this.pinAnioLectivo = pinAnioLectivo;
    }

    public SgComponentePlanGrado getComponentePlanGrado() {
        return componentePlanGrado;
    }

    public void setComponentePlanGrado(SgComponentePlanGrado componentePlanGrado) {
        this.componentePlanGrado = componentePlanGrado;
    }
   
    
}
