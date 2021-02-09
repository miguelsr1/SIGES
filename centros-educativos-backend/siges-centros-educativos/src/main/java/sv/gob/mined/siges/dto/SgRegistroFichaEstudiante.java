package sv.gob.mined.siges.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;

public class SgRegistroFichaEstudiante implements Serializable  {
    
    private String dui;
    private String numCarneResidente;
    private Long paisCarneResidentePk;
    private String numPasaporte;
    private Long paisDocumentoPk;
    private String primerNombre;
    private String segundoNombre;
    private String tercerNombre;
    private String primerApellido;
    private String segundoApellido;
    private String tercerApellido;
    private Long sexoPk;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate fechaNacimiento;
    
    private Long estadoFamiliarPk;
    private Long nacionalidadPk;
    private Long zonaPk;
    private Long departamentoPk;
    private Long municipioPk;
    private String direccion;
    private Long opcionPk;
    private Long programaEducativoPk;
    private Long gradoPk;
    private Integer anioLectivo;
    private String numTramite;
    private String nombreSede;
    private Long planEstudioPk;
    
    private Boolean partidaNacimiento;       
    private Integer partidaNacimientoAnio;    
    private Long partidaNacimientoNumero;
    private String partidaNacimientoFolio;
    private String partidaNacimientoLibro;
    private String partidaNacimientoComplemento;
    private Long partidaNacimientoDepartamento;
    private Long partidaNacimientoMunicipio;    
    
    

    public SgRegistroFichaEstudiante() {
    }
    

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getNumCarneResidente() {
        return numCarneResidente;
    }

    public void setNumCarneResidente(String numCarneResidente) {
        this.numCarneResidente = numCarneResidente;
    }

    public String getNumPasaporte() {
        return numPasaporte;
    }

    public void setNumPasaporte(String numPasaporte) {
        this.numPasaporte = numPasaporte;
    }

    public Long getPaisDocumentoPk() {
        return paisDocumentoPk;
    }

    public void setPaisDocumentoPk(Long paisDocumentoPk) {
        this.paisDocumentoPk = paisDocumentoPk;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getTercerNombre() {
        return tercerNombre;
    }

    public void setTercerNombre(String tercerNombre) {
        this.tercerNombre = tercerNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getTercerApellido() {
        return tercerApellido;
    }

    public void setTercerApellido(String tercerApellido) {
        this.tercerApellido = tercerApellido;
    }

    public Long getSexoPk() {
        return sexoPk;
    }

    public void setSexoPk(Long sexoPk) {
        this.sexoPk = sexoPk;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Long getEstadoFamiliarPk() {
        return estadoFamiliarPk;
    }

    public void setEstadoFamiliarPk(Long estadoFamiliarPk) {
        this.estadoFamiliarPk = estadoFamiliarPk;
    }

    public Long getNacionalidadPk() {
        return nacionalidadPk;
    }

    public void setNacionalidadPk(Long nacionalidadPk) {
        this.nacionalidadPk = nacionalidadPk;
    }

    public Long getZonaPk() {
        return zonaPk;
    }

    public void setZonaPk(Long zonaPk) {
        this.zonaPk = zonaPk;
    }

    public Long getDepartamentoPk() {
        return departamentoPk;
    }

    public void setDepartamentoPk(Long departamentoPk) {
        this.departamentoPk = departamentoPk;
    }

    public Long getMunicipioPk() {
        return municipioPk;
    }

    public void setMunicipioPk(Long municipioPk) {
        this.municipioPk = municipioPk;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

 
    public Long getOpcionPk() {
        return opcionPk;
    }

    public void setOpcionPk(Long opcionPk) {
        this.opcionPk = opcionPk;
    }

    public Long getProgramaEducativoPk() {
        return programaEducativoPk;
    }

    public void setProgramaEducativoPk(Long programaEducativoPk) {
        this.programaEducativoPk = programaEducativoPk;
    }

    public Long getGradoPk() {
        return gradoPk;
    }

    public void setGradoPk(Long gradoPk) {
        this.gradoPk = gradoPk;
    }

    public String getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(String numTramite) {
        this.numTramite = numTramite;
    }

    public Integer getAnioLectivo() {
        return anioLectivo;
    }

    public void setAnioLectivo(Integer anioLectivo) {
        this.anioLectivo = anioLectivo;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public Long getPlanEstudioPk() {
        return planEstudioPk;
    }

    public void setPlanEstudioPk(Long planEstudioPk) {
        this.planEstudioPk = planEstudioPk;
    }

    public Boolean getPartidaNacimiento() {
        return partidaNacimiento;
    }

    public void setPartidaNacimiento(Boolean partidaNacimiento) {
        this.partidaNacimiento = partidaNacimiento;
    }

    public Integer getPartidaNacimientoAnio() {
        return partidaNacimientoAnio;
    }

    public void setPartidaNacimientoAnio(Integer partidaNacimientoAnio) {
        this.partidaNacimientoAnio = partidaNacimientoAnio;
    }

    public Long getPartidaNacimientoNumero() {
        return partidaNacimientoNumero;
    }

    public void setPartidaNacimientoNumero(Long partidaNacimientoNumero) {
        this.partidaNacimientoNumero = partidaNacimientoNumero;
    }

    public String getPartidaNacimientoFolio() {
        return partidaNacimientoFolio;
    }

    public void setPartidaNacimientoFolio(String partidaNacimientoFolio) {
        this.partidaNacimientoFolio = partidaNacimientoFolio;
    }

    public String getPartidaNacimientoLibro() {
        return partidaNacimientoLibro;
    }

    public void setPartidaNacimientoLibro(String partidaNacimientoLibro) {
        this.partidaNacimientoLibro = partidaNacimientoLibro;
    }

    public String getPartidaNacimientoComplemento() {
        return partidaNacimientoComplemento;
    }

    public void setPartidaNacimientoComplemento(String partidaNacimientoComplemento) {
        this.partidaNacimientoComplemento = partidaNacimientoComplemento;
    }

    public Long getPartidaNacimientoDepartamento() {
        return partidaNacimientoDepartamento;
    }

    public void setPartidaNacimientoDepartamento(Long partidaNacimientoDepartamento) {
        this.partidaNacimientoDepartamento = partidaNacimientoDepartamento;
    }

    public Long getPartidaNacimientoMunicipio() {
        return partidaNacimientoMunicipio;
    }

    public void setPartidaNacimientoMunicipio(Long partidaNacimientoMunicipio) {
        this.partidaNacimientoMunicipio = partidaNacimientoMunicipio;
    }

    public Long getPaisCarneResidentePk() {
        return paisCarneResidentePk;
    }

    public void setPaisCarneResidentePk(Long paisCarneResidentePk) {
        this.paisCarneResidentePk = paisCarneResidentePk;
    }



    
    


    
    
}
