/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

    public SgComponentePlanGrado getComponentePlanGrado() {
        return componentePlanGrado;
    }

    public void setComponentePlanGrado(SgComponentePlanGrado componentePlanGrado) {
        this.componentePlanGrado = componentePlanGrado;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.pinSeccion);
        hash = 71 * hash + Objects.hashCode(this.pinEstudiantes);
        hash = 71 * hash + Objects.hashCode(this.pinFechaDesde);
        hash = 71 * hash + Objects.hashCode(this.pinFechaHasta);
        hash = 71 * hash + Objects.hashCode(this.componentePlanEstudio);
        hash = 71 * hash + Objects.hashCode(this.componentePlanGrado);
        hash = 71 * hash + Objects.hashCode(this.componentePlanEstudioPrueba);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SgPorcentajeAsistenciasRequest other = (SgPorcentajeAsistenciasRequest) obj;
        if (!Objects.equals(this.pinSeccion, other.pinSeccion)) {
            return false;
        }
        if (!Objects.equals(this.pinEstudiantes, other.pinEstudiantes)) {
            return false;
        }
        if (!Objects.equals(this.pinFechaDesde, other.pinFechaDesde)) {
            return false;
        }
        if (!Objects.equals(this.pinFechaHasta, other.pinFechaHasta)) {
            return false;
        }
        if (!Objects.equals(this.componentePlanEstudio, other.componentePlanEstudio)) {
            return false;
        }
        if (!Objects.equals(this.componentePlanGrado, other.componentePlanGrado)) {
            return false;
        }
        if (!Objects.equals(this.componentePlanEstudioPrueba, other.componentePlanEstudioPrueba)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "SgPorcentajeAsistenciasRequest{" + "pinSeccion=" + pinSeccion + ", pinEstudiantes=" + pinEstudiantes + ", pinFechaDesde=" + pinFechaDesde + ", pinFechaHasta=" + pinFechaHasta + ", componentePlanEstudio=" + componentePlanEstudio + ", componentePlanGrado=" + componentePlanGrado + ", componentePlanEstudioPrueba=" + componentePlanEstudioPrueba + '}';
    }

    
  
   
    
}
