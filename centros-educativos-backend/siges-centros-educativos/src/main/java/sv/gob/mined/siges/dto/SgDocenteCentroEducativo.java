package sv.gob.mined.siges.dto;

import java.io.Serializable;


public class SgDocenteCentroEducativo implements Serializable {
    
    
    private String centroEducativoCodigo;
    private String nip;
    private String dui;
    private String docenteNombre;
    private String sexo;
    private String especialidad;
    private String cargo;
    private Boolean activo;

    public String getCentroEducativoCodigo() {
        return centroEducativoCodigo;
    }

    public void setCentroEducativoCodigo(String centroEducativoCodigo) {
        this.centroEducativoCodigo = centroEducativoCodigo;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getDocenteNombre() {
        return docenteNombre;
    }

    public void setDocenteNombre(String docenteNombre) {
        this.docenteNombre = docenteNombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    

    
    
    
    
  

  
    
    
}
