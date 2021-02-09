/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import sv.gob.mined.siges.persistencia.entidades.SgCalificacionEstudiante;
import sv.gob.mined.siges.persistencia.entidades.SgComponentePlanGrado;

/**
 *
 * @author usuario
 */
public class SgComponenteArchivoInfo implements Serializable {

    private String nombreNormalizadoComponente;
    private String nombreComponente;
    private SgComponentePlanGrado componentePlanGrado;
    private List<SgCalificacionEstudiante> calificacionEstudianteList;
    

    public SgComponenteArchivoInfo() {
    }

    public String getNombreNormalizadoComponente() {
        return nombreNormalizadoComponente;
    }

    public void setNombreNormalizadoComponente(String nombreNormalizadoComponente) {
        this.nombreNormalizadoComponente = nombreNormalizadoComponente;
    }

    

    public SgComponentePlanGrado getComponentePlanGrado() {
        return componentePlanGrado;
    }

    public void setComponentePlanGrado(SgComponentePlanGrado componentePlanGrado) {
        this.componentePlanGrado = componentePlanGrado;
    }

    public List<SgCalificacionEstudiante> getCalificacionEstudianteList() {
        return calificacionEstudianteList;
    }

    public void setCalificacionEstudianteList(List<SgCalificacionEstudiante> calificacionEstudianteList) {
        this.calificacionEstudianteList = calificacionEstudianteList;
    }

    public String getNombreComponente() {
        return nombreComponente;
    }

    public void setNombreComponente(String nombreComponente) {
        this.nombreComponente = nombreComponente;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.nombreComponente);
        hash = 13 * hash + Objects.hashCode(this.componentePlanGrado);
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
        final SgComponenteArchivoInfo other = (SgComponenteArchivoInfo) obj;
        if (!Objects.equals(this.nombreComponente, other.nombreComponente)) {
            return false;
        }
        if (!Objects.equals(this.componentePlanGrado, other.componentePlanGrado)) {
            return false;
        }
        return true;
    }


  
    
    
    
    

   
}
