/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.gob.mined.siges.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.time.LocalDateTime;


@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "eimPk", scope = SgEstudiante.class)
public class SgEstudianteImpresion implements Serializable {

  
    private Long eimPk;
    

    private SgSolicitudImpresion eimSolicitudImpresion;
    

    private SgEstudiante eimEstudiante;
    

    private Boolean eimAnulada;   

    private LocalDateTime eimUltModFecha;

    private String eimUltModUsuario;
    
    private Integer eimVersion;
    
    private String codigoHash;
    
    private SgMotivoAnulacionTitulo eimMotivoAnulacion;
    
    private String eimObservacionAnulada;  
    
    public SgEstudianteImpresion(){
        
    }

    public Long getEimPk() {
        return eimPk;
    }

    public void setEimPk(Long eimPk) {
        this.eimPk = eimPk;
    }

    public SgSolicitudImpresion getEimSolicitudImpresion() {
        return eimSolicitudImpresion;
    }

    public void setEimSolicitudImpresion(SgSolicitudImpresion eimSolicitudImpresion) {
        this.eimSolicitudImpresion = eimSolicitudImpresion;
    }

    public SgEstudiante getEimEstudiante() {
        return eimEstudiante;
    }

    public void setEimEstudiante(SgEstudiante eimEstudiante) {
        this.eimEstudiante = eimEstudiante;
    }

    public Boolean getEimAnulada() {
        return eimAnulada;
    }

    public void setEimAnulada(Boolean eimAnulada) {
        this.eimAnulada = eimAnulada;
    }

    public SgMotivoAnulacionTitulo getEimMotivoAnulacion() {
        return eimMotivoAnulacion;
    }

    public void setEimMotivoAnulacion(SgMotivoAnulacionTitulo eimMotivoAnulacion) {
        this.eimMotivoAnulacion = eimMotivoAnulacion;
    }


    public LocalDateTime getEimUltModFecha() {
        return eimUltModFecha;
    }

    public void setEimUltModFecha(LocalDateTime eimUltModFecha) {
        this.eimUltModFecha = eimUltModFecha;
    }

    public String getEimUltModUsuario() {
        return eimUltModUsuario;
    }

    public void setEimUltModUsuario(String eimUltModUsuario) {
        this.eimUltModUsuario = eimUltModUsuario;
    }

    public Integer getEimVersion() {
        return eimVersion;
    }

    public void setEimVersion(Integer eimVersion) {
        this.eimVersion = eimVersion;
    }


    public String getCodigoHash() {
        return codigoHash;
    }

    public void setCodigoHash(String codigoHash) {
        this.codigoHash = codigoHash;
    }

    public String getEimObservacionAnulada() {
        return eimObservacionAnulada;
    }

    public void setEimObservacionAnulada(String eimObservacionAnulada) {
        this.eimObservacionAnulada = eimObservacionAnulada;
    }

      
    @Override
    public String toString() {
        return "SgEstudianteImpresion{" + "eimPk=" + eimPk + ", eimEstudiante=" + eimEstudiante.getEstPk().toString() + '}';
    }

}
