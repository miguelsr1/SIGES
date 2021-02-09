package sv.gob.mined.siges.dto;

import java.io.Serializable;


public class SgSeccionCentroEducativo implements Serializable {
    
    
    private String centroEducativoCodigo;
    private String nivelEducativo;
    private String jornada;
    private String grado;
    private String organizacionCurricular;
    private String modalidadEducativa;
    private String modalidadAtencion;
    private String ciclo;
    private String opcion;
    private String sectorEconomico;
    private String programaEducativo;
    private Long id;
    private String seccion;

    public String getCentroEducativoCodigo() {
        return centroEducativoCodigo;
    }

    public void setCentroEducativoCodigo(String centroEducativoCodigo) {
        this.centroEducativoCodigo = centroEducativoCodigo;
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getOrganizacionCurricular() {
        return organizacionCurricular;
    }

    public void setOrganizacionCurricular(String organizacionCurricular) {
        this.organizacionCurricular = organizacionCurricular;
    }

    public String getModalidadEducativa() {
        return modalidadEducativa;
    }

    public void setModalidadEducativa(String modalidadEducativa) {
        this.modalidadEducativa = modalidadEducativa;
    }

    public String getModalidadAtencion() {
        return modalidadAtencion;
    }

    public void setModalidadAtencion(String modalidadAtencion) {
        this.modalidadAtencion = modalidadAtencion;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public String getSectorEconomico() {
        return sectorEconomico;
    }

    public void setSectorEconomico(String sectorEconomico) {
        this.sectorEconomico = sectorEconomico;
    }

    public String getProgramaEducativo() {
        return programaEducativo;
    }

    public void setProgramaEducativo(String programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }
    
    
    

}
