/*
 * Sofis Solutions
 */
package sv.gob.mined.siges.dto;

import java.io.Serializable;

public class SgConsultaMatriculasSede implements Serializable {
    
    private String nivelNombre;
    private String cicloNombre;
    private String modalidadEduNombre;
    private String modalidadAtenNombre;
    private String gradoNombre;
    private Long cantMatMasculino;
    private Long cantMatFemenino;
    

    public SgConsultaMatriculasSede() {
    }

    public String getNivelNombre() {
        return nivelNombre;
    }

    public void setNivelNombre(String nivelNombre) {
        this.nivelNombre = nivelNombre;
    }

    public String getCicloNombre() {
        return cicloNombre;
    }

    public void setCicloNombre(String cicloNombre) {
        this.cicloNombre = cicloNombre;
    }

    public String getGradoNombre() {
        return gradoNombre;
    }

    public void setGradoNombre(String gradoNombre) {
        this.gradoNombre = gradoNombre;
    }

    public Long getCantMatMasculino() {
        return cantMatMasculino;
    }

    public void setCantMatMasculino(Long cantMatMasculino) {
        this.cantMatMasculino = cantMatMasculino;
    }

    public Long getCantMatFemenino() {
        return cantMatFemenino;
    }

    public void setCantMatFemenino(Long cantMatFemenino) {
        this.cantMatFemenino = cantMatFemenino;
    }

    public String getModalidadEduNombre() {
        return modalidadEduNombre;
    }

    public void setModalidadEduNombre(String modalidadEduNombre) {
        this.modalidadEduNombre = modalidadEduNombre;
    }

    public String getModalidadAtenNombre() {
        return modalidadAtenNombre;
    }

    public void setModalidadAtenNombre(String modalidadAtenNombre) {
        this.modalidadAtenNombre = modalidadAtenNombre;
    }

    
    
   
     
}
